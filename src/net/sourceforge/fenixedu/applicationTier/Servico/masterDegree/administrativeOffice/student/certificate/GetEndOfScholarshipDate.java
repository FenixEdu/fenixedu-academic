package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.certificate;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class GetEndOfScholarshipDate extends Service {

    public Date run(Integer studentCurricularPlanID) {
	StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanID);

	IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
		.getInstance();

	IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory
		.getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());

	return masterDegreeCurricularPlanStrategy.dateOfEndOfScholarship(studentCurricularPlan);

    }

}
