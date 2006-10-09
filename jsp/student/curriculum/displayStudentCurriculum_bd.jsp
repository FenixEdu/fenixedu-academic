<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.EnrollmentState" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment" %>

<html:xhtml/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>

<bean:define id="organizedByGroups" name="organizedByGroups" scope="request" type="java.lang.Boolean" />
<bean:define id="enrolmentStateSelectionType" name="enrolmentStateSelectionType" scope="request" type="java.lang.Integer" />	

<bean:define id="infoStudentCPs" name="studentCPs" scope="request" type="net.sourceforge.fenixedu.dataTransferObject.util.InfoStudentCurricularPlansWithSelectedEnrollments" />	
<bean:define id="selectedStudentCPs" name="infoStudentCPs" property="infoStudentCurricularPlans" />

<bean:size id="numCPs" name="selectedStudentCPs"/>

<html:form action="/viewCurriculum.do?method=getStudentCP">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
	
	<table class="tstyle5 thlight thright thmiddle mbottom0">
		<tr>
			<th><bean:message bundle="APPLICATION_RESOURCES" key="label.person.name" />:</th>
			<td>
				<bean:write name="studentPerson" property="nome"/>			
			</td>
		</tr>

		<tr>
			<th><bean:message key="label.studentCurricularPlan.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.studentCPID" property="studentCPID"
						onchange='this.form.submit();'> <html:options collection="allSCPs" property="value" labelProperty="label" />
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		
		<tr>
			<th><bean:message key="label.enrollmentsFilter.basic" bundle="STUDENT_RESOURCES" /></th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.select" property="select"
					onchange='this.form.submit();' >
					<html:options collection="enrollmentOptions" property="value" labelProperty="label"/>
				</html:select>
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
	</table>
	
	<logic:present property="studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
	</logic:present>

	<logic:notEqual name="numCPs" value="0">
		<br/>
		<bean:message bundle="BOLONHA_MANAGER_RESOURCES" key="view.structure.organized.by"/>:<br/>
		<html:radio property="organizedByGroups" value="true" onchange='this.form.submit();'/><bean:message key="groups" bundle="BOLONHA_MANAGER_RESOURCES"/>
		<html:radio property="organizedByGroups" value="false" onchange='this.form.submit();'/><bean:message key="years" bundle="BOLONHA_MANAGER_RESOURCES"/>
		<br/>
		<br/>
		<logic:iterate id="infoStudentCurricularPlan" name="selectedStudentCPs">

			<bean:define id="dateFormated">
				<dt:format pattern="dd.MM.yyyy">
					<bean:write name="infoStudentCurricularPlan" property="startDate.time"/>
				</dt:format>
			</bean:define>

			<div class="infoop2 mvert2 mtop0">
				<p>
					<strong>
						<bean:message key="label.curricularplan" bundle="STUDENT_RESOURCES" />: 
					</strong> 
					<bean:message bundle="ENUMERATION_RESOURCES" name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.tipoCurso.name"/>
					<bean:message bundle="APPLICATION_RESOURCES" key="label.in"/> 
					<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>,
					<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.name"/>
				</p>
				<p>
					<strong>
						<bean:message key="label.beginDate" bundle="STUDENT_RESOURCES" />: 
					</strong> 
					<bean:write name="dateFormated"/>
				</p>
				<p>
					<logic:present name="infoStudentCurricularPlan" property="specialization">
						<bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
					</logic:present>
					<strong>
						<bean:message key="label.number" />: 
					</strong>
					<bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/>
				</p>
			</div>
	
			<fr:view name="infoStudentCurricularPlan" property="studentCurricularPlan">
				<fr:layout>
					<fr:property name="organizedByGroups" value="<%=organizedByGroups.toString()%>"/>
					<fr:property name="initialWidth" value="70"/>
					<fr:property name="widthDecreasePerLevel" value="3"/>
					<fr:property name="tablesClasses" value="showinfo3 mvert0"/>
					<fr:property name="groupRowClasses" value="bgcolor2"/>
					<fr:property name="groupNameClasses" value="aleft"/>
					<fr:property name="enrolmentClasses" value="smalltxt acenter,smalltxt acenter,smalltxt acenter,smalltxt acenter,acenter"/>
					<fr:property name="enrolmentStateSelectionType" value="<%=enrolmentStateSelectionType.toString()%>"/>
				</fr:layout>
			</fr:view>
