/*
 * Created on 28/Ago/2003
 *
 */
package ServidorAplicacao.Servico.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.GroupProperties;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.Student;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.Servico.exceptions.NonValidChangeServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class GroupEnrolment implements IServico
{

    private static GroupEnrolment _servico = new GroupEnrolment();
    /**
     * The singleton access method of this class.
     **/
    public static GroupEnrolment getService()
    {
        return _servico;
    }

    /**
     * The actor of this class.
     **/
    private GroupEnrolment()
    {
    }

    /**
     * Devolve o nome do servico
     **/
    public final String getNome()
    {
        return "GroupEnrolment";
    }

    public boolean run(
        Integer groupPropertiesCode,
        Integer shiftCode,
        Integer groupNumber,
        List studentCodes,
        String username)
        throws FenixServiceException
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudentGroupAttend persistentStudentGroupAttend =
                sp.getIPersistentStudentGroupAttend();
            IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IFrequentaPersistente persistentAttend = sp.getIFrequentaPersistente();

            IGroupProperties groupProperties =
                (IGroupProperties) sp.getIPersistentGroupProperties().readByOId(
                    new GroupProperties(groupPropertiesCode),
                    false);
            ITurno shift = (ITurno) sp.getITurnoPersistente().readByOId(new Turno(shiftCode), false);

            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory =
                GroupEnrolmentStrategyFactory.getInstance();
            IGroupEnrolmentStrategy strategy =
                enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

            Integer result =
                strategy.enrolmentPolicyNewGroup(groupProperties, studentCodes.size() + 1, shift);

            if (result.equals(new Integer(-1)))
                throw new InvalidArgumentsServiceException();

            if (result.equals(new Integer(-2)))
                throw new NonValidChangeServiceException();

            if (result.equals(new Integer(-3)))
                throw new NotAuthorizedException();

            List allStudentGroup = new ArrayList();
            allStudentGroup =
                persistentStudentGroup.readAllStudentGroupByGroupProperties(groupProperties);

            IStudentGroup newStudentGroup =
                persistentStudentGroup.readStudentGroupByGroupPropertiesAndGroupNumber(
                    groupProperties,
                    groupNumber);

            if (newStudentGroup != null)
                throw new FenixServiceException();

            newStudentGroup = new StudentGroup(groupNumber, groupProperties, shift);
            persistentStudentGroup.lockWrite(newStudentGroup);

            IStudent userStudent = sp.getIPersistentStudent().readByUsername(username);
            IFrequenta userAttend =
                sp.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(
                    userStudent,
                    groupProperties.getExecutionCourse());

            Iterator iterGroups = allStudentGroup.iterator();
            while (iterGroups.hasNext())
            {
                IStudentGroup existingStudentGroup = (IStudentGroup) iterGroups.next();
                IStudentGroupAttend newStudentGroupAttend = null;
                Iterator iterator = studentCodes.iterator();

                while (iterator.hasNext())
                {
                    IStudent student =
                        (IStudent) persistentStudent.readByOId(
                            new Student((Integer) iterator.next()),
                            false);

                    IFrequenta attend =
                        persistentAttend.readByAlunoAndDisciplinaExecucao(
                            student,
                            groupProperties.getExecutionCourse());

                    newStudentGroupAttend =
                        persistentStudentGroupAttend.readBy(existingStudentGroup, attend);

                    if (newStudentGroupAttend != null)
                        throw new ExistingServiceException();

                }
                IStudentGroupAttend userStudentGroupAttend =
                    persistentStudentGroupAttend.readBy(existingStudentGroup, userAttend);

                if (userStudentGroupAttend != null)
                    throw new InvalidSituationServiceException();

            }

            Iterator iter = studentCodes.iterator();

            while (iter.hasNext())
            {

                IStudent student =
                    (IStudent) persistentStudent.readByOId(new Student((Integer) iter.next()), false);

                IFrequenta attend =
                    persistentAttend.readByAlunoAndDisciplinaExecucao(
                        student,
                        groupProperties.getExecutionCourse());

                IStudentGroupAttend notExistingSGAttend =
                    new StudentGroupAttend(newStudentGroup, attend);

                persistentStudentGroupAttend.lockWrite(notExistingSGAttend);
            }
            IStudentGroupAttend notExistingUserSGAttend =
                new StudentGroupAttend(newStudentGroup, userAttend);

            persistentStudentGroupAttend.lockWrite(notExistingUserSGAttend);

        } catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }
        return true;
    }

}
