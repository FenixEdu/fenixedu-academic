<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<h2><bean:message key="label.shifts"/></h2>

<ul>
	<li>
		<html:link page="/manageExecutionCourse.do?method=manageShifts" paramId="executionCourseID" paramName="executionCourseID">
			<bean:message key="label.back"/>
		</html:link>
	</li>
</ul>
<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<span class="warning0"><!--  w3c complient -->
		<html:messages id="info" message="true"/>
	</span>
</p>
<logic:present name="shift">
	<bean:define id="shiftID" name="shift" property="idInternal"/>
	<p>
		<b><bean:message key="label.shift"/>:</b> <fr:view name="shift" property="nome"/>
	</p>
	<logic:empty name="registrations">
		<p><em><bean:message key="label.shifts.empty"/></em></p>
	</logic:empty>
	
	<logic:present name="registration">
		<p><span class="success0">O aluno <fr:view name="registration" property="person.name"/> foi removido do turno.</span></p>
	</logic:present>
	
	<logic:notEmpty name="registrations">
		<bean:define id="executionCourseID" name="executionCourseID"/>
		<p class="mbottom0">
			<html:link page="<%= "/manageExecutionCourse.do?method=removeAttendsFromShift&executionCourseID=" + executionCourseID + "&removeAll=true" %>" paramId="shiftID" paramName="shift" paramProperty="idInternal">
				<bean:message key="label.shifts.remove"/>
			</html:link>
		</p>
		
		<logic:present name="showPhotos">
			<html:link page="<%="/manageExecutionCourse.do?method=editShift&amp;shiftID=" + request.getParameter("shiftID")+ "&amp;showPhotos=true&amp;executionCourseID=" + request.getParameter("executionCourseID")%>">
			    	<bean:message key="label.viewPhoto"/>
			</html:link>
		</logic:present>
		<logic:notPresent name="showPhotos">
			<html:link page="<%="/manageExecutionCourse.do?method=editShift&amp;shiftID=" + request.getParameter("shiftID")+ "&amp;executionCourseID=" + request.getParameter("executionCourseID")%>">
			    	<bean:message key="label.notViewPhoto"/>
			</html:link>
		</logic:notPresent>
		
		<table class="tstyle1">
			<tr>
				<th>
					<bean:message key="label.number"/>
				</th>
				<logic:notPresent name="showPhotos">
					<th>
						<bean:message key="label.photo" />
					</th>
				</logic:notPresent>
				<th>
					<bean:message key="label.name"/>
				</th>
				<th>
				</th>
			</tr>
			<logic:iterate id="registration" name="registrations">
				<bean:define id="registrationID" name="registration" property="idInternal"/>
				<tr>
					<td>
						<bean:write name="registration" property="person.student.number"/>
					</td>
					<logic:notPresent name="showPhotos">
						<td class="acenter">
							<bean:define id="personID" name="registration" property="person.idInternal"/>
							<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
						</td>
					</logic:notPresent>	
					<td>
						<fr:view name="registration" property="person.name"/>
					</td>
					<td>
						<a href="<%= request.getContextPath() + "/teacher/manageExecutionCourse.do?method=removeAttendsFromShift&shiftID=" + shiftID + "&registrationID=" + registrationID + "&executionCourseID=" + executionCourseID %>">
							<bean:message key="label.remove"/>
						</a>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>
	<br>
	<b><bean:message key="label.add.student.to.shift" bundle="APPLICATION_RESOURCES"/>:</b>
	<p>
	<fr:form action="<%="/manageExecutionCourse.do?method=insertStudentInShift&shiftID=" + shiftID + "&executionCourseID=" + request.getParameter("executionCourseID") %>">
		<fr:edit id="numberForm" name="personBean" schema="student.number">
			<fr:edit id="personBean" name="personBean" visible="false" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.add"/>
		</html:submit >
	</fr:form>
	</p>
</logic:present>