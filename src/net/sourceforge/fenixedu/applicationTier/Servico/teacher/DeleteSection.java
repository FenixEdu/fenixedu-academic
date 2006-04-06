package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

/**
 * 
 * @author lmac1
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteSection extends Service {

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        Section sectionToDelete = rootDomainObject.readSectionByOID(sectionCode);

        if (sectionToDelete == null) {
            throw new FenixServiceException("non existing section");
        }

        //testFilesExistence(sectionToDelete);

        sectionToDelete.delete();

        return Boolean.TRUE;
    }

    /*private void testFilesExistence(Section sectionToDelete) throws notAuthorizedServiceDeleteException {
        for (final Item item :  sectionToDelete.getAssociatedItems()) {
            Collection<FileSuportObject> listFiles = JdbcMysqlFileSupport.listFiles(item.getSlideName());
            if (!listFiles.isEmpty()) {
                throw new notAuthorizedServiceDeleteException();
            }
        }

        for (final Section section : sectionToDelete.getAssociatedSections()) {
            testFilesExistence(section);
        }
    }*/
}