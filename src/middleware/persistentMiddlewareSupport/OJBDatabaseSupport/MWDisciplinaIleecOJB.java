package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinasIleec;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author David Santos
 * 3/Dez/2003
 */

public class MWDisciplinaIleecOJB extends ObjectFenixOJB implements IPersistentMWDisciplinasIleec {
    
    public MWDisciplinaIleecOJB() {
    }

	public List readAll() throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		return queryList(MWDisciplinaIleec.class, criteria);
	}

	public List readByCodigoDisciplina(String codigoDisciplina) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("codigoDisciplina", codigoDisciplina);
		return queryList(MWDisciplinaIleec.class, criteria);
	}

	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return readSpan(MWDisciplinaIleec.class, criteria, numberOfElementsInSpan, spanNumber);
	}
	
	public Integer countAll()
	{
		return new Integer(count(MWDisciplinaIleec.class, new Criteria()));
	}
}