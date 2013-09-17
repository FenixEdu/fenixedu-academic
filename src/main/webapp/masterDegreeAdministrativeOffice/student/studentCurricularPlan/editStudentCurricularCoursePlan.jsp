<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentInExtraCurricularCourse" %>



<h2 align="left"><bean:message key="title.studentCurricularPlan"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/alterStudentCurricularPlan">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
<bean:define id="externalId" name="studentCurricularPlan" property="externalId"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlanId" property="studentCurricularPlanId" value="<%= externalId.toString() %>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<table border="0" cellspacing="3" cellpadding="10">
	<tr>
		<td>
			<strong><bean:message key="label.student.degree" /></strong>
			<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.name" />
			<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome" />
		</td>						
	</tr>	
	<tr>
		<td>
			<strong><bean:message key="label.student.branch" /></strong>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.branch" property="branch">
				<html:options collection="<%= PresentationConstants.BRANCH %>" property="externalId" labelProperty="name"/>
		    </html:select>
			<!--<logic:empty name="studentCurricularPlan" property="infoBranch">
				<bean:message key="label.masterDegree.administrativeOffice.noBranch"/>
			</logic:empty>
			<logic:notEmpty name="studentCurricularPlan" property="infoBranch">
				<logic:equal name="studentCurricularPlan" property="infoBranch.name" value="<%= new String("") %>">
					<bean:message key="label.masterDegree.administrativeOffice.noBranch"/>
				</logic:equal>
				<logic:notEqual name="studentCurricularPlan" property="infoBranch.name" value="<%= new String("") %>">
						<bean:write name="studentCurricularPlan" property="infoBranch.name"/>
				</logic:notEqual>
			</logic:notEmpty>-->
		</td>						
	</tr>		
	<tr>
		<td>
			<strong><bean:message key="label.student.specialization" /></strong>
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="STUDENT_CURRICULAR_PLAN_INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.specialization" property="specialization">
				<html:options collection="values" property="value" labelProperty="label"/>
		    </html:select>
		</td>
	</tr>		
<!--	<tr>
		<td>
			<strong><bean:message key="label.student.state" /></strong>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState" bundle="ENUMERATION_RESOURCES"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.currentState" property="currentState" >
	          	 	<html:options collection="values" property="value" labelProperty="label"/>
	    	 	</html:select>   
		</td>						
	</tr>	-->
	<tr>
		<td>
			<strong><bean:message key="label.student.startDate" /></strong>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.startDate" property="startDate" /> 
			<span class="error"><!-- Error messages go here --><bean:message key="message.dateFormat"/></span>
		</td>								
	</tr>	
	<tr>
		<td>
			<strong><bean:message key="label.student.credits" /></strong>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.credits" name="studentCurricularPlanForm" property="credits" size="4"/>
		</td>
	</tr>	
	<tr>
		<td>
			<strong><bean:message key="label.student.completedCourses" /></strong>	
			<bean:write name="studentCurricularPlan" property="completedCourses" />
		</td>
	</tr>
	<tr>
		<td>
			<strong><bean:message key="label.student.enrolledCourses" /></strong>
			<bean:write name="studentCurricularPlan" property="enrolledCourses" />
		</td>
	</tr>	
	<tr>
		<td>
			<strong><bean:message key="label.student.classification" /></strong>
			<bean:write name="studentCurricularPlan" property="classification" />
		</td>
	</tr>	
	<tr>
		<td>
		<strong><bean:message key="label.student.observations"/></strong>
		
		</td>
	</tr>
	<tr>
		<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.observations" name="studentCurricularPlan" property="observations" cols="40" rows="4"/>
		</td>
	</tr>
	<tr>
		<td>
			<logic:notPresent name="studentCurricularPlan" property="infoEnrolments">
				<p><bean:message key="message.no.enrolments" /></p>
			</logic:notPresent>
			
			<logic:present name="studentCurricularPlan" property="infoEnrolments">
				<bean:size id="sizeEnrolments" name="studentCurricularPlan" property="infoEnrolments" />
	
				<logic:lessEqual name="sizeEnrolments" value="0">
					<p><h2><bean:message key="message.no.enrolments" /></h2></p>
				</logic:lessEqual>
				
				<logic:greaterThan name="sizeEnrolments" value="0">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.size" property="size" value='<%= pageContext.findAttribute("sizeEnrolments").toString() %>'/>
					<table>
						<tr>
							<td colspan="3" align="center"><h3><bean:message key="title.enrolments"/></h3></td>
						</tr>	
						<tr>
							<th align="left"><bean:message key="label.enrolment.curricularCourse"/></th>
							<th align="left"><bean:message key="label.curricularPlan"/></th>
							<th align="left"><bean:message key="label.enrolment.type"/></th>
							<th align="left"><bean:message key="label.enrolment.state"/></th>
							<th align="left"><bean:message key="label.enrolment.year"/></th>
							<th align="left"><bean:message key="label.enrolment.extraCurricular"/></th>							
						</tr>
						<logic:iterate id="infoEnrolment" name="studentCurricularPlan" property="infoEnrolments">
							<tr>	
								<td>
									<bean:write name="infoEnrolment" property="infoCurricularCourse.code" />&nbsp;
									<bean:write name="infoEnrolment" property="infoCurricularCourse.name" />
								</td>
								<td>
									<bean:write name="infoEnrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.name" />
								</td>									
								<td>
									<bean:message name="infoEnrolment" property="enrollmentTypeResourceKey" bundle="ENUMERATION_RESOURCES"/>
								</td>
								<td>
									<bean:message name="infoEnrolment" property="enrollmentState.name"/>
								</td>
								<td>
									<bean:write name="infoEnrolment" property="infoExecutionPeriod.infoExecutionYear.year" />&nbsp;
									<bean:write name="infoEnrolment" property="infoExecutionPeriod.name" />
								</td>
								<td>
									<bean:define  id="idEnrolment" name="infoEnrolment" property="externalId"/>
									<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.extraCurricularCourses" property="extraCurricularCourses"><bean:write name="infoEnrolment" property="externalId"/> </html:multibox >&nbsp;
								</td>
								<td>
									<logic:equal name="infoEnrolment" property="enrollmentState.name" value="ANNULED">
										<bean:define  id="idEnrolment" name="infoEnrolment" property="externalId" />
										<html:link page="<%= "/editStudentCurricularPlan.do?method=enrol&amp;enrolmentID=" + idEnrolment.toString() %>">
											<bean:message key="label.enrol" bundle="APPLICATION_RESOURCES"/>
										</html:link>										
									</logic:equal>
								</td>								
							</tr>		
						</logic:iterate>
					</table>
				</logic:greaterThan>
			</logic:present>				
		</td>
	</tr>	
</table>	
 <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
</html:submit>
</html:form>	






