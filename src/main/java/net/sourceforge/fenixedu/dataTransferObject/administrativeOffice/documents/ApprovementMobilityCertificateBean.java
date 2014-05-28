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
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.documents;

import java.io.Serializable;

public class ApprovementMobilityCertificateBean implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private String courseName;

    private String ectsCredits;

    private String istGrade;

    private String ectsGrade;

    private String date;

    public ApprovementMobilityCertificateBean() {
        super();
        this.courseName = null;
        this.ectsCredits = null;
        this.istGrade = null;
        this.ectsGrade = null;
        this.date = null;
    }

    public ApprovementMobilityCertificateBean(String courseName, String ectsCredits, String istGrade, String ectsGrade,
            String date) {
        super();
        this.courseName = courseName;
        this.ectsCredits = ectsCredits;
        this.istGrade = istGrade;
        this.ectsGrade = ectsGrade;
        this.date = date;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEctsCredits() {
        return ectsCredits;
    }

    public void setEctsCredits(String ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

    public String getIstGrade() {
        return istGrade;
    }

    public void setIstGrade(String istGrade) {
        this.istGrade = istGrade;
    }

    public String getEctsGrade() {
        return ectsGrade;
    }

    public void setEctsGrade(String ectsGrade) {
        this.ectsGrade = ectsGrade;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
