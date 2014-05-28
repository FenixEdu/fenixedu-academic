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

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.view.participants" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="process" name="process" />
<bean:define id="processId" name="process" property="externalId" />

<p>
	<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>
 
<p><strong><bean:message  key="label.phd.edit.participant" bundle="PHD_RESOURCES"/></strong></p>

<logic:empty name="process" property="participants" >
	<br />
	<bean:message key="label.phd.participants.empty" bundle="PHD_RESOURCES" />
</logic:empty>

<logic:notEmpty name="process" property="participants">
	<br/>
	
	<fr:view name="process" property="participants">
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.PhdParticipant">
			<fr:slot name="name" />
			<fr:slot name="title" />
			<fr:slot name="category" />
			<fr:slot name="workLocation" />
			<fr:slot name="institution" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10 thleft" />
			<fr:link 	name="edit" 
						link="<%= "/phdIndividualProgramProcess.do?method=prepareEditPhdParticipant&amp;phdParticipantId=${externalId}&amp;processId=" + processId %>"
						label="link.net.sourceforge.fenixedu.domain.phd.PhdParticipant.edit,PHD_RESOURCES" />
		</fr:layout>
	</fr:view>

</logic:notEmpty>
