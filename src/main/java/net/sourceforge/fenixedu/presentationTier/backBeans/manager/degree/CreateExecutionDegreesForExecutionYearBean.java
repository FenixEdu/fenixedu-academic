/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.manager.degree;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.faces.component.UISelectItems;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateExecutionDegreesForExecutionYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.Data;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;

import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class CreateExecutionDegreesForExecutionYearBean extends FenixBackingBean {
    private String chosenDegreeType;

    private String[] choosenDegreeCurricularPlansIDs;

    private String[] choosenBolonhaDegreeCurricularPlansIDs;

    private UISelectItems degreeCurricularPlansSelectItems;

    public UISelectItems bolonhaDegreeCurricularPlansSelectItems;

    private String campus;

    private Boolean temporaryExamMap;

    private Integer lessonSeason1BeginDay;

    private Integer lessonSeason1BeginMonth;

    private Integer lessonSeason1EndDay;

    private Integer lessonSeason1EndMonth;

    private Integer lessonSeason2BeginDay;

    private Integer lessonSeason2BeginMonth;

    private Integer lessonSeason2EndDay;

    private Integer lessonSeason2EndMonth;

    private Integer lessonSeason2BeginDayPart2;

    private Integer lessonSeason2BeginMonthPart2;

    private Integer lessonSeason2EndDayPart2;

    private Integer lessonSeason2EndMonthPart2;

    private Integer examsSeason1BeginDay;

    private Integer examsSeason1BeginMonth;

    private Integer examsSeason1EndDay;

    private Integer examsSeason1EndMonth;

    private Integer examsSeason2BeginDay;

    private Integer examsSeason2BeginMonth;

    private Integer examsSeason2EndDay;

    private Integer examsSeason2EndMonth;

    private Integer examsSpecialSeasonBeginDay;

    private Integer examsSpecialSeasonBeginMonth;

    private Integer examsSpecialSeasonEndDay;

    private Integer examsSpecialSeasonEndMonth;

    private Integer gradeSubmissionNormalSeason1EndDay;

    private Integer gradeSubmissionNormalSeason1EndMonth;

    private Integer gradeSubmissionNormalSeason2EndDay;

    private Integer gradeSubmissionNormalSeason2EndMonth;

    private Integer gradeSubmissionSpecialSeasonEndDay;

    private Integer gradeSubmissionSpecialSeasonEndMonth;

    private List<DegreeCurricularPlan> createdDegreeCurricularPlans;

    public CreateExecutionDegreesForExecutionYearBean() {
        super();
    }

    public List<SelectItem> getDegreeTypes() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem("dropDown.Default", BundleUtil.getString(Bundle.ENUMERATION, "dropDown.Default")));
        for (final DegreeType bolonhaDegreeType : DegreeType.values()) {
            result.add(new SelectItem(bolonhaDegreeType.name(),
                    BundleUtil.getString(Bundle.ENUMERATION, bolonhaDegreeType.name())));
        }

        return result;
    }

    public String getChosenDegreeType() {
        return chosenDegreeType;
    }

    public void setChosenDegreeType(String chosenDegreeType) {
        this.chosenDegreeType = chosenDegreeType;
    }

    public UISelectItems getDegreeCurricularPlansSelectItems() {
        if (this.degreeCurricularPlansSelectItems == null) {
            final DegreeType degreeType = getDegreeType(getChosenDegreeType());

            final List<SelectItem> result;
            if (degreeType == null) {
                result = Collections.EMPTY_LIST;
            } else {
                result = new ArrayList<SelectItem>();

                final List<DegreeCurricularPlan> toShow =
                        DegreeCurricularPlan.readByDegreeTypeAndState(degreeType, DegreeCurricularPlanState.ACTIVE);
                Collections
                        .sort(toShow,
                                DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

                for (final DegreeCurricularPlan degreeCurricularPlan : toShow) {
                    result.add(new SelectItem(degreeCurricularPlan.getExternalId(), BundleUtil.getString(Bundle.ENUMERATION,
                            degreeType.getName())
                            + " "
                            + degreeCurricularPlan.getDegree().getName()
                            + " - "
                            + degreeCurricularPlan.getName()));
                }
            }
            this.degreeCurricularPlansSelectItems = new UISelectItems();
            this.degreeCurricularPlansSelectItems.setValue(result);
        }

        return this.degreeCurricularPlansSelectItems;
    }

    public void setDegreeCurricularPlansSelectItems(UISelectItems degreeCurricularPlansSelectItems) {
        this.degreeCurricularPlansSelectItems = degreeCurricularPlansSelectItems;
    }

    public UISelectItems getBolonhaDegreeCurricularPlansSelectItems() {
        if (this.bolonhaDegreeCurricularPlansSelectItems == null) {
            final DegreeType bolonhaDegreeType = DegreeType.valueOf(getChosenDegreeType());

            final List<DegreeCurricularPlan> toShow = new ArrayList<DegreeCurricularPlan>();
            for (final Degree degree : Degree.readBolonhaDegrees()) {
                if (degree.getDegreeType() == bolonhaDegreeType) {
                    for (final DegreeCurricularPlan degreeCurricularPlan : degree.getActiveDegreeCurricularPlans()) {
                        if (!degreeCurricularPlan.isDraft()) {
                            toShow.add(degreeCurricularPlan);
                        }
                    }
                }
            }
            Collections.sort(toShow,
                    DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

            final List<SelectItem> result = new ArrayList<SelectItem>();
            for (final DegreeCurricularPlan degreeCurricularPlan : toShow) {
                result.add(new SelectItem(degreeCurricularPlan.getExternalId(), BundleUtil.getString(Bundle.ENUMERATION,
                        bolonhaDegreeType.getName())
                        + " "
                        + degreeCurricularPlan.getDegree().getName()
                        + " - "
                        + degreeCurricularPlan.getName()));
            }

            this.bolonhaDegreeCurricularPlansSelectItems = new UISelectItems();
            this.bolonhaDegreeCurricularPlansSelectItems.setValue(result);
        }
        return this.bolonhaDegreeCurricularPlansSelectItems;
    }

    public void setBolonhaDegreeCurricularPlansSelectItems(UISelectItems bolonhaDegreeCurricularPlansSelectItems) {
        this.bolonhaDegreeCurricularPlansSelectItems = bolonhaDegreeCurricularPlansSelectItems;
    }

    private DegreeType getDegreeType(final String chosenDegreeType) {
        return DegreeType.valueOf(chosenDegreeType);
    }

    public List getExecutionYears() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
            result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
        }
        if (getChoosenExecutionYearID() == null && result.size() > 0) {
            setChoosenExecutionYearID(ExecutionYear.readCurrentExecutionYear().getExternalId());
            setYearsAccordingToChosenExecutionYear();
        }
        return result;
    }

    public void onChoosenExecutionYearChanged(ValueChangeEvent valueChangeEvent) {
        setChoosenExecutionYearID((String) valueChangeEvent.getNewValue());

        setYearsAccordingToChosenExecutionYear();
    }

    private void setYearsAccordingToChosenExecutionYear() {
        int beginYear = getChoosenExecutionYear().getBeginDateYearMonthDay().getYear();
        int endYear = getChoosenExecutionYear().getEndDateYearMonthDay().getYear();

        setLessonSeason1BeginYear(beginYear);
        setLessonSeason1EndYear(beginYear);
        setExamsSeason1BeginYear(endYear);
        setExamsSeason1EndYear(endYear);
        setGradeSubmissionNormalSeason1EndYear(endYear);

        setLessonSeason2BeginYear(endYear);
        setLessonSeason2EndYear(endYear);
        setExamsSeason2BeginYear(endYear);
        setExamsSeason2EndYear(endYear);
        setGradeSubmissionNormalSeason2EndYear(endYear);

        setExamsSpecialSeasonBeginYear(endYear);
        setExamsSpecialSeasonEndYear(endYear);
        setGradeSubmissionSpecialSeasonEndYear(endYear);
    }

    public List getAllCampus() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final Space campus : Space.getAllCampus()) {
            result.add(new SelectItem(campus.getName(), campus.getName()));
        }
        return result;
    }

    public String createExecutionDegrees() throws FenixActionException {

        Calendar lessonSeason1BeginDate = Calendar.getInstance();
        lessonSeason1BeginDate
                .set(getLessonSeason1BeginYear(), getLessonSeason1BeginMonth(), getLessonSeason1BeginDay(), 0, 0, 0);

        Calendar lessonSeason1EndDate = Calendar.getInstance();
        lessonSeason1EndDate.set(getLessonSeason1EndYear(), getLessonSeason1EndMonth(), getLessonSeason1EndDay(), 23, 59, 59);

        Calendar lessonSeason2BeginDate = Calendar.getInstance();
        lessonSeason2BeginDate
                .set(getLessonSeason2BeginYear(), getLessonSeason2BeginMonth(), getLessonSeason2BeginDay(), 0, 0, 0);

        Calendar lessonSeason2EndDate = Calendar.getInstance();
        lessonSeason2EndDate.set(getLessonSeason2EndYear(), getLessonSeason2EndMonth(), getLessonSeason2EndDay(), 23, 59, 59);

        Calendar lessonSeason2BeginDatePart2 = Calendar.getInstance();
        lessonSeason2BeginDatePart2.set(getLessonSeason2BeginYear(), getLessonSeason2BeginMonthPart2(),
                getLessonSeason2BeginDayPart2(), 0, 0, 0);

        Calendar lessonSeason2EndDatePart2 = Calendar.getInstance();
        lessonSeason2EndDatePart2.set(getLessonSeason2EndYear(), getLessonSeason2EndMonthPart2(), getLessonSeason2EndDayPart2(),
                23, 59, 59);

        Calendar examsSeason1BeginDate = Calendar.getInstance();
        examsSeason1BeginDate.set(getExamsSeason1BeginYear(), getExamsSeason1BeginMonth(), getExamsSeason1BeginDay(), 0, 0, 0);

        Calendar examsSeason1EndDate = Calendar.getInstance();
        examsSeason1EndDate.set(getExamsSeason1EndYear(), getExamsSeason1EndMonth(), getExamsSeason1EndDay(), 23, 59, 59);

        Calendar examsSeason2BeginDate = Calendar.getInstance();
        examsSeason2BeginDate.set(getExamsSeason2BeginYear(), getExamsSeason2BeginMonth(), getExamsSeason2BeginDay(), 0, 0, 0);

        Calendar examsSeason2EndDate = Calendar.getInstance();
        examsSeason2EndDate.set(getExamsSeason2EndYear(), getExamsSeason2EndMonth(), getExamsSeason2EndDay(), 23, 59, 59);

        Calendar examsSpecialSeasonBeginDate = Calendar.getInstance();
        examsSpecialSeasonBeginDate.set(getExamsSpecialSeasonBeginYear(), getExamsSpecialSeasonBeginMonth(),
                getExamsSpecialSeasonBeginDay(), 0, 0, 0);

        Calendar examsSpecialSeasonEndDate = Calendar.getInstance();
        examsSpecialSeasonEndDate.set(getExamsSpecialSeasonEndYear(), getExamsSpecialSeasonEndMonth(),
                getExamsSpecialSeasonEndDay(), 23, 59, 59);

        Calendar gradeSubmissionNormalSeason1EndDate = Calendar.getInstance();
        gradeSubmissionNormalSeason1EndDate.set(getGradeSubmissionNormalSeason1EndYear(),
                getGradeSubmissionNormalSeason1EndMonth(), getGradeSubmissionNormalSeason1EndDay(), 23, 59, 59);

        Calendar gradeSubmissionNormalSeason2EndDate = Calendar.getInstance();
        gradeSubmissionNormalSeason2EndDate.set(getGradeSubmissionNormalSeason2EndYear(),
                getGradeSubmissionNormalSeason2EndMonth(), getGradeSubmissionNormalSeason2EndDay(), 23, 59, 59);

        Calendar gradeSubmissionSpecialSeasonEndDate = Calendar.getInstance();
        gradeSubmissionSpecialSeasonEndDate.set(getGradeSubmissionSpecialSeasonEndYear(),
                getGradeSubmissionSpecialSeasonEndMonth(), getGradeSubmissionSpecialSeasonEndDay(), 23, 59, 59);

        try {
            createdDegreeCurricularPlans =
                    CreateExecutionDegreesForExecutionYear.run(getChoosenDegreeCurricularPlansIDs(),
                            getChoosenBolonhaDegreeCurricularPlansIDs(), getChoosenExecutionYearID(), getCampus(),
                            !getTemporaryExamMap(), lessonSeason1BeginDate, lessonSeason1EndDate, lessonSeason2BeginDate,
                            lessonSeason2EndDate, lessonSeason2BeginDatePart2, lessonSeason2EndDatePart2, examsSeason1BeginDate,
                            examsSeason1EndDate, examsSeason2BeginDate, examsSeason2EndDate, examsSpecialSeasonBeginDate,
                            examsSpecialSeasonEndDate, gradeSubmissionNormalSeason1EndDate, gradeSubmissionNormalSeason2EndDate,
                            gradeSubmissionSpecialSeasonEndDate);
        } catch (IllegalDataAccessException e) {
            throw new FenixActionException(e);
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getKey(), e.getArgs()));
            setChoosenDegreeCurricularPlansIDs(null);
            return "";
        }

        return "success";
    }

    public List<DegreeCurricularPlan> getCreatedDegreeCurricularPlans() {
        return createdDegreeCurricularPlans;
    }

    public List getDays() {
        return Data.getMonthDaysSelectItems();
    }

    public List getMonths() {
        return Data.getMonthsSelectItems();
    }

    public List getYears() {
        return Data.getExpirationYearsSelectItems();
    }

    public String[] getChoosenDegreeCurricularPlansIDs() {
        return choosenDegreeCurricularPlansIDs;
    }

    public void setChoosenDegreeCurricularPlansIDs(String[] choosenDegreeCurricularPlansIDs) {
        this.choosenDegreeCurricularPlansIDs = choosenDegreeCurricularPlansIDs;
    }

    public String[] getChoosenBolonhaDegreeCurricularPlansIDs() {
        return choosenBolonhaDegreeCurricularPlansIDs;
    }

    public void setChoosenBolonhaDegreeCurricularPlansIDs(String[] choosenBolonhaDegreeCurricularPlansIDs) {
        this.choosenBolonhaDegreeCurricularPlansIDs = choosenBolonhaDegreeCurricularPlansIDs;
    }

    public ExecutionYear getChoosenExecutionYear() {
        return FenixFramework.getDomainObject(getChoosenExecutionYearID());
    }

    public String getChoosenExecutionYearID() {
        return (String) this.getViewState().getAttribute("choosenExecutionYearID");
    }

    public void setChoosenExecutionYearID(String choosenExecutionYearID) {
        this.getViewState().setAttribute("choosenExecutionYearID", choosenExecutionYearID);
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public Integer getExamsSeason1BeginDay() {
        return examsSeason1BeginDay;
    }

    public void setExamsSeason1BeginDay(Integer examsSeason1BeginDay) {
        this.examsSeason1BeginDay = examsSeason1BeginDay;
    }

    public Integer getExamsSeason1BeginMonth() {
        return examsSeason1BeginMonth;
    }

    public void setExamsSeason1BeginMonth(Integer examsSeason1BeginMonth) {
        this.examsSeason1BeginMonth = examsSeason1BeginMonth;
    }

    public Integer getExamsSeason1BeginYear() {
        return (Integer) this.getViewState().getAttribute("examsSeason1BeginYear");
    }

    public void setExamsSeason1BeginYear(Integer examsSeason1BeginYear) {
        this.getViewState().setAttribute("examsSeason1BeginYear", examsSeason1BeginYear);
    }

    public Integer getExamsSeason1EndDay() {
        return examsSeason1EndDay;
    }

    public void setExamsSeason1EndDay(Integer examsSeason1EndDay) {
        this.examsSeason1EndDay = examsSeason1EndDay;
    }

    public Integer getExamsSeason1EndMonth() {
        return examsSeason1EndMonth;
    }

    public void setExamsSeason1EndMonth(Integer examsSeason1EndMonth) {
        this.examsSeason1EndMonth = examsSeason1EndMonth;
    }

    public Integer getExamsSeason1EndYear() {
        return (Integer) this.getViewState().getAttribute("examsSeason1EndYear");
    }

    public void setExamsSeason1EndYear(Integer examsSeason1EndYear) {
        this.getViewState().setAttribute("examsSeason1EndYear", examsSeason1EndYear);
    }

    public Integer getExamsSeason2BeginDay() {
        return examsSeason2BeginDay;
    }

    public void setExamsSeason2BeginDay(Integer examsSeason2BeginDay) {
        this.examsSeason2BeginDay = examsSeason2BeginDay;
    }

    public Integer getExamsSeason2BeginMonth() {
        return examsSeason2BeginMonth;
    }

    public void setExamsSeason2BeginMonth(Integer examsSeason2BeginMonth) {
        this.examsSeason2BeginMonth = examsSeason2BeginMonth;
    }

    public Integer getExamsSeason2BeginYear() {
        return (Integer) this.getViewState().getAttribute("examsSeason2BeginYear");
    }

    public void setExamsSeason2BeginYear(Integer examsSeason2BeginYear) {
        this.getViewState().setAttribute("examsSeason2BeginYear", examsSeason2BeginYear);
    }

    public Integer getExamsSeason2EndDay() {
        return examsSeason2EndDay;
    }

    public void setExamsSeason2EndDay(Integer examsSeason2EndDay) {
        this.examsSeason2EndDay = examsSeason2EndDay;
    }

    public Integer getExamsSeason2EndMonth() {
        return examsSeason2EndMonth;
    }

    public void setExamsSeason2EndMonth(Integer examsSeason2EndMonth) {
        this.examsSeason2EndMonth = examsSeason2EndMonth;
    }

    public Integer getExamsSeason2EndYear() {
        return (Integer) this.getViewState().getAttribute("examsSeason2EndYear");
    }

    public void setExamsSeason2EndYear(Integer examsSeason2EndYear) {
        this.getViewState().setAttribute("examsSeason2EndYear", examsSeason2EndYear);
    }

    public Integer getLessonSeason1BeginDay() {
        return lessonSeason1BeginDay;
    }

    public void setLessonSeason1BeginDay(Integer lessonSeason1BeginDay) {
        this.lessonSeason1BeginDay = lessonSeason1BeginDay;
    }

    public Integer getLessonSeason1BeginMonth() {
        return lessonSeason1BeginMonth;
    }

    public void setLessonSeason1BeginMonth(Integer lessonSeason1BeginMonth) {
        this.lessonSeason1BeginMonth = lessonSeason1BeginMonth;
    }

    public Integer getLessonSeason1BeginYear() {
        return (Integer) this.getViewState().getAttribute("lessonSeason1BeginYear");
    }

    public void setLessonSeason1BeginYear(Integer lessonSeason1BeginYear) {
        this.getViewState().setAttribute("lessonSeason1BeginYear", lessonSeason1BeginYear);
    }

    public Integer getLessonSeason1EndDay() {
        return lessonSeason1EndDay;
    }

    public void setLessonSeason1EndDay(Integer lessonSeason1EndDay) {
        this.lessonSeason1EndDay = lessonSeason1EndDay;
    }

    public Integer getLessonSeason1EndMonth() {
        return lessonSeason1EndMonth;
    }

    public void setLessonSeason1EndMonth(Integer lessonSeason1EndMonth) {
        this.lessonSeason1EndMonth = lessonSeason1EndMonth;
    }

    public Integer getLessonSeason1EndYear() {
        return (Integer) this.getViewState().getAttribute("lessonSeason1EndYear");
    }

    public void setLessonSeason1EndYear(Integer lessonSeason1EndYear) {
        this.getViewState().setAttribute("lessonSeason1EndYear", lessonSeason1EndYear);
    }

    public Integer getLessonSeason2BeginDay() {
        return lessonSeason2BeginDay;
    }

    public void setLessonSeason2BeginDay(Integer lessonSeason2BeginDay) {
        this.lessonSeason2BeginDay = lessonSeason2BeginDay;
    }

    public Integer getLessonSeason2BeginMonth() {
        return lessonSeason2BeginMonth;
    }

    public void setLessonSeason2BeginMonth(Integer lessonSeason2BeginMonth) {
        this.lessonSeason2BeginMonth = lessonSeason2BeginMonth;
    }

    public Integer getLessonSeason2BeginYear() {
        return (Integer) this.getViewState().getAttribute("lessonSeason2BeginYear");
    }

    public void setLessonSeason2BeginYear(Integer lessonSeason2BeginYear) {
        this.getViewState().setAttribute("lessonSeason2BeginYear", lessonSeason2BeginYear);
    }

    public Integer getLessonSeason2EndDay() {
        return lessonSeason2EndDay;
    }

    public void setLessonSeason2EndDay(Integer lessonSeason2EndDay) {
        this.lessonSeason2EndDay = lessonSeason2EndDay;
    }

    public Integer getLessonSeason2EndMonth() {
        return lessonSeason2EndMonth;
    }

    public void setLessonSeason2EndMonth(Integer lessonSeason2EndMonth) {
        this.lessonSeason2EndMonth = lessonSeason2EndMonth;
    }

    public Integer getLessonSeason2EndYear() {
        return (Integer) this.getViewState().getAttribute("lessonSeason2EndYear");
    }

    public void setLessonSeason2EndYear(Integer lessonSeason2EndYear) {
        this.getViewState().setAttribute("lessonSeason2EndYear", lessonSeason2EndYear);
    }

    public Boolean getTemporaryExamMap() {
        return temporaryExamMap;
    }

    public void setTemporaryExamMap(Boolean temporaryExamMap) {
        this.temporaryExamMap = temporaryExamMap;
    }

    public Integer getExamsSpecialSeasonBeginDay() {
        return examsSpecialSeasonBeginDay;
    }

    public void setExamsSpecialSeasonBeginDay(Integer examsSpecialSeasonBeginDay) {
        this.examsSpecialSeasonBeginDay = examsSpecialSeasonBeginDay;
    }

    public Integer getExamsSpecialSeasonBeginMonth() {
        return examsSpecialSeasonBeginMonth;
    }

    public void setExamsSpecialSeasonBeginMonth(Integer examsSpecialSeasonBeginMonth) {
        this.examsSpecialSeasonBeginMonth = examsSpecialSeasonBeginMonth;
    }

    public Integer getExamsSpecialSeasonBeginYear() {
        return (Integer) this.getViewState().getAttribute("examsSpecialSeasonBeginYear");
    }

    public void setExamsSpecialSeasonBeginYear(Integer examsSpecialSeasonBeginYear) {
        this.getViewState().setAttribute("examsSpecialSeasonBeginYear", examsSpecialSeasonBeginYear);
    }

    public Integer getExamsSpecialSeasonEndDay() {
        return examsSpecialSeasonEndDay;
    }

    public void setExamsSpecialSeasonEndDay(Integer examsSpecialSeasonEndDay) {
        this.examsSpecialSeasonEndDay = examsSpecialSeasonEndDay;
    }

    public Integer getExamsSpecialSeasonEndMonth() {
        return examsSpecialSeasonEndMonth;
    }

    public void setExamsSpecialSeasonEndMonth(Integer examsSpecialSeasonEndMonth) {
        this.examsSpecialSeasonEndMonth = examsSpecialSeasonEndMonth;
    }

    public Integer getExamsSpecialSeasonEndYear() {
        return (Integer) this.getViewState().getAttribute("examsSpecialSeasonEndYear");
    }

    public void setExamsSpecialSeasonEndYear(Integer examsSpecialSeasonEndYear) {
        this.getViewState().setAttribute("examsSpecialSeasonEndYear", examsSpecialSeasonEndYear);
    }

    public Integer getGradeSubmissionNormalSeason1EndDay() {
        return gradeSubmissionNormalSeason1EndDay;
    }

    public void setGradeSubmissionNormalSeason1EndDay(Integer gradeSubmissionNormalSeason1EndDay) {
        this.gradeSubmissionNormalSeason1EndDay = gradeSubmissionNormalSeason1EndDay;
    }

    public Integer getGradeSubmissionNormalSeason1EndMonth() {
        return gradeSubmissionNormalSeason1EndMonth;
    }

    public void setGradeSubmissionNormalSeason1EndMonth(Integer gradeSubmissionNormalSeason1EndMonth) {
        this.gradeSubmissionNormalSeason1EndMonth = gradeSubmissionNormalSeason1EndMonth;
    }

    public Integer getGradeSubmissionNormalSeason1EndYear() {
        return (Integer) this.getViewState().getAttribute("gradeSubmissionNormalSeason1EndYear");
    }

    public void setGradeSubmissionNormalSeason1EndYear(Integer gradeSubmissionNormalSeason1EndYear) {
        this.getViewState().setAttribute("gradeSubmissionNormalSeason1EndYear", gradeSubmissionNormalSeason1EndYear);
    }

    public Integer getGradeSubmissionNormalSeason2EndDay() {
        return gradeSubmissionNormalSeason2EndDay;
    }

    public void setGradeSubmissionNormalSeason2EndDay(Integer gradeSubmissionNormalSeason2EndDay) {
        this.gradeSubmissionNormalSeason2EndDay = gradeSubmissionNormalSeason2EndDay;
    }

    public Integer getGradeSubmissionNormalSeason2EndMonth() {
        return gradeSubmissionNormalSeason2EndMonth;
    }

    public void setGradeSubmissionNormalSeason2EndMonth(Integer gradeSubmissionNormalSeason2EndMonth) {
        this.gradeSubmissionNormalSeason2EndMonth = gradeSubmissionNormalSeason2EndMonth;
    }

    public Integer getGradeSubmissionNormalSeason2EndYear() {
        return (Integer) this.getViewState().getAttribute("gradeSubmissionNormalSeason2EndYear");
    }

    public void setGradeSubmissionNormalSeason2EndYear(Integer gradeSubmissionNormalSeason2EndYear) {
        this.getViewState().setAttribute("gradeSubmissionNormalSeason2EndYear", gradeSubmissionNormalSeason2EndYear);
    }

    public Integer getGradeSubmissionSpecialSeasonEndDay() {
        return gradeSubmissionSpecialSeasonEndDay;
    }

    public void setGradeSubmissionSpecialSeasonEndDay(Integer gradeSubmissionSpecialSeasonEndDay) {
        this.gradeSubmissionSpecialSeasonEndDay = gradeSubmissionSpecialSeasonEndDay;
    }

    public Integer getGradeSubmissionSpecialSeasonEndMonth() {
        return gradeSubmissionSpecialSeasonEndMonth;
    }

    public void setGradeSubmissionSpecialSeasonEndMonth(Integer gradeSubmissionSpecialSeasonEndMonth) {
        this.gradeSubmissionSpecialSeasonEndMonth = gradeSubmissionSpecialSeasonEndMonth;
    }

    public Integer getGradeSubmissionSpecialSeasonEndYear() {
        return (Integer) this.getViewState().getAttribute("gradeSubmissionSpecialSeasonEndYear");
    }

    public void setGradeSubmissionSpecialSeasonEndYear(Integer gradeSubmissionSpecialSeasonEndYear) {
        this.getViewState().setAttribute("gradeSubmissionSpecialSeasonEndYear", gradeSubmissionSpecialSeasonEndYear);
    }

    public Integer getLessonSeason2BeginDayPart2() {
        return lessonSeason2BeginDayPart2;
    }

    public void setLessonSeason2BeginDayPart2(Integer lessonSeason2BeginDayPart2) {
        this.lessonSeason2BeginDayPart2 = lessonSeason2BeginDayPart2;
    }

    public Integer getLessonSeason2BeginMonthPart2() {
        return lessonSeason2BeginMonthPart2;
    }

    public void setLessonSeason2BeginMonthPart2(Integer lessonSeason2BeginMonthPart2) {
        this.lessonSeason2BeginMonthPart2 = lessonSeason2BeginMonthPart2;
    }

    public Integer getLessonSeason2EndDayPart2() {
        return lessonSeason2EndDayPart2;
    }

    public void setLessonSeason2EndDayPart2(Integer lessonSeason2EndDayPart2) {
        this.lessonSeason2EndDayPart2 = lessonSeason2EndDayPart2;
    }

    public Integer getLessonSeason2EndMonthPart2() {
        return lessonSeason2EndMonthPart2;
    }

    public void setLessonSeason2EndMonthPart2(Integer lessonSeason2EndMonthPart2) {
        this.lessonSeason2EndMonthPart2 = lessonSeason2EndMonthPart2;
    }

}