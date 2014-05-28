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

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />

	<bean:message key="title.edit.candidacy.qualifications" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="processId" name="process" property="externalId" />

<fr:form id="editQualificationForm" action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=addQualification&processId=" + processId %>">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	
	<bean:define id="hash" name="process" property="candidacyHashCode.value" />
	
	<p>
		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash" >
			« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
		</html:link>
	</p>
	
	<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.editQualifications" bundle="PHD_RESOURCES" /></h2>

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<logic:notPresent name="candidacyBean">
		<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">
		<logic:notEmpty name="qualificationBean">
	
			<div class="fs_form">
			<fieldset style="display: block;">
				<legend><bean:message key="title.public.phd.qualifications" bundle="PHD_RESOURCES" /></legend>
				
				<fr:edit id="qualificationBean" name="qualificationBean" schema="Public.PhdProgramCandidacyProcess.qualification">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="thlight thleft"/>
						<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="optionalMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=editQualificationsInvalid&processId=" + processId %>"/>
				</fr:edit>	
			</fieldset>
			</div>
			<p><html:submit><bean:message bundle="PHD_RESOURCES" key="label.public.phd.add.qualification"/></html:submit></p>
		</logic:notEmpty>
		
		<logic:notEmpty name="process" property="individualProgramProcess.qualifications">
			<logic:iterate id="qualification" name="process" property="individualProgramProcess.qualifications" indexId="index" >
				<p class="mtop2 mbottom05"><strong><%= index.intValue() + 1 %>.</strong></p>
				<bean:define id="qualificationId" name="qualification" property="externalId" />
				<fr:view name="qualification" schema="Phd.Qualification.view">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value="width175px,,,,"/>
					</fr:layout>
				</fr:view>
				<p class="mtop05">
					<html:link action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=removeQualification&processId=" + processId + "&qualificationId=" + qualificationId %>">
						- <bean:message key="label.remove" bundle="PHD_RESOURCES"/>
					</html:link>
				</p>
			</logic:iterate>
		</logic:notEmpty>
	</logic:present>
</logic:equal>

</fr:form>
