/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 9/Out/2003
 *
 *
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeThesisState;
import net.sourceforge.fenixedu.util.State;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.YearMonthDay;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeThesis extends MasterDegreeThesis_Base {

    public MasterDegreeThesis() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public MasterDegreeThesisDataVersion getActiveMasterDegreeThesisDataVersion() {

        for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : getMasterDegreeThesisDataVersions()) {
            if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                return masterDegreeThesisDataVersion;
            }
        }

        return null;
    }

    public String getDissertationTitle() {
        return getActiveMasterDegreeThesisDataVersion().getDissertationTitle();
    }

    public YearMonthDay getProofDateYearMonthDay() {
        final MasterDegreeProofVersion version = getActiveMasterDegreeProofVersion();
        return version != null ? version.getProofDateYearMonthDay() : null;
    }

    public MasterDegreeProofVersion getActiveMasterDegreeProofVersion() {
        MasterDegreeProofVersion activeMasterDegreeProofVersion = null;

        for (MasterDegreeProofVersion candidateMasterDegreeProofVersion : this.getMasterDegreeProofVersions()) {
            if (candidateMasterDegreeProofVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                activeMasterDegreeProofVersion = candidateMasterDegreeProofVersion;
                break;
            }
        }

        return activeMasterDegreeProofVersion;
    }

    public boolean isConcluded() {
        MasterDegreeProofVersion activeMasterDegreeProofVersion = getActiveMasterDegreeProofVersion();
        return activeMasterDegreeProofVersion != null && activeMasterDegreeProofVersion.isConcluded();
    }

    public boolean isConcluded(Integer year) {
        return isConcluded() && getActiveMasterDegreeProofVersion().getProofDateYearMonthDay().getYear() == year;
    }

    public MasterDegreeThesisState getState() {
        MasterDegreeProofVersion activeMasterDegreeProofVersion = getActiveMasterDegreeProofVersion();

        if (activeMasterDegreeProofVersion != null) {
            if (activeMasterDegreeProofVersion.isConcluded()) {
                return MasterDegreeThesisState.CONCLUDED;
            }
            if (activeMasterDegreeProofVersion.getThesisDeliveryDateYearMonthDay() != null) {
                return MasterDegreeThesisState.DELIVERED;
            } else {
                return MasterDegreeThesisState.NOT_DELIVERED;
            }
        }

        return MasterDegreeThesisState.NOT_DELIVERED;
    }

    public void delete() {
        getMasterDegreeThesisDataVersions().clear();
        getMasterDegreeProofVersions().clear();
        setStudentCurricularPlan(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion> getMasterDegreeThesisDataVersions() {
        return getMasterDegreeThesisDataVersionsSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeThesisDataVersions() {
        return !getMasterDegreeThesisDataVersionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeProofVersion> getMasterDegreeProofVersions() {
        return getMasterDegreeProofVersionsSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeProofVersions() {
        return !getMasterDegreeProofVersionsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.curriculum.ConclusionProcessVersion> getConclusionProcessVersions() {
        return getConclusionProcessVersionsSet();
    }

    @Deprecated
    public boolean hasAnyConclusionProcessVersions() {
        return !getConclusionProcessVersionsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasStudentCurricularPlan() {
        return getStudentCurricularPlan() != null;
    }

}
