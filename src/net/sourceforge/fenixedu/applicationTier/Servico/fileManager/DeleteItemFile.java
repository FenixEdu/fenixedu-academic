package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;

import org.apache.slide.common.SlideException;

public class DeleteItemFile extends Service {

	public void run(Integer itemId, String fileName) throws FenixServiceException, ExcepcaoPersistencia, SlideException {
		final Item item = rootDomainObject.readItemByOID(itemId);
        	final String slidename = item.getSlideName();
        	JdbcMysqlFileSupport.deleteFile(slidename, fileName);
	}

}
