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

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Country;
import Dominio.IBranch;
import Dominio.ICountry;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionPeriod;
import Dominio.IPessoa;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Pessoa;
import Dominio.StudentCurricularPlan;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * 
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
			System.out.println("Students to update: [" + numberOfStudents.toString() + "]");
			sp.confirmarTransaccao();
			int numberOfElementsInSpan = UpdateStudent.NUMBER_OF_ELEMENTS_IN_SPAN;
			
			int numberOfSpans = numberOfStudents.intValue() / numberOfElementsInSpan;
			numberOfSpans =  numberOfStudents.intValue() % numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			
			for (int span = 0; span < numberOfSpans; span++)
			{
				sp.iniciarTransaccao();
				sp.clearCache();
				sp.confirmarTransaccao();
//				System.gc();

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
		
		IStudent student = persistentStudent.readStudentByNumberAndDegreeType(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);
		
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
			if (!studentCurricularPlan.getDegreeCurricularPlan().equals(degreeCurricularPlan))
			{
				if(!studentCurricularPlan.getDegreeCurricularPlan().getName().equals("LEIC 2003") || !degreeCurricularPlan.getName().equals("LEIC - Currículo Antigo"))
				{
					System.out.print("The Student [" + mwStudent.getNumber() + "] has changed his degree!");
					System.out.println(" [" + studentCurricularPlan.getDegreeCurricularPlan().getName() + " -> " + degreeCurricularPlan.getName() + "]");

					sp.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);	
					studentCurricularPlan.setCurrentState(StudentCurricularPlanState.INCOMPLETE_OBJ);
					UpdateStudent.createNewStudentCurricularPlan(student, degreeCurricularPlan, branch, sp);
				}
			} else if (!studentCurricularPlan.getBranch().equals(branch))
			{
				sp.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);	
				studentCurricularPlan.setBranch(branch);
			}		
		}

		// If the person has one master degree curricular plan associated then we cannot change his information.
		// This means that the person has a Degree Student and a Master Degree Student
		// We admit that in this case the Master Degree information is the most recent one and therefore we won't change 
		// his information

		// All the students associated to this person
//		List studentList = persistentStudent.readbyPerson(student.getPerson());
//
//		if (UpdateStudent.hasMasterDegree(studentList, sp))
//		{
//			System.out.println("Master Degree Student Found [Person ID " + student.getPerson().getIdInternal() + "]");
//		} else
//		{
//			// Change all the information
//			UpdateStudent.updateStudentPerson(student.getPerson(), mwStudent.getMiddlewarePerson());
//		}
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

