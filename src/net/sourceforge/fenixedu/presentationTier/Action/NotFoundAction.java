package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class NotFoundAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	response.addIntHeader("Not Found", HttpServletResponse.SC_NOT_FOUND);
	response.setContentType("text/html");
	final ServletOutputStream outputStream = response.getOutputStream();
//	final StringBuilder stringBuilder = new StringBuilder();
	outputStream.print("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"pt\" xml:lang=\"pt\">");
	outputStream.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />");
	outputStream.print("<body><h1>");
	outputStream.print(BundleUtil.getStringFromResourceBundle("resources.GlobalResources", "error.message.resource.not.found"));
	outputStream.print("</body></html>");
//	stringBuilder.toString();
	outputStream.close();
	return null;
    }

}
