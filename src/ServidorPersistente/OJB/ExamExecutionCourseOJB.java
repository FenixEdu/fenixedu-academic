/*
 * ExamExecutionCourseOJB.java
 * 
 * Created on 2003/03/29
 */

package ServidorPersistente.OJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ExamExecutionCourse;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamExecutionCourse;

public class ExamExecutionCourseOJB extends ObjectFenixOJB implements IPersistentExamExecutionCourse
{

    public IExamExecutionCourse readBy(IExam exam, IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        
        Criteria crit = new Criteria();
        crit.addEqualTo("exam.season", exam.getSeason());
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo(
                "executionCourse.executionPeriod.name",
                executionCourse.getExecutionPeriod().getName());
        crit.addEqualTo(
                "executionCourse.executionPeriod.executionYear.year",
                executionCourse.getExecutionPeriod().getExecutionYear().getYear());
        return (IExamExecutionCourse) queryObject(ExamExecutionCourse.class, crit);
       
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo(
            "executionCourse.executionPeriod.name",
            executionCourse.getExecutionPeriod().getName());
        crit.addEqualTo(
            "executionCourse.executionPeriod.executionYear.year",
            executionCourse.getExecutionPeriod().getExecutionYear().getYear());
        return queryList(ExamExecutionCourse.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addOrderBy("executionCourse.sigla", true);
        crit.addOrderBy("exam.season", true);
        return queryList(ExamExecutionCourse.class, crit);

    }

    

    public void delete(IExam exam) throws ExcepcaoPersistencia
    {
        List examsExecutionCourses = readByCriteria(exam);
        for (int i = 0; i < examsExecutionCourses.size(); i++)
        {
            IExamExecutionCourse examExecutionCourse =
                (IExamExecutionCourse) examsExecutionCourses.get(i);
            super.delete(examExecutionCourse);
        }
    }

    public void delete(IExamExecutionCourse examExecutionCourse) throws ExcepcaoPersistencia
    {
        super.delete(examExecutionCourse);
    }

}
