package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.IAreaCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseGroup;
import net.sourceforge.fenixedu.domain.IScientificArea;
import net.sourceforge.fenixedu.domain.OptionalCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularCourseGroup;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.collections.Predicate;

public class CurricularCourseGroupVO extends VersionedObjectsBase implements IPersistentCurricularCourseGroup{

	
	
	   public List readByBranchAndAreaType(Integer branchId, AreaType areaType) throws ExcepcaoPersistencia {
		   IBranch branch = (IBranch)readByOID(Branch.class, branchId);
		   return branch.getAreaCurricularCourseGroups(areaType);
	    }

	    public ICurricularCourseGroup readByBranchAndCurricularCourseAndAreaType(Integer branchId,
	            Integer curricularCourseId, AreaType areaType) throws ExcepcaoPersistencia {
			
		   IBranch branch = (IBranch)readByOID(Branch.class, branchId);
		   List<ICurricularCourseGroup> curricularCourseGroups = branch.getAreaCurricularCourseGroups(areaType);
		   
		   for (ICurricularCourseGroup curricularCourseGroup : curricularCourseGroups){
			   List<ICurricularCourse> curricularCourses = curricularCourseGroup.getCurricularCourses();
				   
			   for (ICurricularCourse curricularCourse : curricularCourses){
				   if (curricularCourse.getIdInternal().equals(curricularCourseId))
					   return curricularCourseGroup;
			   }
		   }
		   
		   return null;
	    }

	    public ICurricularCourseGroup readByBranchAndScientificAreaAndAreaType(Integer branchId,
	            Integer scientificAreaId, AreaType areaType) throws ExcepcaoPersistencia {
			
			IBranch branch = (IBranch)readByOID(Branch.class, branchId);
			List<IAreaCurricularCourseGroup> curricularCourseGroups = branch.getAreaCurricularCourseGroups(areaType);
			
			for (ICurricularCourseGroup curricularCourseGroup : curricularCourseGroups){
				List<IScientificArea> scientificAreas = curricularCourseGroup.getScientificAreas();
				
				for (IScientificArea scientificArea : scientificAreas){
					if (scientificArea.getIdInternal().equals(scientificAreaId))
						return curricularCourseGroup;
				}		
			}
			return null;
	    }

		
	    public List readAllOptionalCurricularCourseGroupsFromDegreeCurricularPlan(
	            final Integer degreeCurricularPlanId) throws ExcepcaoPersistencia {
			
			Collection<ICurricularCourseGroup> curricularCourseGroups = readAll(OptionalCurricularCourseGroup.class);
			
			return (List)CollectionUtils.select(curricularCourseGroups, new Predicate(){
				public boolean evaluate(Object obj){
					return ((ICurricularCourseGroup)obj).getBranch().getDegreeCurricularPlan().getIdInternal().equals(degreeCurricularPlanId);
				}
			});
			
	    }

		
	    public List readOptionalCurricularCourseGroupsFromArea(final Integer areaId) throws ExcepcaoPersistencia {

			Collection<ICurricularCourseGroup> curricularCourseGroups = readAll(OptionalCurricularCourseGroup.class);
			
			return (List)CollectionUtils.select(curricularCourseGroups, new Predicate(){
				public boolean evaluate(Object obj){
					return ((ICurricularCourseGroup)obj).getBranch().getIdInternal().equals(areaId);
				}
			});
	    }
}
