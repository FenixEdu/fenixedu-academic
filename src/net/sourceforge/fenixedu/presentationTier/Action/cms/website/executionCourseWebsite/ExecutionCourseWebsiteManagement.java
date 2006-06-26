/**
 * 
 */


package net.sourceforge.fenixedu.presentationTier.Action.cms.website.executionCourseWebsite;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.website.WriteExecutionCourseWebsite;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.website.WriteExecutionCourseWebsite.ExecutionCourseAlreadyHasWebsiteException;
import net.sourceforge.fenixedu.applicationTier.Servico.cms.website.WriteExecutionCourseWebsite.WriteExecutionCourseWebsiteParameters;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.cms.website.Website;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>  <br/> <br/> <br/>
 * Created on 15:26:21,6/Dez/2005
 * @version $Id$
 */
public class ExecutionCourseWebsiteManagement extends FenixDispatchAction
{
	public ActionForward viewAll(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{
		try
		{
			IUserView userView = SessionUtils.getUserView(request);
			Collection<Website> websites = rootDomainObject.readAllDomainObjects(Website.class);
			Collection<InfoExecutionPeriod> infoExecutionPeriods = (Collection<InfoExecutionPeriod>) ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);
			if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty())
			{
				// exclude closed execution periods
				infoExecutionPeriods = (Collection<InfoExecutionPeriod>) CollectionUtils.select(infoExecutionPeriods, new Predicate()
				{

					public boolean evaluate(Object input)
					{
						InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;
						if (!infoExecutionPeriod.getState().equals(PeriodState.CLOSED))
						{
							return true;
						}
						return false;
					}
				});

				ComparatorChain comparator = new ComparatorChain();
				comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
				comparator.addComparator(new BeanComparator("name"), true);

				request.setAttribute("websites", websites);
				request.setAttribute("executionPeriods", infoExecutionPeriods);
			}
		}

		catch (Exception e)
		{
			throw new FenixActionException(e);
		}

