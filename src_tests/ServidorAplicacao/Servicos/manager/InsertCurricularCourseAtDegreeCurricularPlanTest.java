/*
 * Created on 2/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.HashMap;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoDegreeCurricularPlan;

/**
 * @author lmac1
 */
public class InsertCurricularCourseAtDegreeCurricularPlanTest extends TestCaseManagerInsertAndEditServices {

	public InsertCurricularCourseAtDegreeCurricularPlanTest(String testName) {
			super(testName);
		}

	protected String getNameOfServiceToBeTested(){
		return "InsertCurricularCourseAtDegreeCurricularPlan";
	}
		
	protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly(){
		return null;
	}
//	insert curricular course with name,code already existing in DB but with another key_degree_curricular_plan
	protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly(){
			InfoDegreeCurricularPlan infoDegreeCP = new InfoDegreeCurricularPlan();
			infoDegreeCP.setIdInternal(new Integer(2));
			InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
			infoCurricularCourse.setName("Analise Matematica I");
			infoCurricularCourse.setCode("AMI");
			infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCP);
		
			Object[] args = { infoCurricularCourse };
				return args;
		}

	
//	insert curricular course with name,code and key_degree_curricular_plan already existing in DB
	protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly(){
		InfoDegreeCurricularPlan infoDegreeCP = new InfoDegreeCurricularPlan();
		infoDegreeCP.setIdInternal(new Integer(1));
		InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
		infoCurricularCourse.setName("Analise Matematica I");
		infoCurricularCourse.setCode("AMI");
		infoCurricularCourse.setInfoDegreeCurricularPlan(infoDegreeCP);
		
		Object[] args = { infoCurricularCourse };
			return args;
	}

	
	
}
