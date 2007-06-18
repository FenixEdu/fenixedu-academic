package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.publication.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.FenixActionForward;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class LibraryThesisDA extends FenixDispatchAction {

	private static abstract class ThesesFilter {
		
		public List<Thesis> filter() {
			List<Thesis> list = new ArrayList<Thesis>();
			
			for (ResearchResult result  : RootDomainObject.getInstance().getResults()) {
				if (! (result instanceof Thesis)) {
					continue;
				}
				
				Thesis thesis = (Thesis) result;
				if (! thesis.isInternalThesis()) {
					continue;
				}
				
				if (isAcceptable(thesis)) {
					list.add(thesis);
				}
			}
			
			return list;
		}

		protected abstract boolean isAcceptable(Thesis thesis);
		
	}
	
	protected List<Thesis> getUnconfirmedTheses() {
		return new ThesesFilter() {

			@Override
			protected boolean isAcceptable(Thesis thesis) {
				return !thesis.isLibraryDetailsConfirmed();
			}
			
		}.filter();
	}


	protected List<Thesis> getOnlyConfirmedTheses() {
		return new ThesesFilter() {

			@Override
			protected boolean isAcceptable(Thesis thesis) {
				return thesis.isLibraryDetailsConfirmed() && !thesis.isLibraryDetailsExported();
			}
			
		}.filter();
	}

	protected List<Thesis> getExportedTheses() {
		return new ThesesFilter() {

			@Override
			protected boolean isAcceptable(Thesis thesis) {
				return thesis.isLibraryDetailsConfirmed() && thesis.isLibraryDetailsExported();
			}
			
		}.filter();
	}

	protected Thesis getThesis(HttpServletRequest request) {
		Integer id = getIdInternal(request, "thesisID");
		
		if (id != null) {
			return (Thesis) RootDomainObject.getInstance().readResearchResultByOID(id);
		}
		
		return null;
	}

	protected List<Thesis> getTheses(HttpServletRequest request) {
		List<Integer> ids = getIdInternals(request, "thesesIDs");
		
		if (ids == null) {
			return Collections.emptyList();
		}
	
		List<Thesis> theses = new ArrayList<Thesis>();
		for (Integer id : ids) {
			Thesis thesis = (Thesis) RootDomainObject.getInstance().readResearchResultByOID(id);
			
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
	
}
