<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2>Página do Aluno</h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>


<h3 class="mtop15 mbottom025"><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<table class="mtop025">
	<tr>
		<td>
			<fr:view name="student" schema="student.show.personAndStudentInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		      		<fr:property name="rowClasses" value="tdhl1,,,,"/>
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


<h3 class="mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="student" property="registrations" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight mtop025 asdasd"/>
		<fr:property name="columnClasses" value=",tdhl1,,"/>
		<fr:property name="linkFormat(view)" value="/student.do?method=visualizeRegistration&registrationID=${idInternal}" />
		<fr:property name="key(view)" value="link.student.visualizeRegistration"/>
		<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES"/>
		<fr:property name="visibleIf(view)" value="isForOffice"/>
		<fr:property name="contextRelative(view)" value="true"/>
	</fr:layout>
</fr:view>


<bean:define id="personId" name="student" property="person.idInternal" />
<h3 class="mbottom025"><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<ul>
	<li>
		<html:link action="<%="/payments.do?method=showEvents&personId=" + personId %>">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.currentEvents" />
		</html:link>
	</li>
	<li>
		<html:link action="<%="/payments.do?method=showEventsWithInstallments&personId=" + personId%>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.eventsWithInstallments" />
		</html:link>
	</li>
	<li>
		<html:link action="<%="/payments.do?method=showPaymentsWithoutReceipt&personId=" + personId %>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.paymentsWithoutReceipt" />
		</html:link>
	</li>
	<li>
		<html:link action="<%="/payments.do?method=showReceipts&personId=" + personId%>">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.receipts" />
		</html:link>
	</li>
</ul>









