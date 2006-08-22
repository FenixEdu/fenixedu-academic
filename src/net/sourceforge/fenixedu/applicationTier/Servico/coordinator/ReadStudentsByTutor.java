/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTutor;
import net.sourceforge.fenixedu.dataTransferObject.InfoTutorWithInfoStudent;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Tânia Pousão
 * 
 */
public class ReadStudentsByTutor extends Service {

    /*
     * This service returns a list with size two: first element is infoTeacher
     * that is tutor second element is the list of students that this tutor
     * tutorizes
     * 
     */

    public List run(Integer executionDegreeId, Integer tutorNumber) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (tutorNumber == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        Teacher teacherDB = Teacher.readByNumber(tutorNumber);
        if (teacherDB == null) {
            throw new FenixServiceException("error.noTutor");
        }
        
        Department department = teacherDB.getCurrentWorkingDepartment();

        // Now only LEEC's teachers can be tutor
        if (!department.getCode().equals(new String("21"))) {
            throw new FenixServiceException("error.tutor.tutor.notLEEC");
        }

        List teacherAndStudentsList = new ArrayList();
        teacherAndStudentsList.add(InfoTeacher.newInfoFromDomain(teacherDB));

        List<Tutor> tutorStudents = Tutor.getAllTutorsByTeacherNumber(tutorNumber);
        if (tutorStudents == null || tutorStudents.isEmpty()) {
            return teacherAndStudentsList;
        }

        List<InfoTutor> infoTutorStudents = new ArrayList();
        for (Tutor tutor : tutorStudents) {
            infoTutorStudents.add(InfoTutorWithInfoStudent.newInfoFromDomain(tutor));
        }
        teacherAndStudentsList.add(infoTutorStudents);

        return teacherAndStudentsList;
    }
}