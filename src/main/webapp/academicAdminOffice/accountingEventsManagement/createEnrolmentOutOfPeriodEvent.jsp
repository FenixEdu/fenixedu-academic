<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><strong><bean:message
	key="label.accountingEvents.management.createEvents"
	bundle="ACADEMIC_OFFICE_RESOURCES" /></strong></h2>
	

<bean:define id="registration" name="accountingEventCreateBean" property="studentCurricularPlan.registration" />	
<logic:present name="registration" property="ingression">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" schema="student.registrationDetail" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
		</fr:layout>
	</fr:view>
	</logic:present>
	
	<logic:notPresent name="registration" property="ingression">
	<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="registration" schema="student.registrationsWithStartData" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
		</fr:layout>
	</fr:view>
</logic:notPresent>

<br/>

<html:messages id="message" message="true" property="error"
	bundle="APPLICATION_RESOURCES">
	<span class="error"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<html:messages id="message" message="true" property="success" 
	bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="success0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:hasMessages type="conversion" for="accountingEventCreateBean">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message show="label"/>:<fr:message /></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="scpID" name="accountingEventCreateBean" property="studentCurricularPlan.externalId" />
<fr:edit id="accountingEventCreateBean"
	name="accountingEventCreateBean"
	schema="EnrolmentOutOfPeriodEventCreateBean.edit"
	action="/accountingEventsManagement.do?method=createEnrolmentOutOfPeriodEvent">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight" />
		<fr:property name="columnClasses" value="nowrap," />
		<fr:destination name="invalid"
			path="/accountingEventsManagement.do?method=prepareCreateEnrolmentOutOfPeriodInvalid" />
		<fr:destination name="executionYearPostBack" path="/accountingEventsManagement.do?method=prepareCreateEnrolmentOutOfPeriodPostback"/>
		<fr:destination name="cancel" path="<%="/accountingEventsManagement.do?method=prepare&scpID=" + scpID %>" />
	</fr:layout>
</fr:edit>
