/*
 * Created on 24/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuide;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */

public class ReadReimbursementGuidesByGuide {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List run(Integer guideId) {
        List<InfoReimbursementGuide> result = new ArrayList<InfoReimbursementGuide>();

        Guide guide = RootDomainObject.getInstance().readGuideByOID(guideId);
        for (ReimbursementGuide reimbursementGuide : guide.getReimbursementGuides()) {
            result.add(InfoReimbursementGuide.newInfoFromDomain(reimbursementGuide));
        }

        return result;
    }

}