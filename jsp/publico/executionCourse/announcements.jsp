<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>


<h2>
	<bean:message key="label.announcements"/>
</h2>

<bean:define id="site" name="executionCourse" property="site"/>
<bean:define id="announcements" name="site" property="sortedAnnouncements"/>
<logic:empty name="announcements">
	<p><em><bean:message key="message.announcements.not.available" /></em></p>
</logic:empty>
<logic:notEmpty name="announcements">
	<logic:iterate id="announcement" name="announcements" >	
		<bean:define id="announcementId" name ="announcement" property="idInternal" />
		<div class="info-lst" id="<%= "a" + announcementId %>">
			<p class="mvert0">
				<span class="greytxt">
					<dt:format pattern="dd/MM/yyyy HH:mm">
						<bean:write name="announcement" property="lastModifiedDate.time"/>
					</dt:format>
				</span>
			</p>
			<h3 class="mvert0">
				<a class="info-title">
					<bean:write name="announcement" property="title"/>
				</a>
			</h3>
			<p class="mtop0">
				<bean:write name="announcement" property="information" filter="false"/>
			</p>
		</div>       
	</logic:iterate>
</logic:notEmpty>
