package net.sourceforge.fenixedu.persistenceTier.grant;

/**
 * 
 * @author Barbosa
 * @author Pica
 */
import net.sourceforge.fenixedu.domain.grant.contract.IGrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentGrantOrientationTeacher extends IPersistentObject {
    public IGrantOrientationTeacher readActualGrantOrientationTeacherByContract(Integer contractId,
            Integer idInternal) throws ExcepcaoPersistencia;
}
