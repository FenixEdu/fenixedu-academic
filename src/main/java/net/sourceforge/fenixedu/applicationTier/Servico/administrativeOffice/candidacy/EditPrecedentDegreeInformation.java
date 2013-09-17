/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.candidacy;


import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import pt.ist.fenixframework.Atomic;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class EditPrecedentDegreeInformation {

    @Atomic
    public static void run(PrecedentDegreeInformationBean precedentDegreeInformationBean) {
        precedentDegreeInformationBean.getPrecedentDegreeInformation().edit(precedentDegreeInformationBean);
    }

}