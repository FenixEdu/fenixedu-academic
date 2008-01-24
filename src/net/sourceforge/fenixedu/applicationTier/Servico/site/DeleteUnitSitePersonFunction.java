package net.sourceforge.fenixedu.applicationTier.Servico.site;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;

import org.joda.time.YearMonthDay;

public class DeleteUnitSitePersonFunction extends ManageVirtualFunction {

	public void run(UnitSite site, PersonFunction personFunction) {
		checkUnit(site, personFunction.getUnit());
		
		YearMonthDay tomorrow = new YearMonthDay().plusDays(1);
		if (! personFunction.belongsToPeriod(tomorrow, null)) {
			throw new DomainException("site.function.personFunction.notFuture");
		}
		
		personFunction.delete();
	}
	
}
