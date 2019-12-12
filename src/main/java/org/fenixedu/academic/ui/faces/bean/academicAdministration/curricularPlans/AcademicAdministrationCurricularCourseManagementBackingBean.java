/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.faces.bean.academicAdministration.curricularPlans;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.academic.domain.DomainObjectUtil;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curriculum.grade.GradeScale;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.dto.commons.CurricularCourseByExecutionSemesterBean;
import org.fenixedu.academic.service.services.bolonhaManager.AddContextToCurricularCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.faces.bean.bolonhaManager.curricularPlans.CurricularCourseManagementBackingBean;
import org.fenixedu.academic.ui.struts.action.exceptions.FenixActionException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

public class AcademicAdministrationCurricularCourseManagementBackingBean extends CurricularCourseManagementBackingBean {

    private String code;

    private String acronym;

    private Integer minimumValueForAcumulatedEnrollments;

    private Integer maximumValueForAcumulatedEnrollments;

    private Integer enrollmentWeigth;

    private Double credits;

    private Double ectsCredits;

    private Double theoreticalHours;

    private Double labHours;

    private Double praticalHours;

    private Double theoPratHours;

    private Boolean toAddNewContext;

    private CurricularCourseByExecutionSemesterBean curricularCourseSemesterBean = null;

    public AcademicAdministrationCurricularCourseManagementBackingBean() {
        if (getCurricularCourse() != null && getExecutionYear() != null) {
            curricularCourseSemesterBean = new CurricularCourseByExecutionSemesterBean(getCurricularCourse(),
                    getExecutionYear().getLastExecutionPeriod());
        }
    }

    @Override
    public CurricularCourseByExecutionSemesterBean getCurricularCourseSemesterBean() {
        return curricularCourseSemesterBean;
    }

    @Override
    public void setCurricularCourseSemesterBean(CurricularCourseByExecutionSemesterBean curricularCourseSemesterBean) {
        this.curricularCourseSemesterBean = curricularCourseSemesterBean;
    }

