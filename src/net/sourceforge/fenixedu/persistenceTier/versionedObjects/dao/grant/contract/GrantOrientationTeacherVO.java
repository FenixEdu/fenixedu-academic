package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.grant.contract;

import java.util.List;

import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.grant.IPersistentGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

public class GrantOrientationTeacherVO extends VersionedObjectsBase implements
        IPersistentGrantOrientationTeacher {

    public IGrantOrientationTeacher readActualGrantOrientationTeacherByContract(Integer contractId,
            Integer idInternal) throws ExcepcaoPersistencia {

        List<IGrantOrientationTeacher> grantOrientationTeachers = (List<IGrantOrientationTeacher>) readAll(GrantOrientationTeacher.class);
        IGrantOrientationTeacher result = null;

        for (IGrantOrientationTeacher teacher : grantOrientationTeachers) {
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