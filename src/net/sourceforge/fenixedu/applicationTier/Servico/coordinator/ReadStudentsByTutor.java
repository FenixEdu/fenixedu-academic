/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTutor;
import net.sourceforge.fenixedu.dataTransferObject.InfoTutorWithInfoStudent;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ITutor;
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

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentDepartment persistentDepartment = sp.getIDepartamentoPersistente();
        IDepartment department = persistentDepartment.readByTeacher(tutorNumber);

        // Now only LEEC's teachers can be tutor
        if (!department.getCode().equals(new String("21"))) {
            throw new FenixServiceException("error.tutor.tutor.notLEEC");
        }

        IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
        ITeacher teacherDB = persistentTeacher.readByNumber(tutorNumber);

        List teacherAndStudentsList = new ArrayList();
        teacherAndStudentsList.add(InfoTeacherWithPerson.newInfoFromDomain(teacherDB));

        IPersistentTutor persistentTutor = sp.getIPersistentTutor();
        List<ITutor> tutorStudents = persistentTutor.readStudentsByTeacher(null, tutorNumber);
        if (tutorStudents == null || tutorStudents.size() <= 0) {
            return teacherAndStudentsList;
        }

        List<InfoTutor> infoTutorStudents = new ArrayList();
        for (ITutor tutor : tutorStudents) {
            infoTutorStudents.add(InfoTutorWithInfoStudent.newInfoFromDomain(tutor));
        }
        teacherAndStudentsList.add(infoTutorStudents);

        return teacherAndStudentsList;
    }
}