<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="administrative.office.services"/></em>
<h2><bean:message key="documents.requirement"/></h2>


<logic:messagesPresent message="true">
	<span class="error"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
</logic:messagesPresent>
<logic:present name="registration">
	<html:form action="/documentRequest">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewDocumentRequestsToCreate" />
		<html:hidden property="registrationId"/>
		<p class="mbottom05">Identificação:</p>
		<table class="tstyle4 thlight thright mtop05">
			<tr>
				<th>
					<bean:message key="student.male.capitalized"/>:
				</th>
				<td colspan="3">
					<bean:write name="registration" property="person.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="label.number"/>:
				</th>
				<td>
					<bean:write name="registration" property="number"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="label.curricularplan"/>:
				</th>
				<td>
					<logic:present name="studentCurricularPlans">
						<bean:define id="studentCurricularPlans" name="registration" property="studentCurricularPlans"/>
						<html:select property="scpId" onchange="this.form.method.value='onSCPChange';this.form.submit()">
							<html:options collection="studentCurricularPlans" property="idInternal" labelProperty="degreeCurricularPlan.name"/>
						</html:select>
					</logic:present>
					<logic:notPresent name="studentCurricularPlans">
						<bean:write name="registration" property="activeStudentCurricularPlan.degreeCurricularPlan.name"/>
					</logic:notPresent>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="address"/>:
				</th>
				<td>
					<bean:write name="registration" property="person.address"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="areaCode"/>:
				</th>
				<td>
					<bean:write name="registration" property="person.areaCode"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="areaOfAreaCode"/>:
				</th>
				<td>
					<bean:write name="registration" property="person.areaOfAreaCode"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="mobile"/>:
				</th>
				<td>
					<bean:write name="registration" property="person.mobile"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="phone"/>:
				</th>
				<td>
					<bean:write name="registration" property="person.phone"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="email"/>:
				</th>
				<td>
					<bean:write name="registration" property="person.email"/>
				</td>
			</tr>
		</table>


<logic:messagesPresent message="true">
	<p class="mtop05 mbottom1">
	<span class="error"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
	</p>
</logic:messagesPresent>


		<logic:present name="executionYears">



		<p class="mtop15 mbottom05"><bean:message key="label.choose.documentToRequest" bundle="STUDENT_RESOURCES"/></p>
 		
			<table class="tstyle2 mtop05">
				<e:labelValues 
					id="documentRequestTypes" 
					enumeration="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" 
					bundle="ENUMERATION_RESOURCES"
					excludedFields="SCHOOL_REGISTRATION_DECLARATION,ENROLMENT_DECLARATION,IRS_DECLARATION,DEGREE_DIPLOMA,DEGREE_FINALIZATION_CERTIFICATE,APPROVEMENT_CERTIFICATE"/>
				<logic:iterate id="documentRequestType" name="documentRequestTypes">
					<tr>
						<td>
							<html:multibox property="chosenDocumentRequestTypes">
								<bean:write name="documentRequestType" property="value"/>
							</html:multibox>
							<bean:write name="documentRequestType" property="label"/>
						</td>
						
						<logic:equal name="documentRequestType" property="value" value="SCHOOL_REGISTRATION_CERTIFICATE">
						<td>
							<bean:message key="label.executionYear"/>:
							<html:select property="schoolRegistrationExecutionYearId" value="currentExecutionYearId" >
								<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
							</html:select>
						</td>
						</logic:equal>
	
						<logic:equal name="documentRequestType" property="value" value="SCHOOL_REGISTRATION_CERTIFICATE">
						<td></td>
						</logic:equal>
							
						<logic:equal name="documentRequestType" property="value" value="ENROLMENT_CERTIFICATE">
						<td>
							<bean:message key="label.executionYear"/>:
							<html:select property="enrolmentExecutionYearId" value="currentExecutionYearId" >
								<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
							</html:select>
							<span class="pleft2">
								<bean:message key="detailed"/>?
								<html:radio property="enrolmentDetailed" value="true"/><bean:message key="label.yes"/>
								<html:radio property="enrolmentDetailed" value="false"/><bean:message key="label.no"/>
							</span>
						</td>
						</logic:equal>
	
						<logic:equal name="documentRequestType" property="value" value="ENROLMENT_CERTIFICATE">
						<td></td>
						</logic:equal>
						
				<!--		<logic:equal name="documentRequestType" property="value" value="DEGREE_FINALIZATION_CERTIFICATE">
						<td>
							<bean:message key="average"/>
							<html:radio property="degreeFinalizationAverage" value="true"/><bean:message key="label.yes"/>
							<html:radio property="degreeFinalizationAverage" value="false"/><bean:message key="label.no"/>
							<span class="pleft2">
								<bean:message key="detailed"/>?
								<html:radio property="degreeFinalizationDetailed" value="true"/><bean:message key="label.yes"/>
								<html:radio property="degreeFinalizationDetailed" value="false"/><bean:message key="label.no"/>
							</span>
						</td>
						</logic:equal>
	
						<logic:equal name="documentRequestType" property="value" value="DEGREE_FINALIZATION_CERTIFICATE">
						<td></td>					
						</logic:equal>	-->
					
					</tr>
				</logic:iterate>
			</table>
			
			<div class="mtop2">
				<bean:message key="document.purpose"/>:
				<e:labelValues 
					id="documentPurposeTypes" 
					enumeration="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType" 
					bundle="ENUMERATION_RESOURCES"/>
				<logic:iterate id="documentPurposeType" name="documentPurposeTypes">
					<p>
						<html:radio property="chosenDocumentPurposeType" idName="documentPurposeType" value="value"/><bean:write name="documentPurposeType" property="label"/>
						<logic:equal name="documentPurposeType" property="value" value="OTHER">
							<html:text property="otherPurpose" size="40"/>
						</logic:equal>
					</p>
				</logic:iterate>
			</div>
			
			<div class="mtop2">
				<bean:message key="notes"/> (<bean:message key="label.optional.lowercase" bundle="STUDENT_RESOURCES"/>):
				<p>
					<html:textarea property="notes" cols="70" rows="2"/>
				</p>
			</div>
			
			<div style="margin-top: 2em;">
				<div class="infoop2">
					<p class="mvert05"><bean:message key="label.urgency.charge" bundle="STUDENT_RESOURCES"/></p>
					<p class="mvert05"><bean:message key="urgency.charge.explanation"/></p>
				</div>
				<p><bean:message key="urgency.charge"/>
					<html:radio property="urgentRequest" value="true"/><bean:message key="label.yes"/> &nbsp;
					<html:radio property="urgentRequest" value="false"/><bean:message key="label.no"/>
				</p>
			</div>
			
			<p class="mtop2"><html:submit styleClass="inputbutton"><bean:message key="button.continue"/></html:submit></p>

		</p>

		</logic:present>
		
	</html:form>
	
</logic:present>
