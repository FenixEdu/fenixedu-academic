package middleware.personAndEmployee;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import ServidorAplicacao.Servico.exceptions.NotExecuteException;

/**
 * @author Fernanda Quitério & Tania Pousão
 */
public class LeituraFicheiroFuncionarioCentroCusto {

  /* sera' que posso ter aqui um Map? */
//  private Hashtable estrutura;
  private Collection ordem;
  private ArrayList lista;
  private File ficheiro;
  private BufferedReader leitura;
//  private FileWriter escrita;
  
  /** construtor por defeito */
  public LeituraFicheiroFuncionarioCentroCusto() {}

  /** retorna uma Collection (ArrayList) */
  /* a ideia e' ter uma ArrayList com Hashtables representando a estrutura necessaria */
  /* os valores para cada chave da Hashtable sao Strings */
  public Collection lerFicheiro(String ficheiroValidas, String delimitador, 
    Hashtable estrutura, Collection ordem) throws NotExecuteException{
            
//    this.estrutura = estrutura;
    this.ordem = ordem;
    this.lista = new ArrayList();
    this.ficheiro = null;
    this.leitura = null;
//    this.escrita = null;
    
    String linhaFicheiro = null;
    Hashtable instancia = null; 
    
    try{
      /* ficheiro com dados de funcionario validos */
      ficheiro = new File(ficheiroValidas);
      leitura = new BufferedReader(new FileReader(ficheiro), new Long(ficheiro.length()).intValue());
    } catch (IOException e) {
      throw new NotExecuteException("error.ficheiro.naoEncontrado");
    }
        
    /* primeira linha contem os cabecalhos */
    try{
        linhaFicheiro = leitura.readLine();
    } catch (IOException e) {
        throw new NotExecuteException("error.ficheiro.impossivelLer");
    }
    
    do{
      /* leitura do ficheiro com dados de funcionario linha a linha */
      try{
        linhaFicheiro = leitura.readLine();
      } catch (IOException e) {
        throw new NotExecuteException("error.ficheiro.impossivelLer");
      }
      
      if((linhaFicheiro != null) && (linhaFicheiro.length()>10)){
        /* aqui construir uma instancia de pessoa e adiciona-la a lista*/  
        instancia = new Hashtable();
        
        instancia = recuperarInstancia(linhaFicheiro, delimitador);
        
        lista.add(instancia);
      }
    } while((linhaFicheiro != null));
        
    return lista;
  }
  
  /** recuperar os atributos para construir a instancia */
  private Hashtable recuperarInstancia(String linha, String delimitador){
    StringTokenizer stringTokenizer = new StringTokenizer(linha, delimitador);      
    Hashtable instancia = new Hashtable();          
    
    
    /* codigo de parsing dos atributos */
    Iterator iterador = ordem.iterator();
    while (iterador.hasNext()){
        instancia.put(iterador.next(), stringTokenizer.nextToken().trim());
    }
    
    //teste a instancia lida
    System.out.println("Valores lidos para instancia");
    System.out.println(instancia.toString());
    
    return instancia;
  }
}