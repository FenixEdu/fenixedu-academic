package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.RequestUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.Util;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

/**
 * @author tfc130
 */
public class PrepareConsultCurricularPlanDispatchAction extends FenixContextDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

        DynaActionForm indexForm = (DynaActionForm) form;

        request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);
        Integer degreeCurricularPlanId = (Integer) request.getAttribute("degreeCurricularPlanID");
        if (degreeCurricularPlanId == null)
            degreeCurricularPlanId = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Integer degreeId = (Integer) request.getAttribute("degreeID");
        if (degreeId == null)
            degreeId = Integer.valueOf(request.getParameter("degreeID"));
        request.setAttribute("degreeID", degreeId);
        
//        String language = getLocaleLanguageFromRequest(request);
        
        Integer index = (Integer) indexForm.get("index");
        
        request.setAttribute("index", index);
        indexForm.set("index", index);

	 		request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);   

		   /* Criar o bean de anos curricutares */
			 List anosCurriculares = new ArrayList();
			 anosCurriculares.add(new LabelValueBean("---------", ""));
			 anosCurriculares.add(new LabelValueBean("1", "1"));
			 anosCurriculares.add(new LabelValueBean("2", "2"));
			 anosCurriculares.add(new LabelValueBean("3", "3"));
			 anosCurriculares.add(new LabelValueBean("4", "4"));
			 anosCurriculares.add(new LabelValueBean("5", "5"));
			 
			 request.setAttribute("curricularYearList", anosCurriculares);
			 
			 
		   /*------------------------------------*/      
		  	 Object argsLerLicenciaturas[] = {degreeCurricularPlanId};
						   
			   
			 
			  InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();

			   List infoExecutionDegreeList = new ArrayList();
			   try {
				  infoExecutionDegreeList = (List) ServiceUtils.executeService(null,
						   "ReadPublicExecutionDegreeByDCPID", argsLerLicenciaturas);
			   } catch (FenixServiceException e) {
				   throw new FenixActionException(e);
			   }

				List executionPeriodsLabelValueList = new ArrayList();
				InfoExecutionDegree infoExecutionDegree1 = (InfoExecutionDegree) infoExecutionDegreeList.get(0);
				//MEU
