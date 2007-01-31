package net.sourceforge.fenixedu.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.security.PasswordEncryptor;
import net.sourceforge.fenixedu.dataTransferObject.InfoPersonEditor;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.grant.owner.GrantOwner;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.domain.person.MaritalStatus;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class PersonTest extends DomainTestBase {

	InfoPersonEditor infoPerson1;
	InfoPersonEditor infoPerson2;
	InfoPersonEditor infoPerson3;

	Role personRole;
	Role teacherRole;
	Role coordinatorRole;
	Role grantOwnerRole;
	Role studentRole;
	Role employeeRole;

	Registration degreeStudent;
	Registration masterDegreeStudent;
    
    Teacher teacher;
    
    GrantOwner grantOwner;
    
    Employee employee;

	IDDocumentType documentType;

	Country country;

	MaritalStatus maritalStatus = MaritalStatus.WIDOWER;

	Boolean availableEmail = Boolean.TRUE;
	Boolean availablePhoto = Boolean.FALSE;
	Boolean availableWebSite = Boolean.TRUE;

    Integer teacherNumber = 11111;
    Integer grantOwnerNumber = 22222;
    Integer degreeStudentNumber = 33333;
    Integer masterDegreeStudentNumber = 44444;
    Integer employeeNumber = 55555;
    
	String username = "Z12345";
	String codigoFiscal = "codigoFiscal";
	String codigoPostal = "codigoPostal";
	String concelhoMorada = "concelhoMorada";
	String concelhoNaturalidade = "concelhoNaturalidade";
	String distritoMorada = "distritoMorada";
	String email = "email@host.com";
	String enderecoWeb = "http://enderecoWeb.com";
	String freguesiaMorada = "freguesiaMorada";
	String freguesiaNaturalidade = "freguesiaNaturalidade";
	String localEmissaoDocumentoIdentificacao = "localEmissao";
	String localidade = "localidade";
	String localidadeCodigoPostal = "localidadeCodigoPostal";
	String address = "endereco";
	String nacionalidade = "nacionalidade";
	String nome = "nome";
	String nomeMae = "nomeMae";
	String nomePai = "nomePai";
	String numContribuinte = "numContribuinte";
	String numDocumentoIdentificacao = "numDocID";
	String profissao = "profissao";
	String telefone = "telefone";
	String telemovel = "telemovel";
	String workPhone = "workPhone";

	Date dataEmissao = new Date(System.currentTimeMillis() - 7000000);
	Date dataValidade = new Date(System.currentTimeMillis() + 7000000);
	Date dataNascimento = new Date(System.currentTimeMillis() - 20000000);

	Gender sexo = Gender.MALE;

	protected void setUp() throws Exception {
	    /*
		super.setUp();

		country = new Country("Portugal", "Portuguese", "PT");

		documentType = IDDocumentType.AIR_FORCE_IDENTITY_CARD;

		personRole = new Role(RoleType.PERSON, null, null, null);
        
		teacherRole = new Role(RoleType.TEACHER, null, null, null);
        
		coordinatorRole = new Role(RoleType.COORDINATOR, null, null, null);
        
		grantOwnerRole = new Role(RoleType.GRANT_OWNER, null, null, null);
        
		studentRole = new Role(RoleType.STUDENT, null, null, null);

		employeeRole = new Role(RoleType.EMPLOYEE, null, null, null);

//        teacher = new Teacher();
        teacher.setTeacherNumber(teacherNumber);
        
//        employee = new Employee();
        employee.setEmployeeNumber(employeeNumber);

        degreeStudent = new Registration();
        degreeStudent.setNumber(degreeStudentNumber);
        degreeStudent.setDegreeType(DegreeType.DEGREE);                

		masterDegreeStudent = new Registration();
		masterDegreeStudent.setDegreeType(DegreeType.MASTER_DEGREE);
        masterDegreeStudent.setNumber(masterDegreeStudentNumber);

        
        
		infoPerson1 = new InfoPersonEditor();
		infoPerson1.setAvailableEmail(availableEmail);
		infoPerson1.setAvailablePhoto(availablePhoto);
		infoPerson1.setAvailableWebSite(availableWebSite);
		infoPerson1.setCodigoFiscal(codigoFiscal);
		infoPerson1.setCodigoPostal(codigoPostal);
		infoPerson1.setConcelhoMorada(concelhoMorada);
		infoPerson1.setConcelhoNaturalidade(concelhoNaturalidade);
		infoPerson1.setDataEmissaoDocumentoIdentificacao(dataEmissao);
		infoPerson1.setDataValidadeDocumentoIdentificacao(dataValidade);
		infoPerson1.setDistritoMorada(distritoMorada);
		infoPerson1.setEmail(email);
		infoPerson1.setEnderecoWeb(enderecoWeb);
		infoPerson1.setFreguesiaMorada(freguesiaMorada);
		infoPerson1.setFreguesiaNaturalidade(freguesiaNaturalidade);
		infoPerson1
				.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
		infoPerson1.setLocalidade(localidade);
		infoPerson1.setLocalidadeCodigoPostal(localidadeCodigoPostal);
		infoPerson1.setMaritalStatus(maritalStatus);
		infoPerson1.setMorada(address);
		infoPerson1.setNascimento(dataNascimento);
		infoPerson1.setNome(nome);
		infoPerson1.setNomeMae(nomeMae);
		infoPerson1.setNomePai(nomePai);
		infoPerson1.setNumContribuinte(numContribuinte);
		infoPerson1.setNumeroDocumentoIdentificacao(numDocumentoIdentificacao);
		infoPerson1.setProfissao(profissao);
		infoPerson1.setSexo(sexo);
		infoPerson1.setTelefone(telefone);
		infoPerson1.setTelemovel(telemovel);
		infoPerson1.setTipoDocumentoIdentificacao(documentType);
		infoPerson1.setWorkPhone(workPhone);

		infoPerson2 = new InfoPersonEditor();
		infoPerson2.setAvailableEmail(!availableEmail);
		infoPerson2.setAvailablePhoto(!availablePhoto);
		infoPerson2.setAvailableWebSite(!availableWebSite);
		infoPerson2.setCodigoFiscal(codigoFiscal + 1);
		infoPerson2.setCodigoPostal(codigoPostal + 1);
		infoPerson2.setConcelhoMorada(concelhoMorada + 1);
		infoPerson2.setConcelhoNaturalidade(concelhoNaturalidade + 1);
		infoPerson2.setDataEmissaoDocumentoIdentificacao(new Date(1));
		infoPerson2.setDataValidadeDocumentoIdentificacao(new Date(2));
		infoPerson2.setDistritoMorada(distritoMorada + 1);
		infoPerson2.setEmail(email + 1);
		infoPerson2.setEnderecoWeb(enderecoWeb + 1);
		infoPerson2.setFreguesiaMorada(freguesiaMorada + 1);
		infoPerson2.setFreguesiaNaturalidade(freguesiaNaturalidade + 1);
		infoPerson2
				.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao + 1);
		infoPerson2.setLocalidade(localidade + 1);
		infoPerson2.setLocalidadeCodigoPostal(localidadeCodigoPostal + 1);
		infoPerson2.setMaritalStatus(MaritalStatus.CIVIL_UNION);
		infoPerson2.setMorada(address + 1);
		infoPerson2.setNascimento(new Date(3));
		infoPerson2.setNome(nome + 1);
		infoPerson2.setNomeMae(nomeMae + 1);
		infoPerson2.setNomePai(nomePai + 1);
		infoPerson2.setNumContribuinte(numContribuinte + 1);
		infoPerson2
				.setNumeroDocumentoIdentificacao(numDocumentoIdentificacao + 1);
		infoPerson2.setProfissao(profissao + 1);
		infoPerson2.setSexo(Gender.FEMALE);
		infoPerson2.setTelefone(telefone + 1);
		infoPerson2.setTelemovel(telemovel + 1);
		infoPerson2.setTipoDocumentoIdentificacao(documentType);
		infoPerson2.setWorkPhone(workPhone + 1);

		infoPerson3 = new InfoPersonEditor();
		infoPerson3.setAvailableEmail(!availableEmail);
		infoPerson3.setAvailablePhoto(!availablePhoto);        
		infoPerson3.setAvailableWebSite(!availableWebSite);
		infoPerson3.setCodigoFiscal(codigoFiscal);
		infoPerson3.setCodigoPostal(codigoPostal);
		infoPerson3.setConcelhoMorada(concelhoMorada);
		infoPerson3.setConcelhoNaturalidade(concelhoNaturalidade);
		infoPerson3.setDataEmissaoDocumentoIdentificacao(dataEmissao);
		infoPerson3.setDataValidadeDocumentoIdentificacao(dataValidade);
		infoPerson3.setDistritoMorada(distritoMorada);
		infoPerson3.setEmail(email + 1);
		infoPerson3.setEnderecoWeb(enderecoWeb + 1);
		infoPerson3.setFreguesiaMorada(freguesiaMorada);
		infoPerson3.setFreguesiaNaturalidade(freguesiaNaturalidade);
		infoPerson3
				.setLocalEmissaoDocumentoIdentificacao(localEmissaoDocumentoIdentificacao);
		infoPerson3.setLocalidade(localidade);
		infoPerson3.setLocalidadeCodigoPostal(localidadeCodigoPostal);
		infoPerson3.setMaritalStatus(maritalStatus);
		infoPerson3.setMorada(address);
		infoPerson3.setNascimento(dataNascimento);
		infoPerson3.setNome(nome);
		infoPerson3.setNomeMae(nomeMae);
		infoPerson3.setNomePai(nomePai);
		infoPerson3.setNumContribuinte(numContribuinte);
		infoPerson3.setNumeroDocumentoIdentificacao(numDocumentoIdentificacao);
		infoPerson3.setProfissao(profissao);
		infoPerson3.setSexo(sexo);
		infoPerson3.setTelefone(telefone);
		infoPerson3.setTelemovel(telemovel + 1);
		infoPerson3.setTipoDocumentoIdentificacao(documentType);
		infoPerson3.setWorkPhone(workPhone + 1);

        grantOwner = new GrantOwner();
        grantOwner.setNumber(grantOwnerNumber);
        */
	}

	public void testCreatePerson1() {
		Person person = new Person(infoPerson1, country);        

		assertPersonContent(person, country);
	}

	public void testCreatePerson2() {
	    /*
		Person person = new Person(nome, numDocumentoIdentificacao,
				documentType, sexo, username);

		assertEquals(person.getNome(), nome);
		assertEquals(person.getDocumentIdNumber(),
				numDocumentoIdentificacao);
		assertEquals(person.getIdDocumentType(), documentType);
		assertEquals(person.getGender(), sexo);
        assertEquals(person.getUsername(), username);
        */
	}

	public void testCreatePerson3() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);

		assertEquals(person.getUsername(), username);
		assertEquals(person.getNome(), nome);
		assertEquals(person.getGender(), Gender.MALE);
		assertEquals(person.getAddress(), address);
		assertEquals(person.getPhone(), telefone);
		assertEquals(person.getMobile(), telemovel);
		assertEquals(person.getWebAddress(), enderecoWeb);
		assertEquals(person.getEmail(), email);
		assertEquals(person.getDocumentIdNumber(),
				numDocumentoIdentificacao);
		assertEquals(person.getIdDocumentType(), IDDocumentType.EXTERNAL);
		assertEquals(person.getAvailableEmail(), Boolean.FALSE);
		assertEquals(person.getAvailablePhoto(), Boolean.FALSE);
		assertEquals(person.getAvailableWebSite(), Boolean.FALSE);
		*/
	}

	public void testEditPerson() {
		Person person = new Person(infoPerson2, country);

		person.edit(infoPerson1, country);
		assertPersonContent(person, country);
	}

	public void testEditPersonalContactInformation() {
		Person person = new Person(infoPerson3, country);

		person.editPersonalContactInformation(infoPerson1);
		assertPersonContent(person, country);
	}

	public void testEdit() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		person.edit("new" + nome, "new" + address, "new" + telefone, "new"
				+ telemovel, "new" + enderecoWeb, "new" + email);

		assertEquals(person.getNome(), "new" + nome);
		assertEquals(person.getAddress(), "new" + address);
		assertEquals(person.getPhone(), "new" + telefone);
		assertEquals(person.getWebAddress(), "new" + enderecoWeb);
		assertEquals(person.getEmail(), "new" + email);
