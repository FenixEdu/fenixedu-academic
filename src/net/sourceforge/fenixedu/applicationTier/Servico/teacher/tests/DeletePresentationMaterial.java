package net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.tests.NewMathMlMaterial;
import net.sourceforge.fenixedu.domain.tests.NewPictureMaterial;
import net.sourceforge.fenixedu.domain.tests.NewStringMaterial;
import net.sourceforge.fenixedu.domain.tests.PictureMaterialFile;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeletePresentationMaterial extends Service {
	public void run(NewMathMlMaterial mathMlMaterial) throws FenixServiceException, ExcepcaoPersistencia {
		mathMlMaterial.delete();
	}

	public void run(NewStringMaterial stringMaterial) throws FenixServiceException, ExcepcaoPersistencia {
		stringMaterial.delete();
	}

	public void run(NewPictureMaterial pictureMaterial) throws FenixServiceException,
			ExcepcaoPersistencia {
	
		PictureMaterialFile pictureMaterialFile = pictureMaterial.getPictureMaterialFile();

		if (pictureMaterialFile.getPictureMaterialsCount() == 1) {
			new DeleteFileRequest(AccessControl.getPerson(),pictureMaterialFile.getExternalStorageIdentification());
		}

		pictureMaterial.delete();
	}
}
