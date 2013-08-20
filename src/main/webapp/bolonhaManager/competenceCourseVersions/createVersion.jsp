<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="competenceCourseID" name="bean"
	property="competenceCourse.externalId" />
<bean:define id="proposal"
	value="<%= String.valueOf(request.getParameter("proposal") != null) %>" />


<em><bean:message key="label.manage.versions" bundle="BOLONHA_MANAGER_RESOURCES" /></em>
<h2><bean:message key="label.competenceCourse.createVersion" bundle="BOLONHA_MANAGER_RESOURCES" /></h2>


<logic:equal name="bean" property="loggedPersonAllowedToCreateChangeRequests" value="false">
	<p>
		<span class="warning0"><bean:message key="error.cannot.create.request.in.chosen.semester" bundle="BOLONHA_MANAGER_RESOURCES" /></span>
	</p>
</logic:equal>
<logic:equal name="bean" property="loggedPersonAllowedToCreateChangeRequests" value="true">
	<logic:equal name="proposal" value="false">
		<logic:equal name="bean" property="requestDraftAvailable" value="true">
			<p>
				<span class="warning0"><bean:message key="label.already.existing.draft.request" bundle="BOLONHA_MANAGER_RESOURCES" /></span>
			</p>
		</logic:equal>
		<logic:equal name="bean" property="requestDraftAvailable" value="false">
			<logic:equal name="bean" property="competenceCourseDefinedForExecutionPeriod" value="true">
				<p>
					<span class="warning0"><bean:message key="label.not.able.to.create.due.to.existing.version" bundle="BOLONHA_MANAGER_RESOURCES" /></span>
				</p>
			</logic:equal>
		</logic:equal>
	</logic:equal>
</logic:equal>

<logic:messagesPresent message="true">
	<p>
	<html:messages id="messages" message="true" bundle="BOLONHA_MANAGER_RESOURCES">
		<span class="error0"><bean:write name="messages" /></span>
	</html:messages>
	</p>
</logic:messagesPresent>



<p class="mtop2 mbottom1 bold">1) Informação Geral</p>

<div class="dinline forminline">

