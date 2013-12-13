/*
 * Created on Oct 10, 2003
 *
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.bennu.core.domain.Bennu;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeThesisDataVersion extends MasterDegreeThesisDataVersion_Base {

    final static Comparator<MasterDegreeThesisDataVersion> LAST_MODIFICATION_COMPARATOR = new BeanComparator("lastModification");

    public MasterDegreeThesisDataVersion() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public MasterDegreeThesisDataVersion(MasterDegreeThesis masterDegreeThesis, Employee responsibleEmployee,
            String dissertationTitle, Date lastModification, State currentState) {
        this();
        this.setMasterDegreeThesis(masterDegreeThesis);
        this.setResponsibleEmployee(responsibleEmployee);
        this.setDissertationTitle(dissertationTitle);
        this.setLastModification(lastModification);
        this.setCurrentState(currentState);
    }

    public static MasterDegreeThesisDataVersion readActiveByDissertationTitle(String dissertationTitle) {
        for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : Bennu.getInstance()
                .getMasterDegreeThesisDataVersionsSet()) {
            if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)
                    && masterDegreeThesisDataVersion.getDissertationTitle().equals(dissertationTitle)) {
                return masterDegreeThesisDataVersion;
            }
        }
        return null;
    }

    public String getGuidersNames() {
        final List<String> names = new ArrayList<String>();

        for (final Teacher teacher : getGuiders()) {
            names.add(teacher.getPerson().getName());
        }

        for (final ExternalContract contract : getExternalGuiders()) {
            names.add(contract.getPerson().getName());
        }

        StringBuilder result = new StringBuilder();
        for (Iterator<String> iter = names.iterator(); iter.hasNext();) {
            result.append(iter.next());
            if (iter.hasNext()) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    @Deprecated
    public java.util.Date getLastModification() {
        org.joda.time.DateTime dt = getLastModificationDateTime();
        return (dt == null) ? null : new java.util.Date(dt.getMillis());
    }

    @Deprecated
    public void setLastModification(java.util.Date date) {
        if (date == null) {
            setLastModificationDateTime(null);
        } else {
            setLastModificationDateTime(new org.joda.time.DateTime(date.getTime()));
        }
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract> getExternalAssistentGuiders() {
        return getExternalAssistentGuidersSet();
    }

    @Deprecated
    public boolean hasAnyExternalAssistentGuiders() {
        return !getExternalAssistentGuidersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Teacher> getGuiders() {
        return getGuidersSet();
    }

    @Deprecated
    public boolean hasAnyGuiders() {
        return !getGuidersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Teacher> getAssistentGuiders() {
        return getAssistentGuidersSet();
    }

    @Deprecated
    public boolean hasAnyAssistentGuiders() {
        return !getAssistentGuidersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract> getExternalGuiders() {
        return getExternalGuidersSet();
    }

    @Deprecated
    public boolean hasAnyExternalGuiders() {
        return !getExternalGuidersSet().isEmpty();
    }

    @Deprecated
    public boolean hasMasterDegreeThesis() {
        return getMasterDegreeThesis() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasResponsibleEmployee() {
        return getResponsibleEmployee() != null;
    }

    @Deprecated
    public boolean hasLastModificationDateTime() {
        return getLastModificationDateTime() != null;
    }

    @Deprecated
    public boolean hasCurrentState() {
        return getCurrentState() != null;
    }

    @Deprecated
    public boolean hasDissertationTitle() {
        return getDissertationTitle() != null;
    }

}
