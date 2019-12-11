package censusanalyser;

public class IndiaCensusDAO {
    public int densityPerSqKm;
    public String state;
    public String stateCode;
    public int areaInSqKm;
    public int population;

    public IndiaCensusDAO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        areaInSqKm = indiaCensusCSV.areaInSqKm;
        densityPerSqKm = indiaCensusCSV.densityPerSqKm;
        population = indiaCensusCSV.population;
    }
}
