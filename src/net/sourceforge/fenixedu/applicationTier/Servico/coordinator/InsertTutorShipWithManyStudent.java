/*
 * Created on 3/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Teacher;
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
public class InsertTutorShipWithManyStudent extends InsertTutorShip {

    public Object run(Integer executionDegreeId, Integer teacherNumber, Integer studentNumberFirst,
            Integer studentNumberSecond) throws FenixServiceException {
        if (teacherNumber == null || studentNumberFirst == null || studentNumberSecond == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        ISuportePersistente sp = null;
        List<Integer> studentsErrors = new ArrayList<Integer>();
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            //execution degree
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            ExecutionDegree executionDegree = (ExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, executionDegreeId);
            String degreeCode = null;
            if (executionDegree != null && executionDegree.getDegreeCurricularPlan() != null
                    && executionDegree.getDegreeCurricularPlan().getDegree() != null) {
                degreeCode = executionDegree.getDegreeCurricularPlan().getDegree().getSigla();
            }

            //teacher
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            Teacher teacher = persistentTeacher.readByNumber(teacherNumber);
            if (teacher == null) {
                throw new NonExistingServiceException("error.tutor.unExistTeacher");
            }

            //students in the range [studentNumberFirst, studentNumberSecond]
            for (int i = studentNumberFirst.intValue(); i <= studentNumberSecond.intValue(); i++) {
                IPersistentStudent persistentStudent = sp.getIPersistentStudent();
                Integer studentNumber = new Integer(i);
                Student student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                        DegreeType.DEGREE);
                if (student == null) {
                    //student doesn't exists...
                    studentsErrors.add(studentNumber);
                    continue;
                }

                if (verifyStudentAlreadyTutor(student, teacher).booleanValue()) {
                    //student already with tutor...
                    studentsErrors.add(studentNumber);
                    continue;
                }

                if (!verifyStudentOfThisDegree(student, DegreeType.DEGREE, degreeCode)
                        .booleanValue()) {
                    //student doesn't belong to this degree
                    studentsErrors.add(studentNumber);
                    continue;
                }

                IPersistentTutor persistentTutor = sp.getIPersistentTutor();
                Tutor tutor = persistentTutor.readTutorByTeacherAndStudent(teacher, student);
                if (tutor == null) {
                    tutor = DomainFactory.makeTutor();
                    tutor.setTeacher(teacher);
                    tutor.setStudent(student);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage() == null) {
                throw new FenixServiceException("error.tutor.associateManyStudent");
            }
            throw new FenixServiceException(e.getMessage());

        }

        //return student's number list that unchained an error
        return studentsErrors;
    }
}