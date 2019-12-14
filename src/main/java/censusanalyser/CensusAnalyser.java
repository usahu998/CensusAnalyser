package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

public class CensusAnalyser {
    public enum Country {INDIA, US}

    private Country country;

    Map<String, CensusDAO> censusStateMap = null;
    Comparator<CensusDAO> censusCSVComparator;
    Map<FieldType, Comparator<CensusDAO>> comparatorMap;

    public CensusAnalyser(Country country) {
        this.country = country;
        this.censusStateMap = new HashMap<>();
        this.comparatorMap = new HashMap<>();

        comparatorMap.put(FieldType.POPULATION, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.population));
        comparatorMap.put(FieldType.AREA, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.totalArea));
        comparatorMap.put(FieldType.STATE, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.state));
        comparatorMap.put(FieldType.DENSITY, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.densityPerSqKm));
    }

    public int loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        CensusAdapter censusAdapter = CensusAdapterFactory.getCensusData(country);
        censusStateMap = censusAdapter.loadCensusData(Country.INDIA, csvFilePath);
        return censusStateMap.size();
    }

    public String getStateWiseSortedCensusData(FieldType fieldName) {
        Comparator<CensusDAO> censusComparator = comparatorMap.get(fieldName);
        ArrayList censusDTO = censusStateMap.values().stream()
                .sorted(censusComparator)
                .map(censusDto -> censusDto.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        return new Gson().toJson(censusDTO);
    }

    public String getDualIndiaAndUSMostPopulationStateWithDensity(FieldType fieldName, FieldType fieldName1) throws CensusAnalyserException {
        if (censusStateMap == null || censusStateMap.size() == 0) {
            throw new CensusAnalyserException("No Census data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator = comparatorMap.get(fieldName).thenComparing(comparatorMap.get(fieldName1));
        ArrayList censusDAOS = censusStateMap.values().stream().
                sorted(censusCSVComparator).
                map(censusDAO -> censusDAO.getCensusDTO(country)).
                collect(Collectors.toCollection(ArrayList::new));
        String sortedStateCensusJson = new Gson().toJson(censusDAOS);
        return sortedStateCensusJson;
    }
}
