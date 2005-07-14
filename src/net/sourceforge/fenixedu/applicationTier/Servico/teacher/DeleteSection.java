package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

/**
 * 
 * @author lmac1
 */
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteSection implements IService {
    
    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentSection persistentSection = sp.getIPersistentSection();
            
            ISite site = persistentSite.readByExecutionCourse(infoExecutionCourseCode);
            ISection sectionToDelete = (ISection) persistentSection.readByOID(Section.class, sectionCode);
           
            if (sectionToDelete == null) {
                throw new FenixServiceException("non existing section");
            }
            
            testFilesExistence(sectionToDelete);
            
            sectionToDelete.delete();
            
            persistentSection.deleteByOID(Section.class, sectionToDelete.getIdInternal());
            
            return new Boolean(true);
    }

    private void testFilesExistence(ISection sectionToDelete) throws notAuthorizedServiceDeleteException {
        IFileSuport fileSuport = FileSuport.getInstance();
        long size = 1;
        size = fileSuport.getDirectorySize(sectionToDelete.getSlideName());
        
        if (size > 0) {
            throw new notAuthorizedServiceDeleteException();
        }
    }
}