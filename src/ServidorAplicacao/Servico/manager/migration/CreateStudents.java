package ServidorAplicacao.Servico.manager.migration;

import java.io.FileOutputStream;
import java.io.PrintWriter;
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

import pt.utl.ist.berserk.logic.serviceManager.IService;
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
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.StudentCurricularPlanState;
import Util.TipoCurso;

/**
 * @author David Santos in Feb 13, 2004
 */

public class CreateStudents extends CreateUpdateDeleteEnrollmentsInCurrentStudentCurricularPlans implements IService
{
	public CreateStudents()
	{
		try
		{
			super.persistentSuport = SuportePersistenteOJB.getInstance();
			super.persistentMiddlewareSuport = PersistentMiddlewareSupportOJB.getInstance();
		} catch (ExcepcaoPersistencia e)
		{
			e.printStackTrace(System.out);
		}

		super.executionPeriod = null;
		super.out = null;
		super.numberOfElementsInSpan = 50;
		super.maximumNumberOfElementsToConsider = 10000;
	}

	public void run(Boolean toLogToFile, String fileName)
	{
		MWStudent mwStudent = null;

		try
		{
			super.out = new PrintWriter(System.out, true);
			if (toLogToFile.booleanValue())
			{
				FileOutputStream file = new FileOutputStream(fileName);
				super.out = new PrintWriter(file);
			}

			IPersistentMWAluno mwAlunoDAO = super.persistentMiddlewareSuport.getIPersistentMWAluno();

			Integer numberOfStudents = mwAlunoDAO.countAll();
			super.out.println("[INFO] Updating a total of [" + numberOfStudents.intValue() + "] student curriculums.");

			int numberOfSpans = numberOfStudents.intValue() / super.numberOfElementsInSpan;
			numberOfSpans = numberOfStudents.intValue() % super.numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			int totalElementsConsidered = 0;

			for (int span = 0; span < numberOfSpans && totalElementsConsidered < super.maximumNumberOfElementsToConsider; span++)
			{
				super.out.println("[INFO] Reading MWStudents...");

				Iterator iterator = mwAlunoDAO.readAllBySpanIterator(new Integer(span), new Integer(super.numberOfElementsInSpan));

				while (iterator.hasNext())
				{
					mwStudent = (MWStudent) mwAlunoDAO.lockIteratorNextObj(iterator);

					this.updateStudents(mwStudent);

					totalElementsConsidered++;
				}
			}
		} catch (Throwable e)
		{
			super.out.println("[ERROR 01] Exception migrating student [" + mwStudent.getNumber() + "] enrolments!");
			e.fillInStackTrace();
			e.printStackTrace(super.out);
		}

		super.out.close();
	}

