/*
 * Created on Feb 25, 2004
 */

package middleware.grantMigration;

import java.util.List;
import middleware.middlewareDomain.grant.MWPerson;
import middleware.middlewareDomain.grant.MWTeacher;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import middleware.persistentMiddlewareSupport.grant.IPersistentMWPersonOJB;
import middleware.persistentMiddlewareSupport.grant.IPersistentMWTeacherOJB;
import org.apache.ojb.broker.PersistenceBroker;
import Dominio.IPessoa;
import Dominio.Pessoa;
import Dominio.grant.owner.IGrantOwner;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPessoaPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.grant.IPersistentGrantOwner;
import Util.EstadoCivil;
import Util.Sexo;
import Util.TipoDocumentoIdentificacao;
/**
 * @author pica
 * @author barbosa
 */
public class MigrateGrantOwners
{

	public static void main(String args[])
	{
		//Migrate grant owner by person!!!
		//Read persons, construct the tree of data and migrate (sucessfully we
		// hope!)
		GrantMigrationLog grantMigrationLog = null;
		GrantMigrationStats grantMigrationStats = null;
		try
		{
			//Objectos informativos
			grantMigrationLog = new GrantMigrationLog();
			grantMigrationStats = new GrantMigrationStats();
			//Iniciar os objectos persistentes
			SuportePersistenteOJB fenixPersistentSuport = SuportePersistenteOJB.getInstance();
			IPersistentGrantOwner persistentGrantOwner = fenixPersistentSuport
					.getIPersistentGrantOwner();
			IPessoaPersistente pessoaPersistente = fenixPersistentSuport.getIPessoaPersistente();
			IPersistentMiddlewareSupport middlewareSuport = PersistentMiddlewareSupportOJB.getInstance();
			IPersistentMWPersonOJB persistentMWPerson = middlewareSuport.getIPersistentMWPerson();
			IPersistentMWTeacherOJB persistentMWTeacher = middlewareSuport.getIPersistentMWTeacher();
			//Ler todas as pessoas
			fenixPersistentSuport.iniciarTransaccao();
			Integer numberOfPersons = persistentMWPerson.countAll();
			fenixPersistentSuport.confirmarTransaccao();
			grantMigrationLog.writeLogLn("*****Numero de pessoas a migrar: " + numberOfPersons);
			int numberOfElementsInSpan = 1000;
			int numberOfSpans = numberOfPersons.intValue() / numberOfElementsInSpan;
			numberOfSpans = numberOfPersons.intValue() % numberOfElementsInSpan > 0
					? numberOfSpans + 1
					: numberOfSpans;
			grantMigrationLog.writeLogLn("*****Blocos de " + numberOfElementsInSpan + " pessoas.");
			grantMigrationLog.writeLogLn("*****Numero de Blocos: " + numberOfSpans + "\n\n");
			for (int span = 0; span < numberOfSpans; span++)
			{
				//Ler as pessoas aos conjuntos de mil
				fenixPersistentSuport.iniciarTransaccao();
				fenixPersistentSuport.clearCache();
				grantMigrationLog.writeLog("\n\n$$$$$$$[MINFO] Reading Span number " + (span + 1)
						+ " of MWPersons...");
				List result = persistentMWPerson.readAllBySpan(new Integer(span), new Integer(
						numberOfElementsInSpan));
				grantMigrationLog.writeLogLn(" Finished!\n\n");
				fenixPersistentSuport.confirmarTransaccao();
				//Ciclo para percorrer as pessoas lidas
				for (int i = 0; i < result.size(); i++)
				{
					fenixPersistentSuport.iniciarTransaccao();
					try
					{
						MWPerson mwPerson = (MWPerson) result.get(i);
						//Fazer o que temos a fazer pessoa a pessoa
						MigratePerson(fenixPersistentSuport, mwPerson, persistentMWPerson,
								persistentGrantOwner, pessoaPersistente, persistentMWTeacher,
								grantMigrationLog, grantMigrationStats);
						fenixPersistentSuport.confirmarTransaccao();
					}
					catch (Exception e)
					{
						grantMigrationLog.writeLog("!!!!!!!!!!!![ERROR]: " + e.getMessage());
					}
				}
			}
			grantMigrationLog.writeLog(grantMigrationStats.presentStats());
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	} /*
	   * 1- Verificar se a pessoa ja existe 1.1.1 Se já existe copiar o id da
	   * pessoa no fenix para a coluna da tabela mwgrant_migracao_pessoa 1.1.2
	   * Se não existe inserir essa pessoa no fenix
	   * 
	   * 1.2 - Verificar se existe algum grant_owner a apontar para essa
	   * pessoa. 1.2.2 Se sim, fazer o update da key_person para a pessoa na
	   * tabela person do fenix
	   */
	private static void MigratePerson(SuportePersistenteOJB fenixPersistentSuport, MWPerson mwPerson,
			IPersistentMWPersonOJB mwPersistentPerson, IPersistentGrantOwner persistentGrantOwner,
			IPessoaPersistente pessoaPersistente, IPersistentMWTeacherOJB persistentMWTeacher,
			GrantMigrationLog grantMigrationLog, GrantMigrationStats grantMigrationStats)
			throws ExcepcaoPersistencia, Exception
	{
		PersistenceBroker broker = fenixPersistentSuport.currentBroker();
		IPessoa pessoa = null;
		try
		{
			grantMigrationStats.incrementMwPersonRead();
			grantMigrationLog.writeLogLn("[INFO]  Migrating person: "
					+ mwPerson.getNumeroDocumentoIdentificacao() + "-" + mwPerson.getNome() + ".");
			TipoDocumentoIdentificacao tipoDocMWPerson = new TipoDocumentoIdentificacao(mwPerson
					.getTipoDocumentoIdentificacao());
			pessoa = pessoaPersistente.lerPessoaPorNumDocIdETipoDocId(mwPerson
					.getNumeroDocumentoIdentificacao(), tipoDocMWPerson);
			if (pessoa == null)
			{
				grantMigrationLog.writeLog("\tInserting person: "
						+ mwPerson.getNumeroDocumentoIdentificacao());
				//Insert NEW person
				pessoa = new Pessoa(mwPerson.getNumeroDocumentoIdentificacao(), tipoDocMWPerson,
						mwPerson.getLocalEmissaoDocumentoIdentificacao(), mwPerson
								.getDataEmissaoDocumentoIdentificacao(), mwPerson
								.getDataValidadeDocumentoIdentificacao(), mwPerson.getNome(), new Sexo(
								mwPerson.getSexo()), new EstadoCivil(mwPerson.getEstadoCivil()),
						mwPerson.getNascimento(), mwPerson.getNomePai(), mwPerson.getNomeMae(), mwPerson
								.getNacionalidade(), mwPerson.getFreguesiaNaturalidade(), mwPerson
								.getConcelhoNaturalidade(), mwPerson.getDistritoNaturalidade(), mwPerson
								.getMorada(), mwPerson.getLocalidade(), mwPerson.getCodigoPostal(),
						null, mwPerson.getFreguesiaMorada(), mwPerson.getConcelhoMorada(), mwPerson
								.getDistritoMorada(), mwPerson.getTelefone(), mwPerson.getTelemovel(),
						mwPerson.getEmail(), mwPerson.getEnderecoWeb(), mwPerson.getNumContribuinte(),
						mwPerson.getProfissao(), mwPerson.getUsername(), mwPerson.getPassword(), null,
						mwPerson.getCodigoFiscal());
				pessoaPersistente.simpleLockWrite(pessoa);
				grantMigrationLog.writeLogLn("[OK]");
				grantMigrationStats.incrementMwPersonMigrated();
			}
			else
			{
				grantMigrationLog.writeLogLn("\tPerson already exists! [OK]");
			}
		}
		catch (Exception e)
		{
			grantMigrationStats.incrementMwPersonErrors();
			grantMigrationLog.writeLog("!!!!!!!!!!!![ERROR]: " + e.getMessage());
		}
		try
		{
			IGrantOwner grantOwner = persistentGrantOwner.readGrantOwnerByPerson(mwPerson
					.getIdInternal());
			grantMigrationStats.incrementMwGrantOwnerRead();
			if (grantOwner != null)
			{
				grantMigrationLog.writeLog("\tPerson is a grant owner(" + grantOwner.getNumber()
						+ ") seting new key person.");
				persistentGrantOwner.simpleLockWrite(grantOwner);
				grantOwner.setPerson(pessoa);
				grantMigrationStats.incrementMwGrantOwnerMigrated();
				grantMigrationLog.writeLogLn(" [OK]");
			}
		}
		catch (Exception e)
		{
			grantMigrationStats.incrementMwGrantOwnerErrors();
			grantMigrationLog.writeLog("!!!!!!!!!!!![ERROR]: " + e.getMessage());
		}
		try
		{
			MWTeacher mwTeacher = persistentMWTeacher.readByChavePessoa(mwPerson.getIdInternal());
			grantMigrationStats.incrementMwTeacherRead();
			if (mwTeacher != null)
			{
				grantMigrationLog.writeLog("\tPerson is a teacher(" + mwTeacher.getNumber()
						+ ") seting new key person.");
				MWPerson mwPersonTemp = new MWPerson();
				mwPersonTemp.setIdInternal(pessoa.getIdInternal());
				mwTeacher.setPerson(mwPersonTemp);
				broker.store(mwTeacher);
				grantMigrationStats.incrementMwTeacherMigrated();
				grantMigrationLog.writeLogLn(" [OK]");
			}
		}
		catch (Exception e)
		{
			grantMigrationStats.incrementMwTeacherErrors();
			grantMigrationLog.writeLog("!!!!!!!!!!!![ERROR]: " + e.getMessage());
		}
	}
}
