package net.sourceforge.fenixedu.domain.grant.owner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContract;
import net.sourceforge.fenixedu.domain.grant.contract.GrantContractRegime;
import net.sourceforge.fenixedu.domain.grant.contract.GrantType;

import org.apache.commons.beanutils.BeanComparator;

public class GrantOwner extends GrantOwner_Base {

    final static Comparator<GrantOwner> NUMBER_COMPARATOR = new BeanComparator("number");

    public GrantOwner() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public GrantContract readGrantContractByNumber(final Integer contractNumber) {
        for (final GrantContract grantContract : this.getGrantContractsSet()) {
            if (grantContract.getContractNumber().equals(contractNumber)) {
                return grantContract;
            }
        }
        return null;
    }

    public GrantContract readGrantContractWithMaximumContractNumber() {
        List<GrantContract> grantContracts = this.getGrantContracts();
        return (!grantContracts.isEmpty()) ? Collections.max(this.getGrantContractsSet(),
                new Comparator<GrantContract>() {
                    public int compare(GrantContract o1, GrantContract o2) {
                        return o1.getContractNumber().compareTo(o2.getContractNumber());
                    }
                }) : null;
    }

    public static Integer readMaxGrantOwnerNumber() {
        List<GrantOwner> grantOwners = RootDomainObject.getInstance().getGrantOwners();
        return (!grantOwners.isEmpty()) ? Collections.max(grantOwners, NUMBER_COMPARATOR).getNumber()
                : null;
    }

    public static GrantOwner readGrantOwnerByNumber(Integer grantOwnerNumber) {
        for (GrantOwner grantOwner : RootDomainObject.getInstance().getGrantOwners()) {
            if (grantOwner.getNumber().equals(grantOwnerNumber)) {
                return grantOwner;
            }
        }
        return null;
    }

    public static Integer countAllGrantOwnerByName(String name) {
        return readAllGrantOwnersByName(name).size();
    }

    public static List<GrantOwner> readGrantOwnerByName(String personName, Integer startIndex,
            Integer numberOfElementsInSpan) {
        List<GrantOwner> grantOwners = readAllGrantOwnersByName(personName);
        if (startIndex != null && numberOfElementsInSpan != null && !grantOwners.isEmpty()) {
            int finalIndex = Math.min(grantOwners.size(), startIndex + numberOfElementsInSpan);
            grantOwners.subList(startIndex, finalIndex);
        }
        return grantOwners;
    }

    private static List<GrantOwner> readAllGrantOwnersByName(String name) {
        List<GrantOwner> grantOwners = new ArrayList();
        final String nameToMatch = name.replaceAll("%", ".*").toLowerCase();
        for (final Person person : Person.readAllPersons()) {
            if (person.getName().toLowerCase().matches(nameToMatch) && person.hasGrantOwner()) {
                grantOwners.add(person.getGrantOwner());
            }
        }
        return grantOwners;
    }

    public static List<GrantOwner> readAllGrantOwnersBySpan(Integer spanNumber,
            Integer numberOfElementsInSpan, String orderBy) {
        List<GrantOwner> grantOwners = new ArrayList<GrantOwner>();
        grantOwners.addAll(RootDomainObject.getInstance().getGrantOwners());
        if (!grantOwners.isEmpty()) {
            Collections.sort(grantOwners, new BeanComparator(orderBy));
            int begin = (spanNumber - 1) * numberOfElementsInSpan;
            int end = Math.min(grantOwners.size(), begin + numberOfElementsInSpan);
            grantOwners.subList(begin, end);
        }
        return grantOwners;
    }

    public static Integer countAllByCriteria(Boolean justActiveContracts,
            Boolean justDesactiveContracts, Date dateBeginContract, Date dateEndContract,
            GrantType grantType) {

        Date currentDate = Calendar.getInstance().getTime();
        int counter = 0;
        boolean exit = false;

        for (GrantOwner grantOwner : RootDomainObject.getInstance().getGrantOwners()) {
            for (GrantContract grantContract : grantOwner.getGrantContracts()) {
                if (grantType != null && grantContract.getGrantType().equals(grantType)) {
                    for (GrantContractRegime grantContractRegime : grantContract.getContractRegimes()) {
                        if (justActiveContracts != null && justActiveContracts.booleanValue()
                                && grantContractRegime.getDateEndContract().before(currentDate)
                                && !grantContract.getEndContractMotive().equals("")) {
                            continue;
                        }
                        if (justDesactiveContracts != null && justDesactiveContracts.booleanValue()
                                && grantContractRegime.getDateEndContract().after(currentDate)
                                && grantContract.getEndContractMotive().equals("")) {
                            continue;
                        }
                        if (dateBeginContract != null
                                && grantContractRegime.getDateBeginContract().before(dateBeginContract)) {
                            continue;
                        }
                        if (dateEndContract != null
                                && grantContractRegime.getDateEndContract().after(dateEndContract)) {
                            continue;
                        }
                        counter++;
                        exit = true;
                        break;
                    }
                    if (exit) {
                        exit = false;
                        break;
                    }
                }
            }
        }
        return counter;
    }

    public boolean hasCurrentContract() {
        for (GrantContract grantContract : getGrantContracts()) {
            if (grantContract.hasActiveRegimes()) {
                return true;
            }
        }
        return false;
    }
}
