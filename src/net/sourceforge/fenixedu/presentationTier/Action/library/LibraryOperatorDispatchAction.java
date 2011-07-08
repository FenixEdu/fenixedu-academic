package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceAttendances;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.CategoryItemRenderer;
import org.jfree.data.DefaultCategoryDataset;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/libraryOperator", module = "library")
@Forwards({ @Forward(name = "libraryOperator", path = "/library/operator/libraryOperator.jsp") })
public class LibraryOperatorDispatchAction extends FenixDispatchAction {
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("attendance", new LibraryAttendance());
	return mapping.findForward("libraryOperator");
    }

    public ActionForward selectLibrary(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	LibraryAttendance attendance = getRenderedObject("attendance");
	RenderUtils.invalidateViewState();
	request.setAttribute("attendance", attendance);
	return mapping.findForward("libraryOperator");
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	LibraryAttendance attendance = getRenderedObject("search.person");
	if (attendance == null) {
	    String personId = request.getParameter("personIstUsername");
	    Space library = AbstractDomainObject.fromExternalId(request.getParameter("libraryId"));
	    attendance = new LibraryAttendance(personId, library);
	}
	if (!StringUtils.isEmpty(attendance.getPersonId())) {
	    if (attendance.getPersonId().startsWith("ist")) {
		attendance.setPerson(Person.readPersonByIstUsername(attendance.getPersonId()));
	    } else {
		attendance.setPerson(Person.readPersonByLibraryCardNumber(attendance.getPersonId()));
	    }
	} else {
	    attendance.setPerson(null);
	}

	request.setAttribute("attendance", attendance);
	return mapping.findForward("libraryOperator");
    }

    public ActionForward generateCardNumber(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	LibraryAttendance attendance = getRenderedObject("person.edit.libraryCardNumber");
	RenderUtils.invalidateViewState();
	attendance.generateCardNumber();
	request.setAttribute("attendance", attendance);
	return mapping.findForward("libraryOperator");
    }

    public ActionForward saveCardNumber(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	LibraryAttendance attendance = getRenderedObject("person.edit.libraryCardNumber");
	RenderUtils.invalidateViewState();
	attendance.saveCardNumber();
	request.setAttribute("attendance", attendance);
	return mapping.findForward("libraryOperator");
    }

    public ActionForward exitPlace(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	LibraryAttendance attendance = getRenderedObject("person.selectPlace");
	if (attendance == null) {
	    SpaceAttendances spaceAttendance = AbstractDomainObject.fromExternalId(request.getParameter("attendanceId"));
	    Space library = AbstractDomainObject.fromExternalId(request.getParameter("libraryId"));
	    attendance = new LibraryAttendance(spaceAttendance, library);
	}
	RenderUtils.invalidateViewState();
	attendance.exitSpace();
	request.setAttribute("attendance", attendance);
	return mapping.findForward("libraryOperator");
    }

    public ActionForward selectPlace(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	LibraryAttendance attendance = getRenderedObject("person.selectPlace");
	RenderUtils.invalidateViewState();
	if (attendance.getSelectedSpace() != null) {
	    attendance.enterSpace();
	}
	request.setAttribute("attendance", attendance);
	return mapping.findForward("libraryOperator");
    }

    public ActionForward createAreaXYChart(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	Space library = getDomainObject(request, "library");

	int occupation = 0;
	for (Space space : library.getContainedSpacesSet()) {
	    if (space.hasCurrentAttendance()) {
		occupation++;
	    }
	}

	final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	dataset.addValue(occupation, "Alunos", "Ocupação");

	final JFreeChart chart = ChartFactory.createBarChart3D(null, null, "Value", dataset, PlotOrientation.HORIZONTAL, false,
		false, false);

	chart.setBackgroundPaint(Color.WHITE);

	final CategoryPlot plot = chart.getCategoryPlot();
	plot.setRangeAxisLocation(AxisLocation.TOP_OR_RIGHT);

	final CategoryItemRenderer renderer1 = plot.getRenderer();

	if (occupation >= library.getSpaceInformation().getCapacity()) {
	    renderer1.setSeriesPaint(0, Color.RED);
	} else {
	    renderer1.setSeriesPaint(0, new Color(0xA0, 0xCF, 0xEC));
	}

	final ValueAxis axis = new NumberAxis3D("");
	axis.setRange(0, library.getSpaceInformation().getCapacity());
	plot.setRangeAxis(axis);

	NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
	    ChartUtilities.writeChartAsPNG(out, chart, 550, 35 + 40 * dataset.getColumnCount());
	    response.setContentType("image/png");
	    response.getOutputStream().write(out.toByteArray());
	    response.getOutputStream().close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }

}
