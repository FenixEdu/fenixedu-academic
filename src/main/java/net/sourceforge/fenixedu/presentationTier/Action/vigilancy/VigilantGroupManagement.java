/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.AddExamCoordinatorsToVigilantGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.AddIncompatiblePerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.AddVigilantsToGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.CreateVigilantGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.DeleteVigilantGroupByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.RemoveExamCoordinatorRole;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.RemoveExamCoordinatorsFromVigilantGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.RemoveIncompatiblePerson;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.RemoveVigilantsFromGroup;
import net.sourceforge.fenixedu.applicationTier.Servico.person.vigilancy.UpdateVigilantGroup;
import net.sourceforge.fenixedu.dataTransferObject.WrittenEvaluationVigilancyView;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.vigilancy.examCoordination.ExamCoordinationApplication;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ExamCoordinationApplication.class, path = "vigilant-groups",
        titleKey = "label.vigilancy.VigilantGroupManagement")
@Mapping(module = "examCoordination", path = "/vigilancy/vigilantGroupManagement")
@Forwards({ @Forward(name = "incompatibilities", path = "/examCoordinator/vigilancy/manageGroupsIncompatibilities.jsp"),
        @Forward(name = "blank", path = "/commons/blank.jsp"),
        @Forward(name = "editVigilantsInGroups", path = "/examCoordinator/vigilancy/manageVigilantsInGroups.jsp"),
        @Forward(name = "prepareVigilantGroup", path = "/examCoordinator/vigilancy/createVigilantGroup.jsp"),
        @Forward(name = "editVigilantGroupPoints", path = "/examCoordinator/vigilancy/editVigilantGroupPoints.jsp"),
        @Forward(name = "vigilants", path = "/examCoordinator/vigilancy/editVigilantsInVigilantGroup.jsp"),
        @Forward(name = "editVigilantBounds", path = "/examCoordinator/vigilancy/manageVigilantsConvokableStatus.jsp"),
        @Forward(name = "editVigilantStartPoints", path = "/examCoordinator/vigilancy/manageVigilantsStartPoints.jsp"),
        @Forward(name = "selectPreviousPointsSchema", path = "/examCoordinator/vigilancy/selectPreviousPointsSchema.jsp"),
        @Forward(name = "addIncompatiblePersonToVigilant", path = "/examCoordinator/vigilancy/addIncompatibilityToVigilant.jsp"),
        @Forward(name = "showPoints", path = "/examCoordinator/vigilancy/showPoints.jsp"),
        @Forward(name = "manageVigilantGroups", path = "/examCoordinator/vigilancy/manageVigilantGroups.jsp"),
        @Forward(name = "showStats", path = "/examCoordinator/vigilancy/showGroupStats.jsp"),
        @Forward(name = "attributes", path = "/examCoordinator/vigilancy/editVigilantGroup.jsp"),
        @Forward(name = "editCoordinators", path = "/examCoordinator/vigilancy/editCoordinatorsInVigilantGroup.jsp") })
public class VigilantGroupManagement extends FenixDispatchAction {

    @StrutsFunctionality(app = ExamCoordinationApplication.class, path = "incompatibilities",
            titleKey = "label.vigilancy.displayIncompatibleInformation")
    @Mapping(module = "examCoordination", path = "/vigilancy/incompatibilitiesVigilantGroupManagement")
    public static class IncompatibilitiesVigilantGroupManagement extends VigilantGroupManagement {
        @Override
        @EntryPoint
        public ActionForward prepareManageIncompatiblesOfVigilants(ActionMapping mapping, ActionForm form,
                HttpServletRequest request, HttpServletResponse response) throws Exception {
            return super.prepareManageIncompatiblesOfVigilants(mapping, form, request, response);
        }
    }

    public ActionForward generateReportForGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String vigilantGroupId = request.getParameter("oid");
        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(vigilantGroupId);

        List<WrittenEvaluationVigilancyView> beans = getStatsViewForVigilantGroup(group);

