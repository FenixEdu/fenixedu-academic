/*
 * Created on 21/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTutor;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa and rmalo
 *  
 */
public class ViewStudentsByTutor extends Service {

    /*
     * This service returns a list of students that tutor tutorizes
     */
    public List run(String userName) throws FenixServiceException, ExcepcaoPersistencia {
        if (userName == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        List<InfoTutor> infoTutorStudents = new ArrayList<InfoTutor>();        
        Teacher teacher = Teacher.readTeacherByUsername(userName);

        List<Tutor> tutorStudents = teacher.getAssociatedTutors();

        if (tutorStudents == null || tutorStudents.isEmpty()) {
            return infoTutorStudents;
        }

        for (Tutor tutor : tutorStudents) {
            infoTutorStudents.add(InfoTutor.newInfoFromDomain(tutor));
        }

        return infoTutorStudents;
    }

}
