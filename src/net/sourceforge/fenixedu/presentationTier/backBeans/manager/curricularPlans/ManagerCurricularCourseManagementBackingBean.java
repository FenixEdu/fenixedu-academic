package net.sourceforge.fenixedu.presentationTier.backBeans.manager.curricularPlans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans.CurricularCourseManagementBackingBean;

public class ManagerCurricularCourseManagementBackingBean extends CurricularCourseManagementBackingBean {

    private String code;

    private String acronym;

    private Integer minimumValueForAcumulatedEnrollments;

    private Integer maximumValueForAcumulatedEnrollments;

    private Integer enrollmentWeigth;

    private Double credits;

    private Double ectsCredits;

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

	    ServiceUtils.executeService(getUserView(), "CreateOldCurricularCourse", getArgumentsToCreate());

	} catch (FenixActionException e) {
	    this.addErrorMessage(bolonhaBundle.getString(e.getMessage()));
	    return "";
	} catch (FenixFilterException e) {
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

    private Object[] getArgumentsToCreate() {
	return new Object[] { getDegreeCurricularPlanID(), getCourseGroupID(), getName(), getNameEn(), getCode(), getAcronym(),
		getMinimumValueForAcumulatedEnrollments(), getMaximumValueForAcumulatedEnrollments(), getWeight(),
		getEnrollmentWeigth(), getCredits(), getEctsCredits(), getCurricularYearID(), getCurricularSemesterID(),
		getBeginExecutionPeriodID(), getEndExecutionPeriodID() };
    }

    public String editOldCurricularCourse() throws FenixFilterException {
	try {
	    ServiceUtils.executeService(getUserView(), "EditOldCurricularCourse", getArgumentsToEdit());
	    setContextID(0); // resetContextID
	} catch (FenixServiceException e) {
	    addErrorMessage(bolonhaBundle.getString(e.getMessage()));
	}
	addInfoMessage(bolonhaBundle.getString("curricularCourseEdited"));
	return "";
    }

    private Object[] getArgumentsToEdit() {
	return new Object[] { getCurricularCourseID(), getName(), getNameEn(), getCode(), getAcronym(),
		getMinimumValueForAcumulatedEnrollments(), getMaximumValueForAcumulatedEnrollments(), getWeight(),
		getEnrollmentWeigth(), getCredits(), getEctsCredits() };
    }

    @Override
    protected List<SelectItem> readExecutionYearItems() {
	final List<SelectItem> result = new ArrayList<SelectItem>();

	final List<ExecutionDegree> executionDegrees = getDegreeCurricularPlan().getExecutionDegrees();

	if (executionDegrees.isEmpty()) {
	    for (final ExecutionYear executionYear : getDegreeCurricularPlan().getBeginContextExecutionYears()) {
		result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));
	    }
	    if (getExecutionYearID() == null) {
		setExecutionYearID((Integer) result.get(0).getValue());
	    }
	    return result;
	}

	for (final ExecutionDegree executionDegree : executionDegrees) {
	    result.add(new SelectItem(executionDegree.getExecutionYear().getIdInternal(), executionDegree.getExecutionYear()
		    .getYear()));
	}

	if (getExecutionYearID() == null) {
	    setExecutionYearID(getDegreeCurricularPlan().getMostRecentExecutionDegree().getExecutionYear().getIdInternal());
	}

	return result;
    }

}
