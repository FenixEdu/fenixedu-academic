package Util;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.struts.util.LabelValueBean;

public class Data {
  private int _dia;
  private int _mes;
  private int _ano;
 
  private static String january = "Janeiro";
  private static String february = "Fevereiro";
  private static String march = "Março";
  private static String april = "Abril";
  private static String may = "Maio";
  private static String june = "Junho";
  private static String july = "Julho";
  private static String august = "Agosto";
  private static String setember = "Setembro";
  private static String october = "Outubro";
  private static String november = "Novembro";
  private static String december = "Dezembro";
  

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
	
	for (int i = 1; i<=31; i++)
		result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
	return result;	
  }


  public static ArrayList getMonths() {
	ArrayList result = new ArrayList();
	
	result.add(new LabelValueBean(Data.january, "0"));
	result.add(new LabelValueBean(Data.february, "1"));
	result.add(new LabelValueBean(Data.march, "2"));
	result.add(new LabelValueBean(Data.april, "3"));
	result.add(new LabelValueBean(Data.may, "4"));
	result.add(new LabelValueBean(Data.june, "5"));
	result.add(new LabelValueBean(Data.july, "6"));
	result.add(new LabelValueBean(Data.august, "7"));
	result.add(new LabelValueBean(Data.setember, "8"));
	result.add(new LabelValueBean(Data.october, "9"));
	result.add(new LabelValueBean(Data.november, "10"));
	result.add(new LabelValueBean(Data.december, "11"));
	return result;	
  }


  public static ArrayList getYears() {
	ArrayList result = new ArrayList();
	Calendar date = Calendar.getInstance();
	
	
	for (int i = date.get(Calendar.YEAR); i>1900; i--)
		result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
	return result;	
  }
  
  
}