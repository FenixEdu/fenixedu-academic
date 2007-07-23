package net.sourceforge.fenixedu.presentationTier.Action.student.tutor;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.ExecutionPeriodStatisticsBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
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

public class TutorInfoDispatchAction extends FenixDispatchAction {
	
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
    	final Person person = getLoggedPerson(request);
    	List<Tutorship> pastTutors = new ArrayList<Tutorship>();
    
    	List<Registration> registrations = person.getStudent().getRegistrations();

    	for(Registration registration : registrations) {
    		List<StudentCurricularPlan> studentCurricularPlans = registration.getStudentCurricularPlans();
    		for(StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
    			for(Tutorship tutorship : studentCurricularPlan.getTutorships()) {
    				if(tutorship.isActive()) {
    					request.setAttribute("actualTutor", tutorship);
    					request.setAttribute("personID", tutorship.getTeacher().getPerson().getIdInternal());
    				} else {
    					pastTutors.add(tutorship);
    				}
    			} 			
    		}	   		
    	} 	
    	request.setAttribute("pastTutors", pastTutors); 	
    	//return mapping.findForward("ShowStudentTutorInfo");  
    	return prepareStudentStatistics(mapping, actionForm, request, response);
    }
    
    public ActionForward prepareStudentStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
    	
    	final Person person = getLoggedPerson(request);
    	List<ExecutionPeriodStatisticsBean> studentStatistics = new ArrayList<ExecutionPeriodStatisticsBean>();
    
    	List<Registration> registrations = person.getStudent().getRegistrations();

    	for(Registration registration : registrations) {
    		List<StudentCurricularPlan> studentCurricularPlans = registration.getStudentCurricularPlans();
    		for(StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {		
    			Set<ExecutionPeriod> executionPeriodsWithEnrolments = studentCurricularPlan.getEnrolmentsExecutionPeriods();
    			for(ExecutionPeriod executionPeriod : executionPeriodsWithEnrolments) {
    				ExecutionPeriodStatisticsBean executionPeriodStatisticsBean = new ExecutionPeriodStatisticsBean(executionPeriod, studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionPeriod));
    				studentStatistics.add(executionPeriodStatisticsBean);    			
    			}
    		}
    	}
    	request.setAttribute("studentStatistics", studentStatistics);   	
    	return mapping.findForward("ShowStudentTutorInfo"); 
    }
    
    public ActionForward createAreaXYChart(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
    	
    	final Person person = getLoggedPerson(request);
    	List<ExecutionPeriodStatisticsBean> studentStatistics = new ArrayList<ExecutionPeriodStatisticsBean>();
    
    	List<Registration> registrations = person.getStudent().getRegistrations();

    	for(Registration registration : registrations) {
    		List<StudentCurricularPlan> studentCurricularPlans = registration.getStudentCurricularPlans();
    		for(StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {		
    			Set<ExecutionPeriod> executionPeriodsWithEnrolments = studentCurricularPlan.getEnrolmentsExecutionPeriods();
    			for(ExecutionPeriod executionPeriod : executionPeriodsWithEnrolments) {
    				ExecutionPeriodStatisticsBean executionPeriodStatisticsBean = new ExecutionPeriodStatisticsBean(executionPeriod, studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionPeriod));
    				studentStatistics.add(executionPeriodStatisticsBean);    			
    			}
    		}
    	}
    	Collections.sort(studentStatistics, new BeanComparator("executionPeriod"));
    	
    	
    	final CategoryDataset dataset1 = createDataset1(studentStatistics);
		
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart3D(
            "Gráfico Cadeiras Inscritas/Aprovadas",  	// chart title
            null,//"Ano Lectivo - Semestre",         	// domain axis label
            "Value",            						// range axis label
            dataset1,           						// data
            PlotOrientation.HORIZONTAL,
            true,               						// include legend
            true,
            false
        );
        
        chart.setBackgroundPaint(new Color(0xF5, 0xF5, 0xF5));

        final CategoryPlot plot = chart.getCategoryPlot();
        //plot.setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        plot.setRangeAxisLocation(AxisLocation.TOP_OR_LEFT);
        final CategoryItemRenderer renderer1 = plot.getRenderer();
        renderer1.setSeriesPaint(0, new Color(0xA0, 0xCF, 0xEC));
        renderer1.setSeriesPaint(1, new Color(0xFF, 0xDA, 0xB9));
        final ValueAxis axis2 = new NumberAxis3D("Nº de Cadeiras");
        plot.setRangeAxis(axis2);
        // change the auto tick unit selection to integer units only.
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        //plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);
		
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
			ChartUtilities.writeChartAsPNG(out, chart, 640, 150+40*dataset1.getColumnCount());
			response.setContentType("image/png");
			response.getOutputStream().write(out.toByteArray());
			response.getOutputStream().close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
		return null; 
    }
    
    private CategoryDataset createDataset1(List<ExecutionPeriodStatisticsBean> studentStatistics) {
   	
    	final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    	
        // row keys...
        final String series1 = "Nº de Cadeiras Inscritas";
        final String series2 = "Nº de Cadeiras Aprovadas"; 

        // column keys...
        String year = new String();
        String semester = new String();
        
        for(ExecutionPeriodStatisticsBean executionPeriodStatisticsBean : studentStatistics) {
        	year = executionPeriodStatisticsBean.getExecutionPeriod().getExecutionYear().getYear();
        	semester = executionPeriodStatisticsBean.getExecutionPeriod().getSemester().toString();
        	dataset.addValue(executionPeriodStatisticsBean.getTotalEnrolmentsNumber(), series1, year+" - "+semester+"º sem");
        	dataset.addValue(executionPeriodStatisticsBean.getApprovedEnrolmentsNumber(), series2, year+" - "+semester+"º sem");
        }
        return dataset;
    }
}
