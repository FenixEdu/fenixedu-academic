package ServidorAplicacao.Servico.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Exam;
import Dominio.ExamStudentRoom;
import Dominio.IExam;
import Dominio.IExamStudentRoom;
import Dominio.IStudent;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExam;
import ServidorPersistente.IPersistentExamStudentRoom;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *  
 */

public class EnrollStudentInExam implements IService
{

    public EnrollStudentInExam()
    {
    }

    public Boolean run(String username, Integer examId) throws FenixServiceException
    {
       
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IStudent student = persistentStudent.readByUsername(username);
            IPersistentExam persistentExam = sp.getIPersistentExam();
            IPersistentExamStudentRoom persistentExamStudentRoom = sp.getIPersistentExamStudentRoom();
            IExam exam = new Exam();
            exam.setIdInternal(examId);
            exam = (IExam) persistentExam.readByOId(exam, true);
            if (exam == null || student == null)
            {

                throw new InvalidArgumentsServiceException();
            }

            IExamStudentRoom examStudentRoom = persistentExamStudentRoom.readBy(exam, student);
            if (examStudentRoom != null)
            {
                throw new ExistingServiceException();
            }
            examStudentRoom = new ExamStudentRoom();
            persistentExamStudentRoom.simpleLockWrite(examStudentRoom);
            examStudentRoom.setExam(exam);
            examStudentRoom.setStudent(student);

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

        return new Boolean(true);

    }

}
