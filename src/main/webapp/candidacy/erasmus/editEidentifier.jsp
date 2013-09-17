<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.edit" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<%-- student information --%>
<logic:notEmpty name="process" property="personalDetails.student">
	<br/>
	<strong><bean:message key="label.studentDetails" bundle="APPLICATION_RESOURCES"/>:</strong>
	<fr:view name="process" property="personalDetails.student" schema="student.show.number.information">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<fr:form action='<%= "/caseHandlingMobilityIndividualApplicationProcess.do?method=executeSetEIdentifierForTesting&processId=" + processId.toString() %>' id="erasmusCandidacyForm">	
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
	<fr:edit 	id="individualCandidacyProcessBean.eidentifier" 
				name="individualCandidacyProcessBean"
				property="personBean" >
        <fr:schema bundle="ACADEMIC_OFFICE_RESOURCES"
            type="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest">
            <fr:slot name="eidentifier" key="label.eidentifier" />
        </fr:schema>				
	</fr:edit>
	
	<html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
</fr:form>
