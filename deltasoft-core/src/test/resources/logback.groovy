/**************************************************************************************************/
/***   DEFAULT LOGBACK CONFIGURATION                                                            ***/
/**************************************************************************************************/

import ch.qos.logback.core.status.OnConsoleStatusListener
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.filter.EvaluatorFilter
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.classic.boolex.JaninoEventEvaluator
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.LevelFilter
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.classic.net.SocketAppender
import ch.qos.logback.classic.turbo.MDCFilter

import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.WARN
import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.TRACE
import static ch.qos.logback.core.spi.FilterReply.ACCEPT
import static ch.qos.logback.core.spi.FilterReply.DENY

statusListener(OnConsoleStatusListener)

def bDeveloper = true
def bWindows = System.getProperty("os.name").toLowerCase().contains("windows")

def strDefaultEncoderPattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
def strJdbcEncoderPattern = "%d{HH:mm:ss.SSS} [%thread] %-6marker[%X{ConnectId}]\\(%X{ExecTime}ms\\) %msg%n"

def oLogfileAppender = ch.qos.logback.core.rolling.RollingFileAppender
def strLogFileName = "/var/log/sportzman/sportzman" 

def lstRootAppenders = ["LOGFILE"]

if (bDeveloper) {
    scan("15 seconds")
    lstRootAppenders.add("BEAGLE")
    oLogfileAppender = ch.qos.logback.core.ConsoleAppender
}

turboFilter(MDCFilter) {
  MDCKey = "Context"
  value = "SYNTHETIC_KEY"
  onMatch = DENY
}

turboFilter(MDCFilter) {
  MDCKey = "asModel"
  value = "AuditQuery"
  onMatch = DENY
}

turboFilter(MDCFilter) {
  MDCKey = "asModel"
  value = "AuditDetail"
  onMatch = DENY
}

turboFilter(MDCFilter) {
  MDCKey = "asModel"
  value = "Suburb"
  onMatch = DENY
}

turboFilter(MDCFilter) {
  MDCKey = "asModel"
  value = "Address"
  onMatch = DENY
}

appender("LOGFILE", oLogfileAppender) {
    if (oLogfileAppender.name.contains("RollingFileAppender")) {
        file = "${strLogFileName}.log"
        rollingPolicy(TimeBasedRollingPolicy) {
            fileNamePattern = "${strLogFileName}_%d{yyyy-MM-dd}.log"
        }
    }
    filter(EvaluatorFilter) {
        evaluator(JaninoEventEvaluator) {
            expression = 'return marker == null;'
        }
        onMatch = ACCEPT
    }
    filter(EvaluatorFilter) {
        evaluator("commit", JaninoEventEvaluator) {
            expression = 'return message.contains("COMMIT");'
        }
        onMatch = DENY
    }
    filter(EvaluatorFilter) {
        evaluator("audit", JaninoEventEvaluator) {
            expression = 'return message.contains("AUDIT_");'
        }
        onMatch = DENY
    }
    filter(EvaluatorFilter) {
        evaluator("update", JaninoEventEvaluator) {
            expression = 'return marker.getName().equals("UPDATE");'
        }
        onMatch = DENY
    }
    filter(EvaluatorFilter) {
        evaluator("query", JaninoEventEvaluator) {
            expression = 'return marker.getName().equals("QUERY");'
        }
        onMatch = DENY
        onMismatch = DENY
    }
    encoder(com.dtc.deltasoft.logging.logback.PatternLayoutEncoder) {
        pattern = "${strDefaultEncoderPattern}"
        jdbcPattern = "${strJdbcEncoderPattern}"
    }
}

appender("STDOUT", ConsoleAppender) {
    target = "System.out"
    filter(LevelFilter) {
        level = WARN
        onMatch = DENY
    }
    filter(LevelFilter) {
        level = ERROR
        onMatch = DENY
    }
    encoder(PatternLayoutEncoder) {
        pattern = "${strDefaultEncoderPattern}"
    }
}

appender("STDERR", ConsoleAppender) {
    target = "System.err"
    filter(ThresholdFilter) {
        level = WARN
    }
    encoder(PatternLayoutEncoder) {
        pattern = "${strDefaultEncoderPattern}"
    }
}

if (bDeveloper) {
    appender("BEAGLE", SocketAppender) {
        remoteHost = "localhost"
        port = 4321
        includeCallerData = true
    }
}

root(INFO, lstRootAppenders)
logger("com.dtc", TRACE)
logger("jdbc", ERROR)
logger("jdbc.sqltiming", DEBUG)
logger("com.googlecode.mapperdao", INFO)
logger("scala.slick.session", INFO)
logger("org.hibernate", INFO)
