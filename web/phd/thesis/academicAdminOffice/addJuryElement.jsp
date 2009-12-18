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
<h2><bean:message key="label.phd.thesis.jury.elements" bundle="PHD_RESOURCES" /></h2>
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
<fr:view schema="PhdIndividualProgramProcess.view.resume" name="process" property="individualProgramProcess">
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
	<fr:edit id="thesisJuryElementBean" name="thesisJuryElementBean" visible="false" />

	<fr:edit id="thesisJuryElementBean.jury.type" name="thesisJuryElementBean" >
		
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdThesisJuryElementBean.class.getName() %>">
			<fr:slot name="participantSelectType" layout="radio-postback" required="true">
				<fr:property name="classes" value="liinline nobullet"/>
				<fr:property name="bundle" value="PHD_RESOURCES" />
			</fr:slot>
			
			
			<%-- ################################################# --%>
			<%-- ### Jury Type selected (NEW or EXISTING) ### --%>
			<%-- ################################################# --%>
	
			<logic:notEmpty name="thesisJuryElementBean" property="participantSelectType">
	
				<%-- Add existing participant to thesis jury --%>
				<logic:equal name="thesisJuryElementBean" property="participantSelectType.name" value="EXISTING">
					<fr:slot name="participant" layout="autoComplete" required="true">
						<fr:property name="size" value="50"/>
						<fr:property name="labelField" value="nameWithTitle"/>
						<fr:property name="indicatorShown" value="true"/>		
						<fr:property name="serviceName" value="SearchInternalPersons"/>
						<fr:property name="serviceName" value="SearchPhdParticipantsByProcess"/>
				        <fr:property name="serviceArgs" value="phdProcessOID=${individualProgramProcess.externalId}"/>
						<fr:property name="className" value="net.sourceforge.fenixedu.domain.phd.PhdParticipant"/>
						<fr:property name="minChars" value="3"/>				
					</fr:slot>
				</logic:equal>
	
				<%-- or create a new one (must select internal or external) --%>
				<logic:equal name="thesisJuryElementBean" property="participantSelectType.name" value="NEW">
	
					<%-- select internal or external element type --%>
					<fr:slot name="participantType" layout="radio-postback" required="true">
						<fr:property name="classes" value="liinline nobullet"/>
						<fr:property name="bundle" value="PHD_RESOURCES" />
					</fr:slot>					
	
					<%-- selected new participant type --%>
					<logic:notEmpty name="thesisJuryElementBean" property="participantType">
	
						<%-- INTERNAL jury type slots --%>
						<logic:equal name="thesisJuryElementBean" property="participantType.name" value="INTERNAL">
							<fr:slot name="personName" layout="autoComplete" required="true">
								<fr:property name="size" value="50"/>
								<fr:property name="labelField" value="name"/>
								<fr:property name="indicatorShown" value="true"/>		
								<fr:property name="serviceName" value="SearchInternalPersons"/>
								<fr:property name="serviceArgs" value="size=50"/>
								<fr:property name="className" value="net.sourceforge.fenixedu.domain.person.PersonName"/>
								<fr:property name="minChars" value="4"/>				
							</fr:slot>
						</logic:equal>
						
						<%-- EXTERNAL element --%>						
						<logic:equal name="thesisJuryElementBean" property="participantType.name" value="EXTERNAL">
							<fr:slot name="name" required="true" />
							<fr:slot name="qualification" required="true" />
							<fr:slot name="category" required="true" />
							<fr:slot name="workLocation">
								<fr:property name="size" value="15" />
							</fr:slot>
							<fr:slot name="email" required="true" />
							<fr:slot name="address" />
							<fr:slot name="phone" />
						</logic:equal>
						<%-- end: create new external element --%>
					</logic:notEmpty>

					<fr:slot name="title" />
				</logic:equal>

				<fr:slot name="reporter" />

			</logic:notEmpty>

		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>

		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=prepareAddJuryElementInvalid&processId=" + processId.toString() %>"/>
		<fr:destination name="postBack" path="<%= "/phdThesisProcess.do?method=prepareAddJuryElementPostback&processId=" + processId.toString() %>"/>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='addJuryElement';"><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='manageThesisJuryElements';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
	
</fr:form>


<%--  ### End of Operation Area  ### --%>

<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>

</logic:present>
