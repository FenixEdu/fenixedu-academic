package net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.identificationCardManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.research.result.OpenFileBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatch;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderBatchSender;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderCardInformation;
import net.sourceforge.fenixedu.domain.cardGeneration.SantanderSequenceNumberGenerator;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.FileUtils;

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
        request.setAttribute("uploadDCHPFileBean", new OpenFileBean());
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
        request.setAttribute("uploadDCHPFileBean", new OpenFileBean());
        return mapping.findForward("entryPoint");
    }

    public ActionForward submitDCHPFile(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        OpenFileBean dchpFileBean = getRenderedObject("uploadDCHPFileBean");

        try {
            final String stringResults = readFile(dchpFileBean);
            String[] splitedFile = stringResults.split("\r\n");
            String firstDetailedLine = (splitedFile.length > 1) ? splitedFile[1] : null;
            int numberOfRegisters = (splitedFile.length > 1) ? Integer.parseInt(splitedFile[0].substring(32, 41)) : 0;
            boolean error = false;
            /*verify the file format and the number of registers*/
            if (firstDetailedLine == null || (splitedFile.length - 2) != numberOfRegisters) {
                addErrorMessage(request, "errors", "message.dchp.file.submit.wrong.format");
                error = true;
            }
            /*verify if the file was submitted*/
            if (!error) {
                String ist_id = SantanderCardInformation.getCardID(firstDetailedLine).trim();
                Person person = Person.findByUsername(ist_id);
                Object[] cards_info = person.getSantanderCardsInformationSet().toArray();
                SantanderCardInformation test_card_info = createNewCardInformation(person, firstDetailedLine);
                for (Object obj : cards_info) {
                    SantanderCardInformation card_info = (SantanderCardInformation) obj;
                    if (card_info.getDchpRegisteLine().equals(test_card_info.getDchpRegisteLine())) {
                        addErrorMessage(request, "errors", "message.dchp.file.submit.already.submited");
                        error = true;
                        break;
                    }
                }
                deleteCardInformation(test_card_info);
            }
            /*store the new entries of the dchp file*/
            if (!error) {
                for (int i = 1; i < splitedFile.length - 1; i++) {
                    String detailedLine = splitedFile[i];
                    /*get Person object*/
                    String username = SantanderCardInformation.getCardID(detailedLine).trim();
                    Person p = Person.findByUsername(username);
                    /*create new CardInformation*/
                    createNewCardInformation(p, detailedLine);
                }
                request.setAttribute("success", "true");
            }
        } catch (IOException e) {
            addErrorMessage(request, e.getMessage(), e.getMessage());
        }
        request.setAttribute("santanderBean", new ManageSantanderCardGenerationBean());
        request.setAttribute("uploadDCHPFileBean", new OpenFileBean());
        return mapping.findForward("entryPoint");
    }

    private String readFile(OpenFileBean dchpFileBean) throws IOException {
        return FileUtils.readFile(dchpFileBean.getInputStream());
    }

    public ActionForward createBatch(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        ExecutionYear executionYear = FenixFramework.getDomainObject(request.getParameter("executionYearEid"));
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
        request.setAttribute("uploadDCHPFileBean", new OpenFileBean());
        return mapping.findForward("entryPoint");
    }

    /*
     * Download | Send | Delete
     */

    public ActionForward downloadBatch(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        SantanderBatch santanderBatch = FenixFramework.getDomainObject(request.getParameter("santanderBatchEid"));
        ExecutionYear executionYear = FenixFramework.getDomainObject(request.getParameter("executionYearEid"));
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
            request.setAttribute("uploadDCHPFileBean", new OpenFileBean());
            return mapping.findForward("entryPoint");
        }

        return null;
    }

    public ActionForward downloadDDXR(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        SantanderBatch santanderBatch = FenixFramework.getDomainObject(request.getParameter("santanderBatchEid"));
        ExecutionYear executionYear = FenixFramework.getDomainObject(request.getParameter("executionYearEid"));
        ManageSantanderCardGenerationBean santanderBean;

        try {
            response.setContentType("application/zip");
            response.setHeader("Content-disposition",
                    "attachment; filename=SantanderTecnico_DDXR&Photos_" + (new DateTime()).toString("yyyyMMddHHmm") + ".zip");
            final ServletOutputStream writer = response.getOutputStream();
            final byte[] zipFile = santanderBatch.getPhotosAndDDXR();
            writer.write(zipFile);
            writer.flush();
            response.flushBuffer();
        } catch (Exception e) {
            addErrorMessage(request, "errors", "error.generatingDDXR&PhotosFailed " + e.getMessage());
            santanderBean = new ManageSantanderCardGenerationBean(executionYear);
            refreshBeanState(santanderBean);
            request.setAttribute("santanderBean", santanderBean);
            request.setAttribute("uploadDCHPFileBean", new OpenFileBean());
            return mapping.findForward("entryPoint");
        }

        return null;
    }

    public ActionForward sendBatch(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        SantanderBatch santanderBatch = FenixFramework.getDomainObject(request.getParameter("santanderBatchEid"));
        Person requester = getUserView(request).getPerson();
        sealBatch(santanderBatch, requester);
        return downloadBatch(mapping, actionForm, request, response);
    }

    public ActionForward deleteBatch(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        SantanderBatch santanderBatch = FenixFramework.getDomainObject(request.getParameter("santanderBatchEid"));
        ExecutionYear executionYear = FenixFramework.getDomainObject(request.getParameter("executionYearEid"));
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
        request.setAttribute("uploadDCHPFileBean", new OpenFileBean());
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
        SantanderBatch lastCreatedBatch = batches.iterator().next();
        return (lastCreatedBatch != null && lastCreatedBatch.getSent() != null);
    }

    @Atomic
    private void createNewBatch(Person requester, ExecutionYear executionYear) {
        new SantanderBatch(requester, executionYear);
    }

    @Atomic
    private void destroyBatch(SantanderBatch batch) {
        batch.delete();
    }

    @Atomic
    private SantanderCardInformation createNewCardInformation(Person person, String line) {
        return new SantanderCardInformation(person, line);
    }

    @Atomic
    private void deleteCardInformation(SantanderCardInformation card_info) {
        card_info.delete();
    }

    private void refreshBeanState(ManageSantanderCardGenerationBean santanderBean) {
        santanderBean.setSantanderBatches(retrieveBatches(santanderBean.getExecutionYear()));
        santanderBean.setAllowNewCreation(canCreateNewBatch(santanderBean.getExecutionYear()));
    }

    @Atomic
    private void sealBatch(SantanderBatch santanderBatch, Person requester) {
        santanderBatch.setSequenceNumber(SantanderSequenceNumberGenerator.getNewSequenceNumber());
        santanderBatch.setSent(new DateTime());
        santanderBatch.setSantanderBatchSender(new SantanderBatchSender(requester));
    }

}
