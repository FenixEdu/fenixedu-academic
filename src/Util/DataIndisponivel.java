package Util;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Implementa um NullObject para representar uma data nao 
 * disponivel
 *
 * @author  Ivo Brandão
 */
public class DataIndisponivel extends java.util.Date  implements Serializable {
    
    /** variaveis de construcao de data invalida */
    private static int ano = 0;
    private static int mes = 0;
    private static int dia = 1;
    
    /** variavel que contem valor de data invalida */
    private static java.util.Date valor = null;
    static {
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(ano + 1900, mes, dia);
    	valor = calendar.getTime();
    }
    
    /** mensagem a imprimir */
    private static String mensagem = new String("Data Indisponível");
    
    /** Creates a new instance of DataIndisponivel */
    public DataIndisponivel() {
        /* invoca construtor da classe mae */
        super();
    }

    /** Identifica esta data como data invalida
     * @return string data indisponivel
     */
    public String toString() {
        return mensagem;
    }
    
    /** Retorna o valor utilizado para definir uma data invalida
     * @return Date(0, 0, 1) (1/1/1900)
     */
    public static java.util.Date getValor(){
        return valor;
    }
    
    public static boolean isDataIndisponivel(java.util.Date dataGenerica){
        boolean resultado = false;
        
        if (dataGenerica.equals(valor)) resultado = true;
        
        return resultado;
    }
    
}
