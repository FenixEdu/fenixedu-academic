/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class EditPrecedentDegreeInformation extends FenixService {

    @Service
    public static void run(PrecedentDegreeInformationBean precedentDegreeInformationBean) {
        precedentDegreeInformationBean.getPrecedentDegreeInformation().edit(precedentDegreeInformationBean);
    }

}