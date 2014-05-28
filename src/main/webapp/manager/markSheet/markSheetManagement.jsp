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

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.markSheet.management"/></h2>

<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<h3 class="mbottom025"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.searchMarkSheet"/></h3>

<fr:edit id="search"
		 name="edit"
		 type="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean"
		 schema="markSheet.base.manager"
		 action="/markSheetManagement.do?method=searchConfirmedMarkSheets">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>

	<fr:destination name="postBack" path="/markSheetManagement.do?method=prepareSearchMarkSheetPostBack"/>
	<fr:destination name="invalid" path="/markSheetManagement.do?method=prepareSearchMarkSheetInvalid"/>
	<fr:destination name="cancel" path="/markSheetManagement.do?method=prepareSearchMarkSheet" />
</fr:edit>



<logic:present name="searchResult">
<div class="mtop15">
	<p>
		<strong><bean:write name="edit" property="curricularCourse.name"/></strong>
		
		<bean:define id="executionPeriodID" name="edit" property="executionPeriod.externalId" />
		<bean:define id="degreeID" name="edit" property="degree.externalId" />
		<bean:define id="degreeCurricularPlanID" name="edit" property="degreeCurricularPlan.externalId" />
		<bean:define id="curricularCourseID" name="edit" property="curricularCourse.externalId" />
	
		<bean:define id="url" name="url"/>	
	
		<logic:empty name="searchResult">
			<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.noMarkSheetsFound"/></em>
		</logic:empty>
	</p>
	
	<logic:notEmpty name="searchResult">
	
		<fr:view name="searchResult" schema="markSheet.manager.confirmed.list">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
				<fr:property name="link(removeGrades)" value='<%= "/markSheetManagement.do?method=listMarkSheet" + url %>'/>
				<fr:property name="key(removeGrades)" value="label.remove.grades"/>
				<fr:property name="param(removeGrades)" value="externalId/msID"/>				
				<fr:property name="bundle(removeGrades)" value="ACADEMIC_OFFICE_RESOURCES" />				
			</fr:layout>
		</fr:view>
		
	</logic:notEmpty>
</div>
</logic:present>
