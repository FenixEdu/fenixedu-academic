package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.ReadGratuitySituationById;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions.ReadAllTransactionsByGratuitySituationID;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentById;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoPaymentTransaction;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoTransaction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/gratuitySituationDetails",
        attribute = "createGuideFromTransactionsForm", formBean = "createGuideFromTransactionsForm", scope = "request",
        parameter = "method")
@Forwards(
        value = { @Forward(name = "showDetails", path = "gratuitySituationDetails", tileProperties = @Tile(title = "teste49")) })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class GratuitySituationDetailsDispatchAction extends FenixDispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        IUserView userView = UserView.getUser();

        String gratuitySituationId = getFromRequest("gratuitySituationId", request);
        String studentId = getFromRequest("studentId", request);

        DynaActionForm createGuideFromTransactionsForm = (DynaActionForm) form;
        createGuideFromTransactionsForm.set("gratuitySituationId", new Integer(gratuitySituationId));
        createGuideFromTransactionsForm.set("studentId", new Integer(studentId));

        // Read Registration
        InfoStudent infoStudent = null;

        try {
            infoStudent = (InfoStudent) ReadStudentById.run(new Integer(studentId));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoStudent == null) {
            throw new FenixActionException("error.exception.masterDegree.nonExistentStudent");
        }
        request.setAttribute(PresentationConstants.STUDENT, infoStudent);

        // Read Gratuity Situation
        InfoGratuitySituation infoGratuitySituation = null;

        try {
            infoGratuitySituation = ReadGratuitySituationById.run(new Integer(gratuitySituationId));

        } catch (ExcepcaoInexistente e) {
            throw new FenixActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute(PresentationConstants.GRATUITY_SITUATION, infoGratuitySituation);

        // Read Transactions
        List infoTransactions = null;

        try {
            infoTransactions = ReadAllTransactionsByGratuitySituationID.run(infoGratuitySituation.getExternalId());

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute(PresentationConstants.TRANSACTION_LIST, infoTransactions);

        Iterator it = infoTransactions.iterator();
        InfoTransaction infoTransaction = null;
        List transactionsWithoutGuideList = new ArrayList();
        while (it.hasNext()) {
            infoTransaction = (InfoTransaction) it.next();
            if (infoTransaction instanceof InfoPaymentTransaction) {
                InfoPaymentTransaction infoPaymentTransaction = (InfoPaymentTransaction) infoTransaction;
                if (infoPaymentTransaction.getInfoGuideEntry() == null) {
                    transactionsWithoutGuideList.add(infoPaymentTransaction.getExternalId());
                } else {
                    transactionsWithoutGuideList.add(null);
                }
            } else {
                transactionsWithoutGuideList.add(null);
            }
        }
        request.setAttribute(PresentationConstants.TRANSACTIONS_WITHOUT_GUIDE_LIST, transactionsWithoutGuideList);

        return mapping.findForward("showDetails");

    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }
}