/*
 * SitioOJB.java
 *
 * Created on 25 de Agosto de 2002, 1:02
 */

package ServidorPersistente.OJB;

/**
 *
 * @author  ars
 */

//import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionCourse;
import Dominio.ISite;
import Dominio.Site;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSite;

public class SiteOJB extends ObjectFenixOJB implements IPersistentSite {

	/** Creates a new instance of SitioOJB */
	public SiteOJB() {
	}

	
	public ISite readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		crit.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
		return (ISite) queryObject(Site.class, crit);

	}

	public List readAll() throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		return queryList(Site.class,crit);
	}

	public void lockWrite(ISite site) throws ExcepcaoPersistencia {
		super.lockWrite(site);
	}

	public void delete(ISite site) throws ExcepcaoPersistencia {
		super.delete(site);
	}

	

}
