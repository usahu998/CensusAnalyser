package censusanalyser;

import java.util.Map;

public class CensusAdapterFactory {
    public static CensusAdapter getCensusData(CensusAnalyser.County country, String... csvFilePath) throws CensusAnalyserException {
        if (country.equals(CensusAnalyser.County.INDIA))
            return new IndiaCensusAdapter();
        if (country.equals(CensusAnalyser.County.US))
            return new IndiaCensusAdapter();
        return null;
    }
}
