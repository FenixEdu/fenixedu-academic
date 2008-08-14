package net.sourceforge.fenixedu.applicationTier.Servico.research.prizes;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.Prize;

public class DeletePrize extends Service {

    public void run(Prize prize) {
	prize.delete();
    }
}
