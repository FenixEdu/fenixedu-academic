<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><strong><bean:message key="link.students" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>


<table >
	<tr>
		<td>
			<fr:view name="student" schema="student.show.personAndStudentInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4"/>
			      		<fr:property name="columnClasses" value="listClasses,,"/>
				</fr:layout>
			</fr:view>
		</td>
		<td>
			<bean:define id="personID" name="student" property="person.idInternal"/>
			<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
		</td>
	</tr>
</table>

<html:link action="">
	<bean:message key="link.edit.personal.data" bundle="ACADEMIC_OFFICE_RESOURCES"/>
</html:link>
		
		

