package ServidorAplicacao.Servico.degreeAdministrativeOffice.enrolment.withoutRules.depercated;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.enrolment.context.depercated.EnrolmentValidationResult;
import ServidorAplicacao.strategy.enrolment.context.depercated.InfoEnrolmentContext;

/**
 * @author David Santos 16/Jun/2003
 */

public class ValidateActualEnrolmentWithoutRules implements IService
{

    public ValidateActualEnrolmentWithoutRules()
    {
    }

    public InfoEnrolmentContext run(
        InfoEnrolmentContext infoEnrolmentContext,
        List erolmentsToRemoveList)
        throws FenixServiceException
    {
        List currentEnroloments = infoEnrolmentContext.getActualEnrolment();

        if (((currentEnroloments == null) || (currentEnroloments.isEmpty()))
            && ((erolmentsToRemoveList == null) || (erolmentsToRemoveList.isEmpty())))
        {
            infoEnrolmentContext.getEnrolmentValidationResult().setErrorMessage(
                EnrolmentValidationResult.NO_CURRICULAR_COURSES_TO_ENROLL);
        }
        else
        {
            infoEnrolmentContext.getEnrolmentValidationResult().setSucess(true);
        }
        return infoEnrolmentContext;
    }
}