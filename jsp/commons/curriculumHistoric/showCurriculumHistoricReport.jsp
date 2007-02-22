<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.util.EnrolmentEvaluationState" %>

<html:xhtml/>

<span class="error"><!-- Error messages go here --><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/></span>

<logic:present name="infoCurriculumHistoricReport">
	<bean:define id="executionYear" name="infoCurriculumHistoricReport" property="executionYear"/>
	<bean:define id="semester" name="infoCurriculumHistoricReport" property="semester" type="java.lang.Integer"/>
	
	<h3 class="bluetxt">
		<bean:message key="message.teachingReport.executionYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
		&nbsp;<bean:write name="executionYear" property="year" />*&nbsp;-&nbsp;
		<bean:message key="label.period" arg0="<%=semester.toString()%>" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
	</h3>
	
	<table width="90%" border="0" cellspacing="1" style="mbottom1">
		<tr>
			<td width="35%">
				<strong><bean:message key="message.teachingReport.curricularName" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong>
			</td>
			<td>
				<bean:write name="infoCurriculumHistoricReport" property="curricularCourse.name"/>
					&nbsp;- &nbsp;
				<bean:write name="infoCurriculumHistoricReport" property="curricularCourse.degreeCurricularPlan.name"/>
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.IN" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" property="enroled"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AV" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" property="evaluated"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" property="approved"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP/IN" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" property="ratioApprovedEnroled"/>%</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP/AV" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" property="ratioApprovedEvaluated"/>%</td>
		</tr>
	</table>

	<logic:notEmpty name="infoCurriculumHistoricReport" property="enrolments">
		<h3 class="bluetxt"><bean:message key="label.students.enrolled.exam" bundle="CURRICULUM_HISTORIC_RESOURCES"/></h3>
		<table cellspacing="1" cellpadding="1">
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.number" bundle="CURRICULUM_HISTORIC_RESOURCES" /> 
		   		</th>
				<th class="listClasses-header">
					<bean:message key="label.name" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>		
			   	<th class="listClasses-header">
					<bean:message key="label.Degree" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
			   	<th class="listClasses-header">
					<bean:message key="label.normal.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
			   	<th class="listClasses-header">
					<bean:message key="label.special.season.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
			   	<th class="listClasses-header">
					<bean:message key="label.improvment.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
			   	<th class="listClasses-header">
					<bean:message key="label.equivalence.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
				<th class="listClasses-header">
					<bean:message key="label.mark" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			   	</th>
			</tr>
			<logic:iterate id="enrolment" name="infoCurriculumHistoricReport" property="enrolments">
				<tr>
					<td class="listClasses">
					 	<bean:write name="enrolment" property="studentCurricularPlan.registration.number"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrolment" property="studentCurricularPlan.registration.person.nome"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrolment" property="studentCurricularPlan.degreeCurricularPlan.degree.sigla"/>
					 </td>

					 <td class="listClasses">
						<logic:notPresent name="enrolment" property="latestNormalEnrolmentEvaluation">
							--
						</logic:notPresent>

					 	<logic:present name="enrolment" property="latestNormalEnrolmentEvaluation">
					 		<logic:notPresent name="enrolment" property="latestNormalEnrolmentEvaluation.grade">								
								<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
							</logic:notPresent>
						 	<logic:present name="enrolment" property="latestNormalEnrolmentEvaluation.grade">
							 	<logic:equal name="enrolment" property="latestNormalEnrolmentEvaluation.grade" value="NA">
									<logic:notEqual name="enrolment" property="latestNormalEnrolmentEvaluation.enrolmentEvaluationState" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
									</logic:notEqual>
									<logic:equal name="enrolment" property="latestNormalEnrolmentEvaluation.enrolmentEvaluationState" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.notEvaluated" bundle="ENUMERATION_RESOURCES" />
									</logic:equal>
								</logic:equal>
								<logic:equal name="enrolment" property="latestNormalEnrolmentEvaluation.grade" value="RE">
									<logic:notEqual name="enrolment" property="latestNormalEnrolmentEvaluation.enrolmentEvaluationState" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
									</logic:notEqual>
									<logic:equal name="enrolment" property="latestNormalEnrolmentEvaluation.enrolmentEvaluationState" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.notApproved" bundle="ENUMERATION_RESOURCES" />
									</logic:equal>									
								</logic:equal>
								<logic:equal name="enrolment" property="latestNormalEnrolmentEvaluation.grade" value="AP">
									<logic:notEqual name="enrolment" property="latestNormalEnrolmentEvaluation.enrolmentEvaluationState" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
									</logic:notEqual>
									<logic:equal name="enrolment" property="latestNormalEnrolmentEvaluation.enrolmentEvaluationState" 
									
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.approved" bundle="ENUMERATION_RESOURCES" />
									</logic:equal>																		
								</logic:equal>
								<logic:greaterThan name="enrolment" property="latestNormalEnrolmentEvaluation.grade" value="0">
									 <logic:lessThan name="enrolment" property="latestNormalEnrolmentEvaluation.grade" value="101">
										<logic:notEqual name="enrolment" property="latestNormalEnrolmentEvaluation.enrolmentEvaluationState" 
											value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
											<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
										</logic:notEqual>
										<logic:equal name="enrolment" property="latestNormalEnrolmentEvaluation.enrolmentEvaluationState" 
											value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
											<bean:write name="enrolment" property="latestNormalEnrolmentEvaluation.grade"/>
										</logic:equal>	
									</logic:lessThan>
								</logic:greaterThan>								
							</logic:present>
						</logic:present>
					 </td>

					 <td class="listClasses">
						<logic:notPresent name="enrolment" property="latestSpecialSeasonEnrolmentEvaluation">
							--
						</logic:notPresent>

					 	<logic:present name="enrolment" property="latestSpecialSeasonEnrolmentEvaluation">
					 		<logic:empty name="enrolment" property="latestSpecialSeasonEnrolmentEvaluation.grade">
								<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
							</logic:empty>
						 	<logic:notEmpty name="enrolment" property="latestSpecialSeasonEnrolmentEvaluation.grade">
							 	<logic:equal name="enrolment" property="latestSpecialSeasonEnrolmentEvaluation.grade" value="NA">
									<bean:message key="msg.notEvaluated" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrolment" property="latestSpecialSeasonEnrolmentEvaluation.grade" value="RE">
									<bean:message key="msg.notApproved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrolment" property="latestSpecialSeasonEnrolmentEvaluation.grade" value="AP">
									<bean:message key="msg.approved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:greaterThan name="enrolment" property="latestSpecialSeasonEnrolmentEvaluation.grade" value="0">
									<logic:lessThan name="enrolment" property="latestSpecialSeasonEnrolmentEvaluation.grade" value="101">
										<bean:write name="enrolment" property="latestSpecialSeasonEnrolmentEvaluation.grade"/>
									</logic:lessThan>
								</logic:greaterThan>
							</logic:notEmpty>
						</logic:present>
					 </td>

					 <td class="listClasses">
						<logic:notPresent name="enrolment" property="latestImprovementEnrolmentEvaluation">
							--
						</logic:notPresent>

					 	<logic:present name="enrolment" property="latestImprovementEnrolmentEvaluation">
					 		<logic:empty name="enrolment" property="latestImprovementEnrolmentEvaluation.grade">
								<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
							</logic:empty>
						 	<logic:notEmpty name="enrolment" property="latestImprovementEnrolmentEvaluation.grade">
							 	<logic:equal name="enrolment" property="latestImprovementEnrolmentEvaluation.grade" value="NA">
									<bean:message key="msg.notEvaluated" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrolment" property="latestImprovementEnrolmentEvaluation.grade" value="RE">
									<bean:message key="msg.notApproved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrolment" property="latestImprovementEnrolmentEvaluation.grade" value="AP">
									<bean:message key="msg.approved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:greaterThan name="enrolment" property="latestImprovementEnrolmentEvaluation.grade" value="0">
									<logic:lessThan name="enrolment" property="latestImprovementEnrolmentEvaluation.grade" value="101">
										<bean:write name="enrolment" property="latestImprovementEnrolmentEvaluation.grade"/>
									</logic:lessThan>
								</logic:greaterThan>
							</logic:notEmpty>
						</logic:present>
					 </td>

					 <td class="listClasses">
						<logic:notPresent name="enrolment" property="latestEquivalenceEnrolmentEvaluation">
							--
						</logic:notPresent>

					 	<logic:present name="enrolment" property="latestEquivalenceEnrolmentEvaluation">
					 		<logic:empty name="enrolment" property="latestEquivalenceEnrolmentEvaluation.grade">
								<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
							</logic:empty>
						 	<logic:notEmpty name="enrolment" property="latestEquivalenceEnrolmentEvaluation.grade">
							 	<logic:equal name="enrolment" property="latestEquivalenceEnrolmentEvaluation.grade" value="NA">
									<bean:message key="msg.notEvaluated" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrolment" property="latestEquivalenceEnrolmentEvaluation.grade" value="RE">
									<bean:message key="msg.notApproved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrolment" property="latestEquivalenceEnrolmentEvaluation.grade" value="AP">
									<bean:message key="msg.approved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:greaterThan name="enrolment" property="latestEquivalenceEnrolmentEvaluation.grade" value="0">
									<logic:lessThan name="enrolment" property="latestEquivalenceEnrolmentEvaluation.grade" value="101">
										<bean:write name="enrolment" property="latestEquivalenceEnrolmentEvaluation.grade"/>
									</logic:lessThan>
								</logic:greaterThan>
							</logic:notEmpty>
						</logic:present>
					 </td>

					 <td class="listClasses">
					 	<logic:equal name="enrolment" property="enrolmentStateApproved" value="false">
							<bean:message name="enrolment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
						</logic:equal>
				
					 	<logic:equal name="enrolment" property="enrolmentStateApproved" value="true">
							<logic:equal name="enrolment" property="latestEnrolmentEvaluation.grade" value="AP">
								<bean:message key="msg.approved" bundle="ENUMERATION_RESOURCES" />
							</logic:equal>
							<logic:notEqual name="enrolment" property="latestEnrolmentEvaluation.grade" value="AP">
								<bean:write name="enrolment" property="latestEnrolmentEvaluation.grade"/>
							</logic:notEqual>
						</logic:equal>
					 </td>

				</tr>
			</logic:iterate>
		</table>

	</logic:notEmpty>

	<br />

	<p>
		<bean:message key="message.teachingReport.note1" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
		<br />
		<bean:message key="message.teachingReport.note2" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
	</p>

</logic:present>
