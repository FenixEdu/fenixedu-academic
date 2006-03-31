package net.sourceforge.fenixedu.applicationTier.Servico.fileManager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;

import org.apache.slide.common.SlideException;

public class RetrieveItemFile extends Service {

    public static FileSuportObject run(final String slidename, final String fileName) throws SlideException {
        return JdbcMysqlFileSupport.retrieveFile(slidename, fileName);
    }

}