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
<%@page import="net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType" %>

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
		
			<li>
				<html:link action="/phdProgramCandidacyProcess.do?method=manageStates" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
					<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.states" />
				</html:link>
			</li>		
		
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
		</ul>
	</td>
  </tr>
</table>

<ul class="operations">

		<li style="display: inline;">
			<html:link action="/phdProgramCandidacyProcess.do?method=manageCandidacyDocuments" paramId="processId" paramName="process" paramProperty="candidacyProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.manageCandidacyDocuments"/>
			</html:link>
		</li>
</ul>	

