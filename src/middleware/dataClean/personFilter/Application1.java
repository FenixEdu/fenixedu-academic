package middleware.dataClean.personFilter;

import java.util.ArrayList;

import middleware.dataClean.personFilter.ServidorErros.LogErros;

import ServidorPersistenteFiltroPessoa.DB;
import ServidorPersistenteFiltroPessoa.ServidorAbrv;
import ServidorPersistenteFiltroPessoa.ServidorMoradas;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Application1 {
  boolean packFrame = false;

  //Construct the application
  public Application1() {
    DB.initConnection();
    ServidorAbrv sa = new ServidorAbrv();
    ServidorMoradas sm = new ServidorMoradas();

    ArrayList todas = sa.lerTodasMoradas();
    Morada mor;
    sm.CriaTabela("_olaTeste");
    int i, maxi;
    maxi = todas.size();
    LimpaOutput res;
    LimpaNaturalidades ln = new LimpaNaturalidades();
    System.out.println("Vou carregar: " + maxi);
    for( i = 0; i < maxi; i++) {
      mor = (Morada) todas.get(i);
      res = ln.limpaNats(mor.getChave(), mor.getNacionalidade(), mor.getDistrito(), mor.getConcelho(),
                         mor.getFreguesia());
      sm.AdicionaMorada(mor.getChave(), res.getPais(), res.getDistrito(),
                        res.getConcelho(), res.getFreguesia(), "_olaTeste");
    }
  }

  //Main method
  public static void main(String[] args) {
    LogErros.limpaErros();
    new Application1();
  }
}