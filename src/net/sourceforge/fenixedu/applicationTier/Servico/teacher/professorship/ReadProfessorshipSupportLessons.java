/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.ProfessorshipSupportLessonsDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author jpvl
 */
public class ReadProfessorshipSupportLessons extends Service {

    public ProfessorshipSupportLessonsDTO run(Integer teacherId, Integer executionCourseId)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ProfessorshipSupportLessonsDTO professorshipSupportLessonsDTO = new ProfessorshipSupportLessonsDTO();

        final Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        final Professorship professorship = (teacher != null) ? teacher.getProfessorshipByExecutionCourse(executionCourse) : null;

        professorshipSupportLessonsDTO.setInfoProfessorship(InfoProfessorship.newInfoFromDomain(professorship));
        
        final List<InfoSupportLesson> infoSupportLessons = new ArrayList<InfoSupportLesson>(professorship.getSupportLessonsCount());
        for (final SupportLesson supportLesson : professorship.getSupportLessonsSet()) {
            infoSupportLessons.add(InfoSupportLesson.newInfoFromDomain(supportLesson));
        }
        professorshipSupportLessonsDTO.setInfoSupportLessonList(infoSupportLessons);
        
        return professorshipSupportLessonsDTO;
    }

}