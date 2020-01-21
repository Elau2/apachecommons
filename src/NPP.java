import java.io.IOException;


import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.apache.commons.csv.*;
import org.apache.commons.math3.stat.StatUtils;
import org.knowm.xchart.*;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

public class NPP implements ExampleChart<XYChart>{
	
	//CSV File
	private static final String filepath = "./Congress_White_House.csv";
	
	//Data set Graphed
    private static List<Integer> dataset = new ArrayList<Integer>();
    
    /**
     * @author Ethan Lau, Siri Phaneendra
     * @version 1.0
     * 
     * getChart - Sets up chart layout
     */
    public XYChart getChart() {
    
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).build();
     
        // Customize Chart
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setLegendPosition(LegendPosition.InsideSW);
        chart.getStyler().setMarkerSize(16);
     
        // Series
        List<Double> xData = new LinkedList<Double>();
        List<Double> yData = new LinkedList<Double>();
        
        double[] datasetArray = new double[dataset.size()]; 
        
        // ArrayList to Array Conversion 
        for (int i =0; i < dataset.size(); i++) 
            datasetArray[i] = dataset.get(i); 
        
        //Use of Apache Commons Math Classes
        double variance = StatUtils.populationVariance(datasetArray);
        double sd = Math.sqrt(variance);
        double mean = StatUtils.mean(datasetArray);
        
        for (double i = 0; i < dataset.size(); i++) {
        	xData.add((i + 1.00 - 0.375) / (((double)dataset.size()) + 0.25));
        	yData.add(((double)dataset.get((int)i)-mean)/sd);
        }
        
        chart.addSeries("NPP", xData, yData);
        
        return chart;
    }
    
    /**
     * @author - Ethan Lau, Siri Phaneendra
     * @version 1.0
     * @param args
     * @throws IOException
     * 
     * main - method called when run, reads data and processes data
     */
    public static void main(String[] args) throws IOException{
    	
    	try (
                Reader reader = Files.newBufferedReader(Paths.get(filepath));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
            ) {
                for (CSVRecord csvRecord : csvParser) {
                    // Accessing Values by Column Index
                	
                    String salary = csvRecord.get(2); //Gets 3rd column of the CSV file
                    if (salary.contains(".")) { //Tests if the value is a salary value
                    	try {
                    		int salaryint = Integer.parseInt(salary.split("\\.")[0]); //converts double to int
                        
                    		dataset.add(salaryint); //adds integer value to dataset list
                    	}
                    	catch (NumberFormatException nfe){ 
                    		
                    	}
                    }
                }
            }
    	
    	Collections.sort(dataset); //sorts data by increasing value
    	
    	//Initializes graph output
    	ExampleChart<XYChart> exampleChart = new NPP();
        XYChart chart = exampleChart.getChart();
        new SwingWrapper<XYChart>(chart).displayChart();
    


    }

}

/*
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

*/
