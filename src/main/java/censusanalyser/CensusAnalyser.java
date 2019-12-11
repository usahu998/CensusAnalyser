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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    Map<String, IndiaCensusDAO> censusStateMap = null;
    Comparator<IndiaCensusDAO> censusCSVComparator = null;
    Map<FieldType, Comparator> comparatorMap = null;


    public CensusAnalyser() {
        this.censusStateMap = new HashMap<String, IndiaCensusDAO>();
        this.comparatorMap = new HashMap<>();

        comparatorMap.put(FieldType.POPULATION, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.population));
        comparatorMap.put(FieldType.AREA, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.areaInSqKm));
        comparatorMap.put(FieldType.STATE, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.state));
        comparatorMap.put(FieldType.DENSITY, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.densityPerSqKm));
    }

    // public CensusAnalyser() {
    //     this.censusStateMap = new HashMap<>();
//    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CSVBuilderFactory.createCSVBuilder();
            Iterator csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
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
            Iterator StateCsvIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            int count = 0;
            while (StateCsvIterator.hasNext()) {
                count++;
                IndiaStateCodeCSV stateCSV = (IndiaStateCodeCSV) StateCsvIterator.next();
                IndiaCensusDAO censusDAO = censusStateMap.get(stateCSV.StateName);
                if (censusDAO == null) continue;
                censusDAO.stateCode = stateCSV.StateCode;
            }
            return count;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.NO_SUCH_FILE);
        } catch (CSVBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public String getStateWiseSortedCensusData(FieldType fieldName) throws CensusAnalyserException {
        List<IndiaCensusDAO> indiaCensusList = censusStateMap.values().stream().collect(Collectors.toList());
        censusCSVComparator = comparatorMap.get(fieldName);
      this.sort(indiaCensusList, censusCSVComparator);
        String sorted = new Gson().toJson(indiaCensusList);
        return sorted;
    }

    private <T> List<IndiaCensusDAO> sort(List<T> object, Comparator<T> objectComparator) {
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
        return (List<IndiaCensusDAO>) object;
    }
}
