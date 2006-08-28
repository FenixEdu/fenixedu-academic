package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.PrintAllCandidatesFilter;
import net.sourceforge.fenixedu.util.PrintAllCandidatesFilterType;
import net.sourceforge.fenixedu.util.SituationName;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Clerigo
 * @author Telmo Nabais
 *
 */
public class PrintAllCandidatesListDispatchAction extends FenixDispatchAction {

	// request parameters	
	public static final String REQUEST_FILTERNEXTSELECTOPTIONS = "filterNextSelectOptions";
	public static final String REQUEST_FILTERSELECTOPTIONS = "filterSelectOptions";
	public static final String REQUEST_FILTERBY = "filterBy";
	public static final String REQUEST_CHOSEN_FILTER = "chosenFilter";	
	public static final String REQUEST_FILTERWITHVALUE = "filterWithValue";
	public static final String REQUEST_DEGREE_CURRICULAR_ID = "degreeCurricularPlanID";
	public static final String REQUEST_EXPORT = "export";
	
	// nome dos metodos [parametro method da action]
	public static final String METHOD_PREPARE = "prepare";
	public static final String METHOD_PRINTALLCANDIDATES = "printAllCandidates";
	
	// CSV
	public final String HEADER_H1 = "\t\t\t\t\t\tListagem de candidatos\t\t";
	public final String HEADER_H2 = "Numero de candidatura\tNome do candidato\tEscola de candidatura\tLicenciatura\tEspecialização\tSituação\tPretende dar aulas\tTelefone\tE-Mail";

	private static final Object STR_ISCOURSEASSISTANT = "Sim";
	private static final Object STR_ISNTCOURSEASSISTANT = "Não";
	private static final Object STR_NOREMARKS = "Não tem";
	
	private boolean reqExportToExcel = false;
	//////

