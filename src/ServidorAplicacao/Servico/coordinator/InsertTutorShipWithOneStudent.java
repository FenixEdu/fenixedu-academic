/*
 * Created on 3/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import Dominio.CursoExecucao;
import Dominio.ICursoExecucao;
import Dominio.IStudent;
import Dominio.ITeacher;
import Dominio.ITutor;
import Dominio.Tutor;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentTutor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class InsertTutorShipWithOneStudent extends InsertTutorShip {
    public InsertTutorShipWithOneStudent() {

    }

    public Object run(Integer executionDegreeId, Integer teacherNumber,
            Integer studentNumber) throws FenixServiceException {
        if (teacherNumber == null || studentNumber == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        ISuportePersistente sp = null;
        Boolean result = Boolean.FALSE;
        try {
            sp = SuportePersistenteOJB.getInstance();

            //execution degree
            ICursoExecucaoPersistente persistentExecutionDegree = sp
                    .getICursoExecucaoPersistente();
            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree
                    .readByOID(CursoExecucao.class, executionDegreeId);
            String degreeCode = null;
            if (executionDegree != null
                    && executionDegree.getCurricularPlan() != null
                    && executionDegree.getCurricularPlan().getDegree() != null) {
                degreeCode = executionDegree.getCurricularPlan().getDegree()
                        .getSigla();
            }

            //teacher
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
            if (teacher == null) {
                throw new NonExistingServiceException(
                        "error.tutor.unExistTeacher");
            }

            //student
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IStudent student = persistentStudent
                    .readStudentByNumberAndDegreeType(studentNumber,
                            TipoCurso.LICENCIATURA_OBJ);
            if (student == null) {
                throw new NonExistingServiceException(
                        "error.tutor.unExistStudent");
            }

            if (verifyStudentAlreadyTutor(student, teacher).booleanValue()) {
                //student already with tutor...
                throw new FenixServiceException(
                        "error.tutor.studentAlreadyWithTutor");
            }

            if (!verifyStudentOfThisDegree(student, TipoCurso.LICENCIATURA_OBJ,
                    degreeCode).booleanValue()) {
                //student doesn't belong to this degree
                throw new FenixServiceException("error.tutor.studentNoDegree");
            }

            IPersistentTutor persistentTutor = sp.getIPersistentTutor();
            ITutor tutor = persistentTutor.readTutorByTeacherAndStudent(
                    teacher, student);
            if (tutor == null) {
                tutor = new Tutor();
                tutor.setTeacher(teacher);
                tutor.setStudent(student);

                persistentTutor.simpleLockWrite(tutor);
            }

            result = Boolean.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() == null) {
                throw new FenixServiceException(
                        "error.tutor.associateOneStudent");
            } else {
                throw new FenixServiceException(e.getMessage());
            }
        }

        return result;
    }
}