package net.sourceforge.fenixedu.persistenceTier.teacher;

import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

public interface IPersistentWeeklyOcupation extends IPersistentObject {
    public WeeklyOcupation readByTeacherId(Integer teacherId) throws ExcepcaoPersistencia;
}