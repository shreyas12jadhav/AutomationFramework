package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ReportManager {

	
	private static ExtentReports extent;
	
	public static ExtentReports getReportInstance() {
		
		if (extent == null) {
			String reportPath = System.getProperty("user.dir")+ "/reports/ExtentReport.html" ;
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			
			spark.config().setReportName("Automation Test Results");
            spark.config().setDocumentTitle("Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Browser", ConfigReader.get("browser"));
		}
	
		return extent;
		
	}
	
}
