/*
 * Created on 2/Out/2003 by jpvl
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import middleware.middlewareDomain.MWStudentClass;
import middleware.persistentMiddlewareSupport.IPersistentMWStudentClass;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author jpvl
 */
public class MWStudentClassOJB extends ObjectFenixOJB implements IPersistentMWStudentClass
{

	/* (non-Javadoc)
	 * @see middleware.persistentMiddlewareSupport.IPersistentMWStudentClass#readAll()
	 */
	public List readAll() throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
	{
		return queryList(MWStudentClass.class, new Criteria());
	}
}
