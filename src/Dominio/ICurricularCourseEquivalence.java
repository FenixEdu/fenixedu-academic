package Dominio;

/**
 * @author David Santos in Jun 29, 2004
 */

public interface ICurricularCourseEquivalence extends IDomainObject {

    public IDegreeCurricularPlan getDegreeCurricularPlan();

    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan);
    
    public Integer getDegreeCurricularPlanKey();
    
    public void setDegreeCurricularPlanKey(Integer degreeCurricularPlanKey);
    
    public ICurricularCourse getEquivalentCurricularCourse();
    
    public void setEquivalentCurricularCourse(ICurricularCourse equivalentCurricularCourse);
    
    public Integer getEquivalentCurricularCourseKey();
    
    public void setEquivalentCurricularCourseKey(Integer equivalentCurricularCourseKey);
    
    public ICurricularCourse getOldCurricularCourse();
    
    public void setOldCurricularCourse(ICurricularCourse oldCurricularCourse);
}