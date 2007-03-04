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

/**
 * @author joaosa and rmalo
 *  
 */
public class ViewStudentsByTutor extends Service {

    public List run(String userName) throws FenixServiceException {
        if (userName == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        final List<InfoTutor> infoTutorStudents = new ArrayList<InfoTutor>();        
        final Teacher teacher = Teacher.readTeacherByUsername(userName);
        for (final Tutor tutor : teacher.getAssociatedTutors()) {
            if (tutor.getStudentCurricularPlan() != null) {
        	infoTutorStudents.add(InfoTutor.newInfoFromDomain(tutor));
            }
        }

        return infoTutorStudents;
    }

}
