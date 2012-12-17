package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.UnitFile;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteUnitFile extends FenixService {

    @Service
    public static void run(final UnitFile file) {
	file.delete();
    }
}