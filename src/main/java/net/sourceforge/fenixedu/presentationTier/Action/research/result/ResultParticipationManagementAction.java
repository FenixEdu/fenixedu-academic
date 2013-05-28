package net.sourceforge.fenixedu.presentationTier.Action.research.result;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits.CreateExternalUnitByName;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.ChangeResultParticipationsOrder;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.CreateResultParticipation;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.DeleteResultParticipation;
import net.sourceforge.fenixedu.applicationTier.Servico.research.result.SaveResultParticipationsOrder;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean.ParticipationType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.OrderChange;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "researcher", path = "/result/resultParticipationManagement", scope = "session", parameter = "method")
@Forwards(value = {
        @Forward(name = "createExternalUnit", path = "/researcher/result/participations/createExternalUnit.jsp"),
        @Forward(name = "viewEditPublication", path = "/resultPublications/showPublication.do"),
        @Forward(name = "editParticipation", path = "/researcher/result/participations/editResultParticipations.jsp",
                tileProperties = @Tile(head = "/commons/renderers/treeRendererHeader.jsp")),
        @Forward(name = "ListPublications", path = "/resultPublications/listPublications.do", tileProperties = @Tile(
                title = "private.operator.personnelmanagement.managementfaculty.teacherevaluation.publications")),
        @Forward(name = "editPatent", path = "/resultPatents/showPatent.do"),
        @Forward(name = "listPatents", path = "/resultPatents/management.do") })
