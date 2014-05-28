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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.request.public.thesis.presentation" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>

<br/>

<%-- pre conditions --%>
<div class="warning1 mbottom05" style="width: 700px;">
	<table>
		<tr>
			<td><bean:message  key="label.phd.publicPresentationSeminar" bundle="PHD_RESOURCES"/>: </td>
			<td class="acenter">
				<html:img src="<%= request.getContextPath() + ((Boolean) request.getAttribute("hasPublicPresentationSeminar") ?  "/images/correct.gif"  : "/images/incorrect.gif" )%>"/>
			</td>
		</tr>
		<logic:present name="hasPublicPresentationSeminarReport">
			<tr>
				<td><bean:message  key="label.phd.public.presentation.seminar.report.document" bundle="PHD_RESOURCES"/> <bean:message  key="label.phd.publicPresentationSeminar" bundle="PHD_RESOURCES"/>: </td>
				<td class="acenter">
					<html:img src="<%= request.getContextPath() + ((Boolean) request.getAttribute("hasPublicPresentationSeminarReport") ?  "/images/correct.gif"  : "/images/incorrect.gif" )%>"/>
				</td>
			</tr>
		</logic:present>
		<tr>
			<td><bean:message  key="label.phd.qualification.exams" bundle="PHD_RESOURCES"/>: </td>
			<td class="acenter">
				<html:img src="<%= request.getContextPath() + ((Boolean) request.getAttribute("hasQualificationExamsToPerform") ?  "/images/incorrect.gif"  : "/images/correct.gif" )%>"/>
			</td>
		</tr>
		<tr>
			<td><bean:message  key="label.phd.school.part" bundle="PHD_RESOURCES"/>: </td>
			<td class="acenter">
				<html:img src="<%= request.getContextPath() + ((Boolean) request.getAttribute("hasSchoolPartConcluded") ?  "/images/correct.gif"  : "/images/incorrect.gif" )%>"/>
			</td>
		</tr>
		<logic:present name="hasPropaeudeuticsOrExtraEntriesApproved">
			<tr>
				<td><bean:message  key="label.phd.hasPropaeudeuticsOrExtraEntriesToApprove" bundle="PHD_RESOURCES"/>: </td>
				<td class="acenter">
					<html:img src="<%= request.getContextPath() + ((Boolean) request.getAttribute("hasPropaeudeuticsOrExtraEntriesApproved") ?  "/images/correct.gif"  : "/images/incorrect.gif" )%>"/>
				</td>
			</tr>
		</logic:present>
	</table>
</div>
<%-- End of pre conditions --%>

<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<bean:define id="requestPublicThesisPresentation" name="requestPublicThesisPresentation" />

<fr:form action="<%="/phdIndividualProgramProcess.do?processId=" + processId.toString() %>" encoding="multipart/form-data">

<input type="hidden" name="method" value="" />

	<fr:edit id="requestPublicThesisPresentation" name="requestPublicThesisPresentation" visible="false" />
	
	<fr:edit id="requestPublicThesisPresentation.edit.documents" name="requestPublicThesisPresentation" property="documents">
		
		<fr:schema type="<%= PhdProgramDocumentUploadBean.class.getName() %>" bundle="PHD_RESOURCES">
			<fr:slot name="type" readOnly="true" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean.type" layout="phd-enum-renderer" />
			<fr:slot name="file" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean.file">
				<fr:property name="fileNameSlot" value="filename"/>
				<fr:property name="size" value="20"/>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
					<fr:property name="maxSize" value="63mb"/>
				</fr:validator>		
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:destination name="invalid" path="<%="/phdIndividualProgramProcess.do?method=prepareRequestPublicThesisPresentationInvalid&processId=" + processId.toString() %>" />
		</fr:layout>
	</fr:edit>
	
  	<fr:edit id="requestPublicThesisPresentation.generateAlert" name="requestPublicThesisPresentation">
  		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisProcessBean.class.getName() %>">
  			<fr:slot name="generateAlert" layout="radio" />
  		</fr:schema>
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
  	</fr:edit>
  	
	<fr:edit id="requestPublicThesisPresentation.edit.remarks" name="requestPublicThesisPresentation">
		
		<fr:schema type="<%= PhdThesisProcessBean.class.getName() %>" bundle="PHD_RESOURCES">
			<fr:slot name="whenThesisDiscussionRequired" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
			<fr:slot name="remarks" layout="longText">
				<fr:property name="columns" value="80"/>
				<fr:property name="rows" value="8"/>
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%="/phdIndividualProgramProcess.do?method=prepareRequestPublicThesisPresentationInvalid&processId=" + processId.toString() %>" />
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='requestPublicThesisPresentation';"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='viewProcess';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>

</fr:form>

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>

