/*
 * Teacher.java
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits;
import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator;
import net.sourceforge.fenixedu.domain.credits.util.InfoCreditsBuilder;

/**
 * @author EP15
 * @author Ivo Brandão
 */
public class Teacher extends Teacher_Base {

    private List degreeFinalProjectStudents;

    private List institutionWorkTimePeriods;

    private List managementPositions;

    private List serviceExemptionSituations;

    private List otherTypeCreditLines;

    private Map creditsMap = new HashMap();

    /** Creates a new instance of Teacher */
    public Teacher() {
    }

    /** Creates a new instance of Teacher */
    public Teacher(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Teacher(IPerson person, Integer teacherNumber) {
        setPerson(person);
        setTeacherNumber(teacherNumber);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ITeacher) {
            resultado = getTeacherNumber().equals(((ITeacher) obj).getTeacherNumber());
        }
        return resultado;
    }

    /**
     * @return Returns the degreeFinalProjectStudents.
     */
    public List getDegreeFinalProjectStudents() {
        return degreeFinalProjectStudents;
    }

    /**
     * @return Returns the institutionWorkTimePeriods.
     */
    public List getInstitutionWorkTimePeriods() {
        return institutionWorkTimePeriods;
    }

    /**
     * @return Returns the managementPositions.
     */
    public List getManagementPositions() {
        return managementPositions;
    }

    /**
     * @return Returns the otherTypeCreditLines.
     */
    public List getOtherTypeCreditLines() {
        return otherTypeCreditLines;
    }

    /**
     * @return Returns the serviceExemptionSituations.
     */
    public List getServiceExemptionSituations() {
        return serviceExemptionSituations;
    }

    /**
     * @param degreeFinalProjectStudents
     *            The degreeFinalProjectStudents to set.
     */
    public void setDegreeFinalProjectStudents(List degreeFinalProjectStudents) {
        this.degreeFinalProjectStudents = degreeFinalProjectStudents;
    }

    /**
     * @param institutionWorkTimePeriods
     *            The institutionWorkTimePeriods to set.
     */
    public void setInstitutionWorkTimePeriods(List institutionWorkTimePeriods) {
        this.institutionWorkTimePeriods = institutionWorkTimePeriods;
    }

    /**
     * @param managementPositions
     *            The managementPositions to set.
     */
    public void setManagementPositions(List managementPositions) {
        this.managementPositions = managementPositions;
    }

    /**
     * @param otherTypeCreditLines
     *            The otherTypeCreditLines to set.
     */
    public void setOtherTypeCreditLines(List otherTypeCreditLines) {
        this.otherTypeCreditLines = otherTypeCreditLines;
    }

    /**
     * @param serviceExemptionSituations
     *            The serviceExemptionSituations to set.
     */
    public void setServiceExemptionSituations(List serviceExemptionSituations) {
        this.serviceExemptionSituations = serviceExemptionSituations;
    }

    public String toString() {
        String result = "[Dominio.Teacher ";
        result += ", teacherNumber=" + getTeacherNumber();
        result += ", person=" + getPerson();
        result += ", category= " + getCategory();
        result += "]";
        return result;
    }

    public InfoCredits getExecutionPeriodCredits(IExecutionPeriod executionPeriod) {
        InfoCredits credits = (InfoCredits) creditsMap.get(executionPeriod);
        if (credits == null) {
            credits = InfoCreditsBuilder.build(this, executionPeriod);
            creditsMap.put(executionPeriod, credits);
        }
        return credits;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.ITeacher#notifyCreditsChange(Dominio.credits.event.CreditsEvent,
     *      Dominio.credits.event.ICreditsEventOriginator)
     */
    public void notifyCreditsChange(CreditsEvent creditsEvent, ICreditsEventOriginator originator) {
        Iterator iterator = this.creditsMap.keySet().iterator();
        while (iterator.hasNext()) {
            IExecutionPeriod executionPeriod = (IExecutionPeriod) iterator.next();
            if (originator.belongsToExecutionPeriod(executionPeriod)) {
                iterator.remove();
            }
        }
    }
}