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
<html:form action="/showTeacherProfessorshipsForManagement">
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
				<td class="listClasses-header">
					<bean:message key="label.professorship.responsibleFor"/>
				</td>
				<logic:present role="role.department.credits.manager">
					<logic:equal name="isDepartmentManager" value="true">
						<td class="listClasses-header">
							<bean:message key="label.professorship.remove" />
						</td>
					</logic:equal>
				</logic:present>
			</tr>
			<logic:iterate id="detailedProfessorship" name="detailedProfessorshipList">
				<bean:define id="professorship" name="detailedProfessorship" property="infoProfessorship"/>
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<tr>
					<td class="listClasses" style="text-align:left">
						<bean:write name="infoExecutionCourse" property="nome"/>
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
					<td class="listClasses">
						<logic:present role="role.department.credits.manager">
							<logic:equal name="isDepartmentManager" value="true">
								<bean:define id="executionCourseId" name="infoExecutionCourse" property="idInternal" />
									<html:multibox property="executionCourseResponsability" value="<%= executionCourseId.toString() %>" />
							</logic:equal>
							<logic:equal name="isDepartmentManager" value="false">
								<logic:equal name="detailedProfessorship" property="responsibleFor" value="true">
									<bean:message key="label.yes"/>
								</logic:equal>
								<logic:equal name="detailedProfessorship" property="responsibleFor" value="false">
									<bean:message key="label.no"/>								
								</logic:equal>
							</logic:equal>
						</logic:present>
					</td>
					<logic:present role="role.department.credits.manager">
						<logic:equal name="isDepartmentManager" value="true">					
							<td class="listClasses">
								<bean:define id="removeLink">
									 /removeProfessorship.do?teacherNumber=<bean:write name="infoTeacher" property="teacherNumber"/>&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>&amp;idInternal=<bean:write name="infoTeacher" property="idInternal"/>&amp;executionCourseId=<bean:write name="professorship" property="infoExecutionCourse.idInternal"/>
								</bean:define>
								<html:link page='<%= removeLink.toString() %>'>
									<bean:message key="link.remove" />
								</html:link>
							</td>
						</logic:equal>
					</logic:present>				
				</tr>
			</logic:iterate>
	 	</table>
		<logic:present role="role.department.credits.manager">
			<logic:equal name="isDepartmentManager" value="true">
			 	<html:submit styleClass="inputbutton">
			 		<bean:message key="button.save"/>
			 	</html:submit>
			</logic:equal>
		</logic:present>		 	
	</html:form>		 	
</logic:notEmpty>
<logic:present role="role.department.credits.manager">
	<logic:equal name="isDepartmentManager" value="true">
		<br />
		<br />
		<html:link page="/createProfessorship.do?method=showExecutionYearExecutionPeriods&amp;page=0" paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
			<bean:message key="link.professorship.addExecutionCourse"/>
		</html:link>
	</logic:equal>
</logic:present>
