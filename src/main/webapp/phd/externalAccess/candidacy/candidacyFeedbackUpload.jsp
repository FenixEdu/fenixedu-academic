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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdProcessAccessType"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.access.PhdExternalOperationBean"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.EmailValidator"%>

<%@page import="pt.ist.fenixWebFramework.renderers.validators.FileValidator"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest.PhdCandidacyFeedbackRequestDocument"%>

<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<bean:message key="label.phds" bundle="PHD_RESOURCES"/>
</div>
<br/>

<bean:define id="accessTypeName"><%= PhdProcessAccessType.CANDIDACY_FEEDBACK_UPLOAD.getLocalizedName() %></bean:define>
<h2><bean:write name="accessTypeName" /></h2>

<br/>
<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdExternalAccess.do?method=prepare" paramId="hash" paramName="participant" paramProperty="accessHashCode">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=operationBean" />
<%--  ### End of Error Messages  ### --%>

<logic:notEmpty name="participant">
	
	<%-- Process details --%>
	<jsp:include page="/phd/externalAccess/processDetails.jsp" />
	
	<logic:notEmpty name="lastFeedbackDocument">
		<br/>
		<strong><bean:message key="label.phd.candidacy.feedback.previous.document" bundle="PHD_RESOURCES" /></strong>
		<fr:view name="lastFeedbackDocument">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4 thlight mtop15" />
				<fr:property name="columnClasses" value="acenter width12em, width30em, thlight" />
			</fr:layout>
			
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdCandidacyFeedbackRequestDocument.class.getName() %>">
				<fr:slot name="uploadTime" />
				<fr:slot name="filename" />
				<fr:slot name="documentVersion"/>
			</fr:schema>
			
		</fr:view>
		<br/>
	</logic:notEmpty>	
	
	<logic:equal name="canUploadDocuments" value="true">

		<fr:form action="/phdExternalAccess.do?method=candidacyFeedbackUpload" encoding="multipart/form-data">
		
			<fr:edit id="operationBean" name="operationBean">
				<fr:schema bundle="PHD_RESOURCES" type="<%= PhdExternalOperationBean.class.getName() %>">
				
					<fr:slot name="email" required="true" validator="<%= EmailValidator.class.getName() %>">
						<fr:property name="size" value="40" />
					</fr:slot>
					<fr:slot name="password" required="true" layout="password">
						<fr:property name="size" value="40" />
					</fr:slot>
					
					<fr:slot name="documentBean.file" required="true">
						<fr:validator name="<%= FileValidator.class.getName() %>">
							<fr:property name="maxSize" value="50mb" />
						</fr:validator>
						<fr:property name="fileNameSlot" value="documentBean.filename"/>
						<fr:property name="size" value="20"/>
					</fr:slot>
					
				</fr:schema>
			
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
					<fr:property name="columnClasses" value=",,tdclear tderror1" />
				</fr:layout>
				
				<fr:destination name="invalid" path="/phdExternalAccess.do?method=prepareCandidacyFeedbackUploadInvalid"/>
			</fr:edit>
			
			<html:submit><bean:message key="label.submit" /></html:submit>
		</fr:form>
	</logic:equal>
	
</logic:notEmpty>


