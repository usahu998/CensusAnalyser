package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try ( Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))){
            Iterator<IndiaCensusCSV> csvIterable =new  OpenCsvBuilder().getCSVFileIterator(reader,IndiaCensusCSV.class);
            return getcount(csvIterable);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    public int loadIndianStateCode(String csvFilePath) throws CensusAnalyserException {
        try ( Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))){
            Iterator<IndiaStateCodeCSV> csvIterable = new  OpenCsvBuilder().getCSVFileIterator(reader,IndiaStateCodeCSV.class);
            return getcount(csvIterable);
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        }
    }

    private  <E> int getcount(Iterator<E> iterator){
        Iterable<E> csvIterable =() -> iterator;
        int numOfEateries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return numOfEateries;
    }
}
