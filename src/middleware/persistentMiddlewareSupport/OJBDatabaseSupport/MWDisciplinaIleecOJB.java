package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import middleware.middlewareDomain.MWDisciplinaGrupoIleec;
import middleware.middlewareDomain.MWGrupoIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinaGrupoIleec;


import org.apache.ojb.broker.query.Criteria;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author David Santos
 * 3/Dez/2003
 */

public class MWDisciplinaIleecOJB extends ObjectFenixOJB implements IPersistentMWDisciplinaIleec
{

	public MWDisciplinaIleecOJB()
	{
	}

	public List readAll() throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return queryList(MWDisciplinaIleec.class, criteria);
	}

	public MWDisciplinaIleec readByCodigoDisciplina(String codigoDisciplina) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("codigoDisciplina", codigoDisciplina);
		return (MWDisciplinaIleec) queryObject(MWDisciplinaIleec.class, criteria);
	}

	public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		return readSpan(MWDisciplinaIleec.class, criteria, numberOfElementsInSpan, spanNumber);
	}

	public Integer countAll()
	{
		return new Integer(count(MWDisciplinaIleec.class, new Criteria()));
	}

	public ArrayList readByGroup(MWGrupoIleec grupoILeec)
		throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{

		IPersistentMiddlewareSupport pmws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDisciplinaGrupoIleec mwDGIleecOJB = pmws.getIPersistentMWDisciplinaGrupoIleec();

		List grupoIleec = mwDGIleecOJB.readByGrupo(grupoILeec);

		Iterator iter = grupoIleec.iterator();
		MWDisciplinaGrupoIleec mwDGIleec;
		MWDisciplinaIleec mwDiscIleec;

		ArrayList courses = new ArrayList();
		while (iter.hasNext())
		{
			Criteria criteria = new Criteria();
			mwDGIleec = (MWDisciplinaGrupoIleec) iter.next();
			criteria.addEqualTo("idDisciplina", mwDGIleec.getIdDisciplina());
			mwDiscIleec = (MWDisciplinaIleec) queryObject(MWDisciplinaIleec.class, criteria);
			courses.add(mwDiscIleec);
		}

		return courses;
	}
}