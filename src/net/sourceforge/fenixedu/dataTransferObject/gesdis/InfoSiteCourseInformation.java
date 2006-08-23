/*
 * Created on 12/Nov/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.Language;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 */
public class InfoSiteCourseInformation extends DataTranferObject implements ISiteComponent {

    private final static int MIN_LENGTH = 10;

    private InfoExecutionCourse infoExecutionCourse;

    private InfoEvaluationMethod infoEvaluationMethod;

    private List infoSiteEvaluationInformations;

    private List infoCurricularCourses;

    private List infoResponsibleTeachers;

    private List infoCurriculums;

    private List infoLecturingTeachers;

    private List infoLessons;

    private List infoBibliographicReferences;

    private List infoDepartments;

    private InfoCourseReport infoCourseReport;

    private Integer numberOfTheoLessons;

    private Integer numberOfPratLessons;

    private Integer numberOfTheoPratLessons;

    private Integer numberOfLabLessons;

    public InfoSiteCourseInformation() {
    }

    /**
     * @return Returns the infoCourseReport.
     */
    public InfoCourseReport getInfoCourseReport() {
        return infoCourseReport;
    }

    /**
     * @param infoCourseReport
     *            The infoCourseReport to set.
     */
    public void setInfoCourseReport(InfoCourseReport infoCourseReport) {
        this.infoCourseReport = infoCourseReport;
    }

    /**
     * @return Returns the infoExecutionCourse.
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * @param infoExecutionCourse
     *            The infoExecutionCourse to set.
     */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    /**
     * @return Returns the infoBibliographicReferences.
     */
    public List getInfoBibliographicReferences() {
        return infoBibliographicReferences;
    }

    /**
     * @param infoBibliographicReferences
     *            The infoBibliographicReferences to set.
     */
    public void setInfoBibliographicReferences(List infoBibliographicReferences) {
        this.infoBibliographicReferences = infoBibliographicReferences;
    }

    /**
     * @return Returns the infoCurricularCourses.
     */
    public List getInfoCurricularCourses() {
        return infoCurricularCourses;
    }

    /**
     * @param infoCurricularCourses
     *            The infoCurricularCourses to set.
     */
    public void setInfoCurricularCourses(List infoCurricularCourses) {
        this.infoCurricularCourses = infoCurricularCourses;
    }

    /**
     * @return Returns the infoCurriculums.
     */
    public List getInfoCurriculums() {
        return infoCurriculums;
    }

    /**
     * @param infoCurriculums
     *            The infoCurriculums to set.
     */
    public void setInfoCurriculums(List infoCurriculums) {
        this.infoCurriculums = infoCurriculums;
    }

    /**
     * @return Returns the infoLecturingTeacher.
     */
    public List getInfoLecturingTeachers() {
        return infoLecturingTeachers;
    }

    /**
     * @param infoLecturingTeacher
     *            The infoLecturingTeacher to set.
     */
    public void setInfoLecturingTeachers(List infoLecturingTeachers) {
        this.infoLecturingTeachers = infoLecturingTeachers;
    }

    /**
     * @return Returns the infoResponsibleTeachers.
     */
    public List getInfoResponsibleTeachers() {
        return infoResponsibleTeachers;
    }

    /**
     * @param infoResponsibleTeachers
     *            The infoResponsibleTeachers to set.
     */
    public void setInfoResponsibleTeachers(List infoResponsibleTeachers) {
        this.infoResponsibleTeachers = infoResponsibleTeachers;
    }

    /**
     * @return Returns the infoLessons.
     */
    public List getInfoLessons() {
        return infoLessons;
    }

    /**
     * @param infoLessons
     *            The infoLessons to set.
     */
    public void setInfoLessons(List infoLessons) {
        this.infoLessons = infoLessons;
    }

    /**
     * @return Returns the infoEvaluationMethod.
     */
    public InfoEvaluationMethod getInfoEvaluationMethod() {
        return infoEvaluationMethod;
    }

    /**
     * @param infoEvaluationMethod
     *            The infoEvaluationMethod to set.
     */
    public void setInfoEvaluationMethod(InfoEvaluationMethod infoEvaluationMethod) {
        this.infoEvaluationMethod = infoEvaluationMethod;
    }

    public Date getLastModificationDate() {
        List dates = new ArrayList();
        dates.add(infoCourseReport.getLastModificationDate());
        dates.addAll(CollectionUtils.collect(infoCurriculums, new Transformer() {

            public Object transform(Object arg0) {
                InfoCurriculum infoCurriculum = (InfoCurriculum) arg0;
                return infoCurriculum.getLastModificationDate();
            }
        }));
        return getMostRecentDate(dates);
    }

    /**
     * @param dates
     * @return
     */
    private Date getMostRecentDate(List dates) {
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = minDate;
        Iterator iter = dates.iterator();
        while (iter.hasNext()) {
            Date date = (Date) iter.next();
            if (date == null)
                continue;
            if (date.getTime() > maxDate.getTime())
                maxDate = date;
        }
        // if the minDate is equal to maxDate then the information wasn't filled
        if (minDate == maxDate)
            maxDate = null;
        return maxDate;
    }

