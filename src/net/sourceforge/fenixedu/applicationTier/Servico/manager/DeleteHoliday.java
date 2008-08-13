package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Holiday;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteHoliday extends Service {

    public void run(final Holiday holiday) {
        holiday.delete();
    }

}
