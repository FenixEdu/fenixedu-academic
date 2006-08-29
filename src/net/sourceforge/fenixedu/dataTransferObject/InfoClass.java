/*
 * InfoClass.java
 * 
 * Created on 31 de Outubro de 2002, 12:27
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.SchoolClass;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class InfoClass extends InfoObject {

    private final DomainReference<SchoolClass> schoolClassDomainReference;

    public SchoolClass getSchoolClass() {
        return schoolClassDomainReference == null ? null : schoolClassDomainReference.getObject();
    }

    public InfoClass(final SchoolClass schoolClass) {
        schoolClassDomainReference = new DomainReference<SchoolClass>(schoolClass);
    }

    public String getNome() {
        return getSchoolClass().getNome();
    }

    public Integer getAnoCurricular() {
        return getSchoolClass().getAnoCurricular();
    }

    public boolean equals(Object obj) {
    	return obj != null && getSchoolClass() == ((InfoClass) obj).getSchoolClass();
    }

    public String toString() {
    	return getSchoolClass().toString();
    }

    public InfoExecutionDegree getInfoExecutionDegree() {
        return InfoExecutionDegree.newInfoFromDomain(getSchoolClass().getExecutionDegree());
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return InfoExecutionPeriod.newInfoFromDomain(getSchoolClass().getExecutionPeriod());
    }

    public static InfoClass newInfoFromDomain(final SchoolClass schoolClass) {
    	return schoolClass == null ? null : new InfoClass(schoolClass);
    }

	@Override
	public Integer getIdInternal() {
		return getSchoolClass().getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}