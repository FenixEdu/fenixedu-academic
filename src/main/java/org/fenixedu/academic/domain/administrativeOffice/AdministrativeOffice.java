/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.administrativeOffice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.serviceRequests.RectorateSubmissionBatch;
import org.fenixedu.academic.domain.serviceRequests.RectorateSubmissionState;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class AdministrativeOffice extends AdministrativeOffice_Base {

    public AdministrativeOffice() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    // static methods
    @Deprecated
    public static AdministrativeOffice readByAdministrativeOfficeType(AdministrativeOfficeType administrativeOfficeType) {

        for (final AdministrativeOffice administrativeOffice : Bennu.getInstance().getAdministrativeOfficesSet()) {

            if (administrativeOffice.getAdministrativeOfficeType() == administrativeOfficeType) {
                return administrativeOffice;
            }

        }
        return null;

    }

    @Deprecated
    public static AdministrativeOffice readDegreeAdministrativeOffice() {
        return readByAdministrativeOfficeType(AdministrativeOfficeType.DEGREE);
    }

    @Deprecated
    public static AdministrativeOffice readMasterDegreeAdministrativeOffice() {
        return readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE);
    }

    public Set<DegreeType> getAdministratedDegreeTypes() {
        Set<DegreeType> result = new HashSet<DegreeType>();
        for (AcademicProgram program : getManagedAcademicProgramSet()) {
            result.add(program.getDegreeType());
        }
        return result;
    }

    public Set<CycleType> getAdministratedCycleTypes() {
        Set<CycleType> result = new HashSet<CycleType>();
        for (AcademicProgram program : getManagedAcademicProgramSet()) {
            result.addAll(program.getCycleTypes());
        }
        return result;
    }

    public Set<Degree> getAdministratedDegrees() {
        final Set<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        for (AcademicProgram program : getManagedAcademicProgramSet()) {
            if (program instanceof Degree) {
                result.add((Degree) program);
            }
        }
        return result;
    }

    public Set<Degree> getAdministratedDegreesForStudentCreationWithoutCandidacy() {
        final Set<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        result.addAll(getAdministratedDegrees());
        return result;
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());

        setUnit(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!getAcademicServiceRequestsSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.AdministrativeOffice.cannot.delete"));
        }
        if (!getManagedAcademicProgramSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.AdministrativeOffice.cannot.delete"));
        }
        if (!getRectorateSubmissionBatchSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "error.AdministrativeOffice.cannot.delete"));
        }
        if (!getAcademicAuthorizationGroupSet().isEmpty()) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION,
                    "error.administrativeOffice.cannotDeleteAdministrativeOfficeUsedInAccessControl"));
        }
    }

    @Deprecated
    public boolean isDegree() {
        return getAdministrativeOfficeType().equals(AdministrativeOfficeType.DEGREE);
    }

    @Deprecated
    public boolean isMasterDegree() {
        return getAdministrativeOfficeType() == AdministrativeOfficeType.MASTER_DEGREE;
    }

    public RectorateSubmissionBatch getCurrentRectorateSubmissionBatch() {
        DateTime last = null;
        RectorateSubmissionBatch current = null;
        for (RectorateSubmissionBatch bag : getRectorateSubmissionBatchSet()) {
            if (!RectorateSubmissionState.UNSENT.equals(bag.getState())) {
                continue;
            }

            if (last == null || bag.getCreation().isAfter(last)) {
                last = bag.getCreation();
                current = bag;
            }
        }
        return current;
    }

    public boolean getHasAnyPhdProgram() {
        for (AcademicProgram program : getManagedAcademicProgramSet()) {
            if (program instanceof PhdProgram) {
                return true;
            }
        }
        return false;
    }

}
