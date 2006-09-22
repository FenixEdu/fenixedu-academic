package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTime;

public class CreateVigilantGroup extends Service {

    public void run(String name, Unit unit, String convokeStrategy, String contactEmail, String rulesLink, DateTime beginFirstAllowedPeriod,
            DateTime endFirstAllowedPeriod, DateTime beginSecondAllowedPeriod,
            DateTime endSecondAllowedPeriod) throws ExcepcaoPersistencia {

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