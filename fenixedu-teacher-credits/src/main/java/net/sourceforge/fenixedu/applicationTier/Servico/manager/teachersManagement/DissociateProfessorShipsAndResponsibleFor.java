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
package org.fenixedu.academic.service.services.manager.teachersManagement;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.dto.InfoProfessorship;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.ShiftProfessorship;
import org.fenixedu.academic.domain.Summary;
import org.fenixedu.academic.domain.SupportLesson;
import org.fenixedu.academic.predicate.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DissociateProfessorShipsAndResponsibleFor {

    @Atomic
    public static Map run(String personNumber, List<String> professorships, List<String> responsibleFors)
            throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        if (personNumber == null) {
            throw new FenixServiceException("nullPersonNumber");
        }
        final Person person = Person.readPersonByUsername(personNumber);
        // final Teacher teacher = Teacher.readByNumber(teacherNumber);
        if (person == null) {
            throw new NonExistingServiceException("noPerson");
        }

        List<InfoProfessorship> professorshipsWithSupportLessons = new ArrayList<InfoProfessorship>();
        List<InfoProfessorship> professorshipsWithShifts = new ArrayList<InfoProfessorship>();
        if (professorships != null && responsibleFors != null) {
            List<Professorship> newProfessorships = new ArrayList<Professorship>();
            for (String professorshipId : professorships) {
                Professorship professorship = FenixFramework.getDomainObject(professorshipId);
                if (professorship == null) {
                    throw new FenixServiceException("nullPSNorRF");
                }

                if (!(professorship.getPerson() == person)) {
                    throw new FenixServiceException("notPSNorRFPerson");
                }
                newProfessorships.add(professorship);
            }

            List<Professorship> newResponsibleFor = new ArrayList<Professorship>();
            for (String responsibleForId : responsibleFors) {
                Professorship responsibleFor = FenixFramework.getDomainObject(responsibleForId);
                if (responsibleFor == null) {
                    throw new FenixServiceException("nullPSNorRF");
                }

                if (!(responsibleFor.getPerson() == person)) {
                    throw new FenixServiceException("notPSNorRFPerson");
                }
                newResponsibleFor.add(responsibleFor);
            }

            // everything is ok for removal, but first check
            // professorships with support lessons and shifts
            for (Professorship professorship : newProfessorships) {
                Collection<SupportLesson> supportLessons = professorship.getSupportLessonsSet();
                Collection<ShiftProfessorship> shiftProfessorships = professorship.getAssociatedShiftProfessorshipSet();

                if ((shiftProfessorships == null || shiftProfessorships.isEmpty())
                        && (supportLessons == null || supportLessons.isEmpty())) {

                    Collection<Summary> summaryList = professorship.getAssociatedSummariesSet();
                    if (summaryList != null && !summaryList.isEmpty()) {
                        for (Summary summary : summaryList) {
                            summary.setProfessorship(null);
                        }
                    }

                    professorship.delete();
                } else {
                    if (supportLessons.size() > 0) {
                        professorshipsWithSupportLessons.add(InfoProfessorship.newInfoFromDomain(professorship));
                    }
                    if (shiftProfessorships.size() > 0) {
                        professorshipsWithShifts.add(InfoProfessorship.newInfoFromDomain(professorship));
                    }
                }
            }

            for (Professorship responsibleFor : newResponsibleFor) {
                responsibleFor.setResponsibleFor(false);
            }
        }

        HashMap<String, List<InfoProfessorship>> professorshipsNotRemoved = new HashMap<String, List<InfoProfessorship>>();
        if (professorshipsWithSupportLessons.size() > 0 || professorshipsWithShifts.size() > 0) {
            professorshipsNotRemoved.put("supportLessons", professorshipsWithSupportLessons);
            professorshipsNotRemoved.put("shifts", professorshipsWithShifts);
        }

        return professorshipsNotRemoved;
    }

}