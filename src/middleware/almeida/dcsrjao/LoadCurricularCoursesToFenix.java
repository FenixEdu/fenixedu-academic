package middleware.almeida.dcsrjao;

import java.util.Iterator;
import java.util.List;

import middleware.almeida.Almeida_disc;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import Dominio.IDegreeCurricularPlan;
import Util.DegreeCurricularPlanState;

/**
 * @author dcs-rjao
 *
 * 21/Mai/2003
 */

public class LoadCurricularCoursesToFenix extends LoadDataToFenix {

	private static LoadCurricularCoursesToFenix loader = null;
	protected static String logString = "";

	public LoadCurricularCoursesToFenix() {
	}

	public static void main(String[] args) {
		if (loader == null) {
			loader = new LoadCurricularCoursesToFenix();
		}

		loader.migrationStart("LoadCurricularCoursesToFenix");

		List almeida_curricularCourse = loader.persistentObjectOJB.readAllAlmeidaDisc();
		Iterator iterator = almeida_curricularCourse.iterator();
		while (iterator.hasNext()) {
			Almeida_disc almeida_disc = (Almeida_disc) iterator.next();
			loader.processCurricularCourse(almeida_disc);
		}

		loader.migrationEnd("LoadCurricularCoursesToFenix", logString);
	}

	public void processCurricularCourse(Almeida_disc almeida_disc) {

		Integer keyDegree = new Integer("" + almeida_disc.getCodcur());
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(keyDegree, DegreeCurricularPlanState.CONCLUDED_OBJ);

		if (degreeCurricularPlan == null) {
			logString += "ERRO: O plano curricular do curso [" + keyDegree + "] não existe!\n";
			return;
		}

		ICurricularCourse curricularCourse = persistentObjectOJB.readCurricularCourseByCodeAndNameAndDegreeCurricularPlan(almeida_disc.getCoddis(), almeida_disc.getNomedis(), degreeCurricularPlan);
		if (curricularCourse == null) {
			curricularCourse = new CurricularCourse();
			curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
			curricularCourse.setName(almeida_disc.getNomedis());
			curricularCourse.setCode(almeida_disc.getCoddis());
			curricularCourse.setMandatory(new Boolean(false));
			
			
			// TODO DAVID-RICARDO: Como deduzir os seguintes campos? 
//			curricularCourse.setCredits();
//			curricularCourse.setCurricularCourseExecutionScope();
//			curricularCourse.setType();
			
			curricularCourse.setUniversityCode("IST");
									
			if (almeida_disc.getTipo() == 0) {
				curricularCourse.setMandatory(new Boolean(true));
			} else if (almeida_disc.getTipo() == 1) {
				curricularCourse.setMandatory(new Boolean(false));
			}
			
			writeElement(curricularCourse);
		} else {
			logString += "INFO: A disciplina [" + almeida_disc.getCoddis() + "] já existe no Plano Curricular [" + degreeCurricularPlan.getName() + "]\n";
			loader.numberUntreatableElements++;
		}
		processCurricularCourseScope(curricularCourse, almeida_disc);
	}

	private void processCurricularCourseScope(ICurricularCourse curricularCourse, Almeida_disc almeida_disc) {
		IBranch branch = processBranch(almeida_disc);
		if(branch == null){
			return;
		}
		
		ICurricularSemester curricularSemester = processCurricularSemester(almeida_disc);
		if(curricularSemester == null){
			return;
		}

		ICurricularCourseScope curricularCourseScope = persistentObjectOJB.readCurricularCourseScopeByUnique(curricularCourse, branch, curricularSemester);
		if (curricularCourseScope == null) {
			curricularCourseScope = new CurricularCourseScope();
			curricularCourseScope.setBranch(branch);
			curricularCourseScope.setCurricularCourse(curricularCourse);
			curricularCourseScope.setCurricularSemester(curricularSemester);
			curricularCourseScope.setMaxIncrementNac(new Integer(2));
			curricularCourseScope.setMinIncrementNac(new Integer(1));
			curricularCourseScope.setWeigth(new Integer(1));
			curricularCourseScope.setPraticalHours(new Double(almeida_disc.getPra()));
			curricularCourseScope.setTheoPratHours(new Double(almeida_disc.getTeo()));
			curricularCourseScope.setLabHours(new Double(almeida_disc.getLab()));
			curricularCourseScope.setTheoPratHours(new Double(almeida_disc.getTeopra()));
			writeElement(curricularCourseScope);
		}else{
			logString += "ERRO: O scope com o curricular course [" + curricularCourse.getCode()+
						"] branch [" + branch.getCode() +
						"] semester [" + curricularSemester.getSemester().intValue() + 
						"] ano [" + curricularSemester.getCurricularYear().getYear().intValue() + 
						"] já existe!\n";
		}		
	}
	
