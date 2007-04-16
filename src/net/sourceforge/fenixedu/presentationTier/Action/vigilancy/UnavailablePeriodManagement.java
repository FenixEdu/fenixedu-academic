package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

public class UnavailablePeriodManagement extends FenixDispatchAction {

    private Vigilant getVigilantForCurrentYear(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        Person person = getLoggedPerson(request);
        Vigilant vigilant = person.getCurrentVigilant();
        return vigilant;
    }

    public ActionForward addUnavailablePeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Vigilant vigilant = getVigilantForCurrentYear(request);
        UnavailablePeriodBean bean = new UnavailablePeriodBean();
        bean.setVigilant(vigilant);

        request.setAttribute("bean", bean);
        return mapping.findForward("addUnavailablePeriod");

    }

    public ActionForward createUnavailablePeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnavailablePeriodBean periodBean = (UnavailablePeriodBean) RenderUtils.getViewState(
                "createUnavailablePeriod").getMetaObject().getObject();
        Vigilant vigilant = periodBean.getVigilant();

        try {
            Object[] args = { vigilant, periodBean.getBeginDate(), periodBean.getEndDate(),
                    periodBean.getJustification() };
            executeService(request, "CreateUnavailablePeriod", args);
        } catch (DomainException e) {
        	request.setAttribute("bean",periodBean);
        	addActionMessage(request, e.getMessage(), null);
            return mapping.findForward("addUnavailablePeriod");           
        }

        putRequestVigilantManagementCompliant(request, vigilant);
        return mapping.findForward("addedUnavailablePeriod");

    }

    public ActionForward editUnavailablePeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	prepareForEdit(request);
        return mapping.findForward("editUnavailablePeriod");
    }

    public ActionForward deleteUnavailablePeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Vigilant vigilant = getVigilantForCurrentYear(request);
        deletePeriod(request);
        putRequestVigilantManagementCompliant(request, vigilant);
        return mapping.findForward("deleteUnavailablePeriod");
    }

    public ActionForward changeUnavailablePeriod(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnavailablePeriodBean periodBean = (UnavailablePeriodBean) RenderUtils.getViewState()
                .getMetaObject().getObject();
        Vigilant vigilant = periodBean.getVigilant();
        try {
        applyChangesToUnavailablePeriod(request, periodBean.getIdInternal(), periodBean.getBeginDate(),
                periodBean.getEndDate(), periodBean.getJustification());
        }catch(DomainException e) {
        	request.setAttribute("bean",periodBean);
        	addActionMessage(request, e.getMessage(), null);
            return mapping.findForward("editUnavailablePeriod");
        }
        putRequestVigilantManagementCompliant(request, vigilant);
        return mapping.findForward("addedUnavailablePeriod");
    }

    public ActionForward changeUnavailablePeriodForVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnavailablePeriodBean periodBean = (UnavailablePeriodBean) RenderUtils.getViewState()
                .getMetaObject().getObject();
        try {
        applyChangesToUnavailablePeriod(request, periodBean.getIdInternal(), periodBean.getBeginDate(),
                periodBean.getEndDate(), periodBean.getJustification());
        } catch(DomainException e) {
        	String gid =  request.getParameter("gid");
        	request.setAttribute("gid", gid);
        	request.setAttribute("bean", periodBean);
        	addActionMessage(request, e.getMessage(), null);
            return mapping.findForward("editPeriodOfVigilant");
        }
        String gid = request.getParameter("gid");
        VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, Integer.valueOf(gid));
        VigilantGroupBean bean = new VigilantGroupBean();
    	ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();
    	bean.setExamCoordinator(coordinator);
    	bean.setSelectedVigilantGroup(group);
    	putUnavailablePeriodsOnRequest(request, group);
    	request.setAttribute("bean",bean);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");
    }

    public ActionForward prepareManageUnavailablePeriodsOfVigilants(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

    	VigilantGroupBean bean = new VigilantGroupBean();
    	ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();
    	bean.setExamCoordinator(coordinator);
    	request.setAttribute("bean",bean);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");

    }
    
    public ActionForward manageUnavailablePeriodsOfVigilants(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

    	VigilantGroupBean bean = (VigilantGroupBean) RenderUtils.getViewState("selectVigilantGroup").getMetaObject().getObject();
    	VigilantGroup group = bean.getSelectedVigilantGroup();
    	if(group!=null) {
    		putUnavailablePeriodsOnRequest(request,group);
    	}
        request.setAttribute("bean",bean);
    	return mapping.findForward("manageUnavailablePeriodsOfVigilants");

    }

    public ActionForward editUnavailablePeriodOfVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        prepareForEdit(request);
        String gid = request.getParameter("gid");
        request.setAttribute("gid", gid);
        return mapping.findForward("editPeriodOfVigilant");
    }

    public ActionForward deleteUnavailablePeriodOfVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        deletePeriod(request);
        VigilantGroupBean bean = new VigilantGroupBean();
    	ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();
    	bean.setExamCoordinator(coordinator);
    	String gid = request.getParameter("gid");
    	VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, Integer.valueOf(gid));
    	bean.setSelectedVigilantGroup(group);
    	putUnavailablePeriodsOnRequest(request, group);
    	request.setAttribute("bean",bean);
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");

    }

    public ActionForward prepareAddPeriodToVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

    	String gid = request.getParameter("gid");
    	VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, Integer.valueOf(gid));
    	
        UnavailablePeriodBean bean = new UnavailablePeriodBean();
        Person person = getLoggedPerson(request);
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        ExamCoordinator coordinator = person.getExamCoordinatorForGivenExecutionYear(executionYear);
        bean.setCoordinator(coordinator);
        bean.setSelectedVigilantGroup(group);
        
        request.setAttribute("gid",gid);
        request.setAttribute("bean", bean);
        return mapping.findForward("prepareAddPeriodToVigilant");

    }

    public ActionForward addUnavailablePeriodToVigilant(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        UnavailablePeriodBean bean = (UnavailablePeriodBean) RenderUtils.getViewState("periodCreation")
                .getMetaObject().getObject();

        try {
        	
        Object[] args = { bean.getVigilant(), bean.getBeginDate(), bean.getEndDate(),
                bean.getJustification(), bean.getSelectedVigilantGroup() };
        executeService(request, "CreateUnavailablePeriod", args);
        }catch(DomainException e) {
        	String gid =  request.getParameter("gid");
        	request.setAttribute("gid", gid);
        	request.setAttribute("bean", bean);
        	addActionMessage(request, e.getMessage(), null);
            return mapping.findForward("prepareAddPeriodToVigilant");
        }
        
        
        VigilantGroupBean beanToPutOnRequest = new VigilantGroupBean();
    	String gid = request.getParameter("gid");
    	
        VigilantGroup group = (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, Integer.valueOf(gid)); 
        ExamCoordinator coordinator = getLoggedPerson(request).getCurrentExamCoordinator();
    	beanToPutOnRequest.setExamCoordinator(coordinator);
    	beanToPutOnRequest.setSelectedVigilantGroup(group);
    	putUnavailablePeriodsOnRequest(request, group);
    	request.setAttribute("bean",beanToPutOnRequest);
        
        return mapping.findForward("manageUnavailablePeriodsOfVigilants");
    }

    private void prepareForEdit(HttpServletRequest request) {
        String id = request.getParameter("oid");
        int idInternal = Integer.valueOf(id);

        UnavailablePeriod unavailablePeriod = (UnavailablePeriod) RootDomainObject
                .readDomainObjectByOID(UnavailablePeriod.class, idInternal);
        UnavailablePeriodBean bean = new UnavailablePeriodBean();
        bean.setIdInternal(idInternal);
        bean.setBeginDate(unavailablePeriod.getBeginDate());
        bean.setEndDate(unavailablePeriod.getEndDate());
        bean.setJustification(unavailablePeriod.getJustification());
        bean.setVigilant(unavailablePeriod.getVigilant());

        request.setAttribute("bean", bean);
    }

    private void deletePeriod(HttpServletRequest request) throws FenixFilterException,
            FenixServiceException {
        String id = request.getParameter("oid");
        Integer idInternal = Integer.valueOf(id);

        try {
            Object[] args = { idInternal };
            executeService(request, "DeleteUnavailablePeriodByOID", args);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
        }
    }

    private void putUnavailablePeriodsOnRequest(HttpServletRequest request, VigilantGroup group) throws Exception {
    	request.setAttribute("unavailablePeriods", group.getUnavailablePeriodsOfVigilantsInGroup());
    }

    private void applyChangesToUnavailablePeriod(HttpServletRequest request, Integer idInternal,
            DateTime begin, DateTime end, String justification) throws Exception {
    		Object[] args = { idInternal, begin, end, justification };
            executeService(request, "EditUnavailablePeriod", args);
    }

    private void putRequestVigilantManagementCompliant(HttpServletRequest request, Vigilant vigilant) {
        VigilantBean bean = new VigilantBean();
        ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        bean.setExecutionYear(executionYear);

        VigilantGroup group = getGroupInRequestOrFirstGroupFromVigilant(request, vigilant);
        bean.setSelectedVigilantGroup(group);
        bean.setVigilantGroups(vigilant.getVigilantGroups());
        bean.setShowUnavailables(true);
        request.setAttribute("bean", bean);
        request.setAttribute("vigilant", vigilant);
    }

    private VigilantGroup getGroupInRequestOrFirstGroupFromVigilant(HttpServletRequest request,
            Vigilant vigilant) {
        String groupId = request.getParameter("gid");
        return (groupId == null) ? vigilant.getVigilantGroups().get(0)
                : (VigilantGroup) RootDomainObject.readDomainObjectByOID(VigilantGroup.class, Integer
                        .valueOf(groupId));
    }

}