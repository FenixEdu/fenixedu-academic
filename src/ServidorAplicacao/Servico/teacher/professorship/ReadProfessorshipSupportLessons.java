/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoProfessorship;
import DataBeans.teacher.professorship.InfoSupportLesson;
import DataBeans.teacher.professorship.ProfessorshipSupportLessonsDTO;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ISupportLesson;
import Dominio.Teacher;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;

/**
 * @author jpvl
 */
public class ReadProfessorshipSupportLessons implements IServico
{
    private static ReadProfessorshipSupportLessons service = new ReadProfessorshipSupportLessons();

    /**
	 * The singleton access method of this class.
	 */
    public static ReadProfessorshipSupportLessons getService()
    {
        return service;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorAplicacao.IServico#getNome()
	 */
    public String getNome()
    {
        return "ReadProfessorshipSupportLessons";
    }

    public ProfessorshipSupportLessonsDTO run(Integer teacherId, Integer executionCourseId)
        throws FenixServiceException
    {
        ProfessorshipSupportLessonsDTO professorshipSupportLessonsDTO =
            new ProfessorshipSupportLessonsDTO();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentProfessorship professorshipDAO = sp.getIPersistentProfessorship();
            IProfessorship professorship =
                professorshipDAO.readByTeacherIDAndExecutionCourseID(
                    new Teacher(teacherId),
                    new ExecutionCourse(executionCourseId));

            InfoProfessorship infoProfessorship =
                Cloner.copyIProfessorship2InfoProfessorship(professorship);

            professorshipSupportLessonsDTO.setInfoProfessorship(infoProfessorship);

            IPersistentSupportLesson supportLessonDAO = sp.getIPersistentSupportLesson();

            List supportLessons = supportLessonDAO.readByProfessorship(professorship);
            List infoSupportLessons = (List) CollectionUtils.collect(supportLessons, new Transformer()
            {

                public Object transform(Object input)
                {
                    ISupportLesson supportLesson = (ISupportLesson) input;
                    InfoSupportLesson infoSupportLesson =
                        Cloner.copyISupportLesson2InfoSupportLesson(supportLesson);
                    return infoSupportLesson;
                }
            });

            professorshipSupportLessonsDTO.setInfoSupportLessonList(infoSupportLessons);

        }
        catch (ExcepcaoPersistencia e)
        {
            e.printStackTrace(System.out);
            throw new FenixServiceException("Problems on database!", e);
        }

        return professorshipSupportLessonsDTO;

    }

}
