package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.owner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOwner;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class GrantOwnerVO extends VersionedObjectsBase implements IPersistentGrantOwner {

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(GrantOwner.class);
    }

    public Integer readMaxGrantOwnerNumber() throws ExcepcaoPersistencia {
        List<GrantOwner> grantOwners = (List<GrantOwner>) readAll(GrantOwner.class);
        Integer maxGrantOwnerNumber = 0;

        for (GrantOwner owner : grantOwners) {
            if (owner.getNumber() > maxGrantOwnerNumber) {
                maxGrantOwnerNumber = owner.getNumber();
            }
        }

        return maxGrantOwnerNumber;
    }

    public GrantOwner readGrantOwnerByNumber(Integer grantOwnerNumber) throws ExcepcaoPersistencia {
        List<GrantOwner> grantOwners = (List<GrantOwner>) readAll(GrantOwner.class);

        for (GrantOwner owner : grantOwners) {
            if (owner.getNumber().equals(grantOwnerNumber)) {
                return owner;
            }
        }

        return null;
    }

    public GrantOwner readGrantOwnerByPerson(Integer personIdInternal) throws ExcepcaoPersistencia {
        Person person = (Person) readByOID(Person.class, personIdInternal);
        return person.getGrantOwner();
    }

    public GrantOwner readGrantOwnerByPersonID(String idNumber, IDDocumentType idType)
            throws ExcepcaoPersistencia {

        List<Person> persons = (List<Person>) readAll(Person.class);

        for (Person person : persons) {
            if (person.getNumeroDocumentoIdentificacao().equals(idNumber)
                    && person.getIdDocumentType().equals(idType)) {
                return person.getGrantOwner();
            }
        }

        return null;
    }

    public Integer countAll() {
        return readAll(GrantOwner.class).size();
    }

    public Integer countAllGrantOwnerByName(String personName) {
        List<GrantOwner> grantOwners = (List<GrantOwner>) readAll(GrantOwner.class);
        int result = 0;

        for (GrantOwner grantOwner : grantOwners) {
            if (grantOwner.getPerson().getNome().equals(personName)) {
                result++;
            }
        }

        return result;
    }

    public Integer countAllByCriteria(Boolean justActiveContracts, Boolean justDesactiveContracts,
            Date dateBeginContract, Date dateEndContract, Integer grantTypeId) {

        List<GrantOwner> grantOwners = (List<GrantOwner>) readAll(GrantOwner.class);
        int result = 0;
        long now = System.currentTimeMillis();

        for (GrantOwner owner : grantOwners) {

            boolean verifiesContract = true;
            for (GrantContract grantContract : owner.getGrantContracts()) {

                for (GrantContractRegime grantContractRegime : grantContract
                        .getContractRegimes()) {
                    if (justActiveContracts != null
                            && justActiveContracts.booleanValue()
                            && (grantContractRegime.getDateEndContract().getTime() < now || !grantContract
                                    .getEndContractMotive().equals(""))) {
                        verifiesContract = false;
                    }

                    if (justDesactiveContracts != null
                            && justDesactiveContracts.booleanValue()
                            && (grantContractRegime.getDateEndContract().getTime() > now || grantContract
                                    .getEndContractMotive().equals(""))) {
                        verifiesContract = false;
                    }

                    if (dateBeginContract != null
                            && grantContractRegime.getDateBeginContract().getTime() < dateBeginContract
                                    .getTime()) {
                        verifiesContract = false;
                    }

                    if (dateEndContract != null
                            && grantContractRegime.getDateEndContract().getTime() > dateEndContract
                                    .getTime()) {
                        verifiesContract = false;
                    }

                    if (grantTypeId != null
                            && !grantContract.getGrantType().getIdInternal().equals(grantTypeId)) {
                        verifiesContract = false;
                    }

                    if (verifiesContract) {
                        break;
                    }
                }

                if (verifiesContract) {
                    result++;
                    break;
                }
                verifiesContract = true;
            }

            if (owner.getGrantContracts().isEmpty() && justActiveContracts == null
                    && justDesactiveContracts == null && dateBeginContract == null
                    && dateEndContract == null && grantTypeId == null) {
                result++;
            }
        }

        return result;
    }

    public List readGrantOwnerByPersonName(String personName, Integer startIndex,
            Integer numberOfElementsInSpan) throws ExcepcaoPersistencia {

        List<GrantOwner> grantOwners = (List<GrantOwner>) readAll(GrantOwner.class);
        List<GrantOwner> result = new ArrayList(numberOfElementsInSpan);

        GrantOwner grantOwner = null;
        for (Iterator iter = grantOwners.listIterator(startIndex); iter.hasNext()
                && result.size() <= numberOfElementsInSpan;) {

            grantOwner = (GrantOwner) iter.next();

            if (grantOwner.getPerson().getNome().equals(personName)) {
                result.add(grantOwner);
            }
        }

        return result;
    }

    public List readAllBySpan(Integer spanNumber, Integer numberOfElementsInSpan)
            throws ExcepcaoPersistencia {

        List<GrantOwner> grantOwners = (List<GrantOwner>) readAll(GrantOwner.class);
        
        int begin = (spanNumber - 1) * numberOfElementsInSpan;
        int end = begin + numberOfElementsInSpan;

        return grantOwners.subList(begin, end);
    }

    public List readAllGrantOwnersBySpan(Integer spanNumber, Integer numberOfElementsInSpan,
            String orderBy) throws ExcepcaoPersistencia {

        List<GrantOwner> grantOwners = (List<GrantOwner>) readAll(GrantOwner.class);
        ComparatorChain comparatorChain = new ComparatorChain(new BeanComparator(orderBy), true);
        Collections.sort(grantOwners, comparatorChain);
        
        int begin = (spanNumber - 1) * numberOfElementsInSpan;
        int end = begin + numberOfElementsInSpan;

        return grantOwners.subList(begin, end);
    }

}