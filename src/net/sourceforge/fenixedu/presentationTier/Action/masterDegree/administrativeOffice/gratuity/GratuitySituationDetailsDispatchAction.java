package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoPaymentTransaction;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoTransaction;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class GratuitySituationDetailsDispatchAction extends FenixDispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        String gratuitySituationId = getFromRequest("gratuitySituationId", request);
        String studentId = getFromRequest("studentId", request);

        DynaActionForm createGuideFromTransactionsForm = (DynaActionForm) form;
        createGuideFromTransactionsForm.set("gratuitySituationId", new Integer(gratuitySituationId));
        createGuideFromTransactionsForm.set("studentId", new Integer(studentId));

        //Read Registration
        InfoStudent infoStudent = null;
        Object argsStudent[] = { new Integer(studentId) };
        try {
            infoStudent = (InfoStudent) ServiceUtils.executeService(userView, "student.ReadStudentById",
                    argsStudent);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoStudent == null) {
            throw new FenixActionException("error.exception.masterDegree.nonExistentStudent");
        }
        request.setAttribute(SessionConstants.STUDENT, infoStudent);

        //Read Gratuity Situation
        InfoGratuitySituation infoGratuitySituation = null;
        Object argsGratuitySituation[] = { new Integer(gratuitySituationId) };
        try {
            infoGratuitySituation = (InfoGratuitySituation) ServiceUtils.executeService(userView,
                    "ReadGratuitySituationById", argsGratuitySituation);

        } catch (ExcepcaoInexistente e) {
            throw new FenixActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute(SessionConstants.GRATUITY_SITUATION, infoGratuitySituation);

        //Read Transactions
        List infoTransactions = null;
        Object argsTransactions[] = { infoGratuitySituation.getIdInternal() };

        try {
            infoTransactions = (List) ServiceUtils.executeService(userView,
                    "ReadAllTransactionsByGratuitySituationID", argsTransactions);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute(SessionConstants.TRANSACTION_LIST, infoTransactions);

        Iterator it = infoTransactions.iterator();
        InfoTransaction infoTransaction = null;
        List transactionsWithoutGuideList = new ArrayList();
        while (it.hasNext()) {
            infoTransaction = (InfoTransaction) it.next();
            if (infoTransaction instanceof InfoPaymentTransaction) {
                InfoPaymentTransaction infoPaymentTransaction = (InfoPaymentTransaction) infoTransaction;
                if (infoPaymentTransaction.getInfoGuideEntry() == null) {
                    transactionsWithoutGuideList.add(infoPaymentTransaction.getIdInternal());
                } else {
                    transactionsWithoutGuideList.add(null);
                }
            } else {
                transactionsWithoutGuideList.add(null);
            }
        }
        request.setAttribute(SessionConstants.TRANSACTIONS_WITHOUT_GUIDE_LIST,
                transactionsWithoutGuideList);

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