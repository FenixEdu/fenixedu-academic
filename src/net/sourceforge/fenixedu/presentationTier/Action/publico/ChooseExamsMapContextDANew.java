/**
 * Project sop 
 * 
 * Package ServidorApresentacao.Action.sop
 * 
 * Created on 2/Apr/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.publico;

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

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamsMapContextDANew extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        if (session != null) {

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);

            Integer degreeId = getFromRequest("degreeID", request);
            request.setAttribute("degreeID", degreeId);

            Integer executionDegreeId = getFromRequest("executionDegreeID", request);
            request.setAttribute("executionDegreeID", executionDegreeId);

            Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

            Boolean inEnglish = getFromRequestBoolean("inEnglish", request);
            request.setAttribute("inEnglish", inEnglish);
    		request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);
    		
    		Integer indice = getFromRequest("indice",request);



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
//        // nao ocorre... pedido passa pelo filtro Autorizacao

    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        DynaActionForm chooseExamContextoForm = (DynaActionForm) form;

        SessionUtils.removeAttributtes(session, SessionConstants.CONTEXT_PREFIX);

        if (session != null) {
            ActionErrors errors = new ActionErrors();

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);

            String[] selectedCurricularYears = (String[]) chooseExamContextoForm
                    .get("selectedCurricularYears");

            Boolean selectAllCurricularYears = (Boolean) chooseExamContextoForm
                    .get("selectAllCurricularYears");

            Integer degreeId = (Integer) chooseExamContextoForm.get("degreeID");

            if ((selectAllCurricularYears != null) && selectAllCurricularYears.booleanValue()) {
                String[] allCurricularYears = { "1", "2", "3", "4", "5" };
                selectedCurricularYears = allCurricularYears;
            }

            List curricularYears = new ArrayList(selectedCurricularYears.length);
            for (int i = 0; i < selectedCurricularYears.length; i++)
                curricularYears.add(new Integer(selectedCurricularYears[i]));

            request.setAttribute("curricularYearList", curricularYears);
            Integer indexValue = getFromRequest("index", request);
            //	int index = indexValue.intValue();
            request.setAttribute("index", indexValue);

            Integer degreeCurricularPlanId = getFromRequest("degreeCurricularPlanID", request);
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

            // Integer degreeId = getFromRequest("degreeID", request);
            request.setAttribute("degreeID", degreeId);
			Integer indice = (Integer)chooseExamContextoForm.get("indice");

			List executionPeriodsLabelValueList = new ArrayList();
		    executionPeriodsLabelValueList = getList(degreeCurricularPlanId);
		    if (executionPeriodsLabelValueList.size() > 1) {			
				   request.setAttribute("lista",executionPeriodsLabelValueList);

            } else {
				   request.removeAttribute("lista");
		    }
			request.setAttribute("indice",indice);
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
            List infoExecutionDegreeList = (List) ServiceUtils.executeService(null,
                    "ReadExecutionDegreesByExecutionYearId", argsLerLicenciaturas);

            Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());

            InfoExecutionDegree infoExecutionDegree1 = new InfoExecutionDegree();
            Iterator iterator = infoExecutionDegreeList.iterator();
            while (iterator.hasNext()) {
                infoExecutionDegree1 = (InfoExecutionDegree) iterator.next();
                if (infoExecutionDegree1.getInfoDegreeCurricularPlan().getIdInternal().equals(
                        degreeCurricularPlanId)) {
                    //		request.setAttribute("infoDegreeCurricularPlan",
                    // infoExecutionDegree1);
                    break;
                }
            }

            /*
             * InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree)
             * infoExecutionDegreeList.get(index);
             */
            InfoExecutionDegree infoExecutionDegree = infoExecutionDegree1;

            //**************************************/

            Object[] args1 = {degreeId};

            List infoDegreeCurricularPlanList = null;
            try {
                infoDegreeCurricularPlanList = (List) ServiceManagerServiceFactory.executeService(null,
                        "ReadPublicDegreeCurricularPlansByDegree", args1);
            } catch (FenixServiceException e) {
                errors.add("impossibleDegreeSite", new ActionError("error.impossibleDegreeSite"));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            //order the list by state and next by begin date
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("state.degreeState"));
            comparatorChain.addComparator(new BeanComparator("initialDate"), true);

            Collections.sort(infoDegreeCurricularPlanList, comparatorChain);

            if (degreeCurricularPlanId != null) {
                Iterator iterator1 = infoDegreeCurricularPlanList.iterator();
                while (iterator1.hasNext()) {
                    InfoDegreeCurricularPlan infoDegreeCurricularPlanElem = (InfoDegreeCurricularPlan) iterator1
                            .next();

                    if (infoDegreeCurricularPlanElem.getIdInternal().equals(degreeCurricularPlanId)) {
                        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlanElem);
                        break;
                    }
                }
            }

            //***************************************/
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod
                    .getIdInternal().toString());

            if (infoExecutionDegree != null) {

                //added by rspl
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
                //-----------------
                request
                        .setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal()
                                .toString());
                RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
                /*
                 * request.setAttribute( "infoDegreeCurricularPlan",
                 * (InfoDegreeCurricularPlan)infoExecutionDegree.getInfoDegreeCurricularPlan());
                 */

            } else {

                return mapping.findForward("Licenciatura execucao inexistente");
            }

        } else
            throw new Exception();
        // nao ocorre... pedido passa pelo filtro Autorizacao

        return mapping.findForward("showExamsMap");

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

    private Boolean getFromRequestBoolean(String parameter, HttpServletRequest request) {
        Boolean parameterBoolean = null;

        String parameterCodeString = request.getParameter(parameter);
        if (parameterCodeString == null) {
            parameterCodeString = (String) request.getAttribute(parameter);
        }
        if (parameterCodeString != null) {
            try {
                parameterBoolean = new Boolean(parameterCodeString);
            } catch (Exception exception) {
                return null;
            }
        }
        return parameterBoolean;
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