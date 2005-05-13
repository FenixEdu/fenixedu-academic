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
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExamExecutionCourse;

import org.apache.ojb.broker.query.Criteria;

public class ExamExecutionCourseOJB extends PersistentObjectOJB implements
        IPersistentExamExecutionCourse {

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("executionCourse.sigla", executionCourse.getSigla());
        crit.addEqualTo("executionCourse.executionPeriod.name", executionCourse.getExecutionPeriod()
                .getName());
        crit.addEqualTo("executionCourse.executionPeriod.executionYear.year", executionCourse
                .getExecutionPeriod().getExecutionYear().getYear());
        return queryList(ExamExecutionCourse.class, crit);

    }


}