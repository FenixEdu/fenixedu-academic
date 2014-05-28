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

<%@page import="net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType"%><html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

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

<bean:define id="candidacyID" name="candidacy" property="externalId" />
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
			<logic:equal name="operationType" value="PRINT_ALL_DOCUMENTS">
				<bean:define id="requiresNewWindow" value="true" />
			</logic:equal>

			<logic:present name="requiresNewWindow">								
				<logic:notEqual name="operationType" value="PRINT_ALL_DOCUMENTS">
					<li>
						<html:link action="<%= "/degreeCandidacyManagement.do?method=doOperation&amp;operationType=" + operationType + "&amp;candidacyID=" + candidacyID%>" target="_blank">
							<bean:message name="operation" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
						</html:link>
					</li>
				</logic:notEqual>				
			</logic:present>
			<logic:notPresent name="requiresNewWindow">				
				<logic:notEqual name="operationType" value="PRINT_ALL_DOCUMENTS">
					<li>
						<html:link action="<%= "/degreeCandidacyManagement.do?method=doOperation&amp;operationType=" + operationType + "&amp;candidacyID=" + candidacyID%>">
							<bean:message name="operation" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
						</html:link>
					</li>
				</logic:notEqual>
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



