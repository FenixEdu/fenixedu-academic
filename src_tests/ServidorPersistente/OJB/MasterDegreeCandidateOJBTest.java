/*
 * MasterDegreeCandidateOJBTest.java
 * NetBeans JUnit based test
 *
 * Created on 17 de Outubro de 2002, 11:37
 * 
 * Testes :
 *  - 1 : Read existing MasterDegreeCandidate by username
 *  - 2 : Read non-existing MasterDegreeCandidate by username
 *  - 3 : Write existing MasterDegreeCandidate 
 *  - 4 : Write non-existing MasterDegreeCandidate 
 *  - 5 : Delete existing MasterDegreeCandidate 
 *  - 6 : Delete non-existing MasterDegreeCandidate 
 *  - 7 : Delete all MasterDegreeCandidates 
 *  - 8 : Read existing MasterDegreeCandidate by candidateNumber, applicationYear and masterDegreeCode
 *  - 9 : Read non-existing MasterDegreeCandidate by candidateNumber, applicationYear and masterDegreeCode
 */

/**
 *
 * Autores :
 *   - Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *   - Joana Mota (jccm@rnl.ist.utl.pt)
 *
 */


package ServidorPersistente.OJB;

import java.util.Calendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import Dominio.ICountry;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IExecutionYear;
import Dominio.IMasterDegreeCandidate;
import Dominio.IPessoa;
import Dominio.MasterDegreeCandidate;
import Dominio.Pessoa;
import ServidorAplicacao.security.PasswordEncryptor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentCountry;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.IPersistentMasterDegreeCandidate;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.EstadoCivil;
import Util.Sexo;
import Util.Specialization;
import Util.TipoDocumentoIdentificacao;

public class MasterDegreeCandidateOJBTest extends TestCaseOJB {

	SuportePersistenteOJB persistentSupport = null;
	IPersistentMasterDegreeCandidate persistentMasterDegreeCandidate = null;
	IPersistentCountry persistentCountry = null;
	ICursoPersistente persistentDegree = null;
	IPersistentExecutionYear persistentExecutionYear = null;
	ICursoExecucaoPersistente persistentExecutionDegree = null;
	IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
	IPessoaPersistente persistentPerson = null;

	public MasterDegreeCandidateOJBTest(java.lang.String testName) {
		super(testName);
	}

	public static void main(java.lang.String[] args) {
		System.out.println(
			"Beginning of test from class MasterDegreeCandidateOJB" + "\n");
		junit.textui.TestRunner.run(suite());
		System.out.println(
			"End of test from class MasterDegreeCandidateOJB" + "\n");
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(MasterDegreeCandidateOJBTest.class);

		return suite;
	}

	protected void setUp() {
		super.setUp();
		try {
			persistentSupport = SuportePersistenteOJB.getInstance();
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			fail("Error");
		}
		persistentMasterDegreeCandidate =
			persistentSupport.getIPersistentMasterDegreeCandidate();
		persistentDegree = persistentSupport.getICursoPersistente();
		persistentCountry = persistentSupport.getIPersistentCountry();
		persistentExecutionYear =
			persistentSupport.getIPersistentExecutionYear();
		persistentExecutionDegree =
			persistentSupport.getICursoExecucaoPersistente();
		persistentDegreeCurricularPlan =
			persistentSupport.getIPersistentDegreeCurricularPlan();
		persistentPerson = persistentSupport.getIPessoaPersistente();
	}

	protected void tearDown() {
		super.tearDown();
	}

