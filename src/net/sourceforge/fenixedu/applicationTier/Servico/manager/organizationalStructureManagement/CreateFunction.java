package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateFunction extends Service {

    public void run(MultiLanguageString functionName, YearMonthDay begin, YearMonthDay end, FunctionType type, Integer unitID)
	    throws FenixServiceException, DomainException {

	Unit unit = (Unit) rootDomainObject.readPartyByOID(unitID);
	if (unit == null) {
	    throw new FenixServiceException("error.function.no.unit");
	}
			
	new Function(functionName, begin, end, type, unit);
    }
}
