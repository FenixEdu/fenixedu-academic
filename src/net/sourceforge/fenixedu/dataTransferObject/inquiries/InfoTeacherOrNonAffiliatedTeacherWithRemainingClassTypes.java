/*
 * Created on 8/Mai/2005 - 11:53:40
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.util.TipoAula;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes extends
		InfoObject {
	
	private InfoTeacher teacher;
	private InfoNonAffiliatedTeacher nonAffiliatedTeacher;

	final private List<TipoAula> remainingClassTypes = new ArrayList<TipoAula>();

	
	public InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes(InfoObject infoTeacherOrNonAffiliatedTeacher, InfoExecutionCourse infoExecutionCourse) {

		if(infoTeacherOrNonAffiliatedTeacher instanceof InfoTeacher) {
			this.teacher = (InfoTeacher) infoTeacherOrNonAffiliatedTeacher;
			this.nonAffiliatedTeacher = null;
			setIdInternal(this.teacher.getIdInternal());

		} else if(infoTeacherOrNonAffiliatedTeacher instanceof InfoNonAffiliatedTeacher) {
			
			this.teacher = null;
			this.nonAffiliatedTeacher = (InfoNonAffiliatedTeacher) infoTeacherOrNonAffiliatedTeacher;
			setIdInternal(this.nonAffiliatedTeacher.getIdInternal());
		}
		
		if(infoExecutionCourse.getTheoreticalHours().doubleValue() > 0) {
			this.remainingClassTypes.add(new TipoAula(TipoAula.TEORICA));
		}
		if(infoExecutionCourse.getPraticalHours().doubleValue() > 0) {
			this.remainingClassTypes.add(new TipoAula(TipoAula.PRATICA));
		}
		if(infoExecutionCourse.getLabHours().doubleValue() > 0) {
			this.remainingClassTypes.add(new TipoAula(TipoAula.LABORATORIAL));
		}
		if(infoExecutionCourse.getTheoPratHours().doubleValue() > 0) {
			this.remainingClassTypes.add(new TipoAula(TipoAula.TEORICO_PRATICA));			
		}
		
	}
	
	public InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes () {
		
	}

	
	/**
	 * @return Returns the remainingClassTypes.
	 */
	public List<TipoAula> getRemainingClassTypes() {
		return remainingClassTypes;
	}

	/**
	 * @return Returns the nonAffiliatedTeacher.
	 */
	public InfoNonAffiliatedTeacher getNonAffiliatedTeacher() {
		return nonAffiliatedTeacher;
	}
	

	/**
	 * @param nonAffiliatedTeacher The nonAffiliatedTeacher to set.
	 */
	public void setNonAffiliatedTeacher(
			InfoNonAffiliatedTeacher nonAffiliatedTeacher) {
		this.nonAffiliatedTeacher = nonAffiliatedTeacher;
	}
	

	/**
	 * @return Returns the teacher.
	 */
	public InfoTeacher getTeacher() {
		return teacher;
	}
	

	/**
	 * @param teacher The teacher to set.
	 */
	public void setTeacher(InfoTeacher teacher) {
		this.teacher = teacher;
	}
	
	public String toString() {
		String result = "[INFOTEACHERORNONAFFILIATEDTEACHERWITHREMAININGCLASSTYPES";
		
		if(this.teacher != null) {
			result += ", " + this.teacher.toString();
		
		} else if(this.nonAffiliatedTeacher != null) {
			result += ", " + this.nonAffiliatedTeacher.toString();
		}
		
		result += this.remainingClassTypes.toString() + "]\n";
		return result;
	}


	/* (non-Javadoc)
	 * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(net.sourceforge.fenixedu.domain.IDomainObject)
	 */
	public void copyFromDomain(IDomainObject domainObject) {
		// TODO Auto-generated method stub
		super.copyFromDomain(domainObject);
	}
	
	
	
}
