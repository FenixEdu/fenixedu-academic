/*
 * Created on 23/Ago/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import Dominio.StudentGroup;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentGroup;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author asnr and scpo
 *  
 */

public class DeleteStudentGroupMembers implements IServico {

    private static DeleteStudentGroupMembers service = new DeleteStudentGroupMembers();

    /**
     * The singleton access method of this class.
     */
    public static DeleteStudentGroupMembers getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private DeleteStudentGroupMembers() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "DeleteStudentGroupMembers";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode, Integer studentGroupCode, List studentUsernames)
            throws FenixServiceException {

        IPersistentExecutionCourse persistentExecutionCourse = null;
        IPersistentStudentGroup persistentStudentGroup = null;
        IFrequentaPersistente persistentAttend = null;
        IPersistentStudent persistentStudent = null;
        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;

        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

            persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
            persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseCode);
            IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
                    StudentGroup.class, studentGroupCode);

            if (studentGroup == null) {
                throw new ExistingServiceException();
            }
            Iterator iterator = studentUsernames.iterator();

            while (iterator.hasNext()) {
                IStudent student = persistentStudent.readByUsername(iterator.next().toString());

                IFrequenta attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student,
                        executionCourse);

                IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend.readBy(
                        studentGroup, attend);
                if (oldStudentGroupAttend != null) {
                    persistentStudentGroupAttend.delete(oldStudentGroupAttend);
                } else {
                    throw new InvalidSituationServiceException();
                }
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}