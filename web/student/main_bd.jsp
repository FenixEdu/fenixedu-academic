<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
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
		<table class="tstyle4 thlight mtop0">
			<logic:iterate id="executionCoursesAnnouncement" name="studentPortalBean" property="executionCoursesAnnouncements">
				<tr>
					<td colspan="5" style="border: none; padding: 0.5em 0; background: #fff;">
						<h4 class="mtop1 mbottom05">
							<app:contentLink name="executionCoursesAnnouncement" property="executionCourse.site" target="_blank">
								<bean:write name="executionCoursesAnnouncement" property="executionCourse.nome"/>
							</app:contentLink>
						</h4>
					</td>
				</tr>
				<logic:notEmpty name="executionCoursesAnnouncement" property="evaluationAnnouncements">
					<tr>
						<th>
						</th>
						<th>
							<bean:message key="message.out.realization.date" bundle="STUDENT_RESOURCES" />
						</th>
						<th>
							<bean:message key="message.out.room" bundle="STUDENT_RESOURCES" />
						</th>
						<th>
							<bean:message key="message.out.enrolment.period.normal"  bundle="STUDENT_RESOURCES" />
						</th>
						<th>
						</th>
					</tr>
					<logic:iterate id="evaluationAnnouncement" name="executionCoursesAnnouncement" property="evaluationAnnouncements">
						<tr>
							<td>
								<bean:write name="evaluationAnnouncement" property="evaluationType"/>
								 "<bean:write name="evaluationAnnouncement" property="identification"/>"
							</td>
							<td class="acenter">
								<span class="color888 smalltxt">
									<bean:write name="evaluationAnnouncement" property="realization"/>
								</span>
							</td>
							<td class="acenter">
								<span class="color888 smalltxt">
									<bean:write name="evaluationAnnouncement" property="room"/>
								</span>
							</td>
							<td class="acenter">
								<span class="color888 smalltxt">
									<bean:write name="evaluationAnnouncement" property="enrolment"/>
								</span>
							</td>
							<logic:equal name="evaluationAnnouncement" property="registered" value="true">
								<td class="acenter">
									<span class="success0">
										<bean:write name="evaluationAnnouncement" property="register"/>
									</span>
								</td>
							</logic:equal>
							<logic:equal name="evaluationAnnouncement" property="registered" value="false">
								<td class="acenter">
									<span class="color888 smalltxt">
										<bean:write name="evaluationAnnouncement" property="register"/>
									</span>
								</td>
							</logic:equal>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty name="executionCoursesAnnouncement" property="evaluationAnnouncements">
					<tr>
						<td colspan="5"><span class="color888 smalltxt"><bean:message key="message.out.no.evaluations" bundle="STUDENT_RESOURCES" /></span></td>
					</tr>
				</logic:empty>
			</logic:iterate>
		</table>
	</logic:iterate>
</logic:notEmpty>
