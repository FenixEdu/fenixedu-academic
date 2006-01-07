package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class UpdateAndReadGratuitySituationsByStudentNumber implements IService {

    public List<InfoGratuitySituation> run(Integer studentNumber) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        List<StudentCurricularPlan> studentCurricularPlansList = sp
                .getIStudentCurricularPlanPersistente().readAllFromStudent(studentNumber.intValue());

        List<InfoGratuitySituation> infoGratuitySituationsList = new ArrayList<InfoGratuitySituation>();

        for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlansList) {

            List<GratuitySituation> gratuitySituations = studentCurricularPlan.getGratuitySituations();
            for (GratuitySituation gratuitySituation : gratuitySituations) {
                gratuitySituation.updateValues();
                
                infoGratuitySituationsList.add(InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree
                        .newInfoFromDomain(gratuitySituation));
            }
        }

        return infoGratuitySituationsList;
    }
}