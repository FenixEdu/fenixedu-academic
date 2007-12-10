package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDVirtualTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;

import org.apache.commons.collections.Predicate;

public class CreateTSDTeacher extends Service {
	public Boolean run(String teacherName, Integer categoryId, Integer requiredHours, Integer tsdId) {
						
		Category category = rootDomainObject.readCategoryByOID(categoryId);
		TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);
		
		if(existsVirtualTeacherWithSameName(tsd.getTSDTeachers(), teacherName)){
			return false;
		}
		
		TSDTeacher tsdTeacher = new TSDVirtualTeacher(category, teacherName, requiredHours);
		tsd.addTSDTeachers(tsdTeacher);
		
		return true;
	}

	private boolean existsVirtualTeacherWithSameName(List<TSDTeacher> tsdTeachers, final String teacherName) {
		return CollectionUtils.exists(tsdTeachers, new Predicate(){
			public boolean evaluate(Object arg) {
				if(arg instanceof TSDVirtualTeacher){
					TSDVirtualTeacher tsdTeacher = (TSDVirtualTeacher) arg;
                	return tsdTeacher.getName().equals(teacherName);
				}
				return false;
            }
		});
	}
}
