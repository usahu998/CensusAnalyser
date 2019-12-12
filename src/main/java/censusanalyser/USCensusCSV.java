package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class USCensusCSV {

   // State Id,State,Population,Housing units,Total area,Water area,Land area,Population Density,Housing Density
@CsvBindByName(column = "State",required = true)
    public String state;
    @CsvBindByName(column = "State Id",required = true)
    public String stateId;
    @CsvBindByName(column = "Population",required = true)
    public int population;
    @CsvBindByName(column = "Total area",required = true)
    public double totalArea;
    @CsvBindByName(column = "Population Density",required = true)
    public double populationDensity;
}
