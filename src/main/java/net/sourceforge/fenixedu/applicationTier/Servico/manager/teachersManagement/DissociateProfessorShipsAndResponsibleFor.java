package net.sourceforge.fenixedu.applicationTier.Servico.manager.teachersManagement;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.SupportLesson;
import net.sourceforge.fenixedu.predicates.RolePredicates;
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
                Collection<SupportLesson> supportLessons = professorship.getSupportLessons();
                Collection<ShiftProfessorship> shiftProfessorships = professorship.getAssociatedShiftProfessorship();

                if ((shiftProfessorships == null || shiftProfessorships.isEmpty())
                        && (supportLessons == null || supportLessons.isEmpty())) {

                    Collection<Summary> summaryList = professorship.getAssociatedSummaries();
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