/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeThesisDataVersion extends MasterDegreeThesisDataVersion_Base {

    final static Comparator<MasterDegreeThesisDataVersion> LAST_MODIFICATION_COMPARATOR = new BeanComparator(
            "lastModification");

    public MasterDegreeThesisDataVersion() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public MasterDegreeThesisDataVersion(MasterDegreeThesis masterDegreeThesis,
            Employee responsibleEmployee, String dissertationTitle, Date lastModification,
            State currentState) {
        this();
        this.setMasterDegreeThesis(masterDegreeThesis);
        this.setResponsibleEmployee(responsibleEmployee);
        this.setDissertationTitle(dissertationTitle);
        this.setLastModification(lastModification);
        this.setCurrentState(currentState);
    }

    public static MasterDegreeThesisDataVersion readActiveByDissertationTitle(String dissertationTitle) {
        for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : RootDomainObject
                .getInstance().getMasterDegreeThesisDataVersions()) {
            if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)
                    && masterDegreeThesisDataVersion.getDissertationTitle().equals(dissertationTitle)) {
                return masterDegreeThesisDataVersion;
            }
        }
        return null;
    }
}
