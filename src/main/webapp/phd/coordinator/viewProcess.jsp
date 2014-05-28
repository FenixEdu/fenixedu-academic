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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<html:xhtml/>

<logic:present role="role(COORDINATOR)">

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.viewProcess" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>

<bean:define id="process" name="process" />

<logic:present name="backMethod">
	<bean:define id="backMethod" name="backMethod"/>
	<html:link action="<%="/phdIndividualProgramProcess.do?method=" + backMethod %>">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</logic:present>

<logic:notPresent name="backMethod">
	<html:link action="/phdIndividualProgramProcess.do?method=manageProcesses">
		« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
</logic:notPresent>

<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<table>
  <tr>
    <td>
		<fr:view schema="PhdIndividualProgramProcess.view" name="process">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop10" />
			</fr:layout>
		</fr:view>
	</td>
    <td style="vertical-align: top; padding-top: 1em;">
    	<ul class="operations">
			<li>
				<jsp:include page="/phd/alertMessagesNotifier.jsp?global=false" />
			</li>
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=manageGuidanceDocuments" paramId="processId" paramName="process" paramProperty="externalId">
					<bean:message key="link.phd.guidance.documents.management" bundle="PHD_RESOURCES" />
				</html:link>
			</li>
			<li>
				<html:link action="/phdIndividualProgramProcess.do?method=viewRefereeLetters" paramId="processId" paramName="process" paramProperty="externalId" >
					<bean:message key="link.phd.referee.letters.view" bundle="PHD_RESOURCES" />
				</html:link>			
			</li>
		</ul>
    </td>
  </tr>
</table>

<%--Thesis --%>
<jsp:include page="/phd/thesis/coordinator/viewThesisProcess.jsp" />

<%-- CAT --%>
<jsp:include page="/phd/seminar/coordinator/viewSeminarProcess.jsp" />

<%-- School part --%>
<jsp:include page="/phd/coordinator/viewSchoolPart.jsp" />


<%--Candidacy --%>
<jsp:include page="/phd/candidacy/coordinator/viewCandidacyProcess.jsp" />

<%--  ### End Of Context Information  ### --%>

<%-- ### Guiding ### --%>
<br/>
<strong>
	<bean:message key="label.phd.guiding" bundle="PHD_RESOURCES"/>
</strong>
<logic:empty name="guidingsList">
	<p><em><bean:message key="message.no.guiding" bundle="PHD_RESOURCES"/></em></p>
</logic:empty>

<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%= "/phdIndividualProgramProcess.do?processId=" + processId %>">
	<input type="hidden" name="method" value="" />
	<logic:notEmpty name="guidingsList">
		<fr:view name="guidingsList">
			<fr:schema type="net.sourceforge.fenixedu.domain.PersonInformationLog" bundle="PHD_RESOURCES">
				<fr:slot name="name" key="label.istid" >
					<fr:property name="classes" value="nobullet noindent"/>   
			   	</fr:slot>
			   	<fr:slot name="qualification" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.qualification"/>
				<fr:slot name="category" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.category"/>
				<fr:slot name="workLocation" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.workLocation"/>
				<fr:slot name="institution" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.institution"/>
				<fr:slot name="address" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.address"/>
				<fr:slot name="email" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.email"/>
				<fr:slot name="phone" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.phone"/>
				<fr:slot name="acceptanceLetter" layout="link" />		
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop15 center" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</fr:form>

<br/>
<strong>
	<bean:message key="label.phd.assistant.guiding" bundle="PHD_RESOURCES"/>
</strong>

<logic:empty name="assistantGuidingsList">
	<p><em><bean:message key="message.no.assistant.guiding" bundle="PHD_RESOURCES"/></em></p>
</logic:empty>

<fr:form action="<%= "/phdIndividualProgramProcess.do?processId=" + processId %>">
	<input type="hidden" name="method" value="" />
	<logic:notEmpty name="assistantGuidingsList">
		<fr:view name="assistantGuidingsList">
			<fr:schema type="net.sourceforge.fenixedu.domain.PersonInformationLog" bundle="PHD_RESOURCES">
				<fr:slot name="name" key="label.istid" >
					<fr:property name="classes" value="nobullet noindent"/>   
			   	</fr:slot>
			   	<fr:slot name="qualification" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.qualification"/>
				<fr:slot name="category" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.category"/>
				<fr:slot name="workLocation" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.workLocation"/>
				<fr:slot name="institution" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.institution"/>
				<fr:slot name="address" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.address"/>
				<fr:slot name="email" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.email"/>
				<fr:slot name="phone" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.phone"/>
				<fr:slot name="acceptanceLetter" layout="link" />		
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 mtop15 center" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</fr:form>

	<%-- ### End of Guiding ### --%>
</logic:present>