/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

public class Data {

    public static final String JANUARY_STRING = "Janeiro";

    public static final String FEBRUARY_STRING = "Fevereiro";

    public static final String MARCH_STRING = "Março";

    public static final String APRIL_STRING = "Abril";

    public static final String MAY_STRING = "Maio";

    public static final String JUNE_STRING = "Junho";

    public static final String JULY_STRING = "Julho";

    public static final String AUGUST_STRING = "Agosto";

    public static final String SETEMBER_STRING = "Setembro";

    public static final String OCTOBER_STRING = "Outubro";

    public static final String NOVEMBER_STRING = "Novembro";

    public static final String DECEMBER_STRING = "Dezembro";

    public static final String OPTION_STRING = "";

    public static final Integer JANUARY = new Integer(0);

    public static final Integer FEBRUARY = new Integer(1);

    public static final Integer MARCH = new Integer(2);

    public static final Integer APRIL = new Integer(3);

    public static final Integer MAY = new Integer(4);

    public static final Integer JUNE = new Integer(5);

    public static final Integer JULY = new Integer(6);

    public static final Integer AUGUST = new Integer(7);

    public static final Integer SETEMBER = new Integer(8);

    public static final Integer OCTOBER = new Integer(9);

    public static final Integer NOVEMBER = new Integer(10);

    public static final Integer DECEMBER = new Integer(11);

    public static final String OPTION_DEFAULT = null;

    public static final List<LabelValueBean> getMonthDays() {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        result.add(new LabelValueBean(Data.OPTION_STRING, Data.OPTION_DEFAULT));
        for (int i = 1; i <= 31; i++) {
            result.add(new LabelValueBean(new Integer(i).toString(), new Integer(i).toString()));
        }
        return result;
    }

    public static final List<LabelValueBean> getMonths() {
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

    public static final List<LabelValueBean> getMonthsStartingInOne() {
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

    public static final boolean validDate(Integer day, Integer month, Integer year) {
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

}