/*
 * Created on 16/Dez/2002
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import middleware.middlewareDomain.MWPrecedenciaDisciplinaDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWPrecedenciaDisciplinaDisciplinaIleec;

import org.apache.ojb.broker.query.Criteria;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *
 */
public class MWPrecedenciaDisciplinaDisciplinaIleecOJB
	extends ObjectFenixOJB
	implements IPersistentMWPrecedenciaDisciplinaDisciplinaIleec
{
	public MWPrecedenciaDisciplinaDisciplinaIleecOJB()
	{
	}

	public List readAll() throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return queryList(MWPrecedenciaDisciplinaDisciplinaIleec.class, criteria);
	}
}
