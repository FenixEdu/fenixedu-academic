<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createCooperation.mainTitle"/></em> <!-- tobundle -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createCooperation.useCasetitle"/></h2>	
	
	<!-- SELECT COOPERATION TYPE -->
	<logic:present name="cooperationTypeBean">
		<p class="mvert1 breadcumbs">
			<span class="actual">
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.cooperation.create.step"/> 1 : </strong>
			<bean:message key="researcher.activity.cooperation.create.cooperationInitialData" bundle="RESEARCHER_RESOURCES"/></span>
				 > 
			<span>
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.cooperation.create.step"/> 2 : </strong>
			<bean:message key="researcher.activity.cooperation.create.cooperationUnit" bundle="RESEARCHER_RESOURCES"/></span>
				 >
			<span>
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.cooperation.create.step"/> 3 : </strong>
			<bean:message key="researcher.activity.cooperation.create.collaborationForm" bundle="RESEARCHER_RESOURCES"/></span>	 
	 	</p>
	 	<logic:messagesPresent message="true">
			<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
				<p><span class="error0"><bean:write name="messages" /></span></p>
			</html:messages>
		</logic:messagesPresent>		
	 	<p class="mtop2 mbottom1">	
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createCooperationUseCase.step.insertDataExplanation1"/></strong>
		</p>	
		<fr:form action="/activities/createCooperation.do?method=prepareAssociateUnitToCooperation">
			<fr:edit id="cooperationTypeBean" name="cooperationTypeBean" schema="cooperationParticipation.cooperationType">
				<fr:layout>
					<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/activities/createCooperation.do?method=prepareCreateCooperationParticipation"/>			
				<fr:destination name="cancel" path="/activities/activitiesManagement.do?method=listActivities"/>
			</fr:edit>
			<html:submit><bean:message key="button.continue" bundle="RESEARCHER_RESOURCES" property="submit"/></html:submit>
			<html:cancel><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:cancel>
		</fr:form>
	</logic:present>
	
	<!-- SELECT ORGANIZATION TYPE / ADD ORGANIZATION -->
	<logic:present name="cooperationUnitBean">	
		<p class="mvert1 breadcumbs">
			<span>
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.cooperation.create.step"/> 1 : </strong>
			<bean:message key="researcher.activity.cooperation.create.cooperationInitialData" bundle="RESEARCHER_RESOURCES"/></span>
				 > 
			<span class="actual">
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.cooperation.create.step"/> 2 : </strong>
			<bean:message key="researcher.activity.cooperation.create.cooperationUnit" bundle="RESEARCHER_RESOURCES"/></span>
				 >
			<span>
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.cooperation.create.step"/> 3 : </strong>
			<bean:message key="researcher.activity.cooperation.create.collaborationForm" bundle="RESEARCHER_RESOURCES"/></span>	 
	 	</p>
	 	<br />
		<fr:view name="cooperationUnitBean" schema="cooperationParticipation.cooperationTypeView">				
			<fr:layout name="tabular-nonNullValues">
				<fr:property name="classes" value="tstyle2 thlight thtop thleft"/>
	    	    <fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,"/>
				<fr:property name="columnClasses" value="width10em, width35em"/>
				<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
			</fr:layout>
		</fr:view>
		
		<logic:messagesPresent message="true">
			<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
				<p><span class="error0"><bean:write name="messages" /></span></p>
			</html:messages>
		</logic:messagesPresent>
		
		<logic:notPresent name="cooperationUnitSchema" scope="request">
		 	<p class="mtop2 mbottom1">	
				<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createCooperationUseCase.step.insertDataExplanation2"/></strong>
			</p>	
			<div class="dinline forminline">
				<fr:form action="/activities/createCooperation.do?method=prepareAssociateUnitToCooperation">	
					<fr:edit id="cooperationUnitBean" name="cooperationUnitBean" schema="cooperationParticipation.unitType">
						<fr:destination name="invalid" path="/activities/createCooperation.do?method=prepareAssociateUnitToCooperation"/>
						<fr:destination name="postback" path="/activities/createCooperation.do?method=prepareAssociateUnitToCooperation"/>				
						<fr:layout>
							<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
							<fr:property name="columnClasses" value=",,tdclear tderror1"/>
						</fr:layout>
					</fr:edit>
					<div class="switchNone">
						<html:submit><bean:message key="button.continue" bundle="RESEARCHER_RESOURCES"/></html:submit>
					</div>
				</fr:form>
			</div>		
		</logic:notPresent>
		
		<logic:present name="cooperationUnitSchema" scope="request">
			<bean:define id="schema" name="cooperationUnitSchema" type="java.lang.String" scope="request" />
			<p class="mtop2 mbottom1">	
				<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createCooperationUseCase.step.insertDataExplanation3"/></strong>
			</p>
			<div class="dinline forminline">
				<fr:form action="/activities/createCooperation.do?method=prepareCreateParticipation">	
					<fr:edit id="cooperationUnitBean" name="cooperationUnitBean"  schema="<%= schema %>">
						<fr:destination name="invalid" path="/activities/createCooperation.do?method=prepareAssociateUnitToCooperation"/>
					<fr:layout>	
						<fr:property name="classes" value="tstyle5 thlight thleft mtop0 mbottom0"/>
						<fr:property name="columnClasses" value="width8em,width40em,tdclear tderror1"/>
					</fr:layout>
					</fr:edit>
					<table class="tstyle5 thlight thright mtop0">
						<tr>
							<td class="width8em"></td>
							<td class="width40em">
								<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.associate"/></html:submit>
							</td>
						</tr>
					</table>
				</fr:form>
				<fr:form action="/activities/createCooperation.do?method=prepareCreateCooperationParticipation">
					<fr:edit id="stateBean" name="cooperationUnitBean" visible="false" >
						<fr:destination name="cancel" path="/activities/activitiesManagement.do?method=listActivities"/>
					</fr:edit>
					<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.back"/></html:submit>
					<html:cancel><bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/></html:cancel>
				</fr:form>
			</div>
		</logic:present>
	</logic:present>
	
	<!-- SELECT COOPERATION COLABORATION FORM -->
	<logic:present name="collaborationFormBean">
		<p class="mvert1 breadcumbs">
			<span>
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.cooperation.create.step"/> 1 : </strong>
			<bean:message key="researcher.activity.cooperation.create.cooperationInitialData" bundle="RESEARCHER_RESOURCES"/></span>
				 > 
			<span>
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.cooperation.create.step"/> 2 : </strong>
			<bean:message key="researcher.activity.cooperation.create.cooperationUnit" bundle="RESEARCHER_RESOURCES"/></span>
				 >
			<span class="actual">
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.cooperation.create.step"/> 3 : </strong>
			<bean:message key="researcher.activity.cooperation.create.collaborationForm" bundle="RESEARCHER_RESOURCES"/></span>	 
	 	</p>
	 	<br />	
	 	<logic:messagesPresent message="true">
			<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
				<p><span class="error0"><bean:write name="messages" /></span></p>
			</html:messages>
		</logic:messagesPresent>	
		<fr:view name="collaborationFormBean" schema="cooperationParticipation.cooperationUnitView">				
			<fr:layout name="tabular-nonNullValues">
				<fr:property name="classes" value="tstyle2 thlight thtop thleft"/>
	    	    <fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,"/>
				<fr:property name="columnClasses" value="width10em, width35em"/>
				<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
			</fr:layout>
		</fr:view>
	 	<p class="mtop2 mbottom1">	
			<strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.createCooperationUseCase.step.insertDataExplanation4"/></strong>
		</p>	
		<div class="dinline forminline">
			<fr:form action="/activities/createCooperation.do?method=createParticipation">	
				<fr:edit id="collaborationFormBean" name="collaborationFormBean" schema="cooperationParticipation.collaborationForm">
					<fr:destination name="invalid" path="/activities/createCooperation.do?method=prepareCreateParticipation"/>
					<fr:destination name="cancel" path="/activities/activitiesManagement.do?method=listActivities" />				
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thleft mtop0"/>
						<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
				</fr:edit>
				<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.finish"/></html:submit>
				<html:cancel><bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/></html:cancel>
			</fr:form>
			<fr:form action="/activities/createCooperation.do?method=prepareAssociateUnitToCooperation">
				<fr:edit id="stateBean" name="collaborationFormBean" visible="false"></fr:edit>
				<html:submit><bean:message bundle="RESEARCHER_RESOURCES" key="button.back"/></html:submit>
			</fr:form>
		</div>			
	</logic:present>	
</logic:present>

<script type="text/javascript" language="javascript">
	switchGlobal();
</script>