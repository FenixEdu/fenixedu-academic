/*
 * Created on 13/Nov/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class InfoProfessionalCareer extends InfoCareer {
    private String entity;

    private String function;

    /**
     *  
     */
    public InfoProfessionalCareer() {
	setCareerType(CareerType.PROFESSIONAL);
    }

    /**
     * @return Returns the entity.
     */
    public String getEntity() {
	return entity;
    }

    /**
     * @param entity
     *            The entity to set.
     */
    public void setEntity(String entity) {
	this.entity = entity;
    }

    /**
     * @return Returns the function.
     */
    public String getFunction() {
	return function;
    }

    /**
     * @param function
     *            The function to set.
     */
    public void setFunction(String function) {
	this.function = function;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer#copyFromDomain
     * (Dominio.teacher.Career)
     */
    public void copyFromDomain(ProfessionalCareer professionalCareer) {
	super.copyFromDomain(professionalCareer);
	if (professionalCareer != null) {
	    setEntity(professionalCareer.getEntity());
	    setFunction(professionalCareer.getFunction());
	    setInfoTeacher(InfoTeacher.newInfoFromDomain(professionalCareer.getTeacher()));
	}
    }

    public static InfoProfessionalCareer newInfoFromDomain(ProfessionalCareer professionalCareer) {
	InfoProfessionalCareer infoProfessionalCareer = null;
	if (professionalCareer != null) {
	    infoProfessionalCareer = new InfoProfessionalCareer();
	    infoProfessionalCareer.copyFromDomain(professionalCareer);
	}

	return infoProfessionalCareer;
    }
}