<div class="forminline">
<fr:form action="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competenceCourseID + "&method=viewBibliography"  + (request.getParameter("proposal") != null ? "&proposal=y" : "")%>">
	<fr:edit id="editVersion" name="bean" visible="false" />

	<bean:define id="schema" value="editCompetenceCourseInformation.common" />
	<bean:define id="loadSchema" value="editCompetenceCourseLoad.semestrial" />

	<logic:equal name="bean" property="regime" value="ANUAL">
		<bean:define id="loadSchema" value="editCompetenceCourseLoad.anual.same.info" />
	</logic:equal>

	<logic:equal name="bean" property="competenceCourseDefinedForExecutionPeriod" value="true">
		<bean:define id="schema" value="editCompetenceCourseInformation.common.simple" />
	</logic:equal>
	
	<logic:equal name="bean" property="requestDraftAvailable" value="true">
		<bean:define id="schema" value="editCompetenceCourseInformation.common.simple" />
	</logic:equal>

	<logic:equal name="proposal" value="true">
		<bean:define id="schema" value="editCompetenceCourseInformation.common.executionYear.readonly" />
	</logic:equal>

	<logic:equal name="bean" property="loggedPersonAllowedToCreateChangeRequests" value="false">
		<bean:define id="schema" value="editCompetenceCourseInformation.common.simple" />
	</logic:equal>

	<fr:edit id="common-part" name="bean" schema="<%= schema %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
		</fr:layout>
		<fr:destination name="postBack"
			path="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competenceCourseID + "&method=prepareCreateVersion" + (request.getParameter("proposal") != null ? "&proposal=y" : "")%>" />
	</fr:edit>

	<logic:equal name="bean" property="loggedPersonAllowedToCreateChangeRequests" value="true">
		<logic:equal name="proposal" value="false">
			<logic:equal name="bean" property="requestDraftAvailable" value="false">
			<logic:equal name="bean" property="competenceCourseDefinedForExecutionPeriod" value="false">
				<fr:edit id="pt-part" name="bean"
					schema="editCompetenceCourseInformation.pt">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
						<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
					</fr:layout>
				</fr:edit>
		
				<p class="mtop2 mbottom1 bold">3) Objectivos, Programa e Metodologia de Avaliação em <b>Inglês</b></p>
				<fr:edit id="en-part" name="bean"
					schema="editCompetenceCourseInformation.en">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
						<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
					</fr:layout>
				</fr:edit>
		
		
				<p class="mtop2 mbottom1 bold">4) Carga Horária</p>
				<fr:edit id="editVersionLoad" name="beanLoad" visible="false" />
				<fr:edit id="versionLoad" name="beanLoad" schema="<%= loadSchema  %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
						<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
					</fr:layout>
					<fr:destination name="loadInformationPostBack"
						path="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competenceCourseID + "&method=prepareCreateVersion" + (request.getParameter("proposal") != null ? "&proposal=y" : "")%>" />
				</fr:edit>
				<logic:equal name="beanLoad" property="sameInformationForBothPeriods"
					value="false">
					<logic:equal name="bean" property="regime" value="ANUAL">
		
						<fr:edit id="versionLoad2" name="beanLoad"
							schema="editCompetenceCourseLoad.anual.diferent.info">
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
								<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
							</fr:layout>
						</fr:edit>
					</logic:equal>
				</logic:equal>
				
		
				<br/>
		
				<p class="dinline">
					<html:submit>
						<bean:message key="label.submit" bundle="APPLICATION_RESOURCES" />
					</html:submit>
				</p>
			</logic:equal>
			</logic:equal>
		</logic:equal>
		<logic:equal name="proposal" value="true">
			<logic:equal name="bean" property="competenceCourseDefinedForExecutionPeriod" value="true">
				
				<p class="mtop2 mbottom1 bold">2) Objectivos, Programa e Metodologia de Avaliação em <b>Português</b></p>
	
				<fr:edit id="pt-part" name="bean"
					schema="editCompetenceCourseInformation.pt">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
						<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
					</fr:layout>
				</fr:edit>
	
	
				<p class="mtop2 mbottom1 bold">3) Objectivos, Programa e Metodologia de Avaliação em <b>Inglês</b></p>
				<fr:edit id="en-part" name="bean"
					schema="editCompetenceCourseInformation.en">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
						<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
					</fr:layout>
				</fr:edit>
	
	
				<p class="mtop2 mbottom1 bold bold">4) Carga Horária</p>
				<fr:edit id="editVersionLoad" name="beanLoad" visible="false" />
				<fr:edit id="versionLoad" name="beanLoad" schema="<%= loadSchema  %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
						<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
					</fr:layout>
					<fr:destination name="loadInformationPostBack"
						path="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competenceCourseID + "&method=prepareCreateVersion" + (request.getParameter("proposal") != null ? "&proposal=y" : "") %>" />
				</fr:edit>
				<logic:equal name="beanLoad" property="sameInformationForBothPeriods"
					value="false">
					<logic:equal name="bean" property="regime" value="ANUAL">
									
						<fr:edit id="versionLoad2" name="beanLoad"
							schema="editCompetenceCourseLoad.anual.diferent.info">
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle5 thlight thright" />
								<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
							</fr:layout>
						</fr:edit>
					</logic:equal>
				</logic:equal>
	
				<br/>
	
				<p class="dinline">
					<html:submit>
						<bean:message key="label.submit" bundle="APPLICATION_RESOURCES" />
					</html:submit>
				</p>
			</logic:equal>
			<logic:equal name="bean" property="requestDraftAvailable" value="true">
				
				<p class="mtop2 mbottom1 bold">2) Objectivos, Programa e Metodologia de Avaliação em <b>Português</b></p>
	
				<fr:edit id="pt-part" name="bean"
					schema="editCompetenceCourseInformation.pt">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
						<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
					</fr:layout>
				</fr:edit>
	
	
				<p class="mtop2 mbottom1 bold">3) Objectivos, Programa e Metodologia de Avaliação em <b>Inglês</b></p>
				<fr:edit id="en-part" name="bean"
					schema="editCompetenceCourseInformation.en">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
						<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
					</fr:layout>
				</fr:edit>
	
	
				<p class="mtop2 mbottom1 bold bold">4) Carga Horária</p>
				<fr:edit id="editVersionLoad" name="beanLoad" visible="false" />
				<fr:edit id="versionLoad" name="beanLoad" schema="<%= loadSchema  %>">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
						<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
					</fr:layout>
					<fr:destination name="loadInformationPostBack"
						path="<%= "/competenceCourses/manageVersions.do?competenceCourseID=" + competenceCourseID + "&method=prepareCreateVersion" + (request.getParameter("proposal") != null ? "&proposal=y" : "") %>" />
				</fr:edit>
				<logic:equal name="beanLoad" property="sameInformationForBothPeriods"
					value="false">
					<logic:equal name="bean" property="regime" value="ANUAL">
									
						<fr:edit id="versionLoad2" name="beanLoad"
							schema="editCompetenceCourseLoad.anual.diferent.info">
							<fr:layout name="tabular">
								<fr:property name="classes" value="tstyle5 thlight thright" />
								<fr:property name="columnClasses" value="width12em,,tderror1 tdclear" />
							</fr:layout>
						</fr:edit>
					</logic:equal>
				</logic:equal>
	
				<br/>
	
				<p class="dinline">
					<html:submit>
						<bean:message key="label.submit" bundle="APPLICATION_RESOURCES" />
					</html:submit>
				</p>
			</logic:equal>
		</logic:equal>
	</logic:equal>
</fr:form>

<fr:form action="<%= "/competenceCourses/manageVersions.do?method=showVersions&competenceCourseID=" + competenceCourseID %>">
	<p class="dinline">
		<html:submit>
			<bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" />
		</html:submit>
	</p>
</fr:form>

</div>

</div>
