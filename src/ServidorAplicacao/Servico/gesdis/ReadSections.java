package ServidorAplicacao.Servico.gesdis;

/**
 * Created on 19/03/2003
 *
 * @author lmac1
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoSite;
import DataBeans.util.Cloner;
import Dominio.ISection;
import Dominio.ISite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadSections implements IServico {

  private static ReadSections service = new ReadSections();

  /**
   * The singleton access method of this class.
   */
  public static ReadSections getService() {
    return service;
  }

  /**
   * The constructor of this class.
   */
  private ReadSections() { }

  /**
   * Service name
   */
  public final String getNome() {
    return "ReadSections";
  }

  /**
   * Executes the service. Returns the current collection of all infosections that belong to a site.
   */
  public List run(InfoSite infoSite) throws FenixServiceException {
    
    ISite site = Cloner.copyInfoSite2ISite(infoSite);
    ISuportePersistente sp;
    List allSections = null;

	
	try {
    	sp = SuportePersistenteOJB.getInstance();
    	allSections = sp.getIPersistentSection().readBySite(site);
    	    	
    	
	} catch (ExcepcaoPersistencia excepcaoPersistencia){
		throw new FenixServiceException(excepcaoPersistencia);
	}

    if (allSections == null || allSections.isEmpty()) {return allSections;} 

    // build the result of this service
    Iterator iterator = allSections.iterator();
    List result = new ArrayList(allSections.size());
    
    while (iterator.hasNext())
		result.add(Cloner.copyISection2InfoSection((ISection) iterator.next()) );

    return result;
  }
}
