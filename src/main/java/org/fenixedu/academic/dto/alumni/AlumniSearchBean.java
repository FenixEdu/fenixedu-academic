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
package net.sourceforge.fenixedu.dataTransferObject.alumni;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.FenixFramework;

public class AlumniSearchBean extends AlumniMailSendToBean {

    private int totalItems;

    private String name;
    private Integer studentNumber;
    private String documentIdNumber;
    private Degree degree;
    private List<Registration> alumni;
    private String email;
    private String mobileNumber;
    private String telephoneNumber;
    private ExecutionYear firstExecutionYear;
    private ExecutionYear finalExecutionYear;

    public AlumniSearchBean() {
        this("", ExecutionYear.readFirstExecutionYear(), ExecutionYear.readLastExecutionYear());
    }

    public AlumniSearchBean(DegreeType degreeType, String name, ExecutionYear firstYear, ExecutionYear lastYear) {
        this(name, firstYear, lastYear);
        setDegreeType(degreeType);
    }

    public AlumniSearchBean(String name, ExecutionYear firstYear, ExecutionYear lastYear) {
        setName(name);
        setFirstExecutionYear(firstYear);
        setFinalExecutionYear(lastYear);
        setDegreeType(null);
    }

    public List<Registration> getAlumni() {
        if (this.alumni == null) {
            return null;
        }
        List<Registration> alumni = new ArrayList<Registration>();
        for (Registration reference : this.alumni) {
            alumni.add(reference);
        }
        return alumni;
    }

    public void setAlumni(List<Registration> alumni) {
        if (this.alumni == null) {
            this.alumni = new ArrayList<Registration>(alumni.size());
        } else {
            this.alumni.clear();
        }

        for (Registration person : alumni) {
            this.alumni.add(person);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearchElementsAsParameters() {
        String urlParameters = "&amp;beansearch=" + this.getDegreeType() + ":" + this.getName() + ":";
        urlParameters += (this.getFirstExecutionYear() == null ? "null" : this.getFirstExecutionYear().getExternalId()) + ":";
        urlParameters += (this.getFinalExecutionYear() == null ? "null" : this.getFinalExecutionYear().getExternalId());
        return urlParameters;
    }

    public static AlumniSearchBean getBeanFromParameters(String requestParameter) {
        final String[] values = requestParameter.split(":");
        final String firstYear = values[2];
        final String finalYear = values[3];

        ExecutionYear first =
                (firstYear.equals("null") ? ExecutionYear.readFirstExecutionYear() : FenixFramework
                        .<ExecutionYear> getDomainObject(firstYear));
        ExecutionYear last =
                (finalYear.equals("null") ? ExecutionYear.readLastExecutionYear() : FenixFramework
                        .<ExecutionYear> getDomainObject(finalYear));

        if (values[0].equals("null")) {
            return new AlumniSearchBean(values[1], first, last);
        } else {
            return new AlumniSearchBean(DegreeType.valueOf(values[0]), values[1], first, last);
        }
    }

    public ExecutionYear getFinalExecutionYear() {
        return finalExecutionYear;
    }

    public void setFinalExecutionYear(ExecutionYear finalExecutionYear) {
        this.finalExecutionYear = finalExecutionYear;
    }

    public ExecutionYear getFirstExecutionYear() {
        return firstExecutionYear;
    }

    public void setFirstExecutionYear(ExecutionYear firstExecutionYear) {
        this.firstExecutionYear = firstExecutionYear;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public Degree getDegree() {
        return this.degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getDocumentIdNumber() {
        return documentIdNumber;
    }

    public void setDocumentIdNumber(String documentIdNumber) {
        this.documentIdNumber = documentIdNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
