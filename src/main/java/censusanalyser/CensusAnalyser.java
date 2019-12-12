package censusanalyser;

import com.google.gson.Gson;

import java.util.*;

public class CensusAnalyser {
    public enum County {INDIA, US}

    Map<String, CensusDAO> censusStateMap;
    Comparator<CensusDAO> censusCSVComparator;
    Map<FieldType, Comparator<CensusDAO>> comparatorMap;

    public CensusAnalyser() {
        this.censusStateMap = new HashMap<>();
        this.comparatorMap = new HashMap<>();

        comparatorMap.put(FieldType.POPULATION, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.population));
        comparatorMap.put(FieldType.AREA, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.areaInSqKm));
        comparatorMap.put(FieldType.STATE, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.state));
        comparatorMap.put(FieldType.DENSITY, censusCSVComparator = Comparator.comparing(indiaCensusCSV -> indiaCensusCSV.densityPerSqKm));
    }

    public int loadCensusData(County country, String... csvFilePath) throws CensusAnalyserException {
        CensusAdapter censusAdapter = CensusAdapterFactory.getCensusData(country, csvFilePath);
        censusStateMap = censusAdapter.loadCensusData(csvFilePath);
        return censusStateMap.size();
    }

    public int loadUSCensusData(String... csvFilePath) throws CensusAnalyserException {
        censusStateMap = new IndiaCensusAdapter().loadCensusData(USCensusCSV.class, csvFilePath);
        return censusStateMap.size();
    }


    public String getStateWiseSortedCensusData(FieldType fieldName) {
        List<CensusDAO> indiaCensusList = new ArrayList<>(censusStateMap.values());
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
}
