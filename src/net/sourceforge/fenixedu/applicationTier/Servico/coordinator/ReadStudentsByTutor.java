/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTutor;
import net.sourceforge.fenixedu.dataTransferObject.InfoTutorWithInfoStudent;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ITutor;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDepartment;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class ReadStudentsByTutor implements IService {

    public ReadStudentsByTutor() {

    }

    /*
     * This service returns a list with size two: first element is infoTeacher
     * that is tutor second element is the list of students that this tutor
     * tutorizes
     *  
     */

    public List run(Integer executionDegreeId, Integer tutorNumber) throws FenixServiceException {
        if (tutorNumber == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        List teacherAndStudentsList = null;
        ISuportePersistente sp = null;
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IPersistentTutor persistentTutor = sp.getIPersistentTutor();

            ITeacher teacher = new Teacher();
            teacher.setTeacherNumber(tutorNumber);

            IPersistentDepartment persistentDepartment = sp.getIDepartamentoPersistente();
            IDepartment department = persistentDepartment.readByTeacher(teacher);

            //Now only LEEC's teachers can be tutor
            if (!department.getCode().equals(new String("21"))) {
                throw new FenixServiceException("error.tutor.tutor.notLEEC");
            }

            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacherDB = persistentTeacher.readByNumber(tutorNumber);

            teacherAndStudentsList = new ArrayList();
            teacherAndStudentsList.add(InfoTeacherWithPerson.newInfoFromDomain(teacherDB));

            List tutorStudents = persistentTutor.readStudentsByTeacher(teacher);
            if (tutorStudents == null || tutorStudents.size() <= 0) {
                return teacherAndStudentsList;
            }

            List infoTutorStudents = new ArrayList();
            ListIterator iterator = tutorStudents.listIterator();
            while (iterator.hasNext()) {
                ITutor tutor = (ITutor) iterator.next();

                InfoTutor infoTutor = InfoTutorWithInfoStudent.newInfoFromDomain(tutor);
                infoTutorStudents.add(infoTutor);
            }
            teacherAndStudentsList.add(infoTutorStudents);
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        return teacherAndStudentsList;
    }
}