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

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet"/></h2>

<p class="breadcumbs"><span><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet.step.one"/></span> &gt; <span class="actual"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet.step.two"/></span></p>

<fr:view name="edit" 
		schema="markSheet.view.step2">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thright thlight"/>
	        <fr:property name="columnClasses" value=",,"/>
		</fr:layout>
</fr:view>


<logic:messagesPresent message="true">
	<ul>
	<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
		<li><span class="error0"><bean:write name="messages" /></span></li>
	</html:messages>
	</ul>
	<br/>
</logic:messagesPresent>

<fr:hasMessages for="edit-enrolments">
	<ul>
	<fr:messages>
		<li><fr:message/></li>
	</fr:messages>
	</ul>
</fr:hasMessages>

<fr:form action="/createMarkSheet.do">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="markSheetManagementForm" property="method" value="createMarkSheetStepTwo" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.epID" name="markSheetManagementForm" property="epID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dID" name="markSheetManagementForm" property="dID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dcpID" name="markSheetManagementForm" property="dcpID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ccID" name="markSheetManagementForm" property="ccID"  />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.msID" name="markSheetManagementForm" property="msID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tn" name="markSheetManagementForm" property="tn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ed" name="markSheetManagementForm" property="ed"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mss" name="markSheetManagementForm" property="mss" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mst" name="markSheetManagementForm" property="mst" />

	<fr:edit id="edit-invisible" name="edit" visible="false"/>

	<p class="mtop15 mbottom05"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.students.capitalized"/>:</strong></p>
	
	<fr:edit id="edit-enrolments" name="edit" property="enrolmentEvaluationBeans" 
			 schema="markSheet.create.step.two" layout="tabular-editable">
		<fr:layout>
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
			<fr:property name="sortBy" value="enrolment.studentCurricularPlan.student.number"/>
		</fr:layout>
		<fr:destination name="invalid" path="/createMarkSheet.do?method=createMarkSheetStepTwoInvalid"/>
	</fr:edit>

	<logic:notEmpty name="edit" property="impossibleEnrolmentEvaluationBeans">
		<br/>
		<p class="mtop15 mbottom05"><strong><span class="error0"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.studentsWithImpossibleEnrolments" />:</span></strong></p>
		<fr:edit id="edit-impossible-enrolments" name="edit" property="impossibleEnrolmentEvaluationBeans" 
			schema="markSheet.create.step.two" layout="tabular-editable">
			<fr:layout>
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
				<fr:property name="sortBy" value="enrolment.studentCurricularPlan.student.number"/>
			</fr:layout>
		<fr:destination name="invalid" path="/createMarkSheet.do?method=createMarkSheetStepTwoInvalid"/>
	</fr:edit>
	<br/>
	</logic:notEmpty>

	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.submit" /></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='backSearchMarkSheet';"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.cancel"/></html:cancel>
</fr:form>
