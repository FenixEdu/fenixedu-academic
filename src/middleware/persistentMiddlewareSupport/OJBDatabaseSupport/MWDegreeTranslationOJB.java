/*
 * Created on 2/Out/2003 by jpvl
 *
 */
package middleware.persistentMiddlewareSupport.OJBDatabaseSupport;

import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.exceptions.PersistentMiddlewareSupportException;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ICurso;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author jpvl
 */
public class MWDegreeTranslationOJB extends ObjectFenixOJB implements IPersistentMWDegreeTranslation
{

    /* (non-Javadoc)
     * @see middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation#readByDegreeCode()
     */
    public MWDegreeTranslation readByDegreeCode(Integer degreeCode)
        throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("degreeCode", degreeCode);
        return (MWDegreeTranslation) queryObject(MWDegreeTranslation.class, criteria);
    }

	/**
	 * @author Nuno Correia
	 * @author Ricardo Rodrigues
	 */
	
    public MWDegreeTranslation readByDegree(ICurso degree)
        throws PersistentMiddlewareSupportException, ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDegree", degree.getIdInternal());
        return (MWDegreeTranslation) queryObject(MWDegreeTranslation.class, criteria);
    }
}
