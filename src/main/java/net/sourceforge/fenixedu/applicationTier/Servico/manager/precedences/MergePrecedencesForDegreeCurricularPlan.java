package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class MergePrecedencesForDegreeCurricularPlan {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(Integer firstPrecedenceID, Integer secondPrecedenceID) throws FenixServiceException {

        if (firstPrecedenceID.intValue() == secondPrecedenceID.intValue()) {
            throw new InvalidArgumentsServiceException("error.manager.samePrecedencesForMerge");
        }

        Precedence firstPrecedence = AbstractDomainObject.fromExternalId(firstPrecedenceID);
        Precedence secondPrecedence = AbstractDomainObject.fromExternalId(secondPrecedenceID);

        firstPrecedence.mergePrecedences(secondPrecedence);
    }

}