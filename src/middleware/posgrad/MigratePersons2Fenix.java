package middleware.posgrad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.Funcionario;
import Dominio.IPersonRole;
import Dominio.IPessoa;
import Dominio.Pessoa;
import ServidorAplicacao.security.PasswordEncryptor;
import Util.EstadoCivil;
import Util.RoleType;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class MigratePersons2Fenix {

	PersistenceBroker broker = null;
	
	
	public MigratePersons2Fenix() {
		broker = PersistenceBrokerFactory.defaultPersistenceBroker();
	}


	public static void main(String args[]) throws Exception{
		MigratePersons2Fenix migratePersons2Fenix = new MigratePersons2Fenix();
		
//		migratePersons2Fenix.broker.beginTransaction();
//		migratePersons2Fenix.broker.clearCache();
		migratePersons2Fenix.migratePosgradPessoa2Fenix();
		
//		migratePersons2Fenix.broker.commitTransaction();
	}
	
	private void migratePosgradPessoa2Fenix() throws Exception{
		IPessoa person2Write = null;
		Posgrad_pessoa person2Convert = null;
		List result = null;
		Query query = null;
		Criteria criteria = null;
		int personsWritten = 0;
		int personsNotWritten = 0;
		int rolesWritten = 0;
		
		
		try {
			System.out.print("A Ler Pessoas de Pos-Graduacao ...");
			
			
			broker.beginTransaction();
			broker.clearCache();
			
			List pessoasPG = getPessoas();
			System.out.println("  Done !");
			
			System.out.println("A Converter " + pessoasPG.size() + " pessoas de Pos-Graduacao para o Fenix ...");

			broker.commitTransaction();

			Iterator iterator = pessoasPG.iterator();
			while(iterator.hasNext()){
				
				broker.beginTransaction();
				broker.clearCache();
				
				person2Convert = (Posgrad_pessoa) iterator.next();
				person2Write = new Pessoa();
				// Remove the PosGrad User
				
				if ((person2Convert.getUsername().equalsIgnoreCase("posgrad2002")) && (person2Convert.getPassword().equalsIgnoreCase("aplica2002"))){
					personsNotWritten++;
					continue;
				}
					

				// Verificar o Tipo de Documento
				TipoDocumentoIdentificacao identificationDocumentType = null;
				if (person2Convert.getTipodocumentoidentificacao().equalsIgnoreCase("BILHETE DE IDENTIDADE")){
					identificationDocumentType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);
				} else if (person2Convert.getTipodocumentoidentificacao().equalsIgnoreCase("PASSAPORTE")){
					identificationDocumentType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.PASSAPORTE);
				} else identificationDocumentType = new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.OUTRO);

				
				criteria = new Criteria();
				criteria.addEqualTo("numeroDocumentoIdentificacao",person2Convert.getNumerodocumentoidentificacao());
				criteria.addEqualTo("tipoDocumentoIdentificacao", identificationDocumentType);
				query = new QueryByCriteria(Pessoa.class,criteria);
				result = (List) broker.getCollectionByQuery(query);

				if (result.size() == 0){
					copyPessoaPG2Person(person2Write, person2Convert, identificationDocumentType);
					person2Write.setPersonRoles(new ArrayList());
					personsWritten++;
				} else {
					person2Write = (IPessoa) result.get(0);
					
					System.out.println("A Pessoa " + person2Write.getNome() + " ja existe.");
					personsNotWritten++;
					// Verificar se a Pessoa e um Funcionario
					criteria = new Criteria();
					
					// A classe funcionario ainda usa os codigos internos por isso temos de procurar pelos codigos internos
					Pessoa personTemp = (Pessoa) person2Write;
					criteria.addEqualTo("chavePessoa", personTemp.getIdInternal());
					query = new QueryByCriteria(Funcionario.class,criteria);
					result = (List) broker.getCollectionByQuery(query);
					
					if (result.size() == 0){
						// A pessoa nao e um funcionario. Entao vamos fazer update dos campos todos
						copyPessoaPG2Person(person2Write, person2Convert, identificationDocumentType);
					}
					
				}
				
				IPersonRole personRole = RoleUtils.readPersonRole((Pessoa) person2Write, RoleType.PERSON, broker);
				if (personRole == null){
 			  		person2Write.getPersonRoles().add(RoleUtils.readRole(RoleType.PERSON, broker));
					rolesWritten++;
				}
				broker.store(person2Write);			
				broker.commitTransaction();	
			}
			System.out.println("   Persons Written : " + personsWritten);
			System.out.println("   Persons NOT Written : " + personsNotWritten);
			System.out.println("   Roles Written : " + rolesWritten);
			System.out.println("   Success !");
		
		} catch(Exception e) {
			System.out.println("Erro na Pessoa de Pos-Graduacao : " + person2Convert.getNome());
			throw new Exception(e);
		}	
					
	}
	
	private List getPessoas() throws Exception {
		Criteria criteria = new Criteria();
		Query query = new QueryByCriteria(Posgrad_pessoa.class,criteria);
		return (List) broker.getCollectionByQuery(query);
	}


	private void copyPessoaPG2Person(IPessoa person2Write, Posgrad_pessoa person2Convert, TipoDocumentoIdentificacao identificationDocumentType) throws Exception{

		try{
			person2Write.setNumeroDocumentoIdentificacao(person2Convert.getNumerodocumentoidentificacao());
			person2Write.setTipoDocumentoIdentificacao(identificationDocumentType);
			person2Write.setLocalEmissaoDocumentoIdentificacao(person2Convert.getLocalemissaodocumentoidentificacao());
			person2Write.setDataEmissaoDocumentoIdentificacao(person2Convert.getDataemissaodocumentoidentificacao());
			person2Write.setNome(person2Convert.getNome());
			person2Write.setNomePai(person2Convert.getNomepai());
			person2Write.setNomeMae(person2Convert.getNomemae());
						
			// Verificar o Sexo
			
			if (person2Convert.getSexo() != null){			
				if (person2Convert.getSexo().equalsIgnoreCase("masculino"))
					person2Write.setSexo(new Sexo(Sexo.MASCULINO));
				else if (person2Convert.getSexo().equalsIgnoreCase("feminino"))
					person2Write.setSexo(new Sexo(Sexo.FEMININO));
//				else {
//					System.out.println("Erro a converter Pessoa  " + person2Convert.getNome() + ". Erro no SEXO. (Encontrado: " + person2Convert.getSexo() + ")");
//				} 
			}						
			// Verificar o Estado Civil
			if (person2Convert.getEstadocivil() != null) {
				if (person2Convert.getEstadocivil().equalsIgnoreCase("solteiro")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.SOLTEIRO));
				} else if (person2Convert.getEstadocivil().equalsIgnoreCase("casado")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.CASADO));
				} else if (person2Convert.getEstadocivil().equalsIgnoreCase("divorciado")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.DIVORCIADO));
				} else if (person2Convert.getEstadocivil().equalsIgnoreCase("viúvo")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.VIUVO));
				} else if (person2Convert.getEstadocivil().equalsIgnoreCase("separado")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.SEPARADO));
				} else if (person2Convert.getEstadocivil().equalsIgnoreCase("união de facto")){
					person2Write.setEstadoCivil(new EstadoCivil(EstadoCivil.UNIAO_DE_FACTO));
				} 
