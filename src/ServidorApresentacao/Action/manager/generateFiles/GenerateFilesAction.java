/*
 * Created on 27/Jan/2004
 *  
 */
package ServidorApresentacao.Action.manager.generateFiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoExecutionYear;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.gratuity.masterDegree.FileNotCreatedServiceException;
import ServidorAplicacao.Servico.exceptions.gratuity.masterDegree.InsufficientSibsPaymentPhaseCodesServiceException;
import ServidorAplicacao.Servico.exceptions.gratuity.masterDegree.InsuranceNotDefinedServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Tânia Pousão
 *  
 */
public class GenerateFilesAction extends FenixDispatchAction {
    public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("firstPage");
    }

    public ActionForward prepareChooseForGenerateFiles(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        String file = request.getParameter("file");
        request.setAttribute("file", file);

        //execution years
        List executionYears = null;
        Object[] args = {};
        try {
            executionYears = (List) ServiceManagerServiceFactory.executeService(null,
                    "ReadNotClosedExecutionYears", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        if (executionYears != null && !executionYears.isEmpty()) {
            ComparatorChain comparator = new ComparatorChain();
            comparator.addComparator(new BeanComparator("year"), true);
            Collections.sort(executionYears, comparator);

            List executionYearLabels = buildLabelValueBeanForJsp(executionYears);
            request.setAttribute("executionYears", executionYearLabels);
        }
        return mapping.findForward("chooseForGenerateFiles");
    }

    private List buildLabelValueBeanForJsp(List infoExecutionYears) {
        List executionYearLabels = new ArrayList();
        CollectionUtils.collect(infoExecutionYears, new Transformer() {
            public Object transform(Object arg0) {
                InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

                LabelValueBean executionYear = new LabelValueBean(infoExecutionYear.getYear(),
                        infoExecutionYear.getYear());
                return executionYear;
            }
        }, executionYearLabels);
        return executionYearLabels;
    }

    public ActionForward generateGratuityFile(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        ActionErrors errors = new ActionErrors();

        String fileType = request.getParameter("file");
        request.setAttribute("file", fileType);

        String executionYear = request.getParameter("executionYear");
        request.setAttribute("executionYear", executionYear);

        Object[] argsExecutionYear = { executionYear };
        InfoExecutionYear infoExecutionYear = null;
        try {
            infoExecutionYear = (InfoExecutionYear) ServiceManagerServiceFactory.executeService(
                    userView, "ReadExecutionYear", argsExecutionYear);

        } catch (FenixServiceException e) {
            throw new FenixActionException();
        }

        if (infoExecutionYear == null) {
            throw new FenixActionException();
        }

        Object[] args = { infoExecutionYear.getIdInternal() };
        String serviceName = null;

        //Create respective file
        if (fileType.equals(new String("sibs"))) {
            serviceName = "GenerateOutgoingSibsPaymentFileByExecutionYearID";

        } else if (fileType.equals(new String("letters"))) {
            serviceName = "GeneratePaymentLettersFileByExecutionYearID";
        }

        try {
            ServiceManagerServiceFactory.executeService(userView, serviceName, args);

        } catch (InsufficientSibsPaymentPhaseCodesServiceException exception) {
            errors.add("noList", new ActionError("error.generateFiles.invalidBind", exception
                    .getMessage()));
            saveErrors(request, errors);
            return mapping.getInputForward();
        } catch (InsuranceNotDefinedServiceException exception) {
            errors.add("noList", new ActionError(exception.getMessage()));
            saveErrors(request, errors);
            return mapping.getInputForward();
        } catch (FileNotCreatedServiceException exception) {
            errors.add("noList", new ActionError(exception.getMessage()));
            saveErrors(request, errors);
            return mapping.getInputForward();
        } catch (FenixServiceException exception) {
            exception.printStackTrace();
            errors.add("noList", new ActionError("error.generateFiles.emptyList"));
            saveErrors(request, errors);
            return mapping.getInputForward();
        }

        return mapping.findForward("confirmation");
    }
}