<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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
		<html:hidden property="method" value="createCurricularCourseValuationGroup"/>
		<html:hidden property="teacherServiceDistribution"/>
		<html:hidden property="valuationCompetenceCourse"/>
		<html:hidden property="valuationGrouping"/>
		<html:hidden property="executionPeriod"/>
		<html:hidden property="courseValuationType"/>
		<html:hidden property="firstTimeEnrolledStudentsManual" value="0"/>
		<html:hidden property="secondTimeEnrolledStudentsManual" value="0"/>	
		<html:hidden property="studentsPerTheoreticalShiftManual" value="0"/>	
		<html:hidden property="studentsPerPraticalShiftManual" value="0"/>		
		<html:hidden property="studentsPerTheoPratShiftManual" value="0"/>		
		<html:hidden property="studentsPerLaboratorialShiftManual" value="0"/>		
		<html:hidden property="weightFirstTimeEnrolledStudentsPerTheoShiftManual" value="0"/>		
		<html:hidden property="weightFirstTimeEnrolledStudentsPerPratShiftManual" value="0"/>		
		<html:hidden property="weightFirstTimeEnrolledStudentsPerTheoPratShiftManual" value="0"/>		
		<html:hidden property="weightFirstTimeEnrolledStudentsPerLabShiftManual" value="0"/>		
		<html:hidden property="weightSecondTimeEnrolledStudentsPerTheoShiftManual" value="0"/>
		<html:hidden property="weightSecondTimeEnrolledStudentsPerPratShiftManual" value="0"/>
		<html:hidden property="weightSecondTimeEnrolledStudentsPerTheoPratShiftManual" value="0"/>
		<html:hidden property="weightSecondTimeEnrolledStudentsPerLabShiftManual" value="0"/>
		<html:hidden property="theoreticalHoursManual" value="0"/>		
		<html:hidden property="praticalHoursManual" value="0"/>		
		<html:hidden property="theoPratHoursManual" value="0"/>		
		<html:hidden property="laboratorialHoursManual" value="0"/>
		<html:hidden property="suppressRedundantHoursTypes" value="true"/>
		<html:hidden property="page" value="2"/>
		
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
						<html:multibox property="curricularCourseValuationArray">
							<bean:write name="curricularCourseValuation" property="idInternal"/>
						</html:multibox>
						<bean:write name="curricularCourseValuation" property="valuationCurricularCourse.degreeCurricularPlan.degree.sigla"/>		
					</logic:iterate>
				</td>
			</tr>
		</table>
		<html:button property="" onclick="this.form.submit();"><bean:message key="label.teacherServiceDistribution.createCourseValuationGroup"/></html:button>
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