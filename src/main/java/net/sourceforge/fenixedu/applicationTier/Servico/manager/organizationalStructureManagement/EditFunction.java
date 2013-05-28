package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EditFunction {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer functionID, MultiLanguageString functionName, YearMonthDay begin, YearMonthDay end,
            FunctionType type) throws FenixServiceException, DomainException {

        Function function = (Function) AbstractDomainObject.fromExternalId(functionID);
        if (function == null) {
            throw new FenixServiceException("error.noFunction");
        }

        function.edit(functionName, begin, end, type);
    }
}