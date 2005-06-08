/*
 * Created on Jun 2, 2005
 *  by jdnf
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class StudentGroupAttendVO extends VersionedObjectsBase implements IPersistentStudentGroupAttend {

    public IStudentGroupAttend readByStudentGroupAndAttend(final Integer studentGroupID,
            final Integer attendID) throws ExcepcaoPersistencia {

        final IAttends attends = (IAttends) readByOID(Attends.class, attendID);
        if (attends != null) {
            final List<IStudentGroupAttend> studentGroupAttends = attends.getStudentGroupAttends();
            for (final IStudentGroupAttend studentGroupAttend : studentGroupAttends) {
                if (studentGroupAttend.getStudentGroup().getIdInternal().equals(studentGroupID)) {
                    return studentGroupAttend;
                }
            }
        }
        return null;
    }
}