    public String getAcronym() {
        if (getCurricularCourse() != null) {
            acronym = getCurricularCourse().getAcronym();
        }
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getCode() {
        if (code == null) {
            code = (getCurricularCourse() != null) ? getCurricularCourse().getCode() : "";
        }
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getCredits() {
        if (credits == null) {
            credits = (getCurricularCourse() != null) ? getCurricularCourse().getCredits() : Double.valueOf(0);
        }
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Double getEctsCredits() {
        if (ectsCredits == null) {
            ectsCredits = (getCurricularCourse() != null) ? getCurricularCourse().getEctsCredits() : Double.valueOf(0);
        }
        return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }

    public Double getTheoreticalHours() {
        if (theoreticalHours == null) {
            theoreticalHours = (getCurricularCourse() != null) ? getCurricularCourse().getTheoreticalHours() : Double.valueOf(0);
        }
        return theoreticalHours;
    }

    public void setTheoreticalHours(final Double theoreticalHours) {
        this.theoreticalHours = theoreticalHours;
    }

    public Double getLabHours() {
        if (labHours == null) {
            labHours = (getCurricularCourse() != null) ? getCurricularCourse().getLabHours() : Double.valueOf(0);
        }
        return labHours;
    }

    public void setLabHours(Double labHours) {
        this.labHours = labHours;
    }

    public Double getPraticalHours() {
        if (praticalHours == null) {
            praticalHours = (getCurricularCourse() != null) ? getCurricularCourse().getPraticalHours() : Double.valueOf(0);
        }
        return praticalHours;
    }

    public void setPraticalHours(Double praticalHours) {
        this.praticalHours = praticalHours;
    }

    public Double getTheoPratHours() {
        if (theoPratHours == null) {
            theoPratHours = (getCurricularCourse() != null) ? getCurricularCourse().getTheoPratHours() : Double.valueOf(0);
        }
        return theoPratHours;
    }

    public void setTheoPratHours(Double theoPratHours) {
        this.theoPratHours = theoPratHours;
    }

    public Integer getEnrollmentWeigth() {
        if (enrollmentWeigth == null) {
            enrollmentWeigth = (getCurricularCourse() != null) ? getCurricularCourse().getEnrollmentWeigth() : Integer.valueOf(0);
        }
        return enrollmentWeigth;
    }

    public void setEnrollmentWeigth(Integer enrollmentWeigth) {
        this.enrollmentWeigth = enrollmentWeigth;
    }

    public Integer getMaximumValueForAcumulatedEnrollments() {
        if (maximumValueForAcumulatedEnrollments == null) {
            maximumValueForAcumulatedEnrollments = (getCurricularCourse() != null) ? getCurricularCourse()
                    .getMaximumValueForAcumulatedEnrollments() : Integer.valueOf(0);
        }
        return maximumValueForAcumulatedEnrollments;
    }

    public void setMaximumValueForAcumulatedEnrollments(Integer maximumValueForAcumulatedEnrollments) {
        this.maximumValueForAcumulatedEnrollments = maximumValueForAcumulatedEnrollments;
    }

    public Integer getMinimumValueForAcumulatedEnrollments() {
        if (minimumValueForAcumulatedEnrollments == null) {
            minimumValueForAcumulatedEnrollments = (getCurricularCourse() != null) ? getCurricularCourse()
                    .getMinimumValueForAcumulatedEnrollments() : Integer.valueOf(0);
        }
        return minimumValueForAcumulatedEnrollments;
    }

    public void setMinimumValueForAcumulatedEnrollments(Integer minimumValueForAcumulatedEnrollments) {
        this.minimumValueForAcumulatedEnrollments = minimumValueForAcumulatedEnrollments;
    }

    @Override
    protected List<SelectItem> readExecutionYearItems() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (isBolonha()) {
            readBolonhaExecutionYears(result);
        } else {
            readPreBolonhaExecutionYears(result);
        }
        Collections.sort(result, new Comparator<SelectItem>() {
            @Override
            public int compare(SelectItem o1, SelectItem o2) {
                return -o1.getLabel().compareTo(o2.getLabel());
            }
        });
        return result;
    }

    private void readBolonhaExecutionYears(final List<SelectItem> result) {
        for (final ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
            result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
        }
        if (getExecutionYearID() == null) {
            final ExecutionDegree mostRecentExecutionDegree = getDegreeCurricularPlan().getMostRecentExecutionDegree();
            setExecutionYearID(mostRecentExecutionDegree != null ? mostRecentExecutionDegree.getExecutionYear()
                    .getExternalId() : ExecutionYear.findCurrent(getDegreeCurricularPlan().getDegree().getCalendar())
                            .getExternalId());
        }
    }

    private void readPreBolonhaExecutionYears(final List<SelectItem> result) {
        for (final ExecutionYear executionYear : rootDomainObject.getExecutionYearsSet()) {
            result.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
        }
        if (getExecutionYearID() == null) {
            setExecutionYearID(ExecutionYear.findCurrent(getDegreeCurricularPlan().getDegree().getCalendar()).getExternalId());
        }
    }

    @Override
    protected List<SelectItem> readExecutionPeriodItems() {
        return isBolonha() ? super.readExecutionPeriodItems() : readPreBolonhaExecutionPeriodItems();
    }

    private List<SelectItem> readPreBolonhaExecutionPeriodItems() {

        final Comparator<ExecutionInterval> comparator = new Comparator<ExecutionInterval>() {

            @Override
            public int compare(final ExecutionInterval o1, final ExecutionInterval o2) {
                final AcademicInterval ai1 = o1.getAcademicInterval();
                final AcademicInterval ai2 = o2.getAcademicInterval();
                final int c = ai1.getStartDateTimeWithoutChronology().compareTo(ai2.getStartDateTimeWithoutChronology());
                return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
            }

        };

        final List<ExecutionInterval> semesters = new ArrayList<ExecutionInterval>(rootDomainObject.getExecutionPeriodsSet());
        Collections.sort(semesters, new ReverseComparator(comparator));

        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final ExecutionInterval semester : semesters) {
            result.add(new SelectItem(semester.getExternalId(), semester.getQualifiedName()));
        }
        return result;
    }

    public boolean isToAddNewContext() {
        if (toAddNewContext == null) {
            toAddNewContext = getAndHoldBooleanParameter("toAddNewContext");
            if (toAddNewContext == null) {
                toAddNewContext = false;
            }
        }
        return toAddNewContext;
    }

    public void setToAddNewContext(Boolean toAddNewContext) {
        this.toAddNewContext = toAddNewContext;
    }

    @Override
    public String addContext() {
        try {
            checkCourseGroup();
            checkCurricularCourse();
            checkCurricularSemesterAndYear();
            AddContextToCurricularCourse.run(getCurricularCourse(), getCourseGroup(), getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID(), getCurricularYearID(), getCurricularSemesterID());
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "addedNewContextToCurricularCourse"));
            setToAddNewContext(false);
            setContextID(null);
        } catch (FenixActionException | FenixServiceException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage(), e.getArgs()));
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "general.error"));
        }
        return "";
    }

    @Override
    public String cancel() {
        setToAddNewContext(false);
        return super.cancel();
    }
}
