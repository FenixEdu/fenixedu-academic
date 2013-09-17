package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.CreateModelGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeleteModelRestriction;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.GenerateTests;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.SelectAtomicQuestionRestrictions;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.SelectQuestionGroupRestriction;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.SwitchPosition;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.UnselectRestriction;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.tests.NewModelGroup;
import net.sourceforge.fenixedu.domain.tests.NewModelRestriction;
import net.sourceforge.fenixedu.domain.tests.NewTestGroup;
import net.sourceforge.fenixedu.domain.tests.NewTestModel;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author jpmsi, lmam
 * 
 */

@Mapping(module = "teacher", path = "/tests/testModels", attribute = "testForm", formBean = "testForm", scope = "request",
        parameter = "method")
@Forwards(value = {
        @Forward(name = "editPreCondition", path = "editPreConditionModelPage"),
        @Forward(name = "editTestModel", path = "/teacher/tests/testModels/editTestModel.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/navigationBarIndex.jsp")),
        @Forward(name = "manageTestModels", path = "/teacher/tests/testModels/manageTestModels.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/navigationBarIndex.jsp")),
        @Forward(name = "generateTests", path = "/teacher/tests/testModels/generateTests.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/navigationBarIndex.jsp")),
        @Forward(name = "deleteTestModel", path = "/teacher/tests/testModels/deleteTestModel.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/navigationBarIndex.jsp")),
        @Forward(name = "editModelGroup", path = "/teacher/tests/testModels/editModelGroup.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/navigationBarIndex.jsp")),
        @Forward(name = "sortTestModel", path = "/teacher/tests/testModels/sortTestModel.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/navigationBarIndex.jsp")),
        @Forward(name = "selectQuestions", path = "/teacher/tests/testModels/selectQuestions.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/navigationBarIndex.jsp")) })
public class TestModelsManagementAction extends FenixDispatchAction {

    public ActionForward manageTestModels(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        Teacher teacher = getPerson(request).getTeacher();

        List<NewTestModel> testModels = new ArrayList<NewTestModel>(teacher.getTestModels());
        request.setAttribute("testModels", testModels);

        List<NewTestGroup> testGroups = new ArrayList<NewTestGroup>();

        for (NewTestGroup testGroup : teacher.getTestGroups()) {
            testGroups.add(testGroup);
        }

        request.setAttribute("testGroups", testGroups);

        return mapping.findForward("manageTestModels");
    }

    public ActionForward createTestModel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        // RenderUtils.invalidateViewState("create-test-model");

