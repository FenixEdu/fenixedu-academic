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
package net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.util.Month;

import org.joda.time.DateTimeFieldType;

public class ChangeTutorshipByEntryYearBean implements Serializable {
    List<ChangeTutorshipBean> changeTutorshipsBeans;

    private ExecutionYear executionYear;

    public ChangeTutorshipByEntryYearBean(ExecutionYear executionYear) {
        this.changeTutorshipsBeans = new ArrayList<ChangeTutorshipBean>();
        this.executionYear = executionYear;
    }

    public ExecutionYear getExecutionYear() {
        return (executionYear);
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public List<ChangeTutorshipBean> getChangeTutorshipsBeans() {
        return changeTutorshipsBeans;
    }

    public void setChangeTutorshipsBeans(List<ChangeTutorshipBean> changeTutorshipsBeans) {
        this.changeTutorshipsBeans = changeTutorshipsBeans;
    }

    public void addTutorship(Tutorship tutorship) {
        ChangeTutorshipBean bean = new ChangeTutorshipBean(tutorship);
        changeTutorshipsBeans.add(bean);
    }

    public class ChangeTutorshipBean implements Serializable {
        private Tutorship tutorship;

        private Month tutorshipEndMonth;

        private Integer tutorshipEndYear;

        public ChangeTutorshipBean(Tutorship tutorship) {
            setTutorship(tutorship);

            if (tutorship.getEndDate() != null) {
                setTutorshipEndMonthFromTutorshipEndDate();
                setTutorshipEndYear(getTutorship().getEndDate().get(DateTimeFieldType.year()));
            } else {
                setTutorshipEndMonth(null);
                setTutorshipEndYear(null);
            }
        }

        public void setTutorshipEndMonthFromTutorshipEndDate() {
            Month month = Month.values()[getTutorship().getEndDate().get(DateTimeFieldType.monthOfYear()) - 1];
            setTutorshipEndMonth(month);
        }

        public Tutorship getTutorship() {
            return (tutorship);
        }

        public void setTutorship(Tutorship tutorship) {
            this.tutorship = tutorship;
        }

        public String getTutorshipEndMonthYear() {
            return generateMonthYearOption(tutorshipEndMonth.getNumberOfMonth(), tutorshipEndYear);
        }

        /*
         * Receives the MonthYear in the format MM/YYYY
         */
        public void setTutorshipEndMonthYear(String tutorshipEndMonthYear) {
            String[] parts = tutorshipEndMonthYear.split("/");

            this.tutorshipEndMonth = Month.fromInt(Integer.parseInt(parts[0]));
            this.tutorshipEndYear = Integer.parseInt(parts[1]);
        }

        public String generateMonthYearOption(int month, int year) {
            return month + "/" + year;
        }

        public Month getTutorshipEndMonth() {
            return tutorshipEndMonth;
        }

        public void setTutorshipEndMonth(Month tutorshipEndMonth) {
            this.tutorshipEndMonth = tutorshipEndMonth;
        }

        public Integer getTutorshipEndYear() {
            return tutorshipEndYear;
        }

        public void setTutorshipEndYear(Integer tutorshipEndYear) {
            this.tutorshipEndYear = tutorshipEndYear;
        }
    }
}
