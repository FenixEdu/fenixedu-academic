package middleware.dataClean.personFilter.ServidorErros;


public class Erros {
  public static final String SEM_DADOS = "Distrito, Concelho e Freguesia sem Dados";
  public static final String ERRO_FREG = "Freguesia inválida para este concelho";
  public static final String ERRO_CONC = "Concelho inválido para este distrito";
  public static final String ERRO_DIST = "Distrito inválido";
  public static final String ERRO_CF_DIST = "Só tem freguesia: Não é possível inferir distrito";
  public static final String ERRO_CF_CONC = "Só tem freguesia: Não é possível inferir concelho";
  public static final String ERRO_CF_FREG = "Só tem freguesia: Não é possível inferir concelho";

  public Erros() {
  }

}