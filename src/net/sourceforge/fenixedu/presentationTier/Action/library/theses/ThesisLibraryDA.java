package net.sourceforge.fenixedu.presentationTier.Action.library.theses;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.dataTransferObject.thesis.ThesisSearchBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.FenixActionForward;

public class ThesisLibraryDA extends FenixDispatchAction {

    protected String buildSearchArgs(ThesisSearchBean search) {
	StringBuffer buffer = new StringBuffer();
	if (search.getText() != null && !search.getText().isEmpty())
	    appendSearchArg(buffer, "text=" + search.getText());
	if (search.getState() != null)
	    appendSearchArg(buffer, "state=" + search.getState().getName());
	if (search.getYear() != null)
	    appendSearchArg(buffer, "year=" + search.getYear().getName());
	return buffer.toString();
    }

    private void appendSearchArg(StringBuffer buffer, String arg) {
	if (buffer.length() == 0)
	    buffer.append('&');
	else if (buffer.charAt(buffer.length() - 1) != '&') {
	    buffer.append('&');
	}
	buffer.append(arg);
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
