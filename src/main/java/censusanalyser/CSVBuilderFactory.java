package censusanalyser;

public class CSVBuilderFactory {
    public static ICSVBuilder createCSVBuilder() {
        return new OpenCsvBuilder();
    }
}
