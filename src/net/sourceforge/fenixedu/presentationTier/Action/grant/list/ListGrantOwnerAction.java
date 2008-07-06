/*
 * Created on Jun 21, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.grant.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantOwnerByOrder;
import net.sourceforge.fenixedu.dataTransferObject.grant.list.InfoListGrantOwnerComplete;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantOwnerAction extends FenixDispatchAction {

    private static final String ORDER_PARAMETER = "orderBy";
    private static final String DEFAULT_ORDER_GETTER = "grantOwnerNumber";
    private static final String ORDER_MARKER = "=";

    public ActionForward listGrantOwners(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final String orderParameter = request.getParameter(ORDER_PARAMETER);
	final String orderGetter = StringUtils.isEmpty(orderParameter) ? DEFAULT_ORDER_GETTER : orderParameter.substring(0,
		orderParameter.indexOf(ORDER_MARKER));

	final List<InfoListGrantOwnerByOrder> grantOwners = getInfoListGrantOwnerByOrder();
	Collections.sort(grantOwners, new BeanComparator(orderGetter));
	request.setAttribute("listGrantOwners", grantOwners);

	final CollectionPager<InfoListGrantOwnerByOrder> pager = new CollectionPager<InfoListGrantOwnerByOrder>(grantOwners, 100);
	request.setAttribute("collectionPager", pager);
	request.setAttribute("numberOfPages", Integer.valueOf(pager.getNumberOfPages()));

	final String pageParameter = request.getParameter("pageNumber");
	final Integer page = StringUtils.isEmpty(pageParameter) ? Integer.valueOf(1) : Integer.valueOf(pageParameter);
	request.setAttribute("pageNumber", page);
	request.setAttribute("resultPage", pager.getPage(page));

	return mapping.findForward("list-grant-owner");
    }

    private List<InfoListGrantOwnerByOrder> getInfoListGrantOwnerByOrder() {
	final List<InfoListGrantOwnerByOrder> result = new ArrayList<InfoListGrantOwnerByOrder>();

	for (final GrantOwner grantOwner : rootDomainObject.getGrantOwnersSet()) {
	    if (grantOwner.hasPerson()) {
		result.add(new InfoListGrantOwnerByOrder(grantOwner));
	    }
	}

	return result;
    }

    public ActionForward showGrantOwner(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer grantOwnerId = null;
	if (verifyParameterInRequest(request, "grantOwnerId")) {
	    grantOwnerId = new Integer(request.getParameter("grantOwnerId"));
	}

	if (grantOwnerId != null) {
	    // Read all the information about the grant owner
	    Object[] args = { grantOwnerId };
	    InfoListGrantOwnerComplete listGrantOwnerCompleteInfo = (InfoListGrantOwnerComplete) ServiceUtils.executeService(
		    "ShowGrantOwner", args);

	    if (listGrantOwnerCompleteInfo != null) {
		// Set the request
		if (listGrantOwnerCompleteInfo.getInfoGrantOwner() != null) {
		    request.setAttribute("infoGrantOwner", listGrantOwnerCompleteInfo.getInfoGrantOwner());
		} else {
		    throw new FenixServiceException();
		}
		if (listGrantOwnerCompleteInfo.getInfoQualifications() != null
			&& !listGrantOwnerCompleteInfo.getInfoQualifications().isEmpty()) {
		    request.setAttribute("infoQualificationList", listGrantOwnerCompleteInfo.getInfoQualifications());
		}
		if (listGrantOwnerCompleteInfo.getInfoListGrantContracts() != null
			&& !listGrantOwnerCompleteInfo.getInfoListGrantContracts().isEmpty()) {
		    request.setAttribute("infoListGrantContractList", listGrantOwnerCompleteInfo.getInfoListGrantContracts());
		}
	    }
	} else {
	    return setError(request, mapping, "errors.grant.unrecoverable", "show-grant-owner", null);
	}
	return mapping.findForward("show-grant-owner");
    }

}
