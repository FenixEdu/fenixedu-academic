<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.candidacy.create" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<fr:form action="/caseHandlingSecondCycleIndividualCandidacyProcess.do">
 	<html:hidden property="method" value="createNewProcess" />

	<fr:edit id="secondCycleIndividualCandidacyProcessBean" name="secondCycleIndividualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="secondCycleIndividualCandidacyProcessBean" property="candidacyProcess">
	
		<h3 class="mtop15 mbottom025"><bean:message key="label.selectDegree" bundle="APPLICATION_RESOURCES"/>:</h3>
		<fr:edit id="secondCycleIndividualCandidacyProcessBean.degree"
			name="secondCycleIndividualCandidacyProcessBean"
			schema="SecondCycleIndividualCandidacyProcessBean.selectDegree.create">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid" />
			<fr:destination name="selectedDegreePostback" path="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillDegreeCandidacyInformationPostback" />
		</fr:edit>
		
		<logic:notEmpty name="secondCycleIndividualCandidacyProcessBean" property="selectedDegree">
		
			<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.precedentDegreeInformation" bundle="APPLICATION_RESOURCES"/>:</h3>
			
			<logic:notEmpty name="secondCycleIndividualCandidacyProcessBean" property="precedentDegreeType">
				
				<logic:equal name="secondCycleIndividualCandidacyProcessBean" property="validPrecedentDegreeInformation" value="true">
					<bean:define id="precedentDegreeTypeName" name="secondCycleIndividualCandidacyProcessBean" property="precedentDegreeType.name" />
					<bean:define id="schema">SecondCycleIndividualCandidacyProcessBean.precedentDegreeInformation.<bean:write name="precedentDegreeTypeName"/></bean:define>
					<fr:edit id="secondCycleIndividualCandidacyProcessBean.precedentDegreeInformation"
						name="secondCycleIndividualCandidacyProcessBean" schema="<%= schema.toString() + ".create" %>">
						<fr:layout name="tabular-editable">
							<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		    		    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
						</fr:layout>
						<fr:destination name="invalid" path="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid" />
						<fr:destination name="precedentDegreeTypePostback" path="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationPostback" />
					</fr:edit>
				</logic:equal>

				<logic:equal name="secondCycleIndividualCandidacyProcessBean" property="validPrecedentDegreeInformation" value="false">
					<fr:edit id="secondCycleIndividualCandidacyProcessBean.precedentDegreeInformation"
						name="secondCycleIndividualCandidacyProcessBean" schema="SecondCycleIndividualCandidacyProcessBean.precedentDegreeInformation.chooseType">
						<fr:layout name="tabular-editable">
							<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	    			    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
						</fr:layout>
						<fr:destination name="invalid" path="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid" />
						<fr:destination name="precedentDegreeTypePostback" path="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationPostback" />
					</fr:edit>
					<strong><em><bean:message key="label.candidacy.invalid.precedentDegree" bundle="APPLICATION_RESOURCES"/></em></strong>
				</logic:equal>
				
			</logic:notEmpty>
			
			<logic:empty name="secondCycleIndividualCandidacyProcessBean" property="precedentDegreeType">
				<fr:edit id="secondCycleIndividualCandidacyProcessBean.precedentDegreeInformation"
					name="secondCycleIndividualCandidacyProcessBean" schema="SecondCycleIndividualCandidacyProcessBean.precedentDegreeInformation.chooseType">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	    		    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
					</fr:layout>
					<fr:destination name="invalid" path="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid" />
					<fr:destination name="precedentDegreeTypePostback" path="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationPostback" />
				</fr:edit>
			</logic:empty>
			
			<h3 class="mtop15 mbottom025"><bean:message key="label.candidacy.information" bundle="APPLICATION_RESOURCES"/>:</h3>
			<fr:edit id="secondCycleIndividualCandidacyProcessBean.optionalInformation"
				name="secondCycleIndividualCandidacyProcessBean"
				schema="SecondCycleIndividualCandidacyProcessBean.optionalInformation">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
			        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
				</fr:layout>
				<fr:destination name="invalid" path="/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid" />
			</fr:edit>
			
		</logic:notEmpty>
		
	</logic:notEmpty>
	
	<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
