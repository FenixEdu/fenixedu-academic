package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWTipoEquivalenciaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWTipoEquivalenciaIleec;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author David Santos
 * 3/Dez/2003
 */

public class MWTipoEquivalenciaIleecOJB extends ObjectFenixOJB implements IPersistentMWTipoEquivalenciaIleec {
    
    public MWTipoEquivalenciaIleecOJB() {
    }

	public List readAll() throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		return queryList(MWTipoEquivalenciaIleec.class, criteria);
	}

	public MWTipoEquivalenciaIleec readByTipoEquivalencia(Integer tipoEquivalencia) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("tipoEquivalencia", tipoEquivalencia);
		return (MWTipoEquivalenciaIleec) queryObject(MWTipoEquivalenciaIleec.class, criteria);
	}

	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return readSpan(MWTipoEquivalenciaIleec.class, criteria, numberOfElementsInSpan, spanNumber);
	}
	
	public Integer countAll()
	{
		return new Integer(count(MWTipoEquivalenciaIleec.class, new Criteria()));
	}
}