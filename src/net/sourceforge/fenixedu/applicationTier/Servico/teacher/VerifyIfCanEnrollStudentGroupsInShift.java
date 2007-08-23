/*
 * Created on 13/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa & rmalo
 * 
 */
public class VerifyIfCanEnrollStudentGroupsInShift extends Service {

    public boolean run(Integer executionCourseCode, Integer groupPropertiesCode, Integer shiftCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        final Grouping grouping = rootDomainObject.readGroupingByOID(groupPropertiesCode);

        if (grouping == null) {
            throw new ExistingServiceException();
        }

        final Shift shift = rootDomainObject.readShiftByOID(shiftCode);

        if (!shift.containsType(grouping.getShiftType())) {
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
