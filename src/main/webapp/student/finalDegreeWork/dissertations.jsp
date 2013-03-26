<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.finalDegreeWork.dissertations"/></h2>

<div class="inobullet mvert15">
	<!-- Error messages go here -->
	<html:errors />
</div>

<logic:present name="infoExecutionDegrees">
	<html:form action="/finalDegreeWorkCandidacy" focus="executionDegreeOID">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="somemethod"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
		<p class="mbottom05">
			<bean:message key="label.finalDegreeWork.dissertations.info"/>:
		</p>
		<p class="mvert05">
			<html:select bundle="HTMLALT_RESOURCES" property="executionYearOID" size="1"
						 onchange='this.form.method.value=\'selectDissertationsExecutionYear\';this.form.page.value=\'0\';this.form.submit();'>
				<html:option value=""><!-- w3c complient--></html:option>
				<html:options property="idInternal"
							  labelProperty="nextYearsYearString"
							  collection="executionYears" />
			</html:select>
		</p>
		<p class="mvert05">
			<html:select bundle="HTMLALT_RESOURCES" property="executionDegreeOID" size="1"
						 onchange='this.form.method.value=\'selectDissertationsExecutionDegree\';this.form.page.value=\'0\';this.form.submit();'>
				<html:option value=""><!-- w3c complient--></html:option>
				<html:options property="idInternal"
							  labelProperty="infoDegreeCurricularPlan.label"
							  collection="infoExecutionDegrees" />
			</html:select>
		</p>
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
		<logic:present name="executionDegree">
		
			<p class="mtop25 mbottom05"><strong><bean:message key="finalDegreeWorkProposal.setProposalPeriod.header" bundle="APPLICATION_RESOURCES"/></strong></p>
			<logic:present name="scheduling">
				<logic:present name="scheduling" property="startOfProposalPeriod">
					<logic:present name="scheduling" property="endOfProposalPeriod">
						<table class="tstyle2 thlight thleft tdcenter mtop05">
							<tr>
				   				<th>
				   					<bean:message key="finalDegreeWorkProposal.setProposalPeriod.start" bundle="APPLICATION_RESOURCES"/>:
				   				</th>
				   				<td>
				   					<bean:write name="scheduling" property="startOfProposalPeriodDateYearMonthDay"/>,
				   					<bean:write name="scheduling" property="startOfProposalPeriodTimeHourMinuteSecond"/>
				   				</td>
				   			</tr>
				   			<tr>
								<th>
									<bean:message key="finalDegreeWorkProposal.setProposalPeriod.end" bundle="APPLICATION_RESOURCES"/>:
								</th>
								<td>
									<bean:write name="scheduling" property="endOfProposalPeriodDateYearMonthDay"/>,
									<bean:write name="scheduling" property="endOfProposalPeriodTimeHourMinuteSecond"/>
								</td>
							</tr>
				   		</table>
			   		</logic:present>
		   		</logic:present>
	   		</logic:present>
	   		<logic:notPresent name="scheduling" property="startOfProposalPeriod">
				<logic:notPresent name="scheduling" property="endOfProposalPeriod">
					<p><em><bean:message key="error.message.ProposalPeriodNotDefined" bundle="STUDENT_RESOURCES"/></em></p>
		   		</logic:notPresent>
	   		</logic:notPresent>

   			<p class="mtop15 mbottom05"><strong><bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.header" bundle="APPLICATION_RESOURCES"/></strong></p>
			<logic:present name="scheduling">
				<logic:present name="scheduling" property="startOfCandidacyPeriod">
					<logic:present name="scheduling" property="endOfCandidacyPeriod">
						<table class="tstyle2 thlight thleft tdcenter mtop05">
		    				<tr>
		    					<th>
		    						<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.start" bundle="APPLICATION_RESOURCES"/>:
		    					</th>
		    					<td>
		    						<bean:write name="scheduling" property="startOfCandidacyPeriodDateYearMonthDay"/>,
		    						<bean:write name="scheduling" property="startOfCandidacyPeriodTimeHourMinuteSecond"/>
		    					</td>
		    				</tr>
							<tr>
		    					<th>
		    						<bean:message key="finalDegreeWorkCandidacy.setCandidacyPeriod.end" bundle="APPLICATION_RESOURCES"/>:
		    					</th>
		    					<td>
		    						<bean:write name="scheduling" property="endOfCandidacyPeriodDateYearMonthDay"/>,
		    						<bean:write name="scheduling" property="endOfCandidacyPeriodTimeHourMinuteSecond"/>
		    					</td>
		    				</tr>
		    			</table>
		   			</logic:present>
		   		</logic:present>
	   		</logic:present>
	   		<logic:notPresent name="scheduling" property="startOfCandidacyPeriod">
		   		<logic:notPresent name="scheduling" property="endOfCandidacyPeriod">
		   			<p><em><bean:message key="error.message.CandidacyPeriodNotDefinedException" bundle="STUDENT_RESOURCES"/></em></p>
		   		</logic:notPresent>
		   	</logic:notPresent>

	   		<p class="mtop15 mbottom05"><strong><bean:message key="finalDegreeWorkCandidacy.setRequirements.header" bundle="APPLICATION_RESOURCES"/></strong></p>
			<logic:present name="scheduling">
				<table class="tstyle2 thlight thleft tdcenter mtop05">
					<tr>
						<th>
							<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCreditsFirstCycle" bundle="APPLICATION_RESOURCES"/>:
						</th>
						<td>
							<logic:present name="scheduling" property="minimumCompletedCreditsFirstCycle">
								<bean:write name="scheduling" property="minimumCompletedCreditsFirstCycle"/>
							</logic:present>
							<logic:notPresent name="scheduling" property="minimumCompletedCreditsFirstCycle">
								-
							</logic:notPresent>
						</td>
					</tr>
					<tr>
						<th>
							<bean:message key="finalDegreeWorkCandidacy.requirements.minimumCompletedCreditsSecondCycle" bundle="APPLICATION_RESOURCES"/>:
						</th>
						<td>
						    <logic:present name="scheduling" property="minimumCompletedCreditsSecondCycle">
								<bean:write name="scheduling" property="minimumCompletedCreditsSecondCycle"/>
							</logic:present>
							<logic:notPresent name="scheduling" property="minimumCompletedCreditsSecondCycle">
								-
							</logic:notPresent>
						</td>
					</tr>
					<tr>
						<th>
							<bean:message key="finalDegreeWorkCandidacy.requirements.maximumNumberOfProposalCandidaciesPerGroup" bundle="APPLICATION_RESOURCES"/>:
						</th>
						<td>
							<logic:present name="scheduling" property="maximumNumberOfProposalCandidaciesPerGroup">
								<bean:write name="scheduling" property="maximumNumberOfProposalCandidaciesPerGroup"/>
							</logic:present>
							<logic:notPresent name="scheduling" property="maximumNumberOfProposalCandidaciesPerGroup">
								-
							</logic:notPresent>
						</td>
					</tr>
					<tr>
						<th>
							<bean:message key="finalDegreeWorkCandidacy.requirements.attributionByTeachers" bundle="APPLICATION_RESOURCES"/>:
						</th>
						<td>
							<logic:equal name="scheduling" property="attributionByTeachers" value="true">
								<bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES"/>
							</logic:equal>
							<logic:equal name="scheduling" property="attributionByTeachers" value="false">
								<bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES"/>
							</logic:equal>
							<logic:notPresent name="scheduling" property="attributionByTeachers">
								-
							</logic:notPresent>
						</td>
					</tr>
					<tr>
						<th>
							<bean:message key="finalDegreeWorkCandidacy.requirements.allowSimultaneousCoorientationAndCompanion" bundle="APPLICATION_RESOURCES"/>:
						</th>
						<td>
							<logic:equal name="scheduling" property="allowSimultaneousCoorientationAndCompanion" value="true">
								<bean:message key="label.yes.capitalized" bundle="APPLICATION_RESOURCES"/>
							</logic:equal>
							<logic:equal name="scheduling" property="allowSimultaneousCoorientationAndCompanion" value="false">
								<bean:message key="label.no.capitalized" bundle="APPLICATION_RESOURCES"/>
							</logic:equal>
							<logic:notPresent name="scheduling" property="allowSimultaneousCoorientationAndCompanion">
								-
							</logic:notPresent>
						</td>
					</tr>
				</table>
			</logic:present>
			<logic:empty name="scheduling">
				<p><em><bean:message key="message.candidacy.requirements.not.available" bundle="STUDENT_RESOURCES"/></em></p>
			</logic:empty>
		</logic:present>
	</html:form>
</logic:present>