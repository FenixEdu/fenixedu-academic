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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/><br/>
</html:messages>

<h2><strong><bean:message key="label.candidate.data" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="candidacy" schema="candidacy.show.candidady">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.candidacy.title.detail" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<logic:empty name="candidacy" property="registration">
	<fr:view name="candidacy" schema="candidacy.short" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>
</logic:empty>
<logic:notEmpty name="candidacy" property="registration">
	<fr:view name="candidacy" schema="candidacy.short.withNumber" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<h2><strong><bean:message key="label.candidacy.title.activeSituation" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="candidacy" property="activeCandidacySituation" schema="candidacySituation.full" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.candidacy.title.documents" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="candidacyDocuments" schema="candidacyDocuments.view" >
	<fr:layout name="tabular" >
		<fr:property name="linkFormat(download)" value="${candidacyDocument.file.downloadUrl}"/>
		<fr:property name="key(download)" value="link.common.download"/>
		<fr:property name="bundle(download)" value="APPLICATION_RESOURCES"/>
		<fr:property name="visibleIf(download)" value="isFileUploaded"/>
		<fr:property name="contextRelative(download)" value="false"/>
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>			
	</fr:layout>
</fr:view>

<bean:define id="candidacy_number" name="candidacy" property="number"/>
<h2><strong><bean:message key="label.candidacy.title.operations" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<table class="tstyle4">
	<logic:equal name="candidacy" property="activeCandidacySituation.canRegister" value="true">
		<tr>
			<td class="listClasses">
				<html:link action="<%= "/dfaCandidacy.do?method=prepareRegisterCandidacy&candidacyNumber=" + candidacy_number.toString() %>">
					<bean:message key="link.candidacy.registerCandidacy" bundle="ADMIN_OFFICE_RESOURCES"/>				
				</html:link>					
			</td>
		</tr>
	</logic:equal>
	<tr>
		<td class="listClasses">
			<html:link action="<%= "/dfaCandidacy.do?method=showCandidacyValidateData&candidacyNumber=" + candidacy_number.toString() %>">
				<bean:message key="link.candidacy.viewValidateCandidateData" bundle="ADMIN_OFFICE_RESOURCES"/>				
			</html:link>					
		</td>
	</tr>	
	<logic:equal name="candidacy" property="concluded" value="false">
		<tr>
			<td class="listClasses">
				<html:link action="<%= "/dfaCandidacy.do?method=prepareAlterCandidacyData&candidacyNumber=" + candidacy_number.toString() %>">
					<bean:message key="link.candidacy.alterCandidateData" bundle="ADMIN_OFFICE_RESOURCES"/>				
				</html:link>					
			</td>	
		</tr>			
	</logic:equal>
	<tr>
		<td class="listClasses">
			<html:link action="/academicAdministration/payments.do?method=showOperations" paramName="candidacy" paramProperty="person.externalId" paramId="personId" 
			module="/academicAdminOffice">
				<bean:message bundle="ADMIN_OFFICE_RESOURCES" key="label.payments.management" />
			</html:link>
		</td>	
	</tr>
	<logic:equal name="candidacy" property="concluded" value="false">
		<tr>
			<td class="listClasses">
			
				<bean:define id="deleteConfirm">
					return confirm('<bean:message bundle="ADMIN_OFFICE_RESOURCES" key="message.confirm.cancel.Candidacy"/>')
				</bean:define>		
				<html:link action="<%= "/dfaCandidacy.do?method=cancelCandidacy&candidacyNumber=" + candidacy_number.toString() %>" onclick="<%= deleteConfirm %>">
					<bean:message key="link.candidacy.cancelCandidacy" bundle="ADMIN_OFFICE_RESOURCES"/>
				</html:link>					
			</td>
		</tr>	
	</logic:equal>
</table>
