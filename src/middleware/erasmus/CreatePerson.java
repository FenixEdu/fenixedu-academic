/*
 * Created on Apr 7, 2004
 */
package middleware.erasmus;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.util.collections.ManageableVector;

import pt.utl.ist.berserk.storage.exceptions.StorageException;
import Dominio.DegreeCurricularPlan;
import Dominio.ICountry;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IPessoa;
import Dominio.IRole;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.IStudentKind;
import Dominio.Pessoa;
import Dominio.Student;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentRole;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentKind;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.AgreementType;
import Util.RandomStringGenerator;
import Util.RoleType;
import Util.Sexo;
import Util.StudentCurricularPlanState;
import Util.StudentState;
import Util.StudentType;
import Util.TipoCurso;
import Util.TipoDocumentoIdentificacao;
/**
 * @author João Mota
 */
public class CreatePerson {
	public static void main(String[] args) {
		try {
			SuportePersistenteOJB persistentSuport = SuportePersistenteOJB
					.getInstance();
			IPessoaPersistente persistentPerson = persistentSuport
					.getIPessoaPersistente();
			IPersistentStudent persistentStudent = persistentSuport
					.getIPersistentStudent();
			IStudentCurricularPlanPersistente persistentStudentCurricularPlan = persistentSuport
					.getIStudentCurricularPlanPersistente();
			IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
					.getIPersistentDegreeCurricularPlan();
			IPersistentCountry persistentCountry = persistentSuport
					.getIPersistentCountry();
			List studentsWithInvalidData = new ArrayList();
            List studentsThatAlreadyExist = new ArrayList();
			persistentSuport.iniciarTransaccao();
			PersistenceBroker broker = persistentSuport.currentBroker();
			List test = (List) broker.getCollectionByQuery(new QueryByCriteria(
					Dbo_aluno.class, null));
			persistentSuport.confirmarTransaccao();
			Iterator iter = test.iterator();
			while (iter.hasNext()) {
				Dbo_aluno erasmusStudent = (Dbo_aluno) iter.next();
				if (!hasValidData(erasmusStudent)) {
					studentsWithInvalidData.add(erasmusStudent);
				} else {
					IPessoa newPerson = new Pessoa();
					IStudent newStudent = new Student();
					IStudentCurricularPlan newStudentCurricularPlan = new StudentCurricularPlan();
					persistentSuport.iniciarTransaccao();
					broker = persistentSuport.currentBroker();
					Integer newNumber = generateNewStudentNumber(erasmusStudent);
					AgreementType agreementType = generateAgreementType(erasmusStudent);
					TipoDocumentoIdentificacao idDocumentType = new TipoDocumentoIdentificacao(
							TipoDocumentoIdentificacao.OUTRO);
					if (doesntExistPerson(erasmusStudent, persistentSuport)) {
						persistentPerson.simpleLockWrite(newPerson);
						setNewPerson(persistentCountry, broker, erasmusStudent,
								newPerson, newNumber, idDocumentType);
					} else {
						newPerson = getPerson(erasmusStudent, persistentSuport);
					}
					boolean existsStudent = doesntExistsStudent(erasmusStudent,
							newPerson, persistentSuport);
					if (existsStudent) {
						persistentStudent.simpleLockWrite(newStudent);
						setNewStudent(persistentSuport, persistentStudent,
								broker, erasmusStudent, newPerson, newStudent,
								newNumber, agreementType);
						if (((Student) newStudent).getStudentKindKey() == null) {
							((Student) newStudent).setStudentKindKey(newStudent
									.getStudentKind().getIdInternal());
						}
						persistentStudentCurricularPlan
								.simpleLockWrite(newStudentCurricularPlan);
						setNewStudentCurricularPlan(persistentSuport, broker,
								erasmusStudent, newStudent,
								newStudentCurricularPlan);
                        giveroles(newStudent,persistentSuport);
					} else {
                        studentsThatAlreadyExist.add(erasmusStudent);
					}
					persistentSuport.confirmarTransaccao();
				}
			}
			System.out.println("End of Migration");
            System.out.println("Errors:");
            System.out.println("Students with invalid data:");
            printStudentList(studentsWithInvalidData);
            System.out.println("Students already existing:");
            printStudentList(studentsThatAlreadyExist);
            System.out.println("The End");
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
		} catch (StorageException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param newStudent
	 * @param persistentSuport
	 */
	private static void giveroles(IStudent newStudent, SuportePersistenteOJB persistentSuport) throws ExcepcaoPersistencia {
		List roles = (List) newStudent.getPerson().getPersonRoles();
        IPersistentRole persistentRole = persistentSuport.getIPersistentRole();
        IRole studentRole =persistentRole.readByRoleType(RoleType.STUDENT);
        IRole personRole = persistentRole.readByRoleType(RoleType.PERSON);
       
        
        if (roles==null) {
            newStudent.getPerson().setPersonRoles(new ManageableVector()); 
            roles =(List) newStudent.getPerson().getPersonRoles();
        }
        if (!roles.contains(personRole)) {
            newStudent.getPerson().getPersonRoles().add(personRole);
        }
        if (!roles.contains(studentRole)) {
             newStudent.getPerson().getPersonRoles().add(studentRole);
        }
	}
	/**
	 * @param ignoredStudents
	 */
	private static void printStudentList(List students) {
		Iterator iter = students.iterator();
        while (iter.hasNext()) {
         Dbo_aluno student = (Dbo_aluno) iter.next();
         System.out.println(student.getNumero());
        }
		
	}
	/**
	 * @param erasmusStudent
	 * @param newPerson
	 * @param persistentSuport
	 * @return
	 */
	private static IStudent getStudent(Dbo_aluno erasmusStudent,
			IPessoa newPerson, SuportePersistenteOJB persistentSuport) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @param erasmusStudent
	 * @param newPerson
	 * @param persistentSuport
	 * @return
	 */
	private static boolean doesntExistsStudent(Dbo_aluno erasmusStudent,
			IPessoa newPerson, SuportePersistenteOJB persistentSuport)
			throws ExcepcaoPersistencia {
		IPersistentStudent persistentStudent = persistentSuport
				.getIPersistentStudent();
		IStudent result = persistentStudent.readByPersonAndDegreeType(
				newPerson, new TipoCurso(TipoCurso.LICENCIATURA));
		if (result == null) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * @param persistentSuport
	 * @param broker
	 * @param erasmusStudent
	 * @param newStudent
	 * @param newStudentCurricularPlan
	 * @throws ExcepcaoPersistencia
	 */
	private static void setNewStudentCurricularPlan(
			SuportePersistenteOJB persistentSuport, PersistenceBroker broker,
			Dbo_aluno erasmusStudent, IStudent newStudent,
			IStudentCurricularPlan newStudentCurricularPlan)
			throws ExcepcaoPersistencia {
		newStudentCurricularPlan.setStudent(newStudent);
		newStudentCurricularPlan
				.setCurrentState(new StudentCurricularPlanState(
						StudentCurricularPlanState.ACTIVE));
		newStudentCurricularPlan
				.setDegreeCurricularPlan(getDegreeCurricularPlan(
						erasmusStudent, broker, persistentSuport));
		newStudentCurricularPlan.setObservations(erasmusStudent.getObs());
		newStudentCurricularPlan
				.setStartDate(generateStartDate(erasmusStudent));
		newStudentCurricularPlan.setAckOptLock(new Integer(1));
	}
	/**
	 * @param persistentSuport
	 * @param persistentStudent
	 * @param broker
	 * @param erasmusStudent
	 * @param newPerson
	 * @param newStudent
	 * @param newNumber
	 * @param agreementType
	 * @throws ExcepcaoPersistencia
	 */
	private static void setNewStudent(SuportePersistenteOJB persistentSuport,
			IPersistentStudent persistentStudent, PersistenceBroker broker,
			Dbo_aluno erasmusStudent, IPessoa newPerson, IStudent newStudent,
			Integer newNumber, AgreementType agreementType)
			throws ExcepcaoPersistencia, StorageException {
		System.out.println(newNumber);
		newStudent.setNumber(newNumber);
		newStudent.setPerson(newPerson);
		newStudent.setAgreementType(agreementType);
		newStudent.setState(new StudentState(StudentState.INSCRITO));
		newStudent.setStudentKind(generateStudentKind(erasmusStudent, broker,
				persistentSuport));
		newStudent.setDegreeType(new TipoCurso(TipoCurso.LICENCIATURA));
		newStudent.setAckOptLock(new Integer(1));
	}
	/**
	 * @param persistentCountry
	 * @param broker
	 * @param erasmusStudent
	 * @param newPerson
	 * @param newNumber
	 * @param idDocumentType
	 * @throws ExcepcaoPersistencia
	 */
	private static void setNewPerson(IPersistentCountry persistentCountry,
			PersistenceBroker broker, Dbo_aluno erasmusStudent,
			IPessoa newPerson, Integer newNumber,
			TipoDocumentoIdentificacao idDocumentType)
			throws ExcepcaoPersistencia {
		newPerson.setUsername("L" + newNumber);
		// System.out.println("L" + newNumber);
		newPerson.setPassword(PasswordEncryptor
				.encryptPassword(RandomStringGenerator
						.getRandomStringGenerator(8)));
		newPerson.setNome(erasmusStudent.getNome() + " "
				+ erasmusStudent.getApelido());
		newPerson.setTipoDocumentoIdentificacao(idDocumentType);
		newPerson.setNumeroDocumentoIdentificacao(erasmusStudent.getBi());
		newPerson.setNascimento(generateCorrectBirthDate(erasmusStudent));
		newPerson.setSexo(generateCorrectSex(erasmusStudent));
		newPerson.setNacionalidade(generateNationality(erasmusStudent, broker));
		newPerson.setLocalidade(generateCorrectLocality(erasmusStudent));
		newPerson.setMorada(erasmusStudent.getMorad_local());
		newPerson.setCodigoPostal(generateCorrectZipCode(erasmusStudent));
		newPerson.setTelefone(erasmusStudent.getTel_local());
		newPerson.setPais(generateCountry(erasmusStudent, broker,
				persistentCountry));
		newPerson.setAckOptLock(new Integer(1));
	}
	/**
	 * @param erasmusStudent
	 * @param persistentSuport
	 * @return
	 */
	private static IPessoa getPerson(Dbo_aluno erasmusStudent,
			SuportePersistenteOJB persistentSuport) throws StorageException {
		IPessoaPersistente persistentPerson = persistentSuport
				.getIPessoaPersistente();
		IPessoa person = null;
		PersistenceBroker broker = persistentSuport.currentBroker();
		Criteria crit = new Criteria();
		crit.addEqualTo("numeroDocumentoIdentificacao", erasmusStudent.getBi());
		Query query = new QueryByCriteria(Pessoa.class, crit);
		List result = (List) broker.getCollectionByQuery(query);
		if (result == null || result.isEmpty()) {
			throw new RuntimeException("the query must return a valid object");
		} else if (result.size() > 1) {
			person = choosePersonFromResult(result, erasmusStudent);
		} else {
			person = (IPessoa) result.get(0);
		}
		return (IPessoa) persistentPerson.readByOId(person, false);
	}
	/**
	 * @param result
	 * @param erasmusStudent
	 * @return
	 */
	private static IPessoa choosePersonFromResult(List result,
			Dbo_aluno erasmusStudent) {
		Iterator iter = result.iterator();
		boolean step = true;
		IPessoa person = null;
		while (iter.hasNext() && step) {
			person = (IPessoa) iter.next();
			if (person.getNome().indexOf(erasmusStudent.getNome()) != -1) {
				step = false;
			}
		}
		if (step == true) {
			person = (IPessoa) result.get(0);
		}
		return person;
	}
	/**
	 * @param erasmusStudent
	 * @param persistentSuport
	 * @return
	 */
	private static boolean doesntExistPerson(Dbo_aluno erasmusStudent,
			SuportePersistenteOJB persistentSuport) throws StorageException {
		IPessoaPersistente persistentPerson = persistentSuport
				.getIPessoaPersistente();
		PersistenceBroker broker = persistentSuport.currentBroker();
		Criteria crit = new Criteria();
		crit.addEqualTo("numeroDocumentoIdentificacao", erasmusStudent.getBi());
		Query query = new QueryByCriteria(Pessoa.class, crit);
		List result = (List) broker.getCollectionByQuery(query);
		if (result == null || result.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * @param erasmusStudent
	 * @param broker
	 * @param persistentSuport
	 * @return
	 */
	private static IDegreeCurricularPlan getDegreeCurricularPlan(
			Dbo_aluno erasmusStudent, PersistenceBroker broker,
			SuportePersistenteOJB persistentSuport) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("codigocurso", erasmusStudent.getLicenciatura());
		Query query = new QueryByCriteria(Dbo_curso.class, criteria);
		Dbo_curso curso = (Dbo_curso) broker.getObjectByQuery(query);
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
				.getIPersistentDegreeCurricularPlan();
		ICursoPersistente persistentDegree = persistentSuport
				.getICursoPersistente();
		ICurso degree = persistentDegree.readByNameAndDegreeType(curso
				.getNome().replaceFirst("CURSO DE ", ""), new TipoCurso(
				TipoCurso.LICENCIATURA));
		IDegreeCurricularPlan degreeCurricularPlan;
		Criteria criteria2 = new Criteria();
		criteria2.addLike("name", "%2003%");
		criteria2.addEqualTo("degree.idInternal", degree.getIdInternal());
		Query query2 = new QueryByCriteria(DegreeCurricularPlan.class,
				criteria2);
		degreeCurricularPlan = (IDegreeCurricularPlan) broker
				.getObjectByQuery(query2);
		return degreeCurricularPlan;
	}
	/**
	 * @param erasmusStudent
	 * @return
	 */
	private static Date generateStartDate(Dbo_aluno erasmusStudent) {
		String dateString = erasmusStudent.getDatini();
		if (dateString.equals("1993/1994") || dateString.equals("1993-1994")) {
			dateString = "01-09-1993";
		}
		if (dateString.equals("1994/1995") || dateString.equals("1994-1995")) {
			dateString = "01-09-1994";
		}
		if (dateString.equals("1995/1996")) {
			dateString = "01-09-1994";
		}
		if (dateString.equals("1996/1997")) {
			dateString = "01-09-1996";
		}
		if (dateString.equals("1997/1998")) {
			dateString = "01-09-1997";
		}
		if (dateString.equals("1999/2000")) {
			dateString = "01-09-1999";
		}
		if (dateString.equals("2003-2004") || dateString.equals("2003/2004")) {
			dateString = "01-09-2003";
		}
		if (dateString.equals("Set 2001")) {
			dateString = "01-09-2001";
		}
		if (dateString.equals("Jan. 2001")) {
			dateString = "01-01-2001";
		}
		if (dateString.equals("Março 2002")) {
			dateString = "01-03-2002";
		}
		if (dateString.equals("2003 - SET") || dateString.equals("2003 SET.")
				|| dateString.equals("SET. 2003")
				|| dateString.equals("2003- SETº")
				|| dateString.equals("2003 SET")
				|| dateString.equals("2003 SETº")) {
			dateString = "01-09-2003";
		}
		if (dateString.equals("2003M OUT.") || dateString.equals("2003 OUT.")
				|| dateString.equals("2003 OUTº")) {
			dateString = "01-10-2003";
		}
		if (dateString.equals("2003 NOV")) {
			dateString = "01-11-2003";
		}
		if (dateString.equals("2004 JAN.")) {
			dateString = "01-01-2004";
		}
		if (dateString.equals("2004 FEV.") || dateString.equals("2004 FEV-")
				|| dateString.equals("2004 FEV")) {
			dateString = "01-02-2004";
		}
		if (dateString.equals("2004 MAR.")) {
			dateString = "01-03-2004";
		}
		if (dateString.equals("XX-XX-XXXX") || dateString.trim().equals("")
				|| dateString.equals("----------")
				|| dateString.equals("XXXXXXXXXX")
				|| dateString.equals("XXXXX")) {
			dateString = "13-04-2004";
		}
		dateString = dateString.replaceAll("\\.", "-");
		dateString = dateString.replaceAll("/", "-");
		dateString = dateString.replaceAll("O", "0");
		String[] dateArray = dateString.split("-");
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		if (dateArray[2].length() > 2
				|| new Integer(dateArray[2]).intValue() > 31) {
			calendar.set(generateCorrectYear(new Integer(dateArray[2].trim())
					.intValue()), new Integer(dateArray[1].trim()).intValue(),
					new Integer(dateArray[0].trim()).intValue());
		} else {
			calendar.set(generateCorrectYear(new Integer(dateArray[0].trim())
					.intValue()), new Integer(dateArray[1].trim()).intValue(),
					new Integer(dateArray[2].trim()).intValue());
		}
		return calendar.getTime();
	}
	/**
	 * @param i
	 * @return
	 */
	private static int generateCorrectYear(int i) {
		if (i > 1000) {
			return i;
		} else if (i < 10) {
			return 2000 + i;
		} else {
			return 1900 + i;
		}
	}
	/**
	 * @param erasmusStudent
	 * @param broker
	 * @param persistentSuport
	 * @return
	 */
	private static IStudentKind generateStudentKind(Dbo_aluno erasmusStudent,
			PersistenceBroker broker, SuportePersistenteOJB persistentSuport)
			throws ExcepcaoPersistencia {
		IPersistentStudentKind persistentStudentKind = persistentSuport
				.getIPersistentStudentKind();
		IStudentKind studentKind = persistentStudentKind
				.readByStudentType(generateStudentType(erasmusStudent));
		return studentKind;
	}
	/**
	 * @param erasmusStudent
	 * @return
	 */
	private static StudentType generateStudentType(Dbo_aluno erasmusStudent) {
		//        0 Interno
		//        1 Interno Eng. Técnico
		//        2 Externo Estrangeiro
		//        3 Externo Nacional
		//        4 Outros
		String tipoAluno = erasmusStudent.getTipo_aluno();
		if (tipoAluno.equals("0")) {
			return new StudentType(StudentType.NORMAL);
		}
		if (tipoAluno.equals("1")) {
			return new StudentType(StudentType.NORMAL);
		}
		if (tipoAluno.equals("2")) {
			return new StudentType(StudentType.FOREIGN_STUDENT);
		}
		if (tipoAluno.equals("3")) {
			return new StudentType(StudentType.EXTERNAL_STUDENT);
		}
		if (tipoAluno.equals("4")) {
			return new StudentType(StudentType.OTHER);
		}
		return new StudentType(StudentType.NORMAL);
	}
	/**
	 * @param erasmusStudent
	 * @param broker
	 * @return
	 */
	private static ICountry generateCountry(Dbo_aluno erasmusStudent,
			PersistenceBroker broker, IPersistentCountry persistentCountry)
			throws ExcepcaoPersistencia {
		ICountry foreignCountry = persistentCountry.readCountryByCode("OTHER");
		Criteria criteria = new Criteria();
		criteria.addEqualTo("codigonac", erasmusStudent.getNacionalidade());
		Query query = new QueryByCriteria(Dbo_nacionalidade.class, criteria);
		Dbo_nacionalidade nationality = (Dbo_nacionalidade) broker
				.getObjectByQuery(query);
		if (nationality != null) {
			ICountry country = persistentCountry.readCountryByName(nationality
					.getDescricao());
			if (country == null) {
				country = persistentCountry
						.readCountryByNationality(nationality.getDescricao());
			}
			if (country == null) {
				return foreignCountry;
			} else {
				return country;
			}
		} else {
			return foreignCountry;
		}
	}
	/**
	 * @param erasmusStudent
	 * @return
	 */
	private static String generateCorrectZipCode(Dbo_aluno erasmusStudent) {
		String zipCode = erasmusStudent.getCodpost_local();
		if (zipCode.equals("") || zipCode.equals("--------")
				|| zipCode.equals("........") || zipCode.equals("XXXXXX")
				|| zipCode.equals("XXXXXXXX") || zipCode.equals("XXXX-XXX")
				|| zipCode.equals("XXXXXXX")) {
			zipCode = "UNKNOWN";
		}
		return zipCode;
	}
	/**
	 * @param erasmusStudent
	 * @return
	 */
	private static String generateCorrectLocality(Dbo_aluno erasmusStudent) {
		String result = "UNKNOWN";
		if (!erasmusStudent.getLocalid_morad().trim().equals("")
				&& erasmusStudent.getLocalid_morad().indexOf("XX") == -1
				&& erasmusStudent.getLocalid_morad().indexOf("--") == -1) {
			result = erasmusStudent.getLocalid_morad();
		}
		return result;
	}
	/**
	 * @param erasmusStudent
	 * @return
	 */
	private static String generateNationality(Dbo_aluno erasmusStudent,
			PersistenceBroker broker) {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("codigonac", erasmusStudent.getNacionalidade());
		Query query = new QueryByCriteria(Dbo_nacionalidade.class, criteria);
		Dbo_nacionalidade nationality = (Dbo_nacionalidade) broker
				.getObjectByQuery(query);
		if (nationality != null) {
			return nationality.getDescricao();
		} else {
			return "UNKNOWN";
		}
	}
	/**
	 * @param erasmusStudent
	 * @return
	 */
	private static Sexo generateCorrectSex(Dbo_aluno erasmusStudent) {
		Sexo sex = null;
		if (erasmusStudent.getSexo().equals("M")) {
			sex = new Sexo(Sexo.MASCULINO);
		} else {
			sex = new Sexo(Sexo.FEMININO);
		}
		return sex;
	}
	/**
	 * @param erasmusStudent
	 * @return
	 */
	private static Date generateCorrectBirthDate(Dbo_aluno erasmusStudent) {
		String dateString = erasmusStudent.getDatnascimento();
		if (dateString.equals("07-051981")) {
			dateString = "07-05-1981";
		}
		if (dateString.equals("07-071978")) {
			dateString = "07-07-1978";
		}
		if (dateString.equals("1979-0609")) {
			dateString = "1979-06-09";
		}
		if (dateString.equals("XX-XX-XXXX") || dateString.trim().equals("")
				|| dateString.equals("----------")) {
			dateString = "13-04-2004";
		}
		dateString = dateString.replaceAll("\\.", "-");
		dateString = dateString.replaceAll("/", "-");
		dateString = dateString.replaceAll("O", "0");
		String[] dateArray = dateString.split("-");
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		if (dateArray[2].length() > 2
				|| new Integer(dateArray[2]).intValue() > 31) {
			calendar.set(generateCorrectYear(new Integer(dateArray[2].trim())
					.intValue()), new Integer(dateArray[1].trim()).intValue(),
					new Integer(dateArray[0].trim()).intValue());
		} else {
			calendar.set(generateCorrectYear(new Integer(dateArray[0].trim())
					.intValue()), new Integer(dateArray[1].trim()).intValue(),
					new Integer(dateArray[2].trim()).intValue());
		}
		return calendar.getTime();
	}
	/**
	 * @param erasmusStudent
	 * @return
	 */
	private static AgreementType generateAgreementType(Dbo_aluno erasmusStudent) {
		String acordo = erasmusStudent.getAcordo();
		if (acordo.equals("1")) {
			return new AgreementType(AgreementType.AIR_FORCE_ACADEMY);
		}
		if (acordo.equals("2")) {
			return new AgreementType(AgreementType.MILITARY_ACADEMY);
		}
		if (acordo.equals("3")) {
			return new AgreementType(AgreementType.NC);
		}
		if (acordo.equals("4")) {
			return new AgreementType(AgreementType.ERASMUS);
		}
		if (acordo.equals("5")) {
			return new AgreementType(AgreementType.SOCRATES);
		}
		if (acordo.equals("6")) {
			return new AgreementType(AgreementType.SOCRATES_ERASMUS);
		}
		if (acordo.equals("7")) {
			return new AgreementType(AgreementType.TEMPUS);
		}
		if (acordo.equals("8")) {
			return new AgreementType(AgreementType.BILATERAL_AGREEMENT);
		}
		if (acordo.equals("9")) {
			return new AgreementType(AgreementType.ALFA2);
		}
		if (acordo.equals("10")) {
			return new AgreementType(AgreementType.UNIFOR);
		}
		return new AgreementType(AgreementType.NONE);
	}
	/**
	 * @param erasmusStudent
	 * @return
	 */
	private static boolean hasValidData(Dbo_aluno erasmusStudent) {
		boolean result = true;
		String bi = erasmusStudent.getBi();
		if (bi == null || bi.trim().equals("")
				|| bi.trim().equals("XXXXXXXXXX")) {
			result = false;
		}
		return result;
	}
	/**
	 * @param erasmusStudent
	 * @return
	 */
	private static Integer generateNewStudentNumber(Dbo_aluno erasmusStudent) {
		if (erasmusStudent.getNumero().equals("785")) {
			return new Integer(101785);
		}
		Integer temp = null;
		String number = erasmusStudent.getNumero();
		try {
			temp = new Integer(number);
		} catch (NumberFormatException e) {
			number = number.replaceAll("A", "");
			temp = new Integer(number);
		}
		return new Integer(100000 + temp.intValue());
	}
}