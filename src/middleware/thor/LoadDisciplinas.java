/*
 * Created on May 15, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package middleware.thor;

import java.util.StringTokenizer;

import middleware.LoadDataFile;
import middleware.almeida.Almeida_coddisc;
import middleware.almeida.Almeida_curram;
import middleware.almeida.Almeida_disc;
import Dominio.Branch;
import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.DegreeCurricularPlan;
import Dominio.IBranch;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurricularSemester;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Util.CurricularCourseExecutionScope;
import Util.CurricularCourseType;
import Util.DegreeCurricularPlanState;

/**
 *
 * @author  Luis Cruz & Sara Ribeiro
 */
public class LoadDisciplinas extends LoadDataFile {

	PersistentObjectOJBReader persistentObjectOJB = null;

	static String bufferToWrite = new String();

	private static LoadDisciplinas loader = null;

	private LoadDisciplinas() {
		super();
		persistentObjectOJB = new PersistentObjectOJBReader();
	}

	public static void main(String[] args) {
		loader = new LoadDisciplinas();
		loader.load();

		loader.writeToFile(bufferToWrite);
	}

	protected void processLine(String line) {
		StringTokenizer stringTokenizer =
			new StringTokenizer(line, getFieldSeparator());

		String anoLectivo = stringTokenizer.nextToken(); // New

		if (anoLectivo.equals("2003")) {

			String codigoDisciplina = stringTokenizer.nextToken();
			String codigoCurso = stringTokenizer.nextToken();

			if ((new Integer(codigoCurso)).intValue() < 24) {

				String codigoRamo = stringTokenizer.nextToken();
				String ano = stringTokenizer.nextToken();
				String semestre = stringTokenizer.nextToken();
				String tipoDisciplina = stringTokenizer.nextToken();
				// (opção ou não)
				String teorica = stringTokenizer.nextToken().replace(',', '.');
				String pratica = stringTokenizer.nextToken().replace(',', '.');
				String laboratorio =
					stringTokenizer.nextToken().replace(',', '.');
				String teoricopratica =
					stringTokenizer.nextToken().replace(',', '.');
				String creditos = stringTokenizer.nextToken().replace(',', '.');
				// New
				String orientacao = stringTokenizer.nextToken(); // New

				Almeida_disc almeida_disc = new Almeida_disc();
				almeida_disc.setCodint(loader.numberElementsWritten + 1);
				if (codigoCurso.charAt(0) == '0')
					codigoCurso.substring(1);
				almeida_disc.setCodcur((new Integer(codigoCurso)).longValue());
				almeida_disc.setCodram((new Integer(codigoRamo)).longValue());
				almeida_disc.setAnodis((new Integer(ano)).longValue());
				almeida_disc.setSemdis((new Integer(semestre)).longValue());
				almeida_disc.setCoddis(codigoDisciplina);
				almeida_disc.setTipo((new Integer(tipoDisciplina)).longValue());
				almeida_disc.setTeo((new Double(teorica)).doubleValue());
				almeida_disc.setTeopra(
					(new Double(teoricopratica)).doubleValue());
				almeida_disc.setPra((new Double(pratica)).doubleValue());
				almeida_disc.setLab((new Double(laboratorio)).doubleValue());
				almeida_disc.setCredits((new Double(creditos)).doubleValue());
				try {
					almeida_disc.setOrientation(
						(new Integer(orientacao)).longValue());
				} catch (NumberFormatException ex) {
					almeida_disc.setOrientation(0);
				}

				Almeida_coddisc almeida_coddisc =
					persistentObjectOJB.readAlmeidaCoddisc(codigoDisciplina);
				almeida_disc.setNomedis(almeida_coddisc.getNomedis());

				//writeElement(almeida_disc);
				processCurricularCourse(almeida_disc);
			}
		}
	}

	private void processCurricularCourse(Almeida_disc almeida_disc) {

		IDegreeCurricularPlan degreeCurricularPlan =
			getDegreeCurricularPlan(new Integer("" + almeida_disc.getCodcur()));

		if (degreeCurricularPlan != null) {

			//System.out.println("Args= " + degreeCurricularPlan.getIdInternal() + " - " + almeida_disc.getCoddis());

			ICurricularCourse curricularCourse =
				persistentObjectOJB.readCurricularCourse(
					degreeCurricularPlan,
					almeida_disc.getCoddis());

			//System.out.println("curricularCourse= " + curricularCourse);

			if (curricularCourse == null) {
				curricularCourse = new CurricularCourse();
				curricularCourse.setCode(almeida_disc.getCoddis());
				curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
				curricularCourse.setCurricularCourseExecutionScope(
					getCurricularCourseExecutionScope(almeida_disc));
				curricularCourse.setName(almeida_disc.getNomedis());
				if (almeida_disc.getTipo() == 0) {
					curricularCourse.setType(
						new CurricularCourseType(
							CurricularCourseType.NORMAL_COURSE));
				} else if (almeida_disc.getTipo() == 1) {
					curricularCourse.setType(
						new CurricularCourseType(
							CurricularCourseType.OPTIONAL_COURSE));
				}				
				processCurricularCourseScope(curricularCourse, almeida_disc);
				
				curricularCourse.setTheoreticalHours(new Double(almeida_disc.getTeo()));
				curricularCourse.setTheoPratHours(new Double(almeida_disc.getTeopra()));
				curricularCourse.setPraticalHours(new Double(almeida_disc.getPra()));
				curricularCourse.setLabHours(new Double(almeida_disc.getLab()));
				curricularCourse.setMaximumValueForAcumulatedEnrollments(null);
				curricularCourse.setMinimumValueForAcumulatedEnrollments(null);				
				curricularCourse.setCredits(
						new Double(almeida_disc.getCredits()));
				curricularCourse.setEctsCredits(null);
				curricularCourse.setEnrollmentWeigth(null);
				
				curricularCourse.setMandatory(null); //	not available
				writeElement(curricularCourse);
				numberElementsWritten++;
			}
		} else {
			bufferToWrite
				+= "Unable to obtain degree curricular plan for degree: ["
				+ almeida_disc.getCodcur()
				+ "]";
		}
	}

