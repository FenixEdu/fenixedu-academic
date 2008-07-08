package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsSummary;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoForReadStudentsWithAttendsByExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean.StudentAttendsStateType;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentStatuteType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.WorkingStudentSelectionType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

public class ReadStudentsWithAttendsByExecutionCourse extends Service {

    public Object run(Integer executionCourseCode, List curricularPlansIds, List enrollmentTypeFilters, List shiftIds,
	    List<WorkingStudentSelectionType> wsSelectionType) throws FenixServiceException, ExcepcaoPersistencia {

	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
	InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);

	final ExecutionCourseSite site = executionCourse.getSite();
	List attends = executionCourse.getAttendsEnrolledOrWithActiveSCP();

	List allDegreeCurricularPlans = getDegreeCurricularPlansFromAttends(attends);
	Set allShifts = executionCourse.getAssociatedShifts();
	List groupProperties = executionCourse.getGroupings();

	Map studentGroupsMap = getStudentGroupsMapFromGroupPropertiesList(groupProperties);

	InfoAttendsSummary infoAttendsSummary = new InfoAttendsSummary();
	int[] enrollmentDistribution = new int[10];

	// filter by Course
	if (curricularPlansIds != null) {
	    final List dcpIds = new ArrayList();
	    dcpIds.addAll(curricularPlansIds);

	    Predicate pCourses = new Predicate() {
		public boolean evaluate(Object o) {
		    Attends attendance = (Attends) o;

		    List scps = attendance.getRegistration().getStudentCurricularPlans();

		    if (scps != null && !scps.isEmpty()) {
			StudentCurricularPlan lastSCP = getStudentCurricularPlanFromAttends(attendance);

			final Integer lastDCPId = lastSCP.getDegreeCurricularPlan().getIdInternal();

			return dcpIds.contains(lastDCPId);
		    }
		    return false;
		}
	    };

	    attends = (List) CollectionUtils.select(attends, pCourses);
	}

	// filter by Enrollment type
	if (enrollmentTypeFilters != null) {
	    boolean enrolledFilter = enrollmentTypeFilters.contains(StudentAttendsStateType.ENROLED);
	    boolean notEnrolledFilter = enrollmentTypeFilters.contains(StudentAttendsStateType.NOT_ENROLED);
	    boolean improvementFilter = enrollmentTypeFilters.contains(StudentAttendsStateType.IMPROVEMENT);
	    boolean specialSeasonFilter = enrollmentTypeFilters.contains(StudentAttendsStateType.SPECIAL_SEASON);

	    List newAttends = new ArrayList();
	    Iterator attendsIterator = attends.iterator();
	    while (attendsIterator.hasNext()) {
		Attends attendacy = (Attends) attendsIterator.next();

		// improvement student (he/she is enrolled)
		if (improvementFilter
			&& attendacy.getEnrolment() != null
			&& (!attendacy.getEnrolment().getExecutionPeriod().equals(executionCourse.getExecutionPeriod()) && attendacy
				.getEnrolment().hasImprovement())) {
		    newAttends.add(attendacy);

		    // normal student (cannot be an improvement student)
		} else if (enrolledFilter
			&& attendacy.getEnrolment() != null
			&& !hasSpecialSeasonEnrolmentEvaluation(attendacy.getEnrolment().getEvaluations())
			&& (attendacy.getEnrolment().getExecutionPeriod().equals(executionCourse.getExecutionPeriod()) || (attendacy
				.getEnrolment().getCurricularCourse().isAnual(
					executionCourse.getExecutionPeriod().getExecutionYear()) && !attendacy.getEnrolment()
				.getExecutionPeriod().equals(executionCourse.getExecutionPeriod())))) {
		    newAttends.add(attendacy);
		    // not enrolled student
		} else if (notEnrolledFilter && attendacy.getEnrolment() == null) {
		    newAttends.add(attendacy);
		    // persistentSupportecial season student
		} else if (specialSeasonFilter && attendacy.getEnrolment() != null
			&& (attendacy.getEnrolment().getExecutionPeriod().equals(executionCourse.getExecutionPeriod()))
			&& hasSpecialSeasonEnrolmentEvaluation(attendacy.getEnrolment().getEvaluations())) {
		    newAttends.add(attendacy);
		}
	    }
	    attends = newAttends;
	}

