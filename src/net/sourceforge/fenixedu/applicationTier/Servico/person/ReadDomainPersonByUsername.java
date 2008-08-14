/**
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.person;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.domain.Person;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 17:51:06,21/Set/2005
 * @version $Id: ReadDomainPersonByUsername.java 17212 2006-02-13 19:00:09Z sana
 *          $
 */
public class ReadDomainPersonByUsername extends Service {

    public Person run(String username) throws ExcepcaoInexistente {
	Person person = Person.readPersonByUsername(username);

	if (person == null)
	    throw new ExcepcaoInexistente("Unknown Person !!");

	return (Person) person; // I'm predictiong the future here were we won't
	// have interfaces anymore
    }
}