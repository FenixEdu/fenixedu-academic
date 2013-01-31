/*
 * Created on 27/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.generateFiles;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.FileNotCreatedServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.InsufficientSibsPaymentPhaseCodesServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.InsuranceNotDefinedServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.gratuity.GenerateOutgoingSibsPaymentFileByExecutionYearID;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.gratuity.GeneratePaymentLettersFileByExecutionYearID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Tânia Pousão
 * 
 */
@Mapping(
		module = "manager",
		path = "/generateFiles",
		input = "/generateFiles.do?method=prepareChooseForGenerateFiles&page=0",
		attribute = "chooseForGenerateFilesForm",
		formBean = "chooseForGenerateFilesForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "confirmation", path = "/manager/generateFiles/confirmation.jsp"),
		@Forward(name = "chooseForGenerateFiles", path = "/manager/generateFiles/chooseForGenerateFiles.jsp"),
		@Forward(name = "firstPage", path = "/manager/generateFiles/welcomeScreen.jsp") })
public class GenerateFilesAction extends FenixDispatchAction {

	private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public ActionForward firstPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("firstPage");
	}

	public ActionForward prepareChooseForGenerateFiles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException {
		String file = request.getParameter("file");
		request.setAttribute("file", file);

		// execution years
		List executionYears = null;

		executionYears = ReadNotClosedExecutionYears.run();

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
			@Override
			public Object transform(Object arg0) {
				InfoExecutionYear infoExecutionYear = (InfoExecutionYear) arg0;

				LabelValueBean executionYear = new LabelValueBean(infoExecutionYear.getYear(), infoExecutionYear.getYear());
				return executionYear;
			}
		}, executionYearLabels);
		return executionYearLabels;
	}

	public ActionForward generateGratuityFile(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IUserView userView = getUserView(request);

		String fileType = request.getParameter("file");
		request.setAttribute("file", fileType);

		String executionYear = request.getParameter("executionYear");
		request.setAttribute("executionYear", executionYear);

		InfoExecutionYear infoExecutionYear = null;
		infoExecutionYear = ReadExecutionYear.run(executionYear);

		if (infoExecutionYear == null) {
			throw new FenixActionException();
		}

		Date paymentEndDate = dateFormat.parse(((DynaActionForm) actionForm).getString("paymentEndDate"));
		byte[] generatedFile = null;
		try {
			// Create respective file
			if (fileType.equals("sibs")) {
				generatedFile =
						GenerateOutgoingSibsPaymentFileByExecutionYearID.run(infoExecutionYear.getIdInternal(), paymentEndDate);
			} else if (fileType.equals("letters")) {
				generatedFile =
						GeneratePaymentLettersFileByExecutionYearID.run(infoExecutionYear.getIdInternal(), paymentEndDate);
			}
		} catch (InsufficientSibsPaymentPhaseCodesServiceException exception) {
			addErrorMessage(request, "noList", "error.generateFiles.invalidBind", exception.getMessage());
			return mapping.getInputForward();

		} catch (InsuranceNotDefinedServiceException exception) {
			addErrorMessage(request, "noList", exception.getMessage());
			return mapping.getInputForward();

		} catch (FileNotCreatedServiceException exception) {
			addErrorMessage(request, "noList", exception.getMessage());
			return mapping.getInputForward();

		} catch (FenixServiceException exception) {
			exception.printStackTrace();
			addErrorMessage(request, "noList", "error.generateFiles.emptyList");
			return mapping.getInputForward();
		}

		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition",
				"attachment; filename=" + fileType + infoExecutionYear.getYear().replace("/", "-") + ".txt");
		ServletOutputStream writer = response.getOutputStream();

		writer.write(generatedFile);
		writer.flush();
		response.flushBuffer();

		return null;
	}
}