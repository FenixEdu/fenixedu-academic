package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class MergePrecedencesForDegreeCurricularPlan {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(Integer firstPrecedenceID, Integer secondPrecedenceID) throws FenixServiceException {

        if (firstPrecedenceID.intValue() == secondPrecedenceID.intValue()) {
            throw new InvalidArgumentsServiceException("error.manager.samePrecedencesForMerge");
        }

        Precedence firstPrecedence = RootDomainObject.getInstance().readPrecedenceByOID(firstPrecedenceID);
        Precedence secondPrecedence = RootDomainObject.getInstance().readPrecedenceByOID(secondPrecedenceID);

        firstPrecedence.mergePrecedences(secondPrecedence);
    }

}