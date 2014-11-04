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
package org.fenixedu.academic.domain;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.masterDegree.MasterDegreeThesisState;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.State;

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

        for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : getMasterDegreeThesisDataVersionsSet()) {
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

        for (MasterDegreeProofVersion candidateMasterDegreeProofVersion : this.getMasterDegreeProofVersionsSet()) {
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
        getMasterDegreeThesisDataVersionsSet().clear();
        getMasterDegreeProofVersionsSet().clear();
        setStudentCurricularPlan(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public static MasterDegreeThesis getMasterDegreeThesis(Registration registration) {
        MasterDegreeThesis result = null;

        for (final StudentCurricularPlan plan : registration.getSortedStudentCurricularPlans()) {
            final MasterDegreeThesis thesis = plan.getMasterDegreeThesis();
            if (result != null && result.isConcluded() && thesis.isConcluded()) {
                throw new DomainException("error.Registration.more.than.one.concluded.thesis");
            }

            if (result == null || !result.isConcluded()) {
                result = thesis;
            }
        }

        return result;
    }

}