<%--
			<table class="tstyle3 mbottom4" style="width: 650px;">
		
				<bean:define id="id" name="infoStudentCurricularPlan" property="idInternal"/>
				<bean:define id="curriculum" name="infoStudentCPs" property='<%="infoEnrollmentsForStudentCPById("+ id +")"%>'/>

				<%
					String actualYear = "0";
					String actualSemester = "0";			
				%>

				<bean:size id="numEnrollments" name="curriculum"/>
				
				<logic:notEqual name="numEnrollments" value="0">
				  	<logic:iterate id="enrolment" name="curriculum"> 
						<%
							if(!actualYear.equals(((InfoEnrolment)enrolment).getInfoExecutionPeriod().getInfoExecutionYear().getYear()) ||
							   !actualSemester.equals(((InfoEnrolment)enrolment).getInfoExecutionPeriod().getSemester().toString()))
							{
								actualYear = ((InfoEnrolment)enrolment).getInfoExecutionPeriod().getInfoExecutionYear().getYear();
								actualSemester = ((InfoEnrolment)enrolment).getInfoExecutionPeriod().getSemester().toString();
						%>	
						<tr>
						  	<th style="width: 80px;">
						  		<bean:message key="label.executionYear" />
						  	</th >
						  	<th style="width: 40px;">
						  		<bean:message bundle="STUDENT_RESOURCES" key="label.semester.abbreviation" />
						  	</th >
						  	<th style="width: 60px;">
						  		<bean:message bundle="APPLICATION_RESOURCES" key="label.degree.name" />
						  	</th>
						  	<th>
						  		<bean:message bundle="APPLICATION_RESOURCES" key="label.curricular.course.name" />
						  	</th>
						  	<th style="width: 100px;">
						  		<bean:message bundle="APPLICATION_RESOURCES" key="label.finalEvaluation" />
						  	</th>
					  	</tr>	
						<%		
							}
						%>
				  		<tr>
							<td class="acenter">
						    	<bean:write name="enrolment" property="infoExecutionPeriod.infoExecutionYear.year"/>
						 	</td>
						  				 
							<td class="acenter">
						    	<bean:write name="enrolment" property="infoExecutionPeriod.semester"/>
						 	</td>
						 	<td class="acenter">
						    	<bean:write name="enrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
						 	</td>
						 	<td style="text-align:left">
						    	<bean:write name="enrolment" property="infoCurricularCourse.name"/>
								<% if ( !((InfoEnrolment) enrolment).getEnrollmentTypeResourceKey().equals("option.curricularCourse.normal") ) {%>
									(<bean:message name="enrolment" bundle="APPLICATION_RESOURCES" property="enrollmentTypeResourceKey" />)
								<% } %>
						 	</td>
						 	<td class="acenter">
								<logic:notEqual name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
									<bean:message name="enrolment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
								</logic:notEqual>
								<logic:equal name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
									<bean:write name="enrolment" property="grade"/>
								</logic:equal>
					 		</td>
				  		</tr>
					</logic:iterate>
				</logic:notEqual>
			    <logic:equal name="numEnrollments" value="0">
					<tr>
						<td>
							<span class="error"><!-- Error messages go here --><bean:message key="message.no.enrolments" bundle="STUDENT_RESOURCES"/></span>
						</td>
					</tr>
				</logic:equal>
			</table>
--%>

		<br/><br/><br/>
		
		</logic:iterate>
	</logic:notEqual>
	 	
	<logic:equal name="numCPs" value="0">
		<span class="error">
			<!-- Error messages go here --><bean:message key="message.no.curricularplans" bundle="STUDENT_RESOURCES"/>
		</span>
	</logic:equal>

</html:form>
