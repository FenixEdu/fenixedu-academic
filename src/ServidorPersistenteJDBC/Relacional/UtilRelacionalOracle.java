package ServidorPersistenteJDBC.Relacional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import ServidorPersistenteJDBC.config.IST2002Properties;

/**
 *
 * @author  Nanda & Tânia
 */
public class UtilRelacionalOracle {
  private static String _dataSource;
  private static String _userName;
  private static String _password;
  private static String _urlBD;
  private static Connection _ligacaoPartilhada = null;
  private static Properties _properties = null;
  private static Map mapaLigacoes = new HashMap();

  public static synchronized void inicializarBaseDados(String filename) {
	_properties = new IST2002Properties(filename);
	_dataSource = _properties.getProperty("IST2002.ServidorPersistente.nomeBD");
	_userName = _properties.getProperty("IST2002.ServidorPersistente.usernameBD");
	_password = _properties.getProperty("IST2002.ServidorPersistente.passwordBD");
	_urlBD= _properties.getProperty("IST2002.ServidorPersistente.URLServidorBD") + _dataSource;
	if (_dataSource == null || _userName == null || _password == null) {
	  System.out.println("UtilRelacionalOracle: propriedades indefinidas.");
	}
	loadDriver();
  }

  public static void limparTabelas() throws Exception {
	try {
	  Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
	  Statement comando = ligacao.createStatement();
	  //comando.executeUpdate("DELETE FROM pessoa");
	  comando.close();
	} catch (Exception e) {
	  System.out.println("UtilRelacionalOracle.limparTabelas: " + e);
	  throw e;
	}
  }

  public static int ultimoIdGerado() throws Exception {
	int ultimoIdGerado = 0;
	try {
	  //verificar para oracle
	  PreparedStatement comando = prepararComando("SELECT LAST_INSERT_ID()");
	  ResultSet resultado = comando.executeQuery();
	  if(resultado.next())
		ultimoIdGerado = resultado.getInt(1);
	} catch (Exception e) {
	  System.out.println("UtilRelacionalOracle.ultimoIdGerado: " + e.toString());
	  throw e;
	}
	return ultimoIdGerado;
  }

  public static synchronized void iniciarTransaccao() throws Exception {
	try {
	  iniciarLigacao();
	  Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
	  ligacao.setAutoCommit(false);
	} catch (SQLException e) {
	  System.out.println("UtilRelacionalOracle: " + e.toString());
	  throw new Exception("Não conseguiu abrir ligação");
	}
  }

  public static synchronized void confirmarTransaccao() throws Exception {
	Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
	try {
	  ligacao.commit();
	  fecharLigacao();
	} catch (SQLException e) {
	  System.out.println("UtilRelacionalOracle: " + e.toString());
	  throw new Exception("Não conseguiu confirmar transacção");
	}
  }

  public static synchronized void cancelarTransaccao() throws Exception {
	Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
	try {
	  ligacao.rollback();
	  fecharLigacao();
	} catch(SQLException e) {
	  System.out.println("UtilRelacionalOracle: " + e.toString());
	  throw new Exception("Não conseguiu cancelar transacção");
	}
  }

  public static synchronized PreparedStatement prepararComando(String statement) {
	Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
	// CODIGO A RETIRAR SE TODO O SERVIDOR DOCENTE USAR EXPLICITANENTE TRANSACCOES...
	if (ligacao == null) {
	  if (_ligacaoPartilhada == null)
		try {
	  _ligacaoPartilhada = DriverManager.getConnection(_urlBD, _userName, _password);
		} catch (java.sql.SQLException e) {
	  System.out.println("UtilRelacionalOracle: " + e.toString());
		}
	  ligacao = _ligacaoPartilhada;
	}
	PreparedStatement sql = null;
	try {
	  sql = ligacao.prepareStatement(statement);
	} catch (java.sql.SQLException e) {
	  System.out.println("UtilRelacionalOracle: " + e.toString());
	} finally {
	  return sql;
	}
  }

  private static void loadDriver() {
	try {
	  //driver de oracle
	  Class.forName("com.mysql.jdbc.Driver").newInstance();
	  //Class.forName("org.gjt.mm.mysql.Driver").newInstance();
	} catch(Exception e) {
	  System.out.println("UtilRelacionalOracle: erro a carregar o driver: " + e.toString());
	}
  }

  private static void iniciarLigacao() {
	try {
	  Connection ligacao = DriverManager.getConnection(_urlBD, _userName, _password);
	  mapaLigacoes.put(Thread.currentThread(), ligacao);
	} catch (Exception e){
	  System.out.println("UtilRelacionalOracle: " + e.toString());
	  System.out.println("Não foi possível iniciar a ligação");
	}
  }

  private static void fecharLigacao() {
	Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
	try {
	  ligacao.close();
	  mapaLigacoes.remove(Thread.currentThread());
	} catch (Exception e) {
	  System.out.println("UtilRelacionalOracle: " + e.toString());
	  System.out.println("Não foi possível fechar a ligação");
	}
  }
}