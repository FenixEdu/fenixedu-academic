/*
 * Created on 6/Jan/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;

/**
 * @author Tânia Pousão
 */
public interface IPersistentGratuitySituation extends IPersistentObject {

	public GratuitySituation readGratuitySituatuionByStudentCurricularPlanAndGratuityValues(
			Integer studentCurricularPlanID, Integer gratuityValuesID) throws ExcepcaoPersistencia;

	public List readGratuitySituationsByDegreeCurricularPlan(Integer degreeCurricularPlanID)
			throws ExcepcaoPersistencia;

	public GratuitySituation readGratuitySituationByExecutionDegreeAndStudent(
			Integer executionDegreeID, Integer studentID) throws ExcepcaoPersistencia;

	public GratuitySituation readByStudentCurricularPlanAndGratuityValuesAndGratuitySituationType(
			Integer studentCurricularPlanID, Integer gratuityValuesID,
			GratuitySituationType gratuitySituationType) throws ExcepcaoPersistencia;
	
	public List readGratuitySituatuionListByStudentCurricularPlan(
            Integer studentCurricularPlanID) throws ExcepcaoPersistencia;

}