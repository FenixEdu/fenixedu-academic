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
<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="individualProgramProcess.externalId">
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

<html:link action="/phdThesisProcess.do?method=prepareCreateConclusionProcess" paramId="processId" paramName="processId">
	<bean:message key="link.phd.conclusionProcess.create" bundle="PHD_RESOURCES" />
</html:link>

<logic:empty name="process" property="individualProgramProcess.phdConclusionProcesses">
	<p><bean:message key="message.phd.conclusionProcess.empty" bundle="PHD_RESOURCES" /></p>
</logic:empty>

<logic:notEmpty name="process" property="individualProgramProcess.phdConclusionProcesses">
	<fr:view name="process" property="individualProgramProcess.phdConclusionProcesses">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess">
			<fr:slot name="version" />
			<fr:slot name="whenCreated" />
			<fr:slot name="conclusionDate" />
			<fr:slot name="finalGrade.localizedName" key="label.net.sourceforge.fenixedu.domain.phd.conclusion.PhdConclusionProcess.finalGrade" bundle="PHD_RESOURCES"/>
			<fr:slot name="thesisEctsCredits" />

		<logic:notEqual name="process" property="individualProgramProcess.candidacyProcess.studyPlanExempted" value="true">
			<fr:slot name="studyPlanEctsCredits" />
		</logic:notEqual>


		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>	
	</fr:view>
</logic:notEmpty>
