@rem ==========================================================================
@rem Processes an SQL script developed for AquaDataStudio and runs it against a
@rem PostgreSQL database using psql.
@rem ==========================================================================

@echo off
setlocal
set SCRIPT_PATH=%~dp0

sed -r -f %SCRIPT_PATH%ads2psql.sed %1 | psql -e %2 %3 %4 %5 %6 %7 %8 %9 

