/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.common
 * 
 * Created on 8/Jan/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.dataTransferObject.CurricularYearAndSemesterAndInfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.base.FenixDateAndTimeDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author jpvl
 */
public class ChooseContextDispatchActionNew extends FenixDateAndTimeDispatchAction {

    protected static final String INFO_DEGREE_INITIALS_PARAMETER = "degreeInitials";

    protected static final String SEMESTER_PARAMETER = "semester";

    protected static final String CURRICULAR_YEAR_PARAMETER = "curricularYear";

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.actions.DispatchAction#dispatchMethod(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.String)
     */
    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {

            String inputPage = request.getParameter(SessionConstants.INPUT_PAGE);
            String nextPage = request.getParameter(SessionConstants.NEXT_PAGE);
            if (inputPage != null)
                request.setAttribute(SessionConstants.INPUT_PAGE, inputPage);
            if (nextPage != null)
                request.setAttribute(SessionConstants.NEXT_PAGE, nextPage);

            IUserView userView = SessionUtils.getUserView(request);

            InfoExecutionPeriod infoExecutionPeriod = setExecutionContext(request);

			Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
			request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

			Integer degreeId = getFromRequest("degreeID", request);
			request.setAttribute("degreeID", degreeId);

			List executionPeriodsLabelValueList = new ArrayList();
			executionPeriodsLabelValueList = getList(degreeCurricularPlanId);
			if (executionPeriodsLabelValueList.size() > 1) {	
					request.setAttribute("lista",executionPeriodsLabelValueList);

			} else {
					request.removeAttribute("lista");
			}
			/*------------------------------------*/
          
                return mapping.findForward("prepare");
          

        }

