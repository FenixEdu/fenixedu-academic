<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<jsp:include page="thesisContextHeader.jsp"/>

<br/>

<logic:present name="manageThesisContext">
	<div class="infoop8">
		<table width="100%">
			<tr>
				<th width="30%">
					<bean:message key="label.coordinator.thesis.periods.and.rules"/>
				</th>
				<th width="30%">
					<bean:message key="label.coordinator.thesis.proposals"/>
				</th>
				<th width="30%">
					<bean:message key="label.coordinator.thesis.evaluation.process"/>
				</th>
			</tr>
			<tr>
				<td class="infoop8">
					<bean:message key="label.thesis.process.proposal.period"/>:
					<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<logic:notPresent name="manageThesisContext" property="scheduleing">
						<bean:message key="label.thesis.process.period.not.defined"/>
					</logic:notPresent>
					<logic:present name="manageThesisContext" property="scheduleing">
						<bean:define id="scheduleing" name="manageThesisContext" property="scheduleing"/>
						<logic:notPresent name="scheduleing" property="proposalInterval">
							<bean:message key="label.thesis.process.period.not.defined"/>
						</logic:notPresent>					
						<logic:present name="scheduleing" property="proposalInterval">
							<fr:view name="scheduleing" property="proposalInterval.start"/>
							-
							<fr:view name="scheduleing" property="proposalInterval.end"/>
						</logic:present>					
					</logic:present>
					<br/>
					<br/>
					<bean:message key="label.thesis.process.candidacy.period"/>:
					<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<logic:notPresent name="manageThesisContext" property="scheduleing">
						<bean:message key="label.thesis.process.period.not.defined"/>
					</logic:notPresent>
					<logic:present name="manageThesisContext" property="scheduleing">
						<bean:define id="scheduleing" name="manageThesisContext" property="scheduleing"/>
						<logic:notPresent name="scheduleing" property="candidacyInterval">
							<bean:message key="label.thesis.process.period.not.defined"/>
						</logic:notPresent>					
						<logic:present name="scheduleing" property="candidacyInterval">
							<fr:view name="scheduleing" property="candidacyInterval.start"/>
							-
							<fr:view name="scheduleing" property="candidacyInterval.end"/>
						</logic:present>					
					</logic:present>
					<br/>
					<br/>
					<bean:message key="label.thesis.process.creation.period"/>:
					<br/>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<logic:present name="manageThesisContext" property="executionDegree.beginThesisCreationPeriod">
						<bean:write name="manageThesisContext" property="executionDegree.beginThesisCreationPeriod"/>
						-
						<bean:write name="manageThesisContext" property="executionDegree.endThesisCreationPeriod"/>
					</logic:present>
					<logic:notPresent name="manageThesisContext" property="executionDegree.beginThesisCreationPeriod">
						<bean:message key="label.thesis.process.period.not.defined"/>
					</logic:notPresent>
					<br/>
					<br/>
					<logic:notPresent name="manageThesisContext" property="scheduleing">
						<bean:message key="label.thesis.process.candidacy.conditions.not.defined"/>
					</logic:notPresent>
					<logic:present name="manageThesisContext" property="scheduleing">
						<bean:define id="scheduleing" name="manageThesisContext" property="scheduleing"/>
						<logic:notEqual name="scheduleing" property="areCandidacyConditionsDefined" value="true">
							<font color="red">
								<bean:message key="label.thesis.process.candidacy.conditions.not.defined"/>
							</font>
						</logic:notEqual>					
						<logic:equal name="scheduleing" property="areCandidacyConditionsDefined" value="true">
							<font color="green">
								<bean:message key="label.thesis.process.candidacy.conditions.defined"/>
							</font>
						</logic:equal>					
					</logic:present>
				</td>
				<td class="infoop8">
					...
				</td>
				<td class="infoop8">
					...
				</td>
			</tr>
		</table>
	</div>
</logic:present>
