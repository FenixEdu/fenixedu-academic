/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadStudentCurricularPlansByNumberAndDegreeType implements IService {

    public ReadStudentCurricularPlansByNumberAndDegreeType() {
    }

    public List run(Integer studentNumber, DegreeType degreeType) throws ExcepcaoInexistente,
            FenixServiceException {
        ISuportePersistente sp = null;

        List studentCurricularPlans = null;

        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            studentCurricularPlans = sp.getIStudentCurricularPlanPersistente()
                    .readByStudentNumberAndDegreeType(studentNumber, degreeType);

        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException("Persistence layer error", ex);
        }

        if ((studentCurricularPlans == null) || (studentCurricularPlans.size() == 0)) {
            throw new NonExistingServiceException();
        }

        Iterator iterator = studentCurricularPlans.iterator();
        List result = new ArrayList();

        while (iterator.hasNext()) {
            IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator.next();
            result
                    .add(InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan
                            .newInfoFromDomain(studentCurricularPlan));
        }

        return result;
    }
}