package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsSummary;
import net.sourceforge.fenixedu.dataTransferObject.InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment;
import net.sourceforge.fenixedu.dataTransferObject.InfoCompositionOfAttendAndDegreeCurricularPlanAndShiftsAndStudentGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanWithDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoForReadStudentsWithAttendsByExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoExecutionCourseAndInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IEnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.AttendacyStateSelectionType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author André Fernandes / João Brito
 */
public class ReadStudentsWithAttendsByExecutionCourse implements IService {

    // doesnt allow an empty list
    private IStudentCurricularPlan GetLastCurricularPlan(List cps) {
        Iterator i = cps.iterator();
        IStudentCurricularPlan lastCP = (IStudentCurricularPlan) cps.get(0);

        while (i.hasNext()) {
            IStudentCurricularPlan cp = (IStudentCurricularPlan) i.next();
            if (cp.getStartDate().after(lastCP.getStartDate()))
                lastCP = cp;
        }
        return lastCP;
    }

    private IStudentCurricularPlan GetActiveCurricularPlan(List cps) {
        Iterator i = cps.iterator();

        while (i.hasNext()) {
            IStudentCurricularPlan cp = (IStudentCurricularPlan) i.next();
            if (cp.getCurrentState().equals(StudentCurricularPlanState.ACTIVE))
                return cp;
        }
        return GetLastCurricularPlan(cps);
    }

    public Object run(Integer executionCourseCode, List curricularPlansIds, List enrollmentTypeFilters,
            List shiftIds) throws FenixServiceException, ExcepcaoPersistencia {
        ISite site = null;

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                .readByOID(ExecutionCourse.class, executionCourseCode);
        InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
                .newInfoFromDomain(executionCourse);

        IPersistentSite persistentSite = sp.getIPersistentSite();
        site = persistentSite.readByExecutionCourse(executionCourseCode);

        List attends = executionCourse.getAttends();

        List allDegreeCurricularPlans = getDegreeCurricularPlansFromAttends(attends);
        List allShifts = sp.getITurnoPersistente()
                .readByExecutionCourse(executionCourse.getIdInternal());
        List groupProperties = executionCourse.getGroupings();

        Map studentGroupsMap = getStudentGroupsMapFromGroupPropertiesList(groupProperties, sp);

        InfoAttendsSummary infoAttendsSummary = new InfoAttendsSummary();
        int[] enrollmentDistribution = new int[10];

        // filter by Course
        if (curricularPlansIds != null) {
            final List dcpIds = new ArrayList();
            dcpIds.addAll(curricularPlansIds);

            Predicate pCourses = new Predicate() {
                public boolean evaluate(Object o) {
                    IAttends attendance = (IAttends) o;

                    List scps = attendance.getAluno().getStudentCurricularPlans();

                    if (scps != null && !scps.isEmpty()) {
                        IStudentCurricularPlan lastSCP = getStudentCurricularPlanFromAttends(attendance);

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
                IAttends attendacy = (IAttends) attendsIterator.next();

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
                                .getExecutionPeriod())) && !hasSpecialSeasonEnrolmentEvaluation(attendacy.getEnrolment().getEvaluations())) {
                    newAttends.add(attendacy);
                    // not enrolled student
                } else if (notEnrolledFilter && attendacy.getEnrolment() == null) {
                    newAttends.add(attendacy);
                    // special season student
                } else if (specialSeasonFilter && attendacy.getEnrolment() != null && (attendacy.getEnrolment().getExecutionPeriod().equals(executionCourse
                        .getExecutionPeriod())) && hasSpecialSeasonEnrolmentEvaluation(attendacy.getEnrolment().getEvaluations())) {
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
                final IShift turno = (IShift) sp.getITurnoPersistente().readByOID(Shift.class, shiftId);

                Iterator attendsIterator = attends.iterator();

                while (attendsIterator.hasNext()) {
                    IAttends attendance = (IAttends) attendsIterator.next();

                    // if an attendance is related to a Shift
                    IStudent student = attendance.getAluno();

                    if (turno.getStudents().contains(student)) {
                        collectedAttends.add(attendance);
                    }
                }
            }
            attends = collectedAttends;
        }

        // building the info
        InfoForReadStudentsWithAttendsByExecutionCourse infoDTO = new InfoForReadStudentsWithAttendsByExecutionCourse();
        List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse.getIdInternal());

