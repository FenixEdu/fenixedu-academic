/*
 * Created on 21/Mar/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ChooseGuideByPersonID implements IService {

    public List run(Integer personID) throws Exception {

        ISuportePersistente sp = null;
        List guides = null;
        IPerson person = null;

        // Check if person exists

        try {
            sp = SuportePersistenteOJB.getInstance();

            person = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class, personID);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error", ex);

            throw newEx;
        }

        if (person == null) {
            throw new NonExistingServiceException();
        }

        try {
            sp = SuportePersistenteOJB.getInstance();
            guides = sp.getIPersistentGuide().readByPerson(person.getNumeroDocumentoIdentificacao(),
                    person.getTipoDocumentoIdentificacao());

            BeanComparator numberComparator = new BeanComparator("number");
            BeanComparator versionComparator = new BeanComparator("version");
            ComparatorChain chainComparator = new ComparatorChain();
            chainComparator.addComparator(numberComparator);
            chainComparator.addComparator(versionComparator);
            Collections.sort(guides, chainComparator);

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        if ((guides == null) || (guides.size() == 0)) {
            return null;
        }

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
    private List getLatestVersions(List guides) {
        List result = new ArrayList();

        Collections.reverse(guides);

        Integer numberAux = null;

        Iterator iterator = guides.iterator();
        while (iterator.hasNext()) {
            IGuide guide = (IGuide) iterator.next();

            if ((numberAux == null) || (numberAux.intValue() != guide.getNumber().intValue())) {
                numberAux = guide.getNumber();
                result.add(Cloner.copyIGuide2InfoGuide(guide));
            }
        }
        Collections.reverse(result);
        return result;
    }

}