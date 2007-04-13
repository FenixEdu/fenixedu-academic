<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<h3><bean:message key="link.teacherServiceDistribution"/></h3>
<em><bean:write name="teacherServiceDistribution" property="name"/></em>


<h4>
	<table class='vtsbc'>
		<tr>
			<td class="courses">
				<bean:message key="label.teacherServiceDistribution.executionYear"/>
			</td>
			<td align="center">
				<bean:write name="teacherServiceDistribution" property="executionYear.year" />
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="label.teacherServiceDistribution.semesters"/>
			</td>
			<td align="center">
				<logic:iterate id="executionPeriod" name="teacherServiceDistribution" property="orderedExecutionPeriods">
					<bean:write name="executionPeriod" property="semester"/>&nbsp;
					<bean:message key="label.teacherServiceDistribution.semester"/>&nbsp;&nbsp;
				</logic:iterate>
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="label.teacherServiceDistribution.currentPhase"/>
				&nbsp;&nbsp;&nbsp;
			</td>
			<td align="center">
				<bean:write name="teacherServiceDistribution" property="currentValuationPhase.name"/>
			</td>
		</tr>
	</table>
</h4>

<ul>
<logic:equal name="permissionsGrantPermission" value="true">		
	<li>
		<html:link page='<%= "/valuationGroupingSupport.do?method=prepareForPermissionServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.permissionSupportService"/>
		</html:link>
	</li>
</logic:equal>
<logic:equal name="phaseManagementPermission" value="true">	
	<li>
	  	<html:link page='<%= "/valuationPhasesManagement.do?method=prepareForValuationPhasesManagement&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
	  		<bean:message key="link.teacherServiceDistribution.valuationPhasesManagement"/>
	  	</html:link>
	</li>
</logic:equal>
<logic:equal name="omissionConfigurationPermission" value="true">		
	<li>
	  	<html:link page='<%= "/valuationPhasesManagement.do?method=prepareForOmissionValuesValuation&amp;edit=no&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
	  		<bean:message key="link.teacherServiceDistribution.omissionValuesValuation"/>
	  	</html:link>
	</li>
</logic:equal>
<logic:equal name="valuationCompetenceCoursesAndTeachersManagementPermission" value="true">
	<li>
	  	<html:link page='<%= "/valuationTeachersGroup.do?method=prepareForValuationTeachersGroupServices&amp;valuationGroupingID=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getCurrentValuationPhase().getRootValuationGrouping().getIdInternal().toString() %>'>
	  		<bean:message key="link.teacherServiceDistribution.manageRootGrouping"/>
	  	</html:link>
	</li>
</logic:equal>
<logic:equal name="coursesAndTeachersManagementPermission" value="true">			
	<li>
		<html:link page='<%= "/valuationGroupingSupport.do?method=prepareForValuationGroupingSupportServices&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.groupingAreaSupportService"/>
		</html:link>
	</li>
</logic:equal>	
<logic:equal name="coursesAndTeachersValuationPermission" value="true">	
	<li>
		<html:link page='<%= "/courseValuation.do?method=prepareForCourseValuation&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.courseValuationService"/>
		</html:link>
	</li>
	<li>
		<html:link page='<%= "/professorshipValuation.do?method=prepareForProfessorshipValuation&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.professorshipValuationService"/>
		</html:link>
	</li>
</logic:equal>
<logic:equal name="automaticValuationPermission" value="true">		
	<li>
		<html:link page='<%= "/valuationPhasesManagement.do?method=prepareForCurrentValuationPhaseDataManagement&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.createAutomaticCourseValuations"/>
		</html:link>
	</li>
</logic:equal>
<logic:equal name="viewTeacherServiceDistributionValuationPermission" value="true">
	<li>
		<html:link page='<%= "/teacherServiceDistributionValuation.do?method=prepareForTeacherServiceDistributionValuation&amp;teacherServiceDistribution=" + ((TeacherServiceDistribution) request.getAttribute("teacherServiceDistribution")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionValuation"/>
		</html:link>
	</li>	
</logic:equal>
<logic:equal name="viewTeacherServiceDistributionValuationPermission" value="false">
	<li>
		<span class="error">
			<bean:message key="label.teacherServiceDistribution.noPermissionsForTSD"/>
		</span>
	</li>	
</logic:equal>
</ul>