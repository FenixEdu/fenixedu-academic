package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituationWithInfoPersonAndInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class UpdateAndReadGratuitySituationsByStudentNumber extends Service {

    public List<InfoGratuitySituation> run(Integer studentNumber) throws ExcepcaoPersistencia {
        
        Registration student = Registration.readStudentByNumberAndDegreeType(studentNumber, DegreeType.MASTER_DEGREE);
        List<StudentCurricularPlan> studentCurricularPlansList = student.getStudentCurricularPlans();

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