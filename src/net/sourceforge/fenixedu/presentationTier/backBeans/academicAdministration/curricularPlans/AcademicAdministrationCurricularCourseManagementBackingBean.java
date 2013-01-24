package net.sourceforge.fenixedu.presentationTier.backBeans.academicAdministration.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.CreateOldCurricularCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditOldCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.commons.CurricularCourseByExecutionSemesterBean;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans.CurricularCourseManagementBackingBean;

import org.apache.commons.collections.comparators.ReverseComparator;

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

    private String gradeScaleString;

    private GradeScale gradeScale;

    private CurricularCourseByExecutionSemesterBean curricularCourseSemesterBean = null;

    public AcademicAdministrationCurricularCourseManagementBackingBean() {
	if (getCurricularCourse() != null && getExecutionYear() != null) {
	    curricularCourseSemesterBean = new CurricularCourseByExecutionSemesterBean(getCurricularCourse(),
		    ExecutionSemester.readBySemesterAndExecutionYear(2, getExecutionYear().getYear()));
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

    public String createOldCurricularCourse() {
	try {
	    checkCourseGroup();
	    checkCurricularSemesterAndYear();

	    CreateOldCurricularCourse.run(getDegreeCurricularPlanID(), getCourseGroupID(), getName(), getNameEn(), getCode(),
		    getAcronym(), getMinimumValueForAcumulatedEnrollments(), getMaximumValueForAcumulatedEnrollments(),
		    getWeight(), getEnrollmentWeigth(), getCredits(), getEctsCredits(), getCurricularYearID(),
		    getCurricularSemesterID(), getBeginExecutionPeriodID(), getEndExecutionPeriodID(), getGradeScale());
	} catch (FenixActionException e) {
	    this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
	    return "";
	} catch (IllegalDataAccessException e) {
	    this.addErrorMessage(bolonhaBundle.getString("error.notAuthorized"));
	    return "buildCurricularPlan";
	} catch (FenixServiceException e) {
	    this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
	    return "";
	} catch (DomainException e) {
	    this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
	    return "";
	} catch (Exception e) {
	    this.addErrorMessage(bolonhaBundle.getString("general.error"));
	    return "buildCurricularPlan";
	}
	addInfoMessage(bolonhaBundle.getString("curricularCourseCreated"));
	return "buildCurricularPlan";
    }

    public String editOldCurricularCourse() throws FenixFilterException {
	try {
	    EditOldCurricularCourse.run(getCurricularCourseID(), getName(), getNameEn(), getCode(), getAcronym(),
		    getMinimumValueForAcumulatedEnrollments(), getMaximumValueForAcumulatedEnrollments(), getWeight(),
		    getEnrollmentWeigth(), getCredits(), getEctsCredits(), getTheoreticalHours(), getLabHours(),
		    getPraticalHours(), getTheoPratHours(), getGradeScale());
	    setContextID(0); // resetContextID
	} catch (FenixServiceException e) {
	    addErrorMessage(bolonhaBundle.getString(e.getMessage()));
	}
	addInfoMessage(bolonhaBundle.getString("curricularCourseEdited"));
	return "";
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
	final List<ExecutionDegree> executionDegrees = getDegreeCurricularPlan().getExecutionDegrees();
	if (executionDegrees.isEmpty()) {
	    for (final ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
		result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));
	    }
	    if (getExecutionYearID() == null) {
		setExecutionYearID(ExecutionYear.readCurrentExecutionYear().getIdInternal());
	    }
	} else {
	    for (final ExecutionDegree executionDegree : executionDegrees) {
		result.add(new SelectItem(executionDegree.getExecutionYear().getIdInternal(), executionDegree.getExecutionYear()
			.getYear()));
	    }
	    if (getExecutionYearID() == null) {
		setExecutionYearID(getDegreeCurricularPlan().getMostRecentExecutionDegree().getExecutionYear().getIdInternal());
	    }
	}
    }

    private void readPreBolonhaExecutionYears(final List<SelectItem> result) {
	for (final ExecutionYear executionYear : rootDomainObject.getExecutionYears()) {
	    result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));
	}
	if (getExecutionYearID() == null) {
	    setExecutionYearID(ExecutionYear.readCurrentExecutionYear().getIdInternal());
	}
    }

    @Override
    protected List<SelectItem> readExecutionPeriodItems() {
	return isBolonha() ? super.readExecutionPeriodItems() : readPreBolonhaExecutionPeriodItems();
    }

    private List<SelectItem> readPreBolonhaExecutionPeriodItems() {
	final List<ExecutionSemester> semesters = new ArrayList<ExecutionSemester>(rootDomainObject.getExecutionPeriods());
	Collections.sort(semesters, new ReverseComparator(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR));

	final List<SelectItem> result = new ArrayList<SelectItem>();
	for (final ExecutionSemester semester : semesters) {
	    result.add(new SelectItem(semester.getIdInternal(), semester.getQualifiedName()));
	}
	return result;
    }

    public List<SelectItem> getGradeScales() {
	List<SelectItem> res = new ArrayList<SelectItem>();
	res.add(new SelectItem(this.NO_SELECTION_STRING, bolonhaBundle.getString("choose")));
	for (GradeScale gradeScale : GradeScale.values()) {
	    res.add(new SelectItem(gradeScale.getName(), enumerationBundle.getString(gradeScale.getName())));
	}
	return res;
    }

    public String getGradeScaleString() {
	if (this.gradeScaleString == null && getCurricularCourse() != null)
	    this.gradeScaleString = getCurricularCourse().getGradeScale() != null ? getCurricularCourse().getGradeScale().name()
		    : null;

	return this.gradeScaleString;
    }

    public void setGradeScaleString(final String value) {
	this.gradeScaleString = value;
    }

    private GradeScale getGradeScale() {
	if (this.gradeScale == null && this.getGradeScaleString() != null
		&& !this.NO_SELECTION_STRING.equals(this.getGradeScaleString())) {
	    this.gradeScale = GradeScale.valueOf(getGradeScaleString());
	} else if (this.gradeScale == null && this.getGradeScaleString() != null
		&& this.NO_SELECTION_STRING.equals(this.getGradeScaleString())) {
	    this.gradeScale = null;
	}
	return this.gradeScale;
    }

}
