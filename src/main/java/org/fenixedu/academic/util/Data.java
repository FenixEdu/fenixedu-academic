/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.struts.util.LabelValueBean;

public class Data extends FenixUtil {
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

    @Override
    public boolean equals(Object o) {
        return o instanceof Data && _dia == ((Data) o).dia() && _mes == ((Data) o).mes() && _ano == ((Data) o).ano();
    }

    public static List<LabelValueBean> getMonthDays() {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT));
        for (int i = 1; i <= 31; i++) {
            result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
        }
        return result;
    }

    public static List<SelectItem> getMonthDaysSelectItems() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        for (int i = 1; i <= 31; i++) {
            result.add(new SelectItem(i, new Integer(i).toString()));
        }
        return result;
    }

    public static List<LabelValueBean> getMonths() {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
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

    public static List<LabelValueBean> getMonthsStartingInOne() {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT));
        result.add(new LabelValueBean(Data.JANUARY_STRING, String.valueOf(Data.JANUARY + 1)));
        result.add(new LabelValueBean(Data.FEBRUARY_STRING, String.valueOf(Data.FEBRUARY + 1)));
        result.add(new LabelValueBean(Data.MARCH_STRING, String.valueOf(Data.MARCH + 1)));
        result.add(new LabelValueBean(Data.APRIL_STRING, String.valueOf(Data.APRIL + 1)));
        result.add(new LabelValueBean(Data.MAY_STRING, String.valueOf(Data.MAY + 1)));
        result.add(new LabelValueBean(Data.JUNE_STRING, String.valueOf(Data.JUNE + 1)));
        result.add(new LabelValueBean(Data.JULY_STRING, String.valueOf(Data.JULY + 1)));
        result.add(new LabelValueBean(Data.AUGUST_STRING, String.valueOf(Data.AUGUST + 1)));
        result.add(new LabelValueBean(Data.SETEMBER_STRING, String.valueOf(Data.SETEMBER + 1)));
        result.add(new LabelValueBean(Data.OCTOBER_STRING, String.valueOf(Data.OCTOBER + 1)));
        result.add(new LabelValueBean(Data.NOVEMBER_STRING, String.valueOf(Data.NOVEMBER + 1)));
        result.add(new LabelValueBean(Data.DECEMBER_STRING, String.valueOf(Data.DECEMBER + 1)));
        return result;
    }

    public static List<SelectItem> getMonthsSelectItems() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(Data.JANUARY, Data.JANUARY_STRING));
        result.add(new SelectItem(Data.FEBRUARY, Data.FEBRUARY_STRING));
        result.add(new SelectItem(Data.MARCH, Data.MARCH_STRING));
        result.add(new SelectItem(Data.APRIL, Data.APRIL_STRING));
        result.add(new SelectItem(Data.MAY, Data.MAY_STRING));
        result.add(new SelectItem(Data.JUNE, Data.JUNE_STRING));
        result.add(new SelectItem(Data.JULY, Data.JULY_STRING));
        result.add(new SelectItem(Data.AUGUST, Data.AUGUST_STRING));
        result.add(new SelectItem(Data.SETEMBER, Data.SETEMBER_STRING));
        result.add(new SelectItem(Data.OCTOBER, Data.OCTOBER_STRING));
        result.add(new SelectItem(Data.NOVEMBER, Data.NOVEMBER_STRING));
        result.add(new SelectItem(Data.DECEMBER, Data.DECEMBER_STRING));
        return result;
    }

    public static List getYears() {
        List result = new ArrayList();
        Calendar date = Calendar.getInstance();

        result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT));

        for (int i = date.get(Calendar.YEAR); i > 1900; i--) {
            result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
        }
        return result;
    }

    public static List<SelectItem> getExpirationYearsSelectItems() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        Calendar date = Calendar.getInstance();

        for (int i = date.get(Calendar.YEAR) - 1; i < (date.get(Calendar.YEAR) + 20); i++) {
            result.add(new SelectItem(i, new Integer(i).toString()));
        }
        return result;
    }

    public static List getExpirationYears() {
        List result = new ArrayList();
        Calendar date = Calendar.getInstance();

        result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT));

        for (int i = date.get(Calendar.YEAR) - 10; i < (date.get(Calendar.YEAR) + 20); i++) {
            result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
        }
        return result;
    }

    public static List getCustomYears(int minYear, int maxYear) {
        List result = new ArrayList();
        result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT));

        for (int i = minYear; i <= maxYear; i++) {
            result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
        }
        return result;
    }

    public static boolean validDate(Integer day, Integer month, Integer year) {
        boolean leapYear = false;

        // check 30 day months
        if (month.equals(APRIL) || month.equals(JUNE) || month.equals(SETEMBER) || month.equals(NOVEMBER)) {
            if (day.intValue() == 31) {
                return false;
            }
        }

        // Verifies if the Year

        if ((year.intValue() % 4 == 0 && year.intValue() % 100 != 0) || (year.intValue() % 400 == 0)) {
            leapYear = true;
        }

        if (month.equals(FEBRUARY) && leapYear && day.intValue() >= 30) {
            return false;
        } else if (month.equals(FEBRUARY) && !leapYear && day.intValue() >= 29) {
            return false;
        }

        return true;
    }

    /**
     * Formats date in format d separator M separator YYYY
     * 
     * @param Date
     *            to Format
     * @return String separator
     * 
     */
    public static String format2DayMonthYear(java.util.Date date, String separator) {
        if (date == null) {
            return null;
        }

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
     * Formats date in format dd separator MM separator YYYY
     * 
     * @param Date
     *            to Format
     * @return String separator
     * 
     */
    public static String format2DayMonthYearWithZeros(java.util.Date date, String separator) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String result = new String();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            result += ("0" + day);
        } else {
            result += day;
        }
        result += separator;
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            result += ("0" + month);
        } else {
            result += month;
        }
        result += separator;
        result += calendar.get(Calendar.YEAR);
        return result;
    }

    /**
     * Formats data in format d-M-YYYY
     * 
     * @param Date
     *            to Format
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