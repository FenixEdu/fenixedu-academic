/*
 * ICursoExecucao.java
 *
 * Created on 2 de Novembro de 2002, 20:50
 */

package Dominio;

import java.io.Serializable;

/**
 *
 * @author  rpfi
 */
public interface ICursoExecucao extends Serializable{
 
  
  IExecutionYear getExecutionYear();
  void setExecutionYear(IExecutionYear newExecutionYear);
  
  IPlanoCurricularCurso getCurricularPlan();
  void setCurricularPlan (IPlanoCurricularCurso curricularPlan);
}
