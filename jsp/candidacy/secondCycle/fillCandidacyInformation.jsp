<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.candidacy.create" bundle="APPLICATION_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="secondCycleIndividualCandidacyProcessBean.precedentDegreeInformation" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<bean:define id="parentProcessId" name="parentProcess" property="idInternal" />

<fr:form action='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?parentProcessId=" + parentProcessId.toString() %>'>

 	<html:hidden property="method" value="createNewProcess" />
	<fr:edit id="secondCycleIndividualCandidacyProcessBean" name="secondCycleIndividualCandidacyProcessBean" visible="false" />

	<logic:notEmpty name="secondCycleIndividualCandidacyProcessBean" property="candidacyProcess">
	
		<fr:edit id="secondCycleIndividualCandidacyProcessBean.candidacyDate" 
			 name="secondCycleIndividualCandidacyProcessBean"
			 schema="SecondCycleIndividualCandidacyProcessBean.candidacyDate">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
		</fr:edit>
	
		<h3 class="mtop15 mbottom025"><bean:message key="label.selectDegree" bundle="APPLICATION_RESOURCES"/>:</h3>
		<fr:edit id="secondCycleIndividualCandidacyProcessBean.degree"
			name="secondCycleIndividualCandidacyProcessBean"
			schema="SecondCycleIndividualCandidacyProcessBean.selectDegree.manage">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
		        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
		</fr:edit>
		
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
					<fr:destination name="invalid" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
					<fr:destination name="precedentDegreeTypePostback" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationPostback&amp;parentProcessId=" + parentProcessId.toString() %>' />
				</fr:edit>
				<logic:equal name="secondCycleIndividualCandidacyProcessBean" property="externalPrecedentDegreeType" value="true">
					<em><bean:message key="label.candidacy.precedentDegree.externalPrecedentDegreeType" bundle="APPLICATION_RESOURCES"/></em> (<html:link action="/externalUnits.do?method=prepareSearch" target="_blank"><bean:message key="label.externalUnits" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link>)
				</logic:equal>
			</logic:equal>

			<logic:equal name="secondCycleIndividualCandidacyProcessBean" property="validPrecedentDegreeInformation" value="false">
				<fr:edit id="secondCycleIndividualCandidacyProcessBean.precedentDegreeInformation"
					name="secondCycleIndividualCandidacyProcessBean" schema="SecondCycleIndividualCandidacyProcessBean.precedentDegreeInformation.chooseType">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
    			    	<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
					</fr:layout>
					<fr:destination name="invalid" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
					<fr:destination name="precedentDegreeTypePostback" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationPostback&amp;parentProcessId=" + parentProcessId.toString() %>' />
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
				<fr:destination name="invalid" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
				<fr:destination name="precedentDegreeTypePostback" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationPostback&amp;parentProcessId=" + parentProcessId.toString() %>' />
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
			<fr:destination name="invalid" path='<%= "/caseHandlingSecondCycleIndividualCandidacyProcess.do?method=fillCandidacyInformationInvalid&amp;parentProcessId=" + parentProcessId.toString() %>'  />
		</fr:edit>
			
	</logic:notEmpty>
	
	<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel onclick="this.form.method.value='listProcesses'; return true;"><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>

</fr:form>