		return mapping.findForward("showlAllExecutionCourseWebsites");
	}

	public ActionForward prepareChooseExecutionPeriod(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{
		IUserView userView = SessionUtils.getUserView(request);

		List infoExecutionPeriods = null;

		try
		{
			infoExecutionPeriods = (List) ServiceUtils.executeService(userView, "ReadExecutionPeriods", null);

		}
		catch (FenixServiceException ex)
		{
			throw new FenixActionException();
		}
		if (infoExecutionPeriods != null && !infoExecutionPeriods.isEmpty())
		{
			// exclude closed execution periods
			infoExecutionPeriods = (List) CollectionUtils.select(infoExecutionPeriods, new Predicate()
			{

				public boolean evaluate(Object input)
				{
					InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) input;
					if (!infoExecutionPeriod.getState().equals(PeriodState.CLOSED))
					{
						return true;
					}
					return false;
				}
			});

			ComparatorChain comparator = new ComparatorChain();
			comparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
			comparator.addComparator(new BeanComparator("name"), true);
			Collections.sort(infoExecutionPeriods, comparator);
		}

		request.setAttribute("executionPeriods", infoExecutionPeriods);
		return this.viewAll(mapping, actionForm, request, response);
	}

	public ActionForward prepareChooseExecDegreeAndCurYear(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{

		IUserView userView = SessionUtils.getUserView(request);
		ExecutionCourseWebSiteManagementForm websiteForm = (ExecutionCourseWebSiteManagementForm) form;
		Integer executionPeriodID = websiteForm.getExecutionPeriodID();

		if (executionPeriodID == null)
		{
			ActionErrors errors = new ActionErrors();
			errors.add("error", new ActionError("error.no.executionPeriod"));
			saveErrors(request, errors);
			return prepareChooseExecutionPeriod(mapping, form, request, response);
		}

		Object args[] =
		{ executionPeriodID };
		List executionDegreeList = null;
		try
		{
			executionDegreeList = (List) ServiceUtils.executeService(userView, "ReadExecutionDegreesByExecutionPeriodId", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		addReadableInformation(executionDegreeList, request);
		Collections.sort(executionDegreeList, new BeanComparator("qualifiedName"));

		request.setAttribute("executionPeriodID", executionPeriodID);
		request.setAttribute("degrees", executionDegreeList);
		return this.prepareChooseExecutionPeriod(mapping, form, request, response);
	}

	/**
	 * @param executionDegreeList
	 */
	private void addReadableInformation(List executionDegreeList, HttpServletRequest request)
	{

		MessageResources resources = this.getResources(request, "ENUMERATION_RESOURCES");

		Iterator<InfoExecutionDegree> iterator = executionDegreeList.iterator();
		while (iterator.hasNext())
		{
			InfoExecutionDegree infoExecutionDegree = iterator.next();
			StringBuilder readableName = new StringBuilder();
			String degreeType = resources.getMessage(this.getLocale(request), infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso().toString());
			readableName.append(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
			String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();
			name = degreeType + " em " + name;
			name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
					+ infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";
			infoExecutionDegree.setQualifiedName(name);

		}
	}

	private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree)
	{

		InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
		Iterator iterator = executionDegreeList.iterator();
		while (iterator.hasNext())
		{
			InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
			if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
					&& !(infoExecutionDegree.equals(infoExecutionDegree2))) return true;
		}
		return false;
	}

	public ActionForward prepareChooseExecutionCourse(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{

		ExecutionCourseWebSiteManagementForm websiteForm = (ExecutionCourseWebSiteManagementForm) form;
		Integer executionPeriodID = websiteForm.getExecutionPeriodID();
		Integer executionDegreeID = websiteForm.getExecutionDegreeID();
		Integer curricularYear = websiteForm.getCurricularYear();

		IUserView userView = SessionUtils.getUserView(request);

		if (executionDegreeID == null)
		{
			ActionErrors errors = new ActionErrors();
			errors.add("error", new ActionError("error.no.executionDegree"));
			saveErrors(request, errors);
			return prepareChooseExecDegreeAndCurYear(mapping, form, request, response);
		}
		if (curricularYear.equals(new Integer(0)))
		{
			ActionErrors errors = new ActionErrors();
			errors.add("error", new ActionError("error.no.curYear"));
			saveErrors(request, errors);
			return prepareChooseExecDegreeAndCurYear(mapping, form, request, response);
		}

		Object args[] =
		{ executionDegreeID, executionPeriodID, curricularYear };
		List infoExecutionCourses;
        Collection websiteTypes;
		try
		{
			infoExecutionCourses = (List) ServiceUtils.executeService(userView, "ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear", args);
            websiteTypes = rootDomainObject.getWebsiteTypesSet();
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
       
		request.setAttribute("courses", infoExecutionCourses);
		request.setAttribute("viewAction", mapping.getPath());
        request.setAttribute("websiteTypes", websiteTypes);
        
		return prepareChooseExecDegreeAndCurYear(mapping, form, request, response);
	}

	public ActionForward createWebsite(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{
		try
		{
			IUserView userView = SessionUtils.getUserView(request);
			ExecutionCourseWebSiteManagementForm websiteForm = (ExecutionCourseWebSiteManagementForm) form;
			WriteExecutionCourseWebsiteParameters parameters = new WriteExecutionCourseWebsite.WriteExecutionCourseWebsiteParameters();

			parameters.setDescription(websiteForm.getDescription());
			parameters.setName(websiteForm.getName());
			parameters.setExecutionCourseID(websiteForm.getExecutionCourseID());
			parameters.setPerson(userView.getPerson());
            parameters.setWebsiteTypeID(websiteForm.getWebsiteTypeID());
            
			ServiceUtils.executeService(userView, "WriteExecutionCourseWebsite", new Object[]
			{ parameters });

			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.executionCourseWebsiteManagement.addWebsite.success.label"));
			saveMessages(request, messages);

		}
		catch (ExecutionCourseAlreadyHasWebsiteException e)
		{
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.executionCourseWebsiteManagement.addWebsite.alreadyExists.error.label"));
			saveErrors(request, messages);
		}
		catch (Exception e)
		{
			throw new FenixActionException(e);
		}

		return this.viewAll(mapping, form, request, response);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException
	{

		try
		{
			IUserView userView = SessionUtils.getUserView(request);
			Integer websiteId = new Integer(request.getParameter("websiteId"));
			Website websiteToDelete = (Website) rootDomainObject.readContentByOID(websiteId);
			if (websiteToDelete != null)
			{
				ServiceUtils.executeService(userView, "DeleteWebsite", new Object[]
				{ websiteToDelete });
			}

			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.executionCourseWebsiteManagement.deleteWebsite.success.label"));
			saveMessages(request, messages);
		}
		catch (Exception e)
		{
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.executionCourseWebsiteManagement.deleteWebsite.error.label"));
			saveErrors(request, messages);
			e.printStackTrace();
		}

		return this.viewAll(mapping, form, request, response);
	}
	
	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException
	{
		try
		{
			Integer websiteId = new Integer(request.getParameter("websiteId"));
			Website websiteToView = (Website) rootDomainObject.readContentByOID(websiteId);
			request.setAttribute("website",websiteToView);
		}
		catch (Exception e)
		{
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("cms.executionCourseWebsiteManagement.viewWebsite.error.label"));
			saveErrors(request, messages);
			e.printStackTrace();
		}
		return null;
	}
}
