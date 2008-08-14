package net.sourceforge.fenixedu.presentationTier.Action.commons.student;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.ExecutionPeriodStatisticsBean;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
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
import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;

public class ViewCurriculumGraphDispatchAction extends FenixDispatchAction {

    public ActionForward createAreaXYChart(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	Registration registration = rootDomainObject.readRegistrationByOID(new Integer(request.getParameter("registrationOID")));

	List<ExecutionPeriodStatisticsBean> studentStatistics = getStudentStatistics(registration);
	Collections.sort(studentStatistics, new BeanComparator("executionPeriod"));

	final CategoryDataset dataset1 = createDataset1(studentStatistics);

	// create the chart...
	final JFreeChart chart = ChartFactory.createBarChart3D("Cadeiras Inscritas/Aprovadas", // chart
											       // title
		null,// "Ano Lectivo - Semestre", // domain axis label
		"Value", // range axis label
		dataset1, // data
		PlotOrientation.HORIZONTAL, true, // include legend
		true, false);

	chart.setBackgroundPaint(new Color(0xF5, 0xF5, 0xF5));

	final CategoryPlot plot = chart.getCategoryPlot();
	plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);

	final CategoryItemRenderer renderer1 = plot.getRenderer();
	renderer1.setSeriesPaint(0, new Color(0xA0, 0xCF, 0xEC));
	renderer1.setSeriesPaint(1, new Color(0xFF, 0xDA, 0xB9));

	final ValueAxis axis2 = new NumberAxis3D("Nº de Cadeiras");
	plot.setRangeAxis(axis2);

	// change the auto tick unit selection to integer units only.
	NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
	    ChartUtilities.writeChartAsPNG(out, chart, 640, 150 + 40 * dataset1.getColumnCount());
	    response.setContentType("image/png");
	    response.getOutputStream().write(out.toByteArray());
	    response.getOutputStream().close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return null;
    }

    /*
     * AUXILIARY METHODS
     */

    private CategoryDataset createDataset1(List<ExecutionPeriodStatisticsBean> studentStatistics) {

	final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	// row keys...
	final String series1 = "Nº de Cadeiras Inscritas";
	final String series2 = "Nº de Cadeiras Aprovadas";

	// column keys...
	String year = new String();
	String semester = new String();

	for (ExecutionPeriodStatisticsBean executionPeriodStatisticsBean : studentStatistics) {
	    year = executionPeriodStatisticsBean.getExecutionPeriod().getExecutionYear().getYear();
	    semester = executionPeriodStatisticsBean.getExecutionPeriod().getSemester().toString();
	    dataset
		    .addValue(executionPeriodStatisticsBean.getTotalEnrolmentsNumber(), series1, year + " - " + semester
			    + "º sem");
	    dataset.addValue(executionPeriodStatisticsBean.getApprovedEnrolmentsNumber(), series2, year + " - " + semester
		    + "º sem");
	}
	return dataset;
    }

    private List<ExecutionPeriodStatisticsBean> getStudentStatistics(Registration registration) {
	List<ExecutionPeriodStatisticsBean> studentStatistics = new ArrayList<ExecutionPeriodStatisticsBean>();

	Map<ExecutionSemester, ExecutionPeriodStatisticsBean> enrolmentsByExecutionPeriod = new HashMap<ExecutionSemester, ExecutionPeriodStatisticsBean>();

	for (StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {
	    for (ExecutionSemester executionSemester : studentCurricularPlan.getEnrolmentsExecutionPeriods()) {
		if (enrolmentsByExecutionPeriod.containsKey(executionSemester)) {
		    ExecutionPeriodStatisticsBean executionPeriodStatisticsBean = enrolmentsByExecutionPeriod
			    .get(executionSemester);
		    executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(studentCurricularPlan
			    .getEnrolmentsByExecutionPeriod(executionSemester));
		    enrolmentsByExecutionPeriod.put(executionSemester, executionPeriodStatisticsBean);
		} else {
		    ExecutionPeriodStatisticsBean executionPeriodStatisticsBean = new ExecutionPeriodStatisticsBean(
			    executionSemester);
		    executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(studentCurricularPlan
			    .getEnrolmentsByExecutionPeriod(executionSemester));
		    enrolmentsByExecutionPeriod.put(executionSemester, executionPeriodStatisticsBean);
		}
	    }
	}
	studentStatistics.addAll(enrolmentsByExecutionPeriod.values());

	return studentStatistics;
    }
}
