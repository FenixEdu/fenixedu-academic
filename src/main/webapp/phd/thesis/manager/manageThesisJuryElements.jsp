<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType"%>


<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.RejectJuryElements"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.ValidateJury"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.PrintJuryElementsDocument"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.AddPresidentJuryElement"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.activities.AddJuryElement"%><html:xhtml/>

<logic:present role="MANAGER">
<bean:define id="individualProcessId" name="process" property="individualProgramProcess.externalId" />

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.thesis.jury.elements" bundle="PHD_RESOURCES" /></h2>
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
<bean:define id="processId" name="process" property="externalId" />
<bean:define id="process" name="process" />

<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<table>
	<tr>
		<td>
			<fr:view schema="PhdIndividualProgramProcess.view.resume" name="process" property="individualProgramProcess">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
				</fr:layout>
			</fr:view>
		</td>
	</tr>
</table>

<logic:equal name="process" property="juryValidated" value="true">
	<br/>
	<strong><bean:message key="label.phd.thesis.jury.validation.date" bundle="PHD_RESOURCES"/>:</strong> <fr:view name="process" property="whenJuryValidated" layout="null-as-label" />
	<br/>
	<strong><bean:message key="label.phd.thesis.jury.designation.date" bundle="PHD_RESOURCES"/>:</strong> <fr:view name="process" property="whenJuryDesignated" layout="null-as-label" />
</logic:equal>

	<br/>
	
<logic:notEmpty name="process" property="juryPresidentDocument">
	<br/>
	<strong><bean:message  key="label.phd.thesis.jury.president.document" bundle="PHD_RESOURCES"/>: </strong>
	<bean:define id="url2" name="process" property="juryPresidentDocument.downloadUrl" />
	<a href="<%= url2.toString() %>">
		<bean:write name="process" property="juryPresidentDocument.documentType.localizedName"/> 
		(<bean:message  key="label.version" bundle="PHD_RESOURCES" /> <bean:write name="process" property="juryPresidentDocument.documentVersion"/>)
	</a>
	<logic:equal name="process" property="juryPresidentDocument.documentAccepted" value="false">
		<span style="color:red"> 
			<bean:message key="label.net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean.document.rejected" bundle="PHD_RESOURCES" />
		</span>
	</logic:equal>
	<br/>
</logic:notEmpty>

<logic:notEmpty name="process" property="juryElementsDocument">
	<strong><bean:message  key="label.phd.thesis.jury.elements.document" bundle="PHD_RESOURCES"/>: </strong>
	<bean:define id="finalThesisDownloadUrl" name="process" property="juryElementsDocument.downloadUrl" />
	<a href="<%= finalThesisDownloadUrl.toString() %>">
		<bean:write name="process" property="juryElementsDocument.documentType.localizedName"/> 
		(<bean:message  key="label.version" bundle="PHD_RESOURCES" /> <bean:write name="process" property="juryElementsDocument.documentVersion"/>)
	</a>
	<logic:equal name="process" property="juryElementsDocument.documentAccepted" value="false"> 
		<span style="color:red">
			<bean:message key="label.net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean.document.rejected" bundle="PHD_RESOURCES" />
		</span>
	</logic:equal>
	<br/><br/>
</logic:notEmpty>

<strong>Presidente:</strong> Presidente do Conselho Científico do IST
<logic:notEmpty name="process" property="presidentJuryElement">
	<br/>
	<strong>Presidente nomeado:</strong> <bean:write name="process" property="presidentJuryElement.nameWithTitle" />
</logic:notEmpty>

<%--  ### End Of Context Information  ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<br/>
<br/>
<strong><bean:message  key="label.phd.thesis.elements" bundle="PHD_RESOURCES"/>:</strong>
<logic:notEmpty name="process" property="thesisJuryElements">
<fr:view name="process" property="thesisJuryElements">

	<fr:schema bundle="PHD_RESOURCES" type="<%= ThesisJuryElement.class.getName() %>">
		<fr:slot name="elementOrder" />
		<fr:slot name="nameWithTitleAndRoleOnProcess" key="label.net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement.nameWithTitle"/>
		<fr:slot name="category" />
		<%-- 
		<fr:slot name="workLocation" />
		<fr:slot name="institution" />
		--%>
		<fr:slot name="email" />
		<fr:slot name="reporter" />
		<fr:slot name="expert" />
	</fr:schema>

	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
		
		<fr:property name="sortBy" value="elementOrder=asc" />
	</fr:layout>
</fr:view>
</logic:notEmpty>
<logic:empty name="process" property="thesisJuryElements">
	<em><bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.no.jury.elements"/></em><br/>
	<br/>
</logic:empty>


<ul class="operations" >
	<phd:activityAvailable process="<%= process %>" activity="<%= PrintJuryElementsDocument.class %>">
	<li style="display: inline;">
		<html:link action="/phdThesisProcess.do?method=printJuryElementsDocument" paramId="processId" paramName="process" paramProperty="externalId"> 
			<bean:message bundle="PHD_RESOURCES" key="label.phd.thesis.print.jury.elements"/>
		</html:link>
	</li>
	</phd:activityAvailable>
</ul>

<%--  ### End of Operation Area  ### --%>

<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>

</logic:present>