	public void testReadExistingMasterDegreeCandidate() {
		System.out.println("- Test 1 : Read Existing Master Degree Candidate");
		IMasterDegreeCandidate masterDegreeCandidateTemp = null;

		try {
			persistentSupport.iniciarTransaccao();
			List result =
				persistentMasterDegreeCandidate
					.readMasterDegreeCandidatesByUsername(
					"nmsn");
			masterDegreeCandidateTemp = (IMasterDegreeCandidate) result.get(0);
			assertNotNull(masterDegreeCandidateTemp);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
		// Testing the obtained candidate

		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getNumeroDocumentoIdentificacao()
				.equals(
				"4444444"));
		assertEquals(
			masterDegreeCandidateTemp
				.getPerson()
				.getTipoDocumentoIdentificacao(),
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getLocalEmissaoDocumentoIdentificacao()
				.equals("Lisboa"));

		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getDataEmissaoDocumentoIdentificacao()
				.toString()
				.equals("2002-10-12"));
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getDataValidadeDocumentoIdentificacao()
				.toString()
				.equals("2005-11-01"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getNome().equals("Nuno"));
		assertEquals(
			masterDegreeCandidateTemp.getPerson().getSexo(),
			new Sexo(Sexo.MASCULINO));
		assertEquals(
			masterDegreeCandidateTemp.getPerson().getEstadoCivil(),
			new EstadoCivil(EstadoCivil.SOLTEIRO));

		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getNascimento()
				.toString()
				.equals(
				"1979-05-12"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getNomePai().equals(
				"Manuel"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getNomeMae().equals("Maria"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getNacionalidade().equals(
				"Portuguesa"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getLocalidade().equals(
				"localidade Nuno"));
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getFreguesiaNaturalidade()
				.equals(
				"Freguesia Nuno"));
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getConcelhoNaturalidade()
				.equals(
				"Concelho Nuno"));
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getDistritoNaturalidade()
				.equals(
				"Distrito Nuno"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getMorada().equals(
				"Morada Nuno"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getCodigoPostal().equals(
				"1700-200"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getFreguesiaMorada().equals(
				"frequesia morada Nuno"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getConcelhoMorada().equals(
				"concelho morada Nuno"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getDistritoMorada().equals(
				"distrito morada Nuno"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getTelefone().equals(
				"214443523"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getTelemovel().equals(
				"96546321"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getEmail().equals("s12@h.c"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getEnderecoWeb().equals(
				"http123"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getCodigoFiscal().equals(
				"444444444"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getProfissao().equals(
				"Profissao"));
		assertTrue(
			masterDegreeCandidateTemp.getMajorDegree().equals("Informatica"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getUsername().equals("nmsn"));

		assertTrue(
			masterDegreeCandidateTemp.getPerson().getPassword().equals(
				PasswordEncryptor.encryptPassword("pass")));
		assertTrue(
			masterDegreeCandidateTemp.getCandidateNumber().equals(
				new Integer(1)));
		assertEquals(
			masterDegreeCandidateTemp.getSpecialization(),
			new Specialization(Specialization.MESTRADO));
		assertTrue(
			masterDegreeCandidateTemp.getMajorDegreeSchool().equals("IST"));
		assertTrue(
			masterDegreeCandidateTemp.getMajorDegreeYear().equals(
				new Integer(2000)));
		assertTrue(
			masterDegreeCandidateTemp.getAverage().equals(new Double(14.99)));

		assertEquals(masterDegreeCandidateTemp.getSituations().size(), 2);

	}

	public void testReadNonExistingMasterDegreeCandidate() {
		System.out.println(
			"- Test 2 : Read non-existing Master Degree Candidate");

		try {
			persistentSupport.iniciarTransaccao();
			List result =
				persistentMasterDegreeCandidate
					.readMasterDegreeCandidatesByUsername(
					"desc");
			assertTrue(result.isEmpty());
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
	}

	public void testWriteExistingMasterDegreeCandidate() {
		System.out.println("- Test 3 : Write Existing Master Degree Candidate");

		List candidates = null;
		try {
			persistentSupport.iniciarTransaccao();

			IMasterDegreeCandidate candidateTemp = new MasterDegreeCandidate();
			candidates =
				persistentExecutionDegree.readByCriteria(candidateTemp);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

		IMasterDegreeCandidate candidate =
			(IMasterDegreeCandidate) candidates.get(0);

		IMasterDegreeCandidate candidateTemp =
			new MasterDegreeCandidate(
				candidate.getPerson(),
				candidate.getExecutionDegree(),
				candidate.getCandidateNumber(),
				candidate.getSpecialization(),
				candidate.getMajorDegree(),
				candidate.getMajorDegreeSchool(),
				candidate.getMajorDegreeYear(),
				candidate.getAverage());

		try {
			persistentSupport.iniciarTransaccao();
			persistentMasterDegreeCandidate.writeMasterDegreeCandidate(
				candidateTemp);
			persistentSupport.confirmarTransaccao();
			fail("testWriteExistingMasterDegreeCandidate");

		} catch (ExistingPersistentException eex) {
			// all is ok
			try {
				persistentSupport.confirmarTransaccao();
			} catch (ExcepcaoPersistencia e) {
				fail("Caught ExcepcaoPersistencia" + e);
			}
		} catch (ExcepcaoPersistencia ex) {
			fail("Caught ExcepcaoPersistencia" + ex);
		}
	}

	public void testWriteNonExistingMasterDegreeCandidate() {
		System.out.println(
			"- Test 4 : Write NonExisting Master Degree Candidate");

		Calendar data = Calendar.getInstance();
		data.set(2002, Calendar.NOVEMBER, 17);

		ICurso masterDegreeTemp = null;
		ICountry countryTemp = null;
		IExecutionYear executionYear = null;
		ICursoExecucao executionDegree = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		IPessoa person = new Pessoa();
		person.setNumeroDocumentoIdentificacao("9786541230");
		person.setCodigoFiscal("0312645978");
		person.setTipoDocumentoIdentificacao(
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		person.setUsername("ars");
		person.setPassword(PasswordEncryptor.encryptPassword("pass2"));

		try {
			persistentSupport.iniciarTransaccao();
			masterDegreeTemp = persistentDegree.readBySigla("LEIC");
			assertNotNull(masterDegreeTemp);
			countryTemp = persistentCountry.readCountryByName("Inglaterra");
			assertNotNull(countryTemp);
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);

			degreeCurricularPlan =
				persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano1",
					masterDegreeTemp);
			assertNotNull(degreeCurricularPlan);

			executionDegree =
				persistentExecutionDegree
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
		assertNotNull(masterDegreeTemp);
		assertNotNull(countryTemp);

		IMasterDegreeCandidate masterDegreeCandidateTemp =
			new MasterDegreeCandidate(
				person,
				executionDegree,
				new Integer(10),
				new Specialization(Specialization.MESTRADO),
				"LEIC",
				"IST",
				new Integer(2002),
				new Double(10.0));

		try {
			persistentSupport.iniciarTransaccao();
			persistentMasterDegreeCandidate.writeMasterDegreeCandidate(
				masterDegreeCandidateTemp);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

		masterDegreeCandidateTemp = null;

		try {
			persistentSupport.iniciarTransaccao();
			List result =
				persistentMasterDegreeCandidate
					.readMasterDegreeCandidatesByUsername(
					"ars");
			masterDegreeCandidateTemp = (IMasterDegreeCandidate) result.get(0);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
		// Testing the obtained Candidate
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getNumeroDocumentoIdentificacao()
				.equals(
				"9786541230"));
		assertEquals(
			masterDegreeCandidateTemp
				.getPerson()
				.getTipoDocumentoIdentificacao(),
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getCodigoFiscal().equals(
				"0312645978"));
		assertTrue(masterDegreeCandidateTemp.getMajorDegree().equals("LEIC"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getUsername().equals("ars"));

		// Ver como se comparam as passwords encriptadas
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getPassword().equals(
				PasswordEncryptor.encryptPassword("pass2")));
		assertTrue(
			masterDegreeCandidateTemp.getCandidateNumber().equals(
				new Integer(10)));
		assertEquals(
			masterDegreeCandidateTemp.getSpecialization(),
			new Specialization(Specialization.MESTRADO));
		assertTrue(
			masterDegreeCandidateTemp.getMajorDegreeSchool().equals("IST"));
		assertTrue(
			masterDegreeCandidateTemp.getMajorDegreeYear().equals(
				new Integer(2002)));
		assertTrue(
			masterDegreeCandidateTemp.getAverage().equals(new Double(10.00)));
	}

	public void testDeleteExistingMasterDegreeCandidate() {
		System.out.println(
			"- Test 5 : Delete Existing Master Degree Candidate");
		IMasterDegreeCandidate masterDegreeCandidateTemp = null;
		try {
			persistentSupport.iniciarTransaccao();
			List result =
				persistentMasterDegreeCandidate
					.readMasterDegreeCandidatesByUsername(
					"nmsn");
			masterDegreeCandidateTemp = (IMasterDegreeCandidate) result.get(0);
			persistentMasterDegreeCandidate.delete(masterDegreeCandidateTemp);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

		masterDegreeCandidateTemp = null;

		// test if it was really deleted
		try {
			persistentSupport.iniciarTransaccao();
			List result =
				persistentMasterDegreeCandidate
					.readMasterDegreeCandidatesByUsername(
					"nmsn");
			assertTrue(result.isEmpty());
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
	}

	public void testDeleteNonExistingMasterDegreeCandidate() {
		System.out.println(
			"- Test 6 : Delete NonExisting Master Degree Candidate");

		IMasterDegreeCandidate masterDegreeCandidateTemp = null;
		try {
			persistentSupport.iniciarTransaccao();
			List result =
				persistentMasterDegreeCandidate
					.readMasterDegreeCandidatesByUsername(
					"nmsn");
			masterDegreeCandidateTemp = (IMasterDegreeCandidate) result.get(0);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

		// Clean all
		try {
			persistentSupport.iniciarTransaccao();
			persistentMasterDegreeCandidate.deleteAll();
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

		try {
			persistentSupport.iniciarTransaccao();
			persistentMasterDegreeCandidate.delete(masterDegreeCandidateTemp);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
	}

	public void testDeleteAllMasterDegreeCandidates() {
		System.out.println("- Test 7 : Delete All Master Degree Candidates");
		try {
			persistentSupport.iniciarTransaccao();
			persistentMasterDegreeCandidate.deleteAll();
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

		// Test if it was really deleted
		try {
			persistentSupport.iniciarTransaccao();
			List result =
				persistentMasterDegreeCandidate
					.readMasterDegreeCandidatesByUsername(
					"nmsn");

			assertTrue(result.isEmpty());
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
	}

	public void testReadMasterDegreeCandidateByNumberAndApplicationYearAndDegreeCode() {
		System.out.println(
			"- Test 8 : Read existing Master Degree Candidate by candidateNumber, applicationYear and masterDegreeCode");
		IMasterDegreeCandidate masterDegreeCandidateTemp = null;

		try {
			persistentSupport.iniciarTransaccao();
			masterDegreeCandidateTemp =
				persistentMasterDegreeCandidate
					.readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(
					new Integer(1),
					"2002/2003",
					"MEEC",
					new Specialization(Specialization.MESTRADO));
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

		assertNotNull(masterDegreeCandidateTemp);

		// Testing the obtained Candidate 
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getNumeroDocumentoIdentificacao()
				.equals(
				"4444444"));
		assertEquals(
			masterDegreeCandidateTemp
				.getPerson()
				.getTipoDocumentoIdentificacao(),
			new TipoDocumentoIdentificacao(
				TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getLocalEmissaoDocumentoIdentificacao()
				.equals("Lisboa"));

		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getDataEmissaoDocumentoIdentificacao()
				.toString()
				.equals("2002-10-12"));
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getDataValidadeDocumentoIdentificacao()
				.toString()
				.equals("2005-11-01"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getNome().equals("Nuno"));
		assertEquals(
			masterDegreeCandidateTemp.getPerson().getSexo(),
			new Sexo(Sexo.MASCULINO));
		assertEquals(
			masterDegreeCandidateTemp.getPerson().getEstadoCivil(),
			new EstadoCivil(EstadoCivil.SOLTEIRO));

		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getNascimento()
				.toString()
				.equals(
				"1979-05-12"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getNomePai().equals(
				"Manuel"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getNomeMae().equals("Maria"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getNacionalidade().equals(
				"Portuguesa"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getLocalidade().equals(
				"localidade Nuno"));
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getFreguesiaNaturalidade()
				.equals(
				"Freguesia Nuno"));
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getConcelhoNaturalidade()
				.equals(
				"Concelho Nuno"));
		assertTrue(
			masterDegreeCandidateTemp
				.getPerson()
				.getDistritoNaturalidade()
				.equals(
				"Distrito Nuno"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getMorada().equals(
				"Morada Nuno"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getCodigoPostal().equals(
				"1700-200"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getFreguesiaMorada().equals(
				"frequesia morada Nuno"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getConcelhoMorada().equals(
				"concelho morada Nuno"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getDistritoMorada().equals(
				"distrito morada Nuno"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getTelefone().equals(
				"214443523"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getTelemovel().equals(
				"96546321"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getEmail().equals("s12@h.c"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getEnderecoWeb().equals(
				"http123"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getCodigoFiscal().equals(
				"444444444"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getProfissao().equals(
				"Profissao"));
		assertTrue(
			masterDegreeCandidateTemp.getMajorDegree().equals("Informatica"));
		assertTrue(
			masterDegreeCandidateTemp.getPerson().getUsername().equals("nmsn"));

		assertTrue(
			masterDegreeCandidateTemp.getPerson().getPassword().equals(
				PasswordEncryptor.encryptPassword("pass")));
		assertTrue(
			masterDegreeCandidateTemp.getCandidateNumber().equals(
				new Integer(1)));
		assertEquals(
			masterDegreeCandidateTemp.getSpecialization(),
			new Specialization(Specialization.MESTRADO));
		assertTrue(
			masterDegreeCandidateTemp.getMajorDegreeSchool().equals("IST"));
		assertTrue(
			masterDegreeCandidateTemp.getMajorDegreeYear().equals(
				new Integer(2000)));
		assertTrue(
			masterDegreeCandidateTemp.getAverage().equals(new Double(14.99)));

		assertEquals(masterDegreeCandidateTemp.getSituations().size(), 2);
	}

	public void testReadNonExistingMasterDegreeCandidate2() {
		System.out.println(
			"- Test 9 : Read non existing MasterDegreeCandidate by candidateNumber, applicationYear and masterDegreeCode");
		IMasterDegreeCandidate masterDegreeCandidateTemp = null;

		try {
			persistentSupport.iniciarTransaccao();
			masterDegreeCandidateTemp =
				persistentMasterDegreeCandidate
					.readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(
					new Integer(999),
					"2000",
					"555",
					new Specialization(Specialization.ESPECIALIZACAO));
			assertNull(masterDegreeCandidateTemp);
			persistentSupport.confirmarTransaccao();

		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
	}

	public void testReadExistingByIdentificationDocNumberAndTypeAndExecutionDegree() {
		System.out.println(
			"- Test 10 : Read existing Master Degree Candidate by Identification Document Number And Type and Execution Degree");
		IMasterDegreeCandidate masterDegreeCandidateTemp1 = null;
		IMasterDegreeCandidate masterDegreeCandidateTemp2 = null;

		try {
			persistentSupport.iniciarTransaccao();
			masterDegreeCandidateTemp1 =
				persistentMasterDegreeCandidate
					.readCandidateByNumberAndApplicationYearAndDegreeCodeAndSpecialization(
					new Integer(1),
					"2002/2003",
					"MEEC",
					new Specialization(Specialization.MESTRADO));
			assertNotNull(masterDegreeCandidateTemp1);

			masterDegreeCandidateTemp2 =
				persistentMasterDegreeCandidate
					.readByIdentificationDocNumberAndTypeAndExecutionDegreeAndSpecialization(
					masterDegreeCandidateTemp1
						.getPerson()
						.getNumeroDocumentoIdentificacao(),
					masterDegreeCandidateTemp1
						.getPerson()
						.getTipoDocumentoIdentificacao()
						.getTipo(),
					masterDegreeCandidateTemp1.getExecutionDegree(),
					masterDegreeCandidateTemp1.getSpecialization());
			assertNotNull(masterDegreeCandidateTemp2);

			assertEquals(
				masterDegreeCandidateTemp1,
				masterDegreeCandidateTemp2);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
	}

	public void testReadByUsernameAndExecutionDegreeAndSpecialization() {
		System.out.println(
			"- Test 11 : Read existing Master Degree Candidate by Username and Execution Degree and Specialization");
		ICurso masterDegreeTemp = null;
		IExecutionYear executionYear = null;
		ICursoExecucao executionDegree = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;

		try {
			persistentSupport.iniciarTransaccao();
			masterDegreeTemp = persistentDegree.readBySigla("MEEC");
			assertNotNull(masterDegreeTemp);

			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);

			degreeCurricularPlan =
				persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano2",
					masterDegreeTemp);
			assertNotNull(degreeCurricularPlan);

			executionDegree =
				persistentExecutionDegree
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

		try {
			persistentSupport.iniciarTransaccao();
			IMasterDegreeCandidate masterDegreeCandidate =
				persistentMasterDegreeCandidate
					.readByUsernameAndExecutionDegreeAndSpecialization(
					"nmsn",
					executionDegree,
					new Specialization(Specialization.MESTRADO));
			assertNotNull(masterDegreeCandidate);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

	}

	public void testReadByExecutionYear() {
		System.out.println("- Test 12 : Read By Execution Year");
		IExecutionYear executionYear = null;

		try {
			persistentSupport.iniciarTransaccao();

			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

		try {
			persistentSupport.iniciarTransaccao();
			List result =
				persistentMasterDegreeCandidate.readByExecutionYear(
					executionYear);
			assertNotNull(result);
			assertEquals(result.size(), 2);
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}

	}

	public void testReadByCandidateNumberAndExecutionDegreeAndSpecialization() {
		System.out.println(
			"- Test 13 : Read By Candidate Number And Execution Degree and Specialization");
		IMasterDegreeCandidate masterDegreeCandidate = null;
		ICursoExecucao executionDegree = null;
		ICurso degree = null;
		IExecutionYear executionYear = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		try {
			persistentSupport.iniciarTransaccao();

			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("MEEC");
			assertNotNull(degree);
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);

			degreeCurricularPlan =
				persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano2",
					degree);
			assertNotNull(degreeCurricularPlan);

			executionDegree =
				persistentExecutionDegree
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);

			// Read the Candidate			
			masterDegreeCandidate =
				persistentMasterDegreeCandidate
					.readByNumberAndExecutionDegreeAndSpecialization(
					new Integer(1),
					executionDegree,
					new Specialization(Specialization.MESTRADO));
			assertNotNull(masterDegreeCandidate);

			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getNumeroDocumentoIdentificacao()
					.equals(
					"4444444"));
			assertEquals(
				masterDegreeCandidate
					.getPerson()
					.getTipoDocumentoIdentificacao(),
				new TipoDocumentoIdentificacao(
					TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getLocalEmissaoDocumentoIdentificacao()
					.equals("Lisboa"));

			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getDataEmissaoDocumentoIdentificacao()
					.toString()
					.equals("2002-10-12"));
			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getDataValidadeDocumentoIdentificacao()
					.toString()
					.equals("2005-11-01"));
			assertTrue(
				masterDegreeCandidate.getPerson().getNome().equals("Nuno"));
			assertEquals(
				masterDegreeCandidate.getPerson().getSexo(),
				new Sexo(Sexo.MASCULINO));
			assertEquals(
				masterDegreeCandidate.getPerson().getEstadoCivil(),
				new EstadoCivil(EstadoCivil.SOLTEIRO));

			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getNascimento()
					.toString()
					.equals(
					"1979-05-12"));
			assertTrue(
				masterDegreeCandidate.getPerson().getNomePai().equals(
					"Manuel"));
			assertTrue(
				masterDegreeCandidate.getPerson().getNomeMae().equals("Maria"));
			assertTrue(
				masterDegreeCandidate.getPerson().getNacionalidade().equals(
					"Portuguesa"));
			assertTrue(
				masterDegreeCandidate.getPerson().getLocalidade().equals(
					"localidade Nuno"));
			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getFreguesiaNaturalidade()
					.equals(
					"Freguesia Nuno"));
			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getConcelhoNaturalidade()
					.equals(
					"Concelho Nuno"));
			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getDistritoNaturalidade()
					.equals(
					"Distrito Nuno"));
			assertTrue(
				masterDegreeCandidate.getPerson().getMorada().equals(
					"Morada Nuno"));
			assertTrue(
				masterDegreeCandidate.getPerson().getCodigoPostal().equals(
					"1700-200"));
			assertTrue(
				masterDegreeCandidate.getPerson().getFreguesiaMorada().equals(
					"frequesia morada Nuno"));
			assertTrue(
				masterDegreeCandidate.getPerson().getConcelhoMorada().equals(
					"concelho morada Nuno"));
			assertTrue(
				masterDegreeCandidate.getPerson().getDistritoMorada().equals(
					"distrito morada Nuno"));
			assertTrue(
				masterDegreeCandidate.getPerson().getTelefone().equals(
					"214443523"));
			assertTrue(
				masterDegreeCandidate.getPerson().getTelemovel().equals(
					"96546321"));
			assertTrue(
				masterDegreeCandidate.getPerson().getEmail().equals("s12@h.c"));
			assertTrue(
				masterDegreeCandidate.getPerson().getEnderecoWeb().equals(
					"http123"));
			assertTrue(
				masterDegreeCandidate.getPerson().getCodigoFiscal().equals(
					"444444444"));
			assertTrue(
				masterDegreeCandidate.getPerson().getProfissao().equals(
					"Profissao"));
			assertTrue(
				masterDegreeCandidate.getMajorDegree().equals("Informatica"));
			assertTrue(
				masterDegreeCandidate.getPerson().getUsername().equals("nmsn"));

			assertTrue(
				masterDegreeCandidate.getPerson().getPassword().equals(
					PasswordEncryptor.encryptPassword("pass")));
			assertTrue(
				masterDegreeCandidate.getCandidateNumber().equals(
					new Integer(1)));
			assertEquals(
				masterDegreeCandidate.getSpecialization(),
				new Specialization(Specialization.MESTRADO));
			assertTrue(
				masterDegreeCandidate.getMajorDegreeSchool().equals("IST"));
			assertTrue(
				masterDegreeCandidate.getMajorDegreeYear().equals(
					new Integer(2000)));
			assertTrue(
				masterDegreeCandidate.getAverage().equals(new Double(14.99)));

			assertEquals(masterDegreeCandidate.getSituations().size(), 2);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
	}

	public void testReadByExecutionDegreeAndPerson() {
		System.out.println("- Test 14 : Read By Execution Degree And Person");
		IMasterDegreeCandidate masterDegreeCandidate = null;
		ICursoExecucao executionDegree = null;
		ICurso degree = null;
		IExecutionYear executionYear = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		try {
			persistentSupport.iniciarTransaccao();

			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("MEEC");
			assertNotNull(degree);
			executionYear =
				persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);

			degreeCurricularPlan =
				persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano2",
					degree);
			assertNotNull(degreeCurricularPlan);

			executionDegree =
				persistentExecutionDegree
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);

			IPessoa person = new Pessoa();
			person.setUsername("nmsn");

			// Read the Candidate			
			masterDegreeCandidate =
				persistentMasterDegreeCandidate.readByExecutionDegreeAndPerson(
					executionDegree,
					person);
			assertNotNull(masterDegreeCandidate);

			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getNumeroDocumentoIdentificacao()
					.equals(
					"4444444"));
			assertEquals(
				masterDegreeCandidate
					.getPerson()
					.getTipoDocumentoIdentificacao(),
				new TipoDocumentoIdentificacao(
					TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE));
			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getLocalEmissaoDocumentoIdentificacao()
					.equals("Lisboa"));

			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getDataEmissaoDocumentoIdentificacao()
					.toString()
					.equals("2002-10-12"));
			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getDataValidadeDocumentoIdentificacao()
					.toString()
					.equals("2005-11-01"));
			assertTrue(
				masterDegreeCandidate.getPerson().getNome().equals("Nuno"));
			assertEquals(
				masterDegreeCandidate.getPerson().getSexo(),
				new Sexo(Sexo.MASCULINO));
			assertEquals(
				masterDegreeCandidate.getPerson().getEstadoCivil(),
				new EstadoCivil(EstadoCivil.SOLTEIRO));

			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getNascimento()
					.toString()
					.equals(
					"1979-05-12"));
			assertTrue(
				masterDegreeCandidate.getPerson().getNomePai().equals(
					"Manuel"));
			assertTrue(
				masterDegreeCandidate.getPerson().getNomeMae().equals("Maria"));
			assertTrue(
				masterDegreeCandidate.getPerson().getNacionalidade().equals(
					"Portuguesa"));
			assertTrue(
				masterDegreeCandidate.getPerson().getLocalidade().equals(
					"localidade Nuno"));
			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getFreguesiaNaturalidade()
					.equals(
					"Freguesia Nuno"));
			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getConcelhoNaturalidade()
					.equals(
					"Concelho Nuno"));
			assertTrue(
				masterDegreeCandidate
					.getPerson()
					.getDistritoNaturalidade()
					.equals(
					"Distrito Nuno"));
			assertTrue(
				masterDegreeCandidate.getPerson().getMorada().equals(
					"Morada Nuno"));
			assertTrue(
				masterDegreeCandidate.getPerson().getCodigoPostal().equals(
					"1700-200"));
			assertTrue(
				masterDegreeCandidate.getPerson().getFreguesiaMorada().equals(
					"frequesia morada Nuno"));
			assertTrue(
				masterDegreeCandidate.getPerson().getConcelhoMorada().equals(
					"concelho morada Nuno"));
			assertTrue(
				masterDegreeCandidate.getPerson().getDistritoMorada().equals(
					"distrito morada Nuno"));
			assertTrue(
				masterDegreeCandidate.getPerson().getTelefone().equals(
					"214443523"));
			assertTrue(
				masterDegreeCandidate.getPerson().getTelemovel().equals(
					"96546321"));
			assertTrue(
				masterDegreeCandidate.getPerson().getEmail().equals("s12@h.c"));
			assertTrue(
				masterDegreeCandidate.getPerson().getEnderecoWeb().equals(
					"http123"));
			assertTrue(
				masterDegreeCandidate.getPerson().getCodigoFiscal().equals(
					"444444444"));
			assertTrue(
				masterDegreeCandidate.getPerson().getProfissao().equals(
					"Profissao"));
			assertTrue(
				masterDegreeCandidate.getMajorDegree().equals("Informatica"));
			assertTrue(
				masterDegreeCandidate.getPerson().getUsername().equals("nmsn"));

			assertTrue(
				masterDegreeCandidate.getPerson().getPassword().equals(
					PasswordEncryptor.encryptPassword("pass")));
			assertTrue(
				masterDegreeCandidate.getCandidateNumber().equals(
					new Integer(1)));
			assertEquals(
				masterDegreeCandidate.getSpecialization(),
				new Specialization(Specialization.MESTRADO));
			assertTrue(
				masterDegreeCandidate.getMajorDegreeSchool().equals("IST"));
			assertTrue(
				masterDegreeCandidate.getMajorDegreeYear().equals(
					new Integer(2000)));
			assertTrue(
				masterDegreeCandidate.getAverage().equals(new Double(14.99)));

			assertEquals(masterDegreeCandidate.getSituations().size(), 2);

			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
	}

	public void testReadByExecutionDegree() {
		System.out.println("- Test 15 : Read By Execution Degree ");
		ICursoExecucao executionDegree = null;
		ICurso degree = null;
		List candidates = null;
		IExecutionYear executionYear = null;
		IDegreeCurricularPlan degreeCurricularPlan = null;
		try {
			persistentSupport.iniciarTransaccao();

			persistentSupport.iniciarTransaccao();
			degree = persistentDegree.readBySigla("MEEC");
			assertNotNull(degree);
			executionYear = persistentExecutionYear.readExecutionYearByName("2002/2003");
			assertNotNull(executionYear);

			degreeCurricularPlan =persistentDegreeCurricularPlan.readByNameAndDegree(
					"plano2",
					degree);
			assertNotNull(degreeCurricularPlan);

			executionDegree =
				persistentExecutionDegree
					.readByDegreeCurricularPlanAndExecutionYear(
					degreeCurricularPlan,
					executionYear);
			assertNotNull(executionDegree);

			// Read the Candidate			
			candidates =
				persistentMasterDegreeCandidate.readByExecutionDegree(
					executionDegree);
			assertNotNull(candidates);
			assertEquals(candidates.size(), 2);
			
			persistentSupport.confirmarTransaccao();
		} catch (ExcepcaoPersistencia ex) {
			fail("    -> Error on test");
		}
	}


} // End of test from Class MasterDegreeCandidateOJB
