package net.sourceforge.fenixedu.presentationTier.Action;

import java.io.PrintWriter;
import java.nio.charset.Charset;

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
        response.setContentType("text/html; charset=" + Charset.defaultCharset().name());
        final PrintWriter writer = response.getWriter();
        writer.print("<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"pt\" xml:lang=\"pt\">");
        writer.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=" + Charset.defaultCharset().name()
                + "\" />");
        writer.print("<body><h1>");
        writer.print(BundleUtil.getStringFromResourceBundle("resources.GlobalResources", "error.message.resource.not.found"));
        writer.print("</body></html>");
        writer.close();
        return null;
    }

}
