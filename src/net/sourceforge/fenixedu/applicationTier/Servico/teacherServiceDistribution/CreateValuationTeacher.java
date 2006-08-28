package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;

import org.apache.commons.collections.Predicate;

public class CreateValuationTeacher extends Service {
	public Boolean run(String teacherName, Integer categoryId, Integer requiredHours, Integer valuationGroupingId) {
						
		Category category = rootDomainObject.readCategoryByOID(categoryId);
		ValuationGrouping valuationGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		
		if(existsGhostTeacherWithSameName(valuationGrouping.getValuationTeachers(), teacherName)){
			return false;
		}
		
		ValuationTeacher valuationTeacher = new ValuationTeacher(category, teacherName, requiredHours);
		valuationGrouping.addValuationTeachers(valuationTeacher);
		
		return true;
	}

	private boolean existsGhostTeacherWithSameName(List<ValuationTeacher> valuationTeachers, final String teacherName) {
		return CollectionUtils.exists(valuationTeachers, new Predicate(){
			public boolean evaluate(Object arg) {
				ValuationTeacher valuationTeacher = (ValuationTeacher) arg;
                return !valuationTeacher.isRealTeacher() && valuationTeacher.getName().equals(teacherName);
            }
		});
	}
}
