package com.dtc.deltasoft.logging.logback;

import java.io.IOException;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Specialisation of {@link PatternLayoutEncoder} to support using a different layout pattern if
 * the {@link ILoggingEvent} is received from the {@code JDBC} logger.
 * 
 */
public class PatternLayoutEncoder extends ch.qos.logback.classic.encoder.PatternLayoutEncoder {

	/**********************************************************************************************/
    /*** Constants                                                                              ***/
    /**********************************************************************************************/

    /**********************************************************************************************/
    /*** Configuration Variables                                                                ***/
    /**********************************************************************************************/

    /**********************************************************************************************/
    /*** Inner Classes                                                                          ***/
    /**********************************************************************************************/

    /**********************************************************************************************/
    /*** Class Fields                                                                           ***/
    /**********************************************************************************************/

    /**********************************************************************************************/
    /*** JavaBeans Simple Properties                                                            ***/
    /**********************************************************************************************/

    String jdbcPattern;

    /**********************************************************************************************/
    /*** JavaBeans Bound Properties                                                             ***/
    /**********************************************************************************************/

    /**********************************************************************************************/
    /*** JavaBeans Constrained Properties                                                       ***/
    /**********************************************************************************************/

    /**********************************************************************************************/
    /*** JavaBeans Indexed Properties                                                           ***/
    /**********************************************************************************************/

    /**********************************************************************************************/
    /*** Private Fields                                                                         ***/
    /**********************************************************************************************/

    private PatternLayout _oDfltPatternLayout;
    private PatternLayout _oJdbcPatternLayout;

	/**********************************************************************************************/
	/*** Event Notification Support                                                             ***/
	/**********************************************************************************************/

	/**********************************************************************************************/
	/*** Miscellaneous Events                                                                   ***/
	/**********************************************************************************************/

    /**********************************************************************************************/
    /*** Class Methods                                                                          ***/
    /**********************************************************************************************/

	/**
	 * Static initialiser.
	 * 
	 */
	static {
	}

    /**********************************************************************************************/
    /*** Instance Initialisers and Constructors                                                 ***/
    /**********************************************************************************************/

    /**
     * Instance initialiser.
     * 
     */
    {
    }

	/**
	 * Default constructor.
	 * 
	 */
	private PatternLayoutEncoder() {
	    super();
	}

    /**********************************************************************************************/
    /*** Standard Object Method Overrides                                                       ***/
    /**********************************************************************************************/

    /**********************************************************************************************/
    /*** Implemented Interface Methods                                                          ***/
    /**********************************************************************************************/

    /**********************************************************************************************/
    /*** JavaBeans Getter/Setter Methods                                                        ***/
    /**********************************************************************************************/
    
	/**
	 * Returns the {@link #jdbcPattern JdbcPattern} JavaBean as a {@link String}.
	 * 
	 * @return
     * The {@link #jdbcPattern JdbcPattern} JavaBean as a {@link String}.
     * 
	 */
    public String getJdbcPattern() {
      return jdbcPattern;
    }

    /**
     * Sets the {@link #jdbcPattern JdbcPattern} JavaBean from a {@link String}.
     * 
     * @param jdbcPattern
     * A {@link String} specifying the new {@link #jdbcPattern JdbcPattern} JavaBean value.
     * 
     */
    public void setJdbcPattern(String jdbcPattern) {
      this.jdbcPattern = jdbcPattern;
    }

    /**********************************************************************************************/
    /*** Miscellaneous Instance Methods                                                         ***/
    /**********************************************************************************************/

	/**
	 * 
	 */
    @Override
    public void start() {
        _oJdbcPatternLayout = new PatternLayout();
        _oJdbcPatternLayout.setContext(context);
        _oJdbcPatternLayout.setPattern(getJdbcPattern());
        _oJdbcPatternLayout.setOutputPatternAsHeader(outputPatternAsHeader);
        _oJdbcPatternLayout.start();
        super.start();

        _oDfltPatternLayout = (PatternLayout)this.layout;
    }
    
	@Override
	public void doEncode(ILoggingEvent oLoggingEvent) throws IOException {
	    if (oLoggingEvent.getLoggerName().equals("JDBC")) {
	        this.layout = _oJdbcPatternLayout;
	    } else {
	        this.layout = _oDfltPatternLayout;
	    }
	    
	    super.doEncode(oLoggingEvent);
	}
}
