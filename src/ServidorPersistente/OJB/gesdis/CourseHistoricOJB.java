/*
 * Created on 17/Fev/2004
 *  
 */
package ServidorPersistente.OJB.gesdis;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICurricularCourse;
import Dominio.gesdis.CourseHistoric;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.gesdis.IPersistentCourseHistoric;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CourseHistoricOJB extends ObjectFenixOJB implements IPersistentCourseHistoric
{

    /**
	 *  
	 */
    public CourseHistoricOJB()
    {
        super();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.gesdis.IPersistentCourseReport#readCourseReportByExecutionCourse(Dominio.IDisciplinaExecucao)
	 */
    public List readByCurricularCourseAndSemester(ICurricularCourse curricularCourse, Integer semester) throws ExcepcaoPersistencia
    {

        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        criteria.addEqualTo("semester", semester);
        return queryList(CourseHistoric.class, criteria);
    }
}
