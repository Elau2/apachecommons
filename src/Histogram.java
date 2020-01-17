import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.apache.commons.csv.*;
import org.apache.commons.math3.stat.Frequency;
import org.knowm.xchart.*;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.internal.*;

public class Histogram {
	
	private static final String filepath = "./Congress_White_House.csv";
    private Map distributionMap;
    private int classWidth;

    public Histogram(List<Integer> dataset) {

        distributionMap = new TreeMap();
        classWidth = 10;
        Map distributionMap = processRawData(dataset);
        List yData = new ArrayList();
        yData.addAll(distributionMap.values());
        List xData = Arrays.asList(distributionMap.keySet().toArray());

        CategoryChart chart = buildChart(xData, yData);
        new SwingWrapper<>(chart).displayChart();

    }

    private CategoryChart buildChart(List xData, List yData) {

        // Create Chart
        CategoryChart chart = new CategoryChartBuilder().width(800).height(600)
                .title("White House Salaries")
                .xAxisTitle("Salary")
                .yAxisTitle("Frequency")
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setAvailableSpaceFill(0.99);
        chart.getStyler().setOverlapped(false);

        chart.addSeries("Salary", xData, yData);

        return chart;
    }

    private Map processRawData(List<Integer> datasetList) {
    	/*
        List<Integer> datasetList = Arrays.asList(
          36, 25, 38, 46, 55, 68, 72,
          55, 36, 38, 67, 45, 22, 48,
          91, 46, 52, 61, 58, 55);
        */
        Frequency frequency = new Frequency();
        datasetList.forEach(d -> frequency.addValue(Double.parseDouble(d.toString())));

        datasetList.stream()
          .map(d -> Double.parseDouble(d.toString()))
          .distinct()
          .forEach(observation -> {
             long observationFrequency = frequency.getCount(observation);
             int upperBoundary = (observation > classWidth)
               ? Math.multiplyExact( (int) Math.ceil(observation / classWidth), classWidth)
               : classWidth;
             int lowerBoundary = (upperBoundary > classWidth)
               ? Math.subtractExact(upperBoundary, classWidth)
               : 0;
             String bin = lowerBoundary + "-" + upperBoundary;

             updateDistributionMap(lowerBoundary, bin, observationFrequency);

          });

        return distributionMap;
    }

    private void updateDistributionMap(int lowerBoundary, String bin, long observationFrequency) {

        int prevLowerBoundary = (lowerBoundary > classWidth) ? lowerBoundary - classWidth : 0;
        String prevBin = prevLowerBoundary + "-" + lowerBoundary;
        if(!distributionMap.containsKey(prevBin))
            distributionMap.put(prevBin, 0);

        if(!distributionMap.containsKey(bin)) {
            distributionMap.put(bin, observationFrequency);
        }
        else {
            long oldFrequency = Long.parseLong(distributionMap.get(bin).toString());
            distributionMap.replace(bin, oldFrequency + observationFrequency);
        }
    }
    

    public static void main(String[] args) throws IOException{
    	List<Integer> dataset = new ArrayList<Integer>();
    	try (
                Reader reader = Files.newBufferedReader(Paths.get(filepath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            ) {
                for (CSVRecord csvRecord : csvParser) {
                    // Accessing Values by Column Index
                	
                    String salary = csvRecord.get(2);
                    if (salary.contains(".")) {
                    	try {
                    		int salaryint = Integer.parseInt(salary.split("\\.")[0]);
                        
                    		dataset.add(salaryint/1000);
                    	}
                    	catch (NumberFormatException nfe){
                    		
                    	}
                    }
                }
            }
    	
        new Histogram(dataset);
    }

}

/**
public class test {
	private static int classWidth;
	
	public static void main(String[] args) {
		System.out.println("Hi");
		List<Integer> data = Arrays.asList(36, 25, 38, 46, 55, 68, 72, 55, 36, 38, 67, 45, 22, 48, 91, 46, 52, 61, 58, 55);
				Frequency freq = new Frequency();
				data.forEach(d -> freq.addValue(Double.parseDouble(d.toString())));
		data.stream()
				  .map(d -> Double.parseDouble(d.toString()))
				  .distinct()
				  .forEach(observation -> {
				      long observationFrequency = freq.getCount(observation);
				      int upperBoundary = (observation > classWidth)
				        ? Math.multiplyExact( (int) Math.ceil(observation / classWidth), classWidth)
				        : classWidth;
				      int lowerBoundary = (upperBoundary > classWidth)
				        ? Math.subtractExact(upperBoundary, classWidth)
				        : 0;
				      String bin = lowerBoundary + "-" + upperBoundary;
				 
				      updateDistributionMap(lowerBoundary, bin, observationFrequency);
				  });
	}

}

//https://www.baeldung.com/apache-commons-frequency
**/