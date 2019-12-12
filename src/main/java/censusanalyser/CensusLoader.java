package censusanalyser;

import com.bridgelabz.csvbuilder.CSVBuilderException;
import com.bridgelabz.csvbuilder.CSVBuilderFactory;
import com.bridgelabz.csvbuilder.ICSVBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CensusLoader {

    public Map<String, IndiaCensusDAO> loadCensusData(CensusAnalyser.County country, String... csvFilePath) throws CensusAnalyserException {
        if(country.equals(CensusAnalyser.County.INDIA))
            return this.loadCensusData(IndiaCensusCSV.class,csvFilePath);
        else if(country.equals(CensusAnalyser.County.US))
            return this.loadCensusData(USCensusCSV.class,csvFilePath);
        else throw new CensusAnalyserException("Incorrect Country",CensusAnalyserException.ExceptionType.INVALID_COUNTRY);
    }

    public <E> Map<String, IndiaCensusDAO> loadCensusData(Class<E> censusCSVClass, String... csvFilePath) throws CensusAnalyserException {
        Map<String, IndiaCensusDAO> censusStateMap= new HashMap<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<E> csvIterator = csvBuilder.getCSVFileIterator(reader, censusCSVClass);
            Iterable<E> csvIterable = () -> csvIterator;
            if (censusCSVClass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCsv -> censusStateMap.put(censusCsv.state, new IndiaCensusDAO(censusCsv)));
            }
            if (censusCSVClass.getName().equals("censusanalyser.USCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(censusCsv -> censusStateMap.put(censusCsv.state, new IndiaCensusDAO(censusCsv)));
            }
            if(csvFilePath.length == 1) return censusStateMap;
            this.loadIndianStateCode(censusStateMap,csvFilePath[1]);
            return censusStateMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.NO_SUCH_FILE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianStateCode(Map<String, IndiaCensusDAO> censusCSVClass, String... csvFilePath) throws CensusAnalyserException {
        Map<String, IndiaCensusDAO> censusStateMap= new HashMap<>() ;
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> StateCsvIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> StateCsvIterator;
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .filter(csvState -> censusStateMap.get(csvState.StateName) != null)
                    .forEach(csvState -> censusStateMap.get(csvState.StateName).stateCode = csvState.StateCode);
            return censusStateMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.NO_SUCH_FILE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
}
