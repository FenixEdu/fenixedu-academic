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
package net.sourceforge.fenixedu.domain.administrativeOffice;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch;
import net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionState;

import org.fenixedu.bennu.core.domain.Bennu;
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

    public Set<Degree> getAdministratedBolonhaDegrees() {
        Set<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        for (Degree degree : getAdministratedDegrees()) {
            if (degree.isBolonhaMasterOrDegree()) {
                result.add(degree);
            }
        }
        return result;
    }

    public Set<Degree> getAdministratedDegreesForStudentCreationWithoutCandidacy() {
        final Set<Degree> result = new TreeSet<Degree>(Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID);
        for (Degree degree : getAdministratedDegrees()) {
            if (degree.getDegreeType().canCreateStudent() && !degree.getDegreeType().canCreateStudentOnlyWithCandidacy()) {
                result.add(degree);
            }
        }
        return result;
    }

    public void delete() {
        checkRulesToDelete();

        setUnit(null);
        setServiceAgreementTemplate(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    private void checkRulesToDelete() {
        if (hasAnyAcademicServiceRequests()) {
            throw new DomainException("error.AdministrativeOffice.cannot.delete");
        }
        if (hasAnyManagedAcademicProgram()) {
            throw new DomainException("error.AdministrativeOffice.cannot.delete");
        }
        if (hasAnyRectorateSubmissionBatch()) {
            throw new DomainException("error.AdministrativeOffice.cannot.delete");
        }
        if (hasAnyEvents()) {
            throw new DomainException("error.AdministrativeOffice.cannot.delete");
        }
        if (hasAnyEventReportQueueJob()) {
            throw new DomainException("error.AdministrativeOffice.cannot.delete");
        }
        if (!getAcademicAuthorizationGroupSet().isEmpty()) {
            throw new DomainException("error.administrativeOffice.cannotDeleteAdministrativeOfficeUsedInAccessControl");
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
        for (AcademicProgram program : getManagedAcademicProgram()) {
            if (program instanceof PhdProgram) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.report.events.EventReportQueueJob> getEventReportQueueJob() {
        return getEventReportQueueJobSet();
    }

    @Deprecated
    public boolean hasAnyEventReportQueueJob() {
        return !getEventReportQueueJobSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accessControl.academicAdministration.PersistentAcademicAuthorizationGroup> getAcademicGroup() {
        return getAcademicGroupSet();
    }

    @Deprecated
    public boolean hasAnyAcademicGroup() {
        return !getAcademicGroupSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.RectorateSubmissionBatch> getRectorateSubmissionBatch() {
        return getRectorateSubmissionBatchSet();
    }

    @Deprecated
    public boolean hasAnyRectorateSubmissionBatch() {
        return !getRectorateSubmissionBatchSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.AcademicProgram> getManagedAcademicProgram() {
        return getManagedAcademicProgramSet();
    }

    @Deprecated
    public boolean hasAnyManagedAcademicProgram() {
        return !getManagedAcademicProgramSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.accounting.AcademicEvent> getEvents() {
        return getEventsSet();
    }

    @Deprecated
    public boolean hasAnyEvents() {
        return !getEventsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest> getAcademicServiceRequests() {
        return getAcademicServiceRequestsSet();
    }

    @Deprecated
    public boolean hasAnyAcademicServiceRequests() {
        return !getAcademicServiceRequestsSet().isEmpty();
    }

    @Deprecated
    public boolean hasServiceAgreementTemplate() {
        return getServiceAgreementTemplate() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasAdministrativeOfficeType() {
        return getAdministrativeOfficeType() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
