/*
 * Created on 17/Ago/2004
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExportGrouping;

/**
 * @author joaosa & rmalo
 */

public class InfoExportGrouping extends InfoObject{


    private InfoGrouping infoGrouping;

    private InfoExecutionCourse infoExecutionCourse;
    
    public InfoExportGrouping() {
        super();
    }

    /**
     * Construtor
     */
    public InfoExportGrouping(InfoGrouping infoGroupProperties,
            InfoExecutionCourse infoExecutionCourse) {

        this.infoGrouping = infoGroupProperties;
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof InfoExportGrouping) {
            result = (getInfoGrouping()
                    .equals(((InfoExportGrouping) arg0)
                            .getInfoGrouping()))
                    && (getInfoExecutionCourse().equals(((InfoExportGrouping) arg0)
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
    public InfoGrouping getInfoGrouping() {
        return infoGrouping;
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
    public void setInfoGrouping(InfoGrouping infoGroupProperties) {
        this.infoGrouping = infoGroupProperties;
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
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.DomainObject)
     */
    public void copyFromDomain(ExportGrouping groupingExecutionCourse) {
        super.copyFromDomain(groupingExecutionCourse);
        this.setInfoGrouping(InfoGrouping.newInfoFromDomain(groupingExecutionCourse.getGrouping()));
        this.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(groupingExecutionCourse.getExecutionCourse()));
    }
    
    public static InfoExportGrouping newInfoFromDomain(ExportGrouping groupingExecutionCourse) {
        InfoExportGrouping infoGroupPropertiesExecutionCourse = null;
        if(groupingExecutionCourse != null) {
            infoGroupPropertiesExecutionCourse = new InfoExportGrouping();
            infoGroupPropertiesExecutionCourse.copyFromDomain(groupingExecutionCourse);
        }
        return infoGroupPropertiesExecutionCourse;
    }
}