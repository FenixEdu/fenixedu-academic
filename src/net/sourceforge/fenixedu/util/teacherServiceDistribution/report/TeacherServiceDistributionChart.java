package net.sourceforge.fenixedu.util.teacherServiceDistribution.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DefaultCategoryDataset;
import org.jfree.data.DefaultPieDataset;

public abstract class TeacherServiceDistributionChart {
    protected TeacherServiceDistribution tsd = null;

    protected List<ExecutionSemester> executionPeriodList = null;

    protected ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.DepartmentMemberResources", Language
	    .getLocale());

    public abstract void execute(final OutputStream outputStream) throws IOException;

    public static TeacherServiceDistributionChart generateValuatedHoursPerGroupingChart() {
	return new TeacherServiceDistributionChart() {
	    public void execute(final OutputStream outputStream) throws IOException {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (this.tsd.hasAnyChilds()) {
		    for (TeacherServiceDistribution grouping : this.tsd.getChilds()) {
			Double allActiveTSDCourseHours = grouping
				.getAllActiveTSDCourseTotalHoursByExecutionPeriods(executionPeriodList);
			// Double allActiveTSDCourseTheoreticalHours = grouping.
			// getAllActiveTSDCourseTheoreticalHoursByExecutionPeriods
			// (executionPeriodList);
			// Double allActiveTSDCoursePraticalHours = grouping.
			// getAllActiveTSDCoursePraticalHoursByExecutionPeriods
			// (executionPeriodList);
			// Double allActiveTSDCourseTheoPratHours = grouping.
			// getAllActiveTSDCourseTheoPratHoursByExecutionPeriods
			// (executionPeriodList);
			// Double allActiveTSDCourseLaboratorialHours =
			// grouping.
			// getAllActiveTSDCourseLaboratorialHoursByExecutionPeriods
			// (executionPeriodList);

			dataset.setValue(allActiveTSDCourseHours, resourceBundle
				.getString("label.teacherServiceDistribution.hours.total"), grouping.getName());
			/*
			 * dataset.setValue( allActiveTSDCourseTheoreticalHours,
			 * resourceBundle
			 * .getString("label.teacherServiceDistribution.theoretical"
			 * ), grouping.getName()); dataset.setValue(
			 * allActiveTSDCoursePraticalHours,
			 * resourceBundle.getString
			 * ("label.teacherServiceDistribution.pratical"),
			 * grouping.getName()); dataset.setValue(
			 * allActiveTSDCourseTheoPratHours,
			 * resourceBundle.getString
			 * ("label.teacherServiceDistribution.theoPrat"),
			 * grouping.getName()); dataset.setValue(
			 * allActiveTSDCourseLaboratorialHours,
			 * resourceBundle.getString
			 * ("label.teacherServiceDistribution.laboratorial"),
			 * grouping.getName());
			 */
		    }

		    JFreeChart chart = ChartFactory.createBarChart3D(resourceBundle
			    .getString("label.teacherServiceDistribution.hoursByGrouping"), resourceBundle
			    .getString("label.teacherServiceDistribution.TeacherServiceDistributions"), resourceBundle
			    .getString("label.teacherServiceDistribution.hours.total"), dataset, PlotOrientation.VERTICAL, true,
			    true, false);

		    ChartUtilities.writeChartAsPNG(outputStream, chart, Integer.parseInt(PropertiesManager
			    .getProperty("tsdProcess.chart.width")), Integer.parseInt(PropertiesManager
			    .getProperty("tsdProcess.chart.height")));
		}
	    }
	};
    }

    public static TeacherServiceDistributionChart generateValuatedNumberStudentsPerGroupingChart() {
	return new TeacherServiceDistributionChart() {
	    public void execute(final OutputStream outputStream) throws IOException {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (tsd.hasAnyChilds()) {
		    for (TeacherServiceDistribution grouping : tsd.getChilds()) {
			Integer allActiveTSDCourseFirstTimeEnrolledStudents = grouping
				.getAllActiveTSDCourseFirstTimeEnrolledStudentsByExecutionPeriods(executionPeriodList);
			Integer allActiveTSDCourseSecondTimeEnrolledStudents = grouping
				.getAllActiveTSDCourseSecondTimeEnrolledStudentsByExecutionPeriods(executionPeriodList);

			dataset.setValue(allActiveTSDCourseFirstTimeEnrolledStudents
				+ allActiveTSDCourseSecondTimeEnrolledStudents, resourceBundle
				.getString("label.teacherServiceDistribution.students.total"), grouping.getName());
			dataset
				.setValue(allActiveTSDCourseFirstTimeEnrolledStudents, resourceBundle
					.getString("label.teacherServiceDistribution..num.firstTimeEnrolledStudents"), grouping
					.getName());
			dataset.setValue(allActiveTSDCourseSecondTimeEnrolledStudents, resourceBundle
				.getString("label.teacherServiceDistribution..num.secondTimeEnrolledStudents"), grouping
				.getName());
		    }

		    JFreeChart chart = ChartFactory.createBarChart3D(resourceBundle
			    .getString("label.teacherServiceDistribution.numberStudentsByGrouping"), resourceBundle
			    .getString("label.teacherServiceDistribution.TeacherServiceDistributions"), resourceBundle
			    .getString("label.teacherServiceDistribution.students.total"), dataset, PlotOrientation.VERTICAL,
			    true, true, false);

		    ChartUtilities.writeChartAsPNG(outputStream, chart, Integer.parseInt(PropertiesManager
			    .getProperty("tsdProcess.chart.width")), Integer.parseInt(PropertiesManager
			    .getProperty("tsdProcess.chart.height")));
		}
	    }
	};
    }

    public static TeacherServiceDistributionChart generateValuatedHoursPerGroupingPieChart() {
	return new TeacherServiceDistributionChart() {
	    public void execute(final OutputStream outputStream) throws IOException {
		Map<String, Double> valuatedTotalHoursPerGroupingMap = new HashMap<String, Double>();

		if (this.tsd.hasAnyChilds()) {
		    for (TeacherServiceDistribution grouping : this.tsd.getChilds()) {
			valuatedTotalHoursPerGroupingMap.put(grouping.getName(), grouping
				.getAllActiveTSDCourseTotalHoursByExecutionPeriods(executionPeriodList));
		    }

		    Double totalHours = 0d;

		    for (String groupingName : valuatedTotalHoursPerGroupingMap.keySet()) {
			totalHours += valuatedTotalHoursPerGroupingMap.get(groupingName);
		    }

		    DefaultPieDataset dataset = new DefaultPieDataset();

		    for (String groupingName : valuatedTotalHoursPerGroupingMap.keySet()) {
			dataset.setValue(groupingName, (new Double(StrictMath.round((valuatedTotalHoursPerGroupingMap
				.get(groupingName) / totalHours) * 100))).intValue());
		    }

		    JFreeChart chart = ChartFactory.createPieChart3D(resourceBundle
			    .getString("label.teacherServiceDistribution.hoursByGrouping")
			    + " %", dataset, true, true, false);

		    ChartUtilities.writeChartAsPNG(outputStream, chart, Integer.parseInt(PropertiesManager
			    .getProperty("tsdProcess.chart.width")), Integer.parseInt(PropertiesManager
			    .getProperty("tsdProcess.chart.height")));
		}

	    }
	};
    }

    public static TeacherServiceDistributionChart generateValuatedNumberStudentsPerGroupingPieChart() {
	return new TeacherServiceDistributionChart() {
	    public void execute(final OutputStream outputStream) throws IOException {
		Map<String, Double> valuatedTotalNumberStudentsPerGroupingMap = new HashMap<String, Double>();

		if (this.tsd.hasAnyChilds()) {
		    for (TeacherServiceDistribution grouping : this.tsd.getChilds()) {
			valuatedTotalNumberStudentsPerGroupingMap.put(grouping.getName(), grouping
				.getAllActiveTSDCourseTotalStudentsByExecutionPeriods(executionPeriodList));
		    }

		    Double totalNumberOfStudents = 0d;

		    for (String groupingName : valuatedTotalNumberStudentsPerGroupingMap.keySet()) {
			totalNumberOfStudents += valuatedTotalNumberStudentsPerGroupingMap.get(groupingName);
		    }

		    DefaultPieDataset dataset = new DefaultPieDataset();

		    for (String groupingName : valuatedTotalNumberStudentsPerGroupingMap.keySet()) {
			dataset.setValue(groupingName, (new Double(StrictMath.round((valuatedTotalNumberStudentsPerGroupingMap
				.get(groupingName) / totalNumberOfStudents) * 100))).intValue());
		    }

		    JFreeChart chart = ChartFactory.createPieChart3D(resourceBundle
			    .getString("label.teacherServiceDistribution.numberStudentsByGrouping")
			    + " %", dataset, true, true, false);

		    ChartUtilities.writeChartAsPNG(outputStream, chart, Integer.parseInt(PropertiesManager
			    .getProperty("tsdProcess.chart.width")), Integer.parseInt(PropertiesManager
			    .getProperty("tsdProcess.chart.height")));
		}

	    }
	};
    }

    public void setExecutionPeriodList(List<ExecutionSemester> executionPeriodList) {
	this.executionPeriodList = executionPeriodList;
    }

    public void setTeacherServiceDistribution(TeacherServiceDistribution tsd) {
	this.tsd = tsd;
    }
}
