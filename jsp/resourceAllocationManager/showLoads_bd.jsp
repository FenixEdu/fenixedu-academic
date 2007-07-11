<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:present name="infoExecutionCourse" scope="request">
	<bean:define id="labHours" name="infoExecutionCourse" property="labHours"/>
	<bean:define id="theoreticalHours" name="infoExecutionCourse" property="theoreticalHours"/>
	<bean:define id="theoPratHours" name="infoExecutionCourse" property="theoPratHours" type="java.lang.Double" />
	<bean:define id="praticalHours" name="infoExecutionCourse" property="praticalHours" type="java.lang.Double" />
	<bean:define id="fieldWork" name="infoExecutionCourse" property="fieldWorkHours" type="java.lang.Double" />
	<bean:define id="problems" name="infoExecutionCourse" property="problemsHours" type="java.lang.Double" />
	<bean:define id="seminary" name="infoExecutionCourse" property="seminaryHours" type="java.lang.Double" />
	<bean:define id="trainingPeriod" name="infoExecutionCourse" property="trainingPeriodHours" type="java.lang.Double" />
	<bean:define id="tutorialOrientation" name="infoExecutionCourse" property="tutorialOrientationHours" type="java.lang.Double" />

	<em><bean:message key="link.writtenEvaluationManagement" bundle="SOP_RESOURCES"/></em>
	<h2><bean:message key="label.hours.load.total" bundle="SOP_RESOURCES"/></h2>

	<h3 class="mtop15 mbottom05"><bean:message key="label.executionCourse" /></h3>

	<table class="tstyle4 thlight tdcenter mtop05">
		<tr>
			<th width="30%" rowspan="2">
				<bean:message key="label.name"/>
			</th>
			<th width="10%" rowspan="2"> 
				<bean:message key="label.code"/>
			</th>
			<th colspan="9" width="60%">
				Carga
			</th>
		</tr>
		<tr>
			<th>
				<bean:message key="label.hours.load.theoretical"/>
			</th>
			<th>
				<bean:message key="label.hours.load.theoretical_practical"/>
			</th>
			<th>
				<bean:message key="label.hours.load.practical"/>
			</th>
			<th>
				<bean:message key="label.hours.load.laboratorial"/>
			</th>			
			<th>
				<bean:message key="label.hours.load.fieldWork"/>
			</th>			
			<th>
				<bean:message key="label.hours.load.problems"/>
			</th>			
			<th>
				<bean:message key="label.hours.load.seminary"/>
			</th>			
			<th>
				<bean:message key="label.hours.load.trainingPeriod"/>
			</th>			
			<th>
				<bean:message key="label.hours.load.tutorialOrientation"/>
			</th>			
		</tr>	
		<tr>
			<td>
				<span class="highlight1"><bean:write name="infoExecutionCourse" property="nome"/></span>
			</td>
			<td>
				<bean:write name="infoExecutionCourse" property="sigla"/>
			</td>
			<td>
				<bean:write name="infoExecutionCourse" property="theoreticalHours"/>
			</td>
			<td>
				<bean:write name="infoExecutionCourse" property="theoPratHours"/>
			</td>
			<td>
				<bean:write name="infoExecutionCourse" property="praticalHours"/>
			</td>
			<td>
				<bean:write name="infoExecutionCourse" property="labHours"/>
			</td>
			<td>
				<bean:write name="infoExecutionCourse" property="fieldWorkHours"/>
			</td>
			<td>
				<bean:write name="infoExecutionCourse" property="problemsHours"/>
			</td>
			<td>
				<bean:write name="infoExecutionCourse" property="seminaryHours"/>
			</td>
			<td>
				<bean:write name="infoExecutionCourse" property="trainingPeriodHours"/>
			</td>
			<td>
				<bean:write name="infoExecutionCourse" property="tutorialOrientationHours"/>
			</td>
		</tr>

	</table>


	<logic:present name="curricularCourses" >

		<h3 class="mtop15"><bean:message key="label.curricularCourses" /></h3>

			<logic:iterate id="curricularCourse" name="curricularCourses" >
				<p>
					<strong><bean:message key="label.curricularCourse"/>: <strong><bean:write name="curricularCourse" property="name" /></strong><br/>
					<strong>Curso :	<strong><bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome" /></strong>
				</p>
								
				<table class="tstyle4 tdcenter thlight">
					<tr>
						<th><bean:message key="message.manager.theoreticalHours" />
						</th>
						<th><bean:message key="message.manager.theoPratHours" />
						</th>
						<th><bean:message key="message.manager.praticalHours" />
						</th>
						<th><bean:message key="message.manager.labHours" />
						</th>
						<th><bean:message key="message.manager.fieldWorkHours" />
						</th>
						<th><bean:message key="message.manager.problemsHours" />
						</th>
						<th><bean:message key="message.manager.seminaryHours" />
						</th>
						<th><bean:message key="message.manager.trainingPeriodHours" />
						</th>
						<th><bean:message key="message.manager.tutorialOrientationHours" />
						</th>
					</tr>
					<tr>
						<td>
							<logic:equal  name="curricularCourse" property="theoreticalHours"  value="<%= theoreticalHours.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="theoreticalHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="theoreticalHours" value="<%= theoreticalHours.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="theoreticalHours"/></font>
							</logic:notEqual>
						</td>
						<td>
							<logic:equal name="curricularCourse" property="theoPratHours" value="<%= theoPratHours.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="theoPratHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="theoPratHours" value="<%= theoPratHours.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="theoPratHours"/></font>
							</logic:notEqual>							
						</td>
						<td>
							<logic:equal name="curricularCourse" property="praticalHours" value="<%= praticalHours.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="praticalHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="praticalHours" value="<%= praticalHours.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="praticalHours"/></font>
							</logic:notEqual>								
						</td>
						<td>
							<logic:equal name="curricularCourse" property="labHours" value="<%= labHours.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="labHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="labHours" value="<%= labHours.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="labHours"/></font>
							</logic:notEqual>								
						</td>
						<td>
							<logic:equal name="curricularCourse" property="fieldWorkHours" value="<%= fieldWork.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="fieldWorkHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="fieldWorkHours" value="<%= fieldWork.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="fieldWorkHours"/></font>
							</logic:notEqual>								
						</td>

						<td>
							<logic:equal name="curricularCourse" property="problemsHours" value="<%= problems.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="problemsHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="problemsHours" value="<%= problems.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="problemsHours"/></font>
							</logic:notEqual>								
						</td>
						<td>
							<logic:equal name="curricularCourse" property="seminaryHours" value="<%= seminary.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="seminaryHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="seminaryHours" value="<%= seminary.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="seminaryHours"/></font>
							</logic:notEqual>								
						</td>
						<td>
							<logic:equal name="curricularCourse" property="trainingPeriodHours" value="<%= trainingPeriod.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="trainingPeriodHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="trainingPeriodHours" value="<%= trainingPeriod.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="trainingPeriodHours"/></font>
							</logic:notEqual>								
						</td>
						<td>
							<logic:equal name="curricularCourse" property="tutorialOrientationHours" value="<%= tutorialOrientation.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="tutorialOrientationHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="tutorialOrientationHours" value="<%= tutorialOrientation.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="tutorialOrientationHours"/></font>
							</logic:notEqual>								
						</td>

					</tr>
				</table>


				<table class="tstyle4 tdcenter thlight">
					<tr>				
						<th>
							<bean:message key="label.manager.curricularCourseScope.branch"/>
						</th>
						<th>
							<bean:message key="label.manager.curricularCourseScope.curricularYear"/>
						</th>
						<th>
							Sem.
							<%--<bean:message key="label.manager.curricularCourseScope.curricularSemester"/> --%>
						</th>	
						<td>
							&nbsp;
						</td>				
					</tr>		
					<%-- Scopes --%>
					<logic:iterate id="scope" name="curricularCourse" property="infoScopes" >
						<tr>
							<td>
								<logic:equal name="scope" property="infoBranch.name" value="" >
									<bean:message key="label.commonBranch" />
								</logic:equal>
								<logic:notEqual name="scope" property="infoBranch.name" value="" >
									<bean:write name="scope" property="infoBranch.name"/>
								</logic:notEqual>
							</td>
							<td>
								<bean:write name="scope" property="infoCurricularSemester.infoCurricularYear.year"/>
							</td>
							<td>
								<bean:write name="scope" property="infoCurricularSemester.semester"/>
							</td>								
							<td>
								&nbsp;
							</td>	
						</tr>
					</logic:iterate>
				</table>
			</logic:iterate>				
	</logic:present>
</logic:present>