//                InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
//                infoDegreeCurricularPlan = infoExecutionDegree1.getInfoDegreeCurricularPlan();
//                infoDegreeCurricularPlan.prepareEnglishPresentation(language);
//                infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
                //
                
                
				executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionDegree1.getInfoExecutionYear().getYear(), "" + infoExecutionDegree1.getInfoExecutionYear().getIdInternal()));
				
      
                for (int i = 1; i < infoExecutionDegreeList.size(); i++) {
					 infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegreeList.get(i);
      		 
					 
					 if (infoExecutionDegree.getInfoExecutionYear().getYear()!= infoExecutionDegree1.getInfoExecutionYear().getYear()) {
                         executionPeriodsLabelValueList.add(new LabelValueBean(infoExecutionDegree.getInfoExecutionYear().getYear(), "" + infoExecutionDegree.getInfoExecutionYear().getIdInternal()));
					   infoExecutionDegree1 = (InfoExecutionDegree) infoExecutionDegreeList.get(i);
					 }
				 }

				 if (executionPeriodsLabelValueList.size() > 1) {
					 request.setAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD,
							 executionPeriodsLabelValueList);

				 } else {
					 request.removeAttribute(SessionConstants.LABELLIST_EXECUTIONPERIOD);
				 }
				 /*------------------------------------*/

            
				 // If executionPeriod was previously selected,form has that value as
				 // default
				 InfoExecutionPeriod selectedExecutionPeriod = (InfoExecutionPeriod) request
						 .getAttribute(SessionConstants.EXECUTION_PERIOD);

				 if (selectedExecutionPeriod != null) {
 
					 indexForm.set("indice", selectedExecutionPeriod.getInfoExecutionYear().getIdInternal());
					 indexForm.set("curYear",new Integer(anosCurriculares.indexOf(anosCurriculares.get(0))));
		//)
					 request.setAttribute(SessionConstants.EXECUTION_PERIOD, selectedExecutionPeriod);
					 request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID, selectedExecutionPeriod
								.getIdInternal().toString());
				 }
				 //----------------------------------------------------------
        	    Object arg[] = {degreeCurricularPlanId,(Integer)indexForm.get("indice")};
        	
				try {
						 infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(null,
								  "ReadPublicExecutionDegreeByDCPID", arg);
					if (infoExecutionDegree == null) {
						List infoExecutionDegrees = new ArrayList();
						Object arg1[] = {degreeCurricularPlanId};
						try {
						  infoExecutionDegrees = (List) ServiceUtils.executeService(null,
								   "ReadPublicExecutionDegreeByDCPID", arg1);
		
							if (infoExecutionDegrees.size() >= 1) {
								infoExecutionDegree= (InfoExecutionDegree) infoExecutionDegrees.get(infoExecutionDegrees.size()-1);
								indexForm.set("indice", infoExecutionDegree.getInfoExecutionYear().getIdInternal());
							}   	
								
						
						} catch (FenixServiceException e1) {
		
							return mapping.findForward("Sucess");
						}
					}
					  } catch (FenixServiceException e) {
					  	
						
						  throw new FenixActionException(e);
					  }
 	
        	RequestUtils.setExecutionDegreeToRequest(request,infoExecutionDegree);
            //TODO: No futuro, os edificios devem ser lidos da BD
            List buildings = Util.readExistingBuldings("*", null);
            request.setAttribute("publico.buildings", buildings);

            //TODO: No futuro, os tipos de salas devem ser lidos da BD
            List types = new ArrayList();
            types.add(new LabelValueBean("*", null));
            types.add(new LabelValueBean("Anfiteatro", (new Integer(TipoSala.ANFITEATRO)).toString()));
            types.add(new LabelValueBean("Laborat�rio", (new Integer(TipoSala.LABORATORIO)).toString()));
            types.add(new LabelValueBean("Plana", (new Integer(TipoSala.PLANA)).toString()));
            request.setAttribute("publico.types", types);

            ActionForward actionForward = mapping.findForward("Sucess");
            return actionForward;
       // }
       // throw new Exception();
    }

    public ActionForward select(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm escolherContextoForm = (DynaActionForm) form;
        HttpSession session = request.getSession(true);
        SessionUtils.removeAttributtes(session, SessionConstants.CONTEXT_PREFIX);
        InfoExecutionPeriod infoExecutionPeriod = new InfoExecutionPeriod();
        
        Integer executionYear = (Integer) escolherContextoForm.get("indice");

        InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
        infoExecutionYear.setIdInternal(executionYear);
        infoExecutionPeriod.setInfoExecutionYear(infoExecutionYear);

        Integer semestre = infoExecutionPeriod.getSemester();
        Integer degreeCurricularPlanId = (Integer) request.getAttribute("degreeCurricularPlanID");
        if (degreeCurricularPlanId == null)
            degreeCurricularPlanId = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        Integer degreeId = (Integer) request.getAttribute("degreeID");
        if (degreeId == null)
            degreeId = Integer.valueOf(request.getParameter("degreeID"));

        request.setAttribute("degreeID", degreeId);

        Integer index = (Integer) escolherContextoForm.get("index");
        Integer curricularYear = (Integer) escolherContextoForm.get("curYear");
        request.setAttribute("index", index);
        request.setAttribute("curYear", curricularYear);
        request.setAttribute("semester", semestre);
        escolherContextoForm.set("index", index);

        Object args[] = { infoExecutionYear };
        List infoExecutionPeriodList;
        try {
            infoExecutionPeriodList = (List) ServiceUtils.executeService(null,
                    "ReadExecutionPeriodsByExecutionYear", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
     
        request.setAttribute(SessionConstants.EXECUTION_PERIOD,
                infoExecutionPeriodList.get(0));
        request.setAttribute(SessionConstants.EXECUTION_PERIOD_OID,
                ((InfoExecutionPeriod) infoExecutionPeriodList.get(0)).getIdInternal().toString());
        RequestUtils.setExecutionPeriodToRequest(request, (InfoExecutionPeriod) infoExecutionPeriodList
                .get(0));

        //----------------------------------------------------------
        InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
        Object arg[] = { degreeCurricularPlanId, executionYear };

        try {
            infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(null,
                    "ReadPublicExecutionDegreeByDCPID", arg);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
       
    //    request.setAttribute("windowLocation",FenixCacheFilter.getPageURL(request));
        RequestUtils.setExecutionDegreeToRequest(request, infoExecutionDegree);
        return mapping.findForward("Sucess");

    }

}
