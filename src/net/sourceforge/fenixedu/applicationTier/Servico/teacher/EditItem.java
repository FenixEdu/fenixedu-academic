package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.domain.ISection;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTier.exceptions.ExistingPersistentException;

/**
 * @author Fernanda Quitério
 *  
 */
public class EditItem implements IServico {

    private static EditItem service = new EditItem();

    /**
     * 
     * The singleton access method of this class.
     *  
     */

    public static EditItem getService() {

        return service;

    }

    public String getNome() {

        return "EditItem";

    }

    // this method reorders some items but not the item that we are editing
    private Integer organizeItemsOrder(Integer newOrder, Integer oldOrder, ISection section)
            throws FenixServiceException {

        IPersistentItem persistentItem = null;
        try {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentItem = persistentSuport.getIPersistentItem();

            List itemsList = persistentItem.readAllItemsBySection(section);

            if (newOrder.intValue() == -2) {
                newOrder = new Integer(itemsList.size() - 1);
            }

            Iterator iterItems = itemsList.iterator();

            if (newOrder.intValue() - oldOrder.intValue() > 0)
                while (iterItems.hasNext()) {
                    IItem iterItem = (IItem) iterItems.next();
                    int iterItemOrder = iterItem.getItemOrder().intValue();

                    if (iterItemOrder > oldOrder.intValue() && iterItemOrder <= newOrder.intValue()) {
                        persistentItem.simpleLockWrite(iterItem);
                        iterItem.setItemOrder(new Integer(iterItemOrder - 1));

                    }
                }
            else
                while (iterItems.hasNext()) {
                    IItem iterItem = (IItem) iterItems.next();
                    int iterItemOrder = iterItem.getItemOrder().intValue();

                    if (iterItemOrder >= newOrder.intValue() && iterItemOrder < oldOrder.intValue()) {

                        persistentItem.simpleLockWrite(iterItem);
                        iterItem.setItemOrder(new Integer(iterItemOrder + 1));

                    }
                }
        } catch (ExistingPersistentException excepcaoPersistencia) {

            throw new ExistingServiceException(excepcaoPersistencia);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {

            throw new FenixServiceException(excepcaoPersistencia);
        }
        return newOrder;
    }

    /**
     * Executes the service.
     *  
     */
    public Boolean run(Integer infoExecutionCourseCode, Integer itemCode, InfoItem newInfoItem)
            throws FenixServiceException {
        IItem item = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentItem persistentItem = sp.getIPersistentItem();

            item = (IItem) persistentItem.readByOID(Item.class, itemCode, true);

            if (item == null) {
                throw new ExistingServiceException();
            }
            InfoItem oldInfoItem = Cloner.copyIItem2InfoItem(item);

            ISection section = Cloner.copyInfoSection2ISection(oldInfoItem.getInfoSection());

            Integer newOrder = newInfoItem.getItemOrder();
            Integer oldOrder = oldInfoItem.getItemOrder();

            if (newOrder.intValue() != oldOrder.intValue()) {
                newOrder = organizeItemsOrder(newOrder, oldOrder, section);
            }

            item.setInformation(newInfoItem.getInformation());
            item.setItemOrder(newOrder);
            item.setName(newInfoItem.getName());
            item.setUrgent(newInfoItem.getUrgent());

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return new Boolean(true);
    }

}