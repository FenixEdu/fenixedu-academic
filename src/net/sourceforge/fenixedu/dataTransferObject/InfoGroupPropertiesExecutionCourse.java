/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IGroupPropertiesExecutionCourse;

/**
 * @author joaosa & rmalo
 */

public class InfoGroupPropertiesExecutionCourse extends InfoObject{


    private InfoGroupProperties infoGroupProperties;

    private InfoExecutionCourse infoExecutionCourse;
    
    public InfoGroupPropertiesExecutionCourse() {
        super();
    }

    /**
     * Construtor
     */
    public InfoGroupPropertiesExecutionCourse(InfoGroupProperties infoGroupProperties,
            InfoExecutionCourse infoExecutionCourse) {

        this.infoGroupProperties = infoGroupProperties;
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof InfoGroupPropertiesExecutionCourse) {
            result = (getInfoGroupProperties()
                    .equals(((InfoGroupPropertiesExecutionCourse) arg0)
                            .getInfoGroupProperties()))
                    && (getInfoExecutionCourse().equals(((InfoGroupPropertiesExecutionCourse) arg0)
                            .getInfoExecutionCourse()));
        }
        return result;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String result = "[INFO_GROUP_PROPERTIES_EXECUTION_COURSE";
        result += "]";
        return result;
    }

    /**
     * @return InfoGroupProperties
     */
    public InfoGroupProperties getInfoGroupProperties() {
        return infoGroupProperties;
    }

    /**
     * @return InfoExecutionCourse
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * Sets the infoGroupProperties.
     * 
     * @param infoGroupProperties
     *            The infoGroupProperties to set
     */
    public void setInfoGroupProperties(InfoGroupProperties infoGroupProperties) {
        this.infoGroupProperties = infoGroupProperties;
    }

    /**
     * Sets the infoExecutionCourse.
     * 
     * @param infoExecutionCourse
     *            The infoExecutionCourse to set
     */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        super.copyFromDomain(groupPropertiesExecutionCourse);
        InfoGroupProperties infoGroupProperties = new InfoGroupProperties();
        InfoExecutionCourse infoExecutionCourse = new InfoExecutionCourse();
        infoGroupProperties.copyFromDomain(groupPropertiesExecutionCourse.getGroupProperties());
        infoExecutionCourse.copyFromDomain(groupPropertiesExecutionCourse.getExecutionCourse());
        this.setInfoGroupProperties(infoGroupProperties);
        this.setInfoExecutionCourse(infoExecutionCourse);
    }
    
    public static InfoGroupPropertiesExecutionCourse newInfoFromDomain(IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse) {
        InfoGroupPropertiesExecutionCourse infoGroupPropertiesExecutionCourse = null;
        if(groupPropertiesExecutionCourse != null) {
            infoGroupPropertiesExecutionCourse = new InfoGroupPropertiesExecutionCourse();
            infoGroupPropertiesExecutionCourse.copyFromDomain(groupPropertiesExecutionCourse);
        }
        return infoGroupPropertiesExecutionCourse;
    }
}