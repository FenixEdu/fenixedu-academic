/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.domain.Seminaries;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public class Seminary extends DomainObject implements ISeminary {

    private String name;

    private String description;

    private List equivalencies;

    private Integer allowedCandidaciesPerStudent;

    private Calendar enrollmentBeginDate;

    private Calendar enrollmentBeginTime;

    private Calendar enrollmentEndDate;

    private Calendar enrollmentEndTime;

    private Boolean hasTheme;

    private Boolean hasCaseStudy;

    private List candidacies;

    public Seminary() {
    }

    public Seminary(String name, String description, List equivalencies) {
        this.setDescription(description);
        this.setName(name);
        this.setEquivalencies(equivalencies);
    }

    public Seminary(String name, String description) {
        this.setDescription(description);
        this.setName(name);
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public List getEquivalencies() {
        return equivalencies;
    }

    /**
     * @param string
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param list
     */
    public void setEquivalencies(List list) {
        equivalencies = list;
    }

    public String toString() {
        String result;
        result = "[Seminary:";
        result += "ID=" + this.getIdInternal();
        result += ",Name=" + this.getName();
        result += ",Description=" + this.getDescription();
        result += ",Allowed Candidacies Per Student=" + this.getAllowedCandidaciesPerStudent();
        result += ",Modalities=" + this.getEquivalencies() + "]";
        return result;
    }

    /**
     * true if the names are equals
     */
    public boolean equals(Object obj) {
        boolean equalsResult = false;
        if (obj instanceof ISeminary) {
            ISeminary seminary = (ISeminary) obj;
            if (seminary.getName() == null)
                equalsResult = (this.getName() == null);
            else
                equalsResult = this.getName().equalsIgnoreCase(seminary.getName());
        }
        return equalsResult;
    }

    /**
     * @return
     */
    public Integer getAllowedCandidaciesPerStudent() {
        return allowedCandidaciesPerStudent;
    }

    /**
     * @param integer
     */
    public void setAllowedCandidaciesPerStudent(Integer integer) {
        allowedCandidaciesPerStudent = integer;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginDate() {
        return enrollmentBeginDate;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentBeginTime() {
        return enrollmentBeginTime;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndDate() {
        return enrollmentEndDate;
    }

    /**
     * @return
     */
    public Calendar getEnrollmentEndTime() {
        return enrollmentEndTime;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginDate(Calendar calendar) {
        enrollmentBeginDate = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentBeginTime(Calendar calendar) {
        enrollmentBeginTime = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndDate(Calendar calendar) {
        enrollmentEndDate = calendar;
    }

    /**
     * @param calendar
     */
    public void setEnrollmentEndTime(Calendar calendar) {
        enrollmentEndTime = calendar;
    }

    /**
     * @return
     */
    public Boolean getHasTheme() {
        return hasTheme;
    }

    /**
     * @param boolean1
     */
    public void setHasTheme(Boolean boolean1) {
        hasTheme = boolean1;
    }

    /**
     * @return
     */
    public Boolean getHasCaseStudy() {
        return hasCaseStudy;
    }

    /**
     * @param boolean1
     */
    public void setHasCaseStudy(Boolean boolean1) {
        hasCaseStudy = boolean1;
    }

    /**
     * @return Returns the candidacies.
     */
    public List getCandidacies() {
        return candidacies;
    }

    /**
     * @param candidacies
     *            The candidacies to set.
     */
    public void setCandidacies(List candidacies) {
        this.candidacies = candidacies;
    }
}