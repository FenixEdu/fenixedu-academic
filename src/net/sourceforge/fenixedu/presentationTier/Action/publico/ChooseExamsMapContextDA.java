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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ChooseExamsMapContextDA extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                .getAttribute(SessionConstants.EXECUTION_PERIOD);
        List curricularYearsList = new ArrayList();
        curricularYearsList.add("1");
        curricularYearsList.add("2");
        curricularYearsList.add("3");
        curricularYearsList.add("4");
        curricularYearsList.add("5");
        curricularYearsList.add("6");
        request.setAttribute("curricularYearList", curricularYearsList);
        /* Cria o form bean com as licenciaturas em execucao. */
        Object argsLerLicenciaturas[] = { infoExecutionPeriod.getInfoExecutionYear() };

        List executionDegreeList = (List) ServiceUtils.executeService(null,
                "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);

        Collections.sort(executionDegreeList, new ComparatorByNameForInfoExecutionDegree());

        List licenciaturas = new ArrayList();

        licenciaturas.add(new LabelValueBean("escolher", ""));

        Iterator iterator = executionDegreeList.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) iterator.next();
            String name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome();

            name = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getTipoCurso()
                    .toString()
                    + " de " + name;

            name += duplicateInfoDegree(executionDegreeList, infoExecutionDegree) ? "-"
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "";

            licenciaturas.add(new LabelValueBean(name, String.valueOf(index++)));
        }

        request.setAttribute("degreeList", licenciaturas);

        return mapping.findForward("chooseExamsMapContext");
    }

    public ActionForward choose(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        DynaActionForm chooseExamContextoForm = (DynaActionForm) form;

        SessionUtils.removeAttributtes(session, SessionConstants.CONTEXT_PREFIX);

        if (session != null) {

            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) request
                    .getAttribute(SessionConstants.EXECUTION_PERIOD);
			
            String[] selectedCurricularYears = (String[]) chooseExamContextoForm
                    .get("selectedCurricularYears");
         
			Integer degreeId = (Integer) chooseExamContextoForm.get("degreeID");		
            Boolean selectAllCurricularYears = (Boolean) chooseExamContextoForm
                    .get("selectAllCurricularYears");

            if ((selectAllCurricularYears != null) && selectAllCurricularYears.booleanValue()) {
                String[] allCurricularYears = { "1", "2", "3", "4", "5" };
                selectedCurricularYears = allCurricularYears;
            }

            List curricularYears = new ArrayList(selectedCurricularYears.length);
            for (int i = 0; i < selectedCurricularYears.length; i++)
                curricularYears.add(new Integer(selectedCurricularYears[i]));

            request.setAttribute("curricularYearList", curricularYears);

         //   int index = Integer.parseInt((String) chooseExamContextoForm.get("index"));

            Object argsLerLicenciaturas[] = { infoExecutionPeriod.getIdInternal(),degreeId };

//            List infoExecutionDegreeList = (List) ServiceUtils.executeService(null,
//                    "ReadExecutionDegreesByExecutionYear", argsLerLicenciaturas);
			List infoExecutionDegreeList = (List) ServiceUtils.executeService(null,
							  "ReadExecutionDegreesByDegreeAndExecutionPeriod", argsLerLicenciaturas);
			
            Collections.sort(infoExecutionDegreeList, new ComparatorByNameForInfoExecutionDegree());

//            InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList
//                    .get(index);
//            /*********************************/
//			InfoExecutionDegree infoExecutionDegree1 = new InfoExecutionDegree();
//					  Iterator iterator = infoExecutionDegreeList.iterator();
//					  while (iterator.hasNext()) {
//						  infoExecutionDegree1 = (InfoExecutionDegree) iterator.next();
//						  if (infoExecutionDegree1.getInfoDegreeCurricularPlan().getIdInternal().equals(
//								  degreeCurricularPlanId)) {
//							  break;
//						  }
//					  }
//
//					  InfoExecutionDegree infoExecutionDegree = infoExecutionDegree1;
//            /*********************************/
//                

			InfoExecutionDegree infoExecutionDegree = null;        
            if (infoExecutionDegreeList.size() == 1) {
				infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(0);
				request.setAttribute("degreeCurricularPlanID", infoExecutionDegree.getInfoDegreeCurricularPlan().getIdInternal());
            }
            	
           
            request.setAttribute("degreeID", degreeId);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD, infoExecutionPeriod);
            request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, infoExecutionPeriod
                    .getIdInternal().toString());
           RequestUtils.setExecutionDegreeToRequest(request,infoExecutionDegree);

            if (infoExecutionDegree != null) {

                //added by rspl
                request.setAttribute(SessionConstants.EXECUTION_DEGREE, infoExecutionDegree);
                //-----------------
                request
                        .setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal()
                                .toString());
                RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
            } else {
								infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(0);
				request.setAttribute(SessionConstants.EXECUTION_DEGREE_LIST, infoExecutionDegreeList);		
//                return mapping.findForward("Licenciatura execucao inexistente");
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
}