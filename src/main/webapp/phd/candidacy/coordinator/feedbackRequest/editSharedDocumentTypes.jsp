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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestProcessBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.coordinator.feedbackRequest.PhdCandidacyFeedbackRequestDA"%>

<logic:notPresent name="feedbackRequestBean">
	<logic:notEmpty name="sharedDocumentTypes">
		<strong><bean:message key="label.phd.candidacy.feedback.shared.documents" bundle="PHD_RESOURCES" />:</strong> <bean:write name="sharedDocumentTypes"/>
		&nbsp;(<html:link action="/phdCandidacyFeedbackRequest.do?method=prepareEditSharedDocuments" paramId="processId" paramName="process" paramProperty="externalId">
			<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.feedback.request.edit.shared.documents"/>
		</html:link>)
	</logic:notEmpty>
</logic:notPresent>

<logic:empty name="sharedDocumentTypes">
	<br/>
	<html:link action="/phdCandidacyFeedbackRequest.do?method=prepareEditSharedDocuments" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.feedback.request.set.shared.documents"/>
	</html:link>
</logic:empty>

<logic:present name="feedbackRequestBean">
	<bean:define id="processId" name="process" property="externalId" />

	<strong><bean:message key="label.phd.candidacy.feedback.shared.documents" bundle="PHD_RESOURCES" />:</strong>
	<fr:edit id="feedbackRequestBean" name="feedbackRequestBean" 
		action="<%= "/phdCandidacyFeedbackRequest.do?method=editSharedDocuments&processId=" + processId %>">

		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdCandidacyFeedbackRequestProcessBean.class.getName() %>">
			<fr:slot name="sharedDocuments" layout="option-select" required="true" >
				<fr:property name="classes" value="nobullet noindent" />
				<fr:property name="sortBy" value="localizedName" />
				
				<fr:property name="providerClass" value="<%= PhdCandidacyFeedbackRequestDA.AvailableDocumentsToShare.class.getName() %>" />
				<%-- created new layout in fenix-renderers-config.xml --%>
				<fr:property name="eachLayout" value="phd-enum-renderer" />
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="cancel" path="<%= "/phdCandidacyFeedbackRequest.do?method=manageFeedbackRequest&processId=" + processId %>" />
	</fr:edit>

</logic:present>
