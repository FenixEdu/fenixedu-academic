<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ page import="org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType" %>

<html:xhtml/>

<h2><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
<h3 class="mtop15"><bean:message key="label.documentRequests.confirmation" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<logic:messagesPresent message="true">
	<span class="error"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
</logic:messagesPresent>

<logic:notEmpty name="warningsToReport">
	<p class="warning0"><bean:message key="document.request.warnings.title"/></p>
	<ul>
		<logic:iterate id="warningToReport" name="warningsToReport">
			<li><bean:message name="warningToReport"/></li>
		</logic:iterate>
	</ul>
</logic:notEmpty>


<fr:form action="/documentRequestsManagement.do?method=create">

	<fr:edit 
		visible="false"
		name="documentRequestCreateBean" 
		type="org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean"
		schema="DocumentRequestCreateBean.chooseDocumentRequestType"/>

	<fr:view schema="DocumentRequestCreateBean.chooseDocumentRequestType" name="documentRequestCreateBean">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mvert0"/>
			<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
		</fr:layout>	
	</fr:view>
	
	<logic:present name="additionalInformationSchemaName">
		<bean:define id="additionalInformationSchemaName" name="additionalInformationSchemaName" type="java.lang.String"/>	
		<fr:view name="documentRequestCreateBean" schema="<%= additionalInformationSchemaName + ".view" %>" 
			type="org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mvert0"/>
				<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
			</fr:layout>	
		</fr:view>		
	</logic:present>
	
	<logic:equal name="documentRequestCreateBean" property="hasCycleTypeDependency" value="true">
	    <fr:view name="documentRequestCreateBean" schema="DocumentRequestCreateBean.requestedCycle"
	        type="org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle4 thright thlight mvert0"/>
                <fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
            </fr:layout>    
	    </fr:view>
	</logic:equal>
	
	<logic:equal name="documentRequestCreateBean" property="isForProgramConclusionPurposes" value="true">
	    <fr:view name="documentRequestCreateBean" schema="DocumentRequestCreateBean.programConclusion"
	        type="org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle4 thright thlight mvert0"/>
                <fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
            </fr:layout>
	    </fr:view>
	</logic:equal>
	
	<logic:equal name="documentRequestCreateBean" property="hasPurposeNeed" value="true">
		<fr:view name="documentRequestCreateBean" schema="DocumentRequestCreateBean.purposes" 
			type="org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mvert0"/>
				<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
			</fr:layout>	
		</fr:view>
	</logic:equal>
	
	<logic:present name="documentRequestCreateBean" property="chosenServiceRequestType.documentRequestType">
		<logic:equal name="documentRequestCreateBean" property="chosenServiceRequestType.documentRequestType.canBeFreeProcessed" value="true">
			<fr:view name="documentRequestCreateBean" schema="DocumentRequestCreateBean.freeProcessed" 
				type="org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mvert0"/>
					<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
				</fr:layout>	
			</fr:view>	
		</logic:equal>
	</logic:present>
	
	<p class="mtop15">
		<html:submit><bean:message key="button.confirm" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</p>
	
</fr:form>
