/*
 * Created on 26/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
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

    public List run(InfoSection infoSection) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentItem persistentItem = sp.getIPersistentItem();

        List<IItem> itemsList = persistentItem.readAllItemsBySection(infoSection.getIdInternal(),
                infoSection.getInfoSite().getInfoExecutionCourse().getSigla(), infoSection.getInfoSite()
                        .getInfoExecutionCourse().getInfoExecutionPeriod().getInfoExecutionYear()
                        .getYear(), infoSection.getInfoSite().getInfoExecutionCourse()
                        .getInfoExecutionPeriod().getName());

        List<InfoItem> infoItemsList = new ArrayList<InfoItem>(itemsList.size());
        for (IItem elem : itemsList) {
            infoItemsList.add(InfoItem.newInfoFromDomain(elem));
        }

//        Collections.sort(infoItemsList);
        return infoItemsList;
    }

}
