package net.sourceforge.fenixedu.domain;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

public class Shift extends Shift_Base {

    public static final ResourceBundle enumerationResourcesBundle = ResourceBundle.getBundle("resources/EnumerationResources");

    public static final Comparator<Shift> SHIFT_COMPARATOR_BY_NAME = new BeanComparator("nome", Collator.getInstance());
    public static final Comparator<Shift> SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS = new ComparatorChain();
        
    static {
        ((ComparatorChain) SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS).addComparator(new BeanComparator("executionCourse.nome"));
	((ComparatorChain) SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS).addComparator(new BeanComparator("shiftTypesIntegerComparator"));
	((ComparatorChain) SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS).addComparator(new BeanComparator("lessonsStringComparator"));
	((ComparatorChain) SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS).addComparator(DomainObject.COMPARATOR_BY_ID);

	Registration.ShiftStudent.addListener(new ShiftStudentListener());
    }

    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManageShifts")
    public Shift(final ExecutionCourse executionCourse, Collection<ShiftType> types, final Integer lotacao) {
	
	super();
	
	setRootDomainObject(RootDomainObject.getInstance());	
	shiftTypeManagement(types, executionCourse);
	setLotacao(lotacao);		
	executionCourse.setShiftNames();
	
	if(!hasAnyCourseLoads()) {
	    throw new DomainException("error.Shift.empty.courseLoads");
	}
    }

    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManageShifts")
    public void edit(List<ShiftType> newTypes, Integer newCapacity, ExecutionCourse newExecutionCourse, String newName) {	

	ExecutionCourse beforeExecutionCourse = getExecutionCourse();

	final Shift otherShiftWithSameNewName = newExecutionCourse.findShiftByName(newName);
	if (otherShiftWithSameNewName != null && otherShiftWithSameNewName != this) {
	    throw new DomainException("error.Shift.with.this.name.already.exists");
	}	
	
	if(newCapacity != null && getStudentsCount() > newCapacity.intValue()) {
	    throw new DomainException("errors.exception.invalid.finalAvailability");
	}
		
	setLotacao(newCapacity);		
	shiftTypeManagement(newTypes, newExecutionCourse);
		
	beforeExecutionCourse.setShiftNames();
	if(!beforeExecutionCourse.equals(newExecutionCourse)) {
	    newExecutionCourse.setShiftNames();   
	}	
	
	if(!hasAnyCourseLoads()) {
	    throw new DomainException("error.Shift.empty.courseLoads");
	}
    }

