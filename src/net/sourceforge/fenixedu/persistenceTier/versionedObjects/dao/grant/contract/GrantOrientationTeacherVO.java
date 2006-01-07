package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class GrantOrientationTeacherVO extends VersionedObjectsBase implements
        IPersistentGrantOrientationTeacher {

    public GrantOrientationTeacher readActualGrantOrientationTeacherByContract(Integer contractId,
            Integer idInternal) throws ExcepcaoPersistencia {

        List<GrantOrientationTeacher> grantOrientationTeachers = (List<GrantOrientationTeacher>) readAll(GrantOrientationTeacher.class);
        GrantOrientationTeacher result = null;

        for (GrantOrientationTeacher teacher : grantOrientationTeachers) {
            if (teacher.getKeyContract().equals(contractId)
                    && !teacher.getIdInternal().equals(idInternal)
                    && (result == null || result.getBeginDate().getTime() < teacher.getBeginDate()
                            .getTime())) {
                result = teacher;
            }
        }
        
        return result;
    }

}