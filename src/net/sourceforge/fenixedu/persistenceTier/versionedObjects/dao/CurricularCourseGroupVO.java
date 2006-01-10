package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.AreaCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.OptionalCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ScientificArea;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.Predicate;

public class CurricularCourseGroupVO extends VersionedObjectsBase implements IPersistentCurricularCourseGroup{

	
	
	   public List readByBranchAndAreaType(Integer branchId, AreaType areaType) throws ExcepcaoPersistencia {
		   Branch branch = (Branch)readByOID(Branch.class, branchId);
		   return branch.getAreaCurricularCourseGroups(areaType);
	    }

	    public CurricularCourseGroup readByBranchAndCurricularCourseAndAreaType(Integer branchId,
	            Integer curricularCourseId, AreaType areaType) throws ExcepcaoPersistencia {
			
		   Branch branch = (Branch)readByOID(Branch.class, branchId);
		   List<CurricularCourseGroup> curricularCourseGroups = branch.getAreaCurricularCourseGroups(areaType);
		   
		   for (CurricularCourseGroup curricularCourseGroup : curricularCourseGroups){
			   List<CurricularCourse> curricularCourses = curricularCourseGroup.getCurricularCourses();
				   
			   for (CurricularCourse curricularCourse : curricularCourses){
				   if (curricularCourse.getIdInternal().equals(curricularCourseId))
					   return curricularCourseGroup;
			   }
		   }
		   
		   return null;
	    }

	    public CurricularCourseGroup readByBranchAndScientificAreaAndAreaType(Integer branchId,
	            Integer scientificAreaId, AreaType areaType) throws ExcepcaoPersistencia {
			
			Branch branch = (Branch)readByOID(Branch.class, branchId);
			List<AreaCurricularCourseGroup> curricularCourseGroups = branch.getAreaCurricularCourseGroups(areaType);
			
			for (CurricularCourseGroup curricularCourseGroup : curricularCourseGroups){
				List<ScientificArea> scientificAreas = curricularCourseGroup.getScientificAreas();
				
				for (ScientificArea scientificArea : scientificAreas){
					if (scientificArea.getIdInternal().equals(scientificAreaId))
						return curricularCourseGroup;
				}		
			}
			return null;
	    }

		
	    public List readAllOptionalCurricularCourseGroupsFromDegreeCurricularPlan(
	            final Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
			
			Collection<CurricularCourseGroup> curricularCourseGroups = readAll(OptionalCurricularCourseGroup.class);
			
			return (List)CollectionUtils.select(curricularCourseGroups, new Predicate(){
				public boolean evaluate(Object obj){
					return ((CurricularCourseGroup)obj).getBranch().getDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanId);
				}
			});
			
	    }

		
	    public List readOptionalCurricularCourseGroupsFromArea(final Integer areaId) throws ExcepcaoPersistencia {

			Collection<CurricularCourseGroup> curricularCourseGroups = readAll(OptionalCurricularCourseGroup.class);
			
			return (List)CollectionUtils.select(curricularCourseGroups, new Predicate(){
				public boolean evaluate(Object obj){
					return ((CurricularCourseGroup)obj).getBranch().getIdInternal().equals(areaId);
				}
			});
	    }
}
