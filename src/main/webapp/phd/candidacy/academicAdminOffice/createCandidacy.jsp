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
<h2><bean:message key="label.phd.candidacy.academicAdminOffice.createCandidacy" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<jsp:include page="createCandidacyStepsBreadcrumb.jsp?step=2"></jsp:include>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=createCandidacyBean.editPersonalInformation.simplified" />
<%--  ### End of Error Messages  ### --%>



<%--  ### Context Information  ### --%>


<%--  ### End Of Context Information  ### --%>



<fr:form action="/phdProgramCandidacyProcess.do?method=createCandidacy">

	<fr:edit id="createCandidacyBean" name="createCandidacyBean" visible="false" />
	
	<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
	<p class="mtop15 mbottom05"><strong><bean:message  key="label.phd.personalInformation" bundle="PHD_RESOURCES"/></strong></p>
	<logic:equal name="isEmployee" value="true">
		<fr:view
			name="createCandidacyBean" property="personBean"
			schema="PhdProgramCandidacyProcessBean.PersonBean.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
		<fr:edit id="createCandidacyBean.editPersonalInformation.simplified"
			name="createCandidacyBean"
			property="personBean" visible="false" />
	</logic:equal>
	<logic:notEqual name="isEmployee" value="true">
		<fr:edit id="createCandidacyBean.editPersonalInformation.simplified"
			name="createCandidacyBean"
			property="personBean"
			schema="PhdProgramCandidacyProcessBean.editPersonalInformation.simplified">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=createCandidacyInvalid" />
			
		</fr:edit>
	</logic:notEqual>
	
	<p class="mtop15 mbottom05"><strong><bean:message  key="label.phd.candidacy.academicAdminOffice.createCandidacy.phdProgramInformation" bundle="PHD_RESOURCES"/></strong></p>
	<fr:edit id="createCandidacyBean.editProgramInformation"
		name="createCandidacyBean"
		schema="PhdProgramCandidacyProcessBean.editProgramInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
			<fr:property name="sortBy" value="program.presentationName" />
		</fr:layout>
		
		<fr:destination name="select-execution-year-postback" path="/phdProgramCandidacyProcess.do?method=createCandidacyPostback" />
		<fr:destination name="migration-postback" path="/phdProgramCandidacyProcess.do?method=createCandidacyPostback" />
		<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=createCandidacyInvalid" />
		<fr:destination name="cancel" path="/phdProgramCandidacyProcess.do?method=cancelCreateCandidacy" />
	</fr:edit>
	
	
	<logic:equal name="createCandidacyBean" property="migratedProcess" value="true">
		<p class="mtop15 mbottom05"><strong><bean:message  key="label.phd.candidacy.academicAdminOffice.createCandidacy.phdStudentNumber" bundle="PHD_RESOURCES"/></strong></p>
		<p class="mtop15 mbottom05"><em><bean:message  key="message.phd.candidacy.academicAdminOffice.createCandidacy.phdStudentNumber.information" bundle="PHD_RESOURCES"/></em></p>
		
		<fr:edit id="createCandidacyBean.migratedProcess"
			name="createCandidacyBean">
			<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean">
				<fr:slot name="phdStudentNumber" required="true" /> 
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			
			<fr:destination name="invalid" path="/phdProgramCandidacyProcess.do?method=createCandidacyInvalid" />
			
		</fr:edit>
	</logic:equal>
	
	<%--  ### End of Operation Area  ### --%>
	
	
	<%--  ### Buttons (e.g. Submit)  ### --%>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" ><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
	
	<%--  ### End of Buttons (e.g. Submit)  ### --%>
	
	</fr:form>
