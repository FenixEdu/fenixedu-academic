/*
 * ExamExecutionCourseOJB.java
 * 
 * Created on 2003/03/29
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.ExamExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamExecutionCourse;

import org.apache.ojb.broker.query.Criteria;

public class ExamExecutionCourseOJB extends PersistentObjectOJB implements
        IPersistentExamExecutionCourse {

    public List readByExecutionCourse(String executionCourseAcronym, String executionPeriodName,
            String year) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.sigla", executionCourseAcronym);
        crit.addEqualTo("executionCourse.executionPeriod.name", executionPeriodName);
        crit.addEqualTo("executionCourse.executionPeriod.executionYear.year", year);
        return queryList(ExamExecutionCourse.class, crit);

    }


}