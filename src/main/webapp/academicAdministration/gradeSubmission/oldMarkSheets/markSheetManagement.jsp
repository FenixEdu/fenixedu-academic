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

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.old.markSheet.management"/></h2>

<ul>
	<li><html:link action="/oldCreateMarkSheet.do?method=prepareCreateMarkSheet"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet"/></html:link></li>
	<li><html:link action="/rectifyOldMarkSheet.do?method=prepareRectifyMarkSheet"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyMarkSheet"/></html:link></li>
</ul>


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
		 schema="oldMarkSheet.search"
		 action="/oldMarkSheetManagement.do?method=searchMarkSheets">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>

	<fr:destination name="postBack" path="/oldMarkSheetManagement.do?method=prepareSearchMarkSheetPostBack"/>
	<fr:destination name="invalid" path="/oldMarkSheetManagement.do?method=prepareSearchMarkSheetInvalid"/>
	<fr:destination name="cancel" path="/oldMarkSheetManagement.do?method=prepareSearchMarkSheet" />
</fr:edit>



<logic:present name="searchResult">
<div class="mtop15">
	<p>
		<strong><bean:write name="edit" property="curricularCourseName"/></strong>
		
		<bean:define id="executionPeriodID" name="edit" property="executionPeriod.externalId" />
		<bean:define id="degreeID" name="edit" property="degree.externalId" />
		<bean:define id="degreeCurricularPlanID" name="edit" property="degreeCurricularPlan.externalId" />
		<bean:define id="curricularCourseID" name="edit" property="curricularCourse.externalId" />
	
		<bean:define id="url" name="url"/>
		<logic:equal name="edit" property="curricularCourse.canCreateMarkSheet" value="true">
		 - 
			<html:link action='<%= "/oldCreateMarkSheet.do?method=prepareCreateMarkSheetFilled" + url %>'><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet"/></html:link>
		</logic:equal>
	
		<logic:empty name="searchResult">
			<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.noMarkSheetsFound"/></em>
		</logic:empty>
	</p>
	
	<logic:notEmpty name="searchResult">

			<logic:iterate id="entry" name="searchResult" >
				<bean:define id="markSheetType" name="entry" property="key"/>
				<bean:define id="markSheetResult" name="entry" property="value"/>

				<div style="margin-left: 2em;">

					<p>
						<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" name="markSheetType" property="name" /></strong>
					</p>
				
					<fr:view name="markSheetResult" property="markSheetsSortedByEvaluationDate" schema="markSheet.search.result.list">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle4" />
							<fr:property name="link(view)" value='<%= "/oldMarkSheetManagement.do?method=viewMarkSheet" + url %>' />
							<fr:property name="key(view)" value="label.markSheet.view" />
							<fr:property name="param(view)" value="externalId/msID" />
							<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="order(view)" value="1" />
							<fr:property name="link(edit)" value='<%= "/editOldMarkSheet.do?method=prepareEditMarkSheet" + url %>' />
							<fr:property name="key(edit)" value="label.markSheet.edit" />
							<fr:property name="param(edit)" value="externalId/msID" />
							<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(edit)" value="canEdit" />
							<fr:property name="order(edit)" value="2" />														
							<fr:property name="link(delete)" value='<%= "/oldMarkSheetManagement.do?method=prepareDeleteMarkSheet" + url %>' />
							<fr:property name="key(delete)" value="label.markSheet.remove" />
							<fr:property name="param(delete)" value="externalId/msID" />
							<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(delete)" value="canEdit" />
							<fr:property name="order(delete)" value="3" />														
							<fr:property name="link(confirm)" value='<%= "/oldMarkSheetManagement.do?method=prepareConfirmMarkSheet" + url %>' />
							<fr:property name="key(confirm)" value="label.markSheet.confirm" />
							<fr:property name="param(confirm)" value="externalId/msID" />
							<fr:property name="bundle(confirm)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(confirm)" value="canConfirm" />
							<fr:property name="order(confirm)" value="4" />																																			
						</fr:layout>
					</fr:view>
				</div>
			</logic:iterate>

	</logic:notEmpty>
</div>
</logic:present>
