package middleware;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author tfc130
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class DataFileLoader {
	private static String dataFilePath;

	// Delimitador do ficheiro.
	private static String fieldSeparator;

	public static final String CONFIG_FILE = "/middleware/middleWare.properties";
	
	public DataFileLoader() throws IOException{
		Properties properties = new Properties();
//		System.out.println(DataFileLoader.CONFIG_FILE);
		
		InputStream input =getClass().getResourceAsStream(DataFileLoader.CONFIG_FILE); 
		properties.load(input);
		String className = getClass().getName();
		fieldSeparator = properties.getProperty(className+".fieldSeparator");
		dataFilePath = properties.getProperty(className+".dataFilePath");
//		System.out.println(getClass().getName());
//		System.out.println("Field Separator:("+fieldSeparator+")");
//		
//		System.out.print("Data File Path:("+dataFilePath+")");
		if (dataFilePath != null && dataFilePath.startsWith("\\")) {
			System.out.print("(\\"+dataFilePath+")");
		}
		else{
//			System.out.print("("+dataFilePath+")");			
		}
	}
	/**
	 * Returns the dataFilePath.
	 * @return String
	 */
	public static String getDataFilePath() {
		return dataFilePath;
	}

	/**
	 * Returns the fieldSeparator.
	 * @return String
	 */
	public static String getFieldSeparator() {
		return fieldSeparator;
	}

}
