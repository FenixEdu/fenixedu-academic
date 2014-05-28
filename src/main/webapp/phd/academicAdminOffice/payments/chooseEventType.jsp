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
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.accounting.events.create" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

	<ul>
		<li>
			<html:link action="/phdAccountingEventsManagement.do?method=createPhdRegistrationFee" paramId="processId" paramName="process" paramProperty="externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.accounting.events.create.registration.fee"/>
			</html:link>
		</li>
		<li>
			<html:link action="/phdAccountingEventsManagement.do?method=prepareCreateInsuranceEvent" paramId="processId" paramName="process" paramProperty="externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.accounting.events.create.insurance.event"/>
			</html:link>
		</li>
		<%-- <logic:equal name="process" property="hasStartedStudies" value="true" > --%>		
		<li>
			<html:link action="/phdAccountingEventsManagement.do?method=prepareCreateGratuityEvent" paramId="processId" paramName="process" paramProperty="externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.accounting.events.create.gratuity.event"/>
			</html:link>
		</li>
		<%-- </logic:equal>--%>
		<logic:notEmpty name="process" property="thesisProcess" >
			<li>
				<html:link action="/phdAccountingEventsManagement.do?method=createPhdThesisRequestFee" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.accounting.events.create.thesis.request.fee"/>
				</html:link>
			</li>
		</logic:notEmpty>
	</ul>
	
	

<%--  ### End of Operation Area  ### --%>
