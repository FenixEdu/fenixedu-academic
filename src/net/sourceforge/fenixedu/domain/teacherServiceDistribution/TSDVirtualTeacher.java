package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonContractSituation;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;

public class TSDVirtualTeacher extends TSDVirtualTeacher_Base {

	private TSDVirtualTeacher() {
		super();
	}

	public TSDVirtualTeacher(ProfessionalCategory professionalCategory, String name, Double requiredHours) {
		this();

		if (professionalCategory == null || name == null || requiredHours == null) {
			throw new DomainException("Parameters.required");
		}

		this.setProfessionalCategory(professionalCategory);
		this.setName(name);
		this.setRequiredHours(requiredHours);
		this.setExtraCreditsName("");
		this.setExtraCreditsValue(0d);
		this.setUsingExtraCredits(false);
	}

	@Override
	public String getName() {
		return super.getName();
	}

	@Override
	public Double getRealHoursByShiftTypeAndExecutionCourses(ShiftType shiftType, List<ExecutionCourse> executionCourseList) {
		Double theoreticalHoursLastYear = 0.0;
		return theoreticalHoursLastYear;
	}

	@Override
	public Double getRequiredHours(final List<ExecutionSemester> executionPeriodList) {
		return super.getRequiredHours();
	}

	@Override
	public void delete() {
		super.delete();
	}

	@Override
	public Double getServiceExemptionCredits(List<ExecutionSemester> executionPeriodList) {
		Double serviceExemptionCredits = 0d;
		return serviceExemptionCredits;
	}

	@Override
	public Double getManagementFunctionsCredits(List<ExecutionSemester> executionPeriodList) {
		Double managementFunctionsCredits = 0d;
		return managementFunctionsCredits;
	}

	@Override
	public Double getRequiredTeachingHours(List<ExecutionSemester> executionPeriodList) {
		return getRequiredHours(executionPeriodList) - (getUsingExtraCredits() ? getExtraCreditsValue(executionPeriodList) : 0.0);
	}

	@Override
	public List<PersonContractSituation> getServiceExemptions(List<ExecutionSemester> executionPeriodList) {
		List<PersonContractSituation> teacherServiceExemptionList = new ArrayList<PersonContractSituation>();
		return teacherServiceExemptionList;
	}

	@Override
	public List<PersonFunction> getManagementFunctions(List<ExecutionSemester> executionPeriodList) {
		List<PersonFunction> personFunctionList = new ArrayList<PersonFunction>();
		return personFunctionList;
	}

	@Override
	public String getEmailUserId() {
		return getAcronym();
	}

	@Override
	public String getShortName() {
		return this.getName();
	}

	@Override
	public String getDistinctName() {
		return getShortName();
	}

	@Override
	public String getTeacherId() {
		return null;
	}

}
