/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.dto.commons;

import java.io.Serializable;

/**
 * This class provides the degrees for the StandaloneIndividualCandidacy
 */
//TODO: DELETE
@Deprecated
public class SearchCurricularCourseByDegree implements Serializable {

//	private ExecutionInterval executionInterval;
//	private DegreeByExecutionYearBean degreeBean;
//	private DegreeCurricularPlan degreeCurricularPlan;
//	private CurricularCourseByExecutionSemesterBean curricularCourseBean;
//
//	public SearchCurricularCourseByDegree(final ExecutionSemester executionSemester) {
//		setExecutionSemester(executionSemester);
//	}
//
//	public ExecutionInterval getExecutionSemester() {
//		return this.executionInterval;
//	}
//
//	public void setExecutionSemester(ExecutionInterval executionSemester) {
//		this.executionInterval = executionSemester;
//	}
//
//	public DegreeByExecutionYearBean getDegreeBean() {
//		return degreeBean;
//	}
//
//	public void setDegreeBean(DegreeByExecutionYearBean degreeBean) {
//		this.degreeBean = degreeBean;
//		setDegreeCurricularPlan(null);
//		setCurricularCourseBean(null);
//	}
//
//	public boolean hasDegreeBean() {
//		return getDegreeBean() != null;
//	}
//
//	public DegreeCurricularPlan getDegreeCurricularPlan() {
//		return this.degreeCurricularPlan;
//	}
//
//	public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
//		if (hasDegreeBean()
//				&& getDegreeBean().getDegree().getDegreeCurricularPlansSet().contains(degreeCurricularPlan)) {
//			this.degreeCurricularPlan = degreeCurricularPlan;
//		} else {
//			this.degreeCurricularPlan = null;
//		}
//		setCurricularCourseBean(null);
//	}
//
//	public void removeDegreeCurricularPlan() {
//		this.degreeCurricularPlan = null;
//	}
//
//	public boolean hasDegreeCurricularPlan() {
//		return getDegreeCurricularPlan() != null;
//	}
//
//	public CurricularCourseByExecutionSemesterBean getCurricularCourseBean() {
//		return curricularCourseBean;
//	}
//
//	public void setCurricularCourseBean(CurricularCourseByExecutionSemesterBean curricularCourseBean) {
//		this.curricularCourseBean = curricularCourseBean;
//	}
//
//	public void removeCurricularCourseBean() {
//		this.curricularCourseBean = null;
//	}
//
//	public boolean hasCurricularCourseBean() {
//		return this.curricularCourseBean != null;
//	}
//
//	static public class SearchCurricularCourseDegreeCurricularPlansProvider implements DataProvider {
//
//		@Override
//		public Object provide(Object source, Object currentValue) {
//			final SearchCurricularCourseByDegree bean = (SearchCurricularCourseByDegree) source;
//
//			if (bean.hasDegreeBean()) {
//				final List<DegreeCurricularPlan> result = getDegreeCurricularPlans(bean);
//				Collections.sort(result, DegreeCurricularPlan.COMPARATOR_BY_NAME);
//				return result;
//
//			} else {
//				return Collections.<DegreeCurricularPlan>emptyList();
//			}
//		}
//
//		private List<DegreeCurricularPlan> getDegreeCurricularPlans(final SearchCurricularCourseByDegree bean) {
//			return new ArrayList<DegreeCurricularPlan>(bean.getDegreeBean().getDegreeCurricularPlans());
//		}
//
//		@Override
//		public Converter getConverter() {
//			return new DomainObjectKeyConverter();
//		}
//	}

}
