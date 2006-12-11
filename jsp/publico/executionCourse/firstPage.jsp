<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="site" name="executionCourse" property="site"/>

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
					<p class="last-announc-info"><fr:view name="announcement" property="body" layout="html" /></p>
				</div>
				<logic:empty name="lastFiveAnnouncements" ></div></logic:empty>
		</logic:notEmpty>

		<logic:notEmpty name="lastFiveAnnouncements">		    	
				<ul class="more-announc">
				<logic:iterate id="announcement" name="lastFiveAnnouncements">
					<bean:define id="announcementId" name ="announcement" property="idInternal" />
					<li class="more-announc"><span class="more-announc-date">
						<fr:view name="announcement" property="lastModification"/> - </span>
						<html:link  page="<%="/announcementManagement.do"+"?method=viewAnnouncements&amp;executionCourseID=" + request.getParameter("executionCourseID") + "#ID_" + announcementId%>"
									anchor="<%= announcementId.toString() %>">
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
				<bean:message key="label.responsible"/>
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
