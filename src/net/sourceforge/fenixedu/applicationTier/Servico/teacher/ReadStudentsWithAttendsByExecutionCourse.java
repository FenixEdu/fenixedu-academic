package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import net.sourceforge.fenixedu.domain.Attends;
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
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.AttendacyStateSelectionType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

public class ReadStudentsWithAttendsByExecutionCourse extends Service {

    // doesnt allow an empty list
    private StudentCurricularPlan GetLastCurricularPlan(List cps) {
	Iterator i = cps.iterator();
	StudentCurricularPlan lastCP = (StudentCurricularPlan) cps.get(0);

	while (i.hasNext()) {
	    StudentCurricularPlan cp = (StudentCurricularPlan) i.next();
	    if (cp.getStartDateYearMonthDay().isAfter(lastCP.getStartDateYearMonthDay()))
		lastCP = cp;
	}
	return lastCP;
    }

    private StudentCurricularPlan GetActiveCurricularPlan(List cps) {
	Iterator i = cps.iterator();

	while (i.hasNext()) {
	    StudentCurricularPlan cp = (StudentCurricularPlan) i.next();
	    if (cp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE))
		return cp;
	}
	return GetLastCurricularPlan(cps);
    }

    public Object run(Integer executionCourseCode, List curricularPlansIds, List enrollmentTypeFilters,
	    List shiftIds) throws FenixServiceException, ExcepcaoPersistencia {

	final ExecutionCourse executionCourse = rootDomainObject
		.readExecutionCourseByOID(executionCourseCode);
	InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);

	final ExecutionCourseSite site = executionCourse.getSite();
	List attends = executionCourse.getAttends();

	List allDegreeCurricularPlans = getDegreeCurricularPlansFromAttends(attends);
	List allShifts = executionCourse.getAssociatedShifts();
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

		    List scps = attendance.getAluno().getStudentCurricularPlans();

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
	    boolean enrolledFilter = enrollmentTypeFilters
		    .contains(AttendacyStateSelectionType.ENROLLED);
	    boolean notEnrolledFilter = enrollmentTypeFilters
		    .contains(AttendacyStateSelectionType.NOT_ENROLLED);
	    boolean improvementFilter = enrollmentTypeFilters
		    .contains(AttendacyStateSelectionType.IMPROVEMENT);
	    boolean specialSeasonFilter = enrollmentTypeFilters
		    .contains(AttendacyStateSelectionType.SPECIAL_SEASON);

	    List newAttends = new ArrayList();
	    Iterator attendsIterator = attends.iterator();
	    while (attendsIterator.hasNext()) {
		Attends attendacy = (Attends) attendsIterator.next();

		// improvement student (he/she is enrolled)
		if (improvementFilter
			&& attendacy.getEnrolment() != null
			&& (!attendacy.getEnrolment().getExecutionPeriod().equals(
				executionCourse.getExecutionPeriod()))) {
		    newAttends.add(attendacy);

		    // normal student (cannot be an improvement student)
		} else if (enrolledFilter
			&& attendacy.getEnrolment() != null
			&& (attendacy.getEnrolment().getExecutionPeriod().equals(executionCourse
				.getExecutionPeriod()))
			&& !hasSpecialSeasonEnrolmentEvaluation(attendacy.getEnrolment()
				.getEvaluations())) {
		    newAttends.add(attendacy);
		    // not enrolled student
		} else if (notEnrolledFilter && attendacy.getEnrolment() == null) {
		    newAttends.add(attendacy);
		    // persistentSupportecial season student
		} else if (specialSeasonFilter
			&& attendacy.getEnrolment() != null
			&& (attendacy.getEnrolment().getExecutionPeriod().equals(executionCourse
				.getExecutionPeriod()))
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
		    Registration registration = attendance.getAluno();

		    if (turno.getStudents().contains(registration)) {
			collectedAttends.add(attendance);
		    }
		}
	    }
	    attends = collectedAttends;
	}

	// building the info
	InfoForReadStudentsWithAttendsByExecutionCourse infoDTO = new InfoForReadStudentsWithAttendsByExecutionCourse();
	List shifts = executionCourse.getAssociatedShifts();

	List infoCompositions = new ArrayList();
	Iterator it = attends.iterator();
	List<Registration> alreadyAddedStudents = new ArrayList<Registration>();

	final Map<Integer, InfoShift> clonedShifts = new HashMap<Integer, InfoShift>();

	while (it.hasNext()) {
	    Attends iFrequenta = (Attends) it.next();
	    Registration registrationToAdd = iFrequenta.getAluno();
	    if (!alreadyAddedStudents.contains(registrationToAdd)) {
		alreadyAddedStudents.add(registrationToAdd);
		InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups infoComposition = new InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups();

		InfoFrequenta infoFrequenta = InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment
			.newInfoFromDomain(iFrequenta);
		infoComposition.setInfoAttends(infoFrequenta);

		// determining the EnrolmentEvaluationType
		if (iFrequenta.getEnrolment() != null) {
		    EnrolmentEvaluationType enrollmentEvaluationType = null;
		    if (!iFrequenta.getEnrolment().getExecutionPeriod().equals(
			    executionCourse.getExecutionPeriod())) {
			enrollmentEvaluationType = EnrolmentEvaluationType.IMPROVEMENT;
		    } else {
			if (hasSpecialSeasonEnrolmentEvaluation(iFrequenta.getEnrolment()
				.getEvaluations())) {
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
		    numberOfEnrollments = countAllEnrolmentsForSameStudent(studentCurricularPlan,
			    enrollment.getCurricularCourse().getName());
		}

		if (numberOfEnrollments >= enrollmentDistribution.length) {
		    int[] newDistribution = new int[Math.max(numberOfEnrollments + 1,
			    enrollmentDistribution.length * 2)];
		    System.arraycopy(enrollmentDistribution, 0, newDistribution, 0,
			    enrollmentDistribution.length);
		    enrollmentDistribution = newDistribution;
		}
		enrollmentDistribution[numberOfEnrollments]++;

		infoComposition.setNumberOfEnrollments(numberOfEnrollments);

		infoCompositions.add(infoComposition);
	    }
	}

	infoDTO.setInfoAttends(infoCompositions);
	infoDTO.setInfoExecutionCourse(infoExecutionCourse);

	List tipoAulas = getClassTypesFromExecutionCourse(executionCourse);
	infoDTO.setClassTypes(tipoAulas);

	infoDTO.setInfoShifts(getInfoShiftsFromList(allShifts));
	infoDTO
		.setInfoDegreeCurricularPlans(getInfoDegreeCurricularPlansFromList(allDegreeCurricularPlans));

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

    private int countAllEnrolmentsForSameStudent(StudentCurricularPlan studentCurricularPlan,
	    String curricularCourseName) {
	int count = 0;
	Degree degree = studentCurricularPlan.getDegreeCurricularPlan().getDegree();
	for (StudentCurricularPlan scp : studentCurricularPlan.getRegistration().getStudentCurricularPlans()) {
	    if (scp.getDegreeCurricularPlan().getDegree() == degree) {
		for (Enrolment enrolment : scp.getEnrolments()) {
		    CurricularCourse course = enrolment.getCurricularCourse();
		    if (course != null) {
			String name = course.getName();
			if ((name == curricularCourseName)
				|| ((name != null) && name.equals(curricularCourseName))) {
			    count++;
			}
		    }
		}
	    }
	}
	return count;
    }

    private TeacherAdministrationSiteView createSiteView(
	    InfoForReadStudentsWithAttendsByExecutionCourse infoSiteStudents, ExecutionCourseSite site)
	    throws FenixServiceException, ExcepcaoPersistencia {

	TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
	ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
		null, null);

	TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
		infoSiteStudents);
	return siteView;
    }

    StudentCurricularPlan getStudentCurricularPlanFromAttends(Attends attendance) {
	if (attendance.getEnrolment() == null)
	    return GetActiveCurricularPlan(attendance.getAluno().getStudentCurricularPlans());
	return attendance.getEnrolment().getStudentCurricularPlan();
    }

    List getDegreeCurricularPlansFromAttends(List attends) {
	List degreeCurricularPlans = new ArrayList();

	Iterator attendsIterator = attends.iterator();

	while (attendsIterator.hasNext()) {
	    Attends attendance = (Attends) attendsIterator.next();
	    DegreeCurricularPlan dcp = getStudentCurricularPlanFromAttends(attendance)
		    .getDegreeCurricularPlan();

	    if (!degreeCurricularPlans.contains(dcp))
		degreeCurricularPlans.add(dcp);
	}
	return degreeCurricularPlans;
    }

    private Map getShiftsByAttends(final List shifts, final Attends attend,
	    final Map<Integer, InfoShift> clonedShifts) throws ExcepcaoPersistencia {
	final Map result = new HashMap();

	for (final Iterator iterator = shifts.iterator(); iterator.hasNext();) {
	    final Shift shift = (Shift) iterator.next();

	    boolean studentInShift = false;
	    List<Registration> students = shift.getStudents();
	    for (Registration registration : students) {
		if (registration == attend.getAluno()) {
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
		result.put(shift.getTipo().getSiglaTipoAula(), infoShift);
	    }
	}

	return result;
    }

    private List getClassTypesFromExecutionCourse(ExecutionCourse executionCourse) {
	List classTypes = new ArrayList();
	if (executionCourse.getTheoreticalHours().doubleValue() > 0) classTypes.add(ShiftType.TEORICA);
	if (executionCourse.getLabHours().doubleValue() > 0) classTypes.add(ShiftType.LABORATORIAL);
	if (executionCourse.getPraticalHours().doubleValue() > 0) classTypes.add(ShiftType.PRATICA);
	if (executionCourse.getTheoPratHours().doubleValue() > 0) classTypes.add(ShiftType.TEORICO_PRATICA);
        if (executionCourse.getSeminaryHours().doubleValue() > 0) classTypes.add(ShiftType.SEMINARY);
        if (executionCourse.getProblemsHours().doubleValue() > 0) classTypes.add(ShiftType.PROBLEMS);
        if (executionCourse.getFieldWorkHours().doubleValue() > 0) classTypes.add(ShiftType.FIELD_WORK);
        if (executionCourse.getTrainingPeriodHours().doubleValue() > 0) classTypes.add(ShiftType.TRAINING_PERIOD);
        if (executionCourse.getTutorialOrientationHours().doubleValue() > 0) classTypes.add(ShiftType.TUTORIAL_ORIENTATION);
	return classTypes;
    }

    private List getInfoShiftsFromList(List shifts) {
	List result = new ArrayList();

	for (Iterator shIterator = shifts.iterator(); shIterator.hasNext();) {
	    Shift sh = (Shift) shIterator.next();
	    result.add(InfoShift.newInfoFromDomain(sh));
	}

	return result;
    }

    private List getInfoDegreeCurricularPlansFromList(List degreeCPs) {
	List result = new ArrayList();

	for (Iterator dcpIterator = degreeCPs.iterator(); dcpIterator.hasNext();) {
	    DegreeCurricularPlan dcp = (DegreeCurricularPlan) dcpIterator.next();

	    result.add(InfoDegreeCurricularPlan.newInfoFromDomain(dcp));
	}

	return result;
    }

    private List getInfoGroupPropertiesFromList(List groupProperties) {
	List result = new ArrayList();

	for (Iterator gpIterator = groupProperties.iterator(); gpIterator.hasNext();) {
	    Grouping gp = (Grouping) gpIterator.next();
	    InfoGrouping infoGP = InfoGrouping.newInfoFromDomain(gp);
	    result.add(infoGP);
	}

	return result;
    }

    private Map getStudentGroupsMapFromGroupPropertiesList(List groupPropertiesList)
	    throws ExcepcaoPersistencia {

	Map result = new HashMap();
	List allStudentsGroups = new ArrayList();

	Iterator gpIt = groupPropertiesList.iterator();
	while (gpIt.hasNext()) {
	    allStudentsGroups.addAll(((Grouping) gpIt.next()).getStudentGroups());
	}

	for (Iterator sgIterator = allStudentsGroups.iterator(); sgIterator.hasNext();) {
	    StudentGroup sg = (StudentGroup) sgIterator.next();
	    List groupAttends = sg.getAttends();
	    List attendsList = (List) CollectionUtils.collect(groupAttends, new Transformer() {

		public Object transform(Object input) {
		    Attends attendacy = (Attends) input;
		    return attendacy;
		}
	    });

	    result.put(sg, attendsList);
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
	return CollectionUtils.exists(evaluations, new Predicate() {
	    public boolean evaluate(Object arg0) {
		EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) arg0;
		return enrolmentEvaluation.getEnrolmentEvaluationType().equals(
			EnrolmentEvaluationType.SPECIAL_SEASON);
	    }
	});
    }
}
