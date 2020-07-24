<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:xhtml/>

<style>
table tr.disabled td { color: #bbb; }
table tr.disabled td span.success0 { color: #555; background: #e5e5e5; }
</style>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<div class="hidden-xs" align="center" style="margin-bottom: 15px">
	<img src="${pageContext.request.contextPath}/images/student_photo1.jpg" alt="<bean:message key="student_photo1" bundle="IMAGE_RESOURCES" />" width="185" height="123" class="img-thumbnail" />
	<img src="${pageContext.request.contextPath}/images/student_photo2.jpg" alt="<bean:message key="student_photo2" bundle="IMAGE_RESOURCES" />" width="185" height="123" class="img-thumbnail" />
	<img src="${pageContext.request.contextPath}/images/student_photo3.jpg" alt="<bean:message key="student_photo3" bundle="IMAGE_RESOURCES" />" width="185" height="123" class="img-thumbnail" />
</div>
<p class="hidden-xs">
	<bean:message key="message.info.student" />
</p>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<logic:notEmpty name="genericDegreeWarnings">
	<div class="alert alert-warning">
		<b><bean:message key="group.enrolment" bundle="STUDENT_RESOURCES"/></b>
		<logic:iterate id="genericDegreeWarning" name="genericDegreeWarnings">
			<p class="mvert05"><bean:write name="genericDegreeWarning"/>.</p>
		</logic:iterate>
	</div>
</logic:notEmpty>

<logic:notEmpty name="studentPortalBeans">
	<logic:iterate id="studentPortalBean" name="studentPortalBeans">
		<h3 class="mtop15 mbottom05">
			<bean:write name="studentPortalBean" property="degree.presentationName"/>
			<logic:notEmpty name="studentPortalBean" property="degree.siteUrl">
			<a href="${studentPortalBean.degree.siteUrl}" target="_blank">
				<span style="font-size: 12px; font-weight: normal;"><bean:message key="link.appearance" bundle="STUDENT_RESOURCES"/></span>
			</a>
			</logic:notEmpty>
		</h3>
		
		<div class="table-responsive">
		<table class="tstyle1 thlight table table-condensed">
		<logic:iterate id="executionCoursesAnnouncement" name="studentPortalBean" property="executionCoursesAnnouncements">
			<tr style="border-top: 2px solid #ddd;">
				<bean:size id="evaluationAnnouncementsNumber" name="executionCoursesAnnouncement" property="evaluationAnnouncements" />

				<% String rowNumber = String.valueOf(evaluationAnnouncementsNumber + 1); %>

				<th rowspan="<%= rowNumber %>" style="vertical-align: top; padding-right: 2em; text-align: left;">
					<h4 class="mtop025">
					<a href="${executionCoursesAnnouncement.executionCourse.siteUrl}" target="_blank">
							<bean:write name="executionCoursesAnnouncement" property="executionCourse.nome"/>
					</a>
					</h4>
				</th>
				<th>
					<logic:notEmpty name="executionCoursesAnnouncement" property="executionCourse.site">
						<logic:notEmpty name="executionCoursesAnnouncement" property="executionCourse.site.alternativeSite">
							<a href="${executionCoursesAnnouncement.executionCourse.site.alternativeSite}" target="_blank">
								<bean:message key="link.alternative"  bundle="APPLICATION_RESOURCES" />
							</a>						
						</logic:notEmpty>
					</logic:notEmpty>
				</th>
				<th><bean:message key="message.out.realization.date" bundle="STUDENT_RESOURCES" /></th>
				<th><bean:message key="message.out.room" bundle="STUDENT_RESOURCES" /></th>
				<th><bean:message key="message.out.enrolment.period.normal"  bundle="STUDENT_RESOURCES" /></th>
				<th></th>
			</tr>
			<logic:notEmpty name="executionCoursesAnnouncement" property="evaluationAnnouncements">
				<logic:iterate id="evaluationAnnouncement" name="executionCoursesAnnouncement" property="evaluationAnnouncements">
					<%-- Várias condições para:
							Testes/Exames: 	disabled, se já foram realizados
											warning, se está a decorrer o seu prazo de inscrição
											normal, se ainda estão para decorrer
											
							Agrupamentos:	normal, se está inscrito
											disabled, se não está inscrito e o periodo de inscrição expirou
					 --%>
					 
				<tr class="<bean:write name="evaluationAnnouncement" property="status"/>">
				
					<td><bean:write name="evaluationAnnouncement" property="evaluationType"/>:
						<bean:write name="evaluationAnnouncement" property="identification"/>
					</td>
					<td class="acenter"><span class="color888 smalltxt"><bean:write name="evaluationAnnouncement" property="realization"/></span></td>
					<td class="acenter"><span class="color888 smalltxt"><bean:write name="evaluationAnnouncement" property="room"/></span></td>
					<td class="acenter nowrap"><span class="color888 smalltxt"><bean:write name="evaluationAnnouncement" property="enrolment"/></span></td>
					<td class="acenter nowrap">
						<logic:equal name="evaluationAnnouncement" property="registered" value="true">
							<span class="success0"><bean:write name="evaluationAnnouncement" property="register"/></span>
						</logic:equal>
						<logic:equal name="evaluationAnnouncement" property="registered" value="false">
							<span class="color888 smalltxt">
		
							<logic:equal name="evaluationAnnouncement" property="enrolmentElapsing" value="false">						
								<bean:write name="evaluationAnnouncement" property="register"/>
							</logic:equal>
							<logic:equal name="evaluationAnnouncement" property="enrolmentElapsing" value="true">
								<logic:equal name="evaluationAnnouncement" property="groupEnrolment" value="false">
									<html:link page="/enrollment/evaluations/showEvaluations.faces">
										<bean:write name="evaluationAnnouncement" property="register"/>
									</html:link>
								</logic:equal>
								<logic:equal name="evaluationAnnouncement" property="groupEnrolment" value="true">
									<html:link page="/enroll/student-groups">
										<bean:write name="evaluationAnnouncement" property="register"/>
									</html:link>
								</logic:equal>
							</logic:equal>
							
							</span>
						</logic:equal>
					</td>
			
				</tr>
					
				</logic:iterate>
			</logic:notEmpty>
				
			<tr class="clear">
				<td colspan="5" style="border: none; padding: 0.75em 0; background: #fff;"></td>
			</tr>

		</logic:iterate>
		</table>
		</div>
	</logic:iterate>
</logic:notEmpty>
