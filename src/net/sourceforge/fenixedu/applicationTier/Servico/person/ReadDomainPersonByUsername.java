/**
 * 
 */


package net.sourceforge.fenixedu.applicationTier.Servico.person;

import java.io.Serializable;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;


/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 17:51:06,21/Set/2005
 * @version $Id$
 */
public class ReadDomainPersonByUsername implements IService, Serializable
{

	public Person run(String username) throws ExcepcaoInexistente, ExcepcaoPersistencia
	{

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		Person person = sp.getIPessoaPersistente().lerPessoaPorUsername(username);

		if (person == null) throw new ExcepcaoInexistente("Unknown Person !!");

		return (Person) person; // I'm predictiong the future here were we won't have interfaces anymore
	}
}