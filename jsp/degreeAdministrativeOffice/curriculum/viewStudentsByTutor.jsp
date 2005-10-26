<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<!-- AVISOS E ERROS -->
<span class="error">
<logic:notPresent name="studentsOfTutor">
	<bean:message key="error.tutor.noStudent" />
</logic:notPresent>

<logic:present name="studentsOfTutor">
<logic:empty name="studentsOfTutor">
	<bean:message key="error.tutor.noStudent" />
</logic:empty>
</logic:present>
<html:errors/>
</span>

<logic:present name="studentsOfTutor">
	<logic:notEmpty name="studentsOfTutor">
		<bean:size id="studentsSize" name="studentsOfTutor" />	
		<b><bean:message key="error.tutor.numberStudents" arg0="<%= studentsSize.toString() %>"/></b><br/><br/>
		<table align="left">	
			<tr>
				<th class="listClasses-header"><bean:message key="label.number" /></th>
				<th class="listClasses-header"><bean:message key="label.masterDegree.administrativeOffice.studentName" /></th>
			</tr>			
			<logic:iterate id="infoTutor" name="studentsOfTutor">
				<bean:define id="tutorId" name="infoTutor" property="idInternal" />
				<bean:define id="studentNumber" name="infoTutor" property="infoStudent.number" />
				<tr class="listClasses">					
					<td><bean:write name="studentNumber"/></td>					
					<td>
						<html:link page="<%= "/viewCurriculum.do?method=getStudentCP&amp;studentNumber=" + studentNumber.toString() %>">
							<bean:write name="infoTutor" property="infoStudent.infoPerson.nome"/>
						</html:link>
					</td>
				</tr>
			</logic:iterate>
		</table>		
	</logic:notEmpty>
</logic:present>