        return this.manageTestModels(mapping, form, request, response);
    }

    public ActionForward createModelGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        TestModelBean bean = (TestModelBean) getMetaObject("create-model-group");

        CreateModelGroup.run(bean.getModelGroup(), bean.getName());

        request.setAttribute("oid", bean.getTestModel().getExternalId());

        RenderUtils.invalidateViewState("create-model-group");

        return this.editTestModel(mapping, form, request, response);
    }

    public ActionForward invalidCreateModelGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        TestModelBean bean = (TestModelBean) getMetaObject("create-model-group");

        request.setAttribute("testModel", bean.getTestModel());
        request.setAttribute("bean", bean);

        return mapping.findForward("editTestModel");
    }

    public ActionForward selectAtomicQuestionRestrictions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        TestModelBean bean = (TestModelBean) getMetaObject("select-atomic-questions");

        NewModelGroup modelGroup = bean.getModelGroup();
        NewTestModel testModel = bean.getTestModel();

        request.setAttribute("testModel", testModel);
        request.setAttribute("bean", bean);

        List<NewModelRestriction> modelRestrictions = validateSelectAtomicQuestionRestriction(request);

        SelectAtomicQuestionRestrictions.run(testModel, modelRestrictions, modelGroup, bean.getValue());

        return mapping.findForward("selectQuestions");
    }

    public ActionForward invalidSelectAtomicQuestionRestrictions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        TestModelBean bean = (TestModelBean) getMetaObject("select-atomic-questions");

        request.setAttribute("testModel", bean.getTestModel());
        request.setAttribute("bean", bean);

        validateSelectAtomicQuestionRestriction(request);

        return mapping.findForward("selectQuestions");
    }

    private List<NewModelRestriction> validateSelectAtomicQuestionRestriction(HttpServletRequest request) {
        String[] modelRestrictionIds = request.getParameterValues("selectedAtomicQuestionRestrictions");

        List<NewModelRestriction> modelRestrictions = new ArrayList<NewModelRestriction>();

        if (modelRestrictionIds != null) {
            for (String atomicRestrictionIdParameter : modelRestrictionIds) {
                NewModelRestriction atomicRestriction =
                        (NewModelRestriction) FenixFramework.getDomainObject(atomicRestrictionIdParameter);
                modelRestrictions.add(atomicRestriction);
            }
        }

        request.setAttribute("selectedAtomicQuestions", modelRestrictions);

        return modelRestrictions;
    }

    public ActionForward selectQuestionGroupRestriction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        TestModelBean bean = (TestModelBean) getMetaObject("select-question-group.model-group");

        NewTestModel testModel = bean.getTestModel();

        request.setAttribute("testModel", testModel);
        request.setAttribute("bean", bean);

        NewModelRestriction modelRestriction = validateSelectQuestionGroupRestriction(request);

        if (modelRestriction == null) {
            return mapping.findForward("selectQuestions");
        }

        SelectQuestionGroupRestriction.run(testModel, modelRestriction, bean.getModelGroup(), bean.getCount(), bean.getValue());

        RenderUtils.invalidateViewState("select-question-group.model-group");

        return mapping.findForward("selectQuestions");
    }

    public ActionForward invalidSelectQuestionGroupRestriction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixServiceException {
        TestModelBean bean = (TestModelBean) getMetaObject("select-question-group.model-group");

        request.setAttribute("testModel", bean.getTestModel());
        request.setAttribute("bean", bean);

        NewModelRestriction modelRestriction = validateSelectQuestionGroupRestriction(request);

        if (modelRestriction == null) {
            return mapping.findForward("selectQuestions");
        }

        request.setAttribute("selectedQuestionGroupRestriction", modelRestriction);

        return mapping.findForward("selectQuestions");
    }

    private NewModelRestriction validateSelectQuestionGroupRestriction(HttpServletRequest request) {
        String modelRestrictionId = getStringFromRequest(request, "selectedQuestionGroup");

        if (modelRestrictionId == null) {
            createMessage(request, "selectedQuestionGroup", "validation.questionGroupRestriction.choose");

            return null;
        }

        return (NewModelRestriction) FenixFramework.getDomainObject(modelRestrictionId);
    }

    public ActionForward unselectRestriction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewModelRestriction modelRestriction = getDomainObject(request, "oid");

        UnselectRestriction.run(modelRestriction);

        NewTestModel testModel = modelRestriction.getTestModel();

        request.setAttribute("testModel", testModel);
        request.setAttribute("bean", new TestModelBean(testModel));

        return mapping.findForward("editTestModel");
    }

    public ActionForward prepareDeleteTestModel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestModel testModel = getDomainObject(request, "oid");

        request.setAttribute("testModel", testModel);

        return mapping.findForward("deleteTestModel");
    }

    public ActionForward editTestModel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestModel testModel = getDomainObject(request, "oid");

        request.setAttribute("testModel", testModel);
        request.setAttribute("bean", new TestModelBean(testModel));

        return mapping.findForward("editTestModel");
    }

    public ActionForward selectQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestModel testModel = getDomainObject(request, "oid");

        request.setAttribute("testModel", testModel);
        request.setAttribute("bean", new TestModelBean(testModel));

        return mapping.findForward("selectQuestions");
    }

    public ActionForward sortTestModel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestModel testModel = getDomainObject(request, "oid");

        request.setAttribute("testModel", testModel);

        return mapping.findForward("sortTestModel");
    }

    public ActionForward switchModelRestriction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        Integer relativePosition = getCodeFromRequest(request, "relativePosition");

        NewModelRestriction modelRestriction = getDomainObject(request, "oid");

        SwitchPosition.run(modelRestriction, relativePosition);

        request.setAttribute("testModel", modelRestriction.getTestModel());

        return mapping.findForward("sortTestModel");
    }

    public ActionForward editModelGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        NewModelGroup modelGroup = getDomainObject(request, "oid");

        request.setAttribute("modelGroup", modelGroup);

        return mapping.findForward("editModelGroup");
    }

    public ActionForward editModelRestriction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewModelRestriction modelRestriction = getDomainObject(request, "oid");

        NewTestModel testModel = modelRestriction.getTestModel();

        request.setAttribute("testModel", testModel);
        request.setAttribute("bean", new TestModelBean(testModel));

        return mapping.findForward("editTestModel");
    }

    public ActionForward deleteTestModel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestModel testModel = getDomainObject(request, "oid");

        DeleteModelRestriction.run(testModel);

        return this.manageTestModels(mapping, form, request, response);
    }

    public ActionForward deleteModelRestriction(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewModelRestriction modelRestriction = getDomainObject(request, "oid");

        NewTestModel testModel = modelRestriction.getTestModel();

        DeleteModelRestriction.run(modelRestriction);

        return new ActionForward(request.getParameter("returnTo") + "&oid=" + testModel.getExternalId(), true);
    }

    public ActionForward prepareGenerateTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {

        NewTestModel testModel = getDomainObject(request, "oid");

        request.setAttribute("firstStep", true);
        request.setAttribute("bean", new TestModelBean(testModel, testModel.getName()));

        return mapping.findForward("generateTests");
    }

    public ActionForward continueGenerateTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        TestModelBean bean = (TestModelBean) getMetaObject("generate-tests");

        request.setAttribute("secondStep", true);
        request.setAttribute("bean", bean);

        if (bean.isUseFinalDate() && bean.isUseVariations()) {
            RenderUtils.invalidateViewState("generate-tests");
            request.setAttribute("useBoth", true);
            return mapping.findForward("generateTests");
        } else if (bean.isUseFinalDate()) {
            RenderUtils.invalidateViewState("generate-tests");
            request.setAttribute("useFinalDate", true);
            return mapping.findForward("generateTests");
        } else if (bean.isUseVariations()) {
            RenderUtils.invalidateViewState("generate-tests");
            request.setAttribute("useVariations", true);
            return mapping.findForward("generateTests");
        } else {
            return generateTests(mapping, form, request, response);
        }
    }

    public ActionForward invalidGenerateTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        TestModelBean bean = (TestModelBean) getMetaObject("generate-tests");

        request.setAttribute("secondStep", true);
        request.setAttribute("bean", bean);

        return mapping.findForward("generateTests");
    }

    public ActionForward generateTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixServiceException {
        TestModelBean bean = (TestModelBean) getMetaObject("generate-tests");

        GenerateTests.run(bean.getTestModel(), bean.getName(), bean.getExecutionCourse(), bean.getVariations(),
                bean.getFinalDate());

        return this.manageTestModels(mapping, form, request, response);
    }

    private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
        Integer code = null;
        Object objectCode = request.getAttribute(codeString);

        if (objectCode != null) {
            if (objectCode instanceof String) {
                code = new Integer((String) objectCode);
            } else if (objectCode instanceof Integer) {
                code = (Integer) objectCode;
            }
        } else {
            String thisCodeString = request.getParameter(codeString);
            if (thisCodeString != null) {
                code = new Integer(thisCodeString);
            }
        }

        return code;
    }

    private Person getPerson(HttpServletRequest request) {
        IUserView userView = getUserView(request);

        return userView.getPerson();
    }

    private Object getMetaObject(String key) {
        IViewState viewState = RenderUtils.getViewState(key);

        if (viewState == null) {
            return null;
        }

        return viewState.getMetaObject().getObject();
    }

    private void createMessage(HttpServletRequest request, String name, String key) {
        ActionMessages messages = getMessages(request);
        messages.add(name, new ActionMessage(key, true));
        saveMessages(request, messages);
    }

}