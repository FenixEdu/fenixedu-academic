package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class VigilantManagement extends FenixDispatchAction {

    public ActionForward prepareMap(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantBean bean = new VigilantBean();
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        prepareBean(bean, request, executionYear);

        return mapping.findForward("displayConvokeMap");
    }

    public ActionForward displayMap(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        VigilantBean bean = (VigilantBean) RenderUtils.getViewState("selectGroup").getMetaObject()
                .getObject();

        prepareBean(bean, request, bean.getExecutionYear());

        RenderUtils.invalidateViewState("selectGroup");
        return mapping.findForward("displayConvokeMap");
    }

    public ActionForward vigilantAcceptsConvoke(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String id = request.getParameter("oid");
        Integer idInternal = Integer.valueOf(id);

        Object[] args = { idInternal };
        executeService(request, "ConfirmConvokeByOID", args);

        Person person = getLoggedPerson(request);
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        Vigilant vigilant = person.getVigilantForGivenExecutionYear(executionYear);
        VigilantGroup group = getGroupFromRequestOrVigilant(request, vigilant);
        VigilantBean bean = new VigilantBean();
        bean.setExecutionYear(executionYear);
        bean.setSelectedVigilantGroup(group);
        bean.setVigilantGroups(vigilant.getVigilantGroups());
        request.setAttribute("vigilant", vigilant);
        request.setAttribute("bean", bean);

        return mapping.findForward("displayConvokeMap");
    }

    public ActionForward prepareEditIncompatiblePerson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Vigilant vigilant = getVigilantForGivenYear(request, ExecutionYear.readCurrentExecutionYear());

        List<VigilantGroup> groups;
        groups = (vigilant != null) ? vigilant.getVigilantGroups() : new ArrayList<VigilantGroup>();
        Set<Vigilant> vigilants = new HashSet<Vigilant>();
        for (VigilantGroup group : groups) {
            vigilants.addAll(group.getVigilants());
        }

        vigilants.remove(vigilant);
        Vigilant incompatibleVigilant = vigilant.getIncompatibleVigilant();

        if (incompatibleVigilant != null)
            vigilants.remove(incompatibleVigilant);

        request.setAttribute("vigilants", new ArrayList<Vigilant>(vigilants));
        request.setAttribute("vigilant", vigilant);
        return mapping.findForward("prepareEditIncompatiblePerson");
    }

    public ActionForward addIncompatiblePerson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Person person = getLoggedPerson(request);
        String oid = request.getParameter("oid");
        Integer idInternal = Integer.valueOf(oid);

        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        Vigilant vigilant = person.getVigilantForGivenExecutionYear(currentExecutionYear);
        Person incompatiblePerson = (Person) RootDomainObject.readDomainObjectByOID(Person.class,
                idInternal);

        Object[] args = { vigilant, incompatiblePerson };
        executeService(request, "AddIncompatiblePerson", args);

        List<VigilantGroup> groups;
        groups = (vigilant != null) ? vigilant.getVigilantGroups() : new ArrayList<VigilantGroup>();
        Set<Vigilant> vigilants = new HashSet<Vigilant>();
        for (VigilantGroup group : groups) {
            vigilants.addAll(group.getVigilants());
        }
        vigilants.remove(vigilant);
        vigilants.remove(incompatiblePerson.getVigilantForGivenExecutionYear(currentExecutionYear));
        request.setAttribute("vigilants", new ArrayList<Vigilant>(vigilants));
        request.setAttribute("vigilant", vigilant);
        return mapping.findForward("prepareEditIncompatiblePerson");
    }

    public ActionForward removeIncompatiblePerson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Person person = getLoggedPerson(request);

        Vigilant vigilant = person.getVigilantForGivenExecutionYear(ExecutionYear
                .readCurrentExecutionYear());
        Object[] args = { vigilant };
        executeService(request, "RemoveIncompatiblePerson", args);

        List<VigilantGroup> groups;
        groups = (vigilant != null) ? vigilant.getVigilantGroups() : new ArrayList<VigilantGroup>();
        Set<Vigilant> vigilants = new HashSet<Vigilant>();
        for (VigilantGroup group : groups) {
            vigilants.addAll(group.getVigilants());
        }
        vigilants.remove(vigilant);

        request.setAttribute("vigilants", new ArrayList<Vigilant>(vigilants));
        request.setAttribute("vigilant", vigilant);
        return mapping.findForward("prepareEditIncompatiblePerson");
    }

    private void prepareBean(VigilantBean bean, HttpServletRequest request, ExecutionYear executionYear)
            throws FenixFilterException, FenixServiceException {
        Vigilant vigilant = getVigilantForGivenYear(request, executionYear);

        bean.setExecutionYear(executionYear);
        VigilantGroup selectedVigilantGroup = bean.getSelectedVigilantGroup();
        if (selectedVigilantGroup != null
                && !selectedVigilantGroup.getExecutionYear().equals(executionYear)) {
            bean.setSelectedVigilantGroup(null);
        }

        if (vigilant != null) {
            List<VigilantGroup> groups = vigilant.getVigilantGroups();
            bean.setVigilantGroups(groups);
            if (groups.size() == 1) {
                VigilantGroup group = groups.get(0);
                bean.setSelectedVigilantGroup(group);
            }
        } else {
            bean.setVigilantGroups(new ArrayList<VigilantGroup>());
        }
        request.setAttribute("vigilant", vigilant);
        request.setAttribute("bean", bean);
    }

    private Vigilant getVigilantForGivenYear(HttpServletRequest request, ExecutionYear executionYear)
            throws FenixFilterException, FenixServiceException {
        Person person = getLoggedPerson(request);
        Vigilant vigilant = person.getVigilantForGivenExecutionYear(executionYear);
        return vigilant;
    }

    private VigilantGroup getGroupFromRequestOrVigilant(HttpServletRequest request, Vigilant vigilant) {
        String groupId = request.getParameter("gid");
        return (groupId == null) ? vigilant.getVigilantGroups().get(0)
                : (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, Integer
                        .valueOf(groupId));
    }

}