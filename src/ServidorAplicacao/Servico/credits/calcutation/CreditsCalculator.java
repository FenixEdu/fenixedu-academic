/*
 * Created on Dec 22, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.credits.calcutation;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;



import Dominio.IExecutionPeriod;
import Dominio.ISupportLesson;
import Dominio.ITeacher;
import Dominio.degree.finalProject.ITeacherDegreeFinalProjectStudent;
import Dominio.teacher.workTime.ITeacherInstitutionWorkTime;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;

/**
 * @author jpvl
 */
public class CreditsCalculator
{
    private CreditsCalculator calculator = new CreditsCalculator();
    
    public CreditsCalculator getInstance()
    {
        return calculator;
    }

    /**
     *  
     */
    private CreditsCalculator()
    {
        super();
    }

    public Double calculateSupportLessonsAfterInsert( ITeacher teacher,
                    IExecutionPeriod executionPeriod, ISupportLesson supportLesson,
                    ISuportePersistente sp ) throws ExcepcaoPersistencia
    {
        IPersistentSupportLesson supportLessonDAO= sp.getIPersistentSupportLesson();
        
        List supportLessonList = supportLessonDAO.readByTeacherAndExecutionPeriod(teacher, executionPeriod);
        
        supportLessonList.remove(supportLesson);
        
        Iterator iterator = supportLessonList.iterator();
        while (iterator.hasNext())
        {
            ISupportLesson sl = (ISupportLesson) iterator.next();
            Calendar calendar =Calendar.getInstance();
            
            calendar.setTime(sl.getEndTime());
            
        }
        
        
        return null;
    }

    public Double calculateSupportLessonsAfterDelete( ITeacher teacher,
                    IExecutionPeriod executionPeriod, ISupportLesson supportLesson,
                    ISuportePersistente sp )
    {
        return null;
    }

    public Double calcuteDegreeFinalProjectStudentAfterInsert( ITeacher teacher,
                    IExecutionPeriod executionPeriod,
                    ITeacherDegreeFinalProjectStudent degreeFinalProjectStudent, ISuportePersistente sp )
    {
        return null;
    }

    public Double calcuteDegreeFinalProjectStudentAfterDelete( ITeacher teacher,
                    IExecutionPeriod executionPeriod,
                    ITeacherDegreeFinalProjectStudent degreeFinalProjectStudent, ISuportePersistente sp )
    {
        return null;
    }

    public Double calcuteTeacherInstitutionWorkingTimeAfterInsert( ITeacher teacher,
                    IExecutionPeriod executionPeriod,
                    ITeacherInstitutionWorkTime teacherInstitutionWorkTime, ISuportePersistente sp )
    {
        return null;
    }

    public Double calcuteTeacherInstitutionWorkingTimeAfterDelete( ITeacher teacher,
                    IExecutionPeriod executionPeriod,
                    ITeacherInstitutionWorkTime teacherInstitutionWorkTime, ISuportePersistente sp )
    {
        return null;
    }
}
