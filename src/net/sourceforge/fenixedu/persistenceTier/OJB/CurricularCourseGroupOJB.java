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

    public List readByBranchAndAreaType(IBranch branch, AreaType areaType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyBranch", branch.getIdInternal());
        criteria.addEqualTo("areaType", areaType);
        return queryList(AreaCurricularCourseGroup.class, criteria);
    }

    public List readByBranch(IBranch branch) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyBranch", branch.getIdInternal());
        return queryList(CurricularCourseGroup.class, criteria);
    }

    public ICurricularCourseGroup readByBranchAndCurricularCourseAndAreaType(IBranch branch,
            ICurricularCourse curricularCourse, AreaType areaType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branch.idInternal", branch.getIdInternal());
        criteria.addEqualTo("curricularCourses.idInternal", curricularCourse.getIdInternal());
        criteria.addEqualTo("areaType", areaType);
        return (ICurricularCourseGroup) queryObject(AreaCurricularCourseGroup.class, criteria);
    }

    public ICurricularCourseGroup readByBranchAndScientificAreaAndAreaType(IBranch branch,
            IScientificArea scientificArea, AreaType areaType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branch.idInternal", branch.getIdInternal());
        criteria.addEqualTo("scientificAreas.idInternal", scientificArea.getIdInternal());
        criteria.addEqualTo("areaType", areaType);
        return (ICurricularCourseGroup) queryObject(AreaCurricularCourseGroup.class, criteria);
    }

    public List readAllOptionalCurricularCourseGroupsFromDegreeCurricularPlan(
            IDegreeCurricularPlan degreeCurricularPlan) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branch.degreeCurricularPlan.idInternal", degreeCurricularPlan
                .getIdInternal());
        return queryList(OptionalCurricularCourseGroup.class, criteria);
    }

    public List readOptionalCurricularCourseGroupsFromArea(IBranch area) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("branch.idInternal", area.getIdInternal());
        return queryList(OptionalCurricularCourseGroup.class, criteria);
    }

}