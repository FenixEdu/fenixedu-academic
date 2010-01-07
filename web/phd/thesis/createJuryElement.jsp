<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean"%>

<html:xhtml/>

<%-- init values --%>
<% 
	request.setAttribute("createMethod", request.getParameter("createMethod"));
	request.setAttribute("invalidMethod", request.getParameter("invalidMethod"));
	request.setAttribute("postbackMethod", request.getParameter("postbackMethod"));
%> 		

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="createMethod" name="createMethod" />
<bean:define id="invalidMethod" name="invalidMethod" />
<bean:define id="postbackMethod" name="postbackMethod" />

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
							<fr:slot name="email" required="true" />
							<fr:slot name="address" />
							<fr:slot name="phone" />
						</logic:equal>
						<%-- end: create new external element --%>
					</logic:notEmpty>

					<fr:slot name="title" />
					<fr:slot name="workLocation">
						<fr:property name="size" value="15" />
					</fr:slot>
					<fr:slot name="institution">
						<fr:property name="size" value="15" />
					</fr:slot>

				</logic:equal>

				<%if (request.getParameter("juryElement") != null) { %>
					<fr:slot name="reporter" />
				<%} %>

			</logic:notEmpty>

		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>

		<fr:destination name="invalid" path="<%= String.format("/phdThesisProcess.do?method=%s&processId=%s", invalidMethod, processId.toString()) %>"/>
		<fr:destination name="postBack" path="<%= String.format("/phdThesisProcess.do?method=%s&processId=%s", postbackMethod , processId.toString()) %>"/>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="<%= String.format("this.form.method.value='%s';", createMethod) %>"><bean:message bundle="PHD_RESOURCES" key="label.add"/></html:submit>
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='manageThesisJuryElements';"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
	
</fr:form>
