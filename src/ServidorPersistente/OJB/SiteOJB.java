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

import org.odmg.QueryException;

import Dominio.IDisciplinaExecucao;
import Dominio.ISite;
import Dominio.Site;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSite;

public class SiteOJB extends ObjectFenixOJB implements IPersistentSite {
    
    /** Creates a new instance of SitioOJB */
    public SiteOJB() {
    }
    
    //To delete
    
//    
//    public List readAnnouncementsByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia{
//        try {
//            String oqlQuery = "select announcement from " + Announcement.class.getName();
//			oqlQuery += " where site.executionCourse.sigla = $1";
//			oqlQuery += " and site.executionCourse.executionPeriod.name = $2";
//			oqlQuery += " and site.executionCourse.executionPeriod.executionYear.year = $3";
//			
//			query.create(oqlQuery);
//			query.bind(executionCourse.getSigla());
//			query.bind(executionCourse.getExecutionPeriod().getName());
//			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
//			
//            List result = (List) query.execute();
//            lockRead(result);
//            
//            return result;
//        } catch (QueryException ex) {
//            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
//        }
//    }
    
    

	public ISite readByExecutionCourse(IDisciplinaExecucao executionCourse) throws ExcepcaoPersistencia {
		try {
			ISite site = null;
			
			String oqlQuery = "select site from " + Site.class.getName();
			oqlQuery += " where executionCourse.sigla = $1";
			oqlQuery += " and executionCourse.executionPeriod.name = $2";
			oqlQuery += " and executionCourse.executionPeriod.executionYear.year = $3";
			query.create(oqlQuery);
			query.bind(executionCourse.getSigla());
			query.bind(executionCourse.getExecutionPeriod().getName());
			query.bind(executionCourse.getExecutionPeriod().getExecutionYear().getYear());
			List result = (List) query.execute();
			lockRead(result);
			if (result.size() != 0)
				site = (ISite) result.get(0);
			return site;
		} catch (QueryException ex) {
			throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
		}
	}
    
    

    
    public List readAll() throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select site from " + Site.class.getName();
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }
    
    public void lockWrite(ISite site) throws ExcepcaoPersistencia {
        super.lockWrite(site);
    }
    
    public void delete(ISite site) throws ExcepcaoPersistencia {
        super.delete(site);
    }
    
    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Site.class.getName();
        super.deleteAll(oqlQuery);
    }
    
}
