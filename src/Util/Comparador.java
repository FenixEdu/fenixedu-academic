package Util;

import java.io.Serializable;
import java.util.Comparator;

import Dominio.Horario;
import Dominio.Justificacao;
import Dominio.MarcacaoPonto;

/**
 *
 * @author  Fernanda Quitério & Tania Pousão
 */
public class Comparador implements Comparator, Serializable {
  private String _objecto = null;
  private String _sentido = null; /* crescente ou decrescente */
  
  /** Creates a new instance of Comparador */
  public Comparador(String objecto, String sentido) {
    _objecto = objecto;
    _sentido = sentido;
  }
  
  /** Creates a new instance of Comparador */
  public Comparador(String objecto) {
    _objecto = objecto;
  }
  
  /** Compares its two arguments for order.  Returns a negative integer,
   * zero, or a positive integer as the first argument is less than, equal
   * to, or greater than the second.<p>
   *
   * The implementor must ensure that <tt>sgn(compare(x, y)) ==
   * -sgn(compare(y, x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
   * implies that <tt>compare(x, y)</tt> must throw an exception if and only
   * if <tt>compare(y, x)</tt> throws an exception.)<p>
   *
   * The implementor must also ensure that the relation is transitive:
   * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
   * <tt>compare(x, z)&gt;0</tt>.<p>
   *
   * Finally, the implementer must ensure that <tt>compare(x, y)==0</tt>
   * implies that <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
   * <tt>z</tt>.<p>
   *
   * It is generally the case, but <i>not</i> strictly required that
   * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
   * any comparator that violates this condition should clearly indicate
   * this fact.  The recommended language is "Note: this comparator
   * imposes orderings that are inconsistent with equals."
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the
   * 	       first argument is less than, equal to, or greater than the
   * 	       second.
   * @throws ClassCastException if the arguments' types prevent them from
   * 	       being compared by this Comparator.
   *
   */
  public int compare(Object o1, Object o2) {
    int resultado = 0;
    
    if(_objecto.equals("Justificacao")){
      Justificacao justificacao1 = (Justificacao) o1;
      Justificacao justificacao2 = (Justificacao) o2;
      
      if(justificacao1.getDiaInicio() != null && justificacao2.getDiaInicio() != null){
        resultado = justificacao1.getDiaInicio().compareTo(justificacao2.getDiaInicio());
      } else if(justificacao1.getDiaInicio() == null && justificacao2.getDiaInicio() != null){
        resultado = -1; //menor
      } else if (justificacao1.getDiaInicio() != null && justificacao2.getDiaInicio() == null){
        resultado = 1; //maior
      }
    } else if(_objecto.equals("MarcacaoPonto")){
      MarcacaoPonto marcacaoPonto1 = (MarcacaoPonto) o1;
      MarcacaoPonto marcacaoPonto2 = (MarcacaoPonto) o2;
      
      if(marcacaoPonto1.getData() != null && marcacaoPonto2.getData() != null){
        resultado = marcacaoPonto1.getData().compareTo(marcacaoPonto2.getData());
      } else if(marcacaoPonto1.getData() == null && marcacaoPonto2.getData() != null){
        resultado = -1; //menor
      } else if (marcacaoPonto1.getData() != null && marcacaoPonto2.getData() == null){
        resultado = 1; //maior
      }
    } else if(_objecto.equals("HorarioRotativo")){
      Horario horario1 = (Horario)o1;
      Horario horario2 = (Horario)o2;
      
      if((horario1.getPosicao() != 0) && (horario2.getPosicao() != 0)){
        if(horario1.getPosicao() > horario2.getPosicao()){
          return 1;
        } else if(horario1.getPosicao() == horario2.getPosicao()){
          return 0;
        } else if(horario1.getPosicao() < horario2.getPosicao()){
          return -1;
        }
      }
    }
    
    if(_sentido.equals("decrescente")){
      if(resultado == -1){
        resultado = 1;
      }else if(resultado == 1){
        resultado = -1;
      }
    }
    return resultado;
  }  
}
