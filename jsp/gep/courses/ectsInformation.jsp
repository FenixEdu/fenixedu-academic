<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<logic:present name="infoSiteCoursesInformation">
	<logic:iterate id="infoSiteCourseInformation" name="infoSiteCoursesInformation">
		<logic:present name="infoExecutionDegree">
			<h2><bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/></h2>
		</logic:present>
		<h3><bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.nome"/></h3>
		<table class="ects_headertable" width="90%" cellspacing="0">
			<tr>
				<td><strong><bean:message key="label.ects.yearOrSemester" bundle="GEP_RESOURCES"/></strong></td>
				<td colspan="3"><bean:message key="label.ects.semestral" bundle="GEP_RESOURCES"/></td>
			</tr>

			<logic:iterate id="curricularCourse" name="infoSiteCourseInformation" property="infoCurricularCourses">
				<logic:present name="infoExecutionDegree">
					<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
			  		<logic:equal name="curricularCourse" 
			  						 property="infoDegreeCurricularPlan.idInternal" 
			  						 value="<%= degreeCurricularPlanId.toString() %>">
			  			<tr>
							<td>
								<strong>
									<bean:message key="label.ects.curricularYear" bundle="GEP_RESOURCES"/>/
									<bean:message key="label.ects.semester" bundle="GEP_RESOURCES"/>/
									<bean:message key="label.ects.branch" bundle="GEP_RESOURCES"/>
								</strong>
							</td>
				  			<td colspan="3">
				  				<logic:iterate id="curricularCourseScope" name="curricularCourse" property="infoScopes">
									<bean:write name="curricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" />&nbsp;
									<bean:write name="curricularCourseScope" property="infoCurricularSemester.semester" />&nbsp;
									<bean:write name="curricularCourseScope" property="infoBranch.acronym"/>
									<br/ >
								</logic:iterate>
							</td>			  				
						</tr>
					 	<tr>
							<td><strong><bean:message key="label.ects.mandatoryOrOptional"
													  bundle="GEP_RESOURCES"/></strong></td>
							<td colspan="3">
							<logic:equal name="curricularCourse" property="mandatory" value="true">
					  			<bean:message key="message.courseInformation.mandatory" />
					  		</logic:equal>
					  		<logic:equal name="curricularCourse" property="mandatory" value="false">
					  			<bean:message key="message.courseInformation.optional" />
					  		</logic:equal>
					  		</td>
						</tr>
							<tr>
								<td><strong><bean:message key="label.ects.credits"
														  bundle="GEP_RESOURCES"/></strong></td>
								<td><bean:message key="label.ects.creditsEcts"
												  bundle="GEP_RESOURCES"/></td>
								<td colspan="2"><bean:message key="label.ects.creditsNational"
															  bundle="GEP_RESOURCES"/></td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td><bean:write name="curricularCourseScope" property="ectsCredits"/></td>
								<td colspan="2"><bean:write name="curricularCourseScope" property="credits"/></td>
							</tr>
					</logic:equal>
				</logic:present>
			</logic:iterate>
			<tr>
				<td><strong><bean:message key="label.ects.webPage"
										  bundle="GEP_RESOURCES"/></strong>
				<td colspan="3">
					<bean:define id="objectCode" name="infoSiteCourseInformation" property="infoExecutionCourse.idInternal"/>
					https://fenix.ist.utl.pt/publico/viewSiteExecutionCourse.do?method=firstPage&objectCode=<%= objectCode %>
				</td>
			</tr>
			<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoResponsibleTeachers">
				<tr>
					<td><strong><bean:message key="label.ects.responsibleTeacher"
											  bundle="GEP_RESOURCES"/></strong></td>
					<td colspan="3"><bean:write name="infoTeacher" property="infoPerson.nome"/></td>
				</tr>
			</logic:iterate>
		</table>
		<br />
		<table cellspacing="0">
			<tr>
				<td colspan="7"><strong>1.&nbsp;<bean:message key="label.ects.semanalHours"
												      bundle="GEP_RESOURCES"/></strong></td>
			</tr>
			<tr>
				<td>
					<bean:write name="infoSiteCourseInformation" 
								property="infoExecutionCourse.theoreticalHours"/>
				</td>
				<td><strong><bean:message key="label.ects.teo"
										  bundle="GEP_RESOURCES"/></td>
				<td>
					<bean:write name="infoSiteCourseInformation" 
								property="infoExecutionCourse.praticalHours"/>
				</td>
				<td><strong><bean:message key="label.ects.prat"
										  bundle="GEP_RESOURCES"/></td>
				<td><bean:write name="infoSiteCourseInformation" 
								property="infoExecutionCourse.theoPratHours"/>
				<td><strong><bean:message key="label.ects.teoPrat"
											bundle="GEP_RESOURCES"/></td>
				<td><bean:write name="infoSiteCourseInformation" 
								property="infoExecutionCourse.labHours"/></td>
				<td><strong><bean:message key="label.ects.lab"
										  bundle="GEP_RESOURCES"/></td>
			</tr>
			</table>
			<table id="ects" width="90%" cellspacing="0">
			<tr>
				<td colspan="7"><strong><br />
			    2.&nbsp;<bean:message key="label.ects.objectives"
							  bundle="GEP_RESOURCES"/></strong></td>
			</tr>
			<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
				<logic:present name="infoExecutionDegree">
					<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
					<logic:equal name="infoCurriculum" 
		  						 property="infoCurricularCourse.infoDegreeCurricularPlan.idInternal" 
		  						 value="<%= degreeCurricularPlanId.toString() %>">
						<tr>
							<td colspan="7">
								 <p><bean:write name="infoCurriculum" property="generalObjectives" filter="false"/></p>
							</td>
						</tr>
					</logic:equal>
				</logic:present>
			</logic:iterate>
			<tr>
				<td colspan="7"><strong><br />
			    3.&nbsp;<bean:message key="label.ects.program"
							  bundle="GEP_RESOURCES"/></strong></td>
			</tr>
			<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
				<logic:present name="infoExecutionDegree">
					<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
					<logic:equal name="infoCurriculum" 
		  						 property="infoCurricularCourse.infoDegreeCurricularPlan.idInternal" 
		  						 value="<%= degreeCurricularPlanId.toString() %>">
						<tr>
							<td colspan="7">
								 <p><bean:write name="infoCurriculum" property="program" filter="false"/></p>
							</td>
						</tr>
					</logic:equal>
				</logic:present>
			</logic:iterate>
			<tr>
				<td colspan="7"><strong><br />
			    4.&nbsp;<bean:message key="label.ects.bibliography"
							  bundle="GEP_RESOURCES"/></strong></td>
			</tr>
			<tr>
				<td colspan="7"><strong><br />
			    <bean:message key="label.ects.principalBiblio"
							  bundle="GEP_RESOURCES"/></strong></td>
			</tr>
			<logic:iterate id="infoBibliographicReference" name="infoSiteCourseInformation" property="infoBibliographicReferences">
				<logic:equal name="infoBibliographicReference" property="optional" value="false">
					<tr>
						<td colspan="7">
							<p>
								<bean:write name="infoBibliographicReference" property="authors"/>;
								<em><bean:write name="infoBibliographicReference" property="title"/></em>,
								<bean:write name="infoBibliographicReference" property="reference"/>,
								<bean:write name="infoBibliographicReference" property="year"/>.
							</p>
						</td>
					</tr>
				</logic:equal>
			</logic:iterate>
			<tr>
				<td colspan="7"><strong><br />
			    <bean:message key="label.ects.secondaryBiblio"
							  bundle="GEP_RESOURCES"/></strong></td>
			</tr>
			<logic:iterate id="infoBibliographicReference" name="infoSiteCourseInformation" property="infoBibliographicReferences">
				<logic:equal name="infoBibliographicReference" property="optional" value="true">
					<tr>
						<td colspan="7">
							<p>
								<bean:write name="infoBibliographicReference" property="authors"/>;
								<em><bean:write name="infoBibliographicReference" property="title"/></em>,
								<bean:write name="infoBibliographicReference" property="reference"/>,
								<bean:write name="infoBibliographicReference" property="year"/>.
							</p>
						</td>
					</tr>
				</logic:equal>
			</logic:iterate>
			<tr>
				<td colspan="7"><strong><br />
			    5.&nbsp;<bean:message key="label.ects.avaliation"
							  bundle="GEP_RESOURCES"/></strong></td>
			</tr>
			<tr>
				<td colspan="7">
					<p>
						<bean:write name="infoSiteCourseInformation" 
									property="infoEvaluationMethod.evaluationElements" 
									filter="false"/>
					</p>
				</td>
			</tr>
			<tr>
				<td colspan="7"><strong><br />
			    6.&nbsp;<bean:message key="label.ects.weeklyHours"
							  bundle="GEP_RESOURCES"/></strong>
			  	</td>
			</tr>
			<tr>
				<td colspan="7"><strong><br />
			    	<bean:message key="label.ects.lastModificationDate"
							  bundle="GEP_RESOURCES"/></strong>
			  	</td>
			</tr>
		</table>
	</logic:iterate>
</logic:present>