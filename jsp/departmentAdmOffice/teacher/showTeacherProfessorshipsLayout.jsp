<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:importAttribute/>
<tiles:useAttribute id="executionCourseLink" name="executionCourseLink" classname="java.lang.String"/>
<tiles:useAttribute id="paramId" name="paramId" classname="java.lang.String"/>
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="infoTeacher" property="teacherNumber"/>
</p>

<logic:notEmpty name="infoProfessorshipList" >	
	<h2><bean:message key="label.teacher.professorships"/></h2>
		<table width="50%"cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header"><bean:message key="label.execution-course.acronym" />
				</td>
				<td class="listClasses-header"><bean:message key="label.execution-course.name" />
				</td>
				<td class="listClasses-header"><bean:message key="label.execution-period" />
				</td>
			</tr>
			<logic:iterate id="professorship" name="infoProfessorshipList">
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<tr>
					<td class="listClasses">
						<html:link page="<%= executionCourseLink %>" paramId="<%= paramId %>" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="sigla"/>
						</html:link>
					</td>			
					<td class="listClasses">
						<html:link page="<%= executionCourseLink %>" paramId="<%= paramId %>" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="nome"/>
						</html:link>
					</td>
					<td class="listClasses">
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>&nbsp;-&nbsp;
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/>
					</td>
					
				</tr>
			</logic:iterate>
	 	</table>
</logic:notEmpty>