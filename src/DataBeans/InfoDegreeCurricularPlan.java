/*
 * PlanoCurricular.java
 *
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 *
 * @author  Nuno Nunes & Joana Mota
 */

package DataBeans;


public class InfoDegreeCurricularPlan{
    
    private InfoDegree infoDegree;
    private String name;

        
    public InfoDegreeCurricularPlan() { }
    

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoDegreeCurricularPlan ) {
            InfoDegreeCurricularPlan infoCurricularPlan = (InfoDegreeCurricularPlan)obj;
            result = ( getName().equals(infoCurricularPlan.getName()) && getInfoDegree().equals(infoCurricularPlan.getInfoDegree()));
        }
        return result;
    }
    
  public String toString() {
    String result = "[PLANO_CURRICULAR_CURSO";
    result += ", name=" + name;
    result += ", curso=" + infoDegree;
    result += "]";
    return result;
  }
    
    

	/**
	 * Returns the infoDegree.
	 * @return InfoDegree
	 */
	public InfoDegree getInfoDegree() {
		return infoDegree;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the infoDegree.
	 * @param infoDegree The infoDegree to set
	 */
	public void setInfoDegree(InfoDegree infoDegree) {
		this.infoDegree = infoDegree;
	}

	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
