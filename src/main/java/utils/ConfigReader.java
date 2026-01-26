package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

//    private static Properties prop;
//
//    public static void loadProperties() {
//        try {
//            prop = new Properties();
//            FileInputStream fis = new FileInputStream("C:/Users/shreyas/eclipse-workspace/AutomationFramework/resources/config.properties");
//            prop.load(fis);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String get(String key) {
//        return prop.getProperty(key);
//    }
	
	private static Properties prop = new Properties();

  static {
      try {
    	  InputStream input = ConfigReader.class
                  .getClassLoader()
                  .getResourceAsStream("config.properties");

          if (input == null) {
              throw new RuntimeException("❌ config.properties not found in resources folder");
          }

          prop.load(input);

      } catch (Exception e) {
          throw new RuntimeException("❌ Failed to load config.properties", e);
      }
  }

  public static String get(String key) {
      return prop.getProperty(key);
  }
 }
	
	


	
