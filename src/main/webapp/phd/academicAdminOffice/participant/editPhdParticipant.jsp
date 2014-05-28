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
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdParticipant"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.view.participants" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="process" name="process" />
<bean:define id="processId" name="process" property="externalId" />

<p>
	<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
		« <bean:message bundle="PHD_RESOURCES" key="label.back" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>
 
<p><strong><bean:message key="label.phd.edit.participant" bundle="PHD_RESOURCES"/></strong></p>


<fr:form action="<%= "/phdIndividualProgramProcess.do?method=editPhdParticipant&amp;processId=" + processId %>" >
		<fr:edit id="phdParticipantBean" name="phdParticipantBean" visible="false" />
	
		<fr:edit name="phdParticipantBean">
			<logic:equal name="phdParticipantBean" property="participant.internal" value="true">
				<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.InternalPhdParticipant">
					<fr:slot name="title" />
					<fr:slot name="category" />
					<fr:slot name="workLocation" />
					<fr:slot name="institution" />
				</fr:schema>
			</logic:equal>
			
			<logic:equal name="phdParticipantBean" property="participant.internal" value="false">
				<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.ExternalPhdParticipant">
					<fr:slot name="name" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
					<fr:slot name="title" />
					<fr:slot name="qualification" />
					<fr:slot name="category" />
					<fr:slot name="workLocation" />
					<fr:slot name="institution" />
					<fr:slot name="address" />
					<fr:slot name="email">
						<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
						<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.EmailValidator" />
					</fr:slot>
				</fr:schema>
			</logic:equal>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			
			<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=editPhdParticipantInvalid&amp;processId=" + processId %>"/>
			<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewPhdParticipants&amp;processId=" + processId  %>" />		
		</fr:edit>
	
	<html:submit><bean:message key="label.edit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
	
</fr:form>
