/*
 * Created on 23/Out/2003
 *
 */
package ServidorAplicacao.Servicos.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExam;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteExamMap;
import DataBeans.SiteView;
import ServidorAplicacao.Servicos.ServiceTestCase;

/**
 * @author  Luis Egidio, lmre@mega.ist.utl.pt
 * 			Nuno Ochoa,  nmgo@mega.ist.utl.pt
 *
 */
public class ExamSiteComponentServiceTest extends ServiceTestCase {

	public ExamSiteComponentServiceTest(String nome) {
		super(nome);
	}

	protected String getDataSetFilePath() {
		return null;
	}

	protected String getNameOfServiceToBeTested() {
		return "ExamSiteComponentService";
	}

	public void testExamSite() {
		SiteView result = null;
		InfoSiteExamMap bodyComponent = new InfoSiteExamMap();
		String executionYearName = "2003/2004";
		String executionPeriodName = "1 Semestre";
		List curricularYears = new ArrayList();
		curricularYears.add(new Integer(1));
		curricularYears.add(new Integer(2));
		String degreeInitials = "LEIC";
		String nameDegreeCurricularPlan = "LEIC2003/2004";

		Object[] args =
			{
				bodyComponent,
				executionYearName,
				executionPeriodName,
				degreeInitials,
				nameDegreeCurricularPlan,
				curricularYears };
		try {
			result =
				(SiteView) gestor.executar(
					null,
					getNameOfServiceToBeTested(),
					args);
						InfoSiteExamMap siteExamMap =
							(InfoSiteExamMap) result.getComponent();
						InfoExamsMap examsMap =
							(InfoExamsMap) siteExamMap.getInfoExamsMap();
						List executionCourses = examsMap.getExecutionCourses();
						int size = executionCourses.size();
						int n_exams=0;
						for (int i = 0; i < size; i++) {
							InfoExecutionCourse executionCourse =
								(InfoExecutionCourse) executionCourses.get(i);
							List exams = executionCourse.getAssociatedInfoExams();
							n_exams = exams.size();
							for (int j = 0; j < n_exams; j++) {
								InfoExam exam = (InfoExam) exams.get(j);
							}
						}
			//			compareDataSet("");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"testReadSiteItems was UNSUCCESSFULY runned by class: "
					+ this.getClass().getName());
			fail("testReadSiteItems");
		}

	}

}
