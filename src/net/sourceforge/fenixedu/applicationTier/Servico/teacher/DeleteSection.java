package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

/**
 * 
 * @author lmac1
 */
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.notAuthorizedServiceDeleteException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.fileSuport.FileSuport;
import net.sourceforge.fenixedu.fileSuport.IFileSuport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

public class DeleteSection implements IServico {
    private static DeleteSection service = new DeleteSection();

    public static DeleteSection getService() {
        return service;
    }

    private DeleteSection() {
    }

    public final String getNome() {
        return "DeleteSection";
    }

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode)
            throws FenixServiceException {
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IPersistentSite persistentSite = sp.getIPersistentSite();
            IPersistentSection persistentSection = sp.getIPersistentSection();
            ISite site = null;
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, infoExecutionCourseCode);
            site = persistentSite.readByExecutionCourse(executionCourse);
            ISection sectionToDelete = (ISection) persistentSection
                    .readByOID(Section.class, sectionCode);
            if (sectionToDelete == null) {
                throw new FenixServiceException("non existing section");
            }
            IFileSuport fileSuport = FileSuport.getInstance();
            long size = 1;

            size = fileSuport.getDirectorySize(sectionToDelete.getSlideName());

            if (size > 0) {
                throw new notAuthorizedServiceDeleteException();
            }
            ISection superiorSection = sectionToDelete.getSuperiorSection();
            Integer sectionToDeleteOrder = sectionToDelete.getSectionOrder();

            persistentSection.delete(sectionToDelete);

            sp.confirmarTransaccao();

            sp.iniciarTransaccao();
            List sectionsReordered = persistentSection.readBySiteAndSection(site, superiorSection);
            Iterator iterSections = sectionsReordered.iterator();
            while (iterSections.hasNext()) {
                ISection section = (ISection) iterSections.next();
                Integer sectionOrder = section.getSectionOrder();
                if (sectionOrder.intValue() > sectionToDeleteOrder.intValue()) {
                    persistentSection.simpleLockWrite(section);
                    section.setSectionOrder(new Integer(sectionOrder.intValue() - 1));
                }
            }
            return new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}