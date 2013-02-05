package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalDegreeCurricularPlanInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalExecutionCourseInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalGroupInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalGroupPropertiesInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalShiftInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalStudentInfo;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * 
 *         Created at 11:23, February the 28th, 2005
 */
public class ReadStudentGroupsExternalInformationByExecutionCourseIDAndStudentUsername extends FenixService {
    @Service
    public static Collection run(Integer executionCourseID, String username) {
        Collection result = new ArrayList();
        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);

        List groupProperties = executionCourse.getGroupings();

        for (Iterator iter = groupProperties.iterator(); iter.hasNext();) {
            Grouping group = (Grouping) iter.next();
            InfoExternalStudentGroup foundGroup = searchForStudentInGrouping(group, username);
            if (foundGroup != null) {
                InfoExternalGroupPropertiesInfo infoGroupProperties = new InfoExternalGroupPropertiesInfo();
                infoGroupProperties.setGroup(foundGroup);
                infoGroupProperties.setMaxCapacity(Integer.valueOf(group.getMaximumCapacity().intValue()));
                infoGroupProperties.setMinCapacity(Integer.valueOf(group.getMinimumCapacity().intValue()));
                infoGroupProperties.setRecomendedCapacity(Integer.valueOf(group.getIdealCapacity().intValue()));
                infoGroupProperties.setName(new String(group.getName()));
                result.add(infoGroupProperties);
            }
        }
        return result;

    }

    private static InfoExternalStudentGroup searchForStudentInGrouping(Grouping group, String username) {
        InfoExternalStudentGroup result = null;
        Collection studentGroups = group.getStudentGroups();

        for (Iterator iter = studentGroups.iterator(); iter.hasNext();) {
            StudentGroup studentGroup = (StudentGroup) iter.next();
            List attends = studentGroup.getAttends();
            for (Iterator iterator = attends.iterator(); iterator.hasNext();) {
                Attends attend = (Attends) iterator.next();
                if (attend.getRegistration().getPerson().hasUsername(username)) {
                    InfoExternalStudentGroup info = new InfoExternalStudentGroup();
                    info.setInfoGroup(buildInfoExternalGroupInfo(studentGroup));
                    info.setStudents(buildStudentInfos(attends.iterator()));
                    result = info;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * @param iterator
     * @param info
     * @return
     * @throws ExcepcaoPersistencia
     */
    private static Collection buildStudentInfos(Iterator iterator) {
        Collection result = new ArrayList();
        for (Iterator iter = iterator; iter.hasNext();) {
            Attends attend = (Attends) iter.next();
            InfoExternalStudentInfo student = getStudentInformation(attend);
            result.add(student);
        }

        return result;

    }

    /**
     * @param studentGroupAttend
     * @param infoExternalStudentGroup
     * @throws ExcepcaoPersistencia
     * @throws ExcepcaoPersistencia
     */
    private static InfoExternalStudentInfo getStudentInformation(Attends attend) {
        InfoExternalStudentInfo student = new InfoExternalStudentInfo();
        student.setCourse(InfoExternalExecutionCourseInfo.newFromExecutionCourse(attend.getExecutionCourse()));
        student.setDegree(InfoExternalDegreeCurricularPlanInfo.newFromDegreeCurricularPlan(attend.getRegistration()
                .getActiveStudentCurricularPlan().getDegreeCurricularPlan()));
        student.setName(new String(attend.getRegistration().getPerson().getName()));
        student.setNumber(new Integer(attend.getRegistration().getNumber().intValue()));

        Collection shifts = findShifts(attend.getRegistration(), attend.getExecutionCourse());
        student.setShifts(buildShiftsInfo(shifts));

        return student;
    }

    /**
     * @param shifts
     * @param student
     */
    private static Collection buildShiftsInfo(Collection shifts) {
        ArrayList result = new ArrayList();
        for (Iterator iter = shifts.iterator(); iter.hasNext();) {
            Shift shift = (Shift) iter.next();
            result.add(InfoExternalShiftInfo.newFromShift(shift));
        }

        return result;
    }

    private static Collection findShifts(Registration aluno, ExecutionCourse disciplinaExecucao) {
        List<Shift> shifts = aluno.getShifts();
        List result = new ArrayList();
        for (Shift shift : shifts) {
            if (shift.getDisciplinaExecucao().equals(disciplinaExecucao)) {
                result.add(shift);
            }

        }
        return result;
    }

    /**
     * @param studentGroupAttend
     * @return
     */
    private static InfoExternalGroupInfo buildInfoExternalGroupInfo(StudentGroup studentGroup) {
        InfoExternalGroupInfo infoExternalGroupInfo = new InfoExternalGroupInfo();
        infoExternalGroupInfo.setNumber(Integer.valueOf(studentGroup.getGroupNumber().intValue()));
        infoExternalGroupInfo.setShift(InfoExternalShiftInfo.newFromShift(studentGroup.getShift()));
        infoExternalGroupInfo.setExecutionCourses(buildExecutionCoursesCollection(studentGroup.getGrouping()
                .getExecutionCourses()));

        return infoExternalGroupInfo;
    }

    /**
     * @param executionCourses
     * @return
     */
    private static Collection buildExecutionCoursesCollection(List executionCourses) {
        Collection result = new ArrayList();
        for (Iterator iter = executionCourses.iterator(); iter.hasNext();) {
            ExecutionCourse course = (ExecutionCourse) iter.next();
            result.add(InfoExternalExecutionCourseInfo.newFromExecutionCourse(course));
        }
        return result;
    }
}