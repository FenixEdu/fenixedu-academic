/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.gesdis.CourseHistoric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.gesdis.IPersistentCourseHistoric;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CourseHistoricOJB extends PersistentObjectOJB implements IPersistentCourseHistoric {

    /**
     *  
     */
    public CourseHistoricOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.gesdis.IPersistentCourseReport#readCourseReportByExecutionCourse(Dominio.IDisciplinaExecucao)
     */
    public List readByCurricularCourseAndSemester(ICurricularCourse curricularCourse, Integer semester)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        criteria.addEqualTo("semester", semester);
        return queryList(CourseHistoric.class, criteria);
    }
}