/*
 * Created on Nov 23, 2003 by jpvl
 *
 */
package DataBeans.teacher.professorship;

import java.util.List;

import DataBeans.DataTranferObject;
import DataBeans.InfoProfessorship;

/**
 * @author jpvl
 */
public class ProfessorshipSupportLessonsDTO extends DataTranferObject {
    InfoProfessorship infoProfessorship;

    List infoSupportLessonList;

    /**
     * @return Returns the infoProfessorShip.
     */
    public InfoProfessorship getInfoProfessorship() {
        return this.infoProfessorship;
    }

    /**
     * @return Returns the infoSupportLessonList.
     */
    public List getInfoSupportLessonList() {
        return this.infoSupportLessonList;
    }

    /**
     * @param infoProfessorShip
     *            The infoProfessorShip to set.
     */
    public void setInfoProfessorship(InfoProfessorship infoProfessorShip) {
        this.infoProfessorship = infoProfessorShip;
    }

    /**
     * @param infoSupportLessonList
     *            The infoSupportLessonList to set.
     */
    public void setInfoSupportLessonList(List infoSupportLessonList) {
        this.infoSupportLessonList = infoSupportLessonList;
    }

}