//				else {
//					System.out.println("Erro a converter Pessoa  " + person2Convert.getNome() + ". Erro no ESTADO CIVIL. (encontrado: " + person2Convert.getEstadocivil() + ")");
//				} 
			}
			
			person2Write.setNascimento(person2Convert.getNascimento());
			person2Write.setNacionalidade(person2Convert.getNacionalidade());
			person2Write.setFreguesiaNaturalidade(person2Convert.getFreguesianaturalidade());
			person2Write.setConcelhoNaturalidade(person2Convert.getConcelhonaturalidade());
			person2Write.setDistritoNaturalidade(person2Convert.getDistritonaturalidade());
			person2Write.setMorada(person2Convert.getMorada());
			person2Write.setLocalidade(person2Convert.getLocalidade());
			person2Write.setCodigoPostal(person2Convert.getCodigopostal());
			person2Write.setFreguesiaMorada(person2Convert.getFreguesiamorada());
			person2Write.setConcelhoMorada(person2Convert.getConcelhomorada());
			person2Write.setDistritoMorada(person2Convert.getDistritomorada());
			person2Write.setTelefone(person2Convert.getTelefone());
//			person2Write.setTelemovel(person2Convert.getTelemovel());
//			person2Write.setEmail(person2Convert.getEmail());
//			person2Write.setEnderecoWeb(person2Convert.getEnderecoweb());
			person2Write.setNumContribuinte(person2Convert.getNumcontribuinte());
			person2Write.setProfissao(person2Convert.getProfissao());
			person2Write.setUsername(person2Convert.getUsername());
			
			
			// FIXME: Depois de correr a primeira vez comentar a linha seguinte
			person2Write.setPassword(PasswordEncryptor.encryptPassword(person2Convert.getNumerodocumentoidentificacao()));
		
		} catch(Exception e){
			System.out.println("Erro a converter a Pessoa " + person2Convert.getNome());
			throw new Exception(e);
		}
	}


}
