package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

public interface IPersistentCompetenceCourse extends IPersistentObject {

    List<CompetenceCourse> readByCurricularStage(CurricularStage curricularStage) throws ExcepcaoPersistencia;

}
