package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

public interface IPersistentCompetenceCourse extends IPersistentObject {

    List<ICompetenceCourse> readByCurricularStage(CurricularStage curricularStage) throws ExcepcaoPersistencia;

}
