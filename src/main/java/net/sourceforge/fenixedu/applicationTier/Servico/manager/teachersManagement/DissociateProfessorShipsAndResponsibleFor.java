package net.sourceforge.fenixedu.applicationTier.Servico.manager.teachersManagement;

import java.util.ArrayList;
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
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DissociateProfessorShipsAndResponsibleFor {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static Map run(String personNumber, List<Integer> professorships, List<Integer> responsibleFors)
            throws FenixServiceException {

        if (personNumber == null) {
            throw new FenixServiceException("nullPersonNumber");
        }
        final Person person = Person.readPersonByIstUsername(personNumber);
        // final Teacher teacher = Teacher.readByNumber(teacherNumber);
        if (person == null) {
            throw new NonExistingServiceException("noPerson");
        }

        List<InfoProfessorship> professorshipsWithSupportLessons = new ArrayList<InfoProfessorship>();
        List<InfoProfessorship> professorshipsWithShifts = new ArrayList<InfoProfessorship>();
        if (professorships != null && responsibleFors != null) {
            List<Professorship> newProfessorships = new ArrayList<Professorship>();
            for (Integer professorshipId : professorships) {
                Professorship professorship = AbstractDomainObject.fromExternalId(professorshipId);
                if (professorship == null) {
                    throw new FenixServiceException("nullPSNorRF");
                }

                if (!(professorship.getPerson() == person)) {
                    throw new FenixServiceException("notPSNorRFPerson");
                }
                newProfessorships.add(professorship);
            }

            List<Professorship> newResponsibleFor = new ArrayList<Professorship>();
            for (Integer responsibleForId : responsibleFors) {
                Professorship responsibleFor = AbstractDomainObject.fromExternalId(responsibleForId);
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
                List<SupportLesson> supportLessons = professorship.getSupportLessons();
                List<ShiftProfessorship> shiftProfessorships = professorship.getAssociatedShiftProfessorship();

                if ((shiftProfessorships == null || shiftProfessorships.isEmpty())
                        && (supportLessons == null || supportLessons.isEmpty())) {

                    List<Summary> summaryList = professorship.getAssociatedSummaries();
                    if (summaryList != null && !summaryList.isEmpty()) {
                        for (Summary summary : summaryList) {
                            summary.removeProfessorship();
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