        throw new Exception();
        // nao ocorre... pedido passa pelo filtro Autorizacao

    }

    public ActionForward preparePublic(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String inputPage = request.getParameter(SessionConstants.INPUT_PAGE);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        //TODO: this semester and curricular year list needs to be refactored
        // in order to incorporate masters
        /* Criar o bean de semestres */
        List semestres = new ArrayList();
        semestres.add(new LabelValueBean("escolher", ""));
        semestres.add(new LabelValueBean("1 �", "1"));
        semestres.add(new LabelValueBean("2 �", "2"));
        request.setAttribute("semestres", semestres);

        /* Criar o bean de anos curricutares */
        List anosCurriculares = new ArrayList();
        anosCurriculares.add(new LabelValueBean("escolher", ""));
        anosCurriculares.add(new LabelValueBean("1 �", "1"));
        anosCurriculares.add(new LabelValueBean("2 �", "2"));
        anosCurriculares.add(new LabelValueBean("3 �", "3"));
        anosCurriculares.add(new LabelValueBean("4 �", "4"));
        anosCurriculares.add(new LabelValueBean("5 �", "5"));
        request.setAttribute("curricularYearList", anosCurriculares);

        /* Cria o form bean com as licenciaturas em execucao. */
        Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

        List executionDegreeList = (List) ServiceUtils.executeService(null,
                "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

        List licenciaturas = new ArrayList();

        licenciaturas.add(new LabelValueBean("escolher", ""));

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        Iterator iterator = executionDegreeList.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                    .toString()
                    + " em " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
        }

        request.setAttribute("degreeList", licenciaturas);

        if (inputPage != null) {
            return mapping.findForward(inputPage);
        }

        // TODO : throw a proper exception
        throw new Exception("SomeOne is messing around with the links");

    }

    public ActionForward nextPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        DynaActionForm escolherContextoForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        //SessionUtils.removeAttributtes(session,
        // SessionConstants.CONTEXT_PREFIX);

        String nextPage = (String) request.getAttribute(SessionConstants.NEXT_PAGE);
        if (nextPage == null) {
            nextPage = request.getParameter(SessionConstants.NEXT_PAGE);
        }

        if (session != null) {
            Integer semestre = ((InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD)).getSemester();
            Integer anoCurricular = (Integer) escolherContextoForm.get("curricularYear");

            int index = Integer.parseInt((String) escolherContextoForm.get("index"));

            request.setAttribute("anoCurricular", anoCurricular);
            request.setAttribute("semestre", semestre);

            //List infoExecutionDegreeList = (List)
            // session.getAttribute(SessionConstants.INFO_EXECUTION_DEGREE_LIST_KEY);
            Object argsLerLicenciaturas[] = { ((InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD)).getInfoExecutionYear() };
            List infoExecutionDegreeList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);
            List licenciaturas = new ArrayList();
            licenciaturas.add(new LabelValueBean("escolher", ""));
            Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());
            //////

            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList
                    .get(index);

            if (infoExecutionDegree != null) {
                CurricularYearAndSemesterAndInfoExecutionDegree cYSiED = new CurricularYearAndSemesterAndInfoExecutionDegree(
                        anoCurricular, semestre, infoExecutionDegree);
                request.setAttribute(SessionConstants.CONTEXT_KEY, cYSiED);

                request.setAttribute(SessionConstants.CURRICULAR_YEAR_KEY, anoCurricular);
                request.setAttribute(SessionConstants.CURRICULAR_YEAR_OID, anoCurricular.toString());
                request.setAttribute(SessionConstants.INFO_EXECUTION_DEGREE_KEY, infoExecutionDegree);
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
                request.setAttribute(SessionConstants.EXECUTION_DEGREE_OID, infoExecutionDegree
                        .getIdInternal().toString());
            } else {
                return mapping.findForward("Licenciatura execucao inexistente");
            }

            //String nextPage = (String)
            // request.getAttribute(SessionConstants.NEXT_PAGE);
            if (nextPage != null) {
                return mapping.findForward(nextPage);
            }

            // TODO : throw a proper exception
            throw new Exception("SomeOne is messing around with the links");
        }

        throw new Exception();
        // nao ocorre... pedido passa pelo filtro Autorizacao

    }

    public ActionForward nextPagePublic(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {
        HttpSession session = request.getSession(false);
        DynaActionForm escolherContextoForm = (DynaActionForm) form;
		ActionErrors errors = new ActionErrors();
        SessionUtils.removeAttributtes(session, SessionConstants.CONTEXT_PREFIX);

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);

        Integer semestre = infoExecutionPeriod.getSemester();
        Integer anoCurricular = (Integer) escolherContextoForm.get("curYear");

        Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);
        
		List executionPeriodsLabelValueList = new ArrayList();
		try {
		executionPeriodsLabelValueList = getList(degreeCurricularPlanId);
		}catch(Exception e) {
			throw new FenixActionException(e);
		}
		
		if (executionPeriodsLabelValueList.size() > 1) {			
			   request.setAttribute("lista",executionPeriodsLabelValueList);

		} else {
			   request.removeAttribute("lista");
		}
        

        Integer degreeId = getFromRequest("degreeID", request);
        request.setAttribute("degreeID", degreeId);
        Integer index = null;
   /*  if (escolherContextoForm.get("index") != null
                && !escolherContextoForm.get("index").equals("null")) {

            index = new Integer((String) escolherContextoForm.get("index"));
        } else {
            if (request.getParameter("index") != null && !request.getParameter("index").equals("null")) {

                index = new Integer(request.getParameter("index"));
            } else {
                index = new Integer(0);
            }
    }*/

        request.setAttribute("curYear", anoCurricular);
        request.setAttribute("semester", semestre);
        		Integer indice = (Integer)escolherContextoForm.get("indice");
		if (indice==null)
			indice = (Integer)getFromRequest("indice", request);
		request.setAttribute("indice",indice);
		escolherContextoForm.set("indice",indice);
		Object argsLerLicenciaturas[] =null;
		if (indice != null) {
					InfoExecutionPeriod infoExecutionPeriod2 = new InfoExecutionPeriod();
		Object args[] = {indice};
			try {
				infoExecutionPeriod2 = (InfoExecutionPeriod )ServiceManagerServiceFactory.executeService(null,
				"ReadExecutionPeriodByOID", args);
			}catch(FenixServiceException e){
				errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
				saveErrors(request, errors);
				return (new ActionForward(mapping.getInput()));
			}
			 infoExecutionPeriod = infoExecutionPeriod2;
			 RequestUtils.setExecutionPeriodToRequest(request,infoExecutionPeriod2);
			 argsLerLicenciaturas= new Object[]{infoExecutionPeriod2.getInfoExecutionYear().getIdInternal()};
		}else {
			 argsLerLicenciaturas= new Object[]{infoExecutionPeriod.getInfoExecutionYear().getIdInternal()};
		}




        //Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

        List infoExecutionDegreeList;
        try {
            infoExecutionDegreeList = (List) ServiceUtils.executeService(null,
                    "ReadExecutionDegreesByExecutionYearId", argsLerLicenciaturas);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        /*
         * InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree)
         * infoExecutionDegreeList .get(index.intValue());
         */

        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        Iterator iterator = infoExecutionDegreeList.iterator();
        while (iterator.hasNext()) {
            infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            if (infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal().equals(
                    degreeCurricularPlanId)) {
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
                request
                        .setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal()
                                .toString());
                request.setAttribute("infoDegreeCurricularPlan", infoExecutionDegree);
                break;
            }
        }

        if (infoExecutionDegree == null) {
            return mapping.findForward("Licenciatura execucao inexistente");
        }

        RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
		request.setAttribute(SessionConstants.INFO_DEGREE_CURRICULAR_PLAN, infoExecutionDegree
					   .getInfoDegreeCurricularPlan());
		request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
		request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod
				.getIdInternal().toString());
        String nextPage = request.getParameter("nextPage");
        if (nextPage != null) {
            return mapping.findForward(nextPage);
        }

        // TODO : throw a proper exception
        throw new FenixActionException("SomeOne is messing around with the links");

    }

    /**
     * Method existencesOfInfoDegree.
     * 
     * @param executionDegreeList
     * @param infoExecutionDegree
     * @return int
     */
    private boolean duplicateInfoDegree(List executionDegreeList, InfoExecutionDegree infoExecutionDegree) {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();
        Iterator iterator = executionDegreeList.iterator();

        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree2 = (InfoExecutionDegree) iterator.next();
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;

        }
        return false;
    }

    /**
     * Method setExecutionContext.
     * 
     * @param request
     */
    // TODO When session is removed from SOP, use method with same name from
    // RequestUtils
    private InfoExecutionPeriod setExecutionContext(HttpServletRequest request) throws Exception {

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY);
        if (infoExecutionPeriod == null) {
            IUserView userView = SessionUtils.getUserView(request);
            infoExecutionPeriod = (InfoExecutionPeriod) ServiceUtils.executeService(userView,
                    "ReadCurrentExecutionPeriod", new Object[0]);

            request.setAttribute(SessionConstants.INFO_EXECUTION_PERIOD_KEY, infoExecutionPeriod);
        }
        return infoExecutionPeriod;
    }

    private Integer getFromRequest(String parameter, HttpServletRequest request) {
        Integer parameterCode = null;
        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null) {
            try {
                parameterCode = new Integer(parameterCodeString);
            } catch (Exception exception) {
                return null;
            }
        }
        return parameterCode;
    }
	private List getList(Integer degreeCurricularPlanId) throws Exception{
		  /*------------------------------------*/      
				  Object argsLerLicenciaturas[] = {degreeCurricularPlanId};
				  InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

				  List infoExecutionDegreeList = new ArrayList();
				  try {
					  infoExecutionDegreeList = (List) ServiceUtils.executeService(null,
								"ReadPublicExecutionDegreeByDCPID", argsLerLicenciaturas);
				  } catch (FenixServiceException e) {
					  throw new Exception(e);
				  }

				  List executionPeriodsLabelValueList = new ArrayList();
				  ComparatorChain comparatorChain = new ComparatorChain();
				  comparatorChain.addComparator(new BeanComparator("infoExecutionYear.idInternal"));
		
				  Collections.sort(infoExecutionDegreeList, comparatorChain);
				  Collections.reverse(infoExecutionDegreeList);
				  for (int i = 0; i < infoExecutionDegreeList.size(); i++) {
					  infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(i);
					
					  InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
					  infoExecutionYear.setIdInternal(infoExecutionDegree.getInfoExecutionYear().getIdInternal());
					  Object args[] = {infoExecutionYear};
					  List infoExecutionPeriodsList = new ArrayList();
					  try {
						  infoExecutionPeriodsList = (List) ServiceUtils.executeService(null,
							   "ReadNotClosedPublicExecutionPeriodsByExecutionYear", args);
					  } catch (FenixServiceException e) {
						  throw new Exception(e);
					  }				 
					  for (int j = 0; j < infoExecutionPeriodsList.size(); j++) {
						  InfoExecutionPeriod infoExecutionPeriod1 = (InfoExecutionPeriod) infoExecutionPeriodsList.get(j);
						  executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionPeriod1.getName() + " - "
						  + infoExecutionPeriod1.getInfoExecutionYear().getYear(), "" + infoExecutionPeriod1.getIdInternal()));
				     	
					  }
				  }
			  return executionPeriodsLabelValueList;
	  }
}