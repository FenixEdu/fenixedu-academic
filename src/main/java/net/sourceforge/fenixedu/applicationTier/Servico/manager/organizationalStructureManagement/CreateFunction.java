package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateFunction {

    @Atomic
    public static void run(MultiLanguageString functionName, YearMonthDay begin, YearMonthDay end, FunctionType type,
            String unitID) throws FenixServiceException, DomainException {
        check(RolePredicates.MANAGER_PREDICATE);

        Unit unit = (Unit) FenixFramework.getDomainObject(unitID);
        if (unit == null) {
            throw new FenixServiceException("error.function.no.unit");
        }

        new Function(functionName, begin, end, type, unit);
    }
}