    /**
     * @return Returns the numberOfLabLessons.
     */
    public Integer getNumberOfLabLessons() {
        return numberOfLabLessons;
    }

    /**
     * @param numberOfLabLessons
     *            The numberOfLabLessons to set.
     */
    public void setNumberOfLabLessons(Integer numberOfLabLessons) {
        this.numberOfLabLessons = numberOfLabLessons;
    }

    /**
     * @return Returns the numberOfPratLessons.
     */
    public Integer getNumberOfPratLessons() {
        return numberOfPratLessons;
    }

    /**
     * @param numberOfPratLessons
     *            The numberOfPratLessons to set.
     */
    public void setNumberOfPratLessons(Integer numberOfPratLessons) {
        this.numberOfPratLessons = numberOfPratLessons;
    }

    /**
     * @return Returns the numberOfTheoLessons.
     */
    public Integer getNumberOfTheoLessons() {
        return numberOfTheoLessons;
    }

    /**
     * @param numberOfTheoLessons
     *            The numberOfTheoLessons to set.
     */
    public void setNumberOfTheoLessons(Integer numberOfTheoLessons) {
        this.numberOfTheoLessons = numberOfTheoLessons;
    }

    /**
     * @return Returns the numberOfTheoPratLessons.
     */
    public Integer getNumberOfTheoPratLessons() {
        return numberOfTheoPratLessons;
    }

    /**
     * @param numberOfTheoPratLessons
     *            The numberOfTheoPratLessons to set.
     */
    public void setNumberOfTheoPratLessons(Integer numberOfTheoPratLessons) {
        this.numberOfTheoPratLessons = numberOfTheoPratLessons;
    }

    /**
     * @return Returns the infoDepartments.
     */
    public List getInfoDepartments() {
        return infoDepartments;
    }

    /**
     * @param infoDepartments
     *            The infoDepartments to set.
     */
    public void setInfoDepartments(List infoDepartments) {
        this.infoDepartments = infoDepartments;
    }

    /**
     * @return Returns the infoSiteEvaluationInformations.
     */
    public List getInfoSiteEvaluationInformations() {
        return infoSiteEvaluationInformations;
    }

    /**
     * @param infoSiteEvaluationInformations
     *            The infoSiteEvaluationInformations to set.
     */
    public void setInfoSiteEvaluationInformations(List infoSiteEvaluationInformations) {
        this.infoSiteEvaluationInformations = infoSiteEvaluationInformations;
    }

    public Integer getNumberOfFieldsFilled() {
        int numberOfFieldsFilled = 0;

        if (!this.infoLecturingTeachers.isEmpty())
            numberOfFieldsFilled++;
        Iterator iter = this.infoBibliographicReferences.iterator();
        while (iter.hasNext()) {
            InfoBibliographicReference infoBibliographicReference = (InfoBibliographicReference) iter
                    .next();
            if (infoBibliographicReference.getTitle() != null
                    && infoBibliographicReference.getTitle().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }
        if (this.infoEvaluationMethod.getEvaluationElements() != null
                && this.infoEvaluationMethod.getEvaluationElements().getContent(Language.pt).length() > MIN_LENGTH)
            numberOfFieldsFilled++;

        iter = this.infoCurriculums.iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getGeneralObjectives() != null
                    && infoCurriculum.getGeneralObjectives().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        iter = this.infoCurriculums.iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getProgram() != null && infoCurriculum.getProgram().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        return new Integer(numberOfFieldsFilled);
    }

    public Integer getNumberOfFieldsFilledEn() {
        int numberOfFieldsFilled = 0;

        if (!this.infoLecturingTeachers.isEmpty())
            numberOfFieldsFilled++;
        Iterator iter = this.infoBibliographicReferences.iterator();
        while (iter.hasNext()) {
            InfoBibliographicReference infoBibliographicReference = (InfoBibliographicReference) iter
                    .next();
            if (infoBibliographicReference.getTitle().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }
        if (this.infoEvaluationMethod.getEvaluationElements().getContent(Language.en) != null
                && this.infoEvaluationMethod.getEvaluationElements().getContent(Language.en).length() > MIN_LENGTH)
            numberOfFieldsFilled++;

        iter = this.infoCurriculums.iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getGeneralObjectivesEn() != null
                    && infoCurriculum.getGeneralObjectivesEn().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        iter = this.infoCurriculums.iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getOperacionalObjectivesEn() != null
                    && infoCurriculum.getOperacionalObjectivesEn().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        iter = this.infoCurriculums.iterator();
        while (iter.hasNext()) {
            InfoCurriculum infoCurriculum = (InfoCurriculum) iter.next();
            if (infoCurriculum.getProgramEn() != null
                    && infoCurriculum.getProgramEn().length() > MIN_LENGTH) {
                numberOfFieldsFilled++;
                break;
            }
        }

        return new Integer(numberOfFieldsFilled);
    }
}