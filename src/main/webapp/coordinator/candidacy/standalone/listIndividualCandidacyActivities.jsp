<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree" %>

<html:xhtml/>


<bean:define id="processName" name="processName" />
<bean:define id="parentProcessId" name="parentProcess" property="externalId" />

<bean:define id="infoExecutionDegree" name="<%=PresentationConstants.MASTER_DEGREE%>" type="InfoExecutionDegree" />
<bean:define id="infoDegreeCurricularPlan" name="infoExecutionDegree" property="infoDegreeCurricularPlan" />
<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="externalId" type="java.lang.String"/>


<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<logic:notEmpty name="process">
	<h2><bean:write name="process" property="displayName" /> </h2>
</logic:notEmpty>

<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcesses&amp;parentProcessId=" + parentProcessId.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>'>
	« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>



<logic:notEmpty name="process">
	<bean:define id="processId" name="process" property="externalId" />
		
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
	
	<%-- show candidacy information --%>
	<br />
	<strong><bean:message key="label.candidacy.data" bundle="APPLICATION_RESOURCES"/>:</strong>
	<fr:view name="process" schema='<%= processName.toString() +  ".view" %>'>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>

	<h3 style="margin-bottom: 0.5em;"><bean:message key="label.over23.qualifications.concluded" bundle="CANDIDATE_RESOURCES"/></h3>
	
	<logic:empty name="process" property="candidacy.concludedFormationList">
		<p class="mtop05"><em><bean:message key="label.over23.has.no.qualifications" bundle="CANDIDATE_RESOURCES"/>.</em></p>	
	</logic:empty>
	
	<logic:notEmpty name="process" property="candidacy.concludedFormationList">
		<table class="tstyle4 thlight thleft">
		<tr>
			<th><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/></th>
			<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/></th>
			<th><bean:message key="label.over23.execution.year.conclusion" bundle="CANDIDATE_RESOURCES"/></th>
		</tr>
		<logic:iterate id="qualification" name="process" property="candidacy.concludedFormationList" indexId="index">
		<tr>
			<td>
				<fr:view 	name="qualification"
							property="designation">
					<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
				</fr:view>
			</td>
			<td>
				<fr:view 	name="qualification"
							property="institution.name">
					<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
				</fr:view>	
			</td>
			<td>
				<fr:view 	name="qualification"
							property="conclusionExecutionYear">
					<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
				</fr:view>
			</td>
		</tr>
		</logic:iterate>
		</table>
	</logic:notEmpty>
	
	<h3 style="margin-bottom: 0.5em;"><bean:message key="label.over23.qualifications.non.concluded" bundle="CANDIDATE_RESOURCES"/></h3>
	
	<logic:empty name="process" property="candidacy.nonConcludedFormationList">
		<p class="mtop05"><em><bean:message key="label.over23.has.no.qualifications" bundle="CANDIDATE_RESOURCES"/>.</em></p>	
	</logic:empty>
	
	<logic:notEmpty name="process" property="candidacy.nonConcludedFormationList">
		<table class="tstyle4 thlight thleft">
			<tr>
				<th><bean:message key="label.over23.qualifications.name" bundle="CANDIDATE_RESOURCES"/></th>
				<th><bean:message key="label.over23.school" bundle="CANDIDATE_RESOURCES"/></th>
			</tr>
			<logic:iterate id="qualification" name="process" property="candidacy.nonConcludedFormationList" indexId="index">
			<tr>
				<td>
					<fr:view 	name="qualification"
								property="designation">
						<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
					</fr:view>
				</td>
			</tr>
			<tr>
				<td>
					<fr:view 	name="qualification"
								property="institution.name">
						<fr:layout name="flow"> <fr:property name="labelExcluded" value="true"/> </fr:layout>
					</fr:view>					
				</td>
			</tr>
			</logic:iterate>
		</table>
	</logic:notEmpty>

	
	<%-- show person information --%>
	<br />
	<strong><bean:message key="label.candidacy.personalData" bundle="APPLICATION_RESOURCES" />:</strong>
	<fr:view name="process" property="personalDetails" schema="CandidacyProcess.personalData">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:view>
	
	<%-- show person address information --%>
	<logic:notEmpty name="process" property="personalDetails">
		<fr:view name="process" property="personalDetails" schema="CandidacyProcess.personPhysicalAddress">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</logic:notEmpty>
