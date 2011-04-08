package net.sourceforge.fenixedu.presentationTier.Action.library;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.library.LibraryCardDTO;
import net.sourceforge.fenixedu.dataTransferObject.library.SelectLibraryBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.library.LibraryCard;
import net.sourceforge.fenixedu.domain.space.Space;
import net.sourceforge.fenixedu.domain.space.SpaceAttendances;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

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
import pt.ist.fenixWebFramework.services.Service;

public class LibraryOperatorDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("selectLibraryBean", new SelectLibraryBean());
	return mapping.findForward("libraryOperator");
    }

    public ActionForward showPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	String personId = request.getParameter("personIstUsername");
	String libraryIdInternal = request.getParameter("libraryIdInternal");
	Person person = Person.readPersonByIstUsername(personId);

	Space library = getLibraryByIdInternal(libraryIdInternal);
	SelectLibraryBean bean = null;

	if (library != null && person != null) {
	    bean = refillBean(person, library);
	    bean.setPersonId(person.getIstUsername());
	    bean.setEditableCardNumber(hasPersonLibraryCardNumber(person));
	}

	request.setAttribute("selectLibraryBean", bean);
	return mapping.findForward("libraryOperator");
    }

    private boolean hasPersonLibraryCardNumber(Person person) {
	if (person == null || person.getLibraryCard() == null || person.getLibraryCard().getCardNumber() == null) {
	    return false;
	}
	return true;
    }

    public ActionForward searchPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	SelectLibraryBean bean = getRenderedObject("search.person");
	Person person = null;

	String ist = "ist";

	if (bean.getPersonId() != null) {

	    if (bean.getPersonId().startsWith(ist)) {
		person = Person.readPersonByIstUsername(bean.getPersonId());
	    } else {
		person = Person.readPersonByLibraryCardNumber(bean.getPersonId());
	    }

	    if (person != null) {
		bean.setPersonInside(isPersonInside(person, bean.getLibrary()));
		bean.setEditableCardNumber(hasPersonLibraryCardNumber(person));
		bean.setPersonEntranceTime(getAttendanceTimeForPerson(bean.getLibrary(), person));
	    }
	}

	bean.setEditCardNumberError(false);
	bean.setPerson(person);

	request.setAttribute("selectLibraryBean", bean);
	return mapping.findForward("libraryOperator");
    }

    private boolean isPersonInside(Person person, Space library) {
	if (person != null) {
	    return person.insideSpace(library) ? true : false;
	}
	return false;
    }

    public ActionForward selectLibrary(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	SelectLibraryBean bean = getRenderedObject("select.library.bean");
	bean.resetPersonInformation();

	if (bean.getLibrary() != null) {
	    fillAllAttendances(bean);
	}

	request.setAttribute("selectLibraryBean", bean);
	RenderUtils.invalidateViewState("select.library.bean");
	return mapping.findForward("libraryOperator");
    }

    private void fillAllAttendances(SelectLibraryBean bean) {
	for (SpaceAttendances att : bean.getLibrary().getAttendances()) {
	    Person person = Person.readPersonByIstUsername(att.getPersonIstUsername());
	    bean.addAttendance(person, att.getEntranceTime().toString("dd/MM/yyyy HH:mm"));
	}
    }

    private String getAttendanceTimeForPerson(Space library, Person person) {
	for (SpaceAttendances att : library.getAttendances()) {
	    if (person.equals(Person.readPersonByIstUsername(att.getPersonIstUsername()))) {
		return att.getEntranceTime().toString("dd/MM/yyyy HH:mm");
	    }
	}
	return null;
    }

    public ActionForward registerStudentLibraryCardNumber(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	SelectLibraryBean bean = getRenderedObject("select.library.bean");
	request.setAttribute("selectLibraryBean", bean);
	return mapping.findForward("libraryOperator");
    }

    @Service
    public ActionForward studentEntrance(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	String personIstUsername = request.getParameter("personIstUsername");
	String libraryIdInternal = request.getParameter("libraryIdInternal");

	Person person = Person.readPersonByIstUsername(personIstUsername);
	Space library = getLibraryByIdInternal(libraryIdInternal);

	if (library != null && person != null) {
	    library.addAttendance(person, getUserView(request).getUsername());
	}

	SelectLibraryBean bean = refillBean(person, library);
	request.setAttribute("selectLibraryBean", bean);

	return mapping.findForward("libraryOperator");
    }

    private SelectLibraryBean refillBean(Person person, Space library) {
	SelectLibraryBean bean = new SelectLibraryBean();
	bean.resetPersonInformation();
	bean.setPerson(person);
	bean.setLibrary(library);
	bean.setPersonInside(isPersonInside(person, bean.getLibrary()));
	bean.setPersonEntranceTime(getAttendanceTimeForPerson(library, person));
	fillAllAttendances(bean);
	return bean;
    }

    private Space getLibraryByIdInternal(String libraryIdInternal) {
	RootDomainObject root = RootDomainObject.getInstance();
	for (Space space : root.getLibraries()) {
	    if (libraryIdInternal.equals(space.getIdInternal().toString())) {
		return space;
	    }
	}
	return null;
    }

    @Service
    public ActionForward studentExit(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	String personIstUsername = request.getParameter("personIstUsername");
	String libraryIdInternal = request.getParameter("libraryIdInternal");

	Person person = Person.readPersonByIstUsername(personIstUsername);
	Space library = getLibraryByIdInternal(libraryIdInternal);

	if (library != null && person != null) {
	    library.removeAttendance(person, getUserView(request).getUsername());
	}

	SelectLibraryBean bean = refillBean(person, library);
	request.setAttribute("selectLibraryBean", bean);

	return mapping.findForward("libraryOperator");
    }

    @Service
    public ActionForward setPersonLibraryCardNumber(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	System.out.println("SET PERSON LIBRARY CARD NUMBER!");

	SelectLibraryBean bean = getRenderedObject("set.library.card.number");
	Person person = bean.getPerson();
	boolean operationExecuted = false;

	if (person != null && bean.getLibraryCardNumber() != null && bean.getLibraryCardNumber().length() > 0) {
	    operationExecuted = addOrChangeLibraryCardNumber(person, bean.getLibraryCardNumber());
	}

	if (operationExecuted) {
	    bean.setEditableCardNumber(true);
	    bean.setEditCardNumberError(false);
	} else {
	    bean.setEditCardNumberError(true);
	}

	request.setAttribute("selectLibraryBean", bean);
	return mapping.findForward("libraryOperator");
    }

    private boolean addOrChangeLibraryCardNumber(Person person, String libraryCardNumber) {
	Person search = Person.readPersonByLibraryCardNumber(libraryCardNumber);

	if (search == null || search.equals(person)) {
	    if (person.getLibraryCard() == null) {
		person.setLibraryCard(new LibraryCard(new LibraryCardDTO(person, person.getPartyClassification())));
	    }
	    person.getLibraryCard().setCardNumber(libraryCardNumber);
	    return true;
	}

	return false;
    }

    @Service
    public ActionForward editPersonLibraryCardNumber(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	String personIstUsername = request.getParameter("personIstUsername");
	String libraryIdInternal = request.getParameter("libraryIdInternal");

	Person person = Person.readPersonByIstUsername(personIstUsername);
	Space library = getLibraryByIdInternal(libraryIdInternal);

	SelectLibraryBean bean = refillBean(person, library);
	bean.setEditableCardNumber(false);
	request.setAttribute("selectLibraryBean", bean);

	return mapping.findForward("libraryOperator");
    }

    public ActionForward cancelEditPersonLibraryCardNumber(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	String personIstUsername = request.getParameter("personIstUsername");
	String libraryIdInternal = request.getParameter("libraryIdInternal");

	Person person = Person.readPersonByIstUsername(personIstUsername);
	Space library = getLibraryByIdInternal(libraryIdInternal);

	SelectLibraryBean bean = refillBean(person, library);
	bean.setEditableCardNumber(true);
	request.setAttribute("selectLibraryBean", bean);

	return mapping.findForward("libraryOperator");
    }

    public ActionForward createAreaXYChart(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	String libraryIdInternal = request.getParameter("libraryIdInternal");
	Space library = getLibraryByIdInternal(libraryIdInternal);

	final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	dataset.addValue(library.getAttendancesCount(), "Alunos", "Ocupação");

	final JFreeChart chart = ChartFactory.createBarChart3D(null, null, "Value", dataset, PlotOrientation.HORIZONTAL, false,
		false, false);

	chart.setBackgroundPaint(Color.WHITE);

	final CategoryPlot plot = chart.getCategoryPlot();
	plot.setRangeAxisLocation(AxisLocation.TOP_OR_RIGHT);

	final CategoryItemRenderer renderer1 = plot.getRenderer();

	if (!library.hasEnoughSpace()) {
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