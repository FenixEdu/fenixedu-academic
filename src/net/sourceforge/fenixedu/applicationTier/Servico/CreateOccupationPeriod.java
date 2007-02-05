package net.sourceforge.fenixedu.applicationTier.Servico;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.OccupationPeriod;

public class CreateOccupationPeriod extends Service {

    public OccupationPeriod run(YearMonthDay begin, YearMonthDay end) {
	OccupationPeriod period = OccupationPeriod.readFor(begin, end);
	if (period == null) {
	    return new OccupationPeriod(begin, end);
	}
	return period;
    }
}
