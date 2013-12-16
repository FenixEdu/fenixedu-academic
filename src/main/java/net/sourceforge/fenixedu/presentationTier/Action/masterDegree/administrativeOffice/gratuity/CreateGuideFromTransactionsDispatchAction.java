package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.contributor.ReadContributor;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.ReadGratuitySituationById;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.gratuity.transactions.ReadAllTransactionsByGratuitySituationID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.CreateGuideFromTransactions;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentById;
import net.sourceforge.fenixedu.dataTransferObject.InfoContributor;
import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoPaymentTransaction;
import net.sourceforge.fenixedu.dataTransferObject.transactions.InfoTransaction;
import net.sourceforge.fenixedu.domain.GuideState;
import net.sourceforge.fenixedu.domain.masterDegree.GuideRequester;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.bennu.core.security.Authenticate;
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
@Mapping(module = "masterDegreeAdministrativeOffice", path = "/createGuideFromTransactions",
        input = "/createGuideFromTransactionsForm.do?method=chooseContributor&page=0",
        attribute = "createGuideFromTransactionsForm", formBean = "createGuideFromTransactionsForm", scope = "request",
        parameter = "method")
@Forwards(value = {
        @Forward(name = "createSuccess", path = "createGuideFromTransactionsSuccess", tileProperties = @Tile(title = "teste41")),
        @Forward(name = "confirmCreate", path = "confirmCreateGuideFromTransactions", tileProperties = @Tile(title = "teste42")),
        @Forward(name = "chooseContributor", path = "chooseContributorForCreateGuideFromTransactions", tileProperties = @Tile(
                title = "teste43")) })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class CreateGuideFromTransactionsDispatchAction extends FenixDispatchAction {

    public ActionForward chooseContributor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("chooseContributor");

    }

    public ActionForward confirmCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm createGuideFromTransactionsForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();

        Integer contributorNumber = (Integer) createGuideFromTransactionsForm.get("contributorNumber");
        String gratuitySituationId = (String) createGuideFromTransactionsForm.get("gratuitySituationId");
        String studentId = (String) createGuideFromTransactionsForm.get("studentId");
        // Read Contributor
        InfoContributor infoContributor = readContributor(mapping, userView, contributorNumber);
        request.setAttribute(PresentationConstants.CONTRIBUTOR, infoContributor);

        // Read Registration
        InfoStudent infoStudent = readStudent(mapping, userView, studentId);
        request.setAttribute(PresentationConstants.STUDENT, infoStudent);

        // Read Transactions
        List infoTransactions = null;

        try {
            infoTransactions = ReadAllTransactionsByGratuitySituationID.run(gratuitySituationId);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        // Remove Transactions with Guide
        Iterator it = infoTransactions.iterator();
        InfoTransaction infoTransaction = null;
        List infoTransactionsWithoutGuides = new ArrayList();
        while (it.hasNext()) {
            infoTransaction = (InfoTransaction) it.next();
            if (infoTransaction instanceof InfoPaymentTransaction) {
                InfoPaymentTransaction infoPaymentTransaction = (InfoPaymentTransaction) infoTransaction;
                if (infoPaymentTransaction.getInfoGuideEntry() == null) {
                    infoTransactionsWithoutGuides.add(infoTransaction);
                }
            }
        }

        request.setAttribute(PresentationConstants.TRANSACTION_LIST, infoTransactionsWithoutGuides);

        return mapping.findForward("confirmCreate");

    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm createGuideFromTransactionsForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();

        Integer contributorNumber = (Integer) createGuideFromTransactionsForm.get("contributorNumber");
        String gratuitySituationId = (String) createGuideFromTransactionsForm.get("gratuitySituationId");
        String studentId = (String) createGuideFromTransactionsForm.get("studentId");
        String[] transactionsWithoutGuide = (String[]) createGuideFromTransactionsForm.get("transactionsWithoutGuide");

        // Read Contributors
        InfoContributor infoContributor = readContributor(mapping, userView, contributorNumber);

        // Read Registration
        InfoStudent infoStudent = readStudent(mapping, userView, studentId);

        // Read Gratuity Situation
        InfoGratuitySituation infoGratuitySituation = readGratuitySituation(userView, gratuitySituationId);

        InfoGuide infoGuide = new InfoGuide();
        infoGuide.setCreationDate(Calendar.getInstance().getTime());
        infoGuide.setGuideRequester(GuideRequester.STUDENT);
        infoGuide.setInfoContributor(infoContributor);
        infoGuide.setInfoExecutionDegree(infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree());
        infoGuide.setInfoPerson(infoStudent.getInfoPerson());
        infoGuide.setVersion(new Integer(1));
        infoGuide.setYear(new Integer(Calendar.getInstance().get(Calendar.YEAR)));

        try {
            infoGuide = CreateGuideFromTransactions.run(infoGuide, "", GuideState.PAYED, Arrays.asList(transactionsWithoutGuide));

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute(PresentationConstants.GUIDE, infoGuide);

        return mapping.findForward("createSuccess");

    }

    /**
     * @param errorMapping
     * @param userView
     * @param contributorNumber
     * @return
     * @throws NonExistingActionException
     * @throws FenixActionException
     */
    private InfoContributor readContributor(ActionMapping errorMapping, User userView, Integer contributorNumber)
            throws NonExistingActionException, FenixActionException {

        InfoContributor infoContributor = null;
        try {
            infoContributor = ReadContributor.runReadContributor(contributorNumber);

        } catch (ExcepcaoInexistente e) {
            throw new NonExistingActionException("error.masterDegree.administrativeOffice.nonExistingContributorSimple",
                    errorMapping.findForward("chooseContributor"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return infoContributor;
    }

    /**
     * @param userView
     * @param gratuitySituationId
     * @return
     * @throws FenixActionException
     */
    private InfoGratuitySituation readGratuitySituation(User userView, String gratuitySituationId)
            throws FenixActionException {
        InfoGratuitySituation infoGratuitySituation = null;

        try {
            infoGratuitySituation = ReadGratuitySituationById.run(gratuitySituationId);

        } catch (ExcepcaoInexistente e) {
            throw new FenixActionException(e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return infoGratuitySituation;
    }

    /**
     * @param mapping
     * @param userView
     * @param studentId
     * @return
     * @throws FenixActionException
     * @throws NonExistingActionException
     */
    private InfoStudent readStudent(ActionMapping mapping, User userView, String studentId) throws FenixActionException,
            NonExistingActionException {
        InfoStudent infoStudent = null;

        try {
            infoStudent = (InfoStudent) ReadStudentById.run(studentId);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoStudent == null) {
            throw new NonExistingActionException("error.exception.masterDegree.nonExistentStudent",
                    mapping.findForward("chooseContributor"));
        }
        return infoStudent;
    }

}