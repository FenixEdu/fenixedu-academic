package Dominio;


/**
 * @author David Santos in Jun 29, 2004
 */

public class CurricularCourseEquivalence extends DomainObject implements ICurricularCourseEquivalence {

    protected Integer oldCurricularCourseKey;

    protected Integer equivalentCurricularCourseKey;

    protected Integer degreeCurricularPlanKey;

    protected ICurricularCourse oldCurricularCourse;

    protected ICurricularCourse equivalentCurricularCourse;

    protected IDegreeCurricularPlan degreeCurricularPlan;

    public CurricularCourseEquivalence() {
    }

    public IDegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }
    
    public Integer getDegreeCurricularPlanKey() {
        return degreeCurricularPlanKey;
    }
    
    public void setDegreeCurricularPlanKey(Integer degreeCurricularPlanKey) {
        this.degreeCurricularPlanKey = degreeCurricularPlanKey;
    }
    
    public ICurricularCourse getEquivalentCurricularCourse() {
        return equivalentCurricularCourse;
    }
    
    public void setEquivalentCurricularCourse(ICurricularCourse equivalentCurricularCourse) {
        this.equivalentCurricularCourse = equivalentCurricularCourse;
    }
    
    public Integer getEquivalentCurricularCourseKey() {
        return equivalentCurricularCourseKey;
    }
    
    public void setEquivalentCurricularCourseKey(Integer equivalentCurricularCourseKey) {
        this.equivalentCurricularCourseKey = equivalentCurricularCourseKey;
    }
    
    public ICurricularCourse getOldCurricularCourse() {
        return oldCurricularCourse;
    }
    
    public void setOldCurricularCourse(ICurricularCourse oldCurricularCourse) {
        this.oldCurricularCourse = oldCurricularCourse;
    }
    
    public Integer getOldCurricularCourseKey() {
        return oldCurricularCourseKey;
    }
    
    public void setOldCurricularCourseKey(Integer oldCurricularCourseKey) {
        this.oldCurricularCourseKey = oldCurricularCourseKey;
    }
    
    

   

}