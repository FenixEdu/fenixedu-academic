/*
 * Created on 18/Dez/2002
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWPrecedenciaNumeroDisciplinasIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWPrecedenciaNumeroDisciplinasIleec;

import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */
public class MWPrecedenciaNumeroDisciplinasIleecOJB
	extends ObjectFenixOJB
	implements IPersistentMWPrecedenciaNumeroDisciplinasIleec
{

	public MWPrecedenciaNumeroDisciplinasIleecOJB()
	{
	}

	public List readAll() throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return queryList(MWPrecedenciaNumeroDisciplinasIleec.class, criteria);
	}

}
