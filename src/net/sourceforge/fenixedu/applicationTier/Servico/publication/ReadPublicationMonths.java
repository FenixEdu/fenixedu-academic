
package net.sourceforge.fenixedu.applicationTier.Servico.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.constants.publication.PublicationConstants;
import net.sourceforge.fenixedu.util.Mes;
import pt.utl.ist.berserk.logic.serviceManager.IService;


public class ReadPublicationMonths implements IService {

    public List<String> run(int publicationTypeId) {
        List<String> MonthList = new ArrayList<String>();
        int i = PublicationConstants.MONTHS_INIT;
        while (i < PublicationConstants.MONTHS_LIMIT) {
            Mes mes = new Mes(i);
            MonthList.add(mes.toString());
            i++;
        }
        return MonthList;
    }
}