/*
 * Created on 3/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;

/**
 * @author Tânia Pousão
 * 
 */
public class InsertTutorShipWithOneStudent extends InsertTutorShip {

    public Object run(Integer executionDegreeId, Integer teacherNumber, Integer studentNumber)
            throws FenixServiceException {
        if (teacherNumber == null || studentNumber == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        Boolean result = Boolean.FALSE;
        try {

            // execution degree
            ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
            String degreeCode = null;
            if (executionDegree != null && executionDegree.getDegreeCurricularPlan() != null
                    && executionDegree.getDegreeCurricularPlan().getDegree() != null) {
                degreeCode = executionDegree.getDegreeCurricularPlan().getDegree().getSigla();
            }

            // teacher
            Teacher teacher = Teacher.readByNumber(teacherNumber);
            if (teacher == null) {
                throw new NonExistingServiceException("error.tutor.unExistTeacher");
            }

            // student
            Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, DegreeType.DEGREE);
            if (registration == null) {
                throw new NonExistingServiceException("error.tutor.unExistStudent");
            }

            if (verifyStudentAlreadyTutor(registration, teacher).booleanValue()) {
                // student already with tutor...
                throw new FenixServiceException("error.tutor.studentAlreadyWithTutor");
            }

            if (!verifyStudentOfThisDegree(registration, DegreeType.DEGREE, degreeCode).booleanValue()) {
                // student doesn't belong to this degree
                throw new FenixServiceException("error.tutor.studentNoDegree");
            }

            Tutor tutor = registration.getAssociatedTutor();
            if (tutor == null) {
                tutor = new Tutor();
                tutor.setTeacher(teacher);
                tutor.setStudent(registration);
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