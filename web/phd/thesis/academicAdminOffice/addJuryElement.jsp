<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.thesis.process" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdThesisProcess.do?method=manageThesisJuryElements&processId=" + processId.toString() %>">
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
<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<strong><bean:message  key="label.phd.add.thesis.jury.element" bundle="PHD_RESOURCES"/></strong><br/>

<fr:form action="<%= "/phdThesisProcess.do?processId=" + processId.toString() %>">

	<input type="hidden" name="method" value="" />
	<fr:edit name="thesisJuryElementBean" visible="false" />

	<%-- #################################### --%>
	<%-- ### Never selected any jury type ### --%>
	<logic:empty name="thesisJuryElementBean" property="juryType">
		<fr:edit id="thesisJuryElementBean.jury.type" name="thesisJuryElementBean" >
		
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisJuryElementBean.class.getName() %>">
				<fr:slot name="juryType" layout="radio-postback" required="true">
					<fr:property name="classes" value="nobullet liinline"/>
				</fr:slot>
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			
			<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=prepareAddJuryElementInvalid&processId=" + processId.toString() %>"/>
			<fr:destination name="postback" path="<%= "/phdThesisProcess.do?method=prepareAddJuryElementPostback&processId=" + processId.toString() %>"/>
		</fr:edit>
	</logic:empty>
	<%-- ############### End ############## --%>

	<%-- ### Jury Type selected (Internal or External) ### --%>
	<logic:notEmpty name="thesisJuryElementBean" property="juryType">
		<fr:edit id="thesisJuryElementBean.jury.type" name="thesisJuryElementBean" >
		
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisJuryElementBean.class.getName() %>">
	
				<fr:slot name="juryType" layout="radio-postback" required="true">
					<fr:property name="classes" value="nobullet liinline"/>
				</fr:slot>
				
				<%-- Add internal jury type slots --%>
				<logic:equal name="thesisJuryElementBean" property="juryType" value="INTERNAL">
					<fr:slot name="personName" layout="autoComplete" required="true">
						<fr:property name="size" value="50"/>
						<fr:property name="labelField" value="person.name"/>
						<fr:property name="indicatorShown" value="true"/>		
						<fr:property name="serviceName" value="SearchInternalPersons"/>
						<fr:property name="serviceArgs" value="size=50"/>
						<fr:property name="className" value="net.sourceforge.fenixedu.domain.person.PersonName"/>
						<fr:property name="minChars" value="4"/>				
					</fr:slot>
				</logic:equal>
				
				<%-- Add external jury type slots --%>
				<logic:equal name="thesisJuryElementBean" property="juryType" value="EXTERNAL">
					<fr:slot name="name" required="true" />
					<fr:slot name="qualification" required="true" />
					<fr:slot name="category" required="true" />
					<fr:slot name="institution" />
					<fr:slot name="address" />
					<fr:slot name="phone" />
					<fr:slot name="email" />
				</logic:equal>

			</fr:schema>

			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
			
			<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=prepareAddJuryElementInvalid&processId=" + processId.toString() %>"/>
			<fr:destination name="postback" path="<%= "/phdThesisProcess.do?method=prepareAddJuryElementPostback&processId=" + processId.toString() %>"/>
		</fr:edit>
		
	</logic:notEmpty>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='addJuryElement';"><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='manageThesisJuryElements';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:submit>
	
</fr:form>


<%--  ### End of Operation Area  ### --%>

<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>

</logic:present>