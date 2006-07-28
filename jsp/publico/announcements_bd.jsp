<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="announcements">
		<h2><bean:message key="message.announcements.not.available" /></h2>
	</logic:empty>
	
	<logic:notEmpty name="component" property="announcements">
		<h2><bean:message key="label.announcements"/></h2>
		<logic:iterate id="announcement" name="component" property="announcements" >	
			<bean:define id="announcementId" name ="announcement" property="idInternal" />
			<div class="info-lst" id="a<%= announcementId.toString() %>">
				<h3>
					<a class="info-title">
						<bean:write name="announcement" property="title"/>
					</a>
					<br />
					<span class="greytxt">
						<dt:format pattern="dd/MM/yyyy HH:mm">
							<bean:write name="announcement" property="lastModifiedDate.time"/>
						</dt:format>
					</span>
				</h3>
                 
				<p><bean:write name="announcement" property="information" filter="false"/></p>
            </div>       
		</logic:iterate>
	</logic:notEmpty>
</logic:present>

<logic:notPresent name="siteView" property="component">
	<h2><bean:message key="message.announcements.not.available" /></h2>
</logic:notPresent>
