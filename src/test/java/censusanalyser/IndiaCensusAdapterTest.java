package censusanalyser;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class IndiaCensusAdapterTest {
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String INDIA_STATE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            IndiaCensusAdapter censusAnalyser = new IndiaCensusAdapter();
            Map<String, CensusDAO> stringCensusCount = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CSV_FILE_PATH);
            Assert.assertEquals(29, stringCensusCount.size());
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void givenIndianCensusCSVFile_ThrowsExceptionWhen_WrongFilePathIsSent() {
        try {
            IndiaCensusAdapter censusAnalyser = new IndiaCensusAdapter();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_SUCH_FILE, e.type);
        }
    }

    @Test
    public void givenIndianCensusCSVFile_WhenStateCodeSentFirst_ThrowsException() {
        try {
            IndiaCensusAdapter censusAnalyser = new IndiaCensusAdapter();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_STATE_CSV_FILE_PATH, INDIA_CENSUS_CSV_FILE_PATH);
        } catch (CensusAnalyserException | RuntimeException e) {
            Assert.assertEquals("Error capturing CSV header!", e.getMessage());
        }
    }

    @Test
    public void givenIndianCensusCSVFile_WhenEmptyFilePathSent_ThrowsException() {
        try {
            IndiaCensusAdapter censusAnalyser = new IndiaCensusAdapter();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, "");
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CSV_HEADER_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianCensusCSVFile_WhenWrongFile_ThrowsException() {
        try {
            IndiaCensusAdapter censusAnalyser = new IndiaCensusAdapter();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_SUCH_FILE, e.type);
        }
    }
}
