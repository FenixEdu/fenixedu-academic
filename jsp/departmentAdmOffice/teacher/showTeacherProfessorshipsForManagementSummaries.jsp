<%@ page import="java.util.TreeMap" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="infoTeacher" property="teacherNumber"/> <br />
	<b> <bean:message key="label.teacher.department"/> </b> <bean:write name="teacherDepartment" property="name"/> 
	<logic:present role="role.department.credits.manager">
		<logic:equal name="isDepartmentManager" value="true">
		</logic:equal>
	</logic:present>
</p>
<logic:messagesPresent>
	<html:errors />
</logic:messagesPresent>
<html:form action="/showTeacherProfessorshipsForSummariesManagement">
	<html:hidden property="idInternal" />	
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
				<html:select property="executionYearId" onchange="this.form.submit();">
					<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
				</html:select>
			</td>
		</tr>
	</table>	
</html:form>
<br />
<logic:notEmpty name="detailedProfessorshipList" >	
	<html:form action="/updateTeacherExecutionYearExecutionCourseResponsabilities">
		<html:hidden property="idInternal" />	
		<html:hidden property="teacherId" />
		<html:hidden property="executionYearId" />

		<h2><bean:message key="label.teacher.professorships"/></h2>
		<table width="100%"cellpadding="5" border="0">
			<tr>
				<td class="listClasses-header" style="text-align:left">
					<bean:message key="label.execution-course.name" />
				</td>
				<td class="listClasses-header" style="text-align:left">
					<bean:message key="label.execution-course.degrees" />
				</td>
				<td class="listClasses-header">
					<bean:message key="label.execution-period" />
				</td>
			</tr>
			
			<bean:define id="args" scope="request" type="java.util.TreeMap" name="args"/>
			<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber" />
			
			<logic:iterate id="detailedProfessorship" name="detailedProfessorshipList">
				<bean:define id="professorship" name="detailedProfessorship" property="infoProfessorship"/>
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<bean:define id="executionCourseId" name="infoExecutionCourse" property="idInternal" />													
				<tr>
					<td class="listClasses" style="text-align:left">
						<logic:present role="role.department.credits.manager">
							<html:hidden property='<%= "hours("+ executionCourseId +")" %>' />							
						</logic:present>
						<% args.clear(); args.put("objectCode", executionCourseId); args.put("teacherNumber", teacherNumber); %>						
						<html:link page="/showSummaries.do?method=showSummaries&amp;page=0" name="args" >
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