        request.setAttribute("vigilantGroup", group);
        request.setAttribute("stats", beans);
        return mapping.findForward("showStats");
    }

    public ActionForward showPoints(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String vigilantGroupId = request.getParameter("oid");
        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(vigilantGroupId);

        List<VigilantWrapper> vigilantWrappers = group.getVigilantWrappersThatCanBeConvoked();
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(VigilantWrapper.CATEGORY_COMPARATOR);
        chain.addComparator(VigilantWrapper.USERNAME_COMPARATOR);
        chain.addComparator(VigilantWrapper.NAME_COMPARATOR);
        Collections.sort(vigilantWrappers, chain);
        request.setAttribute("vigilants", vigilantWrappers);
        request.setAttribute("group", group);

        return mapping.findForward("showPoints");
    }

    protected List<WrittenEvaluationVigilancyView> getStatsViewForVigilantGroup(VigilantGroup group) {
        List<WrittenEvaluation> evaluations = group.getAllAssociatedWrittenEvaluations();
        Collections.sort(evaluations, WrittenEvaluation.COMPARATOR_BY_BEGIN_DATE);

        List<WrittenEvaluationVigilancyView> beans = new ArrayList<WrittenEvaluationVigilancyView>();
        for (WrittenEvaluation evaluation : evaluations) {
            beans.add(new WrittenEvaluationVigilancyView(evaluation));
        }
        return beans;
    }

    public ActionForward prepareVigilantGroupCreation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        prepareManagementBean(request, ExecutionYear.readCurrentExecutionYear());
        return mapping.findForward("prepareVigilantGroup");

    }

    private void putIncompatibilitiesInRequest(HttpServletRequest request, VigilantGroup group) throws FenixServiceException {

        List<VigilantWrapper> incompatibilities = new ArrayList<VigilantWrapper>();
        incompatibilities.addAll(group.getVigilantWrappersWithIncompatiblePerson());

        request.setAttribute("vigilantWrappers", new ArrayList<VigilantWrapper>(incompatibilities));
    }

    public ActionForward prepareManageIncompatiblesOfVigilants(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = new VigilantGroupBean();
        ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();

        String groupId = request.getParameter("gid");
        if (groupId != null) {
            VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(groupId);
            bean.setSelectedVigilantGroup(group);
            putIncompatibilitiesInRequest(request, group);
        }
        bean.setExamCoordinator(coordinator);
        request.setAttribute("bean", bean);
        return mapping.findForward("incompatibilities");
    }

    public ActionForward manageIncompatiblesOfVigilants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("selectVigilantGroup").getMetaObject().getObject();
        VigilantGroup group = bean.getSelectedVigilantGroup();

        if (group != null) {
            putIncompatibilitiesInRequest(request, group);
        }
        request.setAttribute("bean", bean);
        return mapping.findForward("incompatibilities");

    }

    public ActionForward deleteIncompatibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String oid = request.getParameter("oid");

        VigilantWrapper vigilantWrapper = (VigilantWrapper) FenixFramework.getDomainObject(oid);

        RemoveIncompatiblePerson.run(vigilantWrapper);

        String gid = request.getParameter("gid");
        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(gid);

        VigilantGroupBean bean = new VigilantGroupBean();
        ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();

        bean.setSelectedVigilantGroup(group);
        bean.setExamCoordinator(coordinator);
        putIncompatibilitiesInRequest(request, group);
        request.setAttribute("bean", bean);

        return mapping.findForward("incompatibilities");

    }

    public List<VigilantWrapperBean> prepareVigilantWrapperBeans(HttpServletRequest request, ExecutionYear executionYear) {
        List<VigilantGroup> groups;
        Person loggedPerson = getLoggedPerson(request);
        ExamCoordinator coordinator = loggedPerson.getExamCoordinatorForGivenExecutionYear(executionYear);

        List<VigilantWrapperBean> vigilantWrapperBeans = new ArrayList<VigilantWrapperBean>();

        if (coordinator != null) {
            Unit unit = coordinator.getUnit();
            groups = unit.getVigilantGroupsForGivenExecutionYear(executionYear);

            Set<Person> persons = new HashSet<Person>();

            for (VigilantGroup group : groups) {
                persons.addAll(group.getPersons());
            }

            for (Person person : persons) {
                VigilantWrapperBean vigilantWrapperBean = new VigilantWrapperBean(person);
                List<VigilantGroup> convokableForGroups = new ArrayList<VigilantGroup>();
                List<VigilantGroup> notConvokableForGroups = new ArrayList<VigilantGroup>();
                for (VigilantGroup group : groups) {
                    VigilantWrapper vigilantWrapper = group.hasPerson(person);
                    if (vigilantWrapper != null && vigilantWrapper.getConvokable()) {
                        convokableForGroups.add(group);
                    } else {
                        notConvokableForGroups.add(group);
                    }
                }
                vigilantWrapperBean.setConvokableForGroups(convokableForGroups);
                vigilantWrapperBean.setNotConvokableForGroups(notConvokableForGroups);
                vigilantWrapperBeans.add(vigilantWrapperBean);
            }
        }
        return vigilantWrapperBeans;
    }

    @EntryPoint
    public ActionForward prepareVigilantGroupManagement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();

        String parameter = request.getParameter("show");
        if ("vigilants".equals(parameter)) {
            List<VigilantWrapperBean> vigilantWrapperBeans = prepareVigilantWrapperBeans(request, executionYear);
            request.setAttribute("vigilantWrapperBeans", vigilantWrapperBeans);
        }

        prepareManagementBean(request, executionYear);

        request.setAttribute("show", parameter);
        return mapping.findForward("manageVigilantGroups");
    }

    public ActionForward changeDisplaySettingsByGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = prepareVigilantGroups(request);
        request.setAttribute("bean", bean);
        request.setAttribute("show", "groups");
        return mapping.findForward("manageVigilantGroups");

    }

    public ActionForward changeDisplaySettingsByVigilants(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("options").getMetaObject().getObject();
        List<VigilantWrapperBean> vigilantWrapperBeans = prepareVigilantWrapperBeans(request, bean.getExecutionYear());
        request.setAttribute("vigilantWrapperBeans", vigilantWrapperBeans);
        request.setAttribute("bean", bean);
        request.setAttribute("show", "vigilants");
        return mapping.findForward("manageVigilantGroups");

    }

    private VigilantGroupBean prepareVigilantGroups(HttpServletRequest request) {
        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("options").getMetaObject().getObject();

        Person person = getLoggedPerson(request);
        ExamCoordinator coordinator = person.getExamCoordinatorForGivenExecutionYear(bean.getExecutionYear());
        if (coordinator != null) {
            bean.setVigilantGroups(coordinator.getVigilantGroups());
        } else {
            bean.setVigilantGroups(Collections.EMPTY_LIST);
        }
        return bean;
    }

    public ActionForward createVigilantGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("createVigilantGroup").getMetaObject().getObject();

        CreateVigilantGroup.run(bean.getName(), bean.getUnit(), bean.getConvokeStrategy(), bean.getContactEmail(),
                bean.getRulesLink(), bean.getBeginFirstUnavailablePeriod(), bean.getEndFirstUnavailablePeriod(),
                bean.getBeginSecondUnavailablePeriod(), bean.getEndSecondUnavailablePeriod());

        prepareManagementBean(request, ExecutionYear.readCurrentExecutionYear());

        return mapping.findForward("manageVigilantGroups");
    }

    public ActionForward prepareEdition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String oid = request.getParameter("oid");
        String forwardTo = request.getParameter("forwardTo");

        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(oid);
        prepareBeanForVigilantGroupEdition(request, group);
        return mapping.findForward(forwardTo);
    }

    public ActionForward selectUnit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String forwardTo = request.getParameter("forwardTo");
        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("selectUnit").getMetaObject().getObject();
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("selectUnit");
        return mapping.findForward(forwardTo);
    }

    public ActionForward applyChangesToVigilantGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean beanWithName =
                (VigilantGroupBean) RenderUtils.getViewState("editVigilantGroup.block1").getMetaObject().getObject();

        VigilantGroupBean beanWithFirstPeriod =
                (VigilantGroupBean) RenderUtils.getViewState("editVigilantGroup.block2").getMetaObject().getObject();

        VigilantGroupBean beanWithSecondPeriod =
                (VigilantGroupBean) RenderUtils.getViewState("editVigilantGroup.block3").getMetaObject().getObject();

        VigilantGroup vigilantGroup = beanWithName.getSelectedVigilantGroup();

        UpdateVigilantGroup.run(vigilantGroup, beanWithName.getName(), beanWithName.getConvokeStrategy(),
                beanWithName.getContactEmail(), beanWithName.getEmailPrefix(), beanWithName.getRulesLink(),
                beanWithFirstPeriod.getBeginFirstUnavailablePeriod(), beanWithFirstPeriod.getEndFirstUnavailablePeriod(),
                beanWithSecondPeriod.getBeginSecondUnavailablePeriod(), beanWithSecondPeriod.getEndSecondUnavailablePeriod());

        prepareManagementBean(request, ExecutionYear.readCurrentExecutionYear());

        return mapping.findForward("manageVigilantGroups");
    }

    public ActionForward deleteVigilantGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String oid = request.getParameter("oid");

        try {
            DeleteVigilantGroupByOID.run(oid);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }
        prepareManagementBean(request, ExecutionYear.readCurrentExecutionYear());
        return mapping.findForward("manageVigilantGroups");

    }

    public ActionForward displayGroupHistory(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("selectGroupAndYear").getMetaObject().getObject();
        Unit unit = bean.getUnit();
        ExecutionYear executionYear = bean.getExecutionYear();

        List<VigilantGroup> vigilantGroups = unit.getVigilantGroupsForGivenExecutionYear(executionYear);
        bean.setVigilantGroups(vigilantGroups);

        VigilantGroup selectedGroup = bean.getSelectedVigilantGroup();

        if (selectedGroup != null && !selectedGroup.getExecutionYear().equals(executionYear)) {
            bean.setSelectedVigilantGroup(null);
        }
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("selectGroupAndYear");
        return mapping.findForward("displayGroupHistory");
    }

    public ActionForward removeCoordinatorsFromGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("removeCoordinators").getMetaObject().getObject();
        List<ExamCoordinator> coordinators = bean.getExamCoordinators();
        VigilantGroup group = bean.getSelectedVigilantGroup();

        RemoveExamCoordinatorsFromVigilantGroup.run(coordinators, group);

        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("removeCoordinators");
        return mapping.findForward("editCoordinators");
    }

    public ActionForward addCoordinatorsToGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("addCoordinators").getMetaObject().getObject();
        List<ExamCoordinator> coordinators = bean.getExamCoordinators();
        VigilantGroup group = bean.getSelectedVigilantGroup();

        AddExamCoordinatorsToVigilantGroup.run(coordinators, group);

        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("addCoordinators");
        return mapping.findForward("editCoordinators");
    }

    public ActionForward checkExamCoordinatorPermissions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        Person person = getLoggedPerson(request);
        ExamCoordinator coordinator = person.getExamCoordinatorForGivenExecutionYear(executionYear);

        if (coordinator == null) {
            RemoveExamCoordinatorRole.run(person);
        }
        return mapping.findForward("blank");
    }

    public ActionForward prepareAddIncompatiblePersonToVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = new VigilantGroupBean();
        Person person = getLoggedPerson(request);
        ExamCoordinator coordinator = person.getCurrentExamCoordinator();
        bean.setExamCoordinator(coordinator);

        String gid = request.getParameter("gid");
        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(gid);
        bean.setSelectedVigilantGroup(group);

        request.setAttribute("bean", bean);
        return mapping.findForward("addIncompatiblePersonToVigilant");
    }

    public ActionForward vigilantSelectedInIncompatibilityScreen(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        VigilantGroupBean bean =
                (VigilantGroupBean) RenderUtils.getViewState("selectVigilantWrapper").getMetaObject().getObject();
        VigilantGroup group = bean.getSelectedVigilantGroup();
        if (group != null) {
            List<VigilantWrapper> vigilantWrappers = new ArrayList<VigilantWrapper>(group.getVigilantWrappers());

            VigilantWrapper selectedVigilantWrapper = bean.getSelectedVigilantWrapper();
            if (selectedVigilantWrapper != null) {
                vigilantWrappers.remove(selectedVigilantWrapper);
                if (selectedVigilantWrapper.getPerson().hasIncompatibleVigilant()) {
                    Person incompatiblePerson = selectedVigilantWrapper.getPerson();
                    Iterator<VigilantWrapper> iterator = vigilantWrappers.iterator();

                    while (iterator.hasNext()) {
                        VigilantWrapper vigilantWrapper = iterator.next();
                        if (incompatiblePerson == vigilantWrapper.getPerson()) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            bean.setVigilants(vigilantWrappers);
        }
        request.setAttribute("bean", bean);
        RenderUtils.invalidateViewState("selectVigilant");
        return mapping.findForward("addIncompatiblePersonToVigilant");
    }

    public ActionForward addIncompatibilityToVigilant(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String vigilantId = request.getParameter("oid");
        String personId = request.getParameter("pid");
        String groupId = request.getParameter("gid");

        VigilantWrapper vigilantWrapper = (VigilantWrapper) FenixFramework.getDomainObject(vigilantId);
        Person person = (Person) FenixFramework.getDomainObject(personId);
        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(groupId);

        AddIncompatiblePerson.run(vigilantWrapper, person);

        VigilantGroupBean bean = prepareBean(getLoggedPerson(request));

        bean.setSelectedVigilantWrapper(vigilantWrapper);
        bean.setSelectedVigilantGroup(group);

        List<VigilantWrapper> vigilantWrappers = new ArrayList<VigilantWrapper>(group.getVigilantWrappersSet());
        vigilantWrappers.remove(vigilantWrapper);
        Collection<VigilantWrapper> incompatibleVigilantWrappers = person.getVigilantWrappersSet();

        for (VigilantWrapper incompatibleVigilantWrapper : incompatibleVigilantWrappers) {
            if (incompatibleVigilantWrapper.getVigilantGroup().equals(group)) {
                vigilantWrappers.remove(incompatibleVigilantWrapper);
            }
        }

        bean.setVigilants(vigilantWrappers);

        request.setAttribute("bean", bean);
        return mapping.findForward("addIncompatiblePersonToVigilant");
    }

    public ActionForward prepareBoundPropertyEdition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String groupId = request.getParameter("oid");

        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(groupId);

        List<VigilantWrapper> vigilantWrappers = new ArrayList<VigilantWrapper>(group.getVigilantWrappers());
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(VigilantWrapper.CATEGORY_COMPARATOR);
        chain.addComparator(VigilantWrapper.USERNAME_COMPARATOR);
        chain.addComparator(VigilantWrapper.NAME_COMPARATOR);
        Collections.sort(vigilantWrappers, chain);
        request.setAttribute("vigilantWrappers", vigilantWrappers);
        request.setAttribute("group", group);
        return mapping.findForward("editVigilantBounds");
    }

    public ActionForward prepareGroupPointsPropertyEdition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String groupId = request.getParameter("oid");
        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(groupId);
        request.setAttribute("group", group);
        return mapping.findForward("editVigilantGroupPoints");
    }

    public ActionForward selectPreviousPointsSchema(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = new VigilantGroupBean();
        ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();
        bean.setExamCoordinator(coordinator);
        request.setAttribute("bean", bean);

        return mapping.findForward("selectPreviousPointsSchema");
    }

    public ActionForward changeYearForPoints(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("selectVigilantGroup").getMetaObject().getObject();
        VigilantGroup group = bean.getSelectedVigilantGroup();
        request.setAttribute("group", group);
        request.setAttribute("bean", bean);

        return mapping.findForward("selectPreviousPointsSchema");
    }

    public ActionForward copySchemaPoints(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String previousGroupID = request.getParameter("selectedGroupID");
        VigilantGroup previousGroup = (VigilantGroup) FenixFramework.getDomainObject(previousGroupID);
        String groupId = request.getParameter("oid");
        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(groupId);

        group.copyPointsFromVigilantGroup(previousGroup);

        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        prepareManagementBean(request, executionYear);

        String parameter = request.getParameter("show");
        request.setAttribute("show", parameter);
        request.setAttribute("group", group);
        return mapping.findForward("manageVigilantGroups");
    }

    public ActionForward prepareStartPointsPropertyEdition(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        String groupId = request.getParameter("oid");

        VigilantGroup group = (VigilantGroup) FenixFramework.getDomainObject(groupId);

        List<VigilantWrapper> vigilantWrappers = group.getVigilantWrappersThatCanBeConvoked();
        ComparatorChain chain = new ComparatorChain();
        chain.addComparator(VigilantWrapper.CATEGORY_COMPARATOR);
        chain.addComparator(VigilantWrapper.USERNAME_COMPARATOR);
        chain.addComparator(VigilantWrapper.NAME_COMPARATOR);
        Collections.sort(vigilantWrappers, chain);
        request.setAttribute("vigilants", vigilantWrappers);
        request.setAttribute("group", group);
        return mapping.findForward("editVigilantStartPoints");
    }

    public ActionForward prepareManageVigilantsInGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person loggedPerson = getLoggedPerson(request);
        ExamCoordinator coordinator = loggedPerson.getCurrentExamCoordinator();
        Department department = getDepartment(coordinator.getUnit());

        ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        List<Employee> employees =
                department.getAllWorkingEmployees(currentYear.getBeginDateYearMonthDay(), currentYear.getEndDateYearMonthDay());

        Collection<VigilantGroup> groups = coordinator.getVigilantGroupsSet();

        List<VigilantBoundBean> bounds = new ArrayList<VigilantBoundBean>();

        for (Employee employee : employees) {
            Person employeePerson = employee.getPerson();

            for (VigilantGroup group : groups) {
                if (group.hasPerson(employeePerson) != null) {
                    bounds.add(new VigilantBoundBean(employeePerson, group, true));
                } else {
                    bounds.add(new VigilantBoundBean(employeePerson, group, false));
                }
            }
        }

        List<VigilantBoundBean> externalBounds = new ArrayList<VigilantBoundBean>();
        ArrayList<Person> persons = new ArrayList<Person>();

        for (VigilantGroup group : groups) {
            persons.addAll(group.getPersons());
        }

        for (Person person : persons) {
            if (!employees.contains(person.getEmployee())) {
                for (VigilantGroup group : groups) {
                    if (group.hasPerson(person) != null) {
                        externalBounds.add(new VigilantBoundBean(person, group, true));
                    } else {
                        externalBounds.add(new VigilantBoundBean(person, group, false));
                    }
                }
            }
        }

        VigilantGroupBean bean = new VigilantGroupBean();
        bean.setExamCoordinator(getLoggedPerson(request).getCurrentExamCoordinator());

        request.setAttribute("bean", bean);
        request.setAttribute("externalBounds", externalBounds);
        request.setAttribute("bounds", bounds);
        return mapping.findForward("editVigilantsInGroups");
    }

    public ActionForward addVigilantsToGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Map<VigilantGroup, List<Person>> peopleToAdd = new HashMap<VigilantGroup, List<Person>>();
        Map<VigilantGroup, List<VigilantWrapper>> vigilantWrappersToRemove = new HashMap<VigilantGroup, List<VigilantWrapper>>();

        List<VigilantBoundBean> beans = (List<VigilantBoundBean>) RenderUtils.getViewState("bounds").getMetaObject().getObject();
        List<VigilantBoundBean> externalBeans =
                (List<VigilantBoundBean>) RenderUtils.getViewState("externalBounds").getMetaObject().getObject();

        beans.addAll(externalBeans);

        for (VigilantBoundBean bean : beans) {
            if (bean.isBounded() && bean.getVigilantGroup().hasPerson(bean.getPerson()) == null) {
                VigilantGroup group = bean.getVigilantGroup();
                if (peopleToAdd.containsKey(group)) {
                    peopleToAdd.get(group).add(bean.getPerson());
                } else {
                    List<Person> people = new ArrayList<Person>();
                    people.add(bean.getPerson());
                    peopleToAdd.put(group, people);
                }
            } else if (!bean.isBounded()) {
                VigilantWrapper vigilantWrapper = bean.getVigilantGroup().hasPerson(bean.getPerson());

                if (vigilantWrapper != null) {
                    VigilantGroup group = bean.getVigilantGroup();
                    if (vigilantWrappersToRemove.containsKey(group)) {
                        vigilantWrappersToRemove.get(group).add(vigilantWrapper);
                    } else {
                        List<VigilantWrapper> vigilantWrappers = new ArrayList<VigilantWrapper>();
                        vigilantWrappers.add(vigilantWrapper);
                        vigilantWrappersToRemove.put(group, vigilantWrappers);
                    }
                }
            }
        }

        AddVigilantsToGroup.run(peopleToAdd);

        List<VigilantWrapper> vigilantWrappersThatCouldNotBeRemoved = RemoveVigilantsFromGroup.run(vigilantWrappersToRemove);

        request.setAttribute("vigilants", vigilantWrappersThatCouldNotBeRemoved);
        RenderUtils.invalidateViewState("bounds");
        RenderUtils.invalidateViewState("externalBounds");
        return prepareManageVigilantsInGroup(mapping, form, request, response);
    }

    public ActionForward addVigilantToGroupByUsername(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantGroupBean bean =
                (VigilantGroupBean) RenderUtils.getViewState("addExternalPersonToGroup").getMetaObject().getObject();
        Collection<VigilantGroup> groups = bean.getVigilantGroups();
        String username = bean.getUsername();

        User user = User.findByUsername(username);
        Person person;
        if (user != null && (person = user.getPerson()) != null) {
            List<Person> people = new ArrayList<Person>();
            people.add(person);
            Map<VigilantGroup, List<Person>> personToAdd = new HashMap<VigilantGroup, List<Person>>();
            for (VigilantGroup group : groups) {
                personToAdd.put(group, people);
            }

            AddVigilantsToGroup.run(personToAdd);
        } else {
            addActionMessage(request, "label.vigilancy.inexistingUsername");
        }

        return prepareManageVigilantsInGroup(mapping, form, request, response);

    }

    private VigilantGroupBean prepareBean(Person person) {
        VigilantGroupBean bean = new VigilantGroupBean();

        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        ExamCoordinator coordinator = person.getExamCoordinatorForGivenExecutionYear(executionYear);
        Unit unit = coordinator.getUnit();

        bean.setExecutionYear(executionYear);
        bean.setPerson(person);
        bean.setUnit(unit);
        bean.setExamCoordinator(coordinator);

        return bean;

    }

    private void prepareBeanForVigilantGroupEdition(HttpServletRequest request, VigilantGroup group) {

        VigilantGroupBean bean = new VigilantGroupBean();
        bean.setEmailPrefix(group.getEmailSubjectPrefix());
        bean.setSelectedVigilantGroup(group);
        bean.setConvokeStrategy(group.getConvokeStrategy());
        bean.setName(group.getName());
        bean.setUnit(group.getUnit());
        bean.setSelectedDepartment(getDepartment(group.getUnit()));
        bean.setContactEmail(group.getContactEmail());
        bean.setRulesLink(group.getRulesLink());
        bean.setEmployees(new ArrayList<Employee>());
        bean.setBeginFirstUnavailablePeriod(group.getBeginOfFirstPeriodForUnavailablePeriods());
        bean.setEndFirstUnavailablePeriod(group.getEndOfFirstPeriodForUnavailablePeriods());
        bean.setBeginSecondUnavailablePeriod(group.getBeginOfSecondPeriodForUnavailablePeriods());
        bean.setEndSecondUnavailablePeriod(group.getEndOfSecondPeriodForUnavailablePeriods());

        request.setAttribute("bean", bean);
    }

    private Department getDepartment(Unit unit) {
        if (unit.isDepartmentUnit()) {
            return unit.getDepartment();
        }
        if (unit.isScientificAreaUnit()) {
            ScientificAreaUnit scientificAreaUnit = (ScientificAreaUnit) unit;
            return scientificAreaUnit.getDepartmentUnit().getDepartment();
        }
        return null;
    }

    private void prepareManagementBean(HttpServletRequest request, ExecutionYear selectedYear) throws FenixServiceException {

        VigilantGroupBean bean = new VigilantGroupBean();
        ExecutionYear currentYear = ExecutionYear.readCurrentExecutionYear();
        bean.setExecutionYear(selectedYear);
        Person person = getLoggedPerson(request);
        ExamCoordinator coordinator = person.getExamCoordinatorForGivenExecutionYear(currentYear);
        if (coordinator != null) {
            Unit unit = coordinator.getUnit();
            List<VigilantGroup> groups = unit.getVigilantGroupsForGivenExecutionYear(selectedYear);
            bean.setUnit(unit);
            bean.setSelectedUnit(unit);
            bean.setSelectedDepartment(getDepartment(unit));
            bean.setVigilantGroups(groups);
            bean.setExamCoordinator(coordinator);
        }
        request.setAttribute("bean", bean);
    }

}