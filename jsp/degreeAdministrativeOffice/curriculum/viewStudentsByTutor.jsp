<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<!-- AVISOS E ERROS -->
<span class="error"><!-- Error messages go here -->
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

		<bean:define id="mails" type="java.lang.String">
			<logic:iterate id="infoTutor" name="studentsOfTutor">
				<logic:present name="infoTutor" property="infoStudent.infoPerson.email">
					<bean:write name="infoTutor" property="infoStudent.infoPerson.email"/>,
				</logic:present>
			</logic:iterate>
		</bean:define>
		<html:link href="<%= "mailto:"+ mails %>">Send mail to all</html:link>


		<br/>

		<table align="left">	
			<tr>
				<th class="listClasses-header"><bean:message key="label.number" /></th>
				<th class="listClasses-header"><bean:message key="label.masterDegree.administrativeOffice.studentName" /></th>
				<th class="listClasses-header"><bean:message key="label.email" /></th>
			</tr>			
			<logic:iterate id="infoTutor" name="studentsOfTutor">
				<bean:define id="tutorId" name="infoTutor" property="idInternal" />
				<bean:define id="studentNumber" name="infoTutor" property="infoStudent.number" />
				<tr class="listClasses">					
					<td><bean:write name="studentNumber"/></td>					
					<td>
						<html:link page="<%= "/viewCurriculum.do?method=prepare&amp;studentNumber=" + studentNumber.toString() %>">
							<bean:write name="infoTutor" property="infoStudent.infoPerson.nome"/>
						</html:link>
					</td>
					<td>
						<logic:present name="infoTutor" property="infoStudent.infoPerson.email">
							<bean:define id="mail" name="infoTutor" property="infoStudent.infoPerson.email"/>
							<html:link href="<%= "mailto:"+ mail %>"><bean:write name="infoTutor" property="infoStudent.infoPerson.email"/></html:link>
						</logic:present>
						<logic:notPresent name="infoTutor" property="infoStudent.infoPerson.email">
							&nbsp;
						</logic:notPresent>
					</td>
				</tr>
			</logic:iterate>
		</table>		
	</logic:notEmpty>
</logic:present>
