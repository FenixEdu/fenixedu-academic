package ServidorPersistenteFiltroPessoa;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServidorMoradas {

  public ServidorMoradas() {
  }

  public void CriaTabela( String nomeTabela ) {
  try {
    PreparedStatement sql =
        DB.prepararComando("CREATE TABLE " +nomeTabela+" (" +
                           "chave INT NOT NULL, " +
                           "nacionalidade INT NOT NULL, " +
                           "distrito INT NOT NULL, " +
                           "concelho INT NOT NULL, " +
                           "freguesia INT NOT NULL)");


      sql.executeUpdate();
      sql.close();
    }
    catch (Exception e) {
      System.out.println("Erro a criar a tabela: "+nomeTabela+" > " +e.toString());
    }

  }

  public void AdicionaMorada(int chave, int pais, int distr, int conc,
                                int freg, String tabela) {
        try {
          PreparedStatement sql =
              DB.prepararComando("INSERT INTO " +tabela+ " VALUES (" +
                                     " ? ," +
                                     " ? ," +
                                     " ? ," +
                                     " ? ," +
                                     " ? )");
          sql.setInt(1, chave);
          sql.setInt(2, pais);
          sql.setInt(3, distr);
          sql.setInt(4, conc);
          sql.setInt(5, freg);
          sql.executeUpdate();
          sql.close();
        }
        catch(SQLException SQLe) {
         System.out.println("Warning" + SQLe.toString());
         System.out.println("Retry...");
         AdicionaMorada(chave, pais, distr, conc, freg, tabela);
       }
     }


}