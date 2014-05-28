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

<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.phd.providers.PhdProgramsAssociatedToCoordinator"  %>
<%@page import="net.sourceforge.fenixedu.domain.phd.email.PhdProgramEmailBean"%>

<html:xhtml/>

<logic:present role="role(COORDINATOR)">

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manage.emails" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<bean:define id="phdEmailBean" name="phdEmailBean" />

<logic:equal name="phdEmailBean" property="showProgramsChoice" value="true">
	<fr:form action="/phdIndividualProgramProcess.do?method=manageEmailBeanPostback">
		<fr:edit id="phdEmailBean" name="phdEmailBean">
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramEmailBean.class.getName() %>">
				<fr:slot name="phdProgram" layout="menu-select-postback">
					<fr:property name="providerClass" value="<%= PhdProgramsAssociatedToCoordinator.class.getName() %>" />
					<fr:property name="format" value="${name}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight" />
			</fr:layout>
		</fr:edit>
	</fr:form>
</logic:equal>

<logic:empty name="phdEmailBean" property="phdProgram" >
	<p><em><bean:message bundle="PHD_RESOURCES" key="label.phd.email.chooseProgram" /></em></p>
</logic:empty>

<logic:notEmpty name="phdEmailBean" property="phdProgram" >

	<p class="mvert05">
		<html:link action="/phdIndividualProgramProcess.do?method=choosePhdEmailRecipients" paramId="phdProgramId" paramName="phdEmailBean" paramProperty="phdProgram.externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.prepare.send.email" />
		</html:link>
	</p>

	<bean:define id="program" name="phdEmailBean" property="phdProgram" /> 
	
	<logic:empty name="program" property="phdProgramEmails">
		<p>
			<em><bean:message key="message.phd.email.do.not.exist" bundle="PHD_RESOURCES" />.</em>
		</p>
	</logic:empty>
	
	<logic:notEmpty name="program" property="phdProgramEmails">
		<fr:view name="program" property="phdProgramEmails">
		
			<fr:schema bundle="PHD_RESOURCES" type="<%= net.sourceforge.fenixedu.domain.phd.email.PhdProgramEmail.class.getName() %>">
				<fr:slot name="whenCreated" layout="year-month"/>
				<fr:slot name="formattedSubject" /> 
			</fr:schema>
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight thleft" />
				
				<fr:link name="view"  label="label.view,PHD_RESOURCES"
					link="/phdIndividualProgramProcess.do?method=viewPhdEmail&phdEmailId=${externalId}"/>
					
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</logic:notEmpty>

</logic:present>
