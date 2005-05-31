package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class EditSection implements IService {
    //	this method reorders some sections but not the section that we are
    // editing
    private Integer organizeSectionsOrder(Integer newOrder, Integer oldOrder, ISection superiorSection,
            ISite site) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSection persistentSection = persistentSuport.getIPersistentSection();

        List sectionsList = null;                
        if(superiorSection != null){
	        sectionsList = persistentSection.readBySiteAndSection(site.getExecutionCourse().getSigla(),                
	                site.getExecutionCourse().getExecutionPeriod().getName(),
	                site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear(),
	                superiorSection.getIdInternal());
        }
        else{
            sectionsList = persistentSection.readBySiteAndSection(site.getExecutionCourse().getSigla(),                
	                site.getExecutionCourse().getExecutionPeriod().getName(),
	                site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear(),
	                null);
        }

        Iterator iterSections = sectionsList.iterator();

        if (newOrder.intValue() == -2) {
            newOrder = new Integer(sectionsList.size() - 1);
        }

        if (newOrder.intValue() - oldOrder.intValue() > 0) {
            while (iterSections.hasNext()) {

                ISection iterSection = (ISection) iterSections.next();
                int iterSectionOrder = iterSection.getSectionOrder().intValue();

                if (iterSectionOrder > oldOrder.intValue() && iterSectionOrder <= newOrder.intValue()) {
                    persistentSection.simpleLockWrite(iterSection);
                    iterSection.setSectionOrder(new Integer(iterSectionOrder - 1));
                }
            }
        } else {
            while (iterSections.hasNext()) {
                ISection iterSection = (ISection) iterSections.next();

                int iterSectionOrder = iterSection.getSectionOrder().intValue();

                if (iterSectionOrder >= newOrder.intValue() && iterSectionOrder < oldOrder.intValue()) {

                    persistentSection.simpleLockWrite(iterSection);
                    iterSection.setSectionOrder(new Integer(iterSectionOrder + 1));
                }
            }
        }

        return newOrder;
    }

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, String newSectionName,
            Integer newOrder) throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSection persistentSection = sp.getIPersistentSection();

        ISection iSection = (ISection) persistentSection.readByOID(Section.class, sectionCode);

        if (iSection == null) {
            throw new NonExistingServiceException();
        }
        persistentSection.simpleLockWrite(iSection);
        iSection.setName(newSectionName);

        ISite site = sp.getIPersistentSite().readByExecutionCourse(infoExecutionCourseCode);

        InfoSection infoSection = Cloner.copyISection2InfoSection(iSection);
        Integer oldOrder = infoSection.getSectionOrder();

        if (newOrder != oldOrder) {
            newOrder = organizeSectionsOrder(newOrder, oldOrder, iSection.getSuperiorSection(), site);
        }

        iSection.setName(newSectionName);
        iSection.setSectionOrder(newOrder);

        return new Boolean(true);
    }
}