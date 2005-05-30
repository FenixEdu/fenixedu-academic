/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.InfoSupportLesson;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.ProfessorshipSupportLessonsDTO;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.teacher.professorship.IPersistentSupportLesson;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author jpvl
 */
public class ReadProfessorshipSupportLessons implements IService {

    public ProfessorshipSupportLessonsDTO run(Integer teacherId, Integer executionCourseId)
            throws FenixServiceException, ExcepcaoPersistencia {
        
        ProfessorshipSupportLessonsDTO professorshipSupportLessonsDTO = new ProfessorshipSupportLessonsDTO();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
        IProfessorship professorship = professorshipDAO.readByTeacherAndExecutionCourse(teacherId,
                executionCourseId);

        InfoProfessorship infoProfessorship = InfoProfessorshipWithAll.newInfoFromDomain(professorship);
        professorshipSupportLessonsDTO.setInfoProfessorship(infoProfessorship);
        IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

        List supportLessons = supportLessonDAO.readByProfessorship(professorship.getIdInternal());
        List infoSupportLessons = (List) CollectionUtils.collect(supportLessons, new Transformer() {

            public Object transform(Object input) {
                ISupportLesson supportLesson = (ISupportLesson) input;
                InfoSupportLesson infoSupportLesson = Cloner
                        .copyISupportLesson2InfoSupportLesson(supportLesson);
                return infoSupportLesson;
            }
        });

        professorshipSupportLessonsDTO.setInfoSupportLessonList(infoSupportLessons);
        return professorshipSupportLessonsDTO;

    }

}