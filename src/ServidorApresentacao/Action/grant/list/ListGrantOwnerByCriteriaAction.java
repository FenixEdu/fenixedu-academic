/*
 * Created on Jun 27, 2004
 */
package ServidorApresentacao.Action.grant.list;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.grant.list.InfoListGrantOwnerComplete;
import DataBeans.grant.list.InfoSpanByCriteriaListGrantOwner;
import DataBeans.grant.list.InfoSpanListGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Pica
 * @author Barbosa
 */
public class ListGrantOwnerByCriteriaAction extends FenixDispatchAction {

    public ActionForward actionStart(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("select-criteria");
    }

    public ActionForward prepareListGrantOwnerByCriteria(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InfoSpanByCriteriaListGrantOwner infoSpanByCriteriaListGrantOwner = null;
        if (verifyParameterInRequest(request, "parameterInRequest")) {
            //The values are in the request

        } else {
            //The values are in the form
            DynaValidatorForm listForm = (DynaValidatorForm) form;

            InfoSpanListGrantOwner infoSpanListGrantOwner = populateInfoFromForm(listForm);
            if (infoSpanListGrantOwner.getSpanNumber().intValue() > infoSpanListGrantOwner
                    .getNumberOfSpans().intValue()) { return setError(request,
                    mapping, "errors.grant.list.invalidSpan",
                    "select-criteria-to-list", null); }
        }

        return listGrantOwner(mapping, request, form, response,
                infoSpanByCriteriaListGrantOwner);
    }

    private ActionForward listGrantOwner(ActionMapping mapping,
            HttpServletRequest request, ActionForm form,
            HttpServletResponse response,
            InfoSpanByCriteriaListGrantOwner infoSpanByCriteriaListGrantOwner) {

        try {
            IUserView userView = SessionUtils.getUserView(request);

            //Read the grant owners
            Object[] args = { infoSpanByCriteriaListGrantOwner};
            List listGrantOwners = (List) ServiceUtils.executeService(userView,
                    "ListGrantOwners", args);

            if (listGrantOwners != null && listGrantOwners.size() != 0) {
                if (listGrantOwners.size() > infoSpanByCriteriaListGrantOwner
                        .getNumberOfElementsInSpan().intValue()) {
                    infoSpanByCriteriaListGrantOwner = (InfoSpanListGrantOwner) listGrantOwners
                            .get(listGrantOwners.size() - 1);
                    listGrantOwners.remove(listGrantOwners.size() - 1);
                }

                //Setting the request
                DynaValidatorForm listForm = (DynaValidatorForm) form;
                setForm(listForm, infoSpanByCriteriaListGrantOwner);
                request.setAttribute("listGrantOwner", listGrantOwners);
                request.setAttribute("spanNumber",
                        infoSpanByCriteriaListGrantOwner.getSpanNumber());
                request.setAttribute("numberOfElementsInSpan",
                        infoSpanByCriteriaListGrantOwner
                                .getNumberOfElementsInSpan());
                request.setAttribute("orderBy",
                        infoSpanByCriteriaListGrantOwner.getOrderBy());
                request.setAttribute("totalElements",
                        infoSpanByCriteriaListGrantOwner.getTotalElements());

                if (infoSpanByCriteriaListGrantOwner.hasBeforeSpan()) {
                    request.setAttribute("beforeSpan",
                            infoSpanByCriteriaListGrantOwner.getBeforeSpan());
                }
                if (infoSpanByCriteriaListGrantOwner.hasAfterSpan()) {
                    request.setAttribute("afterSpan",
                            infoSpanByCriteriaListGrantOwner.getAfterSpan());
                }
                request.setAttribute("numberOfSpans",
                        infoSpanByCriteriaListGrantOwner.getNumberOfSpans());
            } else {
                throw new Exception();
            }
        } catch (FenixServiceException e) {
            return setError(request, mapping, "errors.grant.unrecoverable",
                    "select-criteria-to-list", null);

        } catch (Exception e) {
            return setError(request, mapping, "errors.grant.unrecoverable",
                    "select-criteria-to-list", null);
        }

        return mapping.findForward("select-criteria-to-list");
    }

