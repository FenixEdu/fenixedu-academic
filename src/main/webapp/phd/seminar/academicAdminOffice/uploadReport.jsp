<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<bean:define id="individualProcessId" name="process" property="individualProgramProcess.externalId" />
<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.upload.public.presentation.seminar.report" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + individualProcessId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<table>
  <tr style="vertical-align: top;">
    <td style="width: 55%">
    	<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process" property="individualProgramProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
	</td>
    <td>
	    <strong><bean:message  key="label.phd.publicPresentationSeminarProcess" bundle="PHD_RESOURCES"/></strong>
		<fr:view schema="PublicPresentationSeminarProcess.view" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
    </td>
  </tr>
</table>
<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<fr:form action="<%="/publicPresentationSeminarProcess.do?processId=" + processId.toString() %>" encoding="multipart/form-data">

	<input type="hidden" name="method" value="" />
	
	<fr:edit id="uploadReportBean" name="uploadReportBean" visible="false"/>

	<fr:edit id="uploadReportBean.generateAlert" name="uploadReportBean">
  		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean">
  			<fr:slot name="generateAlert" layout="radio" />
  		</fr:schema>
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
  	</fr:edit>
	
	<fr:edit id="uploadReportBean.edit.document"
		name="uploadReportBean"
		schema="PublicPresentationSeminarProcessBean.edit.document.and.remarks">
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path="<%="/publicPresentationSeminarProcess.do?method=prepareUploadReportInvalid&processId=" + processId.toString() %>" />
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='uploadReport';"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='viewIndividualProgramProcess';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>

</fr:form>

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>
