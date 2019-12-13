package censusanalyser;

public class CensusAnalyserException extends Exception {

    public enum ExceptionType {
        CENSUS_FILE_PROBLEM, UNABLE_TO_PARSE, NO_CENSUS_DATA, NULL_DATA_FOUND, NO_SUCH_HEADER, NO_SUCH_FILE, NO_SUCH_FIELD, INVALID_COUNTRY, CSV_HEADER_PROBLEM
    }

    public ExceptionType type;

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
