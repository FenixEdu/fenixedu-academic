package Util;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.struts.util.LabelValueBean;

public class Data {
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
  public static String OPTION_STRING = "Escolha uma opção";
  
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
  public static Integer OPTION_DEFAULT = new Integer(-1);


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
    return o instanceof Data &&
           _dia == ((Data)o).dia() &&
           _mes == ((Data)o).mes() &&
           _ano == ((Data)o).ano();
  }
  
  public static ArrayList getMonthDays() {
	ArrayList result = new ArrayList();
	result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT.toString()));
	for (int i = 1; i<=31; i++)
		result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
	return result;	
  }


  public static ArrayList getMonths() {
	ArrayList result = new ArrayList();
	result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT.toString()));
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
	
	result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT.toString()));

	for (int i = date.get(Calendar.YEAR); i>1900; i--)
		result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
	return result;	
  }
  

  public static ArrayList getExpirationYears() {
	  ArrayList result = new ArrayList();
	  Calendar date = Calendar.getInstance();
	
	  result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT.toString()));

	  for (int i = date.get(Calendar.YEAR); i<(date.get(Calendar.YEAR) + 20); i++)
		  result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
	  return result;	
	}

  
  public static boolean validDate(Integer day, Integer month) {
  	
	// check 30 day months
	if (month.equals(APRIL) || month.equals(JUNE) || month.equals(SETEMBER) || month.equals(NOVEMBER))
		if (day.intValue() == 31)
		return false;

	if (month.equals(FEBRUARY) && day.intValue() >= 30)
		return false;	 
	
	
  	return true;
  }
  
}