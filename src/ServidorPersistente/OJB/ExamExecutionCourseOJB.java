/*
 * ExamExecutionCourseOJB.java
 * 
 * Created on 2003/03/29
 */

package ServidorPersistente.OJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Exam;
import Dominio.ExamExecutionCourse;
import Dominio.IExam;
import Dominio.IExamExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExamExecutionCourse;
import ServidorPersistente.exceptions.ExistingPersistentException;

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

    public void lockWrite(IExamExecutionCourse examExecutionCourseToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {
        IExamExecutionCourse examExecutionCourseFromDB = null;
        if (examExecutionCourseToWrite == null)
            return;

        // read exam
        examExecutionCourseFromDB =
            this.readBy(
                examExecutionCourseToWrite.getExam(),
                examExecutionCourseToWrite.getExecutionCourse());

        // if (exam not in database) then write it
        if (examExecutionCourseFromDB == null)
        {
            super.lockWrite(examExecutionCourseToWrite);
            tx.commit();
            tx.begin();

            IExam exam = examExecutionCourseToWrite.getExam();
            IExecutionCourse executionCourse = examExecutionCourseToWrite.getExecutionCourse();

            List associatedExecutionCourses =
                examExecutionCourseToWrite.getExam().getAssociatedExecutionCourses();

            List associatedExams = examExecutionCourseToWrite.getExecutionCourse().getAssociatedExams();

            if (associatedExams == null)
            {
                associatedExams = new ArrayList();
                executionCourse.setAssociatedExams(associatedExams);
            }
            associatedExams.add(exam);

            if (associatedExecutionCourses == null)
            {
                associatedExecutionCourses = new ArrayList();
                exam.setAssociatedExecutionCourses(associatedExecutionCourses);
            }
            associatedExecutionCourses.add(executionCourse);
            //			PersistenceBroker pb = ((HasBroker)tx).getBroker();
            //			pb.clearCache();
            //			pb.removeFromCache(examExecutionCourseToWrite.getExam());
            //			pb.removeFromCache(examExecutionCourseToWrite.getExecutionCourse());
        }

        // else if (exam is mapped to the database then write any existing
		// changes)
        else if (
            (examExecutionCourseToWrite instanceof Exam)
                && ((Exam) examExecutionCourseFromDB).getIdInternal().equals(
                    ((Exam) examExecutionCourseToWrite).getIdInternal()))
        {
            examExecutionCourseFromDB.setExam(examExecutionCourseToWrite.getExam());
            examExecutionCourseFromDB.setExecutionCourse(
                examExecutionCourseToWrite.getExecutionCourse());
            // else throw an AlreadyExists exception.
        }
        else
            throw new ExistingPersistentException();
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
