//provalvelmente não será preciso


package middleware.dataClean.personFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;

public class Parameters {

	private static Parameters theInstance = null;

	/**
	 * Return the unique instance of this class.
	 * @return the unique instance
	 */
	public static Parameters getInstance() {
		if (theInstance == null) {
			synchronized (Parameters.class) {
				if (theInstance == null)
					theInstance = new Parameters();
			}
		}
		return theInstance;
	}

	private Properties cleanProperties;

	// These are default values for the parameters
	// They are set only if the configuration file /conf/clean.conf is missing
	private String defaults[][] = {

		// The database driver to the RDBMS
		{ "clean.driver", "com.mysql.jdbc.Driver" },

		// The url to the RDBMS
		{
			"clean.url", "jdbc:mysql://localhost:3306/personFilter" },

		// The RDBMS user
		{
			"clean.user", "root" },

		// The RDBMS user's password
		{
			"clean.password", "" },

		// The error log file location
		{
			"clean.errorFile", "FicheiroErros.txt" },

		// Flag for generation oof log file
		{
			"clean.log", "false" },

		// Value for other
		{
			"clean.other", "-1" }, 
		};

	private Parameters() {
		try {
			String confFile = "/personFilter.properties";

			//BufferedReader br = new BufferedReader(new FileReader(confFile));
			InputStream input = this.getClass().getResourceAsStream(confFile) ;
			
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			
			cleanProperties = new Properties();
			String line = br.readLine();
			while (line != null) {
				StringTokenizer st = new StringTokenizer(line);
				try {
					String key = st.nextToken();
					String value = st.nextToken();
					if (!key.startsWith("//") && !value.startsWith("//")) {
						key = "clean." + key;
						cleanProperties.put(key, value);
					}
				} catch (NoSuchElementException e) {
					// do nothing, not a good property value
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			cleanProperties = new Properties();
			System.out.println("Error: configuration file missing !");
			System.out.println("Setting default values for all the parameters!");
			for (int i = 0; i < defaults.length; i++) {
				String key = defaults[i][0];
				String value = defaults[i][1];
				cleanProperties.put(key, value);
			}
		}
		Properties p = System.getProperties();
		for (Enumeration e = p.propertyNames(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			if (key.indexOf("clean.") == 0) {
				String value = p.getProperty(key);
				cleanProperties.put(key, value);
			}
		}
	}

	/**
	 * Return the value of paramameter.
	 * @param name parameter name
	 */
	public String get(String name) {
		String s = cleanProperties.getProperty(name);
		return s;
	}

	/**
	 * Sets the value of the given paramameter.
	 * @param name parameter name
	 * @param value the new parameter value
	 */
	public void put(String name, String value) {
		cleanProperties.put(name, value);
	}

	/**
	 * Return the properties of ajax framework.
	 * @return ajax parameters
	 */
	public Properties getProperties() {
		return cleanProperties;
	}
}
