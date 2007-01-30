package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.tests.NewMathMlMaterial;
import net.sourceforge.fenixedu.domain.tests.NewPictureMaterial;
import net.sourceforge.fenixedu.domain.tests.NewStringMaterial;
import net.sourceforge.fenixedu.domain.tests.PictureMaterialFile;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.IFileManager;

public class DeletePresentationMaterial extends Service {
	public void run(NewMathMlMaterial mathMlMaterial) throws FenixServiceException, ExcepcaoPersistencia {
		mathMlMaterial.delete();
	}

	public void run(NewStringMaterial stringMaterial) throws FenixServiceException, ExcepcaoPersistencia {
		stringMaterial.delete();
	}

	public void run(NewPictureMaterial pictureMaterial) throws FenixServiceException,
			ExcepcaoPersistencia {
		IFileManager fileManager = FileManagerFactory.getFactoryInstance().getFileManager();

		PictureMaterialFile pictureMaterialFile = pictureMaterial.getPictureMaterialFile();

		if (pictureMaterialFile.getPictureMaterialsCount() == 1) {
			fileManager.deleteFile(pictureMaterialFile.getExternalStorageIdentification());
		}

		pictureMaterial.delete();
	}
}
