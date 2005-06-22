/*
 * Created on 25/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.AreaCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IScientificArea;
import net.sourceforge.fenixedu.domain.OptionalCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 *  
 */
public class CurricularCourseGroupOJB extends PersistentObjectOJB implements
        IPersistentCurricularCourseGroup {

    public CurricularCourseGroupOJB() {
    }

    public List readByBranchAndAreaType(Integer branchId, AreaType areaType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyBranch", branchId);
        criteria.addEqualTo("areaType", areaType);
        criteria.addEqualTo("ojbConcreteClass", AreaCurricularCourseGroup.class.getName());
        return queryList(AreaCurricularCourseGroup.class, criteria);
    }

    public ICurricularCourseGroup readByBranchAndCurricularCourseAndAreaType(Integer branchId,
            Integer curricularCourseId, AreaType areaType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branch.idInternal", branchId);
        criteria.addEqualTo("curricularCourses.idInternal", curricularCourseId);
        criteria.addEqualTo("areaType", areaType);
        criteria.addEqualTo("ojbConcreteClass", AreaCurricularCourseGroup.class.getName());
        return (ICurricularCourseGroup) queryObject(AreaCurricularCourseGroup.class, criteria);
    }

    public ICurricularCourseGroup readByBranchAndScientificAreaAndAreaType(Integer branchId,
            Integer scientificAreaId, AreaType areaType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branch.idInternal", branchId);
        criteria.addEqualTo("scientificAreas.idInternal", scientificAreaId);
        criteria.addEqualTo("areaType", areaType);
        criteria.addEqualTo("ojbConcreteClass", AreaCurricularCourseGroup.class.getName());
        return (ICurricularCourseGroup) queryObject(AreaCurricularCourseGroup.class, criteria);
    }

    public List readAllOptionalCurricularCourseGroupsFromDegreeCurricularPlan(
            Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branch.degreeCurricularPlan.idInternal", degreeCurricularPlanId);
        criteria.addEqualTo("ojbConcreteClass", OptionalCurricularCourseGroup.class.getName());
        return queryList(OptionalCurricularCourseGroup.class, criteria);
    }

    public List readOptionalCurricularCourseGroupsFromArea(Integer areaId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branch.idInternal", areaId);
        criteria.addEqualTo("ojbConcreteClass", OptionalCurricularCourseGroup.class.getName());
        return queryList(OptionalCurricularCourseGroup.class, criteria);
    }

}