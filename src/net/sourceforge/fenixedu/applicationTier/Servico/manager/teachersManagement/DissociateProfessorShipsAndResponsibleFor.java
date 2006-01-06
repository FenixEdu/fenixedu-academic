package net.sourceforge.fenixedu.applicationTier.Servico.manager.teachersManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShiftProfessorship;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ISupportLesson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DissociateProfessorShipsAndResponsibleFor implements IService {

    public Map run(Integer teacherNumber, List<Integer> professorships, List<Integer> responsibleFors)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (teacherNumber == null) {
            throw new FenixServiceException("nullTeacherNumber");
        }

        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        final ITeacher teacher = sp.getIPersistentTeacher().readByNumber(teacherNumber);
        if (teacher == null) {
            throw new NonExistingServiceException("noTeacher");
        }

        List<InfoProfessorship> professorshipsWithSupportLessons = new ArrayList<InfoProfessorship>();
        List<InfoProfessorship> professorshipsWithShifts = new ArrayList<InfoProfessorship>();
        if (professorships != null && responsibleFors != null) {
            List<IProfessorship> newProfessorships = new ArrayList<IProfessorship>();
            for (Integer professorshipId : professorships) {
                IProfessorship professorship = (IProfessorship) sp.getIPersistentObject().readByOID(
                        Professorship.class, professorshipId);
                if (professorship == null) {
                    throw new FenixServiceException("nullPSNorRF");
                }

                if (!(professorship.getTeacher() == teacher)) {
                    throw new FenixServiceException("notPSNorRFTeacher");
                }
                newProfessorships.add(professorship);
            }

            List<IProfessorship> newResponsibleFor = new ArrayList<IProfessorship>();
            for (Integer responsibleForId : responsibleFors) {
                IProfessorship responsibleFor = (IProfessorship) sp.getIPersistentObject().readByOID(
                        Professorship.class, responsibleForId);
                if (responsibleFor == null) {
                    throw new FenixServiceException("nullPSNorRF");
                }

                if (!(responsibleFor.getTeacher() == teacher)) {
                    throw new FenixServiceException("notPSNorRFTeacher");
                }
                newResponsibleFor.add(responsibleFor);
            }

            // everything is ok for removal, but first check
            // professorships with support lessons and shifts
            for (IProfessorship professorship : newProfessorships) {
                List<ISupportLesson> supportLessons = professorship.getSupportLessons();
                List<IShiftProfessorship> shiftProfessorships = professorship
                        .getAssociatedShiftProfessorship();

                if ((shiftProfessorships == null || shiftProfessorships.isEmpty())
                        && (supportLessons == null || supportLessons.isEmpty())) {

                    final IPersistentSummary persistentSummary = sp.getIPersistentSummary();
                    List<ISummary> summaryList = persistentSummary.readByTeacher(professorship
                            .getExecutionCourse().getIdInternal(), professorship.getTeacher()
                            .getTeacherNumber());
                    if (summaryList != null && !summaryList.isEmpty()) {
                        for (ISummary summary : summaryList) {
                            summary.removeProfessorship();
                        }
                    }

                    professorship.delete();
                } else {
                    if (supportLessons.size() > 0) {
                        professorshipsWithSupportLessons.add(InfoProfessorshipWithAll
                                .newInfoFromDomain(professorship));
                    }
                    if (shiftProfessorships.size() > 0) {
                        professorshipsWithShifts.add(InfoProfessorshipWithAll
                                .newInfoFromDomain(professorship));
                    }
                }
            }

            for (IProfessorship responsibleFor : newResponsibleFor) {
                responsibleFor.setResponsibleFor(false);
            }
        }

        HashMap<String, List<InfoProfessorship>> professorshipsNotRemoved = new HashMap<String, List<InfoProfessorship>>();
        if (professorshipsWithSupportLessons.size() > 0 || professorshipsWithShifts.size() > 0) {
            professorshipsNotRemoved.put(new String("supportLessons"), professorshipsWithSupportLessons);
            professorshipsNotRemoved.put(new String("shifts"), professorshipsWithShifts);
        }

        return professorshipsNotRemoved;
    }

}
