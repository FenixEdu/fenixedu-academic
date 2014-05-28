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
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/education/fct-phd-programmes/">FCT Doctoral Programmes</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="editQualificationForm" action="/applications/epfl/phdProgramCandidacyProcess.do">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodId" name="method" value="addQualificationToExistingCandidacy" />
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>	
	
	<a href="#" onclick="javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodId').value='backToViewCandidacy';document.getElementById('editQualificationForm').submit();">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>
	<br/>
	<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.qualifications" bundle="PHD_RESOURCES" /></h2>

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
				<legend><bean:message key="label.public.phd.add.qualification" bundle="PHD_RESOURCES" /></legend>
				<p class="mtop05"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
				
				<fr:edit id="qualificationBean" name="qualificationBean" schema="Public.PhdProgramCandidacyProcess.qualification">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="thlight thleft"/>
						<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=editQualificationsInvalid" />
				</fr:edit>	
			</fieldset>
			</div>
			<p><html:submit onclick="document.getElementById('methodId').value='addQualificationToExistingCandidacy';" bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.public.phd.add.qualification"/></html:submit></p>
			<br/>
		</logic:notEmpty>
		
		<logic:notEmpty name="candidacyBean" property="candidacyHashCode.individualProgramProcess.qualifications">
			<logic:iterate id="qualification" name="candidacyBean" property="candidacyHashCode.individualProgramProcess.qualifications" indexId="index" >
				<p class="mtop2 mbottom05"><strong><%= index.intValue() + 1 %>.</strong></p>
				<bean:define id="qualificationId" name="qualification" property="externalId" />
				<fr:view name="qualification" schema="Phd.Qualification.view">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value="width175px,,,,"/>
					</fr:layout>
				</fr:view>
				<p class="mtop05"><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + qualificationId + "; document.getElementById(\"methodId\").value=\"removeQualificationFromExistingCandidacy\"; document.getElementById(\"editQualificationForm\").submit();" %>' href="#" >- <bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
			</logic:iterate>
		</logic:notEmpty>
		<%-- <p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"methodId\").value=\"prepareAddQualificationToExistingCandidacy\"; document.getElementById(\"editQualificationForm\").submit();" %>' href="#" >+ <bean:message key="label.add" bundle="PHD_RESOURCES"/></a></p> --%>
	</logic:present>
</logic:equal>

</fr:form>
