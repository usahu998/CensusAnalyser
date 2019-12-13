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
    private static final String US_CENSUS_DATA_FILE_PATH = "./src/test/resources/USCensusData.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIAN_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException ignored) {
        }
    }

    @Test
    public void givenIndianCensusCSVFileReturnsIncorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIAN_STATE_CODE_CSV_FILE_PATH);
            Assert.assertNotEquals(37, numOfRecords);
        } catch (CensusAnalyserException ignored) {
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_CSV_FILE_PATH, INDIAN_STATE_CODE_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_SUCH_FILE, e.type);
        }
    }

    @Test
    public void givenIndiaCensusData_WithNoSuchHeader_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CSVBuilderException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, WRONG_FILE_TYPE_PATH, INDIAN_STATE_CODE_CSV_FILE_PATH);
        } catch (RuntimeException | CensusAnalyserException e) {
            Assert.assertEquals("Error capturing CSV header!", e.getMessage());
        }
    }

    @Test
    public void givenIndianStateCodeCSVFileReturnsCorrectRecords() {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        int value = 0;
        try {
            value = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIAN_STATE_CODE_CSV_FILE_PATH);
        } catch (CensusAnalyserException ignored) {

        }
        Assert.assertEquals(29, value);
    }

    @Test
    public void giveIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIAN_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusDate = censusAnalyser.getStateWiseSortedCensusData(FieldType.STATE);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusDate, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", censusCSV[0].state);
        } catch (CensusAnalyserException ignored) {
        }
    }

    @Test
    public void giveIndianCensusData_WhenNotSortedOnState_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIAN_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusDate = censusAnalyser.getStateWiseSortedCensusData(FieldType.STATE);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusDate, IndiaCensusCSV[].class);
            Assert.assertNotEquals("Andhra Pradesh", censusCSV[1].state);
        } catch (CensusAnalyserException ignored) {
        }
    }

    @Test
    public void giveIndianCensusData_WhenSortedOnPopulation_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIAN_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusDate = censusAnalyser.getStateWiseSortedCensusData(FieldType.POPULATION);
            IndiaCensusCSV[] censusCSV = new Gson().fromJson(sortedCensusDate, IndiaCensusCSV[].class);
            Assert.assertEquals(607688, censusCSV[0].population);
        } catch (CensusAnalyserException ignored) {
        }
    }

    @Test
    public void givenUSCensusData_ShouldReturnCorrectRecord() throws CensusAnalyserException {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.US);
        int usCensusCount = censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_DATA_FILE_PATH);
        Assert.assertEquals(51, usCensusCount);
    }

    @Test
    public void givenLoadCensusData_ForIndia_ShouldReturnNumberOfRecords() throws CensusAnalyserException {
        CensusAnalyser censusAnalyser = new CensusAnalyser(CensusAnalyser.Country.INDIA);
        int value = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIAN_STATE_CODE_CSV_FILE_PATH);
        Assert.assertEquals(29, value);
    }
}

