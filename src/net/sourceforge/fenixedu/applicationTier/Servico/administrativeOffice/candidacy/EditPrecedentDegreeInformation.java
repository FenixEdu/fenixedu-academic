/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class EditPrecedentDegreeInformation extends FenixService {

    public void run(PrecedentDegreeInformationBean precedentDegreeInformationBean) {

	precedentDegreeInformationBean.getPrecedentDegreeInformation().edit(precedentDegreeInformationBean);

    }

}
