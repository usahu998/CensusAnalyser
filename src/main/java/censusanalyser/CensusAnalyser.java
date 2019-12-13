package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

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
        censusStateMap = censusAdapter.loadCensusData(csvFilePath);
        return censusStateMap.size();
    }

    public String getStateWiseSortedCensusData(FieldType fieldName) {
        Comparator<CensusDAO> censusComparator = comparatorMap.get(fieldName);
        ArrayList censusDTO = censusStateMap.values().stream().sorted(censusComparator).map(censusDto -> censusDto.getCensusDTO(country))
                .collect(Collectors.toCollection(ArrayList::new));
        return new Gson().toJson(censusDTO);
    }
}
