/*
 * Created on 11/Dez/2004
 */
package ServidorApresentacao.Action.managementAssiduousness;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoEmployee;
import DataBeans.managementAssiduousness.InfoExtraWorkRequests;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Tânia Pousão
 * 
 */
public class RequestsExtraWorkAction extends FenixDispatchAction {
    public ActionForward prepareRequests(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (request.getAttribute("infoExtraWorkRequestsList") == null
                || (request.getAttribute("infoExtraWorkRequestsList") != null 
                        && ((List)request.getAttribute("infoExtraWorkRequestsList")).size() <= 0 )) {
            List infoExtraWorkRequestsList = new ArrayList();
            for (int i = 0; i < 10; i++) {
                InfoExtraWorkRequests infoExtraWorkRequests = new InfoExtraWorkRequests();
                infoExtraWorkRequests.setInfoEmployee(new InfoEmployee());
                infoExtraWorkRequestsList.add(infoExtraWorkRequests);
            }
            request.setAttribute("infoExtraWorkRequestsList",
                    infoExtraWorkRequestsList);
        }

        return mapping.findForward("inputResquets");
    }

    public ActionForward requestsExtraWork(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        try {
            ActionErrors actionErrors = new ActionErrors();
            HttpSession session = request.getSession();

            IUserView userView = SessionUtils.getUserView(request);
            String usernameWhoKey = userView.getUtilizador();

            DynaActionForm requestsExtraWorkFormBean = (DynaActionForm) form;
            String beginDateForm = (String) requestsExtraWorkFormBean
                    .get("beginDate");
            String endDateForm = (String) requestsExtraWorkFormBean
                    .get("endDate");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy");
            simpleDateFormat.setLenient(false);
            Date beginDate = simpleDateFormat.parse(beginDateForm);
            Date endDate = simpleDateFormat.parse(endDateForm);

            String costCenterCode = (String) requestsExtraWorkFormBean
                    .get("cc");
            String costCenterCodeMoney = (String) requestsExtraWorkFormBean
                    .get("ccMoney");

            List infoExtraWorkRequestsList = null;
            if (requestsExtraWorkFormBean.get("size") != null) {
                Integer sizeList = (Integer) requestsExtraWorkFormBean
                        .get("size");

                infoExtraWorkRequestsList = new ArrayList();
                for (int i = 0; i < sizeList.intValue(); i++) {
                    InfoExtraWorkRequests infoExtraWorkRequests = getExtraWorkRequests(
                            request, i);
                    if (infoExtraWorkRequests != null) {
                        infoExtraWorkRequests.setBeginDate(beginDate);
                        infoExtraWorkRequests.setEndDate(endDate);
                        infoExtraWorkRequestsList.add(infoExtraWorkRequests);
                    }
                }
            }

            List infoExtraWorkRequestsListAfterWrite = null;
            Object[] args = { usernameWhoKey, infoExtraWorkRequestsList,
                    costCenterCode, costCenterCodeMoney };
            try {
                infoExtraWorkRequestsListAfterWrite = (List) ServiceManagerServiceFactory
                        .executeService(userView, "WriteExtraWorkRequests",
                                args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
                actionErrors.add("error.extra.work.requests", new ActionError(
                        "error.impossivel.extraWork.requests.write"));
                saveErrors(request, actionErrors);

                return mapping.getInputForward();
            }
            if (infoExtraWorkRequestsListAfterWrite == null
                    || infoExtraWorkRequestsListAfterWrite.size() <= 0) {
                actionErrors.add("error.extra.work.requests", new ActionError(
                        "error.impossivel.extraWork.requests.write"));
                saveErrors(request, actionErrors);

                return mapping.getInputForward();
            }

            request.setAttribute("infoExtraWorkRequestsList",
                    infoExtraWorkRequestsListAfterWrite);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (mapping.findForward("requestsExtraWorkConfirmation"));
    }

    public ActionForward readAndDeleteRequests(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ActionErrors actionErrors = new ActionErrors();
        HttpSession session = request.getSession();
        DynaActionForm requestsExtraWorkFormBean = (DynaActionForm) form;

        try {

            IUserView userView = SessionUtils.getUserView(request);
            String usernameWhoKey = userView.getUtilizador();

            List infoExtraWorkRequestsList = null;
            if (requestsExtraWorkFormBean.get("size") != null) {
                Integer sizeList = (Integer) requestsExtraWorkFormBean
                        .get("size");

                infoExtraWorkRequestsList = new ArrayList();
                for (int i = 0; i < sizeList.intValue(); i++) {
                    InfoExtraWorkRequests infoExtraWorkRequests = getIDExtraWorkRequests(
                            request, i);
                    if (infoExtraWorkRequests != null) {
                        infoExtraWorkRequestsList.add(infoExtraWorkRequests);
                    }
                }
            }

            List infoExtraWorkRequestsListAfter = null;
            Object[] args = { usernameWhoKey, infoExtraWorkRequestsList };
            try {
                infoExtraWorkRequestsListAfter = (List) ServiceManagerServiceFactory
                        .executeService(userView,
                                "ReadAndDeleteExtraWorkRequests", args);
            } catch (FenixServiceException e) {
                e.printStackTrace();
                actionErrors.add("error.extra.work.requests", new ActionError(
                        "error.impossivel.extraWork.requests.write"));
                saveErrors(request, actionErrors);

                return mapping.getInputForward();
            }
            if (infoExtraWorkRequestsListAfter != null
                    && infoExtraWorkRequestsListAfter.size() > 0) {
                request.setAttribute("infoExtraWorkRequestsList",
                        infoExtraWorkRequestsListAfter);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                        "dd/MM/yyyy");
                simpleDateFormat.setLenient(false);

                Date beginDate = ((InfoExtraWorkRequests) infoExtraWorkRequestsListAfter
                        .get(0)).getBeginDate();
                if (beginDate != null) {
                    requestsExtraWorkFormBean.set("beginDate", simpleDateFormat
                            .format(beginDate));
                }

                Date endDate = ((InfoExtraWorkRequests) infoExtraWorkRequestsListAfter
                        .get(0)).getEndDate();
                if (endDate != null) {
                    requestsExtraWorkFormBean.set("endDate", simpleDateFormat
                            .format(endDate));
                }

                requestsExtraWorkFormBean.set("cc",
                        ((InfoExtraWorkRequests) infoExtraWorkRequestsListAfter
                                .get(0)).getInfoCostCenterExtraWork().getCode());
                requestsExtraWorkFormBean.set("ccMoney",
                        ((InfoExtraWorkRequests) infoExtraWorkRequestsListAfter
                                .get(0)).getInfoCostCenterMoney().getCode());
            } else {
                return prepareRequests(mapping, requestsExtraWorkFormBean, request,
    	                response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        String todo = (String) requestsExtraWorkFormBean.get("todo");
        if(todo != null && todo.length() > 0 && todo.equals("editar")) {
	        return prepareRequests(mapping, requestsExtraWorkFormBean, request,
	                response);
        } 
        
        return (mapping.findForward("requestsExtraWorkConfirmation"));        
    }

    private InfoExtraWorkRequests getExtraWorkRequests(
            HttpServletRequest request, int index) {
        try {
            Integer employeeNumber = null;
            Integer idInternal = null;
            Boolean option1 = new Boolean(false);
            Boolean option2 = new Boolean(false);
            Boolean option3 = new Boolean(false);
            Boolean option4 = new Boolean(false);
            Boolean option5 = new Boolean(false);
            Boolean option6 = new Boolean(false);
            Boolean option7 = new Boolean(false);

            if (request.getParameter("infoExtraWorkRequests[" + index
                    + "].infoEmployee.employeeNumber") != null
                    && request.getParameter(
                            "infoExtraWorkRequests[" + index
                                    + "].infoEmployee.employeeNumber").length() > 0) {
                employeeNumber = Integer.valueOf(request
                        .getParameter("infoExtraWorkRequests[" + index
                                + "].infoEmployee.employeeNumber"));

                if (request.getParameter("infoExtraWorkRequests[" + index
                        + "].idInternal") != null
                        && request.getParameter(
                                "infoExtraWorkRequests[" + index
                                        + "].idInternal").length() > 0) {
                    idInternal = Integer.valueOf(request
                            .getParameter("infoExtraWorkRequests[" + index
                                    + "].idInternal"));
                }
                if (request.getParameter("infoExtraWorkRequests[" + index
                        + "].option1") != null) {
                    option1 = Boolean.valueOf(request
                            .getParameter("infoExtraWorkRequests[" + index
                                    + "].option1"));
                }
                if (request.getParameter("infoExtraWorkRequests[" + index
                        + "].option2") != null) {
                    option2 = Boolean.valueOf(request
                            .getParameter("infoExtraWorkRequests[" + index
                                    + "].option2"));
                }
                if (request.getParameter("infoExtraWorkRequests[" + index
                        + "].option3") != null) {
                    option3 = Boolean.valueOf(request
                            .getParameter("infoExtraWorkRequests[" + index
                                    + "].option3"));
                }
                if (request.getParameter("infoExtraWorkRequests[" + index
                        + "].option4") != null) {
                    option4 = Boolean.valueOf(request
                            .getParameter("infoExtraWorkRequests[" + index
                                    + "].option4"));
                }
                if (request.getParameter("infoExtraWorkRequests[" + index
                        + "].option5") != null) {
                    option5 = Boolean.valueOf(request
                            .getParameter("infoExtraWorkRequests[" + index
                                    + "].option5"));
                }
                if (request.getParameter("infoExtraWorkRequests[" + index
                        + "].option6") != null) {
                    option6 = Boolean.valueOf(request
                            .getParameter("infoExtraWorkRequests[" + index
                                    + "].option6"));
                }
                if (request.getParameter("infoExtraWorkRequests[" + index
                        + "].option7") != null) {
                    option7 = Boolean.valueOf(request
                            .getParameter("infoExtraWorkRequests[" + index
                                    + "].option7"));
                }
            }

            if (employeeNumber != null) {
                InfoExtraWorkRequests extraWorkRequests = new InfoExtraWorkRequests();
                extraWorkRequests.setIdInternal(idInternal);
                extraWorkRequests.setInfoEmployee(new InfoEmployee());
                extraWorkRequests.getInfoEmployee().setEmployeeNumber(
                        employeeNumber);
                extraWorkRequests.setOption1(option1);
                extraWorkRequests.setOption2(option2);
                extraWorkRequests.setOption3(option3);
                extraWorkRequests.setOption4(option4);
                extraWorkRequests.setOption5(option5);
                extraWorkRequests.setOption6(option6);
                extraWorkRequests.setOption7(option7);

                return extraWorkRequests;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private InfoExtraWorkRequests getIDExtraWorkRequests(
            HttpServletRequest request, int index) {
        try {
            Integer idInternal = null;
            Boolean forDelete = new Boolean(false);

            if (request.getParameter("infoExtraWorkRequests[" + index
                    + "].idInternal") != null
                    && request.getParameter(
                            "infoExtraWorkRequests[" + index + "].idInternal")
                            .length() > 0) {
                idInternal = Integer.valueOf(request
                        .getParameter("infoExtraWorkRequests[" + index
                                + "].idInternal"));

                if (request.getParameter("infoExtraWorkRequests[" + index
                        + "].forDelete") != null) {
                    forDelete = Boolean.valueOf(request
                            .getParameter("infoExtraWorkRequests[" + index
                                    + "].forDelete"));
                }
            }

            if (idInternal != null) {
                InfoExtraWorkRequests infoExtraWorkRequests = new InfoExtraWorkRequests();

                infoExtraWorkRequests.setIdInternal(idInternal);
                infoExtraWorkRequests.setForDelete(forDelete);

                return infoExtraWorkRequests;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
