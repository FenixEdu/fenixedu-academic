package net.sourceforge.fenixedu.persistenceTier.teacher;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentExternalActivity extends IPersistentObject {
    public List readByTeacherId(Integer teacherId) throws ExcepcaoPersistencia;
}