	/**
	 * @param almeida_disc
	 * @return
	 */
	private CurricularCourseExecutionScope getCurricularCourseExecutionScope(Almeida_disc almeida_disc) {
		// TODO : Maybe there is a way to know this.
		//        Ask around.
		return new CurricularCourseExecutionScope(
			CurricularCourseExecutionScope.SEMESTRIAL);
	}

	/**
	 * @param integer
	 * @return
	 */
	private IDegreeCurricularPlan getDegreeCurricularPlan(Integer idDegree) {
		IDegreeCurricularPlan degreeCurricularPlan = null;

		ICurso degree = persistentObjectOJB.readDegree(idDegree);
		if (degree != null) {
			degreeCurricularPlan =
				persistentObjectOJB.readDegreeCurricularPlan(
					degree.getSigla(),
					"2003/2004");

			if (degreeCurricularPlan == null) {
				degreeCurricularPlan = new DegreeCurricularPlan();
				degreeCurricularPlan.setDegree(degree);
				degreeCurricularPlan.setName(degree.getSigla() + "2003/2004");

				degreeCurricularPlan.setDegreeDuration(null); //not available
				degreeCurricularPlan.setInitialDate(null); //not available
				degreeCurricularPlan.setEndDate(null); //not available
				degreeCurricularPlan.setMinimalYearForOptionalCourses(null);
				//not available
				degreeCurricularPlan.setState(
					new DegreeCurricularPlanState(
						DegreeCurricularPlanState.NOT_ACTIVE));
				writeElement(degreeCurricularPlan);
				numberElementsWritten++;
			}

		} else {
			bufferToWrite += "Error reading degree: [" + idDegree + "]\n";
			numberUntreatableElements++;
		}

		return degreeCurricularPlan;
	}

	private void processCurricularCourseScope(
		ICurricularCourse curricularCourse,
		final Almeida_disc almeida_disc) {

		ICurricularCourseScope curricularCourseScope =
			new CurricularCourseScope();
		curricularCourseScope.setBranch(
			getBranch(almeida_disc, curricularCourse));
		curricularCourseScope.setCurricularCourse(curricularCourse);
		curricularCourseScope.setCurricularSemester(
			makeCurricularSemester(
				almeida_disc.getAnodis(),
				almeida_disc.getSemdis()));

		//	TODO: add sets for begin and end dates and ectsCredits
		
		// Write this elemente
		writeElement(curricularCourseScope);
		numberElementsWritten++;
	}

	/**
	 * @param almeida_curram
	 * @return
	 */
	private IBranch getBranch(
		Almeida_disc almeida_disc,
		ICurricularCourse curricularCourse) {
		Almeida_curram almeida_curram =
			persistentObjectOJB.readAlmeida_Curram(
				new Integer("" + almeida_disc.getCodcur()),
				new Integer("" + almeida_disc.getCodram()));

		if (almeida_curram != null) {

			/*			System.out.println("Reading Branch: ");
						System.out.println("  Descri: [" + almeida_curram.getDescri() + "}");
						System.out.println("  Code:   [" + ""
			                                                + almeida_curram.getCodcur()
			                                                + almeida_curram.getCodram()
			                                                + almeida_curram.getCodorien() + "}");
			*/
			IBranch branch =
				persistentObjectOJB.readBranch(
					almeida_curram.getDescri(),
					""
						+ almeida_curram.getCodcur()
						+ almeida_curram.getCodram()
						+ almeida_curram.getCodorien());

			//			System.out.println("Result of read: " + branch);

			if (branch == null) {
				branch = new Branch();
				branch.setCode(
					""
						+ almeida_curram.getCodcur()
						+ almeida_curram.getCodram()
						+ almeida_curram.getCodorien());
				branch.setName(almeida_curram.getDescri());
				branch.setDegreeCurricularPlan(
					curricularCourse.getDegreeCurricularPlan());
				branch.setScopes(null);
				writeElement(branch);
				numberElementsWritten++;
			}
			return branch;
		} else {
			bufferToWrite += "Error creating branch: ["
				+ almeida_disc.getCodram()
				+ "]\n";
			numberUntreatableElements++;
			return null;
		}
	}

	/**
	 * @param l
	 * @return
	 */
	private ICurricularSemester makeCurricularSemester(
		long year,
		long semester) {
		return persistentObjectOJB.readCurricularSemester(
			new Integer("" + year),
			new Integer("" + semester));
	}

	protected String getFilename() {
		return "etc/migrationNextSemester/DISCIPLINAS.TXT";
	}

	protected String getFieldSeparator() {
		return "\t";
	}

	/* (non-Javadoc)
	 * @see middleware.almeida.LoadDataFile#getFilenameOutput()
	 */
	protected String getFilenameOutput() {

		return "etc/migrationNextSemester/DISCIPLINAS_PROBLEM.TXT";
	}

}
