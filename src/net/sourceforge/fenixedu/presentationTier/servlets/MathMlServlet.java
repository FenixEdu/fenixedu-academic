package net.sourceforge.fenixedu.presentationTier.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.tests.NewMathMlMaterial;
import net.sourceforge.jeuclid.util.Converter;

import org.apache.avalon.framework.logger.NullLogger;
import org.apache.tools.ant.filters.StringInputStream;

public class MathMlServlet extends HttpServlet {

	RootDomainObject rootDomainObject = RootDomainObject.getInstance();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.process(request, response);
	}

	protected void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String oid = request.getParameter("oid");

		if (oid == null) {
			return;
		}

		int mathMlMaterialId = Integer.parseInt(oid);

		NewMathMlMaterial mathMlMaterial = (NewMathMlMaterial) rootDomainObject
				.readNewPresentationMaterialByOID(mathMlMaterialId);

		response.setContentType("image/gif");

		InputStream inputStream = new StringInputStream(mathMlMaterial.getMathMl());

		OutputStream outputStream = response.getOutputStream();

		Converter.convert(inputStream, outputStream, Converter.TYPE_GIF, new NullLogger());
	}

}