//		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
//		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();
//
//		MWBranch mwBranch = persistentBranch.readByDegreeCodeAndBranchCode(mwStudent.getDegreecode(), new Integer(0));
//		
//		String degreeName = StringUtils.substringAfter(mwBranch.getDescription(), "DE ");
//		
//		if (degreeName.indexOf("TAGUS") != -1) {
//			degreeName = "Engenharia Informática e de Computadores - Taguspark";
//		}
//
//		IExecutionPeriod executionPeriod = sp.getIPersistentExecutionPeriod().readActualExecutionPeriod();
//		ICursoExecucao executionDegree = sp.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(degreeName, executionPeriod.getExecutionYear(), TipoCurso.LICENCIATURA_OBJ);
//
//		return executionDegree.getCurricularPlan();
	}

	private static IBranch getBranch(MWStudent mwStudent, IDegreeCurricularPlan degreeCurricularPlan, ISuportePersistente sp) throws Throwable
	{
//		IBranch branch = null;
//		
//		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
//		IPersistentMWBranch persistentMWBranch = mws.getIPersistentMWBranch();
//		IPersistentBranch persistentBranch = sp.getIPersistentBranch();
//
//		MWBranch mwBranch = persistentMWBranch.readByDegreeCodeAndBranchCode(mwStudent.getDegreecode(), mwStudent.getBranchcode());
//
//		if (mwBranch != null) {
//			String realBranchCode = null;
//
//			if (mwBranch.getDescription().startsWith("CURSO DE ")) {
//				realBranchCode = new String("");
//			} else {
//				realBranchCode = new String(mwBranch.getDegreecode().toString() + mwBranch.getBranchcode().toString() + mwBranch.getOrientationcode().toString());
//			}
//
//			branch = persistentBranch.readByDegreeCurricularPlanAndCode(degreeCurricularPlan, realBranchCode);
//
//		} else {
//			branch = CreateAndUpdateAllPastCurriculums.solveBranchesProblemsForDegrees1And4And6And51And53And54And64(mwStudent.getDegreecode(), mwStudent.getBranchcode(), degreeCurricularPlan, persistentBranch);
//		}
//
//		return branch;

		IBranch branch = null;
		
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();
		
		sp.clearCache();
		MWBranch mwbranch = persistentBranch.readByDegreeCodeAndBranchCode(mwStudent.getDegreecode(), mwStudent.getBranchcode());
		
		if (mwbranch == null) {
			System.out.println("Aluno " + mwStudent.getNumber());
			System.out.println("Curso " + mwStudent.getDegreecode());
			System.out.println("Ramo " + mwStudent.getBranchcode());
		} else
		{
			branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, mwbranch.getDescription());
		}
		
		if (branch == null) {
			branch = sp.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, "");
		}

		if (branch == null) {
			System.out.println("DCP " + degreeCurricularPlan.getName());
			System.out.println("Ramo Inexistente " + mwbranch);
		}
		
		return branch;
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
		fenixPerson.setPais(UpdateStudent.getCountry(oldPerson.getCountrycode().toString()));
		fenixPerson.setSexo(PersonUtils.getSex(oldPerson.getSex()));
		fenixPerson.setTelefone(oldPerson.getPhone());
		fenixPerson.setTipoDocumentoIdentificacao(PersonUtils.getDocumentIdType(oldPerson.getDocumentidtype()));
	}

	public static ICountry getCountry(String countryCode) throws Throwable
	{
		Criteria criteria = new Criteria();

		if (countryCode.equals("01") ||
				countryCode.equals("02") ||
				countryCode.equals("03") ||
				countryCode.equals("04") ||
				countryCode.equals("05") ||
				countryCode.equals("06") ||
				countryCode.equals("1") ||
				countryCode.equals("2") ||
				countryCode.equals("3") ||
				countryCode.equals("4") ||
				countryCode.equals("5") ||
				countryCode.equals("6") ||
				countryCode.equals("0"))
		{
			criteria.addEqualTo("name", "PORTUGAL");
		} else if (countryCode.equals("10"))
		{
			criteria.addEqualTo("name", "ANGOLA");
		} else if (countryCode.equals("11"))
		{
			criteria.addEqualTo("name", "BRASIL");
		} else if (countryCode.equals("12"))
		{
			criteria.addEqualTo("name", "CABO VERDE");
		} else if (countryCode.equals("13"))
		{
			criteria.addEqualTo("name", "GUINE-BISSAO");
		} else if (countryCode.equals("14"))
		{
			criteria.addEqualTo("name", "MOCAMBIQUE");
		} else if (countryCode.equals("15"))
		{
			criteria.addEqualTo("name", "SAO TOME E PRINCIPE");
		} else if (countryCode.equals("16"))
		{
			criteria.addEqualTo("name", "TIMOR LORO SAE");
		} else if (countryCode.equals("20"))
		{
			criteria.addEqualTo("name", "BELGICA");
		} else if (countryCode.equals("21"))
		{
			criteria.addEqualTo("name", "DINAMARCA");
		} else if (countryCode.equals("22"))
		{
			criteria.addEqualTo("name", "ESPANHA");
		} else if (countryCode.equals("23"))
		{
			criteria.addEqualTo("name", "FRANCA");
		} else if (countryCode.equals("24"))
		{
			criteria.addEqualTo("name", "HOLANDA");
		} else if (countryCode.equals("25"))
		{	
			criteria.addEqualTo("name", "IRLANDA");
		} else if (countryCode.equals("26"))
		{
			criteria.addEqualTo("name", "ITALIA");
		} else if (countryCode.equals("27"))
		{
			criteria.addEqualTo("name", "LUXEMBURGO");
		} else if (countryCode.equals("28"))
		{
			criteria.addEqualTo("name", "ALEMANHA");
		} else if (countryCode.equals("29"))
		{
			criteria.addEqualTo("name", "REINO UNIDO");
		} else if (countryCode.equals("30"))
		{
			criteria.addEqualTo("name", "SUECIA");
		} else if (countryCode.equals("31"))
		{
			criteria.addEqualTo("name", "NORUEGA");
		} else if (countryCode.equals("32"))
		{
			criteria.addEqualTo("name", "POLONIA");
		} else if (countryCode.equals("33"))
		{
			criteria.addEqualTo("name", "AFRICA DO SUL");
		} else if (countryCode.equals("34"))
		{
			criteria.addEqualTo("name", "ARGENTINA");
		} else if (countryCode.equals("35"))
		{
			criteria.addEqualTo("name", "CANADA");
		} else if (countryCode.equals("36"))
		{
			criteria.addEqualTo("name", "CHILE");
		} else if (countryCode.equals("37"))
		{
			criteria.addEqualTo("name", "EQUADOR");
		} else if (countryCode.equals("38"))
		{
			criteria.addEqualTo("name", "ESTADOS UNIDOS DA AMERICA");
		} else if (countryCode.equals("39"))
		{
			criteria.addEqualTo("name", "IRAO");
		} else if (countryCode.equals("40"))
		{
			criteria.addEqualTo("name", "MARROCOS");
		} else if (countryCode.equals("41"))
		{
			criteria.addEqualTo("name", "VENEZUELA");
		} else if (countryCode.equals("42"))
		{
			criteria.addEqualTo("name", "AUSTRALIA");
		} else if (countryCode.equals("43"))
		{
			criteria.addEqualTo("name", "PAQUISTAO");
		} else if (countryCode.equals("44"))
		{
			criteria.addEqualTo("name", "REPUBLICA DO ZAIRE");
		} else if (countryCode.equals("47"))
		{
			criteria.addEqualTo("name", "LIBIA");
		} else if (countryCode.equals("48"))
		{
			criteria.addEqualTo("name", "PALESTINA");
		} else if (countryCode.equals("49"))
		{
			criteria.addEqualTo("name", "ZIMBABUE");
		} else if (countryCode.equals("50"))
		{
			criteria.addEqualTo("name", "MEXICO");
		} else if (countryCode.equals("51"))
		{
			criteria.addEqualTo("name", "RUSSIA");
		} else if (countryCode.equals("52"))
		{
			criteria.addEqualTo("name", "AUSTRIA");
		} else if (countryCode.equals("53"))
		{
			criteria.addEqualTo("name", "IRAQUE");
		} else if (countryCode.equals("54"))
		{
			criteria.addEqualTo("name", "PERU");
		} else if (countryCode.equals("55"))
		{
			criteria.addEqualTo("name", "ESTADOS UNIDOS DA AMERICA");
		} else if (countryCode.equals("60"))
		{
			criteria.addEqualTo("name", "ROMENIA");
		} else if (countryCode.equals("61"))
		{
			criteria.addEqualTo("name", "REPUBLICA CHECA");
		} else
		{
			System.out.println("COUNTRY > NULL");
			return null;
		}

		Query query = new QueryByCriteria(Country.class, criteria);

		PersistenceBroker broker = null;
		try {
			SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
			broker = sp.currentBroker();
		} catch (Exception e) {
			System.out.println("Failled obtainning broker.");
		}
		
		List result = (List) broker.getCollectionByQuery(query);
		
		if (result.size() == 0)
		{	
			return null;
		} else
		{	
			return (ICountry) result.get(0);
		}
	}
}