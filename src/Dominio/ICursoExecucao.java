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
  IDegreeCurricularPlan getCurricularPlan();
  ITeacher getCoordinator();
  
  void setCurricularPlan (IDegreeCurricularPlan curricularPlan);
  void setExecutionYear(IExecutionYear newExecutionYear);
  void setCoordinator(ITeacher coordinator);
  
}
