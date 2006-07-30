<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" %> 

<em><bean:message key="administrative.office.services"/></em>
<h2><bean:message key="documents.requirement"/></h2>

<logic:messagesPresent message="true">
	<span class="error"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
</logic:messagesPresent>

<logic:present name="student">
	<html:form action="/documentRequest">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewDocumentRequestsToCreate" />
		
		<p class="mbottom05">Identificação:</p>
		<table class="tstyle4 thlight thright mtop05">
			<tr>
				<th>
					<bean:message key="student.male.capitalized"/>:
				</th>
				<td colspan="3">
					<bean:write name="student" property="person.name"/>:
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="label.number"/>:
				</th>
				<td>
					<bean:write name="student" property="number"/>:
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="label.curricularplan"/>:
				</th>
				<td>
					<logic:present name="studentCurricularPlans">
						<bean:define id="studentCurricularPlans" name="student" property="studentCurricularPlans"/>
						<html:select property="scpId" onchange="this.form.method.value='onSCPChange';this.form.submit()">
							<html:options collection="studentCurricularPlans" property="idInternal" labelProperty="degreeCurricularPlan.name"/>
						</html:select>
					</logic:present>
					<logic:notPresent name="studentCurricularPlans">
						<bean:write name="student" property="activeStudentCurricularPlan.degreeCurricularPlan.name"/>
					</logic:notPresent>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="address"/>:
				</th>
				<td>
					<bean:write name="student" property="person.address"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="areaCode"/>:
				</th>
				<td>
					<bean:write name="student" property="person.areaCode"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="areaOfAreaCode"/>:
				</th>
				<td>
					<bean:write name="student" property="person.areaOfAreaCode"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="mobile"/>:
				</th>
				<td>
					<bean:write name="student" property="person.mobile"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="phone"/>:
				</th>
				<td>
					<bean:write name="student" property="person.phone"/>
				</td>
			</tr>
			<tr>
				<th>
					<bean:message key="email"/>:
				</th>
				<td>
					<bean:write name="student" property="person.email"/>
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



		<p class="mbottom05"><%--<bean:message key="document.to.request"/>--%>Escolha a(s) certidões que deseja requerer:</p>
 		
			<table class="tstyle2 mtop05">
				<e:labelValues id="documentRequestTypes" enumeration="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" bundle="ENUMERATION_RESOURCES"/>
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
						
						<logic:equal name="documentRequestType" property="value" value="DEGREE_FINALIZATION_CERTIFICATE">
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
						</logic:equal>	
					
					</tr>
				</logic:iterate>
			</table>

			
<%--
		<div class="mtop2">
			<e:labelValues id="documentRequestTypes" enumeration="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" bundle="ENUMERATION_RESOURCES"/>
			<logic:iterate id="documentRequestType" name="documentRequestTypes">
				<p style="margin-top: 1em;">
					<html:multibox property="chosenDocumentRequestTypes">
						<bean:write name="documentRequestType" property="value"/>
					</html:multibox>
					<bean:write name="documentRequestType" property="label"/>
				</p>
				<logic:equal name="documentRequestType" property="value" value="SCHOOL_REGISTRATION_CERTIFICATE">
					<p style="margin-left: 2em;">
						<bean:message key="label.executionYear"/>:<br/>
						<html:select property="schoolRegistrationExecutionYearId" value="currentExecutionYearId" >
							<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
						</html:select>
					</p>
				</logic:equal>
				<logic:equal name="documentRequestType" property="value" value="ENROLMENT_CERTIFICATE">
					<p style="margin-left: 2em;">
						<bean:message key="label.executionYear"/>:<br/>
						<html:select property="enrolmentExecutionYearId" value="currentExecutionYearId" >
							<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
						</html:select>
						<br/><br/>
						<bean:message key="detailed"/>?<br/>
						<html:radio property="enrolmentDetailed" value="true"/><bean:message key="label.yes"/><br/>
						<html:radio property="enrolmentDetailed" value="false"/><bean:message key="label.no"/>
					</p>
				</logic:equal>
				<logic:equal name="documentRequestType" property="value" value="DEGREE_FINALIZATION_CERTIFICATE">
					<p style="margin-left: 2em; border-color: gray; border: 2">
						<bean:message key="average"/><br/>
						<html:radio property="degreeFinalizationAverage" value="true"/><bean:message key="label.yes"/><br/>
						<html:radio property="degreeFinalizationAverage" value="false"/><bean:message key="label.no"/><br/>
						<br/>
						<bean:message key="detailed"/>?<br/>
						<html:radio property="degreeFinalizationDetailed" value="true"/><bean:message key="label.yes"/><br/>
						<html:radio property="degreeFinalizationDetailed" value="false"/><bean:message key="label.no"/>
					</p>
				</logic:equal>
			</logic:iterate>
		</div>
--%>
			
			
			
			
			
			
			<p class="mtop2">
				<bean:message key="document.purpose"/>:
				<e:labelValues id="documentPurposeTypes" enumeration="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType" bundle="ENUMERATION_RESOURCES"/>
				<logic:iterate id="documentPurposeType" name="documentPurposeTypes">
					<p>
						<html:radio property="chosenDocumentPurposeType" idName="documentPurposeType" value="value"/><bean:write name="documentPurposeType" property="label"/>
						<logic:equal name="documentPurposeType" property="value" value="OTHER">
							<html:text property="otherPurpose" size="40"/>
						</logic:equal>
					</p>
				</logic:iterate>
			</p>
			
			<p class="mtop2">
				<bean:message key="notes"/>:
				<p>
					<html:textarea property="notes" cols="70" rows="2"/>
				</p>
			</p>
			
			<p style="margin-top: 2em;">
				<bean:message key="urgency.charge"/><br/>
				<div class="warning0"><bean:message key="urgency.charge.explanation"/></div>
				
				<p>
					<html:radio property="urgentRequest" value="true"/><bean:message key="label.yes"/>
				</p>
				<p>
					<html:radio property="urgentRequest" value="false"/><bean:message key="label.no"/>
				</p>
			</p>
			
			<p class="mtop2"><html:submit styleClass="inputbutton"><bean:message key="button.continue"/></html:submit></p>


		</logic:present>
		
	</html:form>
	
</logic:present>
