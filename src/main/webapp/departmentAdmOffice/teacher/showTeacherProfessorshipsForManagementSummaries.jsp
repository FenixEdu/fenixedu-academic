<%@ page import="java.util.TreeMap" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="infoTeacher" property="teacherId"/> <br />
	<b> <bean:message key="label.teacher.department"/> </b> <bean:write name="teacherDepartment" property="name"/> 
</p>
<logic:messagesPresent>
	<html:errors />
</logic:messagesPresent>
<html:form action="/showTeacherProfessorshipsForSummariesManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId" />	
	<table width="100%">
		<tr>
			<td class="infoop">
				<bean:message key="message.courses.chooseExecutionYear"/>
			</td>
		</tr>
		<tr><td><br /></td></tr>
		<tr>
			<td>
				<bean:message key="label.executionYear"/>:
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearId" property="executionYearId" onchange="this.form.submit();">
					<html:options collection="executionYears" property="externalId" labelProperty="year"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
	</table>	
</html:form>
<br />
<logic:notEmpty name="detailedProfessorshipList" >	
	<html:form action="/updateTeacherExecutionYearExecutionCourseResponsabilities">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId" />	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearId" property="executionYearId" />

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
					<bean:message key="label.execution-period" />
				</th>
			</tr>
			
			<bean:define id="args" scope="request" type="java.util.TreeMap" name="args"/>
			<bean:define id="teacherId" name="infoTeacher" property="teacherId" />
			
			<logic:iterate id="detailedProfessorship" name="detailedProfessorshipList">
				<bean:define id="professorship" name="detailedProfessorship" property="infoProfessorship"/>
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<bean:define id="executionCourseId" name="infoExecutionCourse" property="externalId" />													
				<tr>
					<td class="listClasses" style="text-align:left">
						<logic:present role="role(DEPARTMENT_CREDITS_MANAGER)">
							<html:hidden alt='<%= "hours("+ executionCourseId +")" %>' property='<%= "hours("+ executionCourseId +")" %>' />							
						</logic:present>
						<% args.clear(); args.put("executionCourseID", executionCourseId); args.put("teacherId_", teacherId); %>						
						<html:link page="/summariesManagement.do?method=prepareShowSummaries&amp;page=0" name="args" >
							<bean:write name="infoExecutionCourse" property="nome"/>
						</html:link>
					</td>
					<td class="listClasses" style="text-align:left">&nbsp;
						<bean:define id="infoDegreeList" name="detailedProfessorship" property="infoDegreeList" />
						<bean:size id="degreeSizeList" name="infoDegreeList"/>
						<logic:iterate id="infoDegree" name="infoDegreeList" indexId="index">
							<bean:write name="infoDegree" property="sigla" /> 
							<logic:notEqual name="degreeSizeList" value="<%= String.valueOf(index.intValue() + 1) %>">
							,
							</logic:notEqual>
						</logic:iterate>
					</td>					
					<td class="listClasses">
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>&nbsp;-&nbsp;
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/>
					</td>		
				</tr>
			</logic:iterate>
	 	</table>		
	</html:form>		 	
</logic:notEmpty>

