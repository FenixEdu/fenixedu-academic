package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalDegreeCurricularPlanInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalExecutionCourseInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalGroupInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalGroupPropertiesInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalShiftInfo;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.externalServices.InfoExternalStudentInfo;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * 
 * Created at 11:23, February the 28th, 2005
 */
public class ReadStudentGroupsExternalInformationByExecutionCourseIDAndStudentUsername implements
        IService {
    public Collection run(Integer executionCourseID, String username) throws ExcepcaoPersistencia {
        Collection result = new ArrayList();
        IPersistentExecutionCourse persistentExecutionCourse = PersistenceSupportFactory
                .getDefaultPersistenceSupport().getIPersistentExecutionCourse();
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseID);

        List groupProperties = executionCourse.getGroupings();

        for (Iterator iter = groupProperties.iterator(); iter.hasNext();) {
            IGrouping group = (IGrouping) iter.next();
            InfoExternalStudentGroup foundGroup = this
                    .searchForStudentInGrouping(group, username);
            if (foundGroup != null) {
                InfoExternalGroupPropertiesInfo infoGroupProperties = new InfoExternalGroupPropertiesInfo();
                infoGroupProperties.setGroup(foundGroup);
                infoGroupProperties.setMaxCapacity(new Integer(group.getMaximumCapacity().intValue()));
                infoGroupProperties.setMinCapacity(new Integer(group.getMinimumCapacity().intValue()));
                infoGroupProperties.setRecomendedCapacity(new Integer(group.getIdealCapacity()
                        .intValue()));
                infoGroupProperties.setName(new String(group.getName()));
                result.add(infoGroupProperties);
            }
        }
        return result;

    }

    private InfoExternalStudentGroup searchForStudentInGrouping(IGrouping group,
            String username) throws ExcepcaoPersistencia {
        InfoExternalStudentGroup result = null;
        Collection studentGroups = group.getStudentGroups();

        for (Iterator iter = studentGroups.iterator(); iter.hasNext();) {
            IStudentGroup studentGroup = (IStudentGroup) iter.next();
            List attends = studentGroup.getAttends();
            for (Iterator iterator = attends.iterator(); iterator.hasNext();) {
                IAttends attend = (IAttends) iterator.next();
                if (attend.getAluno().getPerson().getUsername().equals(
                        username)) {
                    InfoExternalStudentGroup info = new InfoExternalStudentGroup();
                    info.setInfoGroup(this.buildInfoExternalGroupInfo(studentGroup));
                    info.setStudents(this.buildStudentInfos(attends.iterator()));
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
    private Collection buildStudentInfos(Iterator iterator) throws ExcepcaoPersistencia {
        Collection result = new ArrayList();
        for (Iterator iter = iterator; iter.hasNext();) {
            IAttends attend = (IAttends) iter.next();
            InfoExternalStudentInfo student = this.getStudentInformation(attend);
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
    private InfoExternalStudentInfo getStudentInformation(IAttends attend)
            throws ExcepcaoPersistencia {
        InfoExternalStudentInfo student = new InfoExternalStudentInfo();
        student.setCourse(InfoExternalExecutionCourseInfo.newFromExecutionCourse(attend.getDisciplinaExecucao()));
        student.setDegree(InfoExternalDegreeCurricularPlanInfo
                .newFromDegreeCurricularPlan(attend.getAluno()
                        .getActiveStudentCurricularPlan().getDegreeCurricularPlan()));
        student.setName(new String(attend.getAluno().getPerson().getNome()));
        student.setNumber(new Integer(attend.getAluno().getNumber().intValue()));

        Collection shifts = this.findShifts(attend.getAluno(),
                attend.getDisciplinaExecucao());
        student.setShifts(this.buildShiftsInfo(shifts));

        return student;
    }

    /**
     * @param shifts
     * @param student
     */
    private Collection buildShiftsInfo(Collection shifts) {
        ArrayList result = new ArrayList();
        for (Iterator iter = shifts.iterator(); iter.hasNext();) {
            IShift shift = (IShift) iter.next();
            result.add(InfoExternalShiftInfo.newFromShift(shift));
        }

        return result;
    }

    private Collection findShifts(IStudent aluno, IExecutionCourse disciplinaExecucao)
            throws ExcepcaoPersistencia {
        List<IShift> shifts = aluno.getShifts();
        List result = new ArrayList();
        for (IShift shift : shifts) {
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
    private InfoExternalGroupInfo buildInfoExternalGroupInfo(IStudentGroup studentGroup) {
        InfoExternalGroupInfo infoExternalGroupInfo = new InfoExternalGroupInfo();
        infoExternalGroupInfo.setNumber(new Integer(studentGroup.getGroupNumber().intValue()));
        infoExternalGroupInfo.setShift(InfoExternalShiftInfo.newFromShift(studentGroup.getShift()));
        infoExternalGroupInfo.setExecutionCourses(this.buildExecutionCoursesCollection(studentGroup
                .getGrouping().getExecutionCourses()));

        return infoExternalGroupInfo;
    }

    /**
     * @param executionCourses
     * @return
     */
    private Collection buildExecutionCoursesCollection(List executionCourses) {
        Collection result = new ArrayList();
        for (Iterator iter = executionCourses.iterator(); iter.hasNext();) {
            IExecutionCourse course = (IExecutionCourse) iter.next();
            result.add(InfoExternalExecutionCourseInfo.newFromExecutionCourse(course));
        }
        return result;
    }
}
