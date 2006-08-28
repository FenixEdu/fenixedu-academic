package net.sourceforge.fenixedu.util.teacherServiceDistribution.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ResourceBundle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DefaultCategoryDataset;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;

public class TeacherServiceDistributionChart {
	private ValuationPhase valuationPhase = null;
	private List<ExecutionPeriod> executionPeriodList = null;
	
	private ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.DepartmentMemberResources");
	
	public TeacherServiceDistributionChart(ValuationPhase valuationPhase, List<ExecutionPeriod> executionPeriodList) {
		this.valuationPhase = valuationPhase;
		this.executionPeriodList = executionPeriodList;
	}
	
	public void generateValuatedHoursPerGrouping(final OutputStream outputStream) throws IOException {
		ValuationGrouping rootValuationGrouping = valuationPhase.getRootValuationGrouping();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		
		if(rootValuationGrouping.hasAnyChilds()) {
			for(ValuationGrouping valuationGrouping : rootValuationGrouping.getChilds()) {
				dataset.setValue(valuationGrouping.getAllActiveCourseValuationTotalHoursByExecutionPeriods(executionPeriodList), resourceBundle.getString("label.teacherServiceDistribution.hours.total"), valuationGrouping.getName());
				dataset.setValue(valuationGrouping.getAllActiveCourseValuationTheoreticalHoursByExecutionPeriods(executionPeriodList), resourceBundle.getString("label.teacherServiceDistribution.theoretical"), valuationGrouping.getName());
				dataset.setValue(valuationGrouping.getAllActiveCourseValuationPraticalHoursByExecutionPeriods(executionPeriodList), resourceBundle.getString("label.teacherServiceDistribution.pratical"), valuationGrouping.getName());
				dataset.setValue(valuationGrouping.getAllActiveCourseValuationTheoPratHoursByExecutionPeriods(executionPeriodList), resourceBundle.getString("label.teacherServiceDistribution.theoPrat"), valuationGrouping.getName());
				dataset.setValue(valuationGrouping.getAllActiveCourseValuationLaboratorialHoursByExecutionPeriods(executionPeriodList), resourceBundle.getString("label.teacherServiceDistribution.laboratorial"), valuationGrouping.getName());
			}
			
			JFreeChart chart = ChartFactory.createBarChart3D(
					resourceBundle.getString("label.teacherServiceDistribution.hoursByGrouping"),
					resourceBundle.getString("label.teacherServiceDistribution.ValuationGroupings"),
					resourceBundle.getString("label.teacherServiceDistribution.hours.total"),
					dataset,
					PlotOrientation.VERTICAL,
					true,
					true,
					false);
			
			ChartUtilities.writeChartAsPNG(outputStream, chart, Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.width")), Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.height")));
		}
	}
	
	public void generateValuatedNumberStudentsPerGrouping(final OutputStream outputStream) throws IOException {
		ValuationGrouping rootValuationGrouping = valuationPhase.getRootValuationGrouping();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		
		if(rootValuationGrouping.hasAnyChilds()) {
			for(ValuationGrouping valuationGrouping : rootValuationGrouping.getChilds()) {
				dataset.setValue(valuationGrouping.getAllActiveCourseValuationTotalStudentsByExecutionPeriods(executionPeriodList), resourceBundle.getString("label.teacherServiceDistribution.students.total"), valuationGrouping.getName());
				dataset.setValue(valuationGrouping.getAllActiveCourseValuationFirstTimeEnrolledStudentsByExecutionPeriods(executionPeriodList), resourceBundle.getString("label.teacherServiceDistribution..num.firstTimeEnrolledStudents"), valuationGrouping.getName());
				dataset.setValue(valuationGrouping.getAllActiveCourseValuationSecondTimeEnrolledStudentsByExecutionPeriods(executionPeriodList), resourceBundle.getString("label.teacherServiceDistribution..num.secondTimeEnrolledStudents"), valuationGrouping.getName());
			}

			JFreeChart chart = ChartFactory.createBarChart3D(
					"Número De Alunos Estimados Por Agrupamento",
					resourceBundle.getString("label.teacherServiceDistribution.ValuationGroupings"),
					resourceBundle.getString("label.teacherServiceDistribution.students.total"),
					dataset,
					PlotOrientation.VERTICAL,
					true,
					true,
					false);
			
			ChartUtilities.writeChartAsPNG(outputStream, chart, Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.width")), Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.height")));			
		}
	}
}
