package ServidorPersistenteJDBC.Relacional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Dominio.Paises;
import ServidorPersistenteJDBC.IPaisesPersistente;


public class PaisesRelacional implements IPaisesPersistente {

  public Paises lerPaisesCodigoPais(int codigoPais) {
    Paises paises = null;

    try {
      PreparedStatement sql =
	  UtilRelacional.prepararComando("SELECT * FROM ass_PAISES " +
                                         "WHERE codigoPais = ?");

      sql.setInt(1, codigoPais);
      ResultSet resultado = sql.executeQuery();
      if (resultado.next()) {
        paises = new Paises(resultado.getInt(1),
                              resultado.getInt(2),
			      resultado.getString(3),
			      resultado.getString(4),
			      resultado.getString(5));
      }
      sql.close();
    }
    catch(SQLException SQLe) {
      System.out.println("PaisesRelacional.lerPaises" + SQLe.toString());
    }
    finally {
      return paises;
    }
  }

  public Paises lerPaises(int codigoInterno) {
    Paises paises = null;

    try {
      PreparedStatement sql =
	  UtilRelacional.prepararComando("SELECT * FROM ass_PAISES " +
                                         "WHERE codigoInterno = ?");

      sql.setInt(1, codigoInterno);
      ResultSet resultado = sql.executeQuery();
      if (resultado.next()) {
        paises = new Paises(resultado.getInt(1),
                              resultado.getInt(2),
			      resultado.getString(3),
			      resultado.getString(4),
			      resultado.getString(5));
      }
      sql.close();
    }
    catch(SQLException SQLe) {
      System.out.println("PaisesRelacional.lerPaises" + SQLe.toString());
    }
    finally {
      return paises;
    }
  }
  
  public int lerPaises(String paises) {
    int pais = 0;

    try {
      PreparedStatement sql =
	  UtilRelacional.prepararComando("SELECT * FROM ass_PAISES " +
                                         "WHERE nomePais =?");

      sql.setString(1, paises);
      ResultSet resultado = sql.executeQuery();
      if (resultado.next()) 
        pais = resultado.getInt(2);      
      sql.close();
    }
    catch(SQLException SQLe) {
      System.out.println("PaisesRelacional.lerPaises" + SQLe.toString());
    }
    finally {
      return pais;
    }
  }
  
  public boolean escreverPaises(Paises paises) {
    boolean resultado = false;

    try {
      PreparedStatement sql =
	UtilRelacional.prepararComando("INSERT INTO ass_PAISES " +
                                       "VALUES (?, ?, ?, ?, ?)");
      sql.setInt(1, paises.getCodigoInterno());
      sql.setInt(2, paises.getCodigoPais());
      sql.setString(3, paises.getNomePais());
      sql.setString(4, paises.getNacionalidade());
      sql.setString(5, paises.getSigla());

      sql.executeUpdate();
      sql.close();
      resultado = true;
    }
    catch(SQLException SQLe) {
      System.out.println("PaisesRelacional.escreverPaises: " + SQLe.toString());
    }
    finally {
      return resultado;
    }
  }

  public boolean alterarPaises(Paises paises) {
    boolean resultado = false;

    try {
      PreparedStatement sql =
	  UtilRelacional.prepararComando("UPDATE ass_PAISES SET " +
                                         "codigoPais = ?,  " +
                                         "nomePais = ?,  " +
                                         "nacionalidade = ?,  " +
                                         "sigla = ?  " +
                                         "WHERE codigoInterno = ? ");
      sql.setInt(1, paises.getCodigoPais());
      sql.setString(2, paises.getNomePais());
      sql.setString(3, paises.getNacionalidade());
      sql.setString(4, paises.getSigla());
      sql.setInt(5, paises.getCodigoInterno());

      sql.executeUpdate();
      sql.close();
      resultado = true;
    }
    catch(SQLException SQLe) {
      System.out.println("PaisesRelacional.alterarPaises: " + SQLe.toString());
    }
    finally {
      return resultado;
    }
  }

  public boolean apagarPaises(int codigoPais) {
    boolean resultado = false;

    try {
      PreparedStatement sql =
	UtilRelacional.prepararComando("DELETE FROM ass_PAISES " +
				       "WHERE codigoPais = ?");
      sql.setInt(1, codigoPais);
      sql.executeUpdate();
      sql.close();
      resultado = true;
    }
    catch(SQLException SQLe) {
      System.out.println("PaisesRelacional.apagarPaises: " + SQLe.toString());
    }
    finally {
      return resultado;
    }
  }
  
   public ArrayList LerTodosPaises() {
            
    ArrayList paises = null;
    paises = new ArrayList();
    try {
      PreparedStatement sql =
	  UtilRelacional.prepararComando("SELECT * FROM ass_PAISES");

      
      ResultSet resultado = sql.executeQuery();
      while (resultado.next()){
        paises.add(resultado.getString(3));
      }
      sql.close();
    }
    catch(SQLException SQLe) {
      System.out.println("PaisesRelacional.LerTodosPaises" + SQLe.toString());
    }
    finally {
      return paises;
    }
  }
  
   public int lerChavePais(String paises) {
    int pais = 0;

    try {
      PreparedStatement sql =
	  UtilRelacional.prepararComando("SELECT * FROM ass_PAISES " +
                                         "WHERE nomePais = ?");

      sql.setString(1, paises);
      ResultSet resultado = sql.executeQuery();
      if (resultado.next()) {
        pais = resultado.getInt(1);
      }
      sql.close();
    }
    catch(SQLException SQLe) {
      System.out.println("PaisesRelacional.lerChavePais" + SQLe.toString());
    }
    finally {
      return pais;
    }
  }
}