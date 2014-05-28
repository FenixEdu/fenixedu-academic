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
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="string"%>
<%@ page import="java.util.Calendar" %>
<logic:present name="infoSiteCoursesInformation">
	<logic:iterate id="infoSiteCourseInformation" name="infoSiteCoursesInformation">

		<logic:present name="infoExecutionDegree">
			<%-- page is to show information of a given execution degree --%>

			<logic:iterate id="curricularCourse" name="infoSiteCourseInformation" property="infoCurricularCourses">
				<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.externalId"/>
				<logic:equal name="curricularCourse" 
	  						 property="infoDegreeCurricularPlan.externalId" 
	  						 value="<%= degreeCurricularPlanId.toString() %>">
					<h2><bean:message key="label.acred.courseInfo" bundle="GEP_RESOURCES"/></h2>
					<h3>
						<bean:message key="label.acred.degree" bundle="GEP_RESOURCES"/>
						<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>
					</h3>
					<h3><bean:message key="label.acred.course" bundle="GEP_RESOURCES"/>
					<bean:write name="curricularCourse" property="name"/></h3>
					<table class="ects_headertable" width="90%" cellspacing="0" border="0">
						<tr>
							<td><strong><bean:message key="label.ects.curricularYear" bundle="GEP_RESOURCES"/></strong></td>
							<td><strong><bean:message key="label.ects.semester" bundle="GEP_RESOURCES"/></strong></td>
							<td colspan="2"><strong><bean:message key="label.ects.branch" bundle="GEP_RESOURCES"/></strong></td>
						</tr>											
						<logic:iterate id="curricularCourseScope" name="curricularCourse" property="infoScopes">
							<tr>
					  			<td><bean:write name="curricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/></td>
								<td><bean:write name="curricularCourseScope" property="infoCurricularSemester.semester" /></td>
								<td colspan="2">
									<logic:present name="curricularCourseScope" property="infoBranch.acronym">
										<bean:write name="curricularCourseScope" property="infoBranch.acronym"/>
									</logic:present>
									<logic:notPresent name="curricularCourseScope" property="infoBranch.acronym">-</logic:notPresent>
								</td>											
							</tr>
						</logic:iterate>
						<tr>
							<td>
								<strong>
									<bean:message key="label.gep.code" bundle="GEP_RESOURCES"/>:
								</strong>
							</td>
							<td>
								<bean:write name="curricularCourse" property="code"/>
							</td>
							<td>
								<strong><bean:message key="label.ects.mandatoryOrOptional"
													  bundle="GEP_RESOURCES"/></strong>
							</td>
							<td>
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
							<td><bean:write name="curricularCourse" property="credits"/></td>
							<td><strong><bean:message key="label.ects.yearOrSemester" bundle="GEP_RESOURCES"/></strong></td>
							<td><bean:message key="label.ects.semestral" bundle="GEP_RESOURCES"/></td>
						</tr>
						<tr>
							<td><strong><bean:message key="label.ects.webPage"
													  bundle="GEP_RESOURCES"/></strong>
							<td colspan="3">
								<% request.setAttribute("locale", net.sourceforge.fenixedu.util.LocaleFactory.pt_PT); %>
								<% session.setAttribute("locale", net.sourceforge.fenixedu.util.LocaleFactory.pt_PT); %>
								<bean:define id="objectCode" name="infoSiteCourseInformation" property="infoExecutionCourse.externalId"/>
								<bean:define id="courseURL" type="java.lang.String">
									<bean:message key="fenix.url" bundle="GLOBAL_RESOURCES"/><bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.sigla"/>/<bean:message key="courseSite.url.subpattern" bundle="GLOBAL_RESOURCES" locale="locale"/>/<bean:write name="curricularCourse" property="acronym"/>/<string:replace replace="/" with="-"><bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/></string:replace>/<string:replace replace=" " with="-"><bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.infoExecutionPeriod.name"/></string:replace>
								</bean:define>
								<html:link href="<%= courseURL %>"><bean:write name="courseURL"/></html:link>
							</td>
						</tr>
						<tr>
							<td>
								<strong><bean:message key="label.ects.executionYear" bundle="GEP_RESOURCES"/></strong>
							</td>							
							<td colspan="3">
								<bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/>
							</td>
						</tr>
						<tr>
							<td>
								<strong><bean:message key="label.ects.executionPeriod" bundle="GEP_RESOURCES"/></strong>
							</td>
							<td colspan="3">
								<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.infoExecutionPeriod.name"/>
							</td>
						</tr>
						<bean:define id="labels" value="false"/>
						<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoResponsibleTeachers">
							<tr>
								<td>
									<logic:equal name="labels" value="false">
										<strong><bean:message key="label.ects.responsibleTeacher"
															  bundle="GEP_RESOURCES"/></strong>
									</logic:equal>
							  	</td>
								<td><bean:write name="infoTeacher" property="infoPerson.nome"/></td>
								<td>
									<logic:equal name="labels" value="false">
										<strong><bean:message key="label.acred.categoryResponsibleTeacher"
															  bundle="GEP_RESOURCES"/></strong>
									</logic:equal>
								</td>
								<td><bean:write name="infoTeacher" property="infoCategory.name"/></td>
							</tr>
							<bean:define id="labels" value="true"/>
						</logic:iterate>
						<br />
						<bean:define id="labels" value="false"/>
						<logic:iterate id="infoTeacherId" name="infoSiteCourseInformation" property="infoLecturingTeachers" type="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher">
							<bean:define id="present">false</bean:define>
							<logic:iterate id="infoResponsible" name="infoSiteCourseInformation" property="infoResponsibleTeachers" type="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher">
								<logic:equal name="infoResponsible" property="externalId" value="<%=infoTeacherId.getExternalId().toString()%>">
									<bean:define id="present">true</bean:define>
								</logic:equal>
							</logic:iterate>
							<logic:equal name="present" value="false">
								<tr>
									<td>
										<logic:equal name="labels" value="false">
											<strong><bean:message key="label.acred.professorshipTeacher"
																	  bundle="GEP_RESOURCES"/></strong>
										</logic:equal>
									</td>					  
									<td colspan="3"><bean:write name="infoTeacherId" property="infoPerson.nome"/></td>
								</tr>
								<bean:define id="labels" value="true"/>
							</logic:equal>
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
								<bean:write name="curricularCourse" 
											property="theoreticalHours"/>
							</td>
							<td><strong><bean:message key="label.ects.teo"
													  bundle="GEP_RESOURCES"/></td>
							<td>
								<bean:write name="curricularCourse" 
											property="praticalHours"/>
							</td>
							<td><strong><bean:message key="label.ects.prat"
													  bundle="GEP_RESOURCES"/></td>
							<td><bean:write name="curricularCourse" 
											property="theoPratHours"/>
							<td><strong><bean:message key="label.ects.teoPrat"
														bundle="GEP_RESOURCES"/></td>
							<td><bean:write name="curricularCourse" 
											property="labHours"/></td>
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
						<bean:define id="curricularCourseId" name="curricularCourse" property="externalId" />
						<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
							<logic:equal name="infoCurriculum" property="infoCurricularCourse.externalId" value="<%= curricularCourseId.toString() %>">
								<tr>
									<td colspan="7">
										 <p><bean:write name="infoCurriculum" property="generalObjectives" filter="false"/></p>
									</td>
								</tr>
							</logic:equal>
						</logic:iterate>
						<tr>
							<td colspan="7">
								<strong>
									<br />
									3.&nbsp;<bean:message key="label.ects.program" bundle="GEP_RESOURCES"/>
								</strong>
						  </td>
						</tr>
						<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
							<logic:equal name="infoCurriculum" property="infoCurricularCourse.externalId" value="<%= curricularCourseId.toString() %>">
								<tr>
									<td colspan="7">
										 <p><bean:write name="infoCurriculum" property="program" filter="false"/></p>
									</td>
								</tr>
							</logic:equal>
						</logic:iterate>
						<tr>
							<td colspan="7">
								<strong>
									<br />
						    		4.&nbsp;
						    		<bean:message key="label.ects.bibliography" bundle="GEP_RESOURCES"/>
					    		</strong>
				    		</td>
						</tr>
						<tr>
							<td colspan="7">
								<strong>
									<br />
						    		<bean:message key="label.ects.principalBiblio" bundle="GEP_RESOURCES"/>
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
							    	<bean:message key="label.ects.secondaryBiblio" bundle="GEP_RESOURCES"/>
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
						    		5.&nbsp;<bean:message key="label.ects.avaliation" bundle="GEP_RESOURCES"/>
					    		</strong>
						  	</td>
						</tr>
						<tr>
							<td colspan="7">
								<p>
									<logic:present name="infoSiteCourseInformation" property="executionCourse.evaluationMethod">
										<bean:write name="infoSiteCourseInformation" 
													property="executionCourse.evaluationMethod.evaluationElements" 
													filter="false"/>
									</logic:present>
									<logic:notPresent name="infoSiteCourseInformation" property="executionCourse.evaluationMethod">
										<bean:write name="infoSiteCourseInformation" property="evaluationMethod" 
													filter="false"/>
									</logic:notPresent>									
								</p>
							</td>
						</tr>
					</table>
					<br/><hr/><br/><br/>
				</logic:equal>
			</logic:iterate>
		</logic:present>
		
		<logic:notPresent name="infoExecutionDegree">
			<%-- page is to show information of ALL execution degrees --%>

			<logic:iterate id="curricularCourse" name="infoSiteCourseInformation" property="infoCurricularCourses">
				<h2><bean:message key="label.acred.courseInfo" bundle="GEP_RESOURCES"/></h2>
				<h3>
					<bean:message key="label.acred.degree" bundle="GEP_RESOURCES"/>
					<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/>
				</h3>
				<br/>
				<h3><bean:message key="label.acred.course" bundle="GEP_RESOURCES"/>
				<bean:write name="curricularCourse" property="name"/></h3>
				<table class="ects_headertable" width="90%" cellspacing="0">
					<tr>
						<td><strong><bean:message key="label.ects.curricularYear" bundle="GEP_RESOURCES"/></strong></td>
						<td><strong><bean:message key="label.ects.semester" bundle="GEP_RESOURCES"/></strong></td>
						<td colspan="2"><strong><bean:message key="label.ects.branch" bundle="GEP_RESOURCES"/></strong></td>
					</tr>											
					<logic:iterate id="curricularCourseScope" name="curricularCourse" property="infoScopes">
						<tr>
				  			<td><bean:write name="curricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/></td>
							<td><bean:write name="curricularCourseScope" property="infoCurricularSemester.semester" /></td>
							<td colspan="2">
								<logic:present name="curricularCourseScope" property="infoBranch.acronym">
									<bean:write name="curricularCourseScope" property="infoBranch.acronym"/>
								</logic:present>
								<logic:notPresent name="curricularCourseScope" property="infoBranch.acronym">-</logic:notPresent>
							</td>											
						</tr>
					</logic:iterate>
					<tr>
						<td>
							<strong>
								<bean:message key="label.gep.code" bundle="GEP_RESOURCES"/>:
							</strong>
						</td>
						<td>
							<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.sigla"/>
						</td>
						<td>
							<strong><bean:message key="label.ects.mandatoryOrOptional"
												  bundle="GEP_RESOURCES"/></strong>
						</td>
						<td>
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
						<td><bean:write name="curricularCourse" property="credits"/></td>
						<td><strong><bean:message key="label.ects.yearOrSemester" bundle="GEP_RESOURCES"/></strong></td>
						<td><bean:message key="label.ects.semestral" bundle="GEP_RESOURCES"/></td>
					</tr>
					<tr>
						<td><strong><bean:message key="label.ects.webPage"
												  bundle="GEP_RESOURCES"/></strong></td>
						<td colspan="3">
							<% request.setAttribute("locale", net.sourceforge.fenixedu.util.LocaleFactory.pt_PT); %>
							<% session.setAttribute("locale", net.sourceforge.fenixedu.util.LocaleFactory.pt_PT); %>
							<bean:define id="objectCode" name="infoSiteCourseInformation" property="infoExecutionCourse.externalId"/>
							<bean:define id="courseURL" type="java.lang.String">
								<bean:message key="fenix.url" bundle="GLOBAL_RESOURCES"/><bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>/<bean:message key="courseSite.url.subpattern" bundle="GLOBAL_RESOURCES" locale="locale"/>/<bean:write name="curricularCourse" property="acronym"/>/<string:replace replace="/" with="-"><bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.infoExecutionPeriod.infoExecutionYear.year"/></string:replace>/<string:replace replace=" " with="-"><bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.infoExecutionPeriod.name"/></string:replace>
							</bean:define>
							<html:link href="<%= courseURL %>"><bean:write name="courseURL"/></html:link>
						</td>
					</tr>
					<tr>
						<td>
							<strong><bean:message key="label.ects.executionYear" bundle="GEP_RESOURCES"/></strong>
						</td>							
						<td colspan="3">
							<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.infoExecutionPeriod.infoExecutionYear.year"/>
						</td>
					</tr>
					<tr>
						<td>
							<strong><bean:message key="label.ects.executionPeriod" bundle="GEP_RESOURCES"/></strong>
						</td>
						<td colspan="3">
							<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.infoExecutionPeriod.name"/>
						</td>
					</tr>
					<bean:define id="labels" value="false"/>
					<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoResponsibleTeachers">
						<tr>
							<td>
								<logic:equal name="labels" value="false">
									<strong><bean:message key="label.ects.responsibleTeacher"
														  bundle="GEP_RESOURCES"/></strong>
								</logic:equal>
						  	</td>
							<td><bean:write name="infoTeacher" property="infoPerson.nome"/></td>
							<td>
								<logic:equal name="labels" value="false">
									<strong><bean:message key="label.acred.categoryResponsibleTeacher"
														  bundle="GEP_RESOURCES"/></strong>
								</logic:equal>
							</td>
							<td><bean:write name="infoTeacher" property="infoCategory.name"/></td>
						</tr>
						<bean:define id="labels" value="true"/>
					</logic:iterate>
					<br />
					<bean:define id="labels" value="false"/>
					<logic:iterate id="infoTeacherId" name="infoSiteCourseInformation" property="infoLecturingTeachers" type="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher">
						<bean:define id="present">false</bean:define>
						<logic:iterate id="infoResponsible" name="infoSiteCourseInformation" property="infoResponsibleTeachers" type="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher">
							<logic:equal name="infoResponsible" property="externalId" value="<%=infoTeacherId.getExternalId().toString()%>">
								<bean:define id="present">true</bean:define>
							</logic:equal>
						</logic:iterate>
						<logic:equal name="present" value="false">
							<tr>
								<td>
									<logic:equal name="labels" value="false">
										<strong><bean:message key="label.acred.professorshipTeacher"
																  bundle="GEP_RESOURCES"/></strong>
									</logic:equal>
								</td>					  
								<td colspan="3"><bean:write name="infoTeacherId" property="infoPerson.nome"/></td>
							</tr>
							<bean:define id="labels" value="true"/>
						</logic:equal>
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
							<bean:write name="curricularCourse" 
										property="theoreticalHours"/>
						</td>
						<td><strong><bean:message key="label.ects.teo"
												  bundle="GEP_RESOURCES"/></td>
						<td>
							<bean:write name="curricularCourse" 
										property="praticalHours"/>
						</td>
						<td><strong><bean:message key="label.ects.prat"
												  bundle="GEP_RESOURCES"/></td>
						<td><bean:write name="curricularCourse" 
										property="theoPratHours"/>
						<td><strong><bean:message key="label.ects.teoPrat"
													bundle="GEP_RESOURCES"/></td>
						<td><bean:write name="curricularCourse" 
										property="labHours"/></td>
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
					<bean:define id="curricularCourseId" name="curricularCourse" property="externalId" />
					<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
						<logic:equal name="infoCurriculum" property="infoCurricularCourse.externalId" value="<%= curricularCourseId.toString() %>">
							<tr>
								<td colspan="7">
									 <p><bean:write name="infoCurriculum" property="generalObjectives" filter="false"/></p>
								</td>
							</tr>
						</logic:equal>
					</logic:iterate>
					<tr>
						<td colspan="7">
							<strong>
								<br />
								3.&nbsp;<bean:message key="label.ects.program" bundle="GEP_RESOURCES"/>
							</strong>
					  </td>
					</tr>
					<logic:iterate id="infoCurriculum" name="infoSiteCourseInformation" property="infoCurriculums">
						<logic:equal name="infoCurriculum" property="infoCurricularCourse.externalId" value="<%= curricularCourseId.toString() %>">
							<tr>
								<td colspan="7">
									 <p><bean:write name="infoCurriculum" property="program" filter="false"/></p>
								</td>
							</tr>
						</logic:equal>
					</logic:iterate>
					<tr>
						<td colspan="7">
							<strong>
								<br />
					    		4.&nbsp;
					    		<bean:message key="label.ects.bibliography" bundle="GEP_RESOURCES"/>
				    		</strong>
			    		</td>
					</tr>
					<tr>
						<td colspan="7">
							<strong>
								<br />
					    		<bean:message key="label.ects.principalBiblio" bundle="GEP_RESOURCES"/>
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
						    	<bean:message key="label.ects.secondaryBiblio" bundle="GEP_RESOURCES"/>
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
					    		5.&nbsp;<bean:message key="label.ects.avaliation" bundle="GEP_RESOURCES"/>
				    		</strong>
					  	</td>
					</tr>
					<tr>
						<td colspan="7">
							<p>
								<logic:present name="infoSiteCourseInformation" property="executionCourse.evaluationMethod">
									<bean:write name="infoSiteCourseInformation" 
												property="executionCourse.evaluationMethod.evaluationElements" 
												filter="false"/>
								</logic:present>
								<logic:notPresent name="infoSiteCourseInformation" property="executionCourse.evaluationMethod">
									<bean:write name="infoSiteCourseInformation" property="evaluationMethod" 
												filter="false"/>
								</logic:notPresent>
							</p>
						</td>
					</tr>
				</table>
			<br/><hr/><br/><br/>
			</logic:iterate>
		</logic:notPresent>

	</logic:iterate>
	<% String today = Calendar.getInstance().getTime().toGMTString(); %>
	<p align="right"><%=today%></p>
</logic:present>