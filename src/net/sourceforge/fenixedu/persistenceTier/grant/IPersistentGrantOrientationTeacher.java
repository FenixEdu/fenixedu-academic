package net.sourceforge.fenixedu.persistenceTier.grant;

/**
 * 
 * @author Barbosa
 * @author Pica
 */
import net.sourceforge.fenixedu.domain.grant.contract.GrantOrientationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentGrantOrientationTeacher extends IPersistentObject {
    public GrantOrientationTeacher readActualGrantOrientationTeacherByContract(Integer contractId,Integer idInternal) throws ExcepcaoPersistencia;
}
