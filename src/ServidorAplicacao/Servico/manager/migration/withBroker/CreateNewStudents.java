package ServidorAplicacao.Servico.manager.migration.withBroker;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWBranch;
import middleware.middlewareDomain.MWPerson;
import middleware.middlewareDomain.MWStudent;
import middleware.persistentMiddlewareSupport.PersistanceBrokerForMigrationProcess;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.query.Criteria;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Country;
import Dominio.IBranch;
import Dominio.ICountry;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentKind;
import Dominio.PersonRole;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.security.PasswordEncryptor;
import Util.EstadoCivil;
import Util.RoleType;
import Util.Sexo;
import Util.StudentCurricularPlanState;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;

/**
 * @author David Santos in Feb 13, 2004
 */

public class CreateNewStudents extends CreateUpdateDeleteEnrollmentsInCurrentStudentCurricularPlans implements IService
{
	private int createdPersons;
	private int createdStudents;
	private int createdStudentCurricularPlans;

	public CreateNewStudents(PersistanceBrokerForMigrationProcess pb)
	{
		if (pb == null)
		{
			super.persistentSuport = new PersistanceBrokerForMigrationProcess();
		} else
		{
			super.persistentSuport = pb;
		}

		this.createdPersons = 0;
		this.createdStudents = 0;
		this.createdStudentCurricularPlans = 0;
		
		super.executionPeriod = null;
		super.out = null;
		super.numberOfElementsInSpan = 50;
		super.maximumNumberOfElementsToConsider = 10000;
	}

	public void run(Boolean toLogToFile, String fileName) throws Throwable
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

			Integer numberOfStudents = super.persistentSuport.countAllMWStudents();
			super.out.println("[INFO] Updating a total of [" + numberOfStudents.intValue() + "] student curriculums.");

			int numberOfSpans = numberOfStudents.intValue() / super.numberOfElementsInSpan;
			numberOfSpans = numberOfStudents.intValue() % super.numberOfElementsInSpan > 0 ? numberOfSpans + 1 : numberOfSpans;
			int totalElementsConsidered = 0;

