package net.sourceforge.fenixedu.persistenceTier.teacher;

import net.sourceforge.fenixedu.domain.teacher.IServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentServiceProviderRegime extends IPersistentObject {
    public IServiceProviderRegime readByTeacherId(Integer teacherId) throws ExcepcaoPersistencia;
}