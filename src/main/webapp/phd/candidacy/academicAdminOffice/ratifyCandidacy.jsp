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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.ratifyCandidacy" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="individualProgramProcessId" name="process" property="individualProgramProcess.externalId" />

<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProgramProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<table>
  <tr style="vertical-align: top;">
    <td style="width: 55%">
    	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process" property="individualProgramProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
    <td>
	    <strong><bean:message  key="label.phd.candidacyProcess" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdProgramCandidacyProcess.view" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
    </td>
  </tr>
</table>


<%--  ### End Of Context Information  ### --%>

<bean:define id="processId" name="process" property="externalId" />
<br/>

<%--  ### Documents  ### --%>
<strong><bean:message  key="label.phd.candidacy.review.documents" bundle="PHD_RESOURCES"/></strong>
<br/>
<logic:empty name="process" property="candidacyReviewDocuments">
	<bean:message  key="label.phd.noDocuments" bundle="PHD_RESOURCES"/>
</logic:empty>
<logic:notEmpty name="process" property="candidacyReviewDocuments">
	<fr:view schema="PhdProgramProcessDocument.review.document" name="process" property="candidacyReviewDocuments">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:property name="linkFormat(view)" value="${downloadUrl}"/>
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="PHD_RESOURCES"/>
			<fr:property name="order(view)" value="0" />
			<fr:property name="hasContext(view)" value="false" />
			<fr:property name="contextRelative(view)" value="false" />
			
			<fr:property name="sortBy" value="documentType=asc" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<%--  ### End Of Documents  ### --%>
<br/><br/>

<bean:define id="individualProgramProcess" name="process" property="individualProgramProcess" />
<bean:define id="migratedProcess" name="individualProgramProcess" property="phdConfigurationIndividualProgramProcess.migratedProcess" type="Boolean"/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<fr:form action="<%= "/phdProgramCandidacyProcess.do?method=ratifyCandidacy&processId=" + processId.toString() %>" encoding="multipart/form-data">
  	
	<fr:edit id="ratifyCandidacyBean"
	name="ratifyCandidacyBean">
		<fr:schema type="net.sourceforge.fenixedu.domain.phd.candidacy.RatifyCandidacyBean" bundle="PHD_RESOURCES">
			<fr:slot name="whenRatified" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
			<fr:slot name="maxDaysToFormalizeRegistration" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
			<fr:slot name="ratificationFile.file" required="<%= !migratedProcess.booleanValue() %>">
				<fr:property name="fileNameSlot" value="ratificationFile.filename"/>
				<fr:property name="size" value="20"/>
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="layout">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=prepareRatifyCandidacyInvalid" />
		</fr:layout>
</fr:edit>
	
<%--  ### Buttons (e.g. Submit)  ### --%>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
<%--  ### End of Buttons (e.g. Submit)  ### --%>

</fr:form>

<br/><br/>


<%--  ### End of Operation Area  ### --%>
