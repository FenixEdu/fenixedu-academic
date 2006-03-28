/*
 * PessoaOJB.java
 * 
 * Created on 15 de Outubro de 2002, 15:16
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Tânia Pousão
 * 
 */
public class PessoaOJB extends PersistentObjectOJB implements IPessoaPersistente {

    public Integer countAllPersonByName(String name) {
        Criteria criteria = new Criteria();
        criteria.addLike("name", name);
        return new Integer(count(Person.class, criteria));
    }

    public Person lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao,
            IDDocumentType tipoDocumentoIdentificacao) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("documentIdNumber", numeroDocumentoIdentificacao);
        criteria.addEqualTo("idDocumentType", tipoDocumentoIdentificacao);
        return (Person) queryObject(Person.class, criteria);
    }

    public List<Person> readPersonsBySubName(String subName) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addLike("name", subName);
        return queryList(Person.class, criteria);
    }

    public List<Person> findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addLike("name", name);

        if (startIndex != null && numberOfElementsInSpan != null) {
            return readInterval(Person.class, criteria, numberOfElementsInSpan, startIndex);
        }
        return queryList(Person.class, criteria);

    }

	public boolean emailOwnedByFenixPerson(Collection<String> emails) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		for (String email: emails)
		{
			Criteria thisEmailCriteria = new Criteria();
			thisEmailCriteria.addEqualTo("email",email);
			criteria.addOrCriteria(thisEmailCriteria);			
		}
		
		return queryList(Person.class,criteria).size()>0;
	}
}