/*
 * Created on 26/Nov/2003
 *  
 */
package DataBeans;

import java.util.List;

import Dominio.AreaCurricularCourseGroup;
import Dominio.ICurricularCourseGroup;
import Util.AreaType;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public abstract class InfoCurricularCourseGroup extends InfoObject {

    protected Integer minimumValue;

    protected Integer maximumValue;

    protected List curricularCourses;

    protected List scientificAreas;

    protected String name;

    protected AreaType areaType;

    protected InfoBranch infoBranch;

    protected List curricularCourseScopes;

    public InfoCurricularCourseGroup() {
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoCurricularCourseGroup) {
            InfoCurricularCourseGroup infoCurricularCourseGroup = (InfoCurricularCourseGroup) obj;
            resultado = (((getInfoBranch() == null && infoCurricularCourseGroup.getInfoBranch() == null) || (getInfoBranch() != null
                    && infoCurricularCourseGroup.getInfoBranch() != null && getInfoBranch().equals(
                    infoCurricularCourseGroup.getInfoBranch())))
                    && ((getAreaType() == null && infoCurricularCourseGroup.getAreaType() == null) || (getAreaType() != null
                            && infoCurricularCourseGroup.getAreaType() != null && getAreaType().equals(
                            infoCurricularCourseGroup.getAreaType()))) && ((getCurricularCourseScopes() == null && infoCurricularCourseGroup
                    .getCurricularCourseScopes() == null) || (getCurricularCourseScopes() != null
                    && infoCurricularCourseGroup.getCurricularCourseScopes() != null && getCurricularCourseScopes()
                    .equals(infoCurricularCourseGroup.getCurricularCourseScopes()))));
        }

        return resultado;
    }

    public InfoBranch getInfoBranch() {
        return infoBranch;
    }

    /**
     * @param branch
     */
    public void setInfoBranch(InfoBranch infoBranch) {
        this.infoBranch = infoBranch;
    }

    /**
     * @return
     */
    public List getCurricularCourseScopes() {
        return curricularCourseScopes;
    }

    /**
     * @param curricularCourseScopes
     */
    public void setCurricularCourseScopes(List curricularCourseScopes) {
        this.curricularCourseScopes = curricularCourseScopes;
    }

    /**
     * @return
     */
    public AreaType getAreaType() {
        return areaType;
    }

    /**
     * @param areaType
     */
    public void setAreaType(AreaType areaType) {
        this.areaType = areaType;
    }

    protected Integer getMaximumValue() {
        return maximumValue;
    }

    protected void setMaximumValue(Integer maximumValue) {
        this.maximumValue = maximumValue;
    }

    protected Integer getMinimumValue() {
        return minimumValue;
    }

    protected void setMinimumValue(Integer minimumValue) {
        this.minimumValue = minimumValue;
    }

    /**
     * @return Returns the curricularCourses.
     */
    protected List getCurricularCourses() {
        return curricularCourses;
    }

    /**
     * @param curricularCourses
     *            The curricularCourses to set.
     */
    protected void setCurricularCourses(List curricularCourses) {
        this.curricularCourses = curricularCourses;
    }

    /**
     * @return Returns the name.
     */
    protected String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    protected void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the scientificAreas.
     */
    protected List getScientificAreas() {
        return scientificAreas;
    }

    /**
     * @param scientificAreas
     *            The scientificAreas to set.
     */
    protected void setScientificAreas(List scientificAreas) {
        this.scientificAreas = scientificAreas;
    }

    public void copyFromDomain(ICurricularCourseGroup curricularCourseGroup) {
        super.copyFromDomain(curricularCourseGroup);
        if (curricularCourseGroup != null) {
            setAreaType(curricularCourseGroup.getAreaType());
            setName(curricularCourseGroup.getName());
        }
    }

    public static InfoCurricularCourseGroup newInfoFromDomain(
            ICurricularCourseGroup curricularCourseGroup) {

        if (curricularCourseGroup != null) {
            if (curricularCourseGroup instanceof AreaCurricularCourseGroup) {
                return InfoAreaCurricularCourseGroup.newInfoFromDomain(curricularCourseGroup);
            }
            return InfoOptionalCurricularCourseGroup.newInfoFromDomain(curricularCourseGroup);

        }
        return null;
    }

}