	// filter by Shift
	if (shiftIds != null) {
	    Iterator shiftIterator = shiftIds.iterator();
	    List collectedAttends = new ArrayList();

	    while (shiftIterator.hasNext()) {

		Integer shiftId = (Integer) shiftIterator.next();
		final Shift turno = rootDomainObject.readShiftByOID(shiftId);

		Iterator attendsIterator = attends.iterator();

		while (attendsIterator.hasNext()) {
		    Attends attendance = (Attends) attendsIterator.next();

		    // if an attendance is related to a Shift
		    Registration registration = attendance.getRegistration();

		    if (turno.getStudents().contains(registration)) {
			collectedAttends.add(attendance);
		    }
		}
	    }
	    attends = collectedAttends;
	}

	if (wsSelectionType != null && allWSSelected(wsSelectionType)) {
	    boolean workingStudentSelected = wsSelectionType.contains(WorkingStudentSelectionType.WORKING_STUDENT);
	    boolean notWorkingStudentSelected = wsSelectionType.contains(WorkingStudentSelectionType.NOT_WORKING_STUDENT);
	    List<Attends> attendsList = new ArrayList<Attends>();
	    for (Attends attendacy : (List<Attends>) attends) {
		boolean isWorkingStudent = attendacy.getRegistration().getStudent().hasActiveStatuteInPeriod(
			StudentStatuteType.WORKING_STUDENT, executionCourse.getExecutionPeriod());
		if ((workingStudentSelected && isWorkingStudent) || (notWorkingStudentSelected && !isWorkingStudent)) {
		    attendsList.add(attendacy);
		}
	    }
	    attends = attendsList;
	}

	// building the info
	InfoForReadStudentsWithAttendsByExecutionCourse infoDTO = new InfoForReadStudentsWithAttendsByExecutionCourse();
	Set shifts = executionCourse.getAssociatedShifts();

	List infoCompositions = new ArrayList();
	Iterator it = attends.iterator();
	List<Registration> alreadyAddedStudents = new ArrayList<Registration>();

	final Map<Integer, InfoShift> clonedShifts = new HashMap<Integer, InfoShift>();

	while (it.hasNext()) {
	    Attends iFrequenta = (Attends) it.next();
	    Registration registrationToAdd = iFrequenta.getRegistration();
	    if (!alreadyAddedStudents.contains(registrationToAdd)) {
		alreadyAddedStudents.add(registrationToAdd);
		InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups infoComposition = new InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups();

		InfoFrequenta infoFrequenta = InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment.newInfoFromDomain(iFrequenta);
		infoComposition.setInfoAttends(infoFrequenta);

		// determining the EnrolmentEvaluationType
		if (iFrequenta.getEnrolment() != null) {
		    EnrolmentEvaluationType enrollmentEvaluationType = null;
		    if (!iFrequenta.getEnrolment().getExecutionPeriod().equals(executionCourse.getExecutionPeriod())
			    && iFrequenta.getEnrolment().hasImprovement()) {
			enrollmentEvaluationType = EnrolmentEvaluationType.IMPROVEMENT;
		    } else {
			if (hasSpecialSeasonEnrolmentEvaluation(iFrequenta.getEnrolment().getEvaluations())) {
			    enrollmentEvaluationType = EnrolmentEvaluationType.SPECIAL_SEASON;
			} else {
			    enrollmentEvaluationType = EnrolmentEvaluationType.NORMAL;
			}

		    }
		    infoFrequenta.setEnrolmentEvaluationType(enrollmentEvaluationType);
		}

		StudentCurricularPlan studentCurricularPlan = getStudentCurricularPlanFromAttends(iFrequenta);
		DegreeCurricularPlan degreeCP = studentCurricularPlan.getDegreeCurricularPlan();
		InfoDegreeCurricularPlan infoDCP = InfoDegreeCurricularPlan.newInfoFromDomain(degreeCP);

		infoComposition.setAttendingStudentInfoDCP(infoDCP);

		Map infoShifts = getShiftsByAttends(shifts, iFrequenta, clonedShifts);
		infoComposition.setInfoShifts(infoShifts);

		Map infoStudentGroups = getInfoStudentGroupsByAttends(studentGroupsMap, iFrequenta);
		infoComposition.setInfoStudentGroups(infoStudentGroups);

		Enrolment enrollment = iFrequenta.getEnrolment();
		int numberOfEnrollments = 0;

		if (enrollment != null) {
		    numberOfEnrollments = countAllEnrolmentsForSameStudent(studentCurricularPlan, enrollment
			    .getCurricularCourse().getName());
		}

		if (numberOfEnrollments >= enrollmentDistribution.length) {
		    int[] newDistribution = new int[Math.max(numberOfEnrollments + 1, enrollmentDistribution.length * 2)];
		    System.arraycopy(enrollmentDistribution, 0, newDistribution, 0, enrollmentDistribution.length);
		    enrollmentDistribution = newDistribution;
		}
		enrollmentDistribution[numberOfEnrollments]++;

		infoComposition.setNumberOfEnrollments(numberOfEnrollments);

		infoCompositions.add(infoComposition);
	    }
	}

