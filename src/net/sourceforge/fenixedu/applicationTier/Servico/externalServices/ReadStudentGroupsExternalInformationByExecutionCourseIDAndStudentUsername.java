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
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IShiftStudent;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * 
 * Created at 11:23, February the 28th, 2005
 */
public class ReadStudentGroupsExternalInformationByExecutionCourseIDAndStudentUsername implements IService
{
    public Collection run(Integer executionCourseID, String username)
            throws ExcepcaoPersistencia
    {
        Collection result = new ArrayList();        
        IPersistentExecutionCourse persistentExecutionCourse = PersistenceSupportFactory.getDefaultPersistenceSupport().getIPersistentExecutionCourse();
        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, executionCourseID);

        List groupProperties = executionCourse.getGroupProperties();
        
        for (Iterator iter = groupProperties.iterator(); iter.hasNext();)
        {
            IGroupProperties group = (IGroupProperties) iter.next();
            InfoExternalStudentGroup foundGroup = this.searchForStudentInGroupProperties(group, username);
            if (foundGroup != null)
            {
                InfoExternalGroupPropertiesInfo infoGroupProperties = new InfoExternalGroupPropertiesInfo();
                infoGroupProperties.setGroup(foundGroup);
                infoGroupProperties.setMaxCapacity(new Integer(group.getMaximumCapacity().intValue()));
                infoGroupProperties.setMinCapacity(new Integer(group.getMinimumCapacity().intValue()));
                infoGroupProperties.setRecomendedCapacity(new Integer(group.getIdealCapacity().intValue()));
                infoGroupProperties.setName(new String(group.getName()));
                result.add(infoGroupProperties);
            }            
        }
        return result;

    }

    private InfoExternalStudentGroup searchForStudentInGroupProperties(IGroupProperties group, String username)
            throws ExcepcaoPersistencia
    {
        InfoExternalStudentGroup result = null;
        Collection studentGroups = group.getAttendsSet().getStudentGroups();
        IPersistentStudentGroupAttend persistentStudentGroupAttend = PersistenceSupportFactory.getDefaultPersistenceSupport()
                .getIPersistentStudentGroupAttend();

        for (Iterator iter = studentGroups.iterator(); iter.hasNext();)
        {
            IStudentGroup studentGroup = (IStudentGroup) iter.next();
            Collection studentGroupAttends = studentGroup.getStudentGroupAttends();
            for (Iterator iterator = studentGroupAttends.iterator(); iterator.hasNext();)
            {
                IStudentGroupAttend currentStudentGroupAttend = (IStudentGroupAttend) iterator.next();
                if (currentStudentGroupAttend.getAttend().getAluno().getPerson().getUsername().equals(
                        username))
                {
                    InfoExternalStudentGroup info = new InfoExternalStudentGroup();
                    info.setInfoGroup(this.buildInfoExternalGroupInfo(studentGroup));
                    info.setStudents(this.buildStudentInfos(studentGroupAttends.iterator()));
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
    private Collection buildStudentInfos(Iterator iterator) throws ExcepcaoPersistencia
    {
        Collection result = new ArrayList();
        for (Iterator iter = iterator; iter.hasNext();)
        {
            IStudentGroupAttend studentGroupAttend = (IStudentGroupAttend) iter.next();
            InfoExternalStudentInfo student = this.getStudentInformation(studentGroupAttend);
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
    private InfoExternalStudentInfo getStudentInformation(IStudentGroupAttend studentGroupAttend) throws ExcepcaoPersistencia
    {
        InfoExternalStudentInfo student = new InfoExternalStudentInfo();
        student.setCourse(InfoExternalExecutionCourseInfo.newFromExecutionCourse(studentGroupAttend.getAttend().getDisciplinaExecucao()));
        student.setDegree(InfoExternalDegreeCurricularPlanInfo.newFromDegreeCurricularPlan(studentGroupAttend.getAttend().getAluno().getActiveStudentCurricularPlan().getDegreeCurricularPlan()));
        student.setName(new String(studentGroupAttend.getAttend().getAluno().getPerson().getNome()));
        student.setNumber(new Integer (studentGroupAttend.getAttend().getAluno().getNumber().intValue()));
                        
        Collection shifts =  this.findShifts(studentGroupAttend.getAttend().getAluno(),studentGroupAttend.getAttend().getDisciplinaExecucao());
        student.setShifts(this.buildShiftsInfo(shifts));
                
        return student;
    }

    /**
     * @param shifts
     * @param student
     */
    private Collection buildShiftsInfo(Collection shifts)
    {
        ArrayList result = new ArrayList();
        for (Iterator iter = shifts.iterator(); iter.hasNext();)
        {
            IShiftStudent shiftStudent = (IShiftStudent) iter.next();
            result.add(InfoExternalShiftInfo.newFromShift(shiftStudent.getShift()));            
        }
        
        return result;
    }

    private Collection findShifts(IStudent aluno, IExecutionCourse disciplinaExecucao) throws ExcepcaoPersistencia
    {
        ITurnoAlunoPersistente persistentStudentShift = PersistenceSupportFactory.getDefaultPersistenceSupport().getITurnoAlunoPersistente();
        return persistentStudentShift.readByStudentAndExecutionCourse(aluno.getIdInternal(), disciplinaExecucao.getIdInternal());
    }

    /**
     * @param studentGroupAttend
     * @return
     */
    private InfoExternalGroupInfo buildInfoExternalGroupInfo(IStudentGroup studentGroup)
    {
        InfoExternalGroupInfo infoExternalGroupInfo = new InfoExternalGroupInfo();
        infoExternalGroupInfo.setNumber(new Integer(studentGroup.getGroupNumber().intValue()));
        infoExternalGroupInfo.setShift(InfoExternalShiftInfo.newFromShift(studentGroup.getShift()));
        infoExternalGroupInfo.setExecutionCourses(this.buildExecutionCoursesCollection(studentGroup.getAttendsSet().getGroupProperties().getExecutionCourses()));
        
        return infoExternalGroupInfo;
    }

    /**
     * @param executionCourses
     * @return
     */
    private Collection buildExecutionCoursesCollection(List executionCourses)
    {
        Collection result = new ArrayList();
        for (Iterator iter = executionCourses.iterator(); iter.hasNext();)
        {
            IExecutionCourse course = (IExecutionCourse) iter.next();
            result.add(InfoExternalExecutionCourseInfo.newFromExecutionCourse(course));
        }
        return result;
    }
}
