/*
 * PessoaOJB.java
 * 
 * Created on 15 de Outubro de 2002, 15:16
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.ojb.broker.query.Criteria;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;
import net.sourceforge.fenixedu.util.TipoDocumentoIdentificacao;

/**
 * @author Tânia Pousão
 *  
 */
public class PessoaOJB extends PersistentObjectOJB implements IPessoaPersistente {

    public PessoaOJB() {
    }

    public void escreverPessoa(IPerson personToWrite) throws ExcepcaoPersistencia,
            ExistingPersistentException, IllegalAccessException, InvocationTargetException {

        IPerson personFromDB1 = null;
        IPerson personFromDB2 = null;

        // If there is nothing to write, simply return.
        if (personToWrite == null)
            return;

        // Read person from database.
        personFromDB1 = this.lerPessoaPorUsername(personToWrite.getUsername());
        personFromDB2 = this.lerPessoaPorNumDocIdETipoDocId(personToWrite
                .getNumeroDocumentoIdentificacao(), personToWrite.getTipoDocumentoIdentificacao());

        // If person is not in database, then write it.
        if ((personFromDB1 == null) && (personFromDB2 == null)) {

            super.lockWrite(personToWrite);
            return;

        } else if ((personFromDB1 != null)
                && (personFromDB2 != null)
                && (personFromDB1.getIdInternal().equals(personToWrite
                        .getIdInternal()))
                && (personFromDB2.getIdInternal().equals(personToWrite
                        .getIdInternal()))) {

            super.lockWrite(personFromDB1);
            BeanUtils.copyProperties(personFromDB1, personToWrite);
            return;

            // else If the person is mapped to the database, then write any
            // existing changes.
        } else if ((personFromDB1 != null)
                && (personFromDB2 == null)
                && (personToWrite instanceof Person)
                && (personFromDB1.getIdInternal().equals(personToWrite
                        .getIdInternal()))) {

            super.lockWrite(personFromDB1);
            BeanUtils.copyProperties(personFromDB1, personToWrite);

            return;

        } else if ((personFromDB2 != null)
                && (personFromDB1 == null)
                && (personToWrite instanceof Person)
                && (personFromDB2.getIdInternal().equals(personToWrite
                        .getIdInternal()))) {
            super.lockWrite(personFromDB1);
            BeanUtils.copyProperties(personFromDB2, personToWrite);

            return;
        }       
        // else Throw an already existing exception
        throw new ExistingPersistentException();
    }

    public void apagarPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao,
            TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia {

        Criteria crit = new Criteria();
        crit.addEqualTo("numeroDocumentoIdentificacao", numeroDocumentoIdentificacao);
        crit.addEqualTo("tipoDocumentoIdentificacao", tipoDocumentoIdentificacao.getTipo());

        List result = queryList(Person.class, crit);
        if (result != null) {

            ListIterator iterator = result.listIterator();
            while (iterator.hasNext()) {

                super.delete(iterator.next());
            }
        }

    }

    public void apagarPessoa(IPerson pessoa) throws ExcepcaoPersistencia {
        super.delete(pessoa);
    }

    public IPerson lerPessoaPorUsername(String username) throws ExcepcaoPersistencia {
        IPerson person = null;

        Criteria criteria = new Criteria();
        criteria.addEqualTo("username", username);
        person = (IPerson) queryObject(Person.class, criteria);
        return person;
    }

    public List findPersonByName(String name) throws ExcepcaoPersistencia {
        List personList = null;

        Criteria criteria = new Criteria();
        criteria.addLike("name", name);
        personList = queryList(Person.class, criteria);
        return personList;
    }

    public List findPersonByName(String name, Integer startIndex, Integer numberOfElementsInSpan)
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

    /*
     * This method return a list with 2 elements. The first is a Integer with
     * the number of elements returned by the main search, The second is a list
     * with the elemts returned by the limited search.
     */
    public List findPersonByNameAndEmailAndUsernameAndDocumentId(String name, String email,
            String username, String documentIdNumber, Integer startIndex, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {
        List personList = null;

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

        if (documentIdNumber != null && documentIdNumber.length() > 0) {
            criteria.addLike("numeroDocumentoIdentificacao", documentIdNumber);
        }

        personList = queryList(Person.class, criteria);
        return personList;
    }

    public IPerson lerPessoaPorNumDocIdETipoDocId(String numeroDocumentoIdentificacao,
            TipoDocumentoIdentificacao tipoDocumentoIdentificacao) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("numeroDocumentoIdentificacao", numeroDocumentoIdentificacao);
        criteria.addEqualTo("tipoDocumentoIdentificacao", tipoDocumentoIdentificacao.getTipo());
        return (IPerson) queryObject(Person.class, criteria);
    }

    public List lerTodasAsPessoas() throws ExcepcaoPersistencia {
        return queryList(Person.class, new Criteria());
    }

    public void alterarPessoa(String numDocId, TipoDocumentoIdentificacao tipoDocId, IPerson pessoa) {
    }

    /*
     * This method return a list with 2 elements. The first is a Integer with
     * the number of elements returned by the main search, The second is a list
     * with the elemts returned by the limited search.
     */
    public List findActivePersonByNameAndEmailAndUsernameAndDocumentId(String name, String email,
            String username, String documentIdNumber, Integer startIndex, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {
        List result = new ArrayList();

        List personList = null;

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
        result.add(0, new Integer(count(Person.class, criteria)));

        if (startIndex == null && numberOfElementsInSpan == null) {
            personList = queryList(Person.class, criteria, "nome", false);
        } else if (startIndex != null && numberOfElementsInSpan != null) {
            personList = readInterval(Person.class, criteria, numberOfElementsInSpan, startIndex,
                    "nome", false);
        }
        result.add(personList);

        return result;
    }

}