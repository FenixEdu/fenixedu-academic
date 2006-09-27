/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class EditPrecedentDegreeInformation extends Service {

    public void run(PrecedentDegreeInformationBean precedentDegreeInformationBean) {

	precedentDegreeInformationBean.getPrecedentDegreeInformation().edit(
		precedentDegreeInformationBean);

    }

}
