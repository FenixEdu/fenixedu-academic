/*
 * Created on Feb 19, 2004
 */
package middleware.persistentMiddlewareSupport.grant.OJBDatabaseSuport;

import java.util.List;

import middleware.middlewareDomain.grant.MWPerson;
import middleware.persistentMiddlewareSupport.grant.IPersistentMWPersonOJB;
import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author pica
 * @author barbosa
 */
public class MWPersonOJB extends ObjectFenixOJB implements IPersistentMWPersonOJB
{
	public MWPersonOJB()
	{
	}

	public List readAll() throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return queryList(MWPerson.class, criteria);
	}

	public MWPerson readByIDNumberAndIDType(String idNumber,Integer idType) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("numeroDocumentoIdentificacao", idNumber);
		criteria.addEqualTo("tipoDocumentoIdentificacao", idType);
		return (MWPerson) queryObject(MWPerson.class, criteria);
	}

    public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return readSpan(MWPerson.class, criteria, numberOfElementsInSpan, spanNumber);
    }
    
    public Integer countAll()
    {
        return new Integer(count(MWPerson.class, new Criteria()));
    }
    
}
