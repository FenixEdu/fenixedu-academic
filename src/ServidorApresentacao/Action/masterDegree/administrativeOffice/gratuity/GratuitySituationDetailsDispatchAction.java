package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoGratuitySituation;
import DataBeans.InfoStudent;
import DataBeans.transactions.InfoPaymentTransaction;
import DataBeans.transactions.InfoTransaction;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.ExcepcaoInexistente;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.masterDegree.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

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

        //Read Student
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