	/**
	 * @param mwStudent
	 * @throws Throwable
	 */
	public void updateStudents(MWStudent mwStudent) throws Throwable
	{
		IPersistentStudent persistentStudent = super.persistentSuport.getIPersistentStudent();

		IStudent student = persistentStudent.readStudentByNumberAndDegreeType(mwStudent.getNumber(), TipoCurso.LICENCIATURA_OBJ);

		if (student == null)
		{
			System.out.println("[ERROR 02] Reading Fenix Student [" + mwStudent.getNumber() + "]!");
			return;
		}

		IDegreeCurricularPlan degreeCurricularPlan = this.getDegreeCurricularPlan(mwStudent);
		if (degreeCurricularPlan == null)
		{
			System.out.println(
				"[ERROR 03] Reading Fenix degreeCurricularPlan for degree ["
					+ mwStudent.getDegreecode()
					+ "] and student ["
					+ mwStudent.getNumber()
					+ "]!");
			return;
		}
		IBranch branch = this.getBranch(mwStudent, degreeCurricularPlan);
		if (branch == null)
		{
			System.out.println(
				"[ERROR 04] Reading Fenix branch with code ["
					+ mwStudent.getBranchcode()
					+ "] for student ["
					+ mwStudent.getNumber()
					+ "]!");
			return;
		}

		IStudentCurricularPlan studentCurricularPlan =
			super.persistentSuport.getIStudentCurricularPlanPersistente().readActiveStudentCurricularPlan(
				student.getNumber(),
				TipoCurso.LICENCIATURA_OBJ);

		if (studentCurricularPlan == null)
		{
			System.out.println("[ERROR 05] No Active Student Curricular Plan for Student [" + student.getNumber() + "]!");
			this.createNewStudentCurricularPlan(student, degreeCurricularPlan, branch);
		} else
		{
			if (!studentCurricularPlan.getDegreeCurricularPlan().equals(degreeCurricularPlan))
			{
				if (!studentCurricularPlan.getDegreeCurricularPlan().getName().equals("LEIC 2003")
					|| !degreeCurricularPlan.getName().equals("LEIC - Currículo Antigo"))
				{
					System.out.print("[INFO] The Student [" + mwStudent.getNumber() + "] has changed his degree!");
					System.out.println(
						" ["
							+ studentCurricularPlan.getDegreeCurricularPlan().getName()
							+ " -> "
							+ degreeCurricularPlan.getName()
							+ "]");

					super.persistentSuport.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);
					studentCurricularPlan.setCurrentState(StudentCurricularPlanState.INCOMPLETE_OBJ);
					this.createNewStudentCurricularPlan(student, degreeCurricularPlan, branch);
				}
			} else if (!studentCurricularPlan.getBranch().equals(branch))
			{
				super.persistentSuport.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);
				studentCurricularPlan.setBranch(branch);
			}
		}

		// If the person has one master degree curricular plan associated then we cannot change his information.
		// This means that the person has a Degree Student and a Master Degree Student
		// We admit that in this case the Master Degree information is the most recent one and therefore we won't change
		// his information

		// All the students associated to this person
		List studentList = persistentStudent.readbyPerson(student.getPerson());

		if (this.hasMasterDegree(studentList))
		{
			System.out.println("[INFO] Master Degree Student Found [Person ID " + student.getPerson().getIdInternal() + "]");
		} else
		{
			// Change all the information
			this.updateStudentPerson(student.getPerson(), mwStudent.getMiddlewarePerson());
		}
	}

	/**
	 * @param student
	 * @param degreeCurricularPlan
	 * @param branch
	 * @throws Throwable
	 */
	private void createNewStudentCurricularPlan(
		IStudent student,
		IDegreeCurricularPlan degreeCurricularPlan,
		IBranch branch)
		throws Throwable
	{
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		super.persistentSuport.getIStudentCurricularPlanPersistente().simpleLockWrite(studentCurricularPlan);
		studentCurricularPlan.setBranch(branch);
		studentCurricularPlan.setSecundaryBranch(null);
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
		studentCurricularPlan.setEmployee(null);
		studentCurricularPlan.setObservations(null);
	}

	/**
	 * @param mwStudent
	 * @return IDegreeCurricularPlan
	 */
	private IDegreeCurricularPlan getDegreeCurricularPlan(MWStudent mwStudent) throws Throwable
	{
		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWDegreeTranslation persistentMWDegreeTranslation = mws.getIPersistentMWDegreeTranslation();

		MWDegreeTranslation mwDegreeTranslation = persistentMWDegreeTranslation.readByDegreeCode(mwStudent.getDegreecode());

		if (mwDegreeTranslation != null)
		{
			String degreeName = mwDegreeTranslation.getDegree().getNome();
			IExecutionPeriod executionPeriod = super.persistentSuport.getIPersistentExecutionPeriod().readActualExecutionPeriod();
			ICursoExecucao executionDegree =
				super.persistentSuport.getICursoExecucaoPersistente().readByDegreeNameAndExecutionYearAndDegreeType(
					degreeName,
					executionPeriod.getExecutionYear(),
					TipoCurso.LICENCIATURA_OBJ);
			return executionDegree.getCurricularPlan();
		} else
		{
			return null;
		}
	}

	/**
	 * @param mwStudent
	 * @param degreeCurricularPlan
	 * @return IBranch
	 * @throws Throwable
	 */
	private IBranch getBranch(MWStudent mwStudent, IDegreeCurricularPlan degreeCurricularPlan) throws Throwable
	{
		IBranch branch = null;

		IPersistentMiddlewareSupport mws = PersistentMiddlewareSupportOJB.getInstance();
		IPersistentMWBranch persistentBranch = mws.getIPersistentMWBranch();

		super.persistentSuport.clearCache();
		MWBranch mwbranch = persistentBranch.readByDegreeCodeAndBranchCode(mwStudent.getDegreecode(), mwStudent.getBranchcode());

		if (mwbranch == null)
		{
			System.out.println("[ERROR 08] mwBranch não existente!");
			System.out.println("[ERROR 08] Aluno " + mwStudent.getNumber());
			System.out.println("[ERROR 08] Curso " + mwStudent.getDegreecode());
			System.out.println("[ERROR 08] Ramo " + mwStudent.getBranchcode());
		} else
		{
			branch =
				super.persistentSuport.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(
					degreeCurricularPlan,
					mwbranch.getDescription());
		}

		if (branch == null)
		{
			branch = super.persistentSuport.getIPersistentBranch().readByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, "");
		}

		if (branch == null)
		{
			System.out.println("[ERROR 09] DCP " + degreeCurricularPlan.getName());
			System.out.println("[ERROR 09] Ramo Inexistente " + mwbranch);
		}

		return branch;
	}

	/**
	 * @param fenixPersonTemp
	 * @param oldPerson
	 * @throws Throwable
	 */
	public void updateStudentPerson(IPessoa fenixPersonTemp, MWPerson oldPerson) throws Throwable
	{
		IPessoa personTemp = new Pessoa();
		personTemp.setIdInternal(fenixPersonTemp.getIdInternal());

		IPessoa fenixPerson = (IPessoa) super.persistentSuport.getIPessoaPersistente().readByOId(personTemp, true);

		if (fenixPerson == null)
		{
			System.out.println("[ERROR 06] Person not Found !");
			return;
		}

		super.persistentSuport.getIPessoaPersistente().simpleLockWrite(fenixPerson);
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
		fenixPerson.setPais(this.getCountry(oldPerson.getCountrycode().toString()));
		fenixPerson.setSexo(PersonUtils.getSex(oldPerson.getSex()));
		fenixPerson.setTelefone(oldPerson.getPhone());
		fenixPerson.setTipoDocumentoIdentificacao(PersonUtils.getDocumentIdType(oldPerson.getDocumentidtype()));
	}

	/**
	 * @param countryCode
	 * @return ICountry
	 * @throws Throwable
	 */
	public ICountry getCountry(String countryCode) throws Throwable
	{
		Criteria criteria = new Criteria();

		if (countryCode.equals("01")
			|| countryCode.equals("02")
			|| countryCode.equals("03")
			|| countryCode.equals("04")
			|| countryCode.equals("05")
			|| countryCode.equals("06")
			|| countryCode.equals("1")
			|| countryCode.equals("2")
			|| countryCode.equals("3")
			|| countryCode.equals("4")
			|| countryCode.equals("5")
			|| countryCode.equals("6")
			|| countryCode.equals("0"))
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
		try
		{
			broker = ((SuportePersistenteOJB) super.persistentSuport).currentBroker();
		} catch (Exception e)
		{
			System.out.println("[ERROR 07] Failled obtainning broker.");
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

	/**
	 * @param studentList
	 * @return boolean 
	 */
	private boolean hasMasterDegree(List studentList) throws Throwable
	{
		Iterator iterator = studentList.iterator();
		while(iterator.hasNext())
		{
			IStudent student = (IStudent) iterator.next();
			
			List studentCurricularPlanList = super.persistentSuport.getIStudentCurricularPlanPersistente().readAllFromStudent(student.getNumber().intValue());
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

}