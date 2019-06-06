<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="org.fenixedu.academic.domain.phd.access.PhdProcessAccessType"%>
<%@page import="org.fenixedu.academic.domain.phd.access.PhdExternalOperationBean"%>

<%@page import="pt.ist.fenixWebFramework.renderers.validators.FileValidator"%>
<%@page import="org.fenixedu.academic.domain.phd.PhdThesisReportFeedbackDocument"%><html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>"><%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<bean:message key="label.phds" bundle="PHD_RESOURCES"/>
</div>
<br/>

<bean:define id="accessTypeName"><%= PhdProcessAccessType.JURY_REPORTER_FEEDBACK_UPLOAD.getLocalizedName() %></bean:define>
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
	
	<logic:notEmpty name="lastReportFeedbackDocument">
		<br/>
		<strong><bean:message key="label.phd.jury.report.feedback.document.previous" bundle="PHD_RESOURCES" /></strong>
		<fr:view name="lastReportFeedbackDocument">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4 thlight mtop15" />
				<fr:property name="columnClasses" value="acenter width12em, width30em, thlight" />
			</fr:layout>
			
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisReportFeedbackDocument.class.getName() %>">
				<fr:slot name="creationDate" />
				<fr:slot name="filename" />
				<fr:slot name="documentVersion"/>
			</fr:schema>
			
		</fr:view>
		<br/>
	</logic:notEmpty>	
	
	<logic:equal name="waitingForJuryReporterFeedback" value="true">

		<fr:form action="/phdExternalAccess.do?method=juryReporterFeedbackUpload" encoding="multipart/form-data">
		
			<fr:edit id="operationBean" name="operationBean">
				<fr:schema bundle="PHD_RESOURCES" type="<%= PhdExternalOperationBean.class.getName() %>">
				
					<%@include file="/phd/externalAccess/common/accessInformation.jsp" %>
					
					<fr:slot name="documentBean.file" required="true">
						<fr:validator name="<%= FileValidator.class.getName() %>">
							<fr:property name="maxSize" value="100mb" />
							<fr:property name="acceptedExtensions" value="pdf" />
							<fr:property name="acceptedTypes" value="application/pdf" />
						</fr:validator>
						<fr:property name="fileNameSlot" value="documentBean.filename"/>
						<fr:property name="size" value="20"/>
					</fr:slot>
					
				</fr:schema>
			
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
					<fr:property name="columnClasses" value=",,tdclear tderror1" />
				</fr:layout>
				
				<fr:destination name="invalid" path="/phdExternalAccess.do?method=prepareJuryReporterFeedbackUploadInvalid"/>
			</fr:edit>
			
			<html:submit><bean:message key="label.submit" /></html:submit>
		</fr:form>
	</logic:equal>
	
</logic:notEmpty>


