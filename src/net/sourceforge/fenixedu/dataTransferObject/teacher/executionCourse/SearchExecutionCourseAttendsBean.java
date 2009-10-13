package net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.accessControl.FixedSetGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.util.WorkingStudentSelectionType;
import pt.utl.ist.fenix.tools.predicates.AndPredicate;
import pt.utl.ist.fenix.tools.predicates.InlinePredicate;
import pt.utl.ist.fenix.tools.predicates.Predicate;
import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class SearchExecutionCourseAttendsBean implements Serializable {

    private DomainReference<ExecutionCourse> executionCourse;
    private Boolean viewPhoto;
    private Collection<StudentAttendsStateType> attendsStates;
    private Collection<WorkingStudentSelectionType> workingStudentTypes;
    private Collection<DomainReference<DegreeCurricularPlan>> degreeCurricularPlans;
    private Collection<DomainReference<Shift>> shifts;
    private Collection<DomainReference<Attends>> attendsResult;
    private transient Map<Integer, Integer> enrolmentsNumberMap;

    public String getEnumerationResourcesString(String name) {
	return ResourceBundle.getBundle("resources/EnumerationResources", Language.getLocale()).getString(name);
    }

    public String getApplicationResourcesString(String name) {
	return ResourceBundle.getBundle("resources/ApplicationResources", Language.getLocale()).getString(name);
    }

    public SearchExecutionCourseAttendsBean(ExecutionCourse executionCourse) {
	setExecutionCourse(executionCourse);
	setViewPhoto(false);
	setAttendsStates(Arrays.asList(StudentAttendsStateType.values()));
	setWorkingStudentTypes(Arrays.asList(WorkingStudentSelectionType.values()));
	setShifts(getExecutionCourse().getAssociatedShifts());
	setDegreeCurricularPlans(getExecutionCourse().getAttendsDegreeCurricularPlans());
	attendsResult = new ArrayList<DomainReference<Attends>>();
    }

    public ExecutionCourse getExecutionCourse() {
	return (this.executionCourse != null) ? this.executionCourse.getObject() : null;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
	this.executionCourse = (executionCourse != null) ? new DomainReference<ExecutionCourse>(executionCourse) : null;
    }

    public Boolean getViewPhoto() {
	return viewPhoto;
    }

    public void setViewPhoto(Boolean viewPhoto) {
	this.viewPhoto = viewPhoto;
    }

    public enum StudentAttendsStateType {
	ENROLED, NOT_ENROLED, IMPROVEMENT, SPECIAL_SEASON;
	public String getQualifiedName() {
	    return StringAppender.append(StudentAttendsStateType.class.getSimpleName(), ".", name());
	}
    }

    public Collection<StudentAttendsStateType> getAttendsStates() {
	return attendsStates;
    }

    public void setAttendsStates(Collection<StudentAttendsStateType> attendsStates) {
	this.attendsStates = attendsStates;
    }

    public Collection<DegreeCurricularPlan> getDegreeCurricularPlans() {
	Collection<DegreeCurricularPlan> dcps = new ArrayList<DegreeCurricularPlan>();
	for (DomainReference<DegreeCurricularPlan> degreeCurricularPlan : degreeCurricularPlans) {
	    dcps.add(degreeCurricularPlan.getObject());
	}
	return dcps;
    }

    public void setShifts(Collection<Shift> shifts) {
	Collection<DomainReference<Shift>> drShifts = new ArrayList<DomainReference<Shift>>();
	for (Shift shift : shifts) {
	    drShifts.add(new DomainReference<Shift>(shift));
	}
	this.shifts = drShifts;
    }

    public Collection<Shift> getShifts() {
	Collection<Shift> shifts = new ArrayList<Shift>();
	for (DomainReference<Shift> shift : this.shifts) {
	    shifts.add(shift.getObject());
	}
	return shifts;
    }

    public void setDegreeCurricularPlans(Collection<DegreeCurricularPlan> degreeCurricularPlans) {
	Collection<DomainReference<DegreeCurricularPlan>> dcps = new ArrayList<DomainReference<DegreeCurricularPlan>>();
	for (DegreeCurricularPlan dcp : degreeCurricularPlans) {
	    dcps.add(new DomainReference<DegreeCurricularPlan>(dcp));
	}
	this.degreeCurricularPlans = dcps;
    }

    public Collection<WorkingStudentSelectionType> getWorkingStudentTypes() {
	return workingStudentTypes;
    }

    public void setWorkingStudentTypes(Collection<WorkingStudentSelectionType> workingStudentTypes) {
	this.workingStudentTypes = workingStudentTypes;
    }

    public Collection<Attends> getAttendsResult() {
	Collection<Attends> attends = new ArrayList<Attends>();
	for (DomainReference<Attends> attendRef : attendsResult) {
	    attends.add(attendRef.getObject());
	}
	return attends;
    }

    public void setAttendsResult(Collection<Attends> atts) {
	ArrayList<DomainReference<Attends>> results = new ArrayList<DomainReference<Attends>>();
	for (Attends attend : atts) {
	    results.add(new DomainReference<Attends>(attend));
	}
	this.attendsResult = results;
    }

    public Map<Integer, Integer> getEnrolmentsNumberMap() {
	return enrolmentsNumberMap;
    }

    public void setEnrolmentsNumberMap(Map<Integer, Integer> enrolmentsNumberMap) {
	this.enrolmentsNumberMap = enrolmentsNumberMap;
    }

    public Predicate<Attends> getFilters() {

	Collection<Predicate<Attends>> filters = new ArrayList<Predicate<Attends>>();

	if (getAttendsStates().size() < StudentAttendsStateType.values().length) {
	    filters.add(new InlinePredicate<Attends, Collection<StudentAttendsStateType>>(getAttendsStates()) {

		@Override
		public boolean eval(Attends attends) {
		    return getValue().contains(attends.getAttendsStateType());
		}

	    });
	}

	if (getWorkingStudentTypes().size() < WorkingStudentSelectionType.values().length) {
	    filters.add(new InlinePredicate<Attends, Collection<WorkingStudentSelectionType>>(getWorkingStudentTypes()) {

		@Override
		public boolean eval(Attends attends) {
		    return getValue().contains(getWorkingStudentType(attends));
		}

		private WorkingStudentSelectionType getWorkingStudentType(Attends attends) {
		    if (attends.getRegistration().getStudent().hasActiveStatuteInPeriod(StudentStatuteType.WORKING_STUDENT,
			    attends.getExecutionPeriod())) {
			return WorkingStudentSelectionType.WORKING_STUDENT;
		    } else {
			return WorkingStudentSelectionType.NOT_WORKING_STUDENT;
		    }
		}
	    });
	}

	if (shifts.size() < getExecutionCourse().getAssociatedShifts().size()) {
	    filters.add(new InlinePredicate<Attends, Collection<Shift>>(getShifts()) {

		@Override
		public boolean eval(Attends attends) {
		    for (Shift shift : getValue()) {
			if (shift.hasStudents(attends.getRegistration())) {
			    return true;
			}
		    }
		    return false;
		}
	    });
	}

	if (degreeCurricularPlans.size() < getExecutionCourse().getAttendsDegreeCurricularPlans().size()) {
	    filters.add(new InlinePredicate<Attends, Collection<DegreeCurricularPlan>>(getDegreeCurricularPlans()) {

		@Override
		public boolean eval(Attends attends) {
		    return getValue().contains(attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan());
		}

	    });

	}

	return new AndPredicate<Attends>(filters);
    }

    public Group getAttendsGroup() {
	List<Person> persons = new ArrayList<Person>();
	for (Attends attends : getAttendsResult()) {
	    persons.add(attends.getRegistration().getStudent().getPerson());
	}
	return new FixedSetGroup(persons);
    }

    public String getLabel() {

	String attendTypeValues = "", degreeNameValues = "", shiftsValues = "", workingStudentsValues = "";

	for (StudentAttendsStateType attendType : attendsStates) {
	    if (!attendTypeValues.isEmpty()) {
		attendTypeValues += ", ";
	    }
	    attendTypeValues += getEnumerationResourcesString(attendType.getQualifiedName());
	}

	for (DomainReference<DegreeCurricularPlan> degree : degreeCurricularPlans) {
	    if (!degreeNameValues.isEmpty()) {
		degreeNameValues += ", ";
	    }
	    degreeNameValues += degree.getObject().getName();
	}

	for (DomainReference<Shift> shift : shifts) {
	    if (!shiftsValues.isEmpty()) {
		shiftsValues += ", ";
	    }
	    shiftsValues += shift.getObject().getPresentationName();
	}

	for (WorkingStudentSelectionType workingStudent : workingStudentTypes) {
	    if (!workingStudentsValues.isEmpty()) {
		workingStudentsValues += ", ";
	    }
	    workingStudentsValues += getEnumerationResourcesString(workingStudent.getQualifiedName());
	}

	return String.format("%s : %s \n%s : %s \n%s : %s \n%s", getApplicationResourcesString("label.selectStudents"),
		attendTypeValues, getApplicationResourcesString("label.attends.courses"), degreeNameValues,
		getApplicationResourcesString("label.selectShift"), shiftsValues, workingStudentsValues);

    }

    public String getSearchElementsAsParameters() {
	String parameters = "";

	parameters += "&amp;executionCourse=" + getExecutionCourse().getIdInternal();
	if (viewPhoto) {
	    parameters += "&amp;viewPhoto=true";
	}
	if (getAttendsStates() != null) {
	    parameters += "&amp;attendsStates=";
	    for (StudentAttendsStateType attendsStateType : getAttendsStates()) {
		parameters += attendsStateType.toString() + ":";
	    }
	}
	if (getWorkingStudentTypes() != null) {
	    parameters += "&amp;workingStudentTypes=";
	    for (WorkingStudentSelectionType workingStudentType : getWorkingStudentTypes()) {
		parameters += workingStudentType.toString() + ":";
	    }
	}
	if (getDegreeCurricularPlans() != null) {
	    parameters += "&amp;degreeCurricularPlans=";
	    for (DegreeCurricularPlan degreeCurricularPlan : getDegreeCurricularPlans()) {
		parameters += degreeCurricularPlan.getIdInternal() + ":";
	    }
	}
	if (getShifts() != null) {
	    parameters += "&amp;shifts=";
	    for (Shift shift : getShifts()) {
		parameters += shift.getIdInternal() + ":";
	    }
	}

	return parameters;
    }
}
