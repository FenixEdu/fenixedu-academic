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

public class UtilRelacional {
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
		_urlBD = _properties.getProperty("IST2002.ServidorPersistente.URLServidorBD") + _dataSource;
		if (_dataSource == null || _userName == null || _password == null) {
			System.out.println("ServidorPersistente: propriedades indefinidas.");
		}
		loadDriver();
	}

	public static void limparTabelas() throws Exception {
		try {
			Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
			Statement comando = ligacao.createStatement();
			comando.executeUpdate("DELETE FROM aluno");
			comando.executeUpdate("DELETE FROM aula");
			//comando.executeUpdate("DELETE FROM cargo");
			//comando.executeUpdate("DELETE FROM cargo_url");
			comando.executeUpdate("DELETE FROM disciplina");
			comando.executeUpdate("DELETE FROM disciplina_curricular_aluno");
			comando.executeUpdate("DELETE FROM disciplina_curricular");
			comando.executeUpdate("DELETE FROM disciplina_execucao");
			comando.executeUpdate("DELETE FROM licenciatura");
			comando.executeUpdate("DELETE FROM licenciatura_execucao");
			comando.executeUpdate("DELETE FROM pessoa");
			comando.executeUpdate("DELETE FROM pessoa_cargo");
			//comando.executeUpdate("DELETE FROM url");
			comando.executeUpdate("DELETE FROM turma");
			comando.executeUpdate("DELETE FROM funcionario");
			comando.executeUpdate("DELETE FROM func_nao_docente");
			comando.executeUpdate("DELETE FROM regime");
			comando.close();
		} catch (Exception e) {
			System.out.println("UtilRelacional.limparTabelas: " + e);
			throw e;
		}
	}

	public static int ultimoIdGerado() throws Exception {
		int ultimoIdGerado = 0;
		try {
			PreparedStatement comando = prepararComando("SELECT LAST_INSERT_ID()");
			ResultSet resultado = comando.executeQuery();
			if (resultado.next())
				ultimoIdGerado = resultado.getInt(1);
		} catch (Exception e) {
			System.out.println("UtilRelacional.ultimoIdGerado: " + e.toString());
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
			System.out.println("ServidorPersistente: " + e.toString());
			System.out.println("Não conseguiu iniciar transacção");
			throw new Exception("Não conseguiu iniciar transacção");
		}
	}

	public static synchronized void confirmarTransaccao() throws Exception {
		Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
		try {
			ligacao.commit();
			fecharLigacao();
		} catch (SQLException e) {
			System.out.println("ServidorPersistente: " + e.toString());
			System.out.println("Não conseguiu confirmar transacção");
			throw new Exception("Não conseguiu confirmar transacção");
		}
	}

	public static synchronized void cancelarTransaccao() throws Exception {
		Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
		try {
			ligacao.rollback();
			fecharLigacao();
		} catch (SQLException e) {
			System.out.println("ServidorPersistente: " + e.toString());
			System.out.println("Não conseguiu cancelar transacção");
			throw new Exception("Não conseguiu cancelar transacção");
		}
	}

	public static synchronized PreparedStatement prepararComando(String statement) {
		Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
		if (ligacao == null) {
			if (_ligacaoPartilhada == null)
				try {
					_ligacaoPartilhada = DriverManager.getConnection(_urlBD, _userName, _password);
				} catch (java.sql.SQLException e) {
					System.out.println("ServidorPersistente: " + e.toString());
					System.out.println("Erro no prepararComando1");
				}
			ligacao = _ligacaoPartilhada;
		}

		try {
			String validationQuery = "SELECT 1";
			Statement stmt = ligacao.createStatement();
			stmt.executeQuery(validationQuery);
		} catch (Exception e) {
			try {
				_ligacaoPartilhada = DriverManager.getConnection(_urlBD, _userName, _password);
			} catch (Exception exc) {
				throw new RuntimeException("Não foi possível efectuar a ligação", exc);
				//throw new IllegalStateException("Não foi possível efectuar a ligação" + exc);
			}
			ligacao = _ligacaoPartilhada;
		}

		PreparedStatement sql = null;
		try {
			sql = ligacao.prepareStatement(statement);
		} catch (java.sql.SQLException e) {
			System.out.println("ServidorPersistente: " + e.toString());
		} finally {
			return sql;
		}
	}

	private static void loadDriver() {
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance();
		} catch (Exception e) {
			System.out.println("ServidorPersistente: erro a carregar o driver: " + e.toString());
		}
	}

	private static void iniciarLigacao() {
		try {
			Connection ligacao = DriverManager.getConnection(_urlBD, _userName, _password);
			mapaLigacoes.put(Thread.currentThread(), ligacao);
		} catch (Exception e) {
			System.out.println("ServidorPersistente: " + e.toString());
			System.out.println("Não foi possível iniciar a ligação");
		}
	}

	private static void fecharLigacao() {
		Connection ligacao = (Connection) mapaLigacoes.get(Thread.currentThread());
		try {
			ligacao.close();
			mapaLigacoes.remove(Thread.currentThread());
		} catch (Exception e) {
			System.out.println("ServidorPersistente: " + e.toString());
			System.out.println("Não foi possível fechar a ligação");
		}
	}

}