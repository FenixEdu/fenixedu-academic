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
		<table class="tstyle2 thleft thlight mtop05">
			<logic:iterate id="executionCoursesAnnouncement" name="studentPortalBean" property="executionCoursesAnnouncements">
				<tr>
					<td>
						<p class="mvert0">
							<app:contentLink name="executionCoursesAnnouncement" property="executionCourse.site" target="_blank">
								<bean:write name="executionCoursesAnnouncement" property="executionCourse.nome"/>
							</app:contentLink>
						</p>
						<logic:notEmpty name="executionCoursesAnnouncement" property="evaluationAnnouncements">
							<ul class="color777 mbottom0">
								<logic:iterate id="evaluationAnnouncement" name="executionCoursesAnnouncement" property="evaluationAnnouncements">
									<li>
										<bean:write name="evaluationAnnouncement" property="evaluationType"/>
										 "<bean:write name="evaluationAnnouncement" property="identification"/>" - 
										<bean:write name="evaluationAnnouncement" property="enrolment"/>
										<bean:write name="evaluationAnnouncement" property="realization"/>
										<bean:write name="evaluationAnnouncement" property="room"/>
										<logic:equal name="evaluationAnnouncement" property="registered" value="true">
											<span class="warning0">
												<bean:message key="label.enroled" bundle="STUDENT_RESOURCES"/>
											</span>
										</logic:equal>
									</li>
								</logic:iterate>
							</ul>
						</logic:notEmpty>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:iterate>
</logic:notEmpty>
