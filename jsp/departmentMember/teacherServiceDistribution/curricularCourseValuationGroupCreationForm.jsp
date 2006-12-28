<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal() %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<html:link page='<%= "/courseValuation.do?method=prepareForCourseValuation&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
		<bean:message key="link.teacherServiceDistribution.courseValuationService"/>
	</html:link>
	>
	<bean:message key="label.teacherServiceDistribution.createCourseValuationGroup"/>
</h3>


<logic:empty name="availableCurricularCourseValuationToGroupList">
<span class="error">
	<br/>
	<bean:message key="label.teacherServiceDistribution.curricularCoursesToGroupCreationNotAvailable"/>
</span>
</logic:empty>

<logic:notEmpty name="availableCurricularCourseValuationToGroupList">
	<html:form action="/curricularCourseValuationGroupCreation">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createCurricularCourseValuationGroup"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationCompetenceCourse" property="valuationCompetenceCourse"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.valuationGrouping" property="valuationGrouping"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.courseValuationType" property="courseValuationType"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.firstTimeEnrolledStudentsManual" property="firstTimeEnrolledStudentsManual" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.secondTimeEnrolledStudentsManual" property="secondTimeEnrolledStudentsManual" value="0"/>	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentsPerTheoreticalShiftManual" property="studentsPerTheoreticalShiftManual" value="0"/>	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentsPerPraticalShiftManual" property="studentsPerPraticalShiftManual" value="0"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentsPerTheoPratShiftManual" property="studentsPerTheoPratShiftManual" value="0"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentsPerLaboratorialShiftManual" property="studentsPerLaboratorialShiftManual" value="0"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weightFirstTimeEnrolledStudentsPerTheoShiftManual" property="weightFirstTimeEnrolledStudentsPerTheoShiftManual" value="0"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weightFirstTimeEnrolledStudentsPerPratShiftManual" property="weightFirstTimeEnrolledStudentsPerPratShiftManual" value="0"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weightFirstTimeEnrolledStudentsPerTheoPratShiftManual" property="weightFirstTimeEnrolledStudentsPerTheoPratShiftManual" value="0"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weightFirstTimeEnrolledStudentsPerLabShiftManual" property="weightFirstTimeEnrolledStudentsPerLabShiftManual" value="0"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weightSecondTimeEnrolledStudentsPerTheoShiftManual" property="weightSecondTimeEnrolledStudentsPerTheoShiftManual" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weightSecondTimeEnrolledStudentsPerPratShiftManual" property="weightSecondTimeEnrolledStudentsPerPratShiftManual" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weightSecondTimeEnrolledStudentsPerTheoPratShiftManual" property="weightSecondTimeEnrolledStudentsPerTheoPratShiftManual" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weightSecondTimeEnrolledStudentsPerLabShiftManual" property="weightSecondTimeEnrolledStudentsPerLabShiftManual" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoreticalHoursManual" property="theoreticalHoursManual" value="0"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.praticalHoursManual" property="praticalHoursManual" value="0"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoPratHoursManual" property="theoPratHoursManual" value="0"/>		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.laboratorialHoursManual" property="laboratorialHoursManual" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.suppressRedundantHoursTypes" property="suppressRedundantHoursTypes" value="true"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
		
		<br/>
		<table class="vtsbc">
			<tr>
				<th>
					<b><bean:message key="label.teacherServiceDistribution.availableCurricularCoursesToAssociate"/></b>
				</th>
			</tr>
			<tr>
				<td>
					<logic:iterate name="availableCurricularCourseValuationToGroupList" id="curricularCourseValuation">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.curricularCourseValuationArray" property="curricularCourseValuationArray">
							<bean:write name="curricularCourseValuation" property="idInternal"/>
						</html:multibox>
						<bean:write name="curricularCourseValuation" property="valuationCurricularCourse.degreeCurricularPlan.degree.sigla"/>		
					</logic:iterate>
				</td>
			</tr>
		</table>
		<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.submit();"><bean:message key="label.teacherServiceDistribution.createCourseValuationGroup"/></html:button>
	</html:form>
	
	<span class="error">
		<html:errors/>
	</span>
</logic:notEmpty>
<br/>
<br/>
<br/>
<html:link page='<%= "/courseValuation.do?method=prepareForCourseValuation&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
	<bean:message key="link.back"/>
</html:link>