    public ActionForward showGrantOwner(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Integer grantOwnerId = null;
        if (verifyParameterInRequest(request, "grantOwnerId")) {
            grantOwnerId = new Integer(request.getParameter("grantOwnerId"));
        }

        IUserView userView = SessionUtils.getUserView(request);

        if (grantOwnerId != null) {
            try {
                //Read all the information about the grant owner
                Object[] args = { grantOwnerId};
                InfoListGrantOwnerComplete listGrantOwnerCompleteInfo = (InfoListGrantOwnerComplete) ServiceUtils
                        .executeService(userView, "ShowGrantOwner", args);

                if (listGrantOwnerCompleteInfo != null) {
                    //Set the request
                    request.setAttribute("infoGrantOwner",
                            listGrantOwnerCompleteInfo.getInfoGrantOwner());
                    request.setAttribute("infoQualificationList",
                            listGrantOwnerCompleteInfo.getInfoQualifications());
                    request.setAttribute("infoListGrantContractList",
                            listGrantOwnerCompleteInfo
                                    .getInfoListGrantContracts());
                }
            } catch (FenixServiceException e) {
                return setError(request, mapping, "errors.grant.unrecoverable",
                        "show-grant-owner", null);
            } catch (Exception e) {
                return setError(request, mapping, "errors.grant.unrecoverable",
                        "show-grant-owner", null);
            }
        } else {
            return setError(request, mapping, "errors.grant.unrecoverable",
                    "show-grant-owner", null);
        }
        return mapping.findForward("show-grant-owner");
    }

    private void setForm(DynaValidatorForm form,
            InfoSpanListGrantOwner infoSpanListGrantOwner) throws Exception {
        form.set("spanNumber", infoSpanListGrantOwner.getSpanNumber());
        form.set("numberOfElementsInSpan", infoSpanListGrantOwner
                .getNumberOfElementsInSpan());
        form.set("totalElements", infoSpanListGrantOwner.getTotalElements());
        form.set("orderBy", infoSpanListGrantOwner.getOrderBy());

    }

    /*
     * <form-property name="spanNumber" type="java.lang.Integer"/>
     * <form-property name="numberOfElementsInSpan" type="java.lang.Integer"/>
     * <form-property name="totalElements" type="java.lang.Integer"/>
     * <form-property name="orderBy" type="java.lang.String"/> <form-property
     * name="ascendingOrder" type="java.lang.Boolean"/>
     * 
     * <form-property name="filterType" type="java.lang.Integer"/>
     * <form-property name="beginContract" type="java.lang.String"/>
     * <form-property name="endContract" type="java.lang.String"/>
     */

    private InfoSpanListGrantOwner populateInfoFromForm(DynaValidatorForm form)
            throws Exception {
        InfoSpanByCriteriaListGrantOwner infoSpanByCriteriaListGrantOwner = new InfoSpanByCriteriaListGrantOwner();

        infoSpanByCriteriaListGrantOwner.setNumberOfElementsInSpan((Integer) form.get("numberOfElementsInSpan"));
        infoSpanByCriteriaListGrantOwner.setSpanNumber((Integer) form.get("spanNumber"));
        infoSpanByCriteriaListGrantOwner.setTotalElements((Integer) form.get("totalElements"));
        infoSpanByCriteriaListGrantOwner.setOrderBy((String) form.get("orderBy"));

        infoSpanByCriteriaListGrantOwner.setJustActiveContract(new Boolean(false));
        infoSpanByCriteriaListGrantOwner.setJustDesactiveContract(new Boolean(false));
        Integer filter = (Integer) form.get("filterType");
        if(filter.equals(new Integer(2))) {
            infoSpanByCriteriaListGrantOwner.setJustActiveContract(new Boolean(true));
        } else if (filter.equals(new Integer(3))) {
            infoSpanByCriteriaListGrantOwner.setJustDesactiveContract(new Boolean(true));
        }
        
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (verifyStringParameterInForm(form, "beginContract")) {
            infoSpanByCriteriaListGrantOwner.setBeginContract(sdf.parse((String) form.get("beginContract")));
        }
        if (verifyStringParameterInForm(form, "endContract")) {
            infoSpanByCriteriaListGrantOwner.setBeginContract(sdf.parse((String) form.get("endContract")));
        }

        return infoSpanByCriteriaListGrantOwner;
    }

}
