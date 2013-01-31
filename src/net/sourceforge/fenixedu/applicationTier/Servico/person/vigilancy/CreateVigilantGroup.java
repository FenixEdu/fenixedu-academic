package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class CreateVigilantGroup extends FenixService {

	@Service
	public static void run(String name, Unit unit, String convokeStrategy, String contactEmail, String rulesLink,
			DateTime beginFirstAllowedPeriod, DateTime endFirstAllowedPeriod, DateTime beginSecondAllowedPeriod,
			DateTime endSecondAllowedPeriod) {

		VigilantGroup vigilantGroup = new VigilantGroup();
		vigilantGroup.setName(name);
		vigilantGroup.setUnit(unit);
		vigilantGroup.setContactEmail(contactEmail);
		vigilantGroup.setRulesLink(rulesLink);
		vigilantGroup.setConvokeStrategy(convokeStrategy);
		vigilantGroup.setBeginOfFirstPeriodForUnavailablePeriods(beginFirstAllowedPeriod);
		vigilantGroup.setEndOfFirstPeriodForUnavailablePeriods(endFirstAllowedPeriod);
		vigilantGroup.setBeginOfSecondPeriodForUnavailablePeriods(beginSecondAllowedPeriod);
		vigilantGroup.setEndOfSecondPeriodForUnavailablePeriods(endSecondAllowedPeriod);
		ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
		vigilantGroup.setExecutionYear(currentYear);

	}

}