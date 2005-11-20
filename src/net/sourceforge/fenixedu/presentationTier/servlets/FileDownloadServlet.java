package net.sourceforge.fenixedu.presentationTier.servlets;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.fileManager.RetrieveItemFile;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

public class FileDownloadServlet extends HttpServlet {

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		final String fileName = request.getParameter("fileName");
		final String itemCodeString = request.getParameter("itemCode");
		final Integer itemCode = Integer.valueOf(itemCodeString);

		final IUserView userView = SessionUtils.getUserView(request);

		try {
			final String slidename = (String) ServiceUtils.executeService(
					userView, "ReadItemSlideName", new Object[] { itemCode });
			final FileSuportObject file = RetrieveItemFile.run(slidename,
					fileName);

			response.setHeader("Content-disposition", "attachment;filename="
					+ file.getFileName());
			response.setContentType(file.getContentType());

			final DataOutputStream dos = new DataOutputStream(response
					.getOutputStream());
			dos.write(file.getContent());
			dos.close();
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

}
