/*
 * ReadMasterDegreeCandidateByUsername.java
 *
 * The Service ReadMasterDegreeCandidateByUsername reads the information of a
 * Candidate and returns it
 * 
 * Created on 02 de Dezembro de 2002, 16:25
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */

package ServidorAplicacao.Servico.person;

import DataBeans.InfoPerson;
import DataBeans.util.Cloner;
import Dominio.IPessoa;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.UserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadPersonByUsername implements IServico {
    
    private static ReadPersonByUsername servico = new ReadPersonByUsername();
    
    /**
     * The singleton access method of this class.
     **/
    public static ReadPersonByUsername getService() {
        return servico;
    }
    
    /**
     * The actor of this class.
     **/
    private ReadPersonByUsername() { 
    }
    
    /**
     * Returns The Service Name */
    
    public final String getNome() {
        return "ReadPersonByUsername";
    }
    
    
    public Object run(UserView userView)
	    throws ExcepcaoInexistente, FenixServiceException {

        ISuportePersistente sp = null;
        
        String username = new String(userView.getUtilizador());
        IPessoa person = null;
         
        try {
            sp = SuportePersistenteOJB.getInstance();
            person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);
        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        } 

		if (person == null)
			throw new ExcepcaoInexistente("Unknown Person !!");	
	
		InfoPerson infoPerson = Cloner.copyIPerson2InfoPerson(person);
		
		return infoPerson;
    }
}