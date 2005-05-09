/*
 * Created on 3/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ITutor;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class InsertTutorShipWithOneStudent extends InsertTutorShip {
    public InsertTutorShipWithOneStudent() {

    }

    public Object run(Integer executionDegreeId, Integer teacherNumber, Integer studentNumber)
            throws FenixServiceException {
        if (teacherNumber == null || studentNumber == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        ISuportePersistente sp = null;
        Boolean result = Boolean.FALSE;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            //execution degree
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, executionDegreeId);
            String degreeCode = null;
            if (executionDegree != null && executionDegree.getDegreeCurricularPlan() != null
                    && executionDegree.getDegreeCurricularPlan().getDegree() != null) {
                degreeCode = executionDegree.getDegreeCurricularPlan().getDegree().getSigla();
            }

            //teacher
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
            if (teacher == null) {
                throw new NonExistingServiceException("error.tutor.unExistTeacher");
            }

            //student
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                    DegreeType.DEGREE);
            if (student == null) {
                throw new NonExistingServiceException("error.tutor.unExistStudent");
            }

            if (verifyStudentAlreadyTutor(student, teacher).booleanValue()) {
                //student already with tutor...
                throw new FenixServiceException("error.tutor.studentAlreadyWithTutor");
            }

            if (!verifyStudentOfThisDegree(student, DegreeType.DEGREE, degreeCode)
                    .booleanValue()) {
                //student doesn't belong to this degree
                throw new FenixServiceException("error.tutor.studentNoDegree");
            }

            IPersistentTutor persistentTutor = sp.getIPersistentTutor();
            ITutor tutor = persistentTutor.readTutorByTeacherAndStudent(teacher, student);
            if (tutor == null) {
                tutor = new Tutor();
                tutor.setTeacher(teacher);
                tutor.setStudent(student);

                persistentTutor.simpleLockWrite(tutor);
            }

            result = Boolean.TRUE;
        } catch (Exception e) {

            if (e.getMessage() == null) {
                throw new FenixServiceException("error.tutor.associateOneStudent");
            }
            throw new FenixServiceException(e.getMessage());

        }

        return result;
    }
}