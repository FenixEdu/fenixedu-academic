package Dominio;

import java.util.List;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * @author David Santos on Jul 26, 2004
 */

public abstract class CurricularCourseGroup extends DomainObject {

    protected Integer minimumValue;

    protected Integer maximumValue;

    protected Integer keyBranch;

    protected IBranch branch;

    protected List curricularCourses;

    protected List scientificAreas;
    
    protected String ojbConcreteClass;

    public Integer getKeyBranch() {
        return keyBranch;
    }

    public void setKeyBranch(Integer keyBranch) {
        this.keyBranch = keyBranch;
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

    public IBranch getBranch() {
        return branch;
    }

    public void setBranch(IBranch branch) {
        this.branch = branch;
    }

    public List getCurricularCourses() {
        return curricularCourses;
    }

    public void setCurricularCourses(List curricularCourses) {
        this.curricularCourses = curricularCourses;
    }

    public List getScientificAreas() {
        return scientificAreas;
    }

    public void setScientificAreas(List scientificAreas) {
        this.scientificAreas = scientificAreas;
    }

    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    public void setOjbConcreteClass(String ojbConcreteClass) {
        this.ojbConcreteClass = ojbConcreteClass;
    }

//    public boolean equals(Object obj) {
//        boolean result = false;
//        if ((result) && (obj instanceof ICurricularCourseGroup)) {
//            ICurricularCourseGroup curricularCourseGroup = (ICurricularCourseGroup)
// obj;
//            result = curricularCourseGroup.getBranch().equals(this.getBranch())
//                    && curricularCourseGroup.getIdInternal().equals(this.getIdInternal());
//        }
//        return result;
//    }
}