        List infoCompositions = new ArrayList();
        Iterator it = attends.iterator();

        final Map<Integer, InfoShift> clonedShifts = new HashMap<Integer, InfoShift>();

        while (it.hasNext()) {
            IAttends iFrequenta = (IAttends) it.next();

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
        			if(hasSpecialSeasonEnrolmentEvaluation(iFrequenta.getEnrolment().getEvaluations())) {
        				enrollmentEvaluationType = EnrolmentEvaluationType.SPECIAL_SEASON;
        			}
        			else {
        				enrollmentEvaluationType = EnrolmentEvaluationType.NORMAL;
        			}
        			
        		}
        		infoFrequenta.getInfoEnrolment().setEnrolmentEvaluationType(enrollmentEvaluationType);
            }

            IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlanFromAttends(iFrequenta);
            IDegreeCurricularPlan degreeCP = studentCurricularPlan.getDegreeCurricularPlan();
            InfoDegreeCurricularPlan infoDCP = InfoDegreeCurricularPlanWithDegree
                    .newInfoFromDomain(degreeCP);

            infoComposition.setAttendingStudentInfoDCP(infoDCP);

            Map infoShifts = getShiftsByAttends(shifts, iFrequenta, sp, clonedShifts);
            infoComposition.setInfoShifts(infoShifts);

            Map infoStudentGroups = getInfoStudentGroupsByAttends(studentGroupsMap, iFrequenta);
            infoComposition.setInfoStudentGroups(infoStudentGroups);

            IEnrolment enrollment = iFrequenta.getEnrolment();
            int numberOfEnrollments = 0;

            if (enrollment != null) {
                numberOfEnrollments = countAllEnrolmentsForSameStudent(studentCurricularPlan, enrollment
                        .getCurricularCourse().getName());
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

    private int countAllEnrolmentsForSameStudent(IStudentCurricularPlan studentCurricularPlan,
            String curricularCourseName) {
        int count = 0;
        IDegree degree = studentCurricularPlan.getDegreeCurricularPlan().getDegree();
        for (IStudentCurricularPlan scp : studentCurricularPlan.getStudent().getStudentCurricularPlans()) {
            if (scp.getDegreeCurricularPlan().getDegree() == degree) {
                for (IEnrolment enrolment : scp.getEnrolments()) {
                    ICurricularCourse course = enrolment.getCurricularCourse();
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
            InfoForReadStudentsWithAttendsByExecutionCourse infoSiteStudents, ISite site)
            throws FenixServiceException, ExcepcaoPersistencia {

        TeacherAdministrationSiteComponentBuilder componentBuilder = new TeacherAdministrationSiteComponentBuilder();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        TeacherAdministrationSiteView siteView = new TeacherAdministrationSiteView(commonComponent,
                infoSiteStudents);
        return siteView;
    }

    IStudentCurricularPlan getStudentCurricularPlanFromAttends(IAttends attendance) {
        if (attendance.getEnrolment() == null)
            return GetActiveCurricularPlan(attendance.getAluno().getStudentCurricularPlans());
        return attendance.getEnrolment().getStudentCurricularPlan();
    }

    List getDegreeCurricularPlansFromAttends(List attends) {
        List degreeCurricularPlans = new ArrayList();

        Iterator attendsIterator = attends.iterator();

        while (attendsIterator.hasNext()) {
            IAttends attendance = (IAttends) attendsIterator.next();
            IDegreeCurricularPlan dcp = getStudentCurricularPlanFromAttends(attendance)
                    .getDegreeCurricularPlan();

            if (!degreeCurricularPlans.contains(dcp))
                degreeCurricularPlans.add(dcp);
        }
        return degreeCurricularPlans;
    }

    private Map getShiftsByAttends(final List shifts, final IAttends attend,
            final ISuportePersistente sp, final Map<Integer, InfoShift> clonedShifts)
            throws ExcepcaoPersistencia {
        final Map result = new HashMap();

        for (final Iterator iterator = shifts.iterator(); iterator.hasNext();) {
            final IShift shift = (IShift) iterator.next();

            boolean studentInShift = false;
            List<IStudent> students = shift.getStudents();
            for (IStudent student : students) {
                if (student == attend.getAluno()) {
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

    private List getClassTypesFromExecutionCourse(IExecutionCourse executionCourse) {
        List classTypes = new ArrayList();
        if (executionCourse.getTheoreticalHours().doubleValue() > 0)
            classTypes.add(ShiftType.TEORICA);

        if (executionCourse.getLabHours().doubleValue() > 0)
            classTypes.add(ShiftType.LABORATORIAL);

        if (executionCourse.getPraticalHours().doubleValue() > 0)
            classTypes.add(ShiftType.PRATICA);

        if (executionCourse.getTheoPratHours().doubleValue() > 0)
            classTypes.add(ShiftType.TEORICO_PRATICA);

        return classTypes;
    }

    private List getInfoShiftsFromList(List shifts) {
        List result = new ArrayList();

        for (Iterator shIterator = shifts.iterator(); shIterator.hasNext();) {
            IShift sh = (IShift) shIterator.next();
            result.add(InfoShiftWithInfoExecutionCourseAndInfoLessons.newInfoFromDomain(sh));
        }

        return result;
    }

    private List getInfoDegreeCurricularPlansFromList(List degreeCPs) {
        List result = new ArrayList();

        for (Iterator dcpIterator = degreeCPs.iterator(); dcpIterator.hasNext();) {
            IDegreeCurricularPlan dcp = (IDegreeCurricularPlan) dcpIterator.next();

            result.add(InfoDegreeCurricularPlanWithDegree.newInfoFromDomain(dcp));
        }

        return result;
    }

    private List getInfoGroupPropertiesFromList(List groupProperties) {
        List result = new ArrayList();

        for (Iterator gpIterator = groupProperties.iterator(); gpIterator.hasNext();) {
            IGrouping gp = (IGrouping) gpIterator.next();
            InfoGrouping infoGP = InfoGrouping.newInfoFromDomain(gp);
            result.add(infoGP);
        }

        return result;
    }

    private Map getStudentGroupsMapFromGroupPropertiesList(List groupPropertiesList,
            ISuportePersistente sp) throws ExcepcaoPersistencia {

        Map result = new HashMap();
        List allStudentsGroups = new ArrayList();

        Iterator gpIt = groupPropertiesList.iterator();
        while (gpIt.hasNext()) {
            allStudentsGroups
                    .addAll(((IGrouping) gpIt.next()).getStudentGroups());
        }

        for (Iterator sgIterator = allStudentsGroups.iterator(); sgIterator.hasNext();) {
            IStudentGroup sg = (IStudentGroup) sgIterator.next();
            List groupAttends = sg.getAttends();
            List attendsList = (List) CollectionUtils.collect(groupAttends, new Transformer() {

                public Object transform(Object input) {                    
                    IAttends attendacy = (IAttends) input;
                    return attendacy;
                }
            });

            result.put(sg, attendsList);
        }

        return result;
    }

    private Map getInfoStudentGroupsByAttends(Map studentsGroupsAttendsListMap, IAttends attends) {
        Map result = new HashMap();

        Collection studentsGroups = studentsGroupsAttendsListMap.keySet();

        for (Iterator groupsIterator = studentsGroups.iterator(); groupsIterator.hasNext();) {
            IStudentGroup sg = (IStudentGroup) groupsIterator.next();

            List attendsList = (List) studentsGroupsAttendsListMap.get(sg);

            if (attendsList.contains(attends)) {
                String groupPropertiesName = sg.getGrouping().getName();
                InfoStudentGroup infoSG = InfoStudentGroup.newInfoFromDomain(sg);
                result.put(groupPropertiesName, infoSG);
            }
        }

        return result;
    }
    
    private boolean hasImprovmentEnrolmentEvaluation(final List<IEnrolmentEvaluation> evaluations) {
    	return CollectionUtils.exists(evaluations, new Predicate() {
			public boolean evaluate(Object arg0) {
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) arg0;
				return enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT);
			}
    	});
    }
    
    private boolean hasSpecialSeasonEnrolmentEvaluation(final List<IEnrolmentEvaluation> evaluations) {
    	return CollectionUtils.exists(evaluations, new Predicate() {
			public boolean evaluate(Object arg0) {
				IEnrolmentEvaluation enrolmentEvaluation = (IEnrolmentEvaluation) arg0;
				return enrolmentEvaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.SPECIAL_SEASON);
			}
    	});
    }
}
