/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.cms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.cms.IExecutionCourseUserGroup;
import net.sourceforge.fenixedu.domain.cms.InvalidUserGroupTypeException;
import net.sourceforge.fenixedu.domain.cms.UserGroup;
import net.sourceforge.fenixedu.domain.cms.UserGroupTypes;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.MessageResources;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 12:10:28,29/Set/2005
 * @version $Id$
 */
public abstract class ExecutionCourseUserGroupManagement extends FenixDispatchAction
{
	public ActionForward prepareChooseExecutionPeriod(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm addGroupForm = (DynaActionForm) actionForm;
		String userGroupTypeString = (String) addGroupForm.get("userGroupType");
		UserGroupTypes userGroupType = UserGroupTypes.valueOf(userGroupTypeString);

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

		request.setAttribute("targetAction",mapping.getPath());		
		request.setAttribute("executionPeriods", infoExecutionPeriods);
		request.setAttribute("userGroupTypeToAdd", userGroupType);
		return mapping.findForward("selectOptionsFor_" + userGroupType);
	}

	public ActionForward prepareChooseExecDegreeAndCurYear(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException
	{

		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm addGroupForm = (DynaActionForm) form;
		Integer executionPeriodID = (Integer) addGroupForm.get("executionPeriodID");

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

		request.setAttribute("targetAction",mapping.getPath());
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
			StringBuffer readableName = new StringBuffer();
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

		DynaActionForm addGroupForm = (DynaActionForm) form;
		Integer executionPeriodID = (Integer) addGroupForm.get("executionPeriodID");
		Integer executionDegreeID = (Integer) addGroupForm.get("executionDegreeID");
		Integer curricularYear = (Integer) addGroupForm.get("curricularYear");

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
		try
		{
			infoExecutionCourses = (List) ServiceUtils.executeService(userView, "ReadExecutionCoursesByExecutionDegreeIdAndExecutionPeriodIdAndCurYear", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		Collections.sort(infoExecutionCourses, new BeanComparator("nome"));
		
		request.setAttribute("targetAction",mapping.getPath());
		request.setAttribute("courses", infoExecutionCourses);
		request.setAttribute("viewAction",mapping.getPath());
		return prepareChooseExecDegreeAndCurYear(mapping, form, request, response);
	}

	public abstract ActionForward viewExecutionCourseGroupElements(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
			FenixFilterException,FenixServiceException;
	
	public ActionForward createGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm addGroupForm = (DynaActionForm) form;
		
		String name = (String) addGroupForm.get("name");
		String description = (String) addGroupForm.get("description");
		Integer executionCourseID = (Integer) addGroupForm.get("executionCourseID");
		String userGroupTypeString = (String) addGroupForm.get("userGroupType");
		UserGroupTypes userGroupType = UserGroupTypes.valueOf(userGroupTypeString);
		UserGroup group = null;
		
		try
		{
			IPerson person  = this.getLoggedPerson(request);
			IExecutionCourse executionCourse = (IExecutionCourse) ServiceManagerServiceFactory.executeService(userView,"ReadDomainExecutionCourseByID",new Object[] {executionCourseID});
			Object writeArgs[] =
			{ executionCourse,name,description,person, userGroupType};			
			group = (UserGroup) ServiceUtils.executeService(userView, "WriteExecutionCourseUserGroup", writeArgs);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		
		request.setAttribute("executionCourseID",executionCourseID);
		request.setAttribute("viewAction",mapping.getPath());
		request.setAttribute("group",group);		
		return mapping.findForward("addGroup");
		
	}

}
