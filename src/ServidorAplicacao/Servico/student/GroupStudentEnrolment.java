/*
 * Created on 27/Ago/2003
 *
 */

package ServidorAplicacao.Servico.student;

import java.util.Iterator;
import java.util.List;

import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorAplicacao.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import ServidorAplicacao.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *
 */
public class GroupStudentEnrolment implements IServico
{

    private static GroupStudentEnrolment _servico = new GroupStudentEnrolment();
    /**
     * The singleton access method of this class.
     **/
    public static GroupStudentEnrolment getService()
    {
        return _servico;
    }

    /**
     * The actor of this class.
     **/
    private GroupStudentEnrolment()
    {
    }

    /**
     * Devolve o nome do servico
     **/
    public final String getNome()
    {
        return "GroupStudentEnrolment";
    }

    public Boolean run(Integer studentGroupCode, String username) throws FenixServiceException
    {

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudentGroupAttend persistentStudentGroupAttend =
                sp.getIPersistentStudentGroupAttend();
            IPersistentStudentGroup persistentStudentGroup = sp.getIPersistentStudentGroup();

            IStudentGroup studentGroup =
                (IStudentGroup) persistentStudentGroup.readByOId(
                    new StudentGroup(studentGroupCode),
                    false);
            IStudent student = sp.getIPersistentStudent().readByUsername(username);

            if (studentGroup == null)
                throw new FenixServiceException();

            IFrequenta attend =
                sp.getIFrequentaPersistente().readByAlunoAndDisciplinaExecucao(
                    student,
                    studentGroup.getGroupProperties().getExecutionCourse());
            IStudentGroupAttend studentGroupAttend =
                persistentStudentGroupAttend.readBy(studentGroup, attend);

            if (studentGroupAttend != null)
                throw new InvalidSituationServiceException();

            IGroupProperties groupProperties = studentGroup.getGroupProperties();
            IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory =
                GroupEnrolmentStrategyFactory.getInstance();
            IGroupEnrolmentStrategy strategy =
                enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

            boolean result =
                strategy.checkPossibleToEnrolInExistingGroup(
                    groupProperties,
                    studentGroup,
                    studentGroup.getShift());
            if (!result)
                throw new InvalidArgumentsServiceException();

            IStudentGroupAttend newStudentGroupAttend = new StudentGroupAttend(studentGroup, attend);
            List allStudentGroup =
                persistentStudentGroup.readAllStudentGroupByGroupProperties(
                    studentGroup.getGroupProperties());
            Iterator iter = allStudentGroup.iterator();
            IStudentGroup group = null;
            IStudentGroupAttend existingStudentAttend = null;
            while (iter.hasNext())
            {
                group = (IStudentGroup) iter.next();
                existingStudentAttend = persistentStudentGroupAttend.readBy(group, attend);
                if (existingStudentAttend != null)
                    throw new InvalidSituationServiceException();

            }
            persistentStudentGroupAttend.lockWrite(newStudentGroupAttend);

        } catch (ExcepcaoPersistencia ex)
        {
            ex.printStackTrace();
        }
        return new Boolean(true);
    }
}