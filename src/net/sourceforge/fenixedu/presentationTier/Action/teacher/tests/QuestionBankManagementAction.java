package net.sourceforge.fenixedu.presentationTier.Action.teacher.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.AssociateParent;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.ChooseCorrector;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.ChoosePreCondition;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.ChooseValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.CreateAtomicQuestion;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.CreateChoice;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.CreateQuestionBank;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeleteChoice;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeleteCorrector;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeleteGrade;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeletePermissionUnit;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeletePreCondition;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeleteQuestion;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DeleteValidator;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.DisassociateParent;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.tests.SwitchPosition;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.tests.NewAtomicQuestion;
import net.sourceforge.fenixedu.domain.tests.NewChoice;
import net.sourceforge.fenixedu.domain.tests.NewCorrector;
import net.sourceforge.fenixedu.domain.tests.NewMultipleChoiceQuestion;
import net.sourceforge.fenixedu.domain.tests.NewPermissionUnit;
import net.sourceforge.fenixedu.domain.tests.NewQuestion;
import net.sourceforge.fenixedu.domain.tests.NewQuestionBank;
import net.sourceforge.fenixedu.domain.tests.NewQuestionGroup;
import net.sourceforge.fenixedu.domain.tests.NewTestElement;
import net.sourceforge.fenixedu.domain.tests.predicates.CompositePredicate;
import net.sourceforge.fenixedu.domain.tests.predicates.Predicate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.tests.PredicateBean.Action;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * 
 * @author jpmsi, lmam
 * 
 */

