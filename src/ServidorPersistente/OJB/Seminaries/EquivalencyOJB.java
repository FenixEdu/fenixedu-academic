/*
 * Created on 4/Ago/2003, 13:08:05
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Seminaries.CourseEquivalency;
import Dominio.Seminaries.ICourseEquivalency;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.IPersistentSeminaryCurricularCourseEquivalency;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 4/Ago/2003, 13:08:05
 * 
 */
public class EquivalencyOJB
    extends ObjectFenixOJB
    implements IPersistentSeminaryCurricularCourseEquivalency
{
    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return super.queryList(CourseEquivalency.class, criteria);
    }
    public void delete(ICourseEquivalency courseEquivalency) throws ExcepcaoPersistencia
    {
        super.deleteByOID(CourseEquivalency.class, courseEquivalency.getIdInternal());
    }
}
