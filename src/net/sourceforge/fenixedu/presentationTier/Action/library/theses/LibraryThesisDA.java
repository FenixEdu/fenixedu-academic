package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.FenixActionForward;

public abstract class LibraryThesisDA extends FenixDispatchAction {

	protected List<Thesis> getUnconfirmedTheses() {
		List<Thesis> result = new ArrayList<Thesis>();
		
		for (Thesis thesis : RootDomainObject.getInstance().getTheses()) {
			if (! thesis.isFinalThesis()) {
				continue;
			}
			
			Integer mark = thesis.getMark();
			if (mark == null || mark <= 10) {
				continue;
			}
			
			if (thesis.isLibraryDetailsConfirmed()) {
				continue;
			}
			
			result.add(thesis);
		}
		
		return result;
	}

	protected Thesis getThesis(HttpServletRequest request) {
		Integer id = getIdInternal(request, "thesisID");
		
		if (id != null) {
			return RootDomainObject.getInstance().readThesisByOID(id);
		}
		
		return null;
	}

	protected List<Thesis> getTheses(HttpServletRequest request) {
		List<Integer> ids = getIdInternals(request, "thesesIDs");
		
		if (ids == null) {
			return null;
		}
	
		List<Thesis> theses = new ArrayList<Thesis>();
		for (Integer id : ids) {
			Thesis thesis = RootDomainObject.getInstance().readThesisByOID(id);
			
			if (thesis == null) {
				return Collections.emptyList();
			}
			
			theses.add(thesis);
		}
	
		return theses;
	}

	protected ActionForward forward(ActionMapping mapping, HttpServletRequest request, String name, String parameter) {
		ActionForward existing = mapping.findForward(name);
		ActionForward result = new FenixActionForward(request, existing);

		if (parameter != null) {
			String[] values = request.getParameterValues(parameter);
		
			if (values == null) {
				return result;
			}
			
			StringBuilder path = new StringBuilder(existing.getPath());
			for (int i = 0; i < values.length; i++) {
				path.append(String.format("&%s=%s", parameter, values[i]));
			}
			
			result.setPath(path.toString());
		}
		
		return result;
	}

	protected List<Thesis> getOnlyConfirmedTheses() {
		List<Thesis> result = new ArrayList<Thesis>();
		
		for (Thesis thesis : RootDomainObject.getInstance().getTheses()) {
			if (! thesis.isLibraryDetailsConfirmed()) {
				continue;
			}
			
			if (thesis.isLibraryDetailsExported()) {
				continue;
			}
			
			result.add(thesis);
		}
		return result;
	}

	protected List<Thesis> getExportedTheses() {
		List<Thesis> result = new ArrayList<Thesis>();
		
		for (Thesis thesis : RootDomainObject.getInstance().getTheses()) {
			if (! thesis.isLibraryDetailsConfirmed()) {
				continue;
			}
			
			if (! thesis.isLibraryDetailsExported()) {
				continue;
			}
			
			result.add(thesis);
		}
		return result;
	}

	
}
