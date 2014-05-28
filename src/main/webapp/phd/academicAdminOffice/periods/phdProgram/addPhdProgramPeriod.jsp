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
<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.EditPhdParticipant"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%-- ### Title #### --%>
<h2><bean:message key="title.phdProgram.periods" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<p>
	<html:link action="/phdProgram.do?method=viewPhdProgramPeriods" paramId="phdProgramId" paramName="phdProgram" paramProperty="externalId" >
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>

<p>
	<strong>
		<bean:message key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriod.add.period" bundle="PHD_RESOURCES"/>
	</strong>
</p>

<bean:define id="phdProgramId" name="phdProgram" property="externalId" />

<fr:form action="<%= "/phdProgram.do?method=addPhdProgramPeriod&amp;phdProgramId=" + phdProgramId %>">
	<fr:edit id="phdProgramContextPeriodBean" name="phdProgramContextPeriodBean" visible="false" />
	
	<fr:edit id="phdProgramContextPeriodBean.edit" name="phdProgramContextPeriodBean">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdProgramContextPeriodBean">
			<fr:slot name="beginDate" required="true"/>
			<fr:slot name="endDate" />
		</fr:schema>

		<fr:layout name="tabular">
		</fr:layout>

		<fr:destination name="invalid" path="<%= "/phdProgram.do?method=addPhdProgramPeriodInvalid&amp;phdProgramId=" + phdProgramId %>"/>
		<fr:destination name="cancel" path="<%= "/phdProgram.do?method=viewPhdProgramPeriods&amp;phdProgramId=" + phdProgramId %>" />
	</fr:edit>
	
	<html:submit><bean:message key="label.submit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
</fr:form>