	private ICurricularSemester processCurricularSemester(Almeida_disc almeida_disc) {
		Integer semester = new Integer("" + almeida_disc.getSemdis());
		Integer year = new Integer("" + almeida_disc.getAnodis());

		ICurricularYear curricularYear = persistentObjectOJB.readCurricularYear(year);
		if (curricularYear == null) {
			logString += "ERRO: O curricularYear [" + year + "] não existe!\n";
			return null;
		}
		ICurricularSemester curricularSemester = persistentObjectOJB.readCurricularSemester(semester, curricularYear);
		if (curricularSemester == null) {
			logString += "ERRO: O curricularSemester [" + semester + "] não existe!\n";
			return null;
		}
		return curricularSemester;
	}

	private IBranch processBranch(Almeida_disc almeida_disc) {
		
		String code = "";
		if (almeida_disc.getCodram() != 0) {
			// FIXME DAVID-RICARDO: Onde esta a informacao do perfil no almeida_disc?
			code = code + almeida_disc.getCodcur() + almeida_disc.getCodram() + "0";
		}
		
		IDegreeCurricularPlan degreeCurricularPlan = persistentObjectOJB.readDegreeCurricularPlanByDegreeKeyAndState(new Integer("" + almeida_disc.getCodcur()), DegreeCurricularPlanState.CONCLUDED_OBJ);
		if(degreeCurricularPlan == null){
			return null;
		}
		
		IBranch branch = persistentObjectOJB.readBranchByUnique(code, degreeCurricularPlan);
		if (branch == null) {
			logString += "ERRO: O ramo com o code [" + code + "] e plano curricular [" + degreeCurricularPlan.getName() + "] não existe!\n";
			return null;
		}
		return branch;		
	}


	protected String getFilenameOutput() {
		return "etc/migration/dcs-rjao/logs/LoadCurricularCoursesToFenix.txt";
	}

//	private String processCurricularCourseCode(String code) {
//		if ((code.equals("24")) || (code.equals("P6")) || (code.equals("QJ"))) {
//			code = "QN";
//		} else if ((code.equals("AG")) || (code.equals("QA")) || (code.equals("QM")) || (code.equals("AC0"))) {
//			code = "PY";
//		} else if ((code.equals("AH")) || (code.equals("QF")) || (code.equals("PS")) || (code.equals("AC1"))) {
//			code = "P5";
//		} else if ((code.equals("AJ")) || (code.equals("S6")) || (code.equals("UY")) || (code.equals("AC2"))) {
//			code = "UN";
//		} else if ((code.equals("AK")) || (code.equals("V5"))) {
//			code = "U8";
//		} else if (code.equals("8Z")) {
//			code = "AV7";
//		} else if (code.equals("BG")) {
//			code = "APS";
//		} else if (code.equals("2R")) {
//			code = "AME";
//		} else if (code.equals("9R")) {
//			code = "AR7";
//		} else if (code.equals("ALG")) {
//			code = "AP9";
//		} else if (code.equals("Z7")) {
//			code = "C4";
//		} else if (code.equals("ZD")) {
//			code = "C5";
//		} else if (code.equals("2S")) {
//			code = "AMG";
//		} else if (code.equals("2U")) {
//			code = "AMD";
//		} else if (code.equals("9S")) {
//			code = "7W";
//		} else if ((code.equals("UP")) || (code.equals("UZ"))) {
//			code = "SF";
//		} else if (code.equals("A5H")) {
//			code = "AMH";
//		} else if (code.equals("A5Y")) {
//			code = "AR8";
//		} else if (code.equals("QW")) {
//			code = "HU";
//		} else if ((code.equals("V4")) || (code.equals("HV"))) {
//			code = "AJM";
//		}
//
//		return code;
//	}
}