/*
 * Created on 14/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IProfessorship;

/**
 * @author jpvl
 */
public class InfoProfessorship extends InfoObject {

    protected InfoTeacher infoTeacher;

    protected InfoExecutionCourse infoExecutionCourse;

    private Double hours;

    private String toDelete;

    public InfoProfessorship() {
    }

    /**
     * @param object
     */
    public InfoProfessorship(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * @return
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * @return
     */
    public InfoTeacher getInfoTeacher() {
        return infoTeacher;
    }

    /**
     * @param course
     */
    public void setInfoExecutionCourse(InfoExecutionCourse course) {
        infoExecutionCourse = course;
    }

    /**
     * @param teacher
     */
    public void setInfoTeacher(InfoTeacher teacher) {
        infoTeacher = teacher;
    }

    /**
     * @return Returns the credits.
     */
    public Double getHours() {
        return this.hours;
    }

    /**
     * @param credits
     *            The credits to set.
     */
    public void setHours(Double hours) {
        this.hours = hours;
    }

    /**
     * @return Returns the toDelete.
     */
    public String getToDelete() {
        return toDelete;
    }

    /**
     * @param toDelete
     *            The toDelete to set.
     */
    public void setToDelete(String toDelete) {
        this.toDelete = toDelete;
    }

    public void copyFromDomain(IProfessorship professorship) {
        super.copyFromDomain(professorship);
        if (professorship != null) {
            setHours(professorship.getHours());
        }
    }

    public static InfoProfessorship newInfoFromDomain(IProfessorship professorship) {
        InfoProfessorship infoProfessorship = null;
        if (professorship != null) {
            infoProfessorship = new InfoProfessorship();
            infoProfessorship.copyFromDomain(professorship);
        }
        return infoProfessorship;
    }

}