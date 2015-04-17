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
/**
 * 
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.utilities;

import org.joda.time.YearMonthDay;

public class StudentDiplomaInformation implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7632049994026398211L;

    private String graduateTitle;

    private String name;

    private String nameOfFather;

    private String nameOfMother;

    private String birthLocale;

    private String degreeName;

    private String dissertationTitle;

    private String classificationResult;

    private YearMonthDay conclusionDate;

    private boolean isMasterDegree;

    private String filename;

    public String getGraduateTitle() {
        return graduateTitle;
    }

    public void setGraduateTitle(String graduateTitle) {
        this.graduateTitle = graduateTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameOfFather() {
        return nameOfFather;
    }

    public void setNameOfFather(String nameOfFather) {
        this.nameOfFather = nameOfFather;
    }

    public String getNameOfMother() {
        return nameOfMother;
    }

    public void setNameOfMother(String nameOfMother) {
        this.nameOfMother = nameOfMother;
    }

    public String getBirthLocale() {
        return birthLocale;
    }

    public void setBirthLocale(String birthLocale) {
        this.birthLocale = birthLocale;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }

    public String getDissertationTitle() {
        return dissertationTitle;
    }

    public void setDissertationTitle(String dissertationTitle) {
        this.dissertationTitle = dissertationTitle;
    }

    public String getClassificationResult() {
        return classificationResult;
    }

    public void setClassificationResult(String classificationResult) {
        this.classificationResult = classificationResult;
    }

    public YearMonthDay getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(YearMonthDay conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public boolean isMasterDegree() {
        return isMasterDegree;
    }

    public void setMasterDegree(boolean isMasterDegree) {
        this.isMasterDegree = isMasterDegree;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}