<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<bean:define id="site" name="executionCourse" property="site"/>

<logic:present name="executionCourse" property="executionPeriod.inquiryResponsePeriod">
	<logic:equal name="executionCourse" property="availableForInquiries" value="true">
		<logic:equal name="executionCourse" property="executionPeriod.inquiryResponsePeriod.insideResponsePeriod" value="true">
			<br/>
			<p>
				<strong>
					<bean:message key="message.inquiries.available.prefix"/>
				</strong>
				<logic:present role="role(STUDENT)">
					<bean:message key="message.inquiries.available.students"/>
					<html:link href="<%= request.getContextPath() + "/student/studentInquiry.do?method=showCoursesToAnswer"%>">
						<bean:message key="message.inquiries.available.at.here"/>	
					</html:link>
					.
				</logic:present>
				<logic:notPresent role="role(STUDENT)">
					<bean:message key="message.inquiries.available.non.students"/>
				</logic:notPresent>				
			</p>
		</logic:equal>
	</logic:equal>
</logic:present>

<logic:notEmpty name="site" property="initialStatement">
	<div class="citation">
		<p>
			<bean:write name="site" property="initialStatement" filter="false"/>
		</p>
	</div>
</logic:notEmpty>

<logic:notEmpty name="lastAnnouncement">		
			<bean:define id="announcement" name="lastAnnouncement"/>
			<div id="announcs">
				<h2 class="announcs-head"><bean:message key="label.lastAnnouncements"/></h2>
				<div class="last-announc">
					<div class="last-announc-name"><fr:view name="announcement" property="subject"/></div>
					<div class="last-announc-post-date">
							<fr:view name="announcement" property="lastModification"/>
					</div>
					<p class="last-announc-info">
						<logic:notEmpty name="lastAnnouncement" property="body" > 
							<fr:view name="announcement" property="body" layout="html" />
						</logic:notEmpty>
						<logic:empty name="lastAnnouncement" property="body" >
						<bean:message  key="label.bodyAnnouncementsEmpty"/>
						</logic:empty>
				</div>
				<logic:empty name="lastFiveAnnouncements"></div></logic:empty>
		</logic:notEmpty>

		<logic:notEmpty name="lastFiveAnnouncements">		    	
				<ul class="more-announc">
				<logic:iterate id="announcement" name="lastFiveAnnouncements">
					<bean:define id="announcementId" name ="announcement" property="externalId" />
					<li class="more-announc"><span class="more-announc-date">
						<fr:view name="announcement" property="lastModification"/> - </span>
						<html:link page="<%="/announcementManagement.do?method=viewAnnouncement&amp;executionCourseID=" + request.getAttribute("executionCourseID") + "&amp;announcementId=" + announcementId %>">
							<fr:view name="announcement" property="subject"/>
						</html:link></li>
				</logic:iterate>
				</ul>
			</div>		    
        </logic:notEmpty>

<logic:notEmpty name="site" property="alternativeSite">
	<div style="margin-top: 2em; margin-bottom: 1em;">
		<h2>
			<bean:message key="message.siteAddress" />
		</h2>
		<bean:define id="alternativeSite" name="site" property="alternativeSite"/>
		<html:link href="<%=(String)pageContext.findAttribute("alternativeSite") %>" target="_blank">
			<bean:write name="alternativeSite" />
		</html:link>
	</div>
	<br/>
</logic:notEmpty>			

<logic:notEmpty name="site" property="introduction">
	<h2>
		<bean:message key="message.introduction" />
	</h2>
	<p>
		<bean:write name="site" property="introduction" filter="false" />
	</p>
</logic:notEmpty>

<bean:define id="professorships" name="executionCourse" property="professorshipsSortedAlphabetically"/>
<logic:notEmpty name="professorships">
	<h2>
		<bean:message key="label.lecturingTeachers"/>
	</h2>
	<logic:iterate id="professorship" name="professorships">
		<logic:equal name="professorship" property="responsibleFor" value="true">
			<p style="margin-top: 6px; margin-bottom: 6px;">
				<bean:define id="professorship" name="professorship" toScope="request"/>
				<jsp:include page="professorshipName.jsp"/>
				(<bean:message key="label.responsible"/>)
			</p>
		</logic:equal>
	</logic:iterate>
	<logic:iterate id="professorship" name="professorships">
		<logic:notEqual name="professorship" property="responsibleFor" value="true">
			<p style="margin-top: 6px; margin-bottom: 6px;">
				<bean:define id="professorship" name="professorship" toScope="request"/>
				<jsp:include page="professorshipName.jsp"/>
			</p>
		</logic:notEqual>
	</logic:iterate>
</logic:notEmpty>
