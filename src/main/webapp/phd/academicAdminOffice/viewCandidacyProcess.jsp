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


<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.activities.RequestCandidacyReview" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.activities.RejectCandidacyProcess" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.activities.RatifyCandidacy" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.activities.RegistrationFormalization" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.activities.AssociateRegistration" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.activities.AddState" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.activities.RemoveLastState" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess"%>

<strong><bean:message  key="label.phd.candidacyProcess" bundle="PHD_RESOURCES"/></strong>
<br/>

<bean:define id="candidacyProcess" name="process" property="candidacyProcess" />
		
<table>
  <tr>
    <td>
		<fr:view schema="PhdProgramCandidacyProcess.view" name="process" property="candidacyProcess">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop10 thleft" />
			</fr:layout>
		</fr:view>
	</td>
	<td>
		<ul class="operations">
			<li>
				<html:link action="/phdProgramCandidacyProcess.do?method=manageNotifications" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.notifications"/>
				</html:link>
			</li>
		
		<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= AddState.class %>">
			<li>
				<logic:equal value="true" name="process" property="currentUserAllowedToManageProcessState">
				<html:link action="/phdProgramCandidacyProcess.do?method=manageStates" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.states" />
				</html:link>
				</logic:equal>
			</li>		
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= RemoveLastState.class %>" >
			<li>
				<html:link action="/phdProgramCandidacyProcess.do?method=prepareEditProcessAttributes" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.edit.attributes" />
				</html:link>
			</li>
		</phd:activityAvailable>		
			
			<logic:notEmpty name="candidacyProcess" property="individualProgramProcess.phdProgram">
				<li>
					<html:link action="/phdProgramCandidacyProcess.do?method=printCandidacyDeclaration&language=pt" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.printCandidacyDeclaration.pt"/>
					</html:link>
				</li>
				<li>
					<html:link action="/phdProgramCandidacyProcess.do?method=printCandidacyDeclaration&language=en" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
						<bean:message bundle="PHD_RESOURCES" key="label.phd.printCandidacyDeclaration.en"/>
					</html:link>
				</li>
				<logic:notEmpty name="process" property="student">
					<li>
						<html:link action="/phdIndividualProgramProcess.do?method=printSchoolRegistrationDeclaration&language=pt" paramId="processId" paramName="process" paramProperty="externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.print.school.registration.declaration"/>
						</html:link>
					</li>
				</logic:notEmpty>
				
				
			</logic:notEmpty>

			<li>
				<html:link action="/phdProgramCandidacyProcess.do?method=viewLogs" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="link.phd.view.log" /> 
				</html:link>					
			</li>
		</ul>
	</td>
  </tr>
</table>

<ul class="operations">
		
		
		<logic:notEmpty name="process" property="student"> 
		<li style="display: inline;">
			<html:link action="/student.do?method=visualizeStudent" paramId="studentID" paramName="process" paramProperty="student.externalId" target="_blank">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.edit.candidacy.information"/>
			</html:link>
		</li>
		</logic:notEmpty>

		<li style="display: inline;">
			<html:link action="/phdProgramCandidacyProcess.do?method=manageCandidacyDocuments" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.manageCandidacyDocuments"/>
			</html:link>
		</li>

	<logic:equal name="process" property="activeState.active" value="true">
		<li style="display: inline;">
			<html:link action="/phdProgramCandidacyProcess.do?method=manageCandidacyReview" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.academicAdminOffice.manageCandidacyReview"/>
			</html:link>
		</li>
		<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= net.sourceforge.fenixedu.domain.phd.candidacy.activities.RequestCandidacyReview.class %>">
			<li style="display: inline;">
				<html:link action="/phdProgramCandidacyProcess.do?method=prepareRequestCandidacyReview" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.academicAdminOffice.request.candidacy.review"/>
			</html:link>
			</li>
		</phd:activityAvailable>
		<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= net.sourceforge.fenixedu.domain.phd.candidacy.activities.RejectCandidacyProcess.class %>" >
			<li style="display: inline;">
				<html:link action="/phdProgramCandidacyProcess.do?method=prepareRejectCandidacyProcess" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.rejectCandidacyProcess"/>
				</html:link>
			</li>
		</phd:activityAvailable>
		<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= net.sourceforge.fenixedu.domain.phd.candidacy.activities.RatifyCandidacy.class %>" >
			<li style="display: inline;">
				<html:link action="/phdProgramCandidacyProcess.do?method=prepareRatifyCandidacy" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.ratifyCandidacy"/>
				</html:link>
			</li>
		</phd:activityAvailable>
		<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= net.sourceforge.fenixedu.domain.phd.candidacy.activities.RegistrationFormalization.class %>" >
			<li style="display: inline;">
				<html:link action="/phdProgramCandidacyProcess.do?method=prepareRegistrationFormalization" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.registrationFormalization"/>
				</html:link>
			</li>
		</phd:activityAvailable>
		<phd:activityAvailable process="<%= candidacyProcess %>" activity="<%= net.sourceforge.fenixedu.domain.phd.candidacy.activities.AssociateRegistration.class %>">
			<li style="display: inline;">
				<html:link action="/phdProgramCandidacyProcess.do?method=prepareAssociateRegistration" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.registrationFormalization.associate.registration"/>
				</html:link>
			</li>
		</phd:activityAvailable>		
		
	</logic:equal>
</ul>	

