package net.sourceforge.fenixedu.applicationTier.Servico.research.prizes;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.research.Prize;

public class DeletePrize extends FenixService {

	public void run(Prize prize) {
		prize.delete();
	}
}
