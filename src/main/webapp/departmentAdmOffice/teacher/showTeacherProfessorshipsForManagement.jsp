<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoPerson" property="nome"/><br />
	<b><bean:message key="label.istid" bundle="APPLICATION_RESOURCES" /></b> <bean:write name="infoPerson" property="istUsername"/> <br />
	<b> <bean:message key="label.teacher.department"/> </b> 
		<logic:present name="teacherDepartment">
			<bean:write name="teacherDepartment" property="name"/> 
		</logic:present>
		<logic:notPresent name="teacherDepartment">
			Esta pessoa não é docente
		</logic:notPresent>
</p>

<html:form action="/showTeacherProfessorshipsForManagement">
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

<logic:messagesPresent message="true">
	<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<logic:notEmpty name="detailedProfessorshipList" >	
	<html:form action="/updateTeacherExecutionYearExecutionCourseResponsabilities">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" property="teacherName"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearId" property="executionYearId" />
		
		<h2><bean:message key="label.teacher.professorships"/></h2>
		<table width="100%"cellpadding="5" border="0">
			<tr>
				<th class="listClasses-header" style="text-align:left">
					<bean:message key="label.execution-course.name" />
				</th>
				<th class="listClasses-header" style="text-align:left">
					<bean:message key="label.execution-course.degrees.all" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.execution-period" />
				</th>
				<th class="listClasses-header">
					<bean:message key="label.professorship.responsibleFor"/>
				</th>
				
				<logic:present role="role(CREDITS_MANAGER)">
					<th class="listClasses-header">
						<bean:message key="label.professorship.hours"/>
					</th>
				</logic:present>
				
				<logic:present role="role(DEPARTMENT_CREDITS_MANAGER)">
					<logic:equal name="isDepartmentManager" value="true">
						<th class="listClasses-header">
							<bean:message key="label.professorship.remove" />
						</th>
					</logic:equal>
				</logic:present>
			</tr>
			<logic:iterate id="detailedProfessorship" name="detailedProfessorshipList">
				<bean:define id="professorship" name="detailedProfessorship" property="infoProfessorship"/>
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<bean:define id="executionCourseId" name="infoExecutionCourse" property="externalId" />

				<tr>
					<td class="listClasses" style="text-align:left">
						<logic:present role="role(DEPARTMENT_CREDITS_MANAGER)">
							<html:hidden alt='<%= "hours("+ executionCourseId +")" %>' property='<%= "hours("+ executionCourseId +")" %>' />							
						</logic:present>
					
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
						<logic:present role="(role(DEPARTMENT_CREDITS_MANAGER) | role(CREDITS_MANAGER))">
							<logic:equal name="isDepartmentManager" value="true">
								<bean:define id="executionCourseId" name="infoExecutionCourse" property="externalId" />
									<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.executionCourseResponsability" property="executionCourseResponsability" value="<%= executionCourseId.toString() %>" />
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
					<logic:present role="role(CREDITS_MANAGER)">
						<td class="listClasses">
							<logic:equal name="detailedProfessorship" property="masterDegreeOnly" value="true">
								<html:text alt='<%= "hours("+ executionCourseId +")" %>' property='<%= "hours("+ executionCourseId +")" %>' size="5"/>
							</logic:equal>
							<logic:equal name="detailedProfessorship" property="masterDegreeOnly" value="false" >
								-- 
							</logic:equal>
						</td>
					</logic:present>

					<logic:present role="role(DEPARTMENT_CREDITS_MANAGER)">
						<logic:equal name="isDepartmentManager" value="true">					
							<td class="listClasses">
								<bean:define id="removeLink">
									 /removeProfessorship.do?teacherId=<bean:write name="infoPerson" property="istUsername"/>&amp;teacherId=<bean:write name="infoPerson" property="externalId"/>&amp;externalId=<bean:write name="infoPerson" property="externalId"/>&amp;executionCourseId=<bean:write name="professorship" property="infoExecutionCourse.externalId"/>&amp;executionYearId=<bean:write name="teacherExecutionCourseResponsabilities" property="executionYearId" />
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
		<logic:present role="role(DEPARTMENT_CREDITS_MANAGER)">
			<logic:equal name="isDepartmentManager" value="true">
			 	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			 		<bean:message key="button.save"/>
			 	</html:submit>
			</logic:equal>
		</logic:present>		 	
		<logic:present role="role(CREDITS_MANAGER)">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.save" property="save" styleClass="inputbutton">
				<bean:message key="button.submit" />
			</html:submit>
		</logic:present>
		
	</html:form>		 	
</logic:notEmpty>

		<br />
		<br />
		<html:link page="/createProfessorship.do?method=showExecutionYearExecutionPeriods&amp;page=0" paramId="teacherId" paramName="infoPerson" paramProperty="istUsername">
			<bean:message key="link.professorship.addExecutionCourse"/>
		</html:link>

