package censusanalyser;

public class CensusDAO {
    public int densityPerSqKm;
    public String state;
    public String stateCode;
    public double areaInSqKm;
    public int population;
    public double populationDensity;

    public CensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        areaInSqKm = indiaCensusCSV.areaInSqKm;
        populationDensity = indiaCensusCSV.populationDensity;
        population = indiaCensusCSV.population;
    }

    public CensusDAO(USCensusCSV censusCSV){
        state=censusCSV.state;
        stateCode=censusCSV.stateId;
        areaInSqKm=censusCSV.totalArea;
        population=censusCSV.population;
        populationDensity=censusCSV.populationDensity;
    }
}
