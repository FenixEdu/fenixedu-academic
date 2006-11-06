package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.Date;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class EditFunction extends Service {

    public void run(Integer functionID, String functionName, Date beginDate, Date endDate,
            FunctionType type) throws ExcepcaoPersistencia, FenixServiceException, DomainException {

        Function function = (Function) rootDomainObject.readAccountabilityTypeByOID(functionID);
        if (function == null) {
            throw new FenixServiceException("error.noFunction");
        }

        YearMonthDay begin = (beginDate != null) ? YearMonthDay.fromDateFields(beginDate) : null;
	YearMonthDay end = (endDate != null) ? YearMonthDay.fromDateFields(endDate) : null;
        function.edit(functionName, begin, end, type);
    }
}