public class ResultParticipationManagementAction extends ResultsManagementAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        final ResearchResult result = getResultFromRequest(request);
        Person person = getLoggedPerson(request);
        if (result == null || (!result.hasPersonParticipation(person) && !result.getCreator().equals(person))) {
            return backToResultList(mapping, form, request, response);
        }

        ResultParticipationCreationBean bean = getRenderedObject("bean");

        setResParticipationRequestAttributes(request, result, (bean == null) ? getBeanFromRequest(request, result) : bean);
        return mapping.findForward("editParticipation");
    }

    public ActionForward changeParticipationType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        final ResultParticipationCreationBean bean = getRenderedObject("bean");
        bean.setParticipator(null);
        bean.setParticipatorName(null);
        ResearchResult result = bean.getResult();
        request.setAttribute("bean", bean);
        request.setAttribute("result", result);
        schemasStateAutomaton(request, result, bean, false);
        checkRolesInCreation(request);
        RenderUtils.invalidateViewState("bean");
        return mapping.findForward("editParticipation");
    }

    public ActionForward changeUnitType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        final ResultParticipationCreationBean bean = getRenderedObject("bean");
        ResearchResult result = bean.getResult();
        request.setAttribute("bean", bean);
        request.setAttribute("result", result);
        schemasStateAutomaton(request, result, bean, true);
        checkRolesInCreation(request);
        RenderUtils.invalidateViewState("bean");
        return mapping.findForward("editParticipation");
    }

    private void checkRolesInCreation(HttpServletRequest request) {
        if (getFromRequest(request, "editRoles") != null) {
            request.setAttribute("editRoles", "editRoles");
        } else if (getFromRequest(request, "alterOrder") != null) {
            request.setAttribute("alterOrder", "alterOrder");
        } else {
            request.setAttribute("removeOnly", "removeOnly");
        }
    }

    public ActionForward createWrapper(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        if (getFromRequest(request, "createNew") != null) {
            return prepareCreateParticipator(mapping, form, request, response);
        } else {
            return create(mapping, form, request, response);
        }
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws  FenixServiceException {
        final ResultParticipationCreationBean bean = getRenderedObject("bean");

        request.setAttribute("bean", bean);
        try {
            if (bean.getParticipator() == null) {
                if (bean.isBeanExternal()) {
                    request.setAttribute("name", bean.getParticipatorName());
                    request.setAttribute("needToCreatePerson", true);
                    schemasStateAutomaton(request, bean.getResult(), bean, false);
                } else {
                    throw new DomainException("error.label.invalidNameForPersonInSelection");
                }
            } else {
                createParticipation(bean);
                schemasStateAutomaton(request, bean.getResult(), bean, false);
            }
        } catch (Exception e) {
            final ActionForward defaultForward = backToResultList(mapping, form, request, response);
            if (bean != null) {
                bean.reset();
            }
            return processException(request, mapping, defaultForward, e);
        }

        // bean.reset();
        checkRolesInCreation(request);
        request.setAttribute("bean", bean);
        request.setAttribute("result", bean.getResult());
        RenderUtils.invalidateViewState();
        return mapping.findForward("editParticipation");
    }

    private void createParticipation(ResultParticipationCreationBean bean) throws  FenixServiceException {

        CreateResultParticipation.run(bean);
    }

    public ActionForward remove(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws  FenixServiceException {
        final ResultParticipation participation = readResultParticipationFromRequest(request);
        if (participation == null) {
            return backToResultList(mapping, form, request, response);
        }

        final Person loggedPerson = getLoggedPerson(request);

        if (participation.getPerson().equals(loggedPerson) && getFromRequest(request, "confirm") == null
                && getFromRequest(request, "cancel") == null) {
            request.setAttribute("deleteConfirmation", participation.getExternalId());
        }

        if (getFromRequest(request, "confirm") != null || !participation.getPerson().equals(loggedPerson)) {

            try {

                DeleteResultParticipation.run(participation);
            } catch (Exception e) {
                final ActionForward defaultForward = backToResultList(mapping, form, request, response);
                return processException(request, mapping, defaultForward, e);
            }
        }

        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward saveOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        final String treeStructure = (String) getFromRequest(request, "tree");
        final ResearchResult result = getResultFromRequest(request);

        if (treeStructure != null && treeStructure.length() != 0) {
            final List<ResultParticipation> newParticipationsOrder = reOrderParticipations(treeStructure, result);
            try {

                SaveResultParticipationsOrder.run(result, newParticipationsOrder);
            } catch (Exception e) {
                final ActionForward defaultForward = backToResultList(mapping, form, request, response);
                return processException(request, mapping, defaultForward, e);
            }
        }

        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward moveUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return move(ResultParticipation.OrderChange.MoveUp, mapping, form, request, response);
    }

    public ActionForward moveDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return move(ResultParticipation.OrderChange.MoveDown, mapping, form, request, response);
    }

    public ActionForward moveTop(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return move(ResultParticipation.OrderChange.MoveTop, mapping, form, request, response);
    }

    public ActionForward moveBottom(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return move(ResultParticipation.OrderChange.MoveBottom, mapping, actionForm, request, response);
    }

    public ActionForward prepareEditRoles(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("editRoles", "editRoles");
        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward prepareAlterOrder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("alterOrder", "alterOrder");
        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward prepareCreateParticipator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        ResearchResult result = getResultFromRequest(request);
        ResultParticipationCreationBean bean =
                (ResultParticipationCreationBean) RenderUtils.getViewState("bean").getMetaObject().getObject();
        bean.setUnitParticipationType(ParticipationType.EXTERNAL);

        setResParticipationRequestAttributes(request, result, bean);
        request.setAttribute("bean", bean);
        if (bean.getParticipator() != null) {
            request.setAttribute("needToCreatePerson", true);
            addActionMessage(request, "label.cannot.select.person.to.create.new");
            return mapping.findForward("editParticipation");
        }
        request.setAttribute("duringCreation", true);
        request.setAttribute("createUnit", true);
        return mapping.findForward("editParticipation");
    }

    public ActionForward createParticipator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        ResearchResult result = getResultFromRequest(request);
        ResultParticipationCreationBean bean =
                (ResultParticipationCreationBean) RenderUtils.getViewState("beanForExternalPerson").getMetaObject().getObject();

        if (bean.getOrganization() != null) {

            ExternalContract contract =
                    InsertExternalPerson.run(new InsertExternalPerson.ServiceArguments(bean.getParticipatorName(), bean
                            .getOrganization()));
            bean.setParticipator(contract.getPerson().getPersonName());
            createParticipation(bean);
        } else {
            request.setAttribute("createUnit", true);
            request.setAttribute("duringCreation", true);
            request.setAttribute("bean", bean);
            setResParticipationRequestAttributes(request, result, bean);
            RenderUtils.invalidateViewState();
            return mapping.findForward("editParticipation");
        }
        return prepareEdit(mapping, form, request, response);
    }

    public ActionForward unitWrapper(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        ResultParticipationCreationBean bean =
                (ResultParticipationCreationBean) RenderUtils.getViewState("beanForExternalPerson").getMetaObject().getObject();

        if (bean.getOrganization() == null) {
            return createUnit(mapping, form, request, response);
        } else {
            return createParticipator(mapping, form, request, response);
        }
    }

    public ActionForward createUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {

        getResultFromRequest(request);

        ResultParticipationCreationBean bean =
                (ResultParticipationCreationBean) RenderUtils.getViewState("beanForExternalPerson").getMetaObject().getObject();

        Unit unit = CreateExternalUnitByName.run(bean.getOrganizationName());
        bean.setOrganization(unit);
        createParticipation(bean);

        return prepareEdit(mapping, form, request, response);
    }

    private ActionForward move(OrderChange orderChange, ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws  FenixServiceException {
        final ResultParticipation participation = readResultParticipationFromRequest(request);
        if (participation == null) {
            return backToResultList(mapping, form, request, response);
        }

        try {

            ChangeResultParticipationsOrder.run(participation, orderChange);
        } catch (Exception e) {
            final ActionForward defaultForward = backToResultList(mapping, form, request, response);
            return processException(request, mapping, defaultForward, e);
        }

        return prepareEdit(mapping, form, request, response);
    }

    private void schemasStateAutomaton(HttpServletRequest request, ResearchResult result, ResultParticipationCreationBean bean,
            boolean automatonIsInUnitSelection) {

        String resultParticipationsSchema =
                result.getIsPossibleSelectPersonRole() ? "resultParticipation.full" : "resultParticipation.withoutRole";
        request.setAttribute("listSchema", resultParticipationsSchema);

        if (!automatonIsInUnitSelection) {
            String createResultParticipationSchema = "resultParticipation.creation";
            if (result.getIsPossibleSelectPersonRole()) {
                createResultParticipationSchema += "WithRole";
            }
            createResultParticipationSchema += (bean.isBeanExternal()) ? ".external" : ".internal";
            request.setAttribute("createSchema", createResultParticipationSchema);
        } else {
            bean.setUnitParticipationType(ParticipationType.EXTERNAL);
            String createResultParticipationSchema = "resultParticipation.fullCreation";
            if (result.getIsPossibleSelectPersonRole()) {
                createResultParticipationSchema += "WithRole";
            }
            createResultParticipationSchema += ".external.readOnly";
            request.setAttribute("createSchema", createResultParticipationSchema);
        }
    }

    private void checkNeededSchemas(HttpServletRequest request, ResearchResult result, ResultParticipationCreationBean bean) {
        String resultParticipationsSchema = "resultParticipation.withoutRole";
        String createResultParticipationSchema = "resultParticipation.creation";

        if ((bean.getParticipator() != null || (bean.getParticipatorName() != null && bean.getParticipatorName().length() > 0))
                && (bean.isBeanExternal() || bean.isUnitExternal())) {
            createResultParticipationSchema = "resultParticipation.fullCreation";
        }

        // Defining schemas with roles
        if (result.getIsPossibleSelectPersonRole()) {
            resultParticipationsSchema = "resultParticipation.full";
            if (!bean.isBeanExternal()) {
                createResultParticipationSchema = "resultParticipation.creationWithRole";
            } else {
                if (bean.getParticipator() == null) {
                    if (bean.getParticipatorName().length() == 0) {
                        createResultParticipationSchema = "resultParticipation.creationWithRole";
                    } else {
                        createResultParticipationSchema = "resultParticipation.fullCreationWithRole";
                    }
                } else {
                    bean.setOrganization(bean.getParticipator().getPerson().getExternalContract().getInstitutionUnit());
                    createResultParticipationSchema = "resultParticipation.creationWithRole";
                }
            }
        }

        createResultParticipationSchema += (bean.isBeanExternal()) ? ".external" : ".internal";

        request.setAttribute("listSchema", resultParticipationsSchema);
        request.setAttribute("createSchema", createResultParticipationSchema);
    }

    private void checkNeededWarnings(HttpServletRequest request, ResearchResult result) throws 
            FenixServiceException {
        Person person = getLoggedPerson(request);
        if (!result.hasPersonParticipation(person) && !result.getCreator().equals(person)) {
            addActionMessage(request, "researcher.ResultParticipation.last.participation.warning");
        }
    }

    private List<ResultParticipation> reOrderParticipations(String treeStructure, ResearchResult result) {
        final List<ResultParticipation> newParticipationsOrder = new ArrayList<ResultParticipation>();
        final List<ResultParticipation> oldParticipationsOrder = result.getOrderedResultParticipations();
        final String[] nodes = treeStructure.split(",");

        for (String node : nodes) {
            String[] parts = node.split("-");

            Integer index = getId(parts[0]) - 1;
            ResultParticipation participation = oldParticipationsOrder.get(index);
            newParticipationsOrder.add(participation);
        }
        return newParticipationsOrder;
    }

    private ResultParticipationCreationBean getBeanFromRequest(HttpServletRequest request, ResearchResult result)
            throws  FenixServiceException {
        ResultParticipationCreationBean bean = (ResultParticipationCreationBean) getFromRequest(request, "bean");

        if (bean == null) {
            bean = new ResultParticipationCreationBean(result);
        }

        return bean;
    }

    private ResultParticipation readResultParticipationFromRequest(HttpServletRequest request) {
        final Integer oid = getRequestParameterAsInteger(request, "participationId");
        ResultParticipation participation = null;

        try {
            participation = ResultParticipation.readByOid(oid);
        } catch (DomainException e) {
            addActionMessage(request, e.getKey(), e.getArgs());
        }

        return participation;
    }

    private void setResParticipationRequestAttributes(HttpServletRequest request, ResearchResult result,
            ResultParticipationCreationBean bean) throws  FenixServiceException {

        schemasStateAutomaton(request, result, bean, false); // Define
        // schemas to
        // use
        checkNeededWarnings(request, result); // Action Warning Messages
        checkRolesInCreation(request);

        request.setAttribute("bean", bean);
        request.setAttribute("result", result);
    }

    protected Integer getId(String id) {
        if (id == null) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultParticipationCreationBean getRenderedObject(String id) {
        return (ResultParticipationCreationBean) super.getRenderedObject(id);
    }

}