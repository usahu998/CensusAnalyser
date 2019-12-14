package censusanalyser;

public class CensusDAO {
    public double densityPerSqKm;
    public String state;
    public String stateCode;
    public double totalArea;
    public int population;
    public double populationDensity;

    public CensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        totalArea = indiaCensusCSV.areaInSqKm;
        populationDensity = indiaCensusCSV.densityPerSqKm;
        population = indiaCensusCSV.population;
    }

    public CensusDAO(USCensusCSV usCensusCSV) {
        state = usCensusCSV.state;
        stateCode = usCensusCSV.stateId;
        totalArea = usCensusCSV.totalArea;
        population = usCensusCSV.population;
        populationDensity = usCensusCSV.populationDensity;
    }

    public Object getCensusDTO(CensusAnalyser.Country country) {
        if (country.equals(CensusAnalyser.Country.INDIA))
            return new IndiaCensusCSV(state, population, totalArea, populationDensity);
        return new USCensusCSV(state, stateCode, population, totalArea, populationDensity);
    }
}
