/*
 * Created on 13/Nov/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.teacher;

import net.sourceforge.fenixedu.domain.teacher.IProfessionalCareer;
import net.sourceforge.fenixedu.util.CareerType;

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
     * @see DataBeans.teacher.InfoCareer#copyFromDomain(Dominio.teacher.ICareer)
     */
    public void copyFromDomain(IProfessionalCareer professionalCareer) {
        super.copyFromDomain(professionalCareer);
        if (professionalCareer != null) {
            setEntity(professionalCareer.getEntity());
            setFunction(professionalCareer.getFunction());
        }
    }

    public static InfoProfessionalCareer newInfoFromDomain(IProfessionalCareer professionalCareer) {
        InfoProfessionalCareer infoProfessionalCareer = null;
        if (professionalCareer != null) {
            infoProfessionalCareer = new InfoProfessionalCareer();
            infoProfessionalCareer.copyFromDomain(professionalCareer);
        }

        return infoProfessionalCareer;
    }
}