			for (int span = 0; span < numberOfSpans && totalElementsConsidered < super.maximumNumberOfElementsToConsider; span++)
			{
				super.out.println("[INFO] Reading MWStudents...");

				Iterator iterator =
					super.persistentSuport.readAllMWStudentsBySpanIterator(
						new Integer(span),
						new Integer(super.numberOfElementsInSpan));

				while (iterator.hasNext())
				{
					mwStudent = (MWStudent) iterator.next();

					this.createStudent(mwStudent);

					totalElementsConsidered++;
				}
			}
		} catch (Throwable e)
		{
			if (mwStudent != null)
			{
				super.out.println("[ERROR 036] Exception migrating student [" + mwStudent.getNumber() + "]!");
			}
			e.printStackTrace(super.out);
			throw new Throwable(e);
		}

		super.out.println("[INFO] DONE!");
		super.out.println("[INFO] Created Persons: [" + this.createdPersons + "]");
		super.out.println("[INFO] Created Students: [" + this.createdStudents + "]");
		super.out.println("[INFO] Created StudentCurricularPlans: [" + this.createdStudentCurricularPlans + "]");
		
		super.out.close();
	}

	/**
	 * @param mwStudent
	 */
	private void createStudent(MWStudent mwStudent)
	{
		super.executionPeriod = super.persistentSuport.readCurrentExecutionPeriod();

		IPessoa person =
			super.persistentSuport.readPersonByDocumentIDNumberAndDocumentIDType(
				mwStudent.getDocumentidnumber(),
				this.getDocumentIdType(mwStudent.getMiddlewarePerson().getDocumentidtype()));

		if (person == null)
		{
			person = this.createPersonFromMWStudent(mwStudent);
		}

		IStudent student = this.createStudent(mwStudent, person);

		this.createStudentCurricularPlan(student, mwStudent);
	}

	/**
	 * @param student
	 * @param mwStudent
	 */
	private void createStudentCurricularPlan(IStudent student, MWStudent mwStudent)
	{
		IDegreeCurricularPlan degreeCurricularPlan = this.getDegreeCurricularPlan(mwStudent);

		IStudentCurricularPlan studentCurricularPlan =
			super.persistentSuport.readStudentCurricularPlanByStudentAndDegreeCurricularPlanAndState(
				student,
				degreeCurricularPlan,
				StudentCurricularPlanState.ACTIVE_OBJ);

		if (studentCurricularPlan == null)
		{
			studentCurricularPlan = new StudentCurricularPlan();
			studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);
			studentCurricularPlan.setBranch(this.getBranch(mwStudent, studentCurricularPlan.getDegreeCurricularPlan()));

			if (studentCurricularPlan.getBranch() == null)
			{
				System.out.println(
					"[ERROR 037] Branch [Degree:"
						+ mwStudent.getDegreecode()
						+ " Branch:"
						+ mwStudent.getBranchcode()
						+ "] not found !");

				return;
			}

			studentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE_OBJ);
			studentCurricularPlan.setGivenCredits(null);
			studentCurricularPlan.setSpecialization(null);
			studentCurricularPlan.setStartDate(Calendar.getInstance().getTime());
			studentCurricularPlan.setStudent(student);
			super.persistentSuport.store(studentCurricularPlan);
			this.createdStudentCurricularPlans++;
		}
	}

	/**
	 * @param mwStudent
	 * @return IDegreeCurricularPlan
	 */
	private IDegreeCurricularPlan getDegreeCurricularPlan(MWStudent mwStudent)
	{
		MWBranch mwBranch = super.persistentSuport.readMWBranchByDegreeCodeAndBranchCode(mwStudent.getDegreecode(), new Integer(0));

		if (mwBranch == null)
		{
			System.out.println("[ERROR 038] Reading Branch " + mwStudent.getBranchcode() + " for degree " + mwStudent.getDegreecode());
			return null;
		}

		String degreeName = StringUtils.substringAfter(mwBranch.getDescription(), "DE ");

		if (degreeName.indexOf("TAGUS") != -1)
		{
			degreeName = "Engenharia Informática e de Computadores - Taguspark";
		}

		ICursoExecucao executionDegree =
			super.persistentSuport.readExecutionDegreeByDegreeNameAndExecutionYearAndDegreeType(
				degreeName,
				executionPeriod.getExecutionYear(),
				TipoCurso.LICENCIATURA_OBJ);

		return executionDegree.getCurricularPlan();
	}

	/**
	 * @param mwStudent
	 * @return IBranch
	 */
	private IBranch getBranch(MWStudent mwStudent, IDegreeCurricularPlan degreeCurricularPlan)
	{
		IBranch branch = null;

		MWBranch mwbranch =
			super.persistentSuport.readMWBranchByDegreeCodeAndBranchCode(mwStudent.getDegreecode(), mwStudent.getBranchcode());

		branch =
			super.persistentSuport.readBranchByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, mwbranch.getDescription());

		if (branch == null)
		{
			branch = super.persistentSuport.readBranchByDegreeCurricularPlanAndBranchName(degreeCurricularPlan, "");
		}

		if (branch == null)
		{
			System.out.println("[ERROR 039] Ramo Antigo " + mwbranch);
		}

		return branch;
	}

	/**
	 * @param mwStudent
	 * @param person
	 * @return IStudent
	 */
	private IStudent createStudent(MWStudent mwStudent, IPessoa person)
	{
		IStudent student = super.persistentSuport.readStudentByPersonAndDegreeType(person, TipoCurso.LICENCIATURA_OBJ);
		if (student == null)
		{
			IStudentKind studentKind = super.persistentSuport.readStudentKindByStudentType(new StudentType(StudentType.NORMAL));
			
			student = new Student();
			student.setNumber(mwStudent.getNumber());
			student.setPerson(person);
			student.setState(new StudentState(StudentState.INSCRITO));
			student.setDegreeType(TipoCurso.LICENCIATURA_OBJ);
			student.setStudentKind(studentKind);
			super.persistentSuport.store(student);
			this.createdStudents++;
		}
		return student;
	}

	/**
	 * @param mwStudent
	 * @return IPessoa
	 */
	private IPessoa createPersonFromMWStudent(MWStudent mwStudent)
	{
		MWPerson mwPerson = mwStudent.getMiddlewarePerson();

		IPessoa person = new Pessoa();

		person.setCodigoFiscal(mwPerson.getFinancialrepcode());
		person.setCodigoPostal(mwPerson.getZipcode());
		person.setConcelhoMorada(mwPerson.getMunicipalityofaddress());
		person.setConcelhoNaturalidade(mwPerson.getMunicipalityofbirth());
		person.setDataEmissaoDocumentoIdentificacao(mwPerson.getDocumentiddate());
		person.setDataValidadeDocumentoIdentificacao(mwPerson.getDocumentidvalidation());
		person.setDistritoMorada(mwPerson.getDistrictofaddress());
		person.setDistritoNaturalidade(mwPerson.getDistrictofbirth());
		person.setEstadoCivil(this.getMaritalStatus(mwPerson.getMaritalstatus()));
		person.setFreguesiaMorada(mwPerson.getParishofaddress());
		person.setFreguesiaNaturalidade(mwPerson.getParishofbirth());
		person.setLocalEmissaoDocumentoIdentificacao(mwPerson.getDocumentidplace());
		person.setLocalidadeCodigoPostal(mwPerson.getAddressAreaCode());
		person.setMorada(mwPerson.getAddress());
		person.setNascimento(mwPerson.getDateofbirth());
		person.setNome(mwPerson.getName());
		person.setNomeMae(mwPerson.getMothername());
		person.setNomePai(mwPerson.getFathername());
		person.setNumContribuinte(mwPerson.getFiscalcode());
		person.setNumeroDocumentoIdentificacao(mwPerson.getDocumentidnumber());
		person.setPais(this.getCountry(mwPerson.getCountrycode()));
		person.setSexo(this.getSex(mwPerson.getSex()));
		person.setTelefone(mwPerson.getPhone());
		person.setTipoDocumentoIdentificacao(this.getDocumentIdType(mwPerson.getDocumentidtype()));
		person.setTelemovel(mwPerson.getMobilephone());
		person.setEmail(mwPerson.getEmail());
		person.setEnderecoWeb(mwPerson.getHomepage());
		person.setUsername("L" + mwStudent.getNumber());
		person.setPassword(PasswordEncryptor.encryptPassword(mwStudent.getDocumentidnumber()));
		super.persistentSuport.store(person);

		IRole role = super.persistentSuport.readRoleByType(RoleType.PERSON);
		IPersonRole personRole = new PersonRole();
		personRole.setPerson(person);
		personRole.setRole(role);
		super.persistentSuport.store(personRole);
		
		role = super.persistentSuport.readRoleByType(RoleType.STUDENT);
		personRole = new PersonRole();
		personRole.setPerson(person);
		personRole.setRole(role);
		super.persistentSuport.store(personRole);
		
		this.createdPersons++;

		return person;
	}

	/**
	 * @param documentIDType
	 * @return TipoDocumentoIdentificacao
	 */
	private TipoDocumentoIdentificacao getDocumentIdType(String documentIDType)
	{
		return new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);
	}

	/**
	 * @param Sex
	 * @return Sexo
	 */
	private Sexo getSex(String sex)
	{
		if (sex.equalsIgnoreCase("M"))
		{
			return new Sexo(Sexo.MASCULINO);
		} else
		{
			return new Sexo(Sexo.FEMININO);
		}
	}

	/**
	 * @param countryCode
	 * @return ICountry
	 */
	private ICountry getCountry(Integer countryCode)
	{
		Criteria criteria = this.buildCountryQueryCriteria(countryCode.toString());

		List result = super.persistentSuport.readByCriteria(Country.class, criteria);

		if (result.size() == 0)
		{	
			return null;
		} else
		{
			return (ICountry) result.get(0);
		}
	}

	/**
	 * @param countryCode
	 * @return Criteria
	 */
	private Criteria buildCountryQueryCriteria(String countryCode)
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
			System.out.println("[ERROR 040] COUNTRY > NULL");
			return null;
		}
		return criteria;
	}

	/**
	 * @param Marital Status
	 * @return EstadoCivil
	 */
	private EstadoCivil getMaritalStatus(String string)
	{
		return new EstadoCivil(EstadoCivil.SOLTEIRO);
	}

}