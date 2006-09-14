package net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTime;

public class UpdateVigilantGroup extends Service {

    public void run(VigilantGroup vigilantGroup, String name, String convokeStrategy,
            DateTime beginFirst, DateTime endFirst, DateTime beginSecond, DateTime endSecond)
            throws ExcepcaoPersistencia {

        vigilantGroup.setName(name);
        vigilantGroup.setConvokeStrategy(convokeStrategy);
        vigilantGroup.setBeginOfFirstPeriodForUnavailablePeriods(beginFirst);
        vigilantGroup.setEndOfFirstPeriodForUnavailablePeriods(endFirst);
        vigilantGroup.setBeginOfSecondPeriodForUnavailablePeriods(beginSecond);
        vigilantGroup.setEndOfSecondPeriodForUnavailablePeriods(endSecond);

    }

}