    @Checked("ResourceAllocationRolePredicates.checkPermissionsToManageShifts")
    public void delete() {
	if (canBeDeleted()) {	    

	    final ExecutionCourse executionCourse = getExecutionCourse();

	    for (; hasAnyAssociatedLessons(); getAssociatedLessons().get(0).delete());
	    for (; hasAnyAssociatedShiftProfessorship(); getAssociatedShiftProfessorship().get(0).delete());
	    for (; hasAnyShiftDistributionEntries(); getShiftDistributionEntries().get(0).delete());
	    
	    getAssociatedClasses().clear();
	    getCourseLoads().clear();	    
	    removeRootDomainObject();
	    deleteDomainObject();

	    executionCourse.setShiftNames();

	} else {
	    throw new DomainException("shift.cannot.be.deleted");
	}
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkRequiredParameters() {
	return getLotacao() != null && !StringUtils.isEmpty(getNome());		 
    }

    @Deprecated
    public ExecutionCourse getDisciplinaExecucao() {
	return getExecutionCourse();
    }
    
    public ExecutionCourse getExecutionCourse() {
	return getCourseLoads().get(0).getExecutionCourse();
    }
    
    public ExecutionSemester getExecutionPeriod() {
	return getExecutionCourse().getExecutionPeriod();
    }
    
    private void shiftTypeManagement(Collection<ShiftType> types, ExecutionCourse executionCourse) {
	if(executionCourse != null) {
	    getCourseLoads().clear();
	    for (ShiftType shiftType : types) {
		CourseLoad courseLoad = executionCourse.getCourseLoadByShiftType(shiftType);
		if(courseLoad != null) {
		    addCourseLoads(courseLoad);
		}	    		
	    }
	}
    }
    
    public List<ShiftType> getTypes(){
	List<ShiftType> result = new ArrayList<ShiftType>();
	for (CourseLoad courseLoad : getCourseLoads()) {
	    result.add(courseLoad.getType());
	}
	return result;
    }
    
    public SortedSet<ShiftType> getSortedTypes(){
	SortedSet<ShiftType> result = new TreeSet<ShiftType>();
	for (CourseLoad courseLoad : getCourseLoads()) {
	    result.add(courseLoad.getType());
	}
	return result;
    }

    public boolean containsType(ShiftType shiftType) {
	if(shiftType != null) {
	    for (CourseLoad courseLoad : getCourseLoads()) {
		if(courseLoad.getType().equals(shiftType)) {
		    return true;
		}
	    }
	}
	return false;
    }
    
    public boolean canBeDeleted() {	
	if (hasAnyAssociatedStudentGroups()) {
	    throw new DomainException("error.deleteShift.with.studentGroups", getNome());
	}
	if (hasAnyStudents()) {
	    throw new DomainException("error.deleteShift.with.students", getNome());
	}
	if (hasAnyAssociatedSummaries()) {
	    throw new DomainException("error.deleteShift.with.summaries", getNome());
	}
	if (hasAnyDegreeTeachingServices()) {
	    throw new DomainException("error.deleteShift.with.degreeTeachingServices", getNome());
	}
	return true;
    }
        
    public BigDecimal getTotalHours() {
	List<Lesson> lessons = getAssociatedLessons();
	BigDecimal lessonTotalHours = BigDecimal.ZERO;
	for (Lesson lesson : lessons) {
	    lessonTotalHours = lessonTotalHours.add(lesson.getTotalHours());
	}	
	return lessonTotalHours;
    }

    public BigDecimal getMaxLessonDuration() {
	BigDecimal maxHours = BigDecimal.ZERO;
	for (Lesson lesson : getAssociatedLessons()) {
	    BigDecimal lessonHours = lesson.getUnitHours();
	    if(maxHours.compareTo(lessonHours) == -1) {
		maxHours = lessonHours;
	    }
	}	
	return maxHours;
    }
    
    public BigDecimal getUnitHours() {
	BigDecimal hours = BigDecimal.ZERO;
	List<Lesson> lessons = getAssociatedLessons();
	for (int i = 0; i < lessons.size(); i++) {
	    Lesson lesson = lessons.get(i);
	    hours = hours.add(lesson.getUnitHours());
	}
	return hours;
    }

    public double hoursAfter(int hour) {
	double hours = 0;
	List<Lesson> lessons = this.getAssociatedLessons();
	for (int i = 0; i < lessons.size(); i++) {
	    Lesson lesson = lessons.get(i);
	    hours += lesson.hoursAfter(hour);
	}
	return hours;
    }

    public void associateSchoolClass(SchoolClass schoolClass) {
	if (schoolClass == null) {
	    throw new NullPointerException();
	}
	if (!this.getAssociatedClasses().contains(schoolClass)) {
	    this.getAssociatedClasses().add(schoolClass);
	}
	if (!schoolClass.getAssociatedShifts().contains(this)) {
	    schoolClass.getAssociatedShifts().add(this);
	}
    }

    public Double getAvailableShiftPercentageForTeacher(Teacher teacher) {
	Double availablePercentage = 100.0;
	for (DegreeTeachingService degreeTeachingService : getDegreeTeachingServices()) {
	    /**
	     * if shift's type is LABORATORIAL the shift professorship
	     * percentage can exceed 100%
	     */
	    if (degreeTeachingService.getProfessorship().getTeacher() != teacher 
		    && (getCourseLoadsCount() != 1 || !containsType(ShiftType.LABORATORIAL))) {
		availablePercentage -= degreeTeachingService.getPercentage();
	    }
	}
	return availablePercentage;
    }

    public SortedSet<Lesson> getLessonsOrderedByWeekDayAndStartTime() {
	final SortedSet<Lesson> lessons = new TreeSet<Lesson>(Lesson.LESSON_COMPARATOR_BY_WEEKDAY_AND_STARTTIME);
	lessons.addAll(getAssociatedLessonsSet());
	return lessons;
    }

    public String getLessonsStringComparator() {
	final StringBuilder stringBuilder = new StringBuilder();
	for (final Lesson lesson : getLessonsOrderedByWeekDayAndStartTime()) {
	    stringBuilder.append(lesson.getDiaSemana().getDiaSemana().toString());
	    stringBuilder.append(lesson.getBeginHourMinuteSecond().toString());
	}
	return stringBuilder.toString();
    }
    
    public Integer getShiftTypesIntegerComparator() {
	final StringBuilder stringBuilder = new StringBuilder();
	for (ShiftType shiftType : getSortedTypes()) {	       
	    stringBuilder.append(shiftType.ordinal() + 1);
	}
	return Integer.valueOf(stringBuilder.toString());
    }

    public boolean reserveForStudent(final Registration registration) {
	if (getLotacao().intValue() > getStudentsCount()) {
	    addStudents(registration);
	    return true;
	} else {
	    return false;
	}
    }

    public SortedSet<ShiftEnrolment> getShiftEnrolmentsOrderedByDate() {
	final SortedSet<ShiftEnrolment> shiftEnrolments = new TreeSet<ShiftEnrolment>(ShiftEnrolment.COMPARATOR_BY_DATE);
	shiftEnrolments.addAll(getShiftEnrolmentsSet());
	return shiftEnrolments;
    }

    public String getClassesPrettyPrint() {
	StringBuilder builder = new StringBuilder();
	int index = 0;
	for (SchoolClass schoolClass : getAssociatedClasses()) {
	    builder.append(schoolClass.getNome());
	    index++;
	    if(index < getAssociatedClassesCount()) {
		builder.append(", ");
	    }
	}
	return builder.toString();
    }

    public String getShiftTypesPrettyPrint() {
	StringBuilder builder = new StringBuilder();	
	int index = 0;
	SortedSet<ShiftType> sortedTypes = getSortedTypes();
	for (ShiftType shiftType : sortedTypes) {
	    builder.append(enumerationResourcesBundle.getString(shiftType.getName()));
	    index++;
	    if(index < sortedTypes.size()) {
		builder.append(", ");
	    }
	}
	return builder.toString();
    }
    
    public String getShiftTypesCodePrettyPrint() {
	StringBuilder builder = new StringBuilder();	
	int index = 0;
	SortedSet<ShiftType> sortedTypes = getSortedTypes();
	for (ShiftType shiftType : sortedTypes) {
	    builder.append(shiftType.getSiglaTipoAula());
	    index++;	    
	    if(index < sortedTypes.size()) {
		builder.append(", ");
	    }
	}
	return builder.toString();
    }
    
    public List<Summary> getExtraSummaries(){
	List<Summary> result = new ArrayList<Summary>();
	Set<Summary> summaries = getAssociatedSummariesSet();
	for (Summary summary : summaries) {
	    if(summary.isExtraSummary()) {
		result.add(summary);
	    }
	} 
	return result;
    }
    
    private static class ShiftStudentListener extends dml.runtime.RelationAdapter<Registration, Shift> {

	@Override
	public void afterAdd(Registration registration, Shift shift) {
	    new ShiftEnrolment(shift, registration);
	}

	@Override
	public void afterRemove(Registration registration, Shift shift) {
	    shift.unEnrolStudent(registration);
	}
    }

    public void unEnrolStudent(final Registration registration) {
	final ShiftEnrolment shiftEnrolment = findShiftEnrolment(registration);
	if (shiftEnrolment != null) {
	    shiftEnrolment.delete();
	}
    }

    private ShiftEnrolment findShiftEnrolment(final Registration registration) {
	for (final ShiftEnrolment shiftEnrolment : getShiftEnrolmentsSet()) {
	    if (shiftEnrolment.getRegistration() == registration) {
		return shiftEnrolment;
	    }
	}
	return null;
    }
    
    public int getCapacityBasedOnSmallestRoom() {
	int capacity = 0;

	for (final Lesson lesson : getAssociatedLessonsSet()) {
	    if (lesson.hasSala()) {
		if (capacity == 0) {
		    capacity = ((AllocatableSpace)lesson.getSala()).getNormalCapacity();
		} else {
		    capacity = Math.min(capacity, ((AllocatableSpace)lesson.getSala()).getNormalCapacity());
		}
	    }
	}

	return capacity + (capacity / 10);
    }

}
