/*
 * Created on Jun 26, 2006
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.payments;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class ReceiptsManagementDispatchAction extends PaymentsManagementDispatchAction {



   

    private Person getPerson(HttpServletRequest request) {
        return (Person) rootDomainObject
                .readPartyByOID(getRequestParameterAsInteger(request, "personId"));
    }

}
