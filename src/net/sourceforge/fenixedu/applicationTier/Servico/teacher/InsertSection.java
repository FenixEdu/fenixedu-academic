package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSection;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Fernanda Quitério
 */
public class InsertSection implements IService {

    private int organizeExistingSectionsOrder(ISection superiorSection, ISite site,
            int insertSectionOrder) throws FenixServiceException, ExcepcaoPersistencia {

        IPersistentSection persistentSection = null;
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        persistentSection = persistentSuport.getIPersistentSection();

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
               
        if (sectionsList != null) {
            if (insertSectionOrder == -1) {
                insertSectionOrder = sectionsList.size();
            }
            Iterator iterSections = sectionsList.iterator();
            while (iterSections.hasNext()) {

                ISection iterSection = (ISection) iterSections.next();
                int sectionOrder = iterSection.getSectionOrder().intValue();

                if (sectionOrder >= insertSectionOrder) {
                    persistentSection.simpleLockWrite(iterSection);
                    iterSection.setSectionOrder(new Integer(sectionOrder + 1));
                }
            }
        }

        return insertSectionOrder;
    }

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, String sectionName,
            Integer sectionOrder) throws FenixServiceException, ExcepcaoPersistencia {

        ISection section = null;

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
        IPersistentSection persistentSection = persistentSuport.getIPersistentSection();
        ISite iSite = persistentSite.readByExecutionCourse(infoExecutionCourseCode);

        ISection parentSection = null;
        if (sectionCode != null) {
            parentSection = (ISection) persistentSection.readByOID(Section.class, sectionCode);
        }
        sectionOrder = new Integer(organizeExistingSectionsOrder(parentSection, iSite, sectionOrder
                .intValue()));

        Calendar calendario = Calendar.getInstance();
        section = new Section();
        persistentSection.simpleLockWrite(section);
        section.setSuperiorSection(parentSection);
        section.setSectionOrder(sectionOrder);
        section.setName(sectionName);
        section.setSite(iSite);
        section.setLastModifiedDate(calendario.getTime());

        return new Boolean(true);
    }
}