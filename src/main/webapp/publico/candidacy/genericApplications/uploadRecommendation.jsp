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
<%@page import="net.sourceforge.fenixedu.domain.candidacy.GenericApplicationRecomentation"%>
<%@page import="org.apache.struts.action.ActionMessages"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.GenericApplicationFile"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.GenericApplication"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod"%>
<%@page import="java.util.SortedSet"%>

<html:xhtml/>

<% final GenericApplicationRecomentation genericRecomentation = (GenericApplicationRecomentation) request.getAttribute("recomentation"); %>

<h2>
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.recomentation.email.subject" arg0="<%= genericRecomentation.getGenericApplication().getName() %>"/>
</h2>

<br/>

<div class="infoop">
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.recomentation.fillInstructions"
		arg0="<%= genericRecomentation.getGenericApplication().getName() %>"
		arg1="<%= genericRecomentation.getGenericApplication().getGenericApplicationPeriod().getTitle().getContent() %>" />
</div>

<logic:messagesPresent message="true">
     <html:messages id="messages" message="true" bundle="CANDIDATE_RESOURCES">
      <p><span class="error0"><bean:write name="messages" filter="false" /></span></p>
     </html:messages>
</logic:messagesPresent>

<logic:present name="recommendationSaved">
	<br/>
	<div id="savedMessage">
		<div class="infoop5">
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.recommendation.saved"/>
		</div>
	</div>
</logic:present>

<table class="tstyle2 thlight thcenter mtop15">
	<tr>
		<td>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.name.person"/>
		</td>
		<td>
			<%= genericRecomentation.getName() %>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.recommendation.institution"/>
		</td>
		<td>
			<%= genericRecomentation.getInstitution() %>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.email"/>
		</td>
		<td>
			<%= genericRecomentation.getEmail() %>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.recommendation.document"/>
		</td>
		<td>
			<% if (genericRecomentation.hasLetterOfRecomentation()) { %>
				<a href="<%= request.getContextPath() +  "/publico/genericApplications.do?method=downloadRecomendationFile&fileExternalId=" 
							+ genericRecomentation.getLetterOfRecomentation().getExternalId() + "&confirmationCode=" + genericRecomentation.getConfirmationCode() %>">
					<%= genericRecomentation.getLetterOfRecomentation().getFilename() %>
				</a>
			<% } %>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.size.in.bytes"/>
		</td>
		<td>
			<% if (genericRecomentation.hasLetterOfRecomentation()) { %>
				<%= genericRecomentation.getLetterOfRecomentation().getSize() %>
			<% } %>	
		</td>
	</tr>
	<tr>
		<td>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.checksum"/>
		</td>
		<td style="color: gray;">
			<% if (genericRecomentation.hasLetterOfRecomentation()) { %>
				<%= genericRecomentation.getLetterOfRecomentation().getChecksumAlgorithm() %>: <%= genericRecomentation.getLetterOfRecomentation().getChecksum() %>
			<% } %>
		</td>
	</tr>
</table>

<% if (genericRecomentation.hasLetterOfRecomentation()) { %>
	<p>
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.recommendation.replace"/>
	</p>
<% } %>

<fr:form id="uploadBeanForm" action="/genericApplications.do" encoding="multipart/form-data">
	<input type="hidden" name="method" value="uploadRecommendation"/>
	<input type="hidden" name="recommendationExternalId" value="<%= genericRecomentation.getExternalId() %>"/>
	<input type="hidden" name="confirmationCode" value="<%= genericRecomentation.getConfirmationCode() %>"/>

	<fr:edit id="uploadBean" name="uploadBean">
		<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacy.util.GenericApplicationUploadBean">
			<fr:slot name="stream" key="label.candidacy.document.file">
    			<fr:property name="fileNameSlot" value="fileName"/>
    			<fr:property name="fileSizeSlot" value="fileSize"/>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
					<fr:property name="maxSize" value="3698688"/>
					<fr:property name="acceptedExtensions" value="pdf" />
					<fr:property name="acceptedTypes" value="application/pdf" />
				</fr:validator>
  			</fr:slot>
		</fr:schema>
		<fr:layout>
			<fr:property name="classes" value="tstyle2 thcenter mtop15"/>
		</fr:layout>
	</fr:edit>
	<p><em><bean:message key="message.max.file.size" bundle="CANDIDATE_RESOURCES"/></em></p>
	<p><em><bean:message key="message.candidacy.upload.pdf.documents" bundle="CANDIDATE_RESOURCES"/></em></p>
	<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>		
</fr:form>

<logic:present name="recommendationSaved">
	<script>
		$(document).hover(function(){
			$('#savedMessage').fadeOut();
		});
		$('input[type=file]').on('click focusin', function() {
			$('#savedMessage').fadeOut();
		});
	</script>
</logic:present>