	/**	Prepara os dados a serem apresentados */
    public ActionForward prepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setAttribute(REQUEST_FILTERSELECTOPTIONS,PrintAllCandidatesFilterType.getMainFiltersAsList());
		if (request.getParameter(REQUEST_DEGREE_CURRICULAR_ID)!=null)
			request.setAttribute(REQUEST_DEGREE_CURRICULAR_ID,request.getParameter("degreeCurricularPlanID"));
		return mapping.findForward("Prepare");
    }
	
	/** prepara o segundo filtro */
    public ActionForward prepareSecondFilter(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintAllCandidatesFilter filterBy = PrintAllCandidatesFilter.INVALID_FILTER;
		int degreeCurricularPlanID = 0;

		DynaActionForm f = (DynaActionForm)form;
		String reqFilterBy = (String) f.get(REQUEST_FILTERBY);
		String reqDegreeCurricularId = request.getParameter(REQUEST_DEGREE_CURRICULAR_ID);
		
		if (request.getParameter(REQUEST_EXPORT)!=null)
			reqExportToExcel = true;
		else reqExportToExcel = false;

        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);

		// parse do valor do filtro e do dcpID
		if (reqFilterBy!=null)
		{
			try {
				filterBy = PrintAllCandidatesFilter.valueOf(reqFilterBy);
				// voltar a coloca-lo no request
				request.setAttribute(REQUEST_FILTERBY, filterBy);
			} catch (Exception e) { filterBy = PrintAllCandidatesFilter.INVALID_FILTER; }
		}
		else return mapping.findForward("Prepare");

		// parse do valor do degreecurricularid
		if (reqDegreeCurricularId!=null)
		{
			try {
				degreeCurricularPlanID = Integer.parseInt(reqDegreeCurricularId);
				// voltar a coloca-lo no request
				request.setAttribute(REQUEST_DEGREE_CURRICULAR_ID, reqDegreeCurricularId);
			} catch (Exception e) { }
		}
		else return mapping.findForward("Prepare");

		// colocar o nome do primeiro filtro no request
		request.setAttribute(REQUEST_CHOSEN_FILTER, new String(PrintAllCandidatesFilterType.getFilterNameByValue(filterBy)));

		// aplicar o filtro [ou por os novos valores no request caso necessario]
		switch (filterBy)
		{
			/** filtros de especializacao */
			case FILTERBY_SPECIALIZATION_VALUE: 
				request.setAttribute(REQUEST_FILTERNEXTSELECTOPTIONS,PrintAllCandidatesFilterType.getSpecializationFiltersAsList());
				return mapping.findForward("Prepare");

			/** filtros de situacao */
			case FILTERBY_SITUATION_VALUE:
				request.setAttribute(REQUEST_FILTERNEXTSELECTOPTIONS,PrintAllCandidatesFilterType.getSituationNameFiltersAsList());
				return mapping.findForward("Prepare");
			
			/** filtro de pretende dar aulas - o ultimo parametro e' indiferente */
			case FILTERBY_GIVESCLASSES_VALUE:
		        return callServiceListAllCandidatesAndForward(userView, request, response, mapping, degreeCurricularPlanID, filterBy, null, reqExportToExcel);

			/** filtro de nao pretende dar aulas - o ultimo parametro e' indiferente  */
			case FILTERBY_DOESNTGIVESCLASSES_VALUE:
		        return callServiceListAllCandidatesAndForward(userView, request, response, mapping, degreeCurricularPlanID, filterBy, null, reqExportToExcel);
		    
			case INVALID_FILTER:
		    	return callServiceListAllCandidatesAndForward(userView,request, response, mapping,degreeCurricularPlanID,PrintAllCandidatesFilter.INVALID_FILTER,null, reqExportToExcel);
		}
		
		return mapping.findForward("Prepare");
    }
	
	/** Prepara a folha que imprime a lista de todos os candidatos */
    public ActionForward printAllCandidatesList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

    	PrintAllCandidatesFilter filterBy = PrintAllCandidatesFilter.INVALID_FILTER;
    	String filterValue = null;
    	int degreeCurricularID = 0;
    	
		DynaActionForm f = (DynaActionForm)form;
		String reqFilterWithValue = (String) f.get(REQUEST_FILTERWITHVALUE);
		String reqFilterBy = request.getParameter(REQUEST_FILTERBY);
		String reqDegreeCurricularId = request.getParameter(REQUEST_DEGREE_CURRICULAR_ID);
		
		if (request.getParameter(REQUEST_EXPORT)!=null)
			reqExportToExcel = true;
		else reqExportToExcel = false;

		HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);

        try {
        	filterBy = PrintAllCandidatesFilter.valueOf(reqFilterBy);
        	
        	if (filterBy==null)
        		filterBy = PrintAllCandidatesFilter.INVALID_FILTER;
        	
        	/** no caso do situation, o array labelvalue tem [string.string] **/
        	if (filterBy == PrintAllCandidatesFilter.FILTERBY_SITUATION_VALUE)
        	{
        		filterValue = new SituationName(reqFilterWithValue).toString();
        	}
        	if (filterBy == PrintAllCandidatesFilter.FILTERBY_SPECIALIZATION_VALUE)
        	{
        		filterValue = reqFilterWithValue;
        	}
        	degreeCurricularID = Integer.parseInt(reqDegreeCurricularId);
        } catch (Exception e) { filterBy = PrintAllCandidatesFilter.INVALID_FILTER; }
        
        return callServiceListAllCandidatesAndForward(userView, request, response, mapping, degreeCurricularID, filterBy, filterValue, reqExportToExcel);
    }

    /** lista todos os candidatos :: listagem ou csv **/
	private ActionForward callServiceListAllCandidatesAndForward(
			IUserView userView, HttpServletRequest request, 
			HttpServletResponse response, ActionMapping mapping, int degreeCurricularID, 
			PrintAllCandidatesFilter filterBy, String filterValue, boolean exportToCSV) 
	throws FenixActionException, IOException {
        List candidates = null;

        Object args[] = { new Integer(degreeCurricularID), filterBy, filterValue };

        try {
            candidates = (List) ServiceManagerServiceFactory.executeService(userView,
                    "ReadDegreeCandidatesWithFilter", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        } catch (FenixFilterException e) {
            throw new FenixActionException(e);
		}

        // por os valores no request
        request.setAttribute("filteredBy",PrintAllCandidatesFilterType.getFilterNameByValue(filterBy));
        request.setAttribute("candidatesList",candidates);
        
        if (exportToCSV)
        {
        	String text = exportToCSV(candidates);

            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            writer.print(text);
            writer.flush();
            response.flushBuffer();
            return null;
        }
        
		return mapping.findForward("PrintList");
	}

	private String exportToCSV(List candidates) {
		StringBuilder sb = new StringBuilder();
		sb.append(HEADER_H1);
		sb.append("\n");
		sb.append(HEADER_H2);
		sb.append("\n");
		for (Object obj: candidates) {
			InfoMasterDegreeCandidate ic = (InfoMasterDegreeCandidate)obj;
		
			sb.append(ic.getCandidateNumber());
			sb.append("\t");
			sb.append(ic.getInfoPerson().getNome());
			sb.append("\t");
			sb.append(ic.getMajorDegreeSchool());
			sb.append("\t");
			sb.append(ic.getMajorDegree());
			sb.append("\t");
			sb.append(ic.getSpecialization());
			sb.append("\t");
			sb.append("Data: ");
			sb.append(ic.getInfoCandidateSituation().getDate());
			sb.append(" - Obs: ");
			if (ic.getInfoCandidateSituation().getRemarks()==null) sb.append(STR_NOREMARKS);
			else sb.append(ic.getInfoCandidateSituation().getRemarks());
			sb.append(" - Situacao: ");
			sb.append(ic.getInfoCandidateSituation().getSituation());
			sb.append("\t");
			if (ic.getCourseAssistant()) sb.append(STR_ISCOURSEASSISTANT);
			else sb.append(STR_ISNTCOURSEASSISTANT);			
			sb.append("\t");
			sb.append(ic.getInfoPerson().getTelefone());
			sb.append("\t");
			sb.append(ic.getInfoPerson().getEmail());			
			sb.append("\n");
		}
		return sb.toString();
	}
}