<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><strong><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>

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

<logic:equal name="student" property="hasActiveRegistrationForOffice" value="true">
	<html:link page="/student.do?method=prepareEditPersonalData" paramId="studentID" paramName="student" paramProperty="idInternal">
		<bean:message key="link.student.editPersonalData" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</logic:equal>

<br/><br/>
<h2><strong><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="student" property="registrations" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
   		<fr:property name="columnClasses" value="listClasses,,"/>
		<fr:property name="linkFormat(view)" value="/student.do?method=visualizeRegistration&registrationID=${idInternal}" />
		<fr:property name="key(view)" value="link.student.visualizeRegistration"/>
		<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
		<fr:property name="visibleIf(view)" value="isForOffice"/>
		<fr:property name="contextRelative(view)" value="true"/>
	</fr:layout>
</fr:view>


<br/><br/>
<bean:define id="personId" name="student" property="person.idInternal" />
<h2><strong><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<table>
	<tr>
		<td>
			<html:link action="<%="/payments.do?method=showEvents&personId=" + personId %>">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currentEvents" />
			</html:link>
		</td>
	</tr>
	<tr>
		<td>
			<html:link action="<%="/payments.do?method=showEventsWithInstallments&personId=" + personId%>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.eventsWithInstallments" />
			</html:link>
		</td>
	</tr>	
	<tr>
		<td>
			<html:link action="<%="/payments.do?method=showPaymentsWithoutReceipt&personId=" + personId %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.paymentsWithoutReceipt" />
			</html:link>
		</td>
	</tr>
	<tr>
		<td>
			<html:link action="<%="/payments.do?method=showReceipts&personId=" + personId%>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.receipts" />
			</html:link>
		</td>
	</tr>
</table>

