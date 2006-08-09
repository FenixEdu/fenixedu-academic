/*
 * Created on Oct 10, 2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeProofVersion extends MasterDegreeProofVersion_Base {

    final static Comparator<MasterDegreeProofVersion> LAST_MODIFICATION_COMPARATOR = new BeanComparator(
            "lastModification");

    public MasterDegreeProofVersion() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public MasterDegreeProofVersion(MasterDegreeThesis masterDegreeThesis, Employee responsibleEmployee,
            Date lastModification, Date proofDate, Date thesisDeliveryDate,
            MasterDegreeClassification finalResult, Integer attachedCopiesNumber, State currentState,
            List juries, List externalJuries) {
        this();
        this.setMasterDegreeThesis(masterDegreeThesis);
        this.setResponsibleEmployee(responsibleEmployee);
        this.setLastModification(lastModification);
        this.setProofDate(proofDate);
        this.setThesisDeliveryDate(thesisDeliveryDate);
        this.setFinalResult(finalResult);
        this.setAttachedCopiesNumber(attachedCopiesNumber);
        this.setCurrentState(currentState);
        this.getJuries().addAll(juries);
        this.getExternalJuries().addAll(externalJuries);
    }

}
