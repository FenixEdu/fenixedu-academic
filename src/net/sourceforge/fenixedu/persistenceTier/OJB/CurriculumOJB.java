/*
 * CurriculumOJB.java
 * 
 * Created on 6 de Janeiro de 2003, 20:44
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author João Mota
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurriculum;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurriculum;

import org.apache.ojb.broker.query.Criteria;

public class CurriculumOJB extends PersistentObjectOJB implements IPersistentCurriculum {

    public ICurriculum readCurriculumByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        List curricularInformation = queryList(Curriculum.class, criteria, "lastModificationDate", false);
        return (curricularInformation != null && curricularInformation.size() > 0 ? (ICurriculum) curricularInformation
                .get(0)
                : null);
    }

    public List readCurriculumHistoryByCurricularCourse(ICurricularCourse curricularCourse)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        return queryList(Curriculum.class, criteria);
    }

    public ICurriculum readCurriculumByCurricularCourseAndExecutionYear(
            ICurricularCourse curricularCourse, IExecutionYear executionYear)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
        criteria.addLessOrEqualThan("lastModificationDate", executionYear.getEndDate());
        List curricularInformation = queryList(Curriculum.class, criteria, "lastModificationDate", false);
        return (curricularInformation != null && curricularInformation.size() > 0 ? (ICurriculum) curricularInformation
                .get(0)
                : null);
    }

    public void delete(ICurriculum curriculum) throws ExcepcaoPersistencia {
        super.delete(curriculum);
    }

}