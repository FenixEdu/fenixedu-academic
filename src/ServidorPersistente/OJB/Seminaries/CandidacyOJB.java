/*
 * Created on 29/Jul/2003, 10:29:18
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorPersistente.OJB.Seminaries;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Seminaries.ICandidacy;
import Dominio.Seminaries.Candidacy;
import Dominio.Seminaries.CaseStudy;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.Seminaries.*;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 29/Jul/2003, 10:29:18
 * 
 */
public class CandidacyOJB extends ObjectFenixOJB implements IPersistentSeminaryCandidacy
{

	public ICandidacy readByName(String name) throws ExcepcaoPersistencia
	{
        Criteria criteria = new Criteria();   
        criteria.addEqualTo("name",name);
        return (ICandidacy)super.queryObject(Candidacy.class,criteria);
	}
    
    public List readByStudentID(Integer id) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();   
        criteria.addEqualTo("student_id_internal",id);
        return (List) super.queryList(Candidacy.class,criteria);
    }

	public List readAll() throws ExcepcaoPersistencia
	{
        Criteria criteria = new Criteria();   
        return (List)super.queryList(CaseStudy.class,criteria);
	}

	public void delete(ICandidacy candidacy) throws ExcepcaoPersistencia
	{
        super.deleteByOID(Candidacy.class,candidacy.getIdInternal());
	}

}
