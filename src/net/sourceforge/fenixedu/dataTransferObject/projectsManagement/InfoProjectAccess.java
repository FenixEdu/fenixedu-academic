/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import java.util.Calendar;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectAccess;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoProjectAccess extends InfoObject {
    private InfoPerson infoPerson;

    private Integer projectCoordinator;

    private Integer keyProject;

    private InfoProject infoProject;

    private Calendar beginDate;

    private Calendar endDate;

    public Calendar getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Calendar beginDate) {
        this.beginDate = beginDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    public void setInfoPerson(InfoPerson infoPerson) {
        this.infoPerson = infoPerson;
    }

    public InfoProject getInfoProject() {
        return infoProject;
    }

    public void setInfoProject(InfoProject infoProject) {
        this.infoProject = infoProject;
    }

    public Integer getKeyProject() {
        return keyProject;
    }

    public void setKeyProject(Integer keyProject) {
        this.keyProject = keyProject;
    }

    public Integer getProjectCoordinator() {
        return projectCoordinator;
    }

    public void setProjectCoordinator(Integer projectCoordinator) {
        this.projectCoordinator = projectCoordinator;
    }

    public String getBeginDateFormatted() {
        String result = new String();
        Calendar date = getBeginDate();
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        return result;
    }

    public String getEndDateFormatted() {
        String result = new String();
        Calendar date = getEndDate();
        result += date.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += date.get(Calendar.MONTH) + 1;
        result += "/";
        result += date.get(Calendar.YEAR);
        return result;
    }

    public void copyFromDomain(IProjectAccess projectAccess) {
        super.copyFromDomain(projectAccess);
        if (projectAccess != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(projectAccess.getPerson()));
            setProjectCoordinator(projectAccess.getKeyProjectCoordinator());
            setKeyProject(projectAccess.getKeyProject());
            setBeginDate(projectAccess.getBeginDate());
            setEndDate(projectAccess.getEndDate());
        }
    }

    public static InfoProjectAccess newInfoFromDomain(IProjectAccess projectAccess) {
        InfoProjectAccess infoProjectAccess = null;
        if (projectAccess != null) {
            infoProjectAccess = new InfoProjectAccess();
            infoProjectAccess.copyFromDomain(projectAccess);
        }
        return infoProjectAccess;
    }
}