	infoDTO.setInfoAttends(infoCompositions);
	infoDTO.setInfoExecutionCourse(infoExecutionCourse);

	Collection tipoAulas = getClassTypesFromExecutionCourse(executionCourse);
	infoDTO.setClassTypes(tipoAulas);

	infoDTO.setInfoShifts(getInfoShiftsFromList(allShifts));
	infoDTO.setInfoDegreeCurricularPlans(getInfoDegreeCurricularPlansFromList(allDegreeCurricularPlans));

	List infoGroupProperties = getInfoGroupPropertiesFromList(groupProperties);
	infoDTO.setInfoGroupProperties(infoGroupProperties);

	List enrollmentDistributionKeys = new ArrayList(enrollmentDistribution.length);
	Map enrollmentDistributionMap = new HashMap();
	for (int i = 0; i < enrollmentDistribution.length; i++) {
	    if (enrollmentDistribution[i] != 0) {
		Integer key = new Integer(i);
		Integer value = new Integer(enrollmentDistribution[i]);
		enrollmentDistributionKeys.add(key);
		enrollmentDistributionMap.put(key, value);
	    }
	}

	infoAttendsSummary.setEnrollmentDistribution(enrollmentDistributionMap);
	infoAttendsSummary.setNumberOfEnrollments(enrollmentDistributionKeys);
	infoDTO.setInfoAttendsSummary(infoAttendsSummary);

	TeacherAdministrationSiteView siteView = createSiteView(infoDTO, site);

