<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType"%><html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<em><bean:message key="portal.candidate" /></em>
<h2><bean:message  key="label.candidacy.candidacyDetails"/></h2>

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="CANDIDATE_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<logic:equal name="candidacy" property="activeCandidacySituation.candidacySituationType.name" value="REGISTERED">
	<fr:view name="candidacy" schema="DegreeCandidacy.view-with-person-details">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight" />
		</fr:layout>
	</fr:view>
</logic:equal>


<logic:notEqual name="candidacy" property="activeCandidacySituation.candidacySituationType.name" value="REGISTERED">
	<fr:view name="candidacy" schema="DegreeCandidacy.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight" />
		</fr:layout>
	</fr:view>
</logic:notEqual>

<bean:define id="emptyOperations">
	<bean:write name="operations" property="empty" />
</bean:define>

<bean:define id="candidacyID" name="candidacy" property="idInternal" />
<logic:notEmpty name="operations">
	<bean:define id="emptyOperations" value="true" />
	<ul>
	<logic:iterate id="operation" name="operations">
		<logic:equal name="operation" property="visible" value="true">
		
			<bean:define id="emptyOperations" value="false" />
			<bean:define id="operationType" name="operation" property="type.name" />

			<logic:equal name="operationType" value="PRINT_SCHEDULE">
				<bean:define id="requiresNewWindow" value="true" />
			</logic:equal>
			<logic:equal name="operationType" value="PRINT_REGISTRATION_DECLARATION"> 
				<bean:define id="requiresNewWindow" value="true" />
			</logic:equal>
			<logic:equal name="operationType" value="PRINT_UNDER_23_TRANSPORTS_DECLARATION">
				<bean:define id="requiresNewWindow" value="true" />
			</logic:equal>
			<logic:equal name="operationType" value="PRINT_ALL_DOCUMENTS">
				<bean:define id="requiresNewWindow" value="true" />
			</logic:equal>

			<logic:present name="requiresNewWindow">
				<li>
					<logic:equal name="operationType" value="PRINT_ALL_DOCUMENTS">
						<logic:present name="isInTaguspark">
							
							<strong><bean:message  key="label.important" bundle="CANDIDATE_RESOURCES"/>:</strong>
							<html:link action="<%= "/degreeCandidacyManagement.do?method=showSummaryFile&amp;candidacyID=" + candidacyID%>" target="_blank">
								<bean:message name="operation" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
							</html:link>
							
						</logic:present>
					</logic:equal>
					<logic:notEqual name="operationType" value="PRINT_ALL_DOCUMENTS">
						
						<html:link action="<%= "/degreeCandidacyManagement.do?method=doOperation&amp;operationType=" + operationType + "&amp;candidacyID=" + candidacyID%>" target="_blank">
							<bean:message name="operation" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
						</html:link>
						
					</logic:notEqual>
				</li>
			</logic:present>
			<logic:notPresent name="requiresNewWindow">
				<li>
					<logic:equal name="operationType" value="PRINT_ALL_DOCUMENTS">
						<logic:present name="isInTaguspark">
						
							<strong><bean:message  key="label.important" bundle="CANDIDATE_RESOURCES"/>:</strong>
							<html:link action="<%= "/degreeCandidacyManagement.do?method=showSummaryFile&amp;candidacyID=" + candidacyID%>">
								<bean:message name="operation" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
							</html:link>
							
						</logic:present>
					</logic:equal>
					<logic:notEqual name="operationType" value="PRINT_ALL_DOCUMENTS">
					
						<html:link action="<%= "/degreeCandidacyManagement.do?method=doOperation&amp;operationType=" + operationType + "&amp;candidacyID=" + candidacyID%>">
							<bean:message name="operation" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
						</html:link>
						
					</logic:notEqual>
				</li>
			</logic:notPresent>
			
		</logic:equal>

	</logic:iterate>
	</ul>
</logic:notEmpty>


<logic:equal name="emptyOperations" value="true">
	<p>
		<span class="error0"><bean:message  key="label.candidacy.candidacyDetails.noOperationsToBeDone"/> </span>
	</p>
</logic:equal>

<logic:equal name="candidacy" property="activeCandidacySituation.candidacySituationType" value="REGISTERED">
	<bean:define id="istUsername" name="person" property="istUsername" />
	<div class="infoop2 mtop2" style="padding: 0.5em 1em;">
		<p class="mvert025"><strong><bean:message key="label.attention"/></strong>:</p>
		<p class="mvert025"><span><bean:message key="label.candidacy.institutional.email.creation.warning" arg0="<%=istUsername.toString()%>"/>:</span></p>
		<p class="mvert05">
			<h3><html:link style="border-bottom: 1px solid #97b7ce;" href="https://ciist.ist.utl.pt/inscricoes/passo2.html"><bean:message key="link.candidacy.institutional.email.creation.nextStep"/> &gt;&gt;</html:link></h3>
		</p>
	</div>
</logic:equal>



