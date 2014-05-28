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

<%@page import="net.sourceforge.fenixedu.domain.phd.individualProcess.activities.SendPhdEmail"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmail" %>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manage.emails" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="process" name="process" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<p>
	<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + processId %>">
		« <bean:message key="label.back" bundle="PHD_RESOURCES" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%-- ### Operational Area ### --%>
<phd:activityAvailable process="<%= process %>" activity="<%= SendPhdEmail.class %>">
	<html:link action="/phdIndividualProgramProcess.do?method=prepareSendPhdEmail" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.emails.create"/>
	</html:link>

</phd:activityAvailable>


<%-- ### History Area ### --%>
<logic:empty name="process" property="phdIndividualProgramProcessEmails">
	<p>
		<em><bean:message key="message.phd.email.do.not.exist" bundle="PHD_RESOURCES" />.</em>
	</p>
</logic:empty>

<logic:notEmpty name="process" property="phdIndividualProgramProcessEmails">
	<fr:view name="process" property="phdIndividualProgramProcessEmails">
	
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdIndividualProgramProcessEmail.class.getName() %>">
			<fr:slot name="whenCreated" layout="year-month"/>
			<fr:slot name="formattedSubject" /> 
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:link name=""  label="label.view,PHD_RESOURCES"
				link="<%= "/phdIndividualProgramProcess.do?method=viewPhdEmail&phdEmailId=${externalId}&processId=" + processId %>"/>
	
		</fr:layout>
	</fr:view>
</logic:notEmpty>
