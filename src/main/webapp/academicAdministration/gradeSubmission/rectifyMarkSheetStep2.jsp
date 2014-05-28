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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyMarkSheet"/></h2>

<p class="breadcumbs"><span><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyMarkSheet.step.one"/></span> &gt; <span class="actual"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyMarkSheet.step.two"/></span></p>

<fr:view name="rectifyBean" property="markSheet" schema="degreeAdministrativeOffice.markSheet.view">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
	</fr:layout>
</fr:view>

<h3><bean:write name="rectifyBean" property="enrolmentEvaluation.enrolment.studentCurricularPlan.student.person.name"/> (<bean:write name="rectifyBean" property="enrolmentEvaluation.enrolment.studentCurricularPlan.student.number"/>)</h3>

<html:errors/>

<logic:messagesPresent message="true">
	<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
		<span class="error0"><bean:write name="messages" /></span>
	</html:messages>
</logic:messagesPresent>

<fr:form action="/rectifyMarkSheet.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="markSheetManagementForm" property="method" value="rectifyMarkSheetStepTwo" />
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.epID" name="markSheetManagementForm" property="epID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dID" name="markSheetManagementForm" property="dID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dcpID" name="markSheetManagementForm" property="dcpID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ccID" name="markSheetManagementForm" property="ccID"  />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.msID" name="markSheetManagementForm" property="msID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tn" name="markSheetManagementForm" property="tn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ed" name="markSheetManagementForm" property="ed"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mss" name="markSheetManagementForm" property="mss" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mst" name="markSheetManagementForm" property="mst" />
	
	<bean:define id="evaluationID" name="rectifyBean" property="enrolmentEvaluation.externalId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationID" name="markSheetManagementForm" property="evaluationID" value="<%= evaluationID.toString() %>"  />

	<fr:edit id="step2" nested="true" name="rectifyBean" schema="markSheet.rectify.two">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright"/>
		    <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>

	<p>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="message.markSheet.rectify"/>
	</p>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectify"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.cancel"/></html:cancel>
	</p>
</fr:form>