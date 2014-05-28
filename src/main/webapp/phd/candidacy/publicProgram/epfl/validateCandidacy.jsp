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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/education/fct-phd-programmes/">FCT Doctoral Programmes</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>
<h2><bean:message key="label.phd.public.submit.candidacy" bundle="PHD_RESOURCES" /></h2>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="validateCandidacyForm" action="/applications/epfl/phdProgramCandidacyProcess.do">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodForm" name="method" value="validateCandidacy" />
	
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<style>
	.warning0 {
	background-color: #fbf8cc;
	/*color: #805500;*/
	padding: 0.5em 1em;
	}
	</style>
	
	<logic:messagesPresent message="true" property="validation">
		<div class="warning0 mvert1">
			<p class="mvert05">Before you submit the aplication you must:</p>
			<ul class="mvert05">
				<html:messages id="messages" message="true" bundle="PHD_RESOURCES" property="validation">
					<li><bean:write name="messages" /></li>
				</html:messages>
			</ul>
		</div>
	</logic:messagesPresent>
	
	<logic:messagesNotPresent message="true" property="validation">
		<p>After you submit your application you can not change the information. Do you want to submit now?</p>
	</logic:messagesNotPresent>
	
	<p>
		<logic:messagesPresent message="true" property="validation">
			<html:submit onclick="javascript:document.getElementById('methodForm').value='backToViewCandidacy';javascript:document.getElementById('validateCandidacyForm').submit();" bundle="HTMLALT_RESOURCES" altKey="submit.submit">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></html:submit>
		</logic:messagesPresent>
		<logic:messagesNotPresent message="true" property="validation">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.phd.public.submit.candidacy"/></html:submit>
			<html:submit onclick="javascript:document.getElementById('methodForm').value='backToViewCandidacy';javascript:document.getElementById('validateCandidacyForm').submit();" bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:submit>
		</logic:messagesNotPresent>
	</p>
</logic:equal>
</fr:form>
