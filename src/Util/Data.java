package Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.struts.util.LabelValueBean;

public class Data implements Serializable {
	private int _dia;
	private int _mes;
	private int _ano;

	public static String JANUARY_STRING = "Janeiro";
	public static String FEBRUARY_STRING = "Fevereiro";
	public static String MARCH_STRING = "Março";
	public static String APRIL_STRING = "Abril";
	public static String MAY_STRING = "Maio";
	public static String JUNE_STRING = "Junho";
	public static String JULY_STRING = "Julho";
	public static String AUGUST_STRING = "Agosto";
	public static String SETEMBER_STRING = "Setembro";
	public static String OCTOBER_STRING = "Outubro";
	public static String NOVEMBER_STRING = "Novembro";
	public static String DECEMBER_STRING = "Dezembro";
	public static String OPTION_STRING = "";

	public static Integer JANUARY = new Integer(0);
	public static Integer FEBRUARY = new Integer(1);
	public static Integer MARCH = new Integer(2);
	public static Integer APRIL = new Integer(3);
	public static Integer MAY = new Integer(4);
	public static Integer JUNE = new Integer(5);
	public static Integer JULY = new Integer(6);
	public static Integer AUGUST = new Integer(7);
	public static Integer SETEMBER = new Integer(8);
	public static Integer OCTOBER = new Integer(9);
	public static Integer NOVEMBER = new Integer(10);
	public static Integer DECEMBER = new Integer(11);
	public static String OPTION_DEFAULT = null;

	/* Construtores */

	public Data(int dia, int mes, int ano) {
		dia(dia);
		mes(mes);
		ano(ano);
	}

	/* Selectores */

	public int dia() {
		return _dia;
	}

	public int mes() {
		return _mes;
	}

	public int ano() {
		return _ano;
	}

	/* Modificadores */

	public void dia(int dia) {
		_dia = dia;
	}

	public void mes(int mes) {
		_mes = mes;
	}

	public void ano(int ano) {
		_ano = ano;
	}

	/* Comparador */

	public boolean equals(Object o) {
		return o instanceof Data && _dia == ((Data) o).dia() && _mes == ((Data) o).mes() && _ano == ((Data) o).ano();
	}

	public static ArrayList getMonthDays() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT));
		for (int i = 1; i <= 31; i++)
			result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
		return result;
	}

	public static ArrayList getMonths() {
		ArrayList result = new ArrayList();
		result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT));
		result.add(new LabelValueBean(Data.JANUARY_STRING, Data.JANUARY.toString()));
		result.add(new LabelValueBean(Data.FEBRUARY_STRING, Data.FEBRUARY.toString()));
		result.add(new LabelValueBean(Data.MARCH_STRING, Data.MARCH.toString()));
		result.add(new LabelValueBean(Data.APRIL_STRING, Data.APRIL.toString()));
		result.add(new LabelValueBean(Data.MAY_STRING, Data.MAY.toString()));
		result.add(new LabelValueBean(Data.JUNE_STRING, Data.JUNE.toString()));
		result.add(new LabelValueBean(Data.JULY_STRING, Data.JULY.toString()));
		result.add(new LabelValueBean(Data.AUGUST_STRING, Data.AUGUST.toString()));
		result.add(new LabelValueBean(Data.SETEMBER_STRING, Data.SETEMBER.toString()));
		result.add(new LabelValueBean(Data.OCTOBER_STRING, Data.OCTOBER.toString()));
		result.add(new LabelValueBean(Data.NOVEMBER_STRING, Data.NOVEMBER.toString()));
		result.add(new LabelValueBean(Data.DECEMBER_STRING, Data.DECEMBER.toString()));
		return result;
	}

	public static ArrayList getYears() {
		ArrayList result = new ArrayList();
		Calendar date = Calendar.getInstance();

		result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT));

		for (int i = date.get(Calendar.YEAR); i > 1900; i--)
			result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
		return result;
	}

	public static ArrayList getExpirationYears() {
		ArrayList result = new ArrayList();
		Calendar date = Calendar.getInstance();

		result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT));

		for (int i = date.get(Calendar.YEAR)-1; i < (date.get(Calendar.YEAR) + 20); i++)
			result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
		return result;
	}

	public static boolean validDate(Integer day, Integer month, Integer year) {
		boolean leapYear = false;

		// check 30 day months
		if (month.equals(APRIL) || month.equals(JUNE) || month.equals(SETEMBER) || month.equals(NOVEMBER))
			if (day.intValue() == 31)
				return false;

		// Verifies if the Year

		if ((year.intValue() % 4 == 0 && year.intValue() % 100 != 0) || (year.intValue() % 400 == 0))
			leapYear = true;

		if (month.equals(FEBRUARY) && leapYear && day.intValue() >= 30)
			return false;
		else if (month.equals(FEBRUARY) && !leapYear && day.intValue() >= 29)
			return false;

		return true;
	}

	/**
	 * Formats date in format d separator M separator YYYY
	 * @param Date to Format
	 * @return String separator
	 * 
	 */
	public static String format2DayMonthYear(java.util.Date date, String separator) {
		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String result = new String();
		result += calendar.get(Calendar.DAY_OF_MONTH);
		result += separator;
		result += calendar.get(Calendar.MONTH) + 1;
		result += separator;
		result += calendar.get(Calendar.YEAR);
		return result;
	}
	/**
	 *	Formats data in format d-M-YYYY 
	 * @param Date to Format
	 */
	public static String format2DayMonthYear(java.util.Date date) {
		return Data.format2DayMonthYear(date, "-");
	}
	
	public static java.util.Date convertStringDate(String stringToConvert, String separator) {
		java.util.Date dateString = null;
		if (stringToConvert != null && stringToConvert.length() > 0) {
			String[] dateTokens = stringToConvert.split(separator);
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DAY_OF_MONTH, (new Integer(dateTokens[0])).intValue());
			calendar.set(Calendar.MONTH, (new Integer(dateTokens[1])).intValue() - 1);
			calendar.set(Calendar.YEAR, (new Integer(dateTokens[2])).intValue());
			dateString = calendar.getTime();
		}
		return dateString;
	}

}