package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlanEditor;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditDegreeCurricularPlan extends Service {

    public void run(InfoDegreeCurricularPlanEditor infoDcp) throws FenixServiceException, ExcepcaoPersistencia {

        if (infoDcp.getIdInternal() == null || infoDcp.getName() == null
                || infoDcp.getInitialDate() == null || infoDcp.getDegreeDuration() == null
                || infoDcp.getMinimalYearForOptionalCourses() == null) {
            throw new InvalidArgumentsServiceException();
        }

        final DegreeCurricularPlan dcpToEdit = rootDomainObject.readDegreeCurricularPlanByOID(
                        infoDcp.getIdInternal());
        if (dcpToEdit == null) {
            throw new FenixServiceException("message.nonExistingDegreeCurricularPlan");
        }

        try {
            dcpToEdit.edit(infoDcp.getName(), infoDcp.getState(), infoDcp.getInitialDate(), infoDcp
                    .getEndDate(), infoDcp.getDegreeDuration(), infoDcp.getMinimalYearForOptionalCourses(),
                    infoDcp.getNeededCredits(), infoDcp.getMarkType(), infoDcp.getNumerusClausus(), infoDcp
                            .getAnotation(), infoDcp.getGradeScale());
        } catch (DomainException e) {
            throw new FenixServiceException(e.getMessage());
        }
    }

}
