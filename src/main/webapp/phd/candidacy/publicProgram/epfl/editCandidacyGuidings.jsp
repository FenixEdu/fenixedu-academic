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

<fr:form id="editGuidingsForm" action="/applications/epfl/phdProgramCandidacyProcess.do">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodId" name="method" value="" />
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>	
	
	<a href="#" onclick="javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodId').value='backToViewCandidacy';document.getElementById('editGuidingsForm').submit();">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>
	<br/>
	<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.guidings" bundle="PHD_RESOURCES"/> <span style="font-weight: normal; font-size: 13px; color: #777;">(<bean:message key="title.public.phd.if.applicable" bundle="PHD_RESOURCES"/>)</span></h2>

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<logic:notPresent name="candidacyBean">
		<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">

		<logic:notEmpty name="candidacyBean" property="guidings">
			<div class="fs_form">
			<fieldset style="display: block;">
				<legend><bean:message key="label.phd.public.candidacy.createCandidacy.edit.guidings" bundle="PHD_RESOURCES"/></legend>
				<p class="mtop05"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
			
				<logic:iterate id="guidingBean" name="candidacyBean" property="guidings" indexId="index">
					<strong><%= index.intValue() + 1 %>.</strong>
					<bean:define id="guidingId" name="index" />
					<fr:edit id="<%= "guidingBean" + guidingId %>" name="guidingBean" schema="Public.PhdProgramGuidingBean.edit">
						<fr:layout name="tabular-editable">
							<fr:property name="classes" value="thlight thleft"/>
							<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
							<fr:property name="requiredMarkShown" value="true" />
						</fr:layout>
						<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=editCandidacyGuidingsInvalid" />
						<fr:destination name="cancel" path="/applications/epfl/phdProgramCandidacyProcess.do?method=prepareEditCandidacyGuidings" />
					</fr:edit>
					<bean:size id="guidingsCount" name="candidacyBean" property="guidings" />
					<logic:greaterThan name="guidingsCount" value="1">
						<p class="mtop05"><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + guidingId + "; document.getElementById(\"methodId\").value=\"removeGuidingFromCreationList\"; document.getElementById(\"editGuidingsForm\").submit();" %>' href="#" >- <bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
					</logic:greaterThan>
				</logic:iterate>
			
			</fieldset>
			</div>
			<p><html:submit onclick="document.getElementById('methodId').value='addGuidingToExistingCandidacy';" bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="button.add"/></html:submit></p>
		</logic:notEmpty>
		
		<logic:notEmpty name="candidacyBean" property="candidacyHashCode.individualProgramProcess.guidings">
			<logic:iterate id="guiding" name="candidacyBean" property="candidacyHashCode.individualProgramProcess.guidings" indexId="index" >
				<p class="mtop2 mbottom05"><strong><%= index.intValue() + 1 %>.</strong></p>
				<bean:define id="guidingId" name="guiding" property="externalId" />
				<fr:view name="guiding" schema="Public.PhdProgramGuiding.view">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value="width175px,,,,"/>
					</fr:layout>
				</fr:view>
				<p class="mtop05"><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + guidingId + "; document.getElementById(\"methodId\").value=\"removeGuidingFromExistingCandidacy\"; document.getElementById(\"editGuidingsForm\").submit();" %>' href="#" >- <bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
			</logic:iterate>
		</logic:notEmpty>
		<%-- <p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"methodId\").value=\"prepareAddGuidingToExistingCandidacy\"; document.getElementById(\"editGuidingsForm\").submit();" %>' href="#" >+ <bean:message key="label.add" bundle="PHD_RESOURCES"/></a></p> --%>
			
	</logic:present>
</logic:equal>

</fr:form>
