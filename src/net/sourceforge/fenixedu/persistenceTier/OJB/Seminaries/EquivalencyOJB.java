/*
 * Created on 4/Ago/2003, 13:08:05
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.CourseEquivalency;
import net.sourceforge.fenixedu.domain.Seminaries.ICourseEquivalency;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 4/Ago/2003, 13:08:05
 *  
 */
public class EquivalencyOJB extends PersistentObjectOJB implements
        IPersistentSeminaryCurricularCourseEquivalency {
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return super.queryList(CourseEquivalency.class, criteria);
    }

    public void delete(ICourseEquivalency courseEquivalency) throws ExcepcaoPersistencia {
        super.deleteByOID(CourseEquivalency.class, courseEquivalency.getIdInternal());
    }
}