/*
 * PessoaOJB.java
 * 
 * Created on 15 de Outubro de 2002, 15:16
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
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

    public IPerson lerPessoaPorUsername(String username) throws ExcepcaoPersistencia {
        IPerson person = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("username", username);
        person = (IPerson) queryObject(Person.class, criteria);
        return person;
    }

    public List<IPerson> findPersonByName(String name) throws ExcepcaoPersistencia {
        List<IPerson> personList = null;

        Criteria criteria = new Criteria();
        criteria.addLike("name", name);
        personList = queryList(Person.class, criteria);
        return personList;
    }

    public List<IPerson> findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {

        Criteria criteria = new Criteria();
        criteria.addLike("name", name);

        if (startIndex != null && numberOfElementsInSpan != null) {
            return readInterval(Person.class, criteria, numberOfElementsInSpan, startIndex);
        }
        return queryList(Person.class, criteria);

    }

    public Integer countAllPersonByName(String name) {
        Criteria criteria = new Criteria();
        criteria.addLike("name", name);
        return new Integer(count(Person.class, criteria));
    }

    public IPerson lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao,
            IDDocumentType tipoDocumentoIdentificacao) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("numeroDocumentoIdentificacao", numeroDocumentoIdentificacao);
        criteria.addEqualTo("idDocumentType", tipoDocumentoIdentificacao);
        return (IPerson) queryObject(Person.class, criteria);
    }

    /*
     * This method return a list with the elents returned by the search.
     */
    public List<IPerson> readActivePersonByNameAndEmailAndUsernameAndDocumentId(String name, String email,
            String username, String documentIdNumber, Integer startIndex, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {

        List<IPerson> personsList = null;

        Criteria criteria = new Criteria();
        if (name != null && name.length() > 0) { criteria.addLike("nome", name); }
        if (email != null && email.length() > 0) { criteria.addLike("email", email); }
        if (username != null && username.length() > 0) { criteria.addLike("username", username); }
        criteria.addNotLike("username", "INA%");
        if (documentIdNumber != null && documentIdNumber.length() > 0) {
            criteria.addLike("numeroDocumentoIdentificacao", documentIdNumber);
        }
        if (startIndex == null && numberOfElementsInSpan == null) {
        	personsList = queryList(Person.class, criteria, "nome", false);
        } else if (startIndex != null && numberOfElementsInSpan != null) {
        	personsList = readInterval(Person.class, criteria, numberOfElementsInSpan, startIndex, "nome", false);
        }
        return personsList;
    }
    
    public Integer countActivePersonByNameAndEmailAndUsernameAndDocumentId(String name, String email,
            String username, String documentIdNumber, Integer startIndex)
    {
    	Criteria criteria = new Criteria();
        if (name != null && name.length() > 0) {
            criteria.addLike("nome", name);
        }
        if (email != null && email.length() > 0) {
            criteria.addLike("email", email);
        }
        if (username != null && username.length() > 0) {
            criteria.addLike("username", username);
        }
        criteria.addNotLike("username", "INA%");
        if (documentIdNumber != null && documentIdNumber.length() > 0) {
            criteria.addLike("numeroDocumentoIdentificacao", documentIdNumber);
        }
        return new Integer(count(Person.class, criteria));    	
    }

    public List<IPerson> readPersonsBySubName(String subName) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addLike("name", subName);
        return queryList(Person.class, criteria);
    }

}