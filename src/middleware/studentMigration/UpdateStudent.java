package middleware.studentMigration;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWDegreeTranslation;
import middleware.middlewareDomain.MWPerson;
import middleware.middlewareDomain.MWStudent;
import middleware.persistentMiddlewareSupport.IPersistentMWAluno;
import middleware.persistentMiddlewareSupport.IPersistentMWBranch;
import middleware.persistentMiddlewareSupport.IPersistentMWDegreeTranslation;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.personMigration.PersonUtils;
import middleware.studentMigration.enrollments.CreateAndUpdateAllPastCurriculums;
import Dominio.IBranch;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Pessoa;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.IPersistentBranch;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class UpdateStudent
{
	private static int NUMBER_OF_ELEMENTS_IN_SPAN = 500;
	
	public static void main(String args[]) throws Exception {

		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWAluno persistentAluno = mws.getIPersistentMWAluno();
		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
		
		MWStudent mwStudent = null;
		
		try {
			sp.iniciarTransaccao();
			Integer numberOfStudents = persistentAluno.countAll();
			sp.confirmarTransaccao();
			int numberOfElementsInSpan = UpdateStudent.NUMBER_OF_ELEMENTS_IN_SPAN;
			
			int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
			numberOfSpans =  numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			
			for (int span = 0; span < numberOfSpans; span++)
			{
				sp.iniciarTransaccao();
				sp.clearCache();
				sp.confirmarTransaccao();
				System.gc();

				sp.iniciarTransaccao();
				System.out.println("Reading Students...");
				List result = persistentAluno.readAllBySpan(new Integer(span), new Integer(numberOfElementsInSpan));
				System.out.println("Updating [" + result.size() + "] students...");
				sp.confirmarTransaccao();		
				
				Iterator iterator = result.iterator();
				while(iterator.hasNext()) {
					mwStudent = (MWStudent) iterator.next();
					sp.iniciarTransaccao();
					UpdateStudent.updateCorrectStudents(mwStudent, sp);
					sp.confirmarTransaccao();
				}
	
			}
		} catch(Throwable e) {
			System.out.println("Error Migrating Student [" + mwStudent.getNumber() + "]");
			e.printStackTrace(System.out);
		}
	}

	public static void updateCorrectStudents(MWStudent mwStudent, SuportePersistenteOJB sp) throws Throwable
	{
		IPersistentStudent persistentStudent = sp.getIPersistentStudent();
		
		IStudent student = persistentStudent.readByNumero(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);
		
		if (student == null) {
			System.out.println("Error Reading Fenix Student [" + mwStudent.getNumber() + "]!");
			return;
		}

		IDegreeCurricularPlan degreeCurricularPlan = UpdateStudent.getDegreeCurricularPlan(mwStudent, sp);
		if (degreeCurricularPlan == null) {
			System.out.println("Error Reading Fenix degreeCurricularPlan for degree [" + mwStudent.getDegreecode() + "] and student [" + mwStudent.getNumber() + "]!");
			return;
		}
		IBranch branch = UpdateStudent.getBranch(mwStudent, degreeCurricularPlan, sp);
		if (branch == null) {
			System.out.println("Error Reading Fenix branch with code [" + mwStudent.getBranchcode() + "] for student [" + mwStudent.getNumber() + "]!");
			return;
		}
		
		IStudentCurricularPlan studentCurricularPlan = sp.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(student.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (studentCurricularPlan == null)
		{
			System.out.println("No Active Student Curricular Plan for Student [" + student.getNumber() + "]!");
			UpdateStudent.createNewStudentCurricularPlan(student, degreeCurricularPlan, branch, sp);
		} else
		{
			sp.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);	
	
			if (!studentCurricularPlan.getDegreeCurricularPlan().equals(degreeCurricularPlan))
			{
				System.out.print("The Student [" + mwStudent.getNumber() + "] has changed his degree!");
				System.out.println("[" + studentCurricularPlan.getDegreeCurricularPlan().getName() + " -> " + degreeCurricularPlan.getName() + "]");
				studentCurricularPlan.setCurrentState(StudentCurricularPlanState.INCOMPLETE_OBJ);
//				studentCurricularPlan.setBranch(branch);
				UpdateStudent.createNewStudentCurricularPlan(student, degreeCurricularPlan, branch, sp);
			} else
			{
				studentCurricularPlan.setBranch(branch);
			}		
		}

		// If the person has one master degree curricular plan associated then we cannot change his information.
		// This means that the person has a Degree Student and a Master Degree Student
		// We admit that in this case the Master Degree information is the most recent one and therefore we won't change 
		// his information

		// All the students associated to this person
		List studentList = persistentStudent.readbyPerson(student.getPerson());

		if (UpdateStudent.hasMasterDegree(studentList, sp))
		{
			System.out.println("Master Degree Student Found [Person ID " + student.getPerson().getIdInternal() + "]");
		} else
		{
			// Change all the information
			UpdateStudent.updateStudentPerson(student.getPerson(), mwStudent.getMiddlewarePerson());
		}
	}
	
	/**
	 * @param student
	 * @param degreeCurricularPlan
	 * @param branch
	 * @param sp
	 */
	private static void createNewStudentCurricularPlan(IStudent student, IDegreeCurricularPlan degreeCurricularPlan, IBranch branch, ISuportePersistente sp) throws Throwable
	{
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		sp.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);
		studentCurricularPlan.setBranch(branch);
		studentCurricularPlan.setClassification(new Double(0));
		studentCurricularPlan.setCompletedCourses(new Integer(0));
		studentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE_OBJ);
		studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);
		studentCurricularPlan.setEnrolledCourses(new Integer(0));
		studentCurricularPlan.setEnrolments(null);
		studentCurricularPlan.setGivenCredits(new Double(0));
		studentCurricularPlan.setSpecialization(null);
		studentCurricularPlan.setStartDate(Calendar.getInstance().getTime());
		studentCurricularPlan.setStudent(student);
	}

	/**
	 * @param studentList
	 * @return boolean 
	 */
	private static boolean hasMasterDegree(List studentList, ISuportePersistente sp) throws Throwable
	{
		Iterator iterator = studentList.iterator();
		while(iterator.hasNext())
		{
			IStudent student = (IStudent) iterator.next();
			
			List studentCurricularPlanList = sp.getIStudentCurricularPlanPersistente().readAllFromStudent(student.getNumber().intValue());
			Iterator iterator2 = studentCurricularPlanList.iterator();
			while(iterator2.hasNext())
			{
				IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator2.next();
				if (studentCurricularPlan.getDegreeCurricularPlan().getDegree().getTipoCurso().equals(TipoCurso.MESTRADO_OBJ))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param mwStudent
	 * @return the New Degree Curricular Plan
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(MWStudent mwStudent, ISuportePersistente sp) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(mwStudent.getDegreecode());

		if (mwDegreeTranslation != null)
		{
			String degreeName = mwDegreeTranslation.getDegree().getNome();
			IExecutionPeriod executionPeriod = sp.getIPersistentExecutionPeriod().readActualExecutionPeriod();
			ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(degreeName, executionPeriod.getExecutionYear(), TipoCurso.LICENCIATURA_OBJ);
			return executionDegree.getCurricularPlan();
		} else {
			return null;
		}
	}

	private static IBranch getBranch(MWStudent mwStudent, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente sp) throws Throwable
	{
		IBranch branch = null;
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentMWBranch = mws.getIPersistentMWBranch();
		IPersistentBranch persistentBranch = sp.getIPersistentBranch();

		MWBranch mwBranch = persistentMWBranch.readByDegreeCodeAndBranchCode(mwStudent.getDegreecode(), mwStudent.getBranchcode());

		if (mwBranch != null) {
			String realBranchCode = null;

			if (mwBranch.getDescription().startsWith("CURSO DE ")) {
				realBranchCode = new String("");
			} else {
				realBranchCode = new String(mwBranch.getDegreecode().toString() + mwBranch.getBranchcode().toString() + mwBranch.getOrientationcode().toString());
			}

			branch = persistentBranch.readByDegreeCurricularPlanAndCode(degreeCurricularPlan, realBranchCode);

		} else {
			branch = CreateAndUpdateAllPastCurriculums.solveBranchesProblemsForDegrees1And4And6And51And53And54And64(mwStudent.getDegreecode(), mwStudent.getBranchcode(), degreeCurricularPlan, persistentBranch);
		}

		return branch;

//		IBranch branch = null;
//		
//		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
//		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();
//		
//		
//		// Get the old BRanch
//		
//		sp.clearCache();
//		MWBranch mwbranch = persistentBranch.readByDegreeCodeAndBranchCode(oldStudent.getDegreecode(), oldStudent.getBranchcode());
//		
//		// Get the new one		
//		
//		
//		if (mwbranch == null) {
//			System.out.println("Aluno " + oldStudent.getNumber());
//			System.out.println("Curso " + oldStudent.getDegreecode());
//			System.out.println("Ramo " + oldStudent.getBranchcode());
//		}
//		
//		branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, mwbranch.getDescription());
//
//
//		if (branch == null) {
//			branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, "");
//		}
//
//		if (branch == null) {
//			System.out.println("DCP " + degreeCurricularPlan.getName());
//			System.out.println("Ramo Inexistente " + mwbranch);
//		}
//		
//		return branch;
	}

	public static void updateStudentPerson(IPessoa fenixPersonTemp, MWPerson oldPerson) throws Throwable
	{
		SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();

		IPessoa personTemp = new Pessoa();
		personTemp.setIdInternal(fenixPersonTemp.getIdInternal());

		IPessoa fenixPerson = (IPessoa) sp.getIPessoaPersistente().readByOId(personTemp, true);

		if (fenixPerson == null) {
			System.out.println("Person not Found !");
			return;
		}

		sp.getIPessoaPersistente().simpleLockWrite(fenixPerson);
		fenixPerson.setCodigoFiscal(oldPerson.getFinancialrepcode());
		fenixPerson.setCodigoPostal(oldPerson.getZipcode());
		fenixPerson.setConcelhoMorada(oldPerson.getMunicipalityofaddress());
		fenixPerson.setConcelhoNaturalidade(oldPerson.getMunicipalityofbirth());
		fenixPerson.setDataEmissaoDocumentoIdentificacao(oldPerson.getDocumentiddate());
		fenixPerson.setDataValidadeDocumentoIdentificacao(oldPerson.getDocumentidvalidation());
		fenixPerson.setDistritoMorada(oldPerson.getDistrictofaddress());
		fenixPerson.setDistritoNaturalidade(oldPerson.getDistrictofbirth());
		fenixPerson.setEstadoCivil(PersonUtils.getMaritalStatus(oldPerson.getMaritalstatus()));
		fenixPerson.setFreguesiaMorada(oldPerson.getParishofaddress());
		fenixPerson.setFreguesiaNaturalidade(oldPerson.getParishofbirth());
		fenixPerson.setLocalEmissaoDocumentoIdentificacao(oldPerson.getDocumentidplace());
		fenixPerson.setLocalidadeCodigoPostal(oldPerson.getAddressAreaCode());
		fenixPerson.setMorada(oldPerson.getAddress());
		fenixPerson.setNascimento(oldPerson.getDateofbirth());
		fenixPerson.setNome(oldPerson.getName());
		fenixPerson.setNomeMae(oldPerson.getMothername());
		fenixPerson.setNomePai(oldPerson.getFathername());
		fenixPerson.setNumContribuinte(oldPerson.getFiscalcode());
		fenixPerson.setNumeroDocumentoIdentificacao(oldPerson.getDocumentidnumber());
		fenixPerson.setPais(PersonUtils.getCountry(oldPerson.getCountrycode()));
		fenixPerson.setSexo(PersonUtils.getSex(oldPerson.getSex()));
		fenixPerson.setTelefone(oldPerson.getPhone());
		fenixPerson.setTipoDocumentoIdentificacao(PersonUtils.getDocumentIdType(oldPerson.getDocumentidtype()));
	}
}