package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IWebSite;
import Dominio.WebSite;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentWebSite;

/**
 * @author  Fernanda Quitério
 * 23/09/2003
 * 
 */
public class WebSiteOJB extends ObjectFenixOJB implements IPersistentWebSite {

	/** Creates a new instance of SitioOJB */
	public WebSiteOJB() {
	}
	
	public List readAll() throws ExcepcaoPersistencia {
		Criteria crit = new Criteria();
		return queryList(WebSite.class,crit);
	}

	

	public void delete(IWebSite site) throws ExcepcaoPersistencia {
		super.delete(site);
	}

	
}