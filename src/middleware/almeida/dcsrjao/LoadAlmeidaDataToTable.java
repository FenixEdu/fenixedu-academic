/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.almeida.dcsrjao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import middleware.almeida.PersistentObjectOJBReader;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Country;
import Dominio.ICountry;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.PersonRole;
import Dominio.Role;
import Util.RoleType;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public abstract class LoadAlmeidaDataToTable {

	protected int numberLinesProcessed = 0;
	protected int numberElementsWritten = 0;
	protected int numberUntreatableElements = 0;
	protected Calendar startTime = null;
	protected Calendar endTime = null;
	protected String actualLine = null;
	protected long inicio = 0;
	protected long fim = 0;
	protected long total = 0;


	protected PersistentObjectOJBReader persistentObjectOJB = null;

	public void load() {
		System.out.println("Loading " + getFilename());
		startTime = Calendar.getInstance();
		persistentObjectOJB = new PersistentObjectOJBReader();
		
		BufferedReader bufferedReader;

		try {
			bufferedReader = new BufferedReader(new FileReader(getFilename()));
			actualLine = bufferedReader.readLine();
			while (actualLine != null) {
				processLine(actualLine);
				actualLine = bufferedReader.readLine();

				numberLinesProcessed++;
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		endTime = Calendar.getInstance();
		System.out.println("Done.");
	}

	/**
	 * @param Line from text file to be processed
	 */
	abstract protected void processLine(String line);

	abstract protected String getFilename();

	abstract protected String getFilenameOutput();

	abstract protected String getFieldSeparator();

	protected void writeElement(Object persistentObject) {
		persistentObjectOJB.lockWrite(persistentObject);
		numberElementsWritten++;
	}

	protected List query(Class classToQuery, Criteria criteria) {
		return persistentObjectOJB.query(classToQuery, criteria);
	}

	protected List query(Object object) {
		return persistentObjectOJB.query(object);
	}

	protected Date convertToJavaDate(String dateString) {
		try {
			String formattedString = dateString.substring(3, 5);
			formattedString += "/" + dateString.substring(0, 2);
			formattedString += "/" + dateString.substring(6);
			return DateFormat.getDateInstance(DateFormat.SHORT).parse(formattedString);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to parse date: " + dateString);
		}
	}

	protected void setupDAO() {
		persistentObjectOJB.beginTransaction();
	}

	protected void shutdownDAO() {
		persistentObjectOJB.commitTransaction();
	}

	protected void writeToFile(String bufferToWrite) {
		try {
			FileWriter outFile = new FileWriter(getFilenameOutput());
			outFile.write(bufferToWrite);
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to obtain a file writer for " + getFilenameOutput());
		}
	}

	protected String report(String logFile) {
		long duration = (endTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
		long durationHour = duration / 3600;
		long durationMin = (duration % 3600) / 60;
		long durationSec = (duration % 3600) % 60;
		String report = "";
		
//		System.out.println("----------------------------------------------------------------");
//		System.out.println("   Report for loading of file: " + getFilename());
//		System.out.println("      Number of lines parsed: " + numberLinesProcessed);
//		System.out.println("      Number of elements added: " + numberElementsWritten);
//		System.out.println("      Number of untreatable elements: " + numberUntreatableElements);
//		System.out.println("      Total processing time: " + durationHour + "h " + durationMin + "m " + durationSec + "s");
//		System.out.println("----------------------------------------------------------------");
//		
		report += "\n----------------------------------------------------------------";
		report += "\n   Report for loading of file: " + getFilename();
		report += "\n      Number of lines parsed: " + numberLinesProcessed;
		report += "\n      Number of elements added: " + numberElementsWritten;
		report += "\n      Number of untreatable elements: " + numberUntreatableElements;
		report += "\n      Total processing time: " + durationHour + "h " + durationMin + "m " + durationSec + "s";
		report += "\n----------------------------------------------------------------";
		
		logFile += report;
		System.out.println(report);
		return logFile;
	}

	public ICountry convertCountry(String countryCode) {

		Criteria criteria = new Criteria();

		if (countryCode.equals("01") || countryCode.equals("02") || countryCode.equals("03") || countryCode.equals("04") || countryCode.equals("05") || countryCode.equals("06")) {
			criteria.addEqualTo("name", "PORTUGAL");
		} else if (countryCode.equals("10")) {
			criteria.addEqualTo("name", "ANGOLA");
		} else if (countryCode.equals("11")) {
			criteria.addEqualTo("name", "BRASIL");
		} else if (countryCode.equals("12")) {
			criteria.addEqualTo("name", "CABO VERDE");
		} else if (countryCode.equals("13")) {
			criteria.addEqualTo("name", "GUINE-BISSAO");
		} else if (countryCode.equals("14")) {
			criteria.addEqualTo("name", "MOCAMBIQUE");
		} else if (countryCode.equals("15")) {
			criteria.addEqualTo("name", "SAO TOME E PRINCIPE");
		} else if (countryCode.equals("16")) {
			criteria.addEqualTo("name", "TIMOR LORO SAE");
		} else if (countryCode.equals("20")) {
			criteria.addEqualTo("name", "BELGICA");
		} else if (countryCode.equals("21")) {
			criteria.addEqualTo("name", "DINAMARCA");
		} else if (countryCode.equals("22")) {
			criteria.addEqualTo("name", "ESPANHA");
		} else if (countryCode.equals("23")) {
			criteria.addEqualTo("name", "FRANCA");
		} else if (countryCode.equals("24")) {
			criteria.addEqualTo("name", "HOLANDA");
		} else if (countryCode.equals("25")) {
			criteria.addEqualTo("name", "IRLANDA");
		} else if (countryCode.equals("26")) {
			criteria.addEqualTo("name", "ITALIA");
		} else if (countryCode.equals("27")) {
			criteria.addEqualTo("name", "LUXEMBURGO");
		} else if (countryCode.equals("28")) {
			criteria.addEqualTo("name", "ALEMANHA");
		} else if (countryCode.equals("29")) {
			criteria.addEqualTo("name", "REINO UNIDO");
		} else if (countryCode.equals("30")) {
			criteria.addEqualTo("name", "SUECIA");
		} else if (countryCode.equals("31")) {
			criteria.addEqualTo("name", "NORUEGA");
		} else if (countryCode.equals("32")) {
			criteria.addEqualTo("name", "POLONIA");
		} else if (countryCode.equals("33")) {
			criteria.addEqualTo("name", "AFRICA DO SUL");
		} else if (countryCode.equals("34")) {
			criteria.addEqualTo("name", "ARGENTINA");
		} else if (countryCode.equals("35")) {
			criteria.addEqualTo("name", "CANADA");
		} else if (countryCode.equals("36")) {
			criteria.addEqualTo("name", "CHILE");
		} else if (countryCode.equals("37")) {
			criteria.addEqualTo("name", "EQUADOR");
		} else if (countryCode.equals("38")) {
			criteria.addEqualTo("name", "ESTADOS UNIDOS DA AMERICA");
		} else if (countryCode.equals("39")) {
			criteria.addEqualTo("name", "IRAO");
		} else if (countryCode.equals("40")) {
			criteria.addEqualTo("name", "MARROCOS");
		} else if (countryCode.equals("41")) {
			criteria.addEqualTo("name", "VENEZUELA");
		} else if (countryCode.equals("42")) {
			criteria.addEqualTo("name", "AUSTRALIA");
		} else if (countryCode.equals("43")) {
			criteria.addEqualTo("name", "PAQUISTAO");
		} else if (countryCode.equals("44")) {
			criteria.addEqualTo("name", "REPUBLICA DO ZAIRE");
		} else if (countryCode.equals("47")) {
			criteria.addEqualTo("name", "LIBIA");
		} else if (countryCode.equals("48")) {
			criteria.addEqualTo("name", "PALESTINA");
		} else if (countryCode.equals("49")) {
			criteria.addEqualTo("name", "ZIMBABUE");
		} else if (countryCode.equals("50")) {
			criteria.addEqualTo("name", "MEXICO");
		} else if (countryCode.equals("51")) {
			criteria.addEqualTo("name", "RUSSIA");
		} else if (countryCode.equals("52")) {
			criteria.addEqualTo("name", "AUSTRIA");
		} else if (countryCode.equals("53")) {
			criteria.addEqualTo("name", "IRAQUE");
		} else if (countryCode.equals("54")) {
			criteria.addEqualTo("name", "PERU");
		} else if (countryCode.equals("60")) {
			criteria.addEqualTo("name", "ROMENIA");
		} else if (countryCode.equals("61")) {
			criteria.addEqualTo("name", "REPUBLICA CHECA");
		} else
			return null;

		List result = query(Country.class, criteria);

		if (result.size() == 0)
			return null;
		else
			return (ICountry) result.get(0);
	}

	public void givePersonRole(IPessoa person) throws Exception {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("roleType", RoleType.PERSON);

		List result = query(Role.class, criteria);

		Role role = null;
		if (result.size() == 0)
			throw new Exception("Role Desconhecido !!!");
		else
			role = (Role) result.get(0);

		IPersonRole newRole = new PersonRole();
		newRole.setPerson(person);
		newRole.setRole(role);

		writeElement(newRole);
	}

	public void signalAlive() {
//		if((numberElementsWritten % 1000) == 0){
//			System.out.print(".");
//		}
//		long duration = (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
//		long durationSec = (duration % 3600) % 60;
//
//		System.out.println("Duração: " + durationSec);

	}
	public void initDuration() {
		this.inicio = Calendar.getInstance().getTimeInMillis();
	}

	public void endDuration() {
		this.fim = Calendar.getInstance().getTimeInMillis();

		long duracaoMills = fim - inicio;

		total = total + duracaoMills;

		System.out.println("Duração: " + duracaoMills);
		System.out.println("Media: " + total / (numberLinesProcessed + 1));
	}

	public void printLine(String className) {
		System.out.println(className + " linha: " + (numberLinesProcessed + 1));
	}
	
	public HashMap setErrorMessage(String errorMessage, String errorDBID, HashMap error) {
		String value = "";
		value = (String) error.get(errorMessage);
		if (value != null) {
			error.remove(errorMessage);
			value += errorDBID;
		} else {
			value = errorDBID;
		}
		error.put(errorMessage, value);
		return error;
	}
}