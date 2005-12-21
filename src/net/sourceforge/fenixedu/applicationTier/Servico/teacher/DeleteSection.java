package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

/**
 * 
 * @author lmac1
 */
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.fileSupport.JdbcMysqlFileSupport;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteSection implements IService {

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSection persistentSection = sp.getIPersistentSection();

        ISection sectionToDelete = (ISection) persistentSection.readByOID(Section.class, sectionCode);

        if (sectionToDelete == null) {
            throw new FenixServiceException("non existing section");
        }

        testFilesExistence(sectionToDelete);

        sectionToDelete.delete();

        return Boolean.TRUE;
    }

    private void testFilesExistence(ISection sectionToDelete) throws notAuthorizedServiceDeleteException {
        for (final IItem item :  sectionToDelete.getAssociatedItems()) {
            Collection<FileSuportObject> listFiles = JdbcMysqlFileSupport.listFiles(item.getSlideName());
            if (!listFiles.isEmpty()) {
                throw new notAuthorizedServiceDeleteException();
            }
        }

        for (final ISection section : sectionToDelete.getAssociatedSections()) {
            testFilesExistence(section);
        }
    }
}