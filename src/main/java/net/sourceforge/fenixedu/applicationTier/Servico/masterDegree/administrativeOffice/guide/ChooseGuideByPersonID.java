/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChooseGuideByPersonID {

    @Atomic
    public static List run(String personID) throws Exception {
        check(RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE);

        // Check if person exists
        Person person = (Person) FenixFramework.getDomainObject(personID);

        if (person == null) {
            throw new NonExistingServiceException();
        }

        List<Guide> guides = new ArrayList<Guide>(person.getGuides());
        if (guides.size() == 0) {
            return null;
        }

        BeanComparator numberComparator = new BeanComparator("number");
        BeanComparator versionComparator = new BeanComparator("version");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(numberComparator);
        chainComparator.addComparator(versionComparator);
        Collections.sort(guides, chainComparator);

        return getLatestVersions(guides);
    }

    /**
     * 
     * This function expects to receive a list ordered by number (Ascending) and
     * version (Descending)
     * 
     * @param guides
     * @return The latest version for the guides
     */
    private static List getLatestVersions(List guides) {
        List result = new ArrayList();

        Collections.reverse(guides);

        Integer numberAux = null;

        Iterator iterator = guides.iterator();
        while (iterator.hasNext()) {
            Guide guide = (Guide) iterator.next();

            if ((numberAux == null) || (numberAux.intValue() != guide.getNumber().intValue())) {
                numberAux = guide.getNumber();
                result.add(InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(guide));
            }
        }
        Collections.reverse(result);
        return result;
    }

}