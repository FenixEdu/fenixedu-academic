<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="announcements">
		<p><em><bean:message key="message.announcements.not.available" /></em></p>
	</logic:empty>
		
	<logic:notEmpty name="component" property="announcements">
		<h2><bean:message key="label.announcements"/></h2>
		<logic:iterate id="announcement" name="component" property="announcements" >	
			<bean:define id="announcementId" name ="announcement" property="externalId" />
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
	<p><em><bean:message key="message.announcements.not.available" /></em></p>
</logic:notPresent>
