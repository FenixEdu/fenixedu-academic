/*
 * Created on 13/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */
public class VerifyIfCanEnrollStudentGroupsInShift implements IService {

    public boolean run(Integer executionCourseCode, Integer groupPropertiesCode, Integer shiftCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente ps = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final ITurnoPersistente persistentShift = ps.getITurnoPersistente();

        final Grouping grouping = (Grouping) ps.getIPersistentObject().readByOID(Grouping.class, groupPropertiesCode);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final Shift shift = (Shift) persistentShift.readByOID(Shift.class, shiftCode);

        if (grouping.getShiftType() != shift.getTipo()) {
            return false;
        }

        final List studentGroups = grouping.getStudentGroups();
        List studentGroupsAux = getStudentGroupsByShift(grouping, shift);

        if (studentGroups.size() == studentGroupsAux.size()) {
            return false;
        }

        return true;
    }

    private List getStudentGroupsByShift(Grouping grouping, Shift shift) {
        List result = new ArrayList();
        List<StudentGroup> studentGroups = grouping.getStudentGroupsWithShift();
        for (final StudentGroup studentGroup : studentGroups) {
            if (studentGroup.getShift().equals(shift)) {
                result.add(studentGroup);
            }
        }
        return result;
    }

}
