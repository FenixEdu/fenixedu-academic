/*
 * Created on 8/Set/2003
 *
 */
package middleware.sigla;

import org.apache.commons.collections.Predicate;

import Dominio.ICurricularCourse;

/**
 * fenix
 * middleware.sigla
 * @author João Mota
 * 8/Set/2003
 *
 */
public class PredicateForSiglaResponsible implements Predicate {

	private ICurricularCourse curricularCourse;

	/* (non-Javadoc)
	 * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
	 */
	public boolean evaluate(Object arg0) {
		Responsavel siglaResponsible = (Responsavel) arg0;
		
		ICurricularCourse curricularCourse = getCurricularCourse();
		return (
			(curricularCourse.getCode().equals(siglaResponsible.getCod_disc()))
				&& (curricularCourse
					.getDegreeCurricularPlan()
					.getDegree()
					.getIdInternal()
					.equals(
						sigla2FenixDegreeNumbers(
							siglaResponsible.getCod_curso()))));
	}

	private Integer sigla2FenixDegreeNumbers(Integer siglaDegreeNumber) {
		if (siglaDegreeNumber.intValue() == 24) {
			siglaDegreeNumber = new Integer(51);
		}
		return siglaDegreeNumber;
	}
	/**
	 * 
	 */
	public PredicateForSiglaResponsible(ICurricularCourse curricularCourse) {
		setCurricularCourse(curricularCourse);
	}

	/**
	 * @return
	 */
	public ICurricularCourse getCurricularCourse() {
		return curricularCourse;
	}

	/**
	 * @param curricularCourse
	 */
	public void setCurricularCourse(ICurricularCourse curricularCourse) {
		this.curricularCourse = curricularCourse;
	}

}
