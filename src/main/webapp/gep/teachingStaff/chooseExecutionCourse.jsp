<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<table >
	<tr class="listClasses-header">
		<th><bean:message key="label.inquiries.year" bundle="INQUIRIES_RESOURCES"/></th>
		<th><bean:message key="label.inquiries.semester" bundle="INQUIRIES_RESOURCES"/></th>
		<th><bean:message key="table.header.inquiries.courseName" bundle="INQUIRIES_RESOURCES"/></th>		
		<th><bean:message key="table.header.inquiries.degrees" bundle="INQUIRIES_RESOURCES"/></th>	
		<th/>					
	</tr>

	<bean:define id="executionYear" name="executionYear" type="java.lang.String" />
	<logic:iterate id="degreeModuleScope" name="sortedScopes">
		<bean:define id="curricularCourse" name="degreeModuleScope" property="curricularCourse"/>
		<logic:iterate id="executionCourse" name="curricularCourse" property="associatedExecutionCourses" >
			<logic:equal name="executionCourse" property="executionPeriod.state" value="CURRENT">
			<logic:equal name="executionCourse" property="availableForInquiries" value="true">
				<tr class="listClasses">
					<td><strong><bean:write name="degreeModuleScope" property="curricularYear" /></strong></td>
					<td><strong><bean:write name="executionCourse" property="executionPeriod.semester" /></strong></td>
					<td>									
						<bean:write name="executionCourse" property="nome" />
					</td>
					<td>
						<logic:iterate id="CurricularCourse" name="executionCourse" property="associatedCurricularCourses" >
							<bean:write name="CurricularCourse" property="degreeCurricularPlan.name" />, 					
						</logic:iterate>
					</td>
					<bean:define id="executionCourseID" name="executionCourse" property="externalId" ></bean:define>
					<td>
						<html:link page='<%= "/teachingStaff.do?method=viewTeachingStaff&executionCourseID=" + executionCourseID %>'>
							Editar Corpo Docente
						</html:link>					
					</td>
				</tr>
			</logic:equal>
			</logic:equal>
		</logic:iterate>		
	</logic:iterate>
	
</table>

