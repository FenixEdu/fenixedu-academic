package ServidorPersistenteFiltroPessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.collections.FastHashMap;
import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.accesslayer.LookupException;

public class DB {
	private static Map pbMap = new FastHashMap();
	private static PBKey pbKey = new PBKey("personFilter");
	
	public DB() {
	}
	
	public static void iniciarTransaccao() throws Exception {
		PersistenceBroker pb = PersistenceBrokerFactory.createPersistenceBroker(pbKey);
		pbMap.put(Thread.currentThread(), pb);
		pb.beginTransaction();		
	}

	public static void confirmarTransaccao() throws Exception {
		PersistenceBroker pb = (PersistenceBroker) pbMap.remove(Thread.currentThread());
		pb.commitTransaction();
		pb.close();
	}

	public static void cancelarTransaccao() throws Exception {
		PersistenceBroker pb = (PersistenceBroker) pbMap.remove(Thread.currentThread());
		pb.abortTransaction();
		pb.close();
	}

	public static PreparedStatement prepararComando(String statement) {
		Connection conn;
	
		PersistenceBroker pb = (PersistenceBroker) pbMap.get(Thread.currentThread());	
		if (pb == null) {
			throw new IllegalStateException("#MAYBE IT IS OLD SHARED CONNECTION CODE!");	
		}
		try {
			conn = pb.serviceConnectionManager().getConnection();
		} catch (LookupException e1) {
			e1.printStackTrace(System.out);
			throw new IllegalStateException(e1.getMessage());
		}
		try {
			if (conn == null || conn.isClosed()){
				throw new IllegalStateException("#Connection closed!");
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
			throw new IllegalStateException(e2.getMessage());
		}
		
		PreparedStatement sql = null;
		try {
			sql = conn.prepareStatement(statement);
			return sql;
		} catch (java.sql.SQLException e) {
			e.printStackTrace(System.out);
			System.out.println("#UtilRelacional: " + e.toString());
			return sql;
		}
	}
}

//Antes
//package ServidorPersistenteFiltroPessoa;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//import middleware.dataClean.personFilter.Parameters;
//
//public class DB {
//		private static Connection _conn;
//
//	public DB() {}
//
//
//	public static synchronized void initConnection() {
//		Parameters par = Parameters.getInstance();
//		loadDriver();
//		try {
//			if (_conn == null )
//				_conn = DriverManager.getConnection(par.get("clean.url"),
//													 par.get("clean.user"), par.get("clean.password"));
//		 // _conn = DriverManager.getConnection("jdbc:odbc:IST2", "sa", "");
//		}
//		catch (Exception e) {
//			System.out.println("Servidor Dados: ERRO: Não consegue ligar à BD: " + e.toString());
//			System.out.println("Verifique ficheiro de configuração");
//			System.exit(1);
//		}
//	}
//
//	public static synchronized void closeConnection() {
//	 try {
//		 _conn.close();
//		 _conn = null;
//	 } catch (Exception e) {
//		 System.out.println("ServidorDados: ERRO: " + e.toString());
//	 }
// }
//
//	private static void loadDriver() {
//		Parameters par = Parameters.getInstance();
//	 try {
//		 System.out.println("ServidorDados: a carregar driver...");
//		 Class.forName(par.get("clean.driver")).newInstance();
//		 //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
//		 System.out.println("ServidorDados: driver carregado");
//	 } catch(Exception e) {
//		 System.out.println("ServidorDados: erro a carregar o driver: " + e.toString());
//	 }
// }
//
//
// public static synchronized void iniciarTransaccao() throws Exception {
//	 try {
//		 initConnection();
//		 _conn.setAutoCommit(true);
//	 } catch (SQLException e) {
//		 System.out.println("ServidorDados: ERRO: " + e.toString());
//		 throw new Exception("Não conseguiu abrir ligação");
//	 }
// }
//
// public static synchronized void confirmarTransaccao() throws Exception {
//	 try {
//		 _conn.commit();
//		 closeConnection();
//	 } catch (SQLException e) {
//		 System.out.println("ServidorDados: ERRO: " + e.toString());
//		 throw new Exception("Não conseguiu confirmar transacção");
//	 }
// }
//
// public static synchronized void cancelarTransaccao() throws Exception {
//	 try {
//		 _conn.rollback();
//		 closeConnection();
//	 } catch(SQLException e) {
//		 System.out.println("ServidorDados: ERRO: " + e.toString());
//		 throw new Exception("Não conseguiu cancelar transacção");
//	 }
// }
//
// static synchronized PreparedStatement prepararComando(String statement) {
//	 PreparedStatement sql = null;
//	 try {
//		 sql = _conn.prepareStatement(statement);
//	 } catch (java.sql.SQLException e) {
//		 System.out.println("ServidorDados: erro a preparar comando: " + e.toString());
//	 } finally {
//		 return sql;
//	 }
// }
//}