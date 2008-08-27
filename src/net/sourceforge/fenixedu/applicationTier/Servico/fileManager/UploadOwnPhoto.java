package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

/**
 * @author Pedro Santos (pmrsa)
 */
public class UploadOwnPhoto extends Service {
    public void run(byte[] contents, byte[] compressed, ContentType contentType) throws ExcepcaoPersistencia, ExcepcaoInexistente {
	Person person = AccessControl.getPerson();
	person.setPersonalPhoto(new Photograph(contentType, new ByteArray(contents), new ByteArray(compressed), PhotoType.USER));
    }
}