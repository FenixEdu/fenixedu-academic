package ServidorPersistenteFiltroPessoa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import middleware.dataClean.personFilter.Parameters;

public class DB {
    private static Connection _conn;


  public DB() {}


  public static synchronized void initConnection() {
    Parameters par = Parameters.getInstance();
    loadDriver();
    try {
      if (_conn == null )
        _conn = DriverManager.getConnection(par.get("clean.url"),
                           par.get("clean.user"), par.get("clean.password"));
     // _conn = DriverManager.getConnection("jdbc:odbc:IST2", "sa", "");
    }
    catch (Exception e) {
      System.out.println("Servidor Dados: ERRO: Não consegue ligar à BD: " + e.toString());
      System.out.println("Verifique ficheiro de configuração");
      System.exit(1);
    }
  }

  public static synchronized void closeConnection() {
   try {
     _conn.close();
     _conn = null;
   } catch (Exception e) {
     System.out.println("ServidorDados: ERRO: " + e.toString());
   }
 }

  private static void loadDriver() {
    Parameters par = Parameters.getInstance();
   try {
     System.out.println("ServidorDados: a carregar driver...");
     Class.forName(par.get("clean.driver")).newInstance();
     //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
     System.out.println("ServidorDados: driver carregado");
   } catch(Exception e) {
     System.out.println("ServidorDados: erro a carregar o driver: " + e.toString());
   }
 }


 public static synchronized void iniciarTransaccao() throws Exception {
   try {
     initConnection();
     _conn.setAutoCommit(true);
   } catch (SQLException e) {
     System.out.println("ServidorDados: ERRO: " + e.toString());
     throw new Exception("Não conseguiu abrir ligação");
   }
 }

 public static synchronized void confirmarTransaccao() throws Exception {
   try {
     _conn.commit();
     closeConnection();
   } catch (SQLException e) {
     System.out.println("ServidorDados: ERRO: " + e.toString());
     throw new Exception("Não conseguiu confirmar transacção");
   }
 }

 public static synchronized void cancelarTransaccao() throws Exception {
   try {
     _conn.rollback();
     closeConnection();
   } catch(SQLException e) {
     System.out.println("ServidorDados: ERRO: " + e.toString());
     throw new Exception("Não conseguiu cancelar transacção");
   }
 }

 static synchronized PreparedStatement prepararComando(String statement) {
   PreparedStatement sql = null;
   try {
     sql = _conn.prepareStatement(statement);
   } catch (java.sql.SQLException e) {
     System.out.println("ServidorDados: erro a preparar comando: " + e.toString());
   } finally {
     return sql;
   }
 }
}