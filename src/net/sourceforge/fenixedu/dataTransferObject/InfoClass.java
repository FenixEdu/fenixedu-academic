/*
 * InfoClass.java
 * 
 * Created on 31 de Outubro de 2002, 12:27
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.SchoolClass;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class InfoClass extends InfoObject {

	private final SchoolClass schoolClass;

    public InfoClass(final SchoolClass schoolClass) {
    	this.schoolClass = schoolClass;
    }

    public String getNome() {
        return schoolClass.getNome();
    }

    public Integer getAnoCurricular() {
        return schoolClass.getAnoCurricular();
    }

    public boolean equals(Object obj) {
    	return obj != null && schoolClass == ((InfoClass) obj).schoolClass;
    }

    public String toString() {
    	return schoolClass.toString();
    }

    public InfoExecutionDegree getInfoExecutionDegree() {
        return InfoExecutionDegree.newInfoFromDomain(schoolClass.getExecutionDegree());
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return InfoExecutionPeriod.newInfoFromDomain(schoolClass.getExecutionPeriod());
    }

    public static InfoClass newInfoFromDomain(final SchoolClass schoolClass) {
    	return schoolClass == null ? null : new InfoClass(schoolClass);
    }

	@Override
	public Integer getIdInternal() {
		return schoolClass.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}