/*
 *
 * Por enquanto não é utilizada!!!
 *
 **/
package ServidorAplicacao.Servico.assiduousness;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import Dominio.MarcacaoPonto;
import ServidorAplicacao.NotExecuteException;
import ServidorAplicacao.ServicoAutorizacao;
import ServidorAplicacao.ServicoSeguro;
import ServidorPersistenteJDBC.ICartaoPersistente;
import ServidorPersistenteJDBC.IMarcacaoPontoPersistente;
import ServidorPersistenteJDBC.SuportePersistente;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroMarcacaoPonto extends ServicoSeguro {  
  public ServicoSeguroMarcacaoPonto(ServicoAutorizacao servicoAutorizacaoLer) {
    super(servicoAutorizacaoLer);
  }
  
  public void execute() throws NotExecuteException {
    File ficheiro = null;
    BufferedReader leitura = null;
    String linhaFicheiro = null;
    Calendar calendarDia = Calendar.getInstance();
    MarcacaoPonto marcacaoPonto = null;
    calendarDia.setLenient(false);
    
    try{
      ficheiro = new File("E:\\projectos\\Assiduidade\\IST\\marcacaoPonto.txt");//directoria no CIIST
      //ficheiro = new File("D:\\IST\\teleponto\\projectos\\assiduidade\\Ist\\marcacaoPonto.txt");//directoria em casa-tania
      //ficheiro = new File("D:\\Nanda\\assiduidade\\IST\\marcacaoPonto.txt");//directoria em casa-nanda
      leitura = new BufferedReader(new FileReader(ficheiro), new Long(ficheiro.length()).intValue());
    } catch (IOException e) {
      throw new NotExecuteException("error.ficheiro.naoEncontrado");
    }
    
    do{
      /* leitura do ficheiro marcacao de ponto linha a linha */
      
      
      try{
        linhaFicheiro = leitura.readLine();
      } catch (IOException e) {
        throw new NotExecuteException("error.ficheiro.impossivelLer");
      }
      
      if(linhaFicheiro != null){
        try{
          calendarDia.set(new Integer(linhaFicheiro.substring(11, 15)).intValue()/*ano*/,
          new Integer(linhaFicheiro.substring(9, 11)).intValue()-1/*mes*/,
          new Integer(linhaFicheiro.substring(7, 9)).intValue()/*dia*/, 
          new Integer(linhaFicheiro.substring(15, 17)).intValue()/*horas*/,
          new Integer(linhaFicheiro.substring(17, 19)).intValue()/*minutos*/,
          new Integer(linhaFicheiro.substring(19, 21)).intValue()/*segundos*/);
          
          marcacaoPonto = new MarcacaoPonto(new Integer(linhaFicheiro.substring(0, 6)).intValue()/*unidade*/, 
          new Timestamp(calendarDia.getTimeInMillis())/*data de marcacao*/,
          new Integer(linhaFicheiro.substring(21, 24)).intValue()/*resto*/,
          new Integer(linhaFicheiro.substring(24, 32)).intValue()/*cartao*/);
          
          ICartaoPersistente iCartaoPersistente = SuportePersistente.getInstance().iCartaoPersistente();
          if(iCartaoPersistente.existeCartao(marcacaoPonto.getNumCartao())){
            if(iCartaoPersistente.lerCartaoPorNumero(marcacaoPonto.getNumCartao(), marcacaoPonto.getData()) == null){
              if(marcacaoPonto.getNumCartao() > 900000){
                marcacaoPonto.setEstado("cartaoSubstitutoInvalido");
              }else {
                marcacaoPonto.setEstado("cartaoFuncionarioInvalido");
              }
            }
          } else{
            marcacaoPonto.setEstado("cartaoDesconhecido");
          }
          
          IMarcacaoPontoPersistente iMarcacaoPontoPersistente = SuportePersistente.getInstance().iMarcacaoPontoPersistente();
          if(!iMarcacaoPontoPersistente.escreverMarcacaoPonto(marcacaoPonto)){
            throw new NotExecuteException("error.marcacaoPonto.naoEscrita");
          }
        } catch (java.lang.NumberFormatException e) {          
          /* marcacoes com erro de leitura */
          /*marcacaoPonto = new MarcacaoPonto(0, null, 0, "erroLeitura", linhaFicheiro);
          
          IMarcacaoPontoPersistente iMarcacaoPontoPersistente = SuportePersistente.getInstance().iMarcacaoPontoPersistente();
          if(!iMarcacaoPontoPersistente.escreverMarcacaoPonto(marcacaoPonto)){
            throw new NotExecuteException("error.marcacaoPonto.naoEscrita");
          }*/
          continue;
        }
      }
    }while(linhaFicheiro != null);
  }
}