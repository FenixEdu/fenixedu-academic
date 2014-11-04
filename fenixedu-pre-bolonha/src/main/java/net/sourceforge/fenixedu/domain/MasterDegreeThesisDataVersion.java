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
 * Created on Oct 10, 2003
 *
 *
 */
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.fenixedu.academic.domain.organizationalStructure.ExternalContract;
import org.fenixedu.academic.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.fenixedu.bennu.core.domain.Bennu;

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

        for (final Teacher teacher : getGuidersSet()) {
            names.add(teacher.getPerson().getName());
        }

        for (final ExternalContract contract : getExternalGuidersSet()) {
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

    public static MasterDegreeThesisDataVersion readActiveMasterDegreeThesisDataVersion(
            StudentCurricularPlan studentCurricularPlan) {
        MasterDegreeThesis masterDegreeThesis = studentCurricularPlan.getMasterDegreeThesis();
        if (masterDegreeThesis != null) {
            for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesis
                    .getMasterDegreeThesisDataVersionsSet()) {
                if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    return masterDegreeThesisDataVersion;
                }
            }
        }
        return null;
    }

    public static List<MasterDegreeThesisDataVersion> readNotActiveMasterDegreeThesisDataVersions(
            StudentCurricularPlan studentCurricularPlan) {
        MasterDegreeThesis masterDegreeThesis = studentCurricularPlan.getMasterDegreeThesis();
        List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = new ArrayList<MasterDegreeThesisDataVersion>();
        if (masterDegreeThesis != null) {
            for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesis
                    .getMasterDegreeThesisDataVersionsSet()) {
                if (!masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    masterDegreeThesisDataVersions.add(masterDegreeThesisDataVersion);
                }
            }
        }
        Collections.sort(masterDegreeThesisDataVersions, new ReverseComparator(
                MasterDegreeThesisDataVersion.LAST_MODIFICATION_COMPARATOR));
        return masterDegreeThesisDataVersions;
    }

    public static List<MasterDegreeThesisDataVersion> readActiveMasterDegreeThesisDataVersions(
            DegreeCurricularPlan degreeCurricularPlan) {
        if (degreeCurricularPlan instanceof EmptyDegreeCurricularPlan) {
            return Collections.emptyList();
        }
        List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = new ArrayList<MasterDegreeThesisDataVersion>();
        for (StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlansSet()) {
            MasterDegreeThesisDataVersion masterDegreeThesisDataVersion =
                    MasterDegreeThesisDataVersion.readActiveMasterDegreeThesisDataVersion(studentCurricularPlan);
            if (masterDegreeThesisDataVersion != null) {
                masterDegreeThesisDataVersions.add(masterDegreeThesisDataVersion);
            }
        }
        return masterDegreeThesisDataVersions;
    }

}
