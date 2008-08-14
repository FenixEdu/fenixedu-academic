package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class DeleteUnitFile extends Service {

    public void run(UnitFile file) {
	String uniqueID = file.getExternalStorageIdentification();
	file.delete();
	new DeleteFileRequest(AccessControl.getPerson(), uniqueID);
    }
}
