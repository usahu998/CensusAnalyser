package censusanalyser;

import com.bridgelabz.csvbuilder.CSVBuilderException;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CensusAnalyserTest {

    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIAN_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_FILE_TYPE_PATH = "/home/admin265/Downloads/CensusAnalyser/CensusAnalyser/gradlew.bat";
    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenIndianCensusCSVFileReturnsIncorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertNotEquals(37, numOfRecords);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_SUCH_FILE, e.type);
        }
    }

    @Test
    public void givenIndianStateCodeCSVFileReturnsCorrectRecords() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int value = 0;
        try {
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);

            value = censusAnalyser.loadIndianStateCode(INDIAN_STATE_CODE_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {

        }
        Assert.assertEquals(37, value);
    }

    @Test
    public void givenIndianStateCodeCSVFileReturnsIncorrectRecords() {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        int value = 0;
        try {
            value = censusAnalyser.loadIndianStateCode(INDIAN_STATE_CODE_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {

        }
        Assert.assertNotEquals(39, value);
    }

    @Test
    public void giveIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusDate = censusAnalyser.getStateWiseSortedCensusData();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusDate, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void giveIndianCensusData_WhenNotSortedOnState_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadIndiaCensusData(INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusDate = censusAnalyser.getStateWiseSortedCensusData();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusDate, IndiaCensusCSV[].class);
            Assert.assertNotEquals("Andhra Pradesh", censusCSV[1].state);
        } catch (CensusAnalyserException e) {
        }
    }

    @Test
    public void giveIndianCensusData_WhenCSVFileIsNull_ShouldReturnException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            String sortedCensusDate = censusAnalyser.getStateWiseSortedCensusData();
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusDate, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFileType_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadIndiaCensusData(WRONG_FILE_TYPE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_SUCH_FILE, e.type);
        }
    }
}

