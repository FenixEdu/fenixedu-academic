/*
 * Created on 22/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication;
import net.sourceforge.fenixedu.domain.ITeacher;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class OldPublication extends OldPublication_Base {
	
	public void delete() {
		removeTeacher();
		super.deleteDomainObject();
	}

	public void edit(InfoOldPublication infoOldPublication, ITeacher teacher) {
		
		if((infoOldPublication == null) || (teacher == null))
			throw new NullPointerException();
		
        this.setLastModificationDate(infoOldPublication.getLastModificationDate());
		this.setOldPublicationType(infoOldPublication.getOldPublicationType());
		this.setPublication(infoOldPublication.getPublication());
		this.setTeacher(teacher);

	}
	
}
