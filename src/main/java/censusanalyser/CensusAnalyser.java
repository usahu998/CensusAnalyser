package censusanalyser;

import com.bridgelabz.csvbuilder.CSVBuilderException;
import com.bridgelabz.csvbuilder.CSVBuilderFactory;
import com.bridgelabz.csvbuilder.ICSVBuilder;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    Map<String, IndiaCensusDAO> censusStateMap;
    Comparator<IndiaCensusDAO> censusCSVComparator;
    Map<FieldType, Comparator<IndiaCensusDAO>> comparatorMap;


    public CensusAnalyser() {
        this.censusStateMap = new HashMap<>();
        this.comparatorMap = new HashMap<>();

        comparatorMap.put(FieldType.POPULATION, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.population));
        comparatorMap.put(FieldType.AREA, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.areaInSqKm));
        comparatorMap.put(FieldType.STATE, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.state));
        comparatorMap.put(FieldType.DENSITY, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.densityPerSqKm));
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaCensusCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> csvIterable = () -> csvFileIterator;
            StreamSupport.stream(csvIterable.spliterator(), false).
                    forEach(censusCsv -> censusStateMap.put(censusCsv.state, new IndiaCensusDAO(censusCsv)));
            return censusStateMap.size();
        } catch (IOException | CSVBuilderException | RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.NO_SUCH_FILE);
        }
    }

    public int loadIndianStateCode(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> StateCsvIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> StateCsvIterator;
            StreamSupport.stream(csvIterable.spliterator(), false)
                    .filter(csvState -> censusStateMap.get(csvState.StateName) != null)
                    .forEach(csvState -> censusStateMap.get(csvState.StateName).stateCode = csvState.StateCode);
            return this.censusStateMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.NO_SUCH_FILE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public String getStateWiseSortedCensusData(FieldType fieldName) {
        List<IndiaCensusDAO> indiaCensusList = new ArrayList<>(censusStateMap.values());
        censusCSVComparator = comparatorMap.get(fieldName);
        this.sort(indiaCensusList, censusCSVComparator);
        return new Gson().toJson(indiaCensusList);
    }

    private <T> void sort(List<T> object, Comparator<T> objectComparator) {
        for (int i = 0; i < object.size() - 1; i++) {
            for (int j = 0; j < object.size() - i - 1; j++) {
                T census1 = object.get(j);
                T census2 = object.get(j + 1);
                if (objectComparator.compare(census1, census2) > 0) {
                    object.set(j, census2);
                    object.set(j + 1, census1);
                }
            }
        }
    }

    public int loadUSCensusData(String usCensusDataFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(usCensusDataFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator<USCensusCSV> StateCsvIterator = csvBuilder.getCSVFileIterator(reader, USCensusCSV.class);
            Iterable<USCensusCSV> csvIterable = () -> StateCsvIterator;
            StreamSupport.stream(csvIterable.spliterator(), false).
                    forEach(censusCsv -> censusStateMap.put(censusCsv.state, new IndiaCensusDAO(censusCsv)));
            return this.censusStateMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.NO_SUCH_FILE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }
}
