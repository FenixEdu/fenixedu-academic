/*
 * Created on 8/Ago/2003
 */

package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GroupProperties;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.ITurno;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author ansr & scpo
 */

public class CreateStudentGroup implements IService
{

    private IPersistentStudentGroup persistentStudentGroup = null;

    /**
     * The constructor of this class.
     */
    public CreateStudentGroup()
    {
    }

    private void checkIfStudentGroupExists(Integer groupNumber, IGroupProperties groupProperties)
            throws FenixServiceException
    {

        IStudentGroup studentGroup = null;

        try
        {

            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();

            studentGroup = persistentStudentGroup.readStudentGroupByGroupPropertiesAndGroupNumber(
                    groupProperties, groupNumber);
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        if (studentGroup != null)
            throw new ExistingServiceException();
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode, Integer groupNumber, Integer groupPropertiesCode,
            Integer shiftCode, List studentCodes) throws FenixServiceException
    {

        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        IPersistentGroupProperties persistentGroupProperites = null;
        IPersistentStudent persistentStudent = null;
        ITurnoPersistente persistentShift = null;
        IFrequentaPersistente persistentAttend = null;
        IPersistentStudentGroup persistentStudentGroup = null;

        try
        {

            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

            persistentGroupProperites = persistentSupport.getIPersistentGroupProperties();
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperites.readByOId(
                    new GroupProperties(groupPropertiesCode), false);

            persistentShift = persistentSupport.getITurnoPersistente();
            ITurno shift = (ITurno) persistentShift.readByOId(new Turno(shiftCode), false);

            checkIfStudentGroupExists(groupNumber, groupProperties);

            persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
            IStudentGroup newStudentGroup = new StudentGroup(groupNumber, groupProperties, shift);
            persistentStudentGroup.simpleLockWrite(newStudentGroup);

            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();

            List allStudentGroup = persistentStudentGroup
                    .readAllStudentGroupByGroupProperties(groupProperties);

            Iterator iterGroups = allStudentGroup.iterator();

            while (iterGroups.hasNext())
            {
                IStudentGroup existingStudentGroup = (IStudentGroup) iterGroups.next();
                IStudentGroupAttend newStudentGroupAttend = null;
                Iterator iterator = studentCodes.iterator();

                while (iterator.hasNext())
                {
                    IStudent student = persistentStudent.readByUsername((String) iterator.next());

                    IFrequenta attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student,
                            groupProperties.getExecutionCourse());

                    newStudentGroupAttend = persistentStudentGroupAttend.readBy(existingStudentGroup,
                            attend);

                    if (newStudentGroupAttend != null)
                        throw new InvalidSituationServiceException();

                }
            }

            Iterator iter = studentCodes.iterator();

            while (iter.hasNext())
            {

                IStudent student = persistentStudent.readByUsername((String) iter.next());

                IFrequenta attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student,
                        groupProperties.getExecutionCourse());

                IStudentGroupAttend notExistingSGAttend = new StudentGroupAttend(newStudentGroup, attend);

                persistentStudentGroupAttend.simpleLockWrite(notExistingSGAttend);
            }

        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;

    }
}