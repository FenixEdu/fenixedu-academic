<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="infoTeacher" property="teacherNumber"/> <br />
	(<i><html:link page="/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1" paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)		
</p>


<html:link page="/createProfessorship.do?method=showExecutionDegrees&amp;page=0" paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
	<bean:message key="link.professorship.addExecutionCourse"/>
</html:link>
<br />
<br />
<logic:messagesPresent>
	<html:errors />
</logic:messagesPresent>
<logic:notEmpty name="detailedProfessorshipList" >	
	<bean:define id="executionCourseLink">
	/manageTeacherShiftProfessorships.do?page=0&amp;method=showForm&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>	
	</bean:define>
	<h2><bean:message key="label.teacher.professorships"/></h2>
		<table width="100%"cellpadding="0" border="0">
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.execution-course.acronym" />
				</td>
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
					<bean:message key="label.professorship.remove" />
				</td>
			</tr>
			<logic:iterate id="detailedProfessorship" name="detailedProfessorshipList">
				<bean:define id="professorship" name="detailedProfessorship" property="infoProfessorship"/>
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<tr>
					<td class="listClasses" >
						<html:link page="<%= executionCourseLink %>" paramId="executionCourseId" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="sigla"/>
						</html:link>
					</td>			
					<td class="listClasses" style="text-align:left">
						<html:link page="<%= executionCourseLink %>" paramId="executionCourseId" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="nome"/>
						</html:link>
					</td>
					<td class="listClasses" style="text-align:left">
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
						<bean:define id="removeLink">
							 /removeProfessorship.do?teacherNumber=<bean:write name="infoTeacher" property="teacherNumber"/>&amp;teacherId=<bean:write name="infoTeacher" property="idInternal"/>&amp;executionCourseId=<bean:write name="professorship" property="infoExecutionCourse.idInternal"/>
						</bean:define>
						<html:link page='<%= removeLink.toString() %>'>
							<bean:message key="link.remove" />
						</html:link>
					</td>
				</tr>
			</logic:iterate>
	 	</table>
</logic:notEmpty>

