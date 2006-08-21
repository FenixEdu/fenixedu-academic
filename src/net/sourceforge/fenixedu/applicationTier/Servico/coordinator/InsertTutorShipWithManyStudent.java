/*
 * Created on 3/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

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
public class InsertTutorShipWithManyStudent extends InsertTutorShip {

    public Object run(Integer executionDegreeId, Integer teacherNumber, Integer studentNumberFirst,
            Integer studentNumberSecond) throws FenixServiceException {
        if (teacherNumber == null || studentNumberFirst == null || studentNumberSecond == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        List<Integer> studentsErrors = new ArrayList<Integer>();
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

            // students in the range [studentNumberFirst, studentNumberSecond]
            for (int i = studentNumberFirst.intValue(); i <= studentNumberSecond.intValue(); i++) {
                Integer studentNumber = new Integer(i);
                Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber,
                        DegreeType.DEGREE);
                if (registration == null) {
                    // student doesn't exists...
                    studentsErrors.add(studentNumber);
                    continue;
                }

                if (verifyStudentAlreadyTutor(registration, teacher).booleanValue()) {
                    // student already with tutor...
                    studentsErrors.add(studentNumber);
                    continue;
                }

                if (!verifyStudentOfThisDegree(registration, DegreeType.DEGREE, degreeCode).booleanValue()) {
                    // student doesn't belong to this degree
                    studentsErrors.add(studentNumber);
                    continue;
                }

                Tutor tutor = registration.getAssociatedTutor();
                if (tutor == null) {
                    tutor = new Tutor();
                    tutor.setTeacher(teacher);
                    tutor.setStudent(registration);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() == null) {
                throw new FenixServiceException("error.tutor.associateManyStudent");
            }
            throw new FenixServiceException(e.getMessage());

        }

        // return student's number list that unchained an error
        return studentsErrors;
    }
}