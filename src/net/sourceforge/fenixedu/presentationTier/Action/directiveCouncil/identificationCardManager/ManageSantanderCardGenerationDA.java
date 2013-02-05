package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.identificationCardManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatch;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatchSender;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderSequenceNumberGenerator;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "identificationCardManager", path = "/manageSantander", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "entryPoint", path = "/identificationCardManager/santander/showSantanderBatches.jsp",
                tileProperties = @Tile(title = "private.identificationcards.santander")),
        @Forward(name = "uploadCardInfo", path = "/identificationCardManager/cardGeneration/uploadCardInfo.jsp",
                tileProperties = @Tile(title = "private.identificationcards.santander")) })
public class ManageSantanderCardGenerationDA extends FenixDispatchAction {

    public ActionForward intro(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        request.setAttribute("santanderBean", new ManageSantanderCardGenerationBean());
        return mapping.findForward("entryPoint");
    }

    public ActionForward selectExecutionYearPostback(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ManageSantanderCardGenerationBean santanderBean = getRenderedObject("santanderBean");
        ExecutionYear year = santanderBean.getExecutionYear();
        if (year != null) {
            refreshBeanState(santanderBean);
        }
        request.setAttribute("santanderBean", santanderBean);
        return mapping.findForward("entryPoint");
    }

    public ActionForward createBatch(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        ExecutionYear executionYear = AbstractDomainObject.fromExternalId(request.getParameter("executionYearEid"));
        ManageSantanderCardGenerationBean santanderBean;

        if (executionYear == null) {
            addErrorMessage(request, "errors", "error.cantCreateNewBatchForExecutionYearNull");
            santanderBean = new ManageSantanderCardGenerationBean();
        } else {
            santanderBean = new ManageSantanderCardGenerationBean(executionYear);
            Person requester = getUserView(request).getPerson();
            createNewBatch(requester, santanderBean.getExecutionYear());
            refreshBeanState(santanderBean);
        }

        request.setAttribute("santanderBean", santanderBean);
        return mapping.findForward("entryPoint");
    }

    /*
     * Download | Send | Delete
     */

    public ActionForward downloadBatch(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        SantanderBatch santanderBatch = AbstractDomainObject.fromExternalId(request.getParameter("santanderBatchEid"));
        ExecutionYear executionYear = AbstractDomainObject.fromExternalId(request.getParameter("executionYearEid"));
        ManageSantanderCardGenerationBean santanderBean;

        try {
            String fileString = santanderBatch.generateTUI();
            response.setContentType("text/plain");
            response.setHeader("Content-disposition",
                    "attachment; filename=SantanderTecnico_TUI_" + (new DateTime()).toString("yyyyMMddHHmm") + ".txt");
            final ServletOutputStream writer = response.getOutputStream();
            writer.write(fileString.getBytes("Cp1252"));
            writer.flush();
            response.flushBuffer();
        } catch (Exception e) {
            addErrorMessage(request, "errors", "error.generatingTUIFailed " + e.getMessage());
            santanderBean = new ManageSantanderCardGenerationBean(executionYear);
            refreshBeanState(santanderBean);
            request.setAttribute("santanderBean", santanderBean);
            return mapping.findForward("entryPoint");
        }

        return null;
    }

    public ActionForward sendBatch(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        SantanderBatch santanderBatch = AbstractDomainObject.fromExternalId(request.getParameter("santanderBatchEid"));
        Person requester = getUserView(request).getPerson();
        sealBatch(santanderBatch, requester);
        return downloadBatch(mapping, actionForm, request, response);
    }

    public ActionForward deleteBatch(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        SantanderBatch santanderBatch = AbstractDomainObject.fromExternalId(request.getParameter("santanderBatchEid"));
        ExecutionYear executionYear = AbstractDomainObject.fromExternalId(request.getParameter("executionYearEid"));
        ManageSantanderCardGenerationBean santanderBean;

        destroyBatch(santanderBatch);

        if (executionYear == null) {
            addErrorMessage(request, "errors", "error.lostTrackOfExecutionYear");
            santanderBean = new ManageSantanderCardGenerationBean();
        } else {
            santanderBean = new ManageSantanderCardGenerationBean(executionYear);
            refreshBeanState(santanderBean);
        }

        request.setAttribute("santanderBean", santanderBean);
        return mapping.findForward("entryPoint");
    }

    private List<SantanderBatch> retrieveBatches(ExecutionYear year) {
        List<SantanderBatch> batches = new ArrayList<SantanderBatch>(year.getSantanderBatches());
        Collections.sort(batches, SantanderBatch.COMPARATOR_BY_MOST_RECENTLY_CREATED);
        return batches;
    }

    private boolean canCreateNewBatch(ExecutionYear year) {
        List<SantanderBatch> batches = retrieveBatches(year);
        if (batches.isEmpty()) {
            return true;
        }
        SantanderBatch lastCreatedBatch = batches.get(0);
        return (lastCreatedBatch != null && lastCreatedBatch.getSent() != null);
    }

    @Service
    private void createNewBatch(Person requester, ExecutionYear executionYear) {
        new SantanderBatch(requester, executionYear);
    }

    @Service
    private void destroyBatch(SantanderBatch batch) {
        batch.delete();
    }

    private void refreshBeanState(ManageSantanderCardGenerationBean santanderBean) {
        santanderBean.setSantanderBatches(retrieveBatches(santanderBean.getExecutionYear()));
        santanderBean.setAllowNewCreation(canCreateNewBatch(santanderBean.getExecutionYear()));
    }

    @Service
    private void sealBatch(SantanderBatch santanderBatch, Person requester) {
        santanderBatch.setSequenceNumber(SantanderSequenceNumberGenerator.getNewSequenceNumber());
        santanderBatch.setSent(new DateTime());
        santanderBatch.setSantanderBatchSender(new SantanderBatchSender(requester));
    }

}
