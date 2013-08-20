<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<bean:parameter id="executionPeriodId" name="executionPeriodId" />
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="teacher" property="person.name"/><br />
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacher" property="teacherId"/> <br />
</p>

<bean:parameter id="executionPeriodId" name="executionPeriodId"/>

<logic:messagesPresent>
	<html:errors />
</logic:messagesPresent>
<logic:notEmpty name="professorshipDTOs" >	
	<bean:define id="executionCourseLink">
	/degreeTeachingServiceManagement.do?page=0&amp;method=showTeachingServiceDetails&amp;executionPeriodId=<bean:write name="executionPeriodId"/>	
	</bean:define>
	<h2><bean:message key="label.teacher.professorships"/></h2>
		<table width="100%"cellpadding="5" border="0">
			<tr>
				<th class="listClasses-header" style="text-align:left">
					<bean:message key="label.execution-course.name" />
				</th>
				<th class="listClasses-header" style="text-align:left">	
					<bean:message key="label.execution-course.degrees" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.professorship.responsibleFor" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.execution-period" />
				</th>
			</tr>
			<logic:iterate id="professorshipDTO" name="professorshipDTOs">
				<bean:define id="professorship" name="professorshipDTO" property="professorship"/>
				<bean:define id="executionCourse" name="professorship" property="executionCourse"/>
				<tr>
					<td class="listClasses" style="text-align:left">
						<html:link page="<%= executionCourseLink %>" paramId="professorshipID" paramName="professorship" paramProperty="externalId">
							<bean:write name="executionCourse" property="nome"/>
						</html:link>
					</td>
					<td class="listClasses" style="text-align:left">
						(<bean:write name="professorship" property="degreeSiglas"/>)
					</td>					
					<td class="listClasses">
						<logic:equal name="professorship" property="responsibleFor" value="true">
							<bean:message key="label.yes"/>
						</logic:equal>
						<logic:equal name="professorship" property="responsibleFor" value="false">
							<bean:message key="label.no"/>
						</logic:equal>					
					</td>					
					<td class="listClasses">
						<bean:write name="executionCourse" property="executionPeriod.name"/>&nbsp;-&nbsp;
						<bean:write name="executionCourse" property="executionPeriod.executionYear.year"/>
					</td>
				</tr>
			</logic:iterate>
	 	</table>
</logic:notEmpty>

