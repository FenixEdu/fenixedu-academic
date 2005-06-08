/*
 * Created on Jun 2, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.AttendsSet;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class StudentGroupVO extends VersionedObjectsBase implements IPersistentStudentGroup {

    public IStudentGroup readStudentGroupByAttendsSetAndGroupNumber(final Integer attendsSetID,
            final Integer studentGroupNumber) throws ExcepcaoPersistencia {

        final IAttendsSet attendsSet = (IAttendsSet) readByOID(AttendsSet.class, attendsSetID);
        if (attendsSet != null) {
            final List<IStudentGroup> studentGroups = attendsSet.getStudentGroups();
            for (final IStudentGroup studentGroup : studentGroups) {
                if (studentGroup.getGroupNumber().equals(studentGroupNumber))
                    return studentGroup;
            }
        }
        return null;
    }

    public List<IStudentGroup> readAllStudentGroupByAttendsSetAndShift(final Integer attendsSetID,
            final Integer shiftID) throws ExcepcaoPersistencia {

        final List<IStudentGroup> result = new ArrayList<IStudentGroup>();
        final IShift shift = (IShift) readByOID(Shift.class, shiftID);

        if (shift != null) {
            final List<IStudentGroup> studentGroups = shift.getAssociatedStudentGroups();
            for (final IStudentGroup studentGroup : studentGroups) {
                if (studentGroup.getAttendsSet().getIdInternal().equals(attendsSetID)) {
                    result.add(studentGroup);
                }
            }
        }
        return result;
    }
}
