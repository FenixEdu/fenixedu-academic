/*
 * Created on 23/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.Student;
import Dominio.StudentGroup;
import Dominio.StudentGroupAttend;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
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

public class InsertStudentGroupMembers implements IService {

    /**
     * The constructor of this class.
     */
    public InsertStudentGroupMembers() {
    }

    /**
     * Executes the service.
     */

    public Boolean run(Integer executionCourseCode, Integer studentGroupCode,
            List studentCodes) throws FenixServiceException {

        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        IPersistentStudentGroup persistentStudentGroup = null;
        IFrequentaPersistente persistentAttend = null;
        IPersistentStudent persistentStudent = null;
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentStudentGroup = persistentSupport
                    .getIPersistentStudentGroup();
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentStudentGroupAttend = persistentSupport
                    .getIPersistentStudentGroupAttend();

            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup
                    .readByOID(StudentGroup.class, studentGroupCode);

            if (studentGroup == null) {
                throw new ExistingServiceException();
            }

            IGroupProperties groupProperties = studentGroup
                    .getGroupProperties();
            List allStudentGroup = persistentStudentGroup
                    .readAllStudentGroupByGroupProperties(groupProperties);

            Iterator iterGroups = allStudentGroup.iterator();

            while (iterGroups.hasNext()) {
                IStudentGroup existingStudentGroup = (IStudentGroup) iterGroups
                        .next();
                IStudentGroupAttend newStudentGroupAttend = null;
                Iterator iterator = studentCodes.iterator();

                while (iterator.hasNext()) {
                    IStudent student = (IStudent) persistentStudent.readByOID(
                            Student.class, (Integer) iterator.next());

                    IFrequenta attend = persistentAttend
                            .readByAlunoAndDisciplinaExecucao(student,
                                    groupProperties.getExecutionCourse());

                    newStudentGroupAttend = persistentStudentGroupAttend
                            .readBy(existingStudentGroup, attend);

                    if (newStudentGroupAttend != null)
                        throw new InvalidSituationServiceException();

                }
            }

            Iterator iter = studentCodes.iterator();

            while (iter.hasNext()) {

                IStudent student = (IStudent) persistentStudent.readByOID(
                        Student.class, (Integer) iter.next());

                IFrequenta attend = persistentAttend
                        .readByAlunoAndDisciplinaExecucao(student,
                                groupProperties.getExecutionCourse());

                IStudentGroupAttend notExistingSGAttend = new StudentGroupAttend(
                        studentGroup, attend);

                persistentStudentGroupAttend
                        .simpleLockWrite(notExistingSGAttend);
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return new Boolean(true);
    }
}