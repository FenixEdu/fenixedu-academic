/*
 * ReadAllCountries.java
 *
 * O Servico ReadAllCountries devolve a lista de paises existentes
 *
 * Created on 16 de Dezembro de 2002, 12:54
 */


/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.general;

import java.util.ArrayList;
import java.util.Iterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCountry;
import Dominio.ICountry;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadAllCountries implements IService {
    
    private static ReadAllCountries servico = new ReadAllCountries();
    
    /**
     * The singleton access method of this class.
     **/
    public static ReadAllCountries getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ReadAllCountries() { 
    }
    
    /**
     * Returns the service name */
    
    public final String getNome() {
        return "ReadAllCountries";
    }
    
    
    public Object run()
	    throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        ArrayList paises = new ArrayList();
        
        try {
            sp = SuportePersistenteOJB.getInstance();
            paises = sp.getIPersistentCountry().readAllCountrys();
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        } 

		if (paises.size() == 0)
			throw new ExcepcaoInexistente("Non existing Countries !!");	
			
			
		ArrayList countryList = new ArrayList();
		Iterator iterador = paises.iterator();	
		while (iterador.hasNext()) {
			countryList.add(new InfoCountry((ICountry) iterador.next()));
		}

		return countryList;
    }
}