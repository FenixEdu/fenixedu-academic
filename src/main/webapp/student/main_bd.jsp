<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<p>
	<img src="<%= request.getContextPath() %>/images/portalEst-id.gif" alt="<bean:message key="portalEst-id" bundle="IMAGE_RESOURCES" />" />
</p>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<div class="photo" align="center">
	<img src="<%= request.getContextPath() %>/images/student_photo1.jpg" alt="<bean:message key="student_photo1" bundle="IMAGE_RESOURCES" />" width="150" height="100" />
	<img src="<%= request.getContextPath() %>/images/student_photo2.jpg" alt="<bean:message key="student_photo2" bundle="IMAGE_RESOURCES" />" width="150" height="100" />
	<img src="<%= request.getContextPath() %>/images/student_photo3.jpg" alt="<bean:message key="student_photo3" bundle="IMAGE_RESOURCES" />" width="150" height="100" />
</div>
<p>
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
	<div class="warning1 mvert15">
		<p class="mvert05"><b><bean:message key="group.enrolment" bundle="STUDENT_RESOURCES"/></b></p>
		<logic:iterate id="genericDegreeWarning" name="genericDegreeWarnings">
			<p class="mvert05"><bean:write name="genericDegreeWarning"/>.</p>
		</logic:iterate>
	</div>
</logic:notEmpty>


<style type="text/css">
table.asd tr {
border-left: 1px solid #ccc;
border-right: 1px solid #ccc;
}
table.asd tr.clear {
border-left: 1px solid #fff;
border-right: 1px solid #fff;
}
table.asd tr h4 {
font-weight: normal;
}
table.asd tr.disabled {
color: #888;
}
table.asd tr td {
border: 0px solid #000;
vertical-align: top;
}
table.asd tr th, table.asd tr td {
padding: 0.5em 1em;
}
table.asd tr.heading th {
text-align: left;
}
table tr.disabled td {
color: #bbb;
}

table tr.disabled td span.success0 {
color: #555;
background: #e5e5e5;
}
</style>


<logic:notEmpty name="studentPortalBeans">
	<logic:iterate id="studentPortalBean" name="studentPortalBeans">
		<h3 class="mtop15 mbottom05">
			<bean:write name="studentPortalBean" property="degree.presentationName"/> - <bean:write name="executionSemester"/>
			<logic:notEmpty name="studentPortalBean" property="degree.site">
			<app:contentLink name="studentPortalBean" property="degree.site" target="_blank">
				<span style="font-size: 12px; font-weight: normal;"><bean:message key="link.appearance" bundle="STUDENT_RESOURCES"/></span>
			</app:contentLink>
			</logic:notEmpty>
		</h3>
		
		
		<table class="tstyle1 thlight mtop1">
		<logic:iterate id="executionCoursesAnnouncement" name="studentPortalBean" property="executionCoursesAnnouncements">
			<tr style="border-top: 3px solid #ddd;">
				<bean:size id="evaluationAnnouncementsNumber" name="executionCoursesAnnouncement" property="evaluationAnnouncements" />

				<% String rowNumber = String.valueOf(evaluationAnnouncementsNumber + 1); %>

				<th rowspan="<%= rowNumber %>" style="vertical-align: top; padding-right: 2em; text-align: left;">
					<h4 class="mtop025">
					<app:contentLink name="executionCoursesAnnouncement" property="executionCourse.site" target="_blank">
							<bean:write name="executionCoursesAnnouncement" property="executionCourse.nome"/>
					</app:contentLink>
					</h4>
				</th>
				<th></th>
				<th><bean:message key="message.out.realization.date" bundle="STUDENT_RESOURCES" /></th>
				<th><bean:message key="message.out.room" bundle="STUDENT_RESOURCES" /></th>
				<th><bean:message key="message.out.enrolment.period.normal"  bundle="STUDENT_RESOURCES" /></th>
				<th></th>
			</tr>
			<logic:notEmpty name="executionCoursesAnnouncement" property="evaluationAnnouncements">
				<logic:iterate id="evaluationAnnouncement" name="executionCoursesAnnouncement" property="evaluationAnnouncements">
					<!-- Várias condições para:
							Testes/Exames: 	disabled, se já foram realizados
											warning, se está a decorrer o seu prazo de inscrição
											normal, se ainda estão para decorrer
											
							Agrupamentos:	normal, se está inscrito
											disabled, se não está inscrito e o periodo de inscrição expirou
					 -->
					 
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
									<html:link page="/viewEnroledExecutionCourses.do?method=prepare">
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
			<!-- <logic:empty name="executionCoursesAnnouncement" property="evaluationAnnouncements">
				<tr>
					<td colspan="5"><span class="color888 smalltxt"><bean:message key="message.out.no.evaluations" bundle="STUDENT_RESOURCES" /></span></td>
				</tr>
			</logic:empty> -->

				
			<tr class="clear">
				<td colspan="5" style="border: none; padding: 0.75em 0; background: #fff;"></td>
			</tr>

		</logic:iterate>
		</table>
	</logic:iterate>
</logic:notEmpty>
