/*
 * Created on 2/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoTeacher;

/**
 * @author lmac1
 */
public class InsertExecutionDegreeAtDegreeCurricularPlanTest extends TestCaseManagerInsertAndEditServices {

	public InsertExecutionDegreeAtDegreeCurricularPlanTest(String testName) {
			super(testName);
		}

	protected String getNameOfServiceToBeTested(){
		return "InsertExecutionDegreeAtDegreeCurricularPlan";
	}

//	insert curricular course with name existing in DB but another key_degree_curricular_plan 	
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
		
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		infoDegreeCurricularPlan.setIdInternal(new Integer(2));
		
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setIdInternal(new Integer(2));
		
		InfoTeacher infoTeacher = new InfoTeacher();
		infoTeacher.setIdInternal(new Integer(6));
		
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
		//infoExecutionDegree.setInfoCoordinator(infoTeacher);
		infoExecutionDegree.setTemporaryExamMap(new Boolean(false));
		
		Object[] args = { infoExecutionDegree };
		return args;
	}

//	insert curricular course with name and key_degree already existing in DB
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		infoDegreeCurricularPlan.setIdInternal(new Integer(2));
		
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
		infoExecutionYear.setIdInternal(new Integer(1));
		
		InfoTeacher infoTeacher = new InfoTeacher();
		infoTeacher.setIdInternal(new Integer(6));
		
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
//		infoExecutionDegree.setInfoCoordinator(infoTeacher);
		infoExecutionDegree.setTemporaryExamMap(new Boolean(false));
		
		Object[] args = { infoExecutionDegree };
		return args;
	}
}