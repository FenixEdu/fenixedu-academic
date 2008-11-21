package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteUnitFile extends FenixService {

    @Service
    public static void run(UnitFile file) {
	String uniqueID = file.getExternalStorageIdentification();
	file.delete();
	new DeleteFileRequest(AccessControl.getPerson(), uniqueID);
    }
}