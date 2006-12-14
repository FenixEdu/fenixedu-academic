<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.EnrollmentState" %>
<%@ page import="net.sourceforge.fenixedu.util.EnrolmentEvaluationState" %>
<span class="error"><!-- Error messages go here --><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/></span>

<logic:present name="infoCurriculumHistoricReport">
	<bean:define id="executionYear" name="infoCurriculumHistoricReport" property="infoExecutionYear"/>
	<bean:define id="semester" name="infoCurriculumHistoricReport" property="semester"/>
	<bean:define id="evaluated" name="infoCurriculumHistoricReport" property="evaluated" type="java.lang.Integer"/>
	<bean:define id="enrolled" name="infoCurriculumHistoricReport" property="enrolled" type="java.lang.Integer"/>
	<bean:define id="approved" name="infoCurriculumHistoricReport" property="aproved" type="java.lang.Integer"/>
	
	<h3 class="bluetxt"><bean:message key="message.teachingReport.executionYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
	&nbsp;<bean:write name="executionYear" property="year" />*&nbsp;-&nbsp;
	<bean:message key="label.period" arg0="<%= String.valueOf(semester) %>" bundle="CURRICULUM_HISTORIC_RESOURCES"/></h3>
	
	<table width="90%" border="0" cellspacing="1">
		<tr>
			<td width="35%"><strong><bean:message key="message.teachingReport.curricularName" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="infoCurriculumHistoricReport" 
							property="infoCurricularCourse.name"/>
							&nbsp;- &nbsp;
				<bean:write name="infoCurriculumHistoricReport" 
							property="infoCurricularCourse.infoDegreeCurricularPlan.name"/>
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.IN" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="enrolled" /></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AV" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="evaluated"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><bean:write name="approved"/></td>
		</tr>
		<% int ap_en = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP/IN" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><%= ap_en %>%</td>
		</tr>
		<% int ap_ev = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP/AV" bundle="CURRICULUM_HISTORIC_RESOURCES"/></strong></td>
			<td><%= ap_ev %>%</td>
		</tr>
	</table>
	<br/>
	<logic:notEmpty name="infoCurriculumHistoricReport" property="enrollments">
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
			<logic:iterate id="enrollment" name="infoCurriculumHistoricReport" property="enrollments">
				<tr>
					<td class="listClasses">
					 	<bean:write name="enrollment" property="infoStudentCurricularPlan.infoStudent.number"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrollment" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
					 </td>
					 <td class="listClasses">
					 	<bean:write name="enrollment" property="infoStudentCurricularPlan.infoDegreeCurricularPlan.infoDegree.sigla"/>
					 </td>
					 <td class="listClasses">
					 	<logic:present name="enrollment" property="infoNormalEnrolmentEvaluation">
					 		<logic:notPresent name="enrollment" property="infoNormalEnrolmentEvaluation.grade">								
								<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
							</logic:notPresent>
						 	<logic:present name="enrollment" property="infoNormalEnrolmentEvaluation.grade">
							 	<logic:equal name="enrollment" property="infoNormalEnrolmentEvaluation.grade" value="NA">
									<logic:notEqual name="enrollment" property="infoNormalEnrolmentEvaluation.state" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
									</logic:notEqual>
									<logic:equal name="enrollment" property="infoNormalEnrolmentEvaluation.state" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.notEvaluated" bundle="ENUMERATION_RESOURCES" />
									</logic:equal>
								</logic:equal>
								<logic:equal name="enrollment" property="infoNormalEnrolmentEvaluation.grade" value="RE">
									<logic:notEqual name="enrollment" property="infoNormalEnrolmentEvaluation.state" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
									</logic:notEqual>
									<logic:equal name="enrollment" property="infoNormalEnrolmentEvaluation.state" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.notApproved" bundle="ENUMERATION_RESOURCES" />
									</logic:equal>									
								</logic:equal>
								<logic:equal name="enrollment" property="infoNormalEnrolmentEvaluation.grade" value="AP">
									<logic:notEqual name="enrollment" property="infoNormalEnrolmentEvaluation.state" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
									</logic:notEqual>
									<logic:equal name="enrollment" property="infoNormalEnrolmentEvaluation.state" 
										value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
										<bean:message key="msg.approved" bundle="ENUMERATION_RESOURCES" />
									</logic:equal>																		
								</logic:equal>
								<logic:greaterThan name="enrollment" property="infoNormalEnrolmentEvaluation.grade" value="0">
									 <logic:lessThan name="enrollment" property="infoNormalEnrolmentEvaluation.grade" value="101">
										<logic:notEqual name="enrollment" property="infoNormalEnrolmentEvaluation.state" 
											value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
											<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
										</logic:notEqual>
										<logic:equal name="enrollment" property="infoNormalEnrolmentEvaluation.state" 
											value="<%= EnrolmentEvaluationState.FINAL_OBJ.toString() %>">
											<bean:write name="enrollment" property="infoNormalEnrolmentEvaluation.grade"/>
										</logic:equal>	
									</logic:lessThan>
								</logic:greaterThan>								
							</logic:present>
						</logic:present>
						<logic:notPresent name="enrollment" property="infoNormalEnrolmentEvaluation">
							--
						</logic:notPresent>
					 </td>
					 <td class="listClasses">
					 	<logic:present name="enrollment" property="infoSpecialSeasonEnrolmentEvaluation">
					 		<logic:empty name="enrollment" property="infoSpecialSeasonEnrolmentEvaluation.grade">
								<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
							</logic:empty>
						 	<logic:notEmpty name="enrollment" property="infoSpecialSeasonEnrolmentEvaluation.grade">
							 	<logic:equal name="enrollment" property="infoSpecialSeasonEnrolmentEvaluation.grade" value="NA">
									<bean:message key="msg.notEvaluated" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrollment" property="infoSpecialSeasonEnrolmentEvaluation.grade" value="RE">
									<bean:message key="msg.notApproved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrollment" property="infoSpecialSeasonEnrolmentEvaluation.grade" value="AP">
									<bean:message key="msg.approved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:greaterThan name="enrollment" property="infoSpecialSeasonEnrolmentEvaluation.grade" value="0">
									<logic:lessThan name="enrollment" property="infoSpecialSeasonEnrolmentEvaluation.grade" value="101">
										<bean:write name="enrollment" property="infoSpecialSeasonEnrolmentEvaluation.grade"/>
									</logic:lessThan>
								</logic:greaterThan>
							</logic:notEmpty>
						</logic:present>
						<logic:notPresent name="enrollment" property="infoSpecialSeasonEnrolmentEvaluation">
							--
						</logic:notPresent>
					 </td>
					 <td class="listClasses">
					 	<logic:present name="enrollment" property="infoImprovmentEnrolmentEvaluation">
					 		<logic:empty name="enrollment" property="infoImprovmentEnrolmentEvaluation.grade">
								<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
							</logic:empty>
						 	<logic:notEmpty name="enrollment" property="infoImprovmentEnrolmentEvaluation.grade">
							 	<logic:equal name="enrollment" property="infoImprovmentEnrolmentEvaluation.grade" value="NA">
									<bean:message key="msg.notEvaluated" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrollment" property="infoImprovmentEnrolmentEvaluation.grade" value="RE">
									<bean:message key="msg.notApproved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrollment" property="infoImprovmentEnrolmentEvaluation.grade" value="AP">
									<bean:message key="msg.approved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:greaterThan name="enrollment" property="infoImprovmentEnrolmentEvaluation.grade" value="0">
									<logic:lessThan name="enrollment" property="infoImprovmentEnrolmentEvaluation.grade" value="101">
										<bean:write name="enrollment" property="infoImprovmentEnrolmentEvaluation.grade"/>
									</logic:lessThan>
								</logic:greaterThan>
							</logic:notEmpty>
						</logic:present>
						<logic:notPresent name="enrollment" property="infoImprovmentEnrolmentEvaluation">
							--
						</logic:notPresent>
					 </td>
					 <td class="listClasses">
					 	<logic:present name="enrollment" property="infoEquivalenceEnrolmentEvaluation">
					 		<logic:empty name="enrollment" property="infoEquivalenceEnrolmentEvaluation.grade">
								<bean:message key="msg.enrolled" bundle="ENUMERATION_RESOURCES" />
							</logic:empty>
						 	<logic:notEmpty name="enrollment" property="infoEquivalenceEnrolmentEvaluation.grade">
							 	<logic:equal name="enrollment" property="infoEquivalenceEnrolmentEvaluation.grade" value="NA">
									<bean:message key="msg.notEvaluated" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrollment" property="infoEquivalenceEnrolmentEvaluation.grade" value="RE">
									<bean:message key="msg.notApproved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:equal name="enrollment" property="infoEquivalenceEnrolmentEvaluation.grade" value="AP">
									<bean:message key="msg.approved" bundle="ENUMERATION_RESOURCES" />
								</logic:equal>
								<logic:greaterThan name="enrollment" property="infoEquivalenceEnrolmentEvaluation.grade" value="0">
									<logic:lessThan name="enrollment" property="infoEquivalenceEnrolmentEvaluation.grade" value="101">
										<bean:write name="enrollment" property="infoEquivalenceEnrolmentEvaluation.grade"/>
									</logic:lessThan>
								</logic:greaterThan>
							</logic:notEmpty>
						</logic:present>
						<logic:notPresent name="enrollment" property="infoEquivalenceEnrolmentEvaluation">
							--
						</logic:notPresent>
					 </td>
					 <td class="listClasses">
					 	<logic:notEqual name="enrollment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
							<bean:message name="enrollment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
						</logic:notEqual>
				
						<logic:equal name="enrollment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
							<logic:equal name="enrollment" property="infoEnrolmentEvaluation.grade" value="AP">
								<bean:message key="msg.approved" bundle="ENUMERATION_RESOURCES" />
							</logic:equal>
							<logic:notEqual name="enrollment" property="infoEnrolmentEvaluation.grade" value="AP">
								<bean:write name="enrollment" property="infoEnrolmentEvaluation.grade"/>
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
			 
