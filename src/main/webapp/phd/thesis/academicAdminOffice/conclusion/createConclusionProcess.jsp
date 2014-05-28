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

<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="title.phd.conclusionProcesses" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdThesisProcess.do?method=listConclusionProcesses" paramId="processId" paramName="processId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process" property="individualProgramProcess">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<%--  ### End Of Context Information  ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<fr:form action="<%= "/phdThesisProcess.do?method=createConclusionProcess&amp;processId=" + processId %>">
	<fr:edit id="phdConclusionProcessBean" name="phdConclusionProcessBean" visible="false" />
	
	<logic:empty name="phdProgramInformation">
		<div class="warning">
			<bean:message bundle="PHD_RESOURCES" key="message.phdConclusionProcess.phdProgramInformation.for.conclusion.date.inexistent" />
		</div>
	</logic:empty>
	
	<logic:notEmpty name="phdProgramInformation">
		<bean:define id="conclusionDate" name="conclusionDateForPhdInformation" type="org.joda.time.LocalDate" />
		
		<bean:define id="exempted" name="phdConclusionProcessBean" property="phdIndividualProgramProcess.studyPlan.exempted" />
		
		<logic:equal value="true" name="exempted">
			<p><strong><bean:message key="message.phdConclusionProcess.phdProgramInformation.for.this.conclusion.date" bundle="PHD_RESOURCES" arg0="<%= conclusionDate.toString("dd/MM/yyyy") %>"/></strong></p>
		</logic:equal>
		
		<logic:equal value="false" name="exempted">
			<p><strong><bean:message key="message.phdConclusionProcess.phdProgramInformation.for.this.registration.conclusion.date" bundle="PHD_RESOURCES"  arg0="<%= conclusionDate.toString("dd/MM/yyyy") %>"/></strong></p>
		</logic:equal>
		
		<fr:view name="phdProgramInformation">
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdProgramInformation" bundle="PHD_RESOURCES">
				<fr:slot name="beginDate" />
				<fr:slot name="minThesisEctsCredits" />
				<fr:slot name="maxThesisEctsCredits" />
				<fr:slot name="minStudyPlanEctsCredits" />
				<fr:slot name="maxStudyPlanEctsCredits" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>		
		</fr:view>
	</logic:notEmpty>
	
	<fr:edit id="phdConclusionProcessBean.edit" name="phdConclusionProcessBean" >
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcessBean">
			<fr:slot name="conclusionDate" required="true" />
			<fr:slot name="grade" required="true" />
			<fr:slot name="thesisEctsCredits" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
			</fr:slot>
			
		<logic:notEqual name="process" property="individualProgramProcess.candidacyProcess.studyPlanExempted" value="true">
			<fr:slot name="studyPlanEctsCredits" required="true">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DoubleValidator" />
			</fr:slot>
		</logic:notEqual>
		
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=createConclusionProcessInvalid&amp;processId=" + processId %>" />
		<fr:destination name="cancel" path="<%= "/phdThesisProcess.do?method=listConclusionProcesses&amp;processId=" + processId %>" />
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /> </html:cancel>	
</fr:form>
