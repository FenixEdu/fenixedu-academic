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
/*
 * Created on Apr 3, 2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public class ExamsMapSlot {

    private Calendar day;

    private List exams;

    private Locale locale;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ExamsMapSlot(Calendar day, List exams) {
        setDay(day);
        setExams(exams);

    }

    public Calendar getDay() {
        return day;
    }

    public List getExams() {
        return exams;
    }

    public void setDay(Calendar calendar) {
        day = calendar;
    }

    public void setExams(List list) {
        exams = list;
    }
}