/*
 * Created on Nov 19, 2003 by jpvl
 *  
 */
package DataBeans.credits;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import DataBeans.DataTranferObject;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftProfessorship;
import DataBeans.teacher.professorship.InfoSupportLesson;

/**
 * @author jpvl
 */
public class TeacherCreditsSheetDTO extends DataTranferObject {

    private List detailedProfessorshipList;

    private List infoCreditLineList;

    private InfoCredits infoCredits;

    private InfoExecutionPeriod infoExecutionPeriod;

    private List infoManagementPositions;

    private List infoMasterDegreeProfessorships;

    private List infoServiceExemptions;

    private List infoShiftProfessorshipList;

    private List infoSupportLessonList;

    private InfoTeacher infoTeacher;

    private List infoTeacherDegreeFinalProjectStudentList;

    private List infoTeacherInstitutionWorkingTimeList;

    private List infoTeacherOtherTypeCreditLineList;

    /**
     * @return Returns the infoProfessorshipList.
     */
    public List getDetailedProfessorshipList() {
        BeanComparator executionCourseName = new BeanComparator(
                "infoProfessorship.infoExecutionCourse.nome");
        Collections.sort(this.detailedProfessorshipList, executionCourseName);
        return this.detailedProfessorshipList;
    }

    public List getExecutionCourseShiftProfessorship(final String executionCourseCode) {
        List executionCourseShiftProfessorships = (List) CollectionUtils.select(this
                .getInfoShiftProfessorshipList(), new Predicate() {

            public boolean evaluate(Object input) {
                InfoShiftProfessorship infoShiftProfessorship = (InfoShiftProfessorship) input;
                return infoShiftProfessorship.getInfoProfessorship().getInfoExecutionCourse().getSigla()
                        .equals(executionCourseCode);
            }
        });
        return executionCourseShiftProfessorships;
    }

    public List getExecutionCourseSupportLessons(final String executionCourseCode) {
        List executionCourseSupportLessons = (List) CollectionUtils.select(this
                .getInfoSupportLessonList(), new Predicate() {

            public boolean evaluate(Object input) {
                InfoSupportLesson infoSupportLesson = (InfoSupportLesson) input;
                return infoSupportLesson.getInfoProfessorship().getInfoExecutionCourse().getSigla()
                        .equals(executionCourseCode);
            }
        });
        return executionCourseSupportLessons;
    }

    /**
     * @return Returns the infoCreditLineList.
     */
    public List getInfoCreditLineList() {
        return this.infoCreditLineList;
    }

    /**
     * @return Returns the infoCredits.
     */
    public InfoCredits getInfoCredits() {
        return infoCredits;
    }

    /**
     * @return Returns the infoExecutionPeriod.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return this.infoExecutionPeriod;
    }

    /**
     * @return Returns the infoManagementPositions.
     */
    public List getInfoManagementPositions() {
        return infoManagementPositions;
    }

    /**
     * @return Returns the infoMasterDegreeProfessorships.
     */
    public List getInfoMasterDegreeProfessorships() {
        return this.infoMasterDegreeProfessorships;
    }

    /**
     * @return Returns the infoServiceExemptions.
     */
    public List getInfoServiceExemptions() {
        return infoServiceExemptions;
    }

    /**
     * @return Returns the infoShiftProfessorshipList.
     */
    public List getInfoShiftProfessorshipList() {
        return this.infoShiftProfessorshipList;
    }

    /**
     * @return Returns the infoSupportLessonList.
     */
    public List getInfoSupportLessonList() {
        return this.infoSupportLessonList;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher() {
        return this.infoTeacher;
    }

    /**
     * @return Returns the infoTeacherDegreeFinalProjectStudentList.
     */
    public List getInfoTeacherDegreeFinalProjectStudentList() {
        return this.infoTeacherDegreeFinalProjectStudentList;
    }

    /**
     * @return Returns the infoTeacherInstitutionWorkingTimeList.
     */
    public List getInfoTeacherInstitutionWorkingTimeList() {
        return this.infoTeacherInstitutionWorkingTimeList;
    }

    /**
     * @return Returns the infoTeacherOtherTypeCreditLineList.
     */
    public List getInfoTeacherOtherTypeCreditLineList() {
        return infoTeacherOtherTypeCreditLineList;
    }

    /**
     * @param infoProfessorshipList
     *            The infoProfessorshipList to set.
     */
    public void setDetailedProfessorshipList(List detailedProfessorshipList) {
        this.detailedProfessorshipList = detailedProfessorshipList;
    }

    /**
     * @param infoCreditLineList
     *            The infoCreditLineList to set.
     */
    public void setInfoCreditLineList(List infoCreditLineList) {
        this.infoCreditLineList = infoCreditLineList;
    }

    /**
     * @param infoCredits
     *            The infoCredits to set.
     */
    public void setInfoCredits(InfoCredits infoCredits) {
        this.infoCredits = infoCredits;
    }

    /**
     * @param infoExecutionPeriod
     *            The infoExecutionPeriod to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
     * @param infoManagementPositions
     */
    public void setInfoManagementPositions(List infoManagementPositions) {
        this.infoManagementPositions = infoManagementPositions;
    }

    /**
     * @param infoMasterDegreeProfessorships
     *            The infoMasterDegreeProfessorships to set.
     */
    public void setInfoMasterDegreeProfessorships(List infoMasterDegreeProfessorships) {
        this.infoMasterDegreeProfessorships = infoMasterDegreeProfessorships;
    }

    /**
     * @param infoServiceExemptions
     */
    public void setInfoServiceExemptions(List infoServiceExemptions) {
        this.infoServiceExemptions = infoServiceExemptions;
    }

    /**
     * @param infoShiftProfessorshipList
     *            The infoShiftProfessorshipList to set.
     */
    public void setInfoShiftProfessorshipList(List infoShiftProfessorshipList) {
        this.infoShiftProfessorshipList = infoShiftProfessorshipList;
    }

    /**
     * @param infoSupportLessonList
     *            The infoSupportLessonList to set.
     */
    public void setInfoSupportLessonList(List infoSupportLessonList) {
        this.infoSupportLessonList = infoSupportLessonList;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher) {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @param infoTeacherDegreeFinalProjectStudentList
     *            The infoTeacherDegreeFinalProjectStudentList to set.
     */
    public void setInfoTeacherDegreeFinalProjectStudentList(List infoTeacherDegreeFinalProjectStudentList) {
        this.infoTeacherDegreeFinalProjectStudentList = infoTeacherDegreeFinalProjectStudentList;
    }

    /**
     * @param infoTeacherInstitutionWorkingTimeList
     *            The infoTeacherInstitutionWorkingTimeList to set.
     */
    public void setInfoTeacherInstitutionWorkingTimeList(List infoTeacherInstitutionWorkingTimeList) {
        this.infoTeacherInstitutionWorkingTimeList = infoTeacherInstitutionWorkingTimeList;
    }

    /**
     * @param infoTeacherOtherTypeCreditLineList
     *            The infoTeacherOtherTypeCreditLineList to set.
     */
    public void setInfoTeacherOtherTypeCreditLineList(List infoTeacherOtherTypeCreditLineList) {
        this.infoTeacherOtherTypeCreditLineList = infoTeacherOtherTypeCreditLineList;
    }

}