*/
	}

	public void testChangeUsername() {
		Person person = new Person(infoPerson1, country);
		person.setUsername(RoleType.PERSON);
		String personOriginalUsername = person.getUsername();
		Person person2 = new Person(infoPerson2, country);
		person2.setUsername(RoleType.PERSON);
		Person person3 = new Person(infoPerson3, country);
		person3.setUsername(RoleType.PERSON);

		person.changeUsername(RoleType.PERSON);
		assertTrue(!personOriginalUsername.equals(person.getUsername()));

		try {
			person.changeUsername(null);
			fail("Empty role type - should have thrown a DomainException");
		} catch (DomainException domainException) {
			// caught expected exception
		}
		assertEquals(person.getUsername(), "newUsername");

		try {
			person.changeUsername(null);
			fail("Null username - should have thrown a DomainException");
		} catch (DomainException domainException) {
			// caught expected exception
		}
		assertEquals(person.getUsername(), "newUsername");

	}

	public void testChangePassword() {

		String someOldPassword = "someOldPassword";
		String newPassword = "newPassword";

		Person person = new Person(infoPerson1, country);
		person.setPassword(PasswordEncryptor.encryptPassword(someOldPassword));

		try {
			person.changePassword("fakePassword", someOldPassword);
			fail("Invalid old password!");
		} catch (DomainException domainException) {
			// this is good, the old password must be correct
		}

		try {
			person.changePassword(someOldPassword, someOldPassword);
			fail("New password must differ from old password");
		} catch (DomainException domainException) {
			// this is good, the new password cannot be the same
		}

		try {
			person.changePassword(someOldPassword, null);
			fail("New password must differ from null");
		} catch (DomainException domainException) {
			// this is good, it should not allow null passwords
		}

		try {
			person.changePassword(someOldPassword, "");
			fail("New password must not be empty");
		} catch (DomainException domainException) {
			// nor empty passwords
		}

		try {
			person.changePassword(someOldPassword, new String(
					numDocumentoIdentificacao));
			fail("New password must differ from the identification document number");
		} catch (DomainException domainException) {
			// nor the identification number
		}

		try {
			person.changePassword(someOldPassword, new String(codigoFiscal));
			fail("New password must differ from the fiscal code number");
		} catch (DomainException domainException) {
			// nor the fiscal code
		}

		try {
			person.changePassword(someOldPassword, new String(numContribuinte));
			fail("New password must differ from the taxpayer number");
		} catch (DomainException domainException) {
			// nor the taxpayer number
		}

		try {
			person.changePassword(someOldPassword, newPassword);
		} catch (DomainException domainException) {
			fail("Should have changed the password, it is a valid one");
		}

		assertTrue(PasswordEncryptor.areEquals(person.getPassword(),
				newPassword));
	}

	public void testAddRoleNormal() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		assertEquals(person.getPersonRolesCount(), 0);
		person.getPersonRoles().add(personRole);
		assertEquals(person.getPersonRolesCount(), 1);
		assertTrue(person.getPersonRoles().contains(personRole));
        
        person.setTeacher(teacher);
		person.getPersonRoles().add(teacherRole);
		assertEquals(person.getPersonRolesCount(), 2);
		assertTrue(person.getPersonRoles().contains(teacherRole));
        
		person.getPersonRoles().add(coordinatorRole);
		assertEquals(person.getPersonRolesCount(), 3);
		assertTrue(person.getPersonRoles().contains(coordinatorRole));
		*/
	}

	public void testAddRoleDependencies() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
        person.setTeacher(teacher);
		assertEquals(person.getPersonRolesCount(), 0);
		try {
			person.getPersonRoles().add(teacherRole);
			fail("The test shouldn't have successfully added the TeacherRole - Dependencies not working correctly");
		} catch (DomainException domainException) {
			assertEquals(person.getUsername(), username);
			// everything went has planned
		}
		person.getPersonRoles().add(personRole);
		try {
			person.getPersonRoles().add(teacherRole);
			// everything went has planned
		} catch (DomainException domainException) {
			fail("The test should have successfully added the TeacherRole - Dependencies not working correctly");
		}
		*/
	}
    
    public void testAddRoleWhenTheConnectionDoesntExist()
    {
	/*
        Person person = new Person(username, nome, Gender.MALE, address,
                telefone, telemovel, enderecoWeb, email,
                numDocumentoIdentificacao, IDDocumentType.EXTERNAL);   
        try {
            person.getPersonRoles().add(teacherRole);
        } catch (DomainException domainException) {
            //Everything went has planned
        }
        */
    }

	public void testAddRoleTwice() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		assertEquals(person.getPersonRolesCount(), 0);
		person.getPersonRoles().add(personRole);
		try {
			person.getPersonRoles().add(personRole);
			//Everything went has planned
		} catch (Exception exception) {
            fail("The test should have run successfully since there is no problem adding a role to a person that already had that role.");
		}
		*/
	}

	public void testRemoveRole1() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		assertEquals(person.getPersonRolesCount(), 0);
		person.getPersonRoles().add(personRole);
		assertEquals(person.getPersonRolesCount(), 1);
		person.getPersonRoles().remove(personRole);
		assertEquals(person.getPersonRolesCount(), 0);
		*/
	}

	public void testRemoveRole2() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
        Teacher teacher = new Teacher(Integer.valueOf(1), person);
        Coordinator coordinator = new Coordinator();
        coordinator.setPerson(person);
		person.getPersonRoles().add(personRole);
		person.getPersonRoles().add(teacherRole);
		person.getPersonRoles().add(coordinatorRole);
		assertEquals(person.getPersonRolesCount(), 3);

		person.getPersonRoles().remove(teacherRole);
		assertEquals(person.getPersonRolesCount(), 1);
		assertFalse(person.getPersonRoles().contains(teacherRole));
		assertFalse(person.getPersonRoles().contains(coordinatorRole));
		*/
	}

	public void testUsernameUpdate1() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		person.getStudent().getRegistrations().add(degreeStudent);
        person.setGrantOwner(grantOwner);
        person.setEmployee(employee);
        person.setTeacher(teacher);

		assertEquals(person.getUsername(), username);
		person.getPersonRoles().add(personRole);
		assertEquals(person.getUsername(), "P12345");
		person.getPersonRoles().add(grantOwnerRole);
		assertEquals(person.getUsername(), "B"+grantOwnerNumber);
		person.getPersonRoles().add(studentRole);
		assertEquals(person.getUsername(), "L"+degreeStudentNumber);
		person.getPersonRoles().add(employeeRole);
		assertEquals(person.getUsername(), "F"+employeeNumber);
		person.getPersonRoles().add(teacherRole);
		assertEquals(person.getUsername(), "D"+teacherNumber);
		*/
	}

	public void testUsernameUpdate2() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		person.getStudent().getRegistrations().add(degreeStudent);
		person.getStudent().getRegistrations().add(masterDegreeStudent);
        person.setGrantOwner(grantOwner);
        person.setEmployee(employee);
        person.setTeacher(teacher);

		assertEquals(person.getUsername(), username);
		person.getPersonRoles().add(personRole);
		assertEquals(person.getUsername(), "P" + username.substring(1));
		person.getPersonRoles().add(grantOwnerRole);
        assertEquals(person.getUsername(), "B"+grantOwnerNumber);
		person.getPersonRoles().add(studentRole);
		assertEquals(person.getUsername(), "M"+masterDegreeStudentNumber);
		person.getPersonRoles().add(employeeRole);
        assertEquals(person.getUsername(), "F"+employeeNumber);
		person.getPersonRoles().add(teacherRole);
        assertEquals(person.getUsername(), "D"+teacherNumber);
        */
	}

	public void testUsernameUpdate3() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
		person.getStudent().getRegistrations().add(degreeStudent);
        person.setGrantOwner(grantOwner);
        person.setEmployee(employee);
        person.setTeacher(teacher);

		person.getPersonRoles().add(personRole);
		person.getPersonRoles().add(grantOwnerRole);
		person.getPersonRoles().add(studentRole);
		person.getPersonRoles().add(employeeRole);
		person.getPersonRoles().add(teacherRole);

		person.getPersonRoles().remove(teacherRole);
		assertEquals(person.getUsername(), "L33333");
		person.getPersonRoles().remove(employeeRole);
		assertEquals(person.getUsername(), "L33333");
		person.getPersonRoles().remove(studentRole);
		assertEquals(person.getUsername(), "B22222");
		person.getPersonRoles().remove(grantOwnerRole);
		assertEquals(person.getUsername(), "P22222");
		*/
	}

	public void testUsernameUpdate4() {
	    /*
		Person person = new Person(username, nome, Gender.MALE, address,
				telefone, telemovel, enderecoWeb, email,
				numDocumentoIdentificacao, IDDocumentType.EXTERNAL);
        person.getStudent().getRegistrations().add(degreeStudent);
        person.setGrantOwner(grantOwner);

		assertEquals(person.getUsername(), username);
		person.getPersonRoles().add(personRole);
		person.getPersonRoles().add(grantOwnerRole);
		person.getPersonRoles().add(studentRole);
		person.getPersonRoles().remove(grantOwnerRole);
		assertEquals(person.getUsername(), "L"+degreeStudentNumber);
		*/
	}

	private void assertPersonContent(Person person, Country country) {

		assertEquals(person.getPais(), country);
		assertEquals(person.getAvailableEmail(), availableEmail);
		assertEquals(person.getAvailablePhoto(), availablePhoto);
		assertEquals(person.getAvailableWebSite(), availableWebSite);
		assertEquals(person.getFiscalCode(), codigoFiscal);
		assertEquals(person.getAreaCode(), codigoPostal);
		assertEquals(person.getDistrictSubdivisionOfResidence(), concelhoMorada);
		assertEquals(person.getDistrictSubdivisionOfBirth(), concelhoNaturalidade);
		assertEquals(person.getEmissionDateOfDocumentId(), dataEmissao);
		assertEquals(person.getExpirationDateOfDocumentId(),
				dataValidade);
		assertEquals(person.getDistrictOfResidence(), distritoMorada);
		assertEquals(person.getEmail(), email);
		assertEquals(person.getWebAddress(), enderecoWeb);
		assertEquals(person.getParishOfResidence(), freguesiaMorada);
		assertEquals(person.getParishOfBirth(), freguesiaNaturalidade);
		assertEquals(person.getPais().getName(), "Portugal");
		assertEquals(person.getPais().getCode(), "PT");
		assertEquals(person.getPais().getNationality(), "Portuguese");
		assertEquals(person.getEmissionLocationOfDocumentId(),
				localEmissaoDocumentoIdentificacao);
		assertEquals(person.getArea(), localidade);
		assertEquals(person.getAreaOfAreaCode(), localidadeCodigoPostal);
		assertEquals(person.getMaritalStatus(), maritalStatus);
		assertEquals(person.getAddress(), address);
		if (country == null) {
			assertEquals(person.getNacionalidade(), nacionalidade);
		} else {
			assertEquals(person.getNacionalidade(), person.getPais()
					.getNationality());
		}
		assertEquals(person.getDateOfBirth(), dataNascimento);
		assertEquals(person.getNome(), nome);
		assertEquals(person.getNameOfMother(), nomeMae);
		assertEquals(person.getNameOfFather(), nomePai);
		assertEquals(person.getSocialSecurityNumber(), numContribuinte);
		assertEquals(person.getDocumentIdNumber(),
				numDocumentoIdentificacao);
		assertEquals(person.getProfession(), profissao);
		assertEquals(person.getGender(), sexo);
		assertEquals(person.getPhone(), telefone);
		assertEquals(person.getMobile(), telemovel);
		assertEquals(person.getIdDocumentType(), documentType);
		assertEquals(person.getWorkPhone(), workPhone);

	}

	public void testIndicatePrivledges() {
		Set<Role> roles = new HashSet<Role>();
		roles.add(personRole);
		roles.add(employeeRole);
		roles.add(teacherRole);
		roles.add(coordinatorRole);

		Person person = new Person();
		person.setEmployee(employee);
		person.setTeacher(teacher);
		person.setUsername(RoleType.PERSON);
		person.indicatePrivledges(roles);

		assertTrue(person.getPersonRoles().containsAll(roles));
		assertTrue(roles.containsAll(person.getPersonRoles()));

		roles.remove(teacherRole);
		try {
			person.indicatePrivledges(roles);
			fail("Expected a domain excpetion for attempting to indicate inconsistent set of roles.");
		} catch (DomainException ex) {
		}

		roles.add(teacherRole);
		try {
			person.indicatePrivledges(roles);
		} catch (DomainException ex) {
			fail("Order of the arguments should not matter!");
		}
	}

}
