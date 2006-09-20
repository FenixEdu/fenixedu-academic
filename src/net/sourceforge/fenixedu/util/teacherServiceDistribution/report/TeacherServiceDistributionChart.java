package net.sourceforge.fenixedu.util.teacherServiceDistribution.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.DefaultCategoryDataset;
import org.jfree.data.DefaultPieDataset;

public abstract class TeacherServiceDistributionChart {
	protected ValuationGrouping valuationGrouping = null;

	protected List<ExecutionPeriod> executionPeriodList = null;

	protected ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.DepartmentMemberResources", LanguageUtils.getLocale());

	public abstract void execute(final OutputStream outputStream) throws IOException;
	
	
	public static TeacherServiceDistributionChart generateValuatedHoursPerGroupingChart() {
		return new TeacherServiceDistributionChart() {
			public void execute(final OutputStream outputStream) throws IOException {
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();

				if (this.valuationGrouping.hasAnyChilds()) {
					for (ValuationGrouping grouping : this.valuationGrouping.getChilds()) {
						Double allActiveCourseValuationTheoreticalHours = grouping.getAllActiveCourseValuationTheoreticalHoursByExecutionPeriods(executionPeriodList);
						Double allActiveCourseValuationPraticalHours = grouping.getAllActiveCourseValuationPraticalHoursByExecutionPeriods(executionPeriodList);
						Double allActiveCourseValuationTheoPratHours = grouping.getAllActiveCourseValuationTheoPratHoursByExecutionPeriods(executionPeriodList);
						Double allActiveCourseValuationLaboratorialHours = grouping.getAllActiveCourseValuationLaboratorialHoursByExecutionPeriods(executionPeriodList);

						dataset.setValue(
								allActiveCourseValuationTheoreticalHours + allActiveCourseValuationPraticalHours
										+ allActiveCourseValuationTheoPratHours
										+ allActiveCourseValuationLaboratorialHours,
								resourceBundle.getString("label.teacherServiceDistribution.hours.total"),
								grouping.getName());
						dataset.setValue(
								allActiveCourseValuationTheoreticalHours,
								resourceBundle.getString("label.teacherServiceDistribution.theoretical"),
								grouping.getName());
						dataset.setValue(
								allActiveCourseValuationPraticalHours,
								resourceBundle.getString("label.teacherServiceDistribution.pratical"),
								grouping.getName());
						dataset.setValue(
								allActiveCourseValuationTheoPratHours,
								resourceBundle.getString("label.teacherServiceDistribution.theoPrat"),
								grouping.getName());
						dataset.setValue(
								allActiveCourseValuationLaboratorialHours,
								resourceBundle.getString("label.teacherServiceDistribution.laboratorial"),
								grouping.getName());
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

					ChartUtilities.writeChartAsPNG(
							outputStream,
							chart,
							Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.width")),
							Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.height")));
				}
			}
		};
	}

	public static TeacherServiceDistributionChart generateValuatedNumberStudentsPerGroupingChart() {
		return new TeacherServiceDistributionChart() {
			public void execute(final OutputStream outputStream) throws IOException {
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();

				if (valuationGrouping.hasAnyChilds()) {
					for (ValuationGrouping grouping : valuationGrouping.getChilds()) {
						Integer allActiveCourseValuationFirstTimeEnrolledStudents = grouping.getAllActiveCourseValuationFirstTimeEnrolledStudentsByExecutionPeriods(executionPeriodList);
						Integer allActiveCourseValuationSecondTimeEnrolledStudents = grouping.getAllActiveCourseValuationSecondTimeEnrolledStudentsByExecutionPeriods(executionPeriodList);

						dataset.setValue(
								allActiveCourseValuationFirstTimeEnrolledStudents
										+ allActiveCourseValuationSecondTimeEnrolledStudents,
								resourceBundle.getString("label.teacherServiceDistribution.students.total"),
								grouping.getName());
						dataset.setValue(
								allActiveCourseValuationFirstTimeEnrolledStudents,
								resourceBundle.getString("label.teacherServiceDistribution..num.firstTimeEnrolledStudents"),
								grouping.getName());
						dataset.setValue(
								allActiveCourseValuationSecondTimeEnrolledStudents,
								resourceBundle.getString("label.teacherServiceDistribution..num.secondTimeEnrolledStudents"),
								grouping.getName());
					}

					JFreeChart chart = ChartFactory.createBarChart3D(
							resourceBundle.getString("label.teacherServiceDistribution.numberStudentsByGrouping"),
							resourceBundle.getString("label.teacherServiceDistribution.ValuationGroupings"),
							resourceBundle.getString("label.teacherServiceDistribution.students.total"),
							dataset,
							PlotOrientation.VERTICAL,
							true,
							true,
							false);

					ChartUtilities.writeChartAsPNG(
							outputStream,
							chart,
							Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.width")),
							Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.height")));
				}
			}
		};
	}

	public static TeacherServiceDistributionChart generateValuatedHoursPerGroupingPieChart() {
		return new TeacherServiceDistributionChart() {
			public void execute(final OutputStream outputStream) throws IOException {
				Map<String, Double> valuatedTotalHoursPerGroupingMap = new HashMap<String, Double>();

				if (this.valuationGrouping.hasAnyChilds()) {
					for (ValuationGrouping grouping : this.valuationGrouping.getChilds()) {
						valuatedTotalHoursPerGroupingMap.put(
								grouping.getName(),
								grouping.getAllActiveCourseValuationTotalHoursByExecutionPeriods(executionPeriodList));
					}

					Double totalHours = 0d;

					for (String groupingName : valuatedTotalHoursPerGroupingMap.keySet()) {
						totalHours += valuatedTotalHoursPerGroupingMap.get(groupingName);
					}

					DefaultPieDataset dataset = new DefaultPieDataset();

					for (String groupingName : valuatedTotalHoursPerGroupingMap.keySet()) {
						dataset.setValue(groupingName, (new Double(
								StrictMath.round((valuatedTotalHoursPerGroupingMap.get(groupingName) / totalHours) * 100))).intValue());
					}

					JFreeChart chart = ChartFactory.createPieChart3D(
							resourceBundle.getString("label.teacherServiceDistribution.hoursByGrouping") + " %",
							dataset,
							true,
							true,
							false);

					ChartUtilities.writeChartAsPNG(
							outputStream,
							chart,
							Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.width")),
							Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.height")));
				}

			}
		};
	}

	public static TeacherServiceDistributionChart generateValuatedNumberStudentsPerGroupingPieChart() {
		return new TeacherServiceDistributionChart() {
			public void execute(final OutputStream outputStream) throws IOException {
				Map<String, Double> valuatedTotalNumberStudentsPerGroupingMap = new HashMap<String, Double>();

				if (this.valuationGrouping.hasAnyChilds()) {
					for (ValuationGrouping grouping : this.valuationGrouping.getChilds()) {
						valuatedTotalNumberStudentsPerGroupingMap.put(
								grouping.getName(),
								grouping.getAllActiveCourseValuationTotalStudentsByExecutionPeriods(executionPeriodList));
					}

					Double totalNumberOfStudents = 0d;

					for (String groupingName : valuatedTotalNumberStudentsPerGroupingMap.keySet()) {
						totalNumberOfStudents += valuatedTotalNumberStudentsPerGroupingMap.get(groupingName);
					}

					DefaultPieDataset dataset = new DefaultPieDataset();

					for (String groupingName : valuatedTotalNumberStudentsPerGroupingMap.keySet()) {
						dataset.setValue(groupingName, (new Double(
								StrictMath.round((valuatedTotalNumberStudentsPerGroupingMap.get(groupingName) / totalNumberOfStudents) * 100))).intValue());
					}

					JFreeChart chart = ChartFactory.createPieChart3D(
							resourceBundle.getString("label.teacherServiceDistribution.numberStudentsByGrouping") + " %",
							dataset,
							true,
							true,
							false);

					ChartUtilities.writeChartAsPNG(
							outputStream,
							chart,
							Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.width")),
							Integer.parseInt(PropertiesManager.getProperty("teacherServiceDistribution.chart.height")));
				}

			}
		};
	}	

	public void setExecutionPeriodList(List<ExecutionPeriod> executionPeriodList) {
		this.executionPeriodList = executionPeriodList;
	}


	public void setValuationGrouping(ValuationGrouping valuationGrouping) {
		this.valuationGrouping = valuationGrouping;
	}
}
