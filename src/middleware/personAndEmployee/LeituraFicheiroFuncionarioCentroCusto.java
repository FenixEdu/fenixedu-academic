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

  private Collection ordem;
  private ArrayList lista;
  private File ficheiro;
  private BufferedReader leitura;

  public LeituraFicheiroFuncionarioCentroCusto() {}

  public Collection lerFicheiro(String ficheiroValidas, String delimitador, 
    Hashtable estrutura, Collection ordem) throws NotExecuteException{
            

    this.ordem = ordem;
    this.lista = new ArrayList();
    this.ficheiro = null;
    this.leitura = null;
        
    String linhaFicheiro = null;
    Hashtable instancia = null; 
    
    try{
      ficheiro = new File(ficheiroValidas);
      leitura = new BufferedReader(new FileReader(ficheiro), new Long(ficheiro.length()).intValue());
    } catch (IOException e) {
      throw new NotExecuteException("error.ficheiro.naoEncontrado");
    }
    
    // first line with header
    try
    {
    	linhaFicheiro = leitura.readLine();
    }
    catch (IOException e)
    {
    	throw new NotExecuteException("error.ficheiro.impossivelLer");
    }
    
    do{
      try{
        linhaFicheiro = leitura.readLine();
      } catch (IOException e) {
        throw new NotExecuteException("error.ficheiro.impossivelLer");
      }
      
      if((linhaFicheiro != null) && (linhaFicheiro.length()>10)){
        //read file line by line and  buil a instance and add it to the list
        instancia = new Hashtable();
        
        instancia = recuperarInstancia(linhaFicheiro, delimitador);
        
        lista.add(instancia);
      }
    } while((linhaFicheiro != null));
        
    try {			    	
    	leitura.close();
    } catch (Exception e) {
    	throw new NotExecuteException("error.ficheiro.impossivelFechar");
    }
    
    try {			
    	ficheiro.delete();			
    } catch (Exception e) {
    	throw new NotExecuteException("error.ficheiro.impossivelApagar");
    }
    
    return lista;
  }
  
  private Hashtable recuperarInstancia(String linha, String delimitador){
    StringTokenizer stringTokenizer = new StringTokenizer(linha, delimitador);      
    Hashtable instancia = new Hashtable();          
   
    Iterator iterador = ordem.iterator();
    while (iterador.hasNext()){
        instancia.put(iterador.next(), stringTokenizer.nextToken().trim());
    }
    
    return instancia;
  }
}