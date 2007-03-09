/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.thesis;

import java.io.IOException;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.thesis.ListMasterDegreeProofsBean;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ListMasterDegreeThesisDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException,
	    IOException {

	ListMasterDegreeProofsBean bean = (ListMasterDegreeProofsBean) getRenderedObject();
	if (bean == null) {
	    bean = new ListMasterDegreeProofsBean();
	} else {
	    Collection<MasterDegreeThesis> masterDegreeThesisCollection = (Collection<MasterDegreeThesis>) ServiceUtils
		    .executeService(SessionUtils.getUserView(request), "ReadActiveMasterDegreeThesis",
			    new Object[] { bean.getThesisState(), bean.getYear(), bean.getDegree() });

	    if (bean.getGenerateFile()) {
		request.setAttribute("chooseDegreeAndYearBean", bean);
		return generateFile(response, masterDegreeThesisCollection);
	    }

	    request.setAttribute("masterDegreeThesisCollection", masterDegreeThesisCollection);
	}

	request.setAttribute("chooseDegreeAndYearBean", bean);
	return mapping.findForward("showList");
    }

    private ActionForward generateFile(HttpServletResponse response,
	    Collection<MasterDegreeThesis> masterDegreeThesisCollection) throws IOException {
	Formatter resultFormatter = new Formatter();
	for (MasterDegreeThesis thesis : masterDegreeThesisCollection) {
	    resultFormatter.format("%d\t%s\tFénix\t", thesis.getStudentCurricularPlan()
		    .getRegistration().getNumber(), thesis.getStudentCurricularPlan()
		    .getDegreeCurricularPlan().getDegree().getNome());

	    List<Teacher> guiders = thesis.getActiveMasterDegreeThesisDataVersion().getGuiders();
	    if (!guiders.isEmpty()) {
		for (Teacher teacher : guiders) {
		    resultFormatter.format("%s\t", teacher.getPerson().getNome());
		}
	    } else {
		List<ExternalContract> externalGuiders = thesis.getActiveMasterDegreeThesisDataVersion()
			.getExternalGuiders();
		if (!externalGuiders.isEmpty()) {
		    for (ExternalContract externalPerson : externalGuiders) {
			resultFormatter.format("%s\t", externalPerson.getPerson().getNome());
		    }
		}
	    }

	    resultFormatter.format("%s\n", thesis.getActiveMasterDegreeThesisDataVersion()
		    .getDissertationTitle());
	}

	response.setHeader("Content-disposition", "attachment;filename=masterDegreeThesisList.txt");
	response.setContentType("application/txt");
	response.getWriter().write(resultFormatter.toString());
	response.getWriter().close();

	return null;
    }

}
