package net.sourceforge.fenixedu.applicationTier.Servico.research;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.UnitFile;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public class DeleteUnitFile extends Service {

	public void run(UnitFile file) {
		String uniqueID = file.getExternalStorageIdentification();
		file.delete();
		FileManagerFactory.getFactoryInstance().getSimpleFileManager().deleteFile(uniqueID);
	}
}
