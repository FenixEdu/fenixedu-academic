<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:write name="tsdProcess" property="name"/></h2>


<table class='tstyle1 thlight thright'>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.executionYear"/>:
		</th>
		<td>
			<bean:write name="tsdProcess" property="executionYear.year" />
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.semesters"/>:
		</th>
		<td>
			<logic:iterate id="executionPeriod" name="tsdProcess" property="orderedExecutionPeriods">
				<bean:write name="executionPeriod" property="semester"/>&nbsp;
				<bean:message key="label.teacherServiceDistribution.semester"/>&nbsp;&nbsp;
			</logic:iterate>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.currentPhase"/>:
		</th>
		<td>
			<bean:write name="tsdProcess" property="currentTSDProcessPhase.name"/>
		</td>
	</tr>
</table>


<ul>
<logic:equal name="permissionsGrantPermission" value="true">		
	<li>
		<html:link page='<%= "/tsdSupport.do?method=prepareForPermissionServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.permissionSupportService"/>
		</html:link>
	</li>
</logic:equal>
<logic:equal name="phaseManagementPermission" value="true">	
	<li>
	  	<html:link page='<%= "/tsdProcessPhasesManagement.do?method=prepareForTSDProcessPhasesManagement&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
	  		<bean:message key="link.teacherServiceDistribution.tsdProcessPhasesManagement"/>
	  	</html:link>
	</li>
</logic:equal>
<logic:equal name="omissionConfigurationPermission" value="true">		
	<li>
	  	<html:link page='<%= "/tsdProcessPhasesManagement.do?method=prepareForOmissionValuesValuation&amp;edit=no&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
	 		<bean:message key="link.teacherServiceDistribution.omissionValuesValuation"/>
	  	</html:link>
	</li>
</logic:equal>
<logic:equal name="tsdCoursesAndTeachersManagementPermission" value="true">
	<li>
	  	<html:link page='<%= "/tsdTeachersGroup.do?method=prepareForTSDTeachersGroupServices&amp;tsdID=" + ((TSDProcess) request.getAttribute("tsdProcess")).getCurrentTSDProcessPhase().getRootTSD().getIdInternal().toString() %>'>
	  		<bean:message key="link.teacherServiceDistribution.manageRootGrouping"/>
	  	</html:link>
	</li>
</logic:equal>

<logic:equal name="teachersManagementPermission" value="true">			
	<li>
		<html:link page='<%= "/tsdSupport.do?method=prepareForTeacherServiceDistributionSupportServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.groupingAreaSupportService"/>
		</html:link>
	</li>
</logic:equal>	
<logic:equal name="coursesValuationPermission" value="true">	
	<li>
		<html:link page='<%= "/tsdCourse.do?method=prepareForTSDCourse&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.tsdCourseService"/>
		</html:link>
	</li>
</logic:equal>
<logic:equal name="teachersValuationPermission" value="true">
	<li>
		<html:link page='<%= "/tsdProfessorship.do?method=prepareForTSDProfessorship&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.tsdProfessorshipService"/>
		</html:link>
	</li>
</logic:equal>
<logic:equal name="automaticValuationPermission" value="true">		
	<li>
		<html:link page='<%= "/tsdProcessPhasesManagement.do?method=prepareForCurrentTSDProcessPhaseDataManagement&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.createAutomaticTSDCourses"/>
		</html:link>
	</li>
</logic:equal>
<logic:equal name="viewTSDProcessValuationPermission" value="true">
	<li>
		<html:link page='<%= "/tsdProcessValuation.do?method=prepareForTSDProcessValuation&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:message key="link.teacherServiceDistribution.tsdProcessValuation"/>
		</html:link>
	</li>	
</logic:equal>
<logic:equal name="viewTSDProcessValuationPermission" value="false">
	 <li>
		<span class="error">
			<bean:message key="label.teacherServiceDistribution.noPermissionsForTSD"/>
		</span>
	</li>	
</logic:equal>
</ul>