package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWEquivalenciaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWEquivalenciasIleec;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author David Santos
 * 3/Dez/2003
 */

public class MWEquivalenciaIleecOJB extends ObjectFenixOJB implements IPersistentMWEquivalenciasIleec {
    
    public MWEquivalenciaIleecOJB() {
    }

	public List readAll() throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		return queryList(MWEquivalenciaIleec.class, criteria);
	}

	public MWEquivalenciaIleec readByTipoEquivalenciaAndCodigoDisciplinaCurriculoAntigo(Integer tipoEquivalencia, String codigoDisciplinaCurriculoAntigo) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("tipoEquivalencia", tipoEquivalencia);
		criteria.addEqualTo("codigoDisciplinaCurriculoAntigo", codigoDisciplinaCurriculoAntigo);
		return (MWEquivalenciaIleec) queryObject(MWEquivalenciaIleec.class, criteria);
	}

	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return readSpan(MWEquivalenciaIleec.class, criteria, numberOfElementsInSpan, spanNumber);
	}
	
	public Integer countAll()
	{
		return new Integer(count(MWEquivalenciaIleec.class, new Criteria()));
	}
}