/*
 * InfoClass.java
 * 
 * Created on 31 de Outubro de 2002, 12:27
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class InfoClass extends InfoObject {

    private final SchoolClass schoolClass;

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public InfoClass(final SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public String getNome() {
        return getSchoolClass().getNome();
    }

    public Integer getAnoCurricular() {
        return getSchoolClass().getAnoCurricular();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getSchoolClass() == ((InfoClass) obj).getSchoolClass();
    }

    @Override
    public String toString() {
        return getSchoolClass().toString();
    }

    public InfoExecutionDegree getInfoExecutionDegree() {
        return InfoExecutionDegree.newInfoFromDomain(getSchoolClass().getExecutionDegree());
    }

    public AcademicInterval getAcademicInterval() {
        return getSchoolClass().getAcademicInterval();
    }

    @Deprecated
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return InfoExecutionPeriod.newInfoFromDomain(getSchoolClass().getExecutionPeriod());
    }

    public static InfoClass newInfoFromDomain(final SchoolClass schoolClass) {
        return schoolClass == null ? null : new InfoClass(schoolClass);
    }

    @Override
    public String getExternalId() {
        return getSchoolClass().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

}
