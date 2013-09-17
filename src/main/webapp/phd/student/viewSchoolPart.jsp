<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd"%>

<logic:present name="registrationConclusionBean">
	<strong><bean:message  key="label.phd.studyPlan" bundle="PHD_RESOURCES"/></strong>
	
	<table>
		<tr>
			<%-- view registration conclusion information --%>
			<td>
				<fr:view schema="PhdIndividualProgramProcess.view.registration.conclusion.bean" name="registrationConclusionBean">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight mtop10" />
					</fr:layout>
				</fr:view>
			</td>
			
			<%-- show operations --%>
			<td>
				<ul class="operations">
					<%-- no operations available yet --%>
				</ul>
			</td>
		</tr>
	</table>
	<br/>
</logic:present>

<logic:notPresent name="registrationConclusionBean">
	<logic:notEmpty name="process" property="studyPlan">
		<strong><bean:message key="label.phd.studyPlan" bundle="PHD_RESOURCES" /></strong>
		<fr:view schema="PhdStudyPlan.description" name="process" property="studyPlan">
			<fr:layout name="tabular"><fr:property name="classes" value="tstyle2 thlight mtop10" /></fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:notPresent>
