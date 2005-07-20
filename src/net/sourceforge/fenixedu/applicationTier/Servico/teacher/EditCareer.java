/*
 * Created on 13/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.framework.EditDomainObjectService;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoProfessionalCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoTeachingCareer;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.ICategory;
import net.sourceforge.fenixedu.domain.teacher.IProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.ITeachingCareer;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCareer;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentCategory;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class EditCareer extends EditDomainObjectService {

    protected IPersistentObject getIPersistentObject(ISuportePersistente sp) {
        IPersistentCareer persistentCareer = sp.getIPersistentCareer();
        return persistentCareer;
    }

	protected void copyInformationFromInfoToDomain(ISuportePersistente sp, InfoObject infoObject, IDomainObject domainObject) throws ExcepcaoPersistencia {
        if (infoObject instanceof InfoProfessionalCareer) {
        	IProfessionalCareer professionalCareer = (ProfessionalCareer) domainObject;
        	InfoProfessionalCareer infoProfessionalCareer = (InfoProfessionalCareer)infoObject;
        	
        	professionalCareer.setBeginYear(infoProfessionalCareer.getBeginYear());
        	professionalCareer.setEndYear(infoProfessionalCareer.getEndYear());
        	professionalCareer.setEntity(infoProfessionalCareer.getEntity());
        	professionalCareer.setFunction(infoProfessionalCareer.getFunction());
        	professionalCareer.setLastModificationDate(infoProfessionalCareer.getLastModificationDate());
        	professionalCareer.setOjbConcreteClass(ProfessionalCareer.class.getName());
        	
        	IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readByNumber(infoProfessionalCareer.getInfoTeacher().getTeacherNumber());
            
            professionalCareer.setTeacher(teacher);
        }
        else {
            InfoTeachingCareer infoTeachingCareer = (InfoTeachingCareer) infoObject;
            ITeachingCareer teachingCareer = (TeachingCareer)domainObject;
            teachingCareer.setBeginYear(infoTeachingCareer.getBeginYear());
            IPersistentCategory persistentCategory = sp.getIPersistentCategory();
            ICategory category = new Category();
            category = (ICategory)persistentCategory.readByOID(Category.class,infoTeachingCareer.getInfoCategory().getIdInternal());
            
            teachingCareer.setCategory(category);
            teachingCareer.setKeyCategory(category.getIdInternal());
            teachingCareer.setCourseOrPosition(infoTeachingCareer.getCourseOrPosition());
            teachingCareer.setEndYear(infoTeachingCareer.getEndYear());
            teachingCareer.setLastModificationDate(infoTeachingCareer.getLastModificationDate());
            teachingCareer.setOjbConcreteClass(TeachingCareer.class.getName());
            
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readByNumber(infoTeachingCareer.getInfoTeacher().getTeacherNumber());
            teachingCareer.setTeacher(teacher);
            teachingCareer.setKeyTeacher(teacher.getIdInternal());
        }
	}

	protected IDomainObject createNewDomainObject(InfoObject infoObject) {

        if (infoObject instanceof InfoProfessionalCareer) {
            return new ProfessionalCareer();
        } else if (infoObject instanceof InfoTeachingCareer) {
            return new TeachingCareer();
        } else {
            throw new Error("Unknown type of InfoCareer: " + infoObject.getClass().getName());
        }
	}

	protected Class getDomainObjectClass() {
		return Career.class ;
	}

}