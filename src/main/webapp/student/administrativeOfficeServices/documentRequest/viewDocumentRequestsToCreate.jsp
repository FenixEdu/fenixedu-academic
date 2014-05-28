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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<p class="mtop15"><strong><bean:message key="label.documentRequests.confirmation" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>

<logic:messagesPresent message="true">
	<p>
		<span class="error"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<logic:notEmpty name="warningsToReport">
	<p class="warning0"><bean:message key="document.request.warnings.title"/></p>
	<ul>
		<logic:iterate id="warningToReport" name="warningsToReport">
			<li><bean:message name="warningToReport"/></li>
		</logic:iterate>
	</ul>
</logic:notEmpty>


<fr:form action="/documentRequest.do?method=create">
<fr:edit schema="DocumentRequestCreateBean.chooseDocumentRequestType" name="documentRequestCreateBean" visible="false"
	type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:edit>

<logic:present name="additionalInformationSchemaName">
	<bean:define id="additionalInformationSchemaName" name="additionalInformationSchemaName" type="java.lang.String"/>
	<fr:edit name="documentRequestCreateBean" schema="<%= additionalInformationSchemaName + ".view" %>" visible="false"
		type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean" />
</logic:present>

<fr:edit name="documentRequestCreateBean" schema="DocumentRequestCreateBean.purposes"  visible="false"
	type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean" />

<fr:view schema="DocumentRequestCreateBean.chooseDocumentRequestType" name="documentRequestCreateBean" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thright thlight mvert0"/>
		<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
	</fr:layout>	
</fr:view>


<logic:present name="additionalInformationSchemaName">
	<bean:define id="additionalInformationSchemaName" name="additionalInformationSchemaName" type="java.lang.String"/>	
	<fr:view name="documentRequestCreateBean" schema="<%= additionalInformationSchemaName + ".view" %>" 
		type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thright thlight mvert0"/>
			<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
		</fr:layout>	
	</fr:view>		
</logic:present>


<fr:view name="documentRequestCreateBean" schema="DocumentRequestCreateBean.purposes" 
	type="net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thright thlight mvert0"/>
		<fr:property name="columnClasses" value="width14em,width20em,tdclear tderror1"/>
	</fr:layout>	
</fr:view>

<p class="mtop15">
	<html:submit><bean:message key="button.confirm" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
</p>
	
</fr:form>
