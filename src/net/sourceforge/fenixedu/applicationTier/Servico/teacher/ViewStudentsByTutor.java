/*
 * Created on 21/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.dataTransferObject.InfoTutor;
import net.sourceforge.fenixedu.dataTransferObject.InfoTutorWithInfoStudent;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ITutor;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa and rmalo
 *  
 */
public class ViewStudentsByTutor implements IService {

    public ViewStudentsByTutor() {

    }

    /*
     * This service returns a list of students that tutor tutorizes
     */

    public List run(String userName) throws FenixServiceException {
        if (userName == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        List infoTutorStudents = new ArrayList();
        ISuportePersistente sp = null;
        try {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentTutor persistentTutor = sp.getIPersistentTutor();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            
            ITeacher teacher = persistentTeacher.readTeacherByUsername(userName);

            List tutorStudents = persistentTutor.readStudentsByTeacher(teacher);
            if (tutorStudents == null) {
                return infoTutorStudents;
            }

            ListIterator iteratorTutorStudents = tutorStudents.listIterator();
            while (iteratorTutorStudents.hasNext()) {
                ITutor tutor = (ITutor) iteratorTutorStudents.next();
                InfoTutor infoTutor = InfoTutorWithInfoStudent.newInfoFromDomain(tutor);
                infoTutorStudents.add(infoTutor);
            }

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }
       return infoTutorStudents;
    }
}