	return siteView;
    }

    private boolean allWSSelected(List<WorkingStudentSelectionType> wsSelectionType) {
	return WorkingStudentSelectionType.values().length == wsSelectionType.size();
    }

    private int countAllEnrolmentsForSameStudent(StudentCurricularPlan studentCurricularPlan, String curricularCourseName) {
	int count = 0;
	Degree degree = studentCurricularPlan.getDegreeCurricularPlan().getDegree();
	for (StudentCurricularPlan scp : studentCurricularPlan.getRegistration().getStudentCurricularPlans()) {
	    if (scp.getDegreeCurricularPlan().getDegree() == degree) {
		for (Enrolment enrolment : scp.getEnrolments()) {
		    CurricularCourse course = enrolment.getCurricularCourse();
		    if (course != null) {
			String name = course.getName();
			if ((name == curricularCourseName) || ((name != null) && name.equals(curricularCourseName))) {
			    count++;
			}
		    }
		}
	    }
	}
	return count;
    }

    private TeacherAdministrationSiteView createSiteView(InfoForReadStudentsWithAttendsByExecutionCourse infoSiteStudents,
	    ExecutionCourseSite site) throws FenixServiceException, ExcepcaoPersistencia {

	TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
	ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null, null, null);

	return new TeacherAdministrationSiteView(commonComponent, infoSiteStudents);
    }

    StudentCurricularPlan getStudentCurricularPlanFromAttends(Attends attendance) {
	final Enrolment enrolment = attendance.getEnrolment();
	return enrolment == null ? attendance.getRegistration().getLastStudentCurricularPlan() : enrolment
		.getStudentCurricularPlan();
    }

    List getDegreeCurricularPlansFromAttends(List<Attends> attends) {
	List degreeCurricularPlans = new ArrayList();

	for (final Attends attendance : attends) {
	    DegreeCurricularPlan dcp = getStudentCurricularPlanFromAttends(attendance).getDegreeCurricularPlan();

	    if (!degreeCurricularPlans.contains(dcp))
		degreeCurricularPlans.add(dcp);
	}
	return degreeCurricularPlans;
    }

    private Map getShiftsByAttends(final Set<Shift> shifts, final Attends attend, final Map<Integer, InfoShift> clonedShifts)
	    throws ExcepcaoPersistencia {
	final Map result = new HashMap();

	for (final Shift shift : shifts) {

	    boolean studentInShift = false;
	    for (final Registration registration : shift.getStudents()) {
		if (registration == attend.getRegistration()) {
		    studentInShift = true;
		    break;
		}
	    }

	    if (studentInShift) {
		InfoShift infoShift = clonedShifts.get(shift.getIdInternal());
		if (infoShift == null) {
		    infoShift = InfoShift.newInfoFromDomain(shift);
		    clonedShifts.put(infoShift.getIdInternal(), infoShift);
		}
		result.put(shift.getShiftTypesCodePrettyPrint(), infoShift);
	    }
	}

	return result;
    }

    private Set getClassTypesFromExecutionCourse(ExecutionCourse executionCourse) {
	final Set<ShiftType> shiftTypes = new TreeSet<ShiftType>();
	for (final CourseLoad courseLoad : executionCourse.getCourseLoadsSet()) {
	    if (!courseLoad.isEmpty()) {
		shiftTypes.add(courseLoad.getType());
	    }
	}
	return shiftTypes;
    }

    private List getInfoShiftsFromList(Set<Shift> allShifts) {
	List result = new ArrayList();

	for (final Shift shift : allShifts) {
	    result.add(InfoShift.newInfoFromDomain(shift));
	}

	return result;
    }

    private List getInfoDegreeCurricularPlansFromList(List<DegreeCurricularPlan> degreeCPs) {
	List result = new ArrayList();

	for (final DegreeCurricularPlan degreeCurricularPlan : degreeCPs) {
	    result.add(InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan));
	}

	return result;
    }

    private List getInfoGroupPropertiesFromList(List<Grouping> groupProperties) {
	List result = new ArrayList();

	for (final Grouping grouping : groupProperties) {
	    result.add(InfoGrouping.newInfoFromDomain(grouping));
	}

	return result;
    }

    private Map getStudentGroupsMapFromGroupPropertiesList(List<Grouping> groupPropertiesList) throws ExcepcaoPersistencia {

	Map result = new HashMap();

	for (final Grouping grouping : groupPropertiesList) {
	    for (final StudentGroup studentGroup : grouping.getStudentGroupsSet()) {
		List groupAttends = studentGroup.getAttends();
		List attendsList = (List) CollectionUtils.collect(groupAttends, new Transformer() {
		    public Object transform(Object input) {
			Attends attendacy = (Attends) input;
			return attendacy;
		    }
		});
		result.put(studentGroup, attendsList);
	    }
	}

	return result;
    }

    private Map getInfoStudentGroupsByAttends(Map studentsGroupsAttendsListMap, Attends attends) {
	Map result = new HashMap();

	Collection studentsGroups = studentsGroupsAttendsListMap.keySet();

	for (Iterator groupsIterator = studentsGroups.iterator(); groupsIterator.hasNext();) {
	    StudentGroup sg = (StudentGroup) groupsIterator.next();

	    List attendsList = (List) studentsGroupsAttendsListMap.get(sg);

	    if (attendsList.contains(attends)) {
		String groupPropertiesName = sg.getGrouping().getName();
		InfoStudentGroup infoSG = InfoStudentGroup.newInfoFromDomain(sg);
		result.put(groupPropertiesName, infoSG);
	    }
	}

	return result;
    }

    private boolean hasSpecialSeasonEnrolmentEvaluation(final List<EnrolmentEvaluation> evaluations) {
	for (final EnrolmentEvaluation enrolmentEvaluation : evaluations) {
	    if (enrolmentEvaluation.getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON) {
		return true;
	    }
	}
	return false;
    }
}
