<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<logic:present name="infoSiteCoursesInformation">
	<logic:iterate id="infoSiteCourseInformation" name="infoSiteCoursesInformation">
		<logic:iterate id="curricularCourse" name="infoSiteCourseInformation" property="infoCurricularCourses">
			<h2><bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/></h2>
			<h3><bean:write name="curricularCourse" property="name"/></h3>
			<table class="ects_headertable" width="90%" cellspacing="0">
				<tr>
					<td><strong><bean:message key="label.ects.yearOrSemester" bundle="GEP_RESOURCES" locale="en"/></strong></td>
					<td colspan="3"><bean:message key="label.ects.semestral" bundle="GEP_RESOURCES" locale="en"/></td>
				</tr>
	  			<tr>
					<td>
						<strong>
							<bean:message key="label.ects.curricularYear" bundle="GEP_RESOURCES" locale="en"/>/
							<bean:message key="label.ects.semester" bundle="GEP_RESOURCES" locale="en"/>/
							<bean:message key="label.ects.branch" bundle="GEP_RESOURCES" locale="en"/>
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
					<td>
						<strong>
							<bean:message key="label.gep.code" bundle="GEP_RESOURCES" locale="en"/>:
						</strong>
					</td>
					<td colspan="3">
						<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.sigla"/>
					</td>
				</tr>
			 	<tr>
					<td><strong><bean:message key="label.ects.mandatoryOrOptional"
											  bundle="GEP_RESOURCES" locale="en"/></strong></td>
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
											  bundle="GEP_RESOURCES" locale="en"/></strong></td>
					<td><bean:message key="label.ects.creditsEcts"
									  bundle="GEP_RESOURCES" locale="en"/></td>
					<td colspan="2"><bean:message key="label.ects.creditsNational"
												  bundle="GEP_RESOURCES" locale="en"/></td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td><bean:write name="curricularCourse" property="ectsCredits"/></td>
					<td colspan="2"><bean:write name="curricularCourse" property="credits"/></td>
				</tr>
				<tr>
					<td><strong><bean:message key="label.ects.webPage"
											  bundle="GEP_RESOURCES" locale="en"/></strong>
					<td colspan="3">
						<bean:define id="objectCode" name="infoSiteCourseInformation" property="infoExecutionCourse.externalId"/>
						<bean:define id="courseURL" type="java.lang.String">
							<bean:message key="fenix.url" bundle="GLOBAL_RESOURCES"/><bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.sigla"/>/<bean:message key="courseSite.url.subpattern" bundle="GLOBAL_RESOURCES"/>/<bean:write name="curricularCourse" property="acronym"/>
						</bean:define>
						<html:link href="<%= courseURL %>"><bean:write name="courseURL"/></html:link>
					</td>
				</tr>
				<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoResponsibleTeachers">
					<tr>
						<td><strong><bean:message key="label.ects.responsibleTeacher"
												  bundle="GEP_RESOURCES" locale="en"/></strong></td>
						<td colspan="3"><bean:write name="infoTeacher" property="infoPerson.nome"/></td>
					</tr>
				</logic:iterate>
			</table>
			<br />
			<table cellspacing="0">
				<tr>
					<td colspan="7"><strong>1.&nbsp;<bean:message key="label.ects.semanalHours"
													      bundle="GEP_RESOURCES" locale="en"/></strong></td>
				</tr>
				<tr>
					<td>
						<bean:write name="curricularCourse" 
									property="theoreticalHours"/>
					</td>
					<td><strong><bean:message key="label.ects.teo"
											  bundle="GEP_RESOURCES" locale="en"/></td>
					<td>
						<bean:write name="curricularCourse" 
									property="praticalHours"/>
					</td>
					<td><strong><bean:message key="label.ects.prat"
											  bundle="GEP_RESOURCES" locale="en"/></td>
					<td><bean:write name="curricularCourse" 
									property="theoPratHours"/>
					<td><strong><bean:message key="label.ects.teoPrat"
												bundle="GEP_RESOURCES" locale="en"/></td>
					<td><bean:write name="curricularCourse" 
									property="labHours"/></td>
					<td><strong><bean:message key="label.ects.lab"
											  bundle="GEP_RESOURCES" locale="en"/></td>
				</tr>
			</table>
			<table id="ects" width="90%" cellspacing="0">
				<tr>
					<td colspan="7"><strong><br />
				    2.&nbsp;<bean:message key="label.ects.objectivesGen"
								  bundle="GEP_RESOURCES" locale="en"/></strong></td>
				</tr>
				<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
					<tr>
						<td colspan="7">
							 <p><bean:write name="infoCurriculum" property="generalObjectivesEn" filter="false"/></p>
						</td>
					</tr>
				</logic:iterate>
				<tr>
					<td colspan="7"><strong><br />
				    3.&nbsp;<bean:message key="label.ects.objectivesOper"
								  bundle="GEP_RESOURCES" locale="en"/></strong></td>
				</tr>
				<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
					<tr>
						<td colspan="7">
							 <p><bean:write name="infoCurriculum" property="operacionalObjectivesEn" filter="false"/></p>
						</td>
					</tr>
				</logic:iterate>
				<tr>
					<td colspan="7">
						<strong>
							<br />
							4.&nbsp;<bean:message key="label.ects.program" bundle="GEP_RESOURCES" locale="en"/>
						</strong>
				  	</td>
				</tr>
				<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
					<tr>
						<td colspan="7">
							 <p><bean:write name="infoCurriculum" property="programEn" filter="false"/></p>
						</td>
					</tr>
				</logic:iterate>
				<tr>
					<td colspan="7">
						<strong>
							<br />
				    		5.&nbsp;
				    		<bean:message key="label.ects.bibliography" bundle="GEP_RESOURCES" locale="en"/>
			    		</strong>
		    		</td>
				</tr>
				<tr>
					<td colspan="7">
						<strong>
							<br />
				    		<bean:message key="label.ects.principalBiblio" bundle="GEP_RESOURCES" locale="en"/>
			    		</strong>
		    		</td>
				</tr>
				<logic:iterate id="bibliographicReference" name="infoSiteCourseInformation" property="executionCourse.associatedBibliographicReferences">
					<logic:equal name="bibliographicReference" property="optional" value="false">
						<tr>
							<td colspan="7">
								<p>
									<bean:write name="bibliographicReference" property="authors"/>;
									<em><bean:write name="bibliographicReference" property="title"/></em>,
									<bean:write name="bibliographicReference" property="reference"/>,
									<bean:write name="bibliographicReference" property="year"/>.
								</p>
							</td>
						</tr>
					</logic:equal>
				</logic:iterate>
				<tr>
					<td colspan="7">
						<strong>
							<br />
					    	<bean:message key="label.ects.secondaryBiblio" bundle="GEP_RESOURCES" locale="en"/>
				    	</strong>
			    	</td>
				</tr>
				<logic:iterate id="bibliographicReference" name="infoSiteCourseInformation" property="executionCourse.associatedBibliographicReferences">
					<logic:equal name="bibliographicReference" property="optional" value="true">
						<tr>
							<td colspan="7">
								<p>
									<bean:write name="bibliographicReference" property="authors"/>;
									<em><bean:write name="bibliographicReference" property="title"/></em>,
									<bean:write name="bibliographicReference" property="reference"/>,
									<bean:write name="bibliographicReference" property="year"/>.
								</p>
							</td>
						</tr>
					</logic:equal>
				</logic:iterate>
				<tr>
					<td colspan="7">
						<strong>
							<br />
				    		6.&nbsp;<bean:message key="label.ects.avaliation" bundle="GEP_RESOURCES" locale="en"/>
			    		</strong>
	    			</td>
				</tr>
				<tr>
					<td colspan="7">
						<p>
								<fr:view name="infoSiteCourseInformation" 
										property="executionCourse.evaluationMethod.evaluationElements" 
										layout="html"/>
						</p>
					</td>
				</tr>
				<tr>
					<td colspan="7">
						<strong>
							<br />
				    		<bean:message key="label.ects.lastModificationDate" bundle="GEP_RESOURCES" locale="en"/>
			    		</strong>
				  	</td>
				</tr>
			</table>
		</logic:iterate>
	</logic:iterate>
</logic:present>