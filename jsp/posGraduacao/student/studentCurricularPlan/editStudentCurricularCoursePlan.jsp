<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentInExtraCurricularCourse" %>



<h2 align="left"><bean:message key="title.studentCurricularPlan"/></h2>

<span class="error"><html:errors/></span>
<html:form action="/alterStudentCurricularPlan">
<html:hidden property="method" value="edit"/>
<bean:define id="idInternal" name="studentCurricularPlan" property="idInternal"/>
<html:hidden property="studentCurricularPlanId" value="<%= idInternal.toString() %>"/>
<html:hidden property="page" value="1"/>
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
			<html:select property="branch">
				<html:options collection="<%= SessionConstants.BRANCH %>" property="idInternal" labelProperty="name"/>
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
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization" excludedFields="INTEGRATED_MASTER_DEGREE" bundle="ENUMERATION_RESOURCES"/>
			<html:select property="specialization">
				<html:options collection="values" property="value" labelProperty="label"/>
		    </html:select>
		</td>
	</tr>		
	<tr>
		<td>
			<strong><bean:message key="label.student.state" /></strong>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState" bundle="ENUMERATION_RESOURCES"/>
				<html:select property="currentState" >
	          	 	<html:options collection="values" property="value" labelProperty="label"/>
	    	 	</html:select>   
			</logic:present>
		</td>						
	</tr>	
	<tr>
		<td>
			<strong><bean:message key="label.student.startDate" /></strong>
			<html:text property="startDate" /> 
			<span class="error"><bean:message key="message.dateFormat"/></span>
		</td>								
	</tr>	
	<tr>
		<td>
			<strong><bean:message key="label.student.credits" /></strong>
			<html:text name="studentCurricularPlanForm" property="credits" size="4"/>
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
		<td><html:textarea name="studentCurricularPlan" property="observations" cols="40" rows="4"/>
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
				<html:hidden property="size" value='<%= pageContext.findAttribute("sizeEnrolments").toString() %>'/>
					<table>
						<tr>
							<td colspan="3" align="center"><h3><bean:message key="title.enrolments"/></h3></td>
						</tr>	
						<tr>
							<th align="left"><bean:message key="label.enrolment.curricularCourse"/></th>
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
									<bean:message name="infoEnrolment" property="enrollmentTypeResourceKey" bundle="DEFAULT"/>
								</td>
								<td>
									<bean:define id="state" name="infoEnrolment" property="enrollmentState" />
									<bean:message key="<%= pageContext.findAttribute("state").toString() %>"/>
								</td>
								<td>
									<bean:write name="infoEnrolment" property="infoExecutionPeriod.infoExecutionYear.year" />&nbsp;
									<bean:write name="infoEnrolment" property="infoExecutionPeriod.name" />
								</td>
								<td>
									<bean:define  id="idEnrolment" name="infoEnrolment" property="idInternal"/>
									<html:multibox property="extraCurricularCourses"><bean:write name="infoEnrolment" property="idInternal"/> </html:multibox >&nbsp;
								</td>
							</tr>		
						</logic:iterate>
					</table>
				</logic:greaterThan>
			</logic:present>				
		</td>
	</tr>	
</table>	
 <html:submit value="Submeter" styleClass="inputbutton">
</html:submit>
</html:form>	






