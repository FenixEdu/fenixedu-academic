package middleware.middlewareDomain;

public class MWTiposEquivalenciaIleec  
{

  private String descricao;
  private Integer tipoEquivalencia;



  public String getDescricao()
  {
     return this.descricao;
  }
  public void setDescricao(String param)
  {
    this.descricao = param;
  }


  public Integer getTipoEquivalencia()
  {
     return this.tipoEquivalencia;
  }
  public void setTipoEquivalencia(Integer param)
  {
    this.tipoEquivalencia = param;
  }


  public String toString(){
    return  " [descricao] " + descricao + " [tipoEquivalencia] " + tipoEquivalencia;
  }
  
  public boolean equals(Object obj)
  {
	  boolean resultado = false;
	  if (obj instanceof MWTiposEquivalenciaIleec)
	  {
		MWTiposEquivalenciaIleec mwepi = (MWTiposEquivalenciaIleec) obj;
		  resultado =
			  getTipoEquivalencia().equals(mwepi.getTipoEquivalencia())
				  && getDescricao().equals(mwepi.getDescricao());
		  }
	  return resultado;
  }
 
}