@Mapping(module = "teacher", path = "/tests/questionBank", input = "/tests/questionBank.do?method=manageQuestionBank?view=tree", attribute = "testForm", formBean = "testForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "associateParent", path = "/teacher/tests/questionBank/associateParent.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "editPreCondition", path = "/teacher/tests/questionBank/editPreCondition.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "manageLinearQuestionBank", path = "/teacher/tests/questionBank/manageLinearQuestionBank.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "manageTreeQuestionBank", path = "/teacher/tests/questionBank/manageTreeQuestionBank.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "deleteQuestion", path = "/teacher/tests/questionBank/deleteQuestion.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "createQuestionGroup", path = "/teacher/tests/questionBank/createQuestionGroup.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "editCorrector", path = "/teacher/tests/questionBank/editCorrector.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "createAtomicQuestion", path = "/teacher/tests/questionBank/createAtomicQuestion.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "editTestElement", path = "/teacher/tests/questionBank/editTestElement.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "editValidator", path = "/teacher/tests/questionBank/editValidator.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "managePermissionUnits", path = "/teacher/tests/questionBank/managePermissionUnits.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")),
		@Forward(name = "disassociateParent", path = "/teacher/tests/questionBank/disassociateParent.jsp", tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp")) })
public class QuestionBankManagementAction extends FenixDispatchAction {
    private static HashMap<String, String> questionBankViewModes = new HashMap<String, String>();

    static {
	questionBankViewModes.put("tree", "manageTreeQuestionBank");
	questionBankViewModes.put("linear", "manageLinearQuestionBank");
	questionBankViewModes.put(null, "manageTreeQuestionBank");
    }

    public ActionForward manageQuestionBank(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer questionBankId = getCodeFromRequest(request, "oid");

	NewQuestionBank questionBank;

	if (questionBankId == null) {
	    questionBank = this.getOwnedQuestionBank(request);
	} else {
	    questionBank = (NewQuestionBank) rootDomainObject.readNewTestElementByOID(questionBankId);
	}

	request.setAttribute("questionBank", questionBank);

	List<NewPermissionUnit> permissionUnits = getUserView(request).getPerson().getPermissionUnits();
	Set<NewQuestionBank> questionBanks = new HashSet<NewQuestionBank>();
	questionBanks.add(getOwnedQuestionBank(request));
	for (NewPermissionUnit permissionUnit : permissionUnits) {
	    questionBanks.add((NewQuestionBank) permissionUnit.getQuestion());
	}

	request.setAttribute("questionBanks", new ArrayList<NewQuestionBank>(questionBanks));
	request.setAttribute("isOwnBank", getOwnedQuestionBank(request).equals(questionBank));

	return mapping.findForward(questionBankViewModes.get(request.getParameter("view")));
    }

    private NewQuestionBank getOwnedQuestionBank(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
	IUserView userView = getUserView(request);

	Person person = userView.getPerson();

	NewQuestionBank questionBank = person.getQuestionBank();

	if (questionBank == null) {
	    questionBank = CreateQuestionBank.run(person);
	}

	return questionBank;
    }

    public ActionForward editTestElement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer testElementId = getCodeFromRequest(request, "oid");

	NewTestElement testElement = rootDomainObject.readNewTestElementByOID(testElementId);

	if (testElement instanceof NewQuestionBank) {
	    return this.manageQuestionBank(mapping, form, request, response);
	}

	request.setAttribute("testElement", testElement);

	return mapping.findForward("editTestElement");
    }

    public ActionForward selectForModel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward prepareAssociateParent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Integer questionId = getCodeFromRequest(request, "oid");

	NewQuestion question = (NewQuestion) rootDomainObject.readNewTestElementByOID(questionId);

	GroupElementBean groupElementBean = new GroupElementBean(question);

	request.setAttribute("bean", groupElementBean);

	return mapping.findForward("associateParent");
    }

    public ActionForward prepareDisassociateParent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Integer questionId = getCodeFromRequest(request, "oid");

	NewQuestion question = (NewQuestion) rootDomainObject.readNewTestElementByOID(questionId);

	GroupElementBean groupElementBean = new GroupElementBean(question);

	request.setAttribute("bean", groupElementBean);

	return mapping.findForward("disassociateParent");
    }

    public ActionForward associateParent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	GroupElementBean groupElementBean = (GroupElementBean) getMetaObject("associateParent");

	AssociateParent.run(groupElementBean.getParent(), groupElementBean.getChild());

	request.setAttribute("oid", groupElementBean.getChild().getIdInternal());

	return editTestElement(mapping, form, request, response);
    }

    public ActionForward disassociateParent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	GroupElementBean groupElementBean = (GroupElementBean) getMetaObject("disassociateParent");

	DisassociateParent.run(groupElementBean.getParent(), groupElementBean.getChild());

	request.setAttribute("oid", groupElementBean.getChild().getIdInternal());

	return editTestElement(mapping, form, request, response);
    }

    public ActionForward prepareEditCorrector(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Integer atomicQuestionId = getCodeFromRequest(request, "oid");

	NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) rootDomainObject.readNewTestElementByOID(atomicQuestionId);

	PredicateBean bean = new PredicateBean(atomicQuestion);

	for (NewCorrector corrector : atomicQuestion.getCorrectors()) {
	    bean.getPredicates().add(corrector.getPredicate());
	}

	request.setAttribute("bean", bean);

	return mapping.findForward("editCorrector");
    }

    public ActionForward prepareEditValidator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Integer atomicQuestionId = getCodeFromRequest(request, "oid");

	NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) rootDomainObject.readNewTestElementByOID(atomicQuestionId);

	PredicateBean bean = new PredicateBean(atomicQuestion);

	if (atomicQuestion.getValidator() != null) {
	    bean.getPredicates().add(atomicQuestion.getValidator());
	}

	request.setAttribute("bean", bean);

	return mapping.findForward("editValidator");
    }

    public ActionForward prepareEditPreCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Integer questionId = getCodeFromRequest(request, "oid");

	NewQuestion question = (NewQuestion) rootDomainObject.readNewTestElementByOID(questionId);

	PredicateBean bean = new PredicateBean(question);

	if (question.getPreCondition() != null) {
	    bean.getPredicates().add(question.getPreCondition());
	}

	request.setAttribute("bean", bean);

	return mapping.findForward("editPreCondition");
    }

    public ActionForward prepareCreateAtomicPredicate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PredicateBean bean = (PredicateBean) getMetaObject("createAtomicPredicate");

	RenderUtils.invalidateViewState("createAtomicPredicate");

	if (bean == null) {
	    bean = (PredicateBean) getMetaObject("chooseAtomicPredicateType");
	    RenderUtils.invalidateViewState("chooseAtomicPredicateType");
	}

	request.setAttribute("bean", bean);

	return mapping.findForward(request.getParameter("returnTo"));
    }

    public ActionForward createAtomicPredicate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	PredicateBean bean = (PredicateBean) getMetaObject("createAtomicPredicate");

	bean.addPredicate();

	bean = new PredicateBean(bean);

	request.setAttribute("bean", bean);

	RenderUtils.invalidateViewState("fillInDetails");

	return mapping.findForward(request.getParameter("returnTo"));
    }

    public ActionForward createGrade(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	NewQuestion question = (NewQuestion) getMetaObject("create-grade");

	request.setAttribute("oid", question.getIdInternal());

	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward managePredicates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	PredicateBean bean = (PredicateBean) getMetaObject("predicateList");

	Action action = bean.getAction();

	if (action != null) {
	    switch (action) {
	    case DELETE:
		bean.getPredicates().removeAll(bean.getSelectedPredicates());

		bean = new PredicateBean(bean);
		break;
	    default:
		break;
	    }
	}

	request.setAttribute("bean", bean);
	RenderUtils.invalidateViewState("predicateList");

	return mapping.findForward(request.getParameter("returnTo"));
    }

    public ActionForward chooseCorrector(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	PredicateBean bean = (PredicateBean) getMetaObject("choosePredicate");

	ChooseCorrector.run((NewAtomicQuestion) bean.getQuestion(), bean.getChoosenPredicate(), bean.getPercentage());

	bean = new PredicateBean(bean);

	request.setAttribute("bean", bean);

	RenderUtils.invalidateViewState("choosePredicate");

	return mapping.findForward("editCorrector");
    }

    public ActionForward chooseValidator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	PredicateBean bean = (PredicateBean) getMetaObject("choosePredicate");

	ChooseValidator.run((NewAtomicQuestion) bean.getQuestion(), bean.getChoosenPredicate());

	bean = new PredicateBean(bean);

	request.setAttribute("bean", bean);

	RenderUtils.invalidateViewState("choosePredicate");

	return mapping.findForward("editValidator");
    }

    public ActionForward choosePreCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	PredicateBean bean = (PredicateBean) getMetaObject("choosePredicate");

	ChoosePreCondition.run(bean.getQuestion(), bean.getChoosenPredicate());

	bean = new PredicateBean(bean);

	request.setAttribute("bean", bean);

	RenderUtils.invalidateViewState("choosePredicate");

	return mapping.findForward("editPreCondition");
    }

    public ActionForward invalidChoosePredicate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	PredicateBean bean = (PredicateBean) getMetaObject("choosePredicate");

	request.setAttribute("bean", bean);

	return mapping.findForward(request.getParameter("returnTo"));
    }

    public ActionForward composePredicates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	PredicateBean bean = (PredicateBean) getMetaObject("predicateList");

	CompositePredicate compositePredicate;
	try {
	    compositePredicate = (CompositePredicate) bean.getCompositePredicateType().getImplementingClass().newInstance();
	} catch (Exception e) {
	    throw new RuntimeException("could.not.instantiate", e);
	}

	for (Predicate predicate : bean.getSelectedPredicates()) {
	    compositePredicate.getPredicates().add(predicate);
	}

	bean.getPredicates().add(compositePredicate);

	bean = new PredicateBean(bean);

	request.setAttribute("bean", bean);

	RenderUtils.invalidateViewState("predicateList");

	return mapping.findForward(request.getParameter("returnTo"));
    }

    public ActionForward prepareManagePermissionUnits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Integer questionId = getCodeFromRequest(request, "oid");

	NewQuestion question = (NewQuestion) rootDomainObject.readNewTestElementByOID(questionId);

	request.setAttribute("question", question);

	return mapping.findForward("managePermissionUnits");
    }

    public ActionForward prepareCreateQuestionGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Integer questionGroupId = getCodeFromRequest(request, "oid");

	NewQuestionGroup questionGroup = (NewQuestionGroup) rootDomainObject.readNewTestElementByOID(questionGroupId);

	request.setAttribute("questionGroup", questionGroup);

	return mapping.findForward("createQuestionGroup");
    }

    public ActionForward createQuestionGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	NewQuestionGroup questionGroup = (NewQuestionGroup) getMetaObject("createQuestionGroup");

	request.setAttribute("oid", questionGroup.getIdInternal());

	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward prepareCreateAtomicQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer questionGroupId = getCodeFromRequest(request, "oid");

	NewQuestionGroup questionGroup = (NewQuestionGroup) rootDomainObject.readNewTestElementByOID(questionGroupId);

	request.setAttribute("questionGroup", questionGroup);
	request.setAttribute("atomicQuestionTypeBean", new AtomicQuestionBean(questionGroup));

	return mapping.findForward("createAtomicQuestion");
    }

    public ActionForward createAtomicQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	AtomicQuestionBean atomicQuestionTypeBean = (AtomicQuestionBean) getMetaObject("edit-atomic-question-type-bean");

	NewQuestion atomicQuestion = CreateAtomicQuestion.run(atomicQuestionTypeBean.getParentQuestionGroup(),
		atomicQuestionTypeBean.getQuestionType());

	request.setAttribute("oid", atomicQuestion.getIdInternal());

	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward invalidCreateAtomicQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	AtomicQuestionBean bean = (AtomicQuestionBean) getMetaObject("edit-atomic-question-type-bean");

	request.setAttribute("questionGroup", bean.getParentQuestionGroup());
	request.setAttribute("atomicQuestionTypeBean", bean);

	return mapping.findForward("createAtomicQuestion");
    }

    public ActionForward createChoice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer multipleChoiceQuestionId = getCodeFromRequest(request, "oid");

	NewMultipleChoiceQuestion multipleChoiceQuestion = (NewMultipleChoiceQuestion) rootDomainObject
		.readNewTestElementByOID(multipleChoiceQuestionId);

	NewChoice choice = CreateChoice.run(multipleChoiceQuestion);

	request.setAttribute("oid", multipleChoiceQuestionId);

	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward deleteCorrector(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer correctorId = getCodeFromRequest(request, "oid");

	NewCorrector corrector = rootDomainObject.readNewCorrectorByOID(correctorId);

	NewAtomicQuestion atomicQuestion = corrector.getAtomicQuestion();

	DeleteCorrector.run(corrector);

	request.setAttribute("oid", atomicQuestion.getIdInternal());

	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward deleteCorrectors(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	String[] correctorOids = request.getParameterValues("correctorOids");

	if (correctorOids != null) {
	    for (String correctorOid : correctorOids) {
		request.setAttribute("oid", Integer.parseInt(correctorOid));

		this.deleteCorrector(mapping, form, request, response);
	    }

	}

	PredicateBean bean = (PredicateBean) getMetaObject("delete-correctors");

	RenderUtils.invalidateViewState("delete-correctors");

	request.setAttribute("bean", bean);

	return mapping.findForward("editCorrector");
    }

    public ActionForward deleteValidator(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer atomicQuestionId = getCodeFromRequest(request, "oid");

	NewAtomicQuestion atomicQuestion = (NewAtomicQuestion) rootDomainObject.readNewTestElementByOID(atomicQuestionId);

	DeleteValidator.run(atomicQuestion);

	request.setAttribute("oid", atomicQuestion.getIdInternal());

	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward deletePreCondition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer questionId = getCodeFromRequest(request, "oid");

	NewQuestion question = (NewQuestion) rootDomainObject.readNewTestElementByOID(questionId);

	DeletePreCondition.run(question);

	request.setAttribute("oid", question.getIdInternal());

	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward deletePermissionUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer permissionUnitId = getCodeFromRequest(request, "oid");

	NewPermissionUnit permissionUnit = rootDomainObject.readNewPermissionUnitByOID(permissionUnitId);

	request.setAttribute("question", permissionUnit.getQuestion());

	DeletePermissionUnit.run(permissionUnit);

	return mapping.findForward("managePermissionUnits");
    }

    public ActionForward deleteChoice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer choiceId = getCodeFromRequest(request, "oid");

	NewChoice choice = (NewChoice) rootDomainObject.readNewTestElementByOID(choiceId);

	request.setAttribute("oid", choice.getMultipleChoiceQuestion().getIdInternal());

	DeleteChoice.run(choice);

	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward deleteGrade(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer atomicQuestionId = getCodeFromRequest(request, "oid");

	NewQuestion question = (NewQuestion) rootDomainObject.readNewTestElementByOID(atomicQuestionId);

	DeleteGrade.run(question);

	request.setAttribute("oid", question.getIdInternal());

	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward prepareDeleteQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer questionId = getCodeFromRequest(request, "oid");
	request.setAttribute("questionGroupId", questionId);
	request.setAttribute("parentQuestionGroupId", getCodeFromRequest(request, "parentQuestionGroupOid"));

	NewQuestion question = (NewQuestion) rootDomainObject.readNewTestElementByOID(questionId);
	request.setAttribute("question", question);

	return mapping.findForward("deleteQuestion");
    }

    public ActionForward deleteQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer questionId = getCodeFromRequest(request, "oid");

	NewQuestion question = (NewQuestion) rootDomainObject.readNewTestElementByOID(questionId);

	NewQuestionBank questionBank = question.getQuestionBank();

	List<NewQuestionGroup> parentQuestionGroups = question.getParentQuestionGroups();

	DeleteQuestion.run(question);

	Integer parentQuestionGroupId = getCodeFromRequest(request, "parentQuestionGroupOid");

	if (parentQuestionGroupId != null) {
	    request.setAttribute("oid", parentQuestionGroupId);
	} else if (parentQuestionGroups.size() == 1) {
	    request.setAttribute("oid", parentQuestionGroups.get(0).getIdInternal());
	    return editTestElement(mapping, form, request, response);
	} else {
	    request.setAttribute("oid", questionBank.getIdInternal());
	}

	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward switchChoice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer choiceId = getCodeFromRequest(request, "oid");
	NewChoice choice = (NewChoice) rootDomainObject.readNewTestElementByOID(choiceId);

	Integer relativePosition = getCodeFromRequest(request, "relativePosition");

	SwitchPosition.run(choice, relativePosition);

	request.setAttribute("oid", choice.getMultipleChoiceQuestion().getIdInternal());
	return this.editTestElement(mapping, form, request, response);
    }

    public ActionForward switchCorrector(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	Integer correctorId = getCodeFromRequest(request, "oid");
	NewCorrector corrector = rootDomainObject.readNewCorrectorByOID(correctorId);

	Integer relativePosition = getCodeFromRequest(request, "relativePosition");

	SwitchPosition.run(corrector, relativePosition);

	request.setAttribute("oid", corrector.getAtomicQuestion().getIdInternal());
	return this.editTestElement(mapping, form, request, response);
    }

    private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
	Integer code = null;
	Object objectCode = request.getAttribute(codeString);

	if (objectCode != null) {
	    if (objectCode instanceof String)
		code = new Integer((String) objectCode);
	    else if (objectCode instanceof Integer)
		code = (Integer) objectCode;
	} else {
	    String thisCodeString = request.getParameter(codeString);
	    if (thisCodeString != null)
		code = new Integer(thisCodeString);
	}

	return code;
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