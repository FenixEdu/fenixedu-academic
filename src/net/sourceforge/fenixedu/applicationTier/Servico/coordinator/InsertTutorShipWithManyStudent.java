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
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ITutor;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author Tânia Pousão
 *  
 */
public class InsertTutorShipWithManyStudent extends InsertTutorShip {

    public InsertTutorShipWithManyStudent() {

    }

    public Object run(Integer executionDegreeId, Integer teacherNumber, Integer studentNumberFirst,
            Integer studentNumberSecond) throws FenixServiceException {
        if (teacherNumber == null || studentNumberFirst == null || studentNumberSecond == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        ISuportePersistente sp = null;
        List studentsErrors = new ArrayList();
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

            //students in the range [studentNumberFirst, studentNumberSecond]
            for (int i = studentNumberFirst.intValue(); i <= studentNumberSecond.intValue(); i++) {
                IPersistentStudent persistentStudent = sp.getIPersistentStudent();
                Integer studentNumber = new Integer(i);
                IStudent student = persistentStudent.readStudentByNumberAndDegreeType(studentNumber,
                        TipoCurso.LICENCIATURA_OBJ);
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

                if (!verifyStudentOfThisDegree(student, TipoCurso.LICENCIATURA_OBJ, degreeCode)
                        .booleanValue()) {
                    //student doesn't belong to this degree
                    studentsErrors.add(studentNumber);
                    continue;
                }

                IPersistentTutor persistentTutor = sp.getIPersistentTutor();
                ITutor tutor = persistentTutor.readTutorByTeacherAndStudent(teacher, student);
                if (tutor == null) {
                    tutor = new Tutor();
                    tutor.setTeacher(teacher);
                    tutor.setStudent(student);

                    persistentTutor.simpleLockWrite(tutor);
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