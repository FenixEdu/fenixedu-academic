package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanEditor;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertDegreeCurricularPlan extends Service {

    public void run(InfoDegreeCurricularPlanEditor infoDcp) throws FenixServiceException, ExcepcaoPersistencia {

        if (infoDcp.getInfoDegree().getIdInternal() == null 
                || infoDcp.getName() == null
                || infoDcp.getInitialDate() == null 
                || infoDcp.getDegreeDuration() == null
                || infoDcp.getMinimalYearForOptionalCourses() == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Degree degree = rootDomainObject.readDegreeByOID(infoDcp.getInfoDegree().getIdInternal());
        if (degree == null) {
            throw new FenixServiceException("error.degreeCurricularPlan.non.existing.degree");
        }

        degree.createPreBolonhaDegreeCurricularPlan(
                infoDcp.getName(), 
                infoDcp.getState(),
                infoDcp.getInitialDate(), 
                infoDcp.getEndDate(), 
                infoDcp.getDegreeDuration(), 
                infoDcp.getMinimalYearForOptionalCourses(), 
                infoDcp.getNeededCredits(), 
                infoDcp.getMarkType(), 
                infoDcp.getNumerusClausus(), 
                infoDcp.getAnotation(), 
                infoDcp.getGradeScale());
    }

}
