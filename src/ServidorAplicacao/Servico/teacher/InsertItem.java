package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoItem;
import DataBeans.util.Cloner;
import Dominio.IItem;
import Dominio.ISection;
import Dominio.Section;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentSection;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author Fernanda Quitério
 */
public class InsertItem implements IService {

    protected int organizeExistingItemsOrder(ISection section, int insertItemOrder)
            throws FenixServiceException {

        IPersistentItem persistentItem = null;
        try {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            persistentItem = persistentSuport.getIPersistentItem();

            List itemsList = persistentItem.readAllItemsBySection(section);

            if (itemsList != null) {

                if (insertItemOrder == -1) {
                    insertItemOrder = itemsList.size();
                }

                Iterator iterItems = itemsList.iterator();
                while (iterItems.hasNext()) {

                    IItem item = (IItem) iterItems.next();
                    int itemOrder = item.getItemOrder().intValue();

                    if (itemOrder >= insertItemOrder) {
                        persistentItem.simpleLockWrite(item);
                        item.setItemOrder(new Integer(itemOrder + 1));
                    }
                }
            }
        } catch (ExistingPersistentException excepcaoPersistencia) {
            throw new ExistingServiceException(excepcaoPersistencia);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        return insertItemOrder;
    }

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, InfoItem infoItem)
            throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        final IPersistentSection persistentSection = persistentSuport.getIPersistentSection();
        final IPersistentItem persistentItem = persistentSuport.getIPersistentItem();

        final ISection section = (ISection) persistentSection.readByOID(Section.class, sectionCode);
        infoItem.setInfoSection(Cloner.copyISection2InfoSection(section));

        if (persistentItem.readBySectionAndName(section, infoItem.getName()) != null) {
            throw new ExistingServiceException();
        }

        final IItem item = Cloner.copyInfoItem2IItem(infoItem);
        persistentItem.simpleLockWrite(item);
        final Integer itemOrder = new Integer(organizeExistingItemsOrder(section, infoItem
                .getItemOrder().intValue()));
        item.setItemOrder(itemOrder);

        return new Boolean(true);
    }

}