package ServidorAplicacao.Servico.teacher;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.Section;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Fernanda Quitério
 */
public class InsertSection implements IService
{

    public InsertSection()
    {
    }

    private int organizeExistingSectionsOrder(ISection superiorSection, ISite site,
            int insertSectionOrder) throws FenixServiceException
    {

        IPersistentSection persistentSection = null;
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentSection = persistentSuport.getIPersistentSection();

            List sectionsList = persistentSection.readBySiteAndSection(site, superiorSection);

            if (sectionsList != null)
            {

                if (insertSectionOrder == -1)
                {
                    insertSectionOrder = sectionsList.size();
                }

                Iterator iterSections = sectionsList.iterator();
                while (iterSections.hasNext())
                {

                    ISection iterSection = (ISection) iterSections.next();
                    int sectionOrder = iterSection.getSectionOrder().intValue();

                    if (sectionOrder >= insertSectionOrder)
                    {
                        persistentSection.simpleLockWrite(iterSection);
                        iterSection.setSectionOrder(new Integer(sectionOrder + 1));

                    }

                }
            }
        }
        catch (ExistingPersistentException excepcaoPersistencia)
        {
            throw new ExistingServiceException(excepcaoPersistencia);
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {

            throw new FenixServiceException(excepcaoPersistencia);
        }
        return insertSectionOrder;
    }

    //infoItem with an infoSection

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, String sectionName,
            Integer sectionOrder) throws FenixServiceException
    {

        ISection section = null;

        try
        {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
            IPersistentSection persistentSection = persistentSuport.getIPersistentSection();

            ExecutionCourse executionCourse = new ExecutionCourse(infoExecutionCourseCode);
            IExecutionCourse iExecutionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(
                    executionCourse, false);
            ISite iSite = persistentSite.readByExecutionCourse(iExecutionCourse);

            ISection parentSection = null;
            if (sectionCode != null)
            {
                parentSection = (ISection) persistentSection.readByOId(new Section(sectionCode), false);
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

        }
        catch (ExistingPersistentException excepcaoPersistencia)
        {

            throw new ExistingServiceException(excepcaoPersistencia);
        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {

            throw new FenixServiceException(excepcaoPersistencia);
        }

        return new Boolean(true);
    }
}