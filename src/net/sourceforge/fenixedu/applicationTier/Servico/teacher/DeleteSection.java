package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

/**
 * 
 * @author lmac1
 */
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;
import net.sourceforge.fenixedu.applicationTier.IService;

public class DeleteSection implements IService {

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSection persistentSection = sp.getIPersistentSection();

        Section sectionToDelete = (Section) persistentSection.readByOID(Section.class, sectionCode);

        if (sectionToDelete == null) {
            throw new FenixServiceException("non existing section");
        }

        testFilesExistence(sectionToDelete);

        sectionToDelete.delete();

        return Boolean.TRUE;
    }

    private void testFilesExistence(Section sectionToDelete) throws notAuthorizedServiceDeleteException {
        for (final Item item :  sectionToDelete.getAssociatedItems()) {
            Collection<FileSuportObject> listFiles = JdbcMysqlFileSupport.listFiles(item.getSlideName());
            if (!listFiles.isEmpty()) {
                throw new notAuthorizedServiceDeleteException();
            }
        }

        for (final Section section : sectionToDelete.getAssociatedSections()) {
            testFilesExistence(section);
        }
    }
}