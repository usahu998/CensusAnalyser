package censusanalyser;

import java.util.Map;

public class USCensusAdapter extends CensusAdapter {

    @Override
    public Map<String, CensusDAO> loadCensusData(CensusAnalyser.Country india, String... csvFilePath) throws CensusAnalyserException {
        return super.loadCensusData(USCensusCSV.class, csvFilePath);
    }
}
