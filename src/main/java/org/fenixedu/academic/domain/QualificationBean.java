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
package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.Comparator;

import org.joda.time.Partial;

public class QualificationBean implements Serializable {

    static private final long serialVersionUID = 3778404795808964270L;

    static public Comparator<QualificationBean> COMPARATOR_BY_MOST_RECENT_ATTENDED_END = new Comparator<QualificationBean>() {

        @Override
        public int compare(QualificationBean o1, QualificationBean o2) {
            if (o1.getAttendedEnd() == null) {
                return 1;
            }
            if (o2.getAttendedEnd() == null) {
                return -1;
            }
            return -o1.getAttendedEnd().compareTo(o2.getAttendedEnd());
        }
    };

    private QualificationType type;
    private String school;
    private String degree;
    private Partial attendedBegin;
    private Partial attendedEnd;
    private String year;
    private String mark;

    public QualificationBean() {
    }

    public QualificationType getType() {
        return type;
    }

    public void setType(QualificationType type) {
        this.type = type;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Partial getAttendedBegin() {
        return attendedBegin;
    }

    public void setAttendedBegin(Partial attendedBegin) {
        this.attendedBegin = attendedBegin;
    }

    public Partial getAttendedEnd() {
        return attendedEnd;
    }

    public void setAttendedEnd(Partial attendedEnd) {
        this.attendedEnd = attendedEnd;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

}
