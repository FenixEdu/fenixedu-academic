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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPhdParticipant"%>

<%-- ### Title #### --%>
<h2><bean:message key="title.phd.candidacy.periods" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdCandidacyPeriodManagement.do?method=list">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
 
<p><strong><bean:message  key="title.phd.candidacy.period.create" bundle="PHD_RESOURCES"/></strong></p>

<fr:form action="/phdCandidacyPeriodManagement.do?method=createPhdCandidacyPeriod">
	<fr:edit id="phdCandidacyPeriodBean" name="phdCandidacyPeriodBean" visible="false" />
	
	<fr:edit id="phdCandidacyPeriodBean.create" name="phdCandidacyPeriodBean">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyPeriodBean">
			<fr:slot name="type" required="true" />
			<fr:slot name="executionYear" layout="menu-select" required="true">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.ExecutionYearsProvider" />
				<fr:property name="format" value="${name}" />
			</fr:slot>
			<fr:slot name="start" required="true" />
			<fr:slot name="end" required="true" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="/phdCandidacyPeriodManagement.do?method=createPhdCandidacyPeriodInvalid" />
		<fr:destination name="cancel" path="/phdCandidacyPeriodManagement.do?method=list" />
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" ><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
	
</fr:form>
