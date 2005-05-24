/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IItem;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentItem;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac2 & lmac1
 */

public class ReadItems implements IService {

    /**
     * The ctor of this class.
     */
    public ReadItems() {
    }

    /**
     * Executes the service.
     *  
     */
    public List run(InfoSection infoSection) throws FenixServiceException {
        List itemsList = null;

        try {

            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentItem persistentItem = sp.getIPersistentItem();

            //ISection section = Cloner.copyInfoSection2ISection(infoSection);

            itemsList = persistentItem.readAllItemsBySection(infoSection.getIdInternal(),
                    infoSection.getInfoSite().getInfoExecutionCourse().getSigla(),
                    infoSection.getInfoSite().getInfoExecutionCourse().getInfoExecutionPeriod().getInfoExecutionYear().getYear(),
                    infoSection.getInfoSite().getInfoExecutionCourse().getInfoExecutionPeriod().getName());
            
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        List infoItemsList = new ArrayList(itemsList.size());
        Iterator iter = itemsList.iterator();

        while (iter.hasNext())
            infoItemsList.add(Cloner.copyIItem2InfoItem((IItem) iter.next()));

        //			if (itemsList == null || itemsList.isEmpty())
        //				throw new InvalidArgumentsServiceException();
        Collections.sort(infoItemsList);
        return infoItemsList;

    }

}

