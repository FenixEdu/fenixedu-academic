<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="action" name="announcementActionVariable" toScope="request"/>

<table class="usitechannels">
	<tr>
		<logic:equal name="site" property="showAnnouncements" value="true"> 
			<th class="usitechannel1">
				<bean:message key="label.announcements"/>
			</th>
		</logic:equal>
		<logic:equal name="site" property="showEvents" value="true">
			<th class="usitechannel2">
			<bean:message key="label.events"/>
			</th>
		</logic:equal>
	</tr>
	<tr>
	<logic:equal name="site" property="showAnnouncements" value="true">
		<td>
			<logic:notEmpty name="announcements">
			<logic:iterate id="announcement" name="announcements">
				<h3 class="mvert025"><fr:view name="announcement" property="subject"/></h3>
				<p class="mtop025 mbottom05" style="color: #888;"><fr:view name="announcement" property="creationDate" layout="no-time"/></p>
				<div class="usitebody mvert025">
					<fr:view name="announcement" property="body">
						<fr:layout name="short-html">
							<fr:property name="length" value="350"/>
							<fr:property name="tooltipShown" value="false"/>
						</fr:layout>
					</fr:view>
				</div>
				<bean:define id="announcementID" name="announcement" property="idInternal"/>
				<bean:define id="siteID" name="site" property="idInternal"/>
				<p class="mtop025 mbottom15">
					<html:link page="<%= action + "?method=viewAnnouncement&amp;siteID=" + siteID + "&amp;announcementId=" + announcementID%>"><bean:message key="link.more"/></html:link><br/>				</p>
			</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="announcements">
				<em><bean:message key="label.noAnnouncements" bundle="MESSAGING_RESOURCES"/></em>
			</logic:empty>
		</td>
	</logic:equal>
	<logic:equal name="site" property="showEvents" value="true">
		<td>
			<logic:notEmpty name="eventAnnouncements">
			<logic:iterate id="announcement" name="eventAnnouncements">
			
				<h3 class="mvert025"><fr:view name="announcement" property="subject"/></h3>
				<p class="mtop025 mbottom05" style="color: #888;">
					<logic:present name="announcement" property="referedSubjectBegin">
					<bean:message key="label.listAnnouncements.event.occurs.from" bundle="MESSAGING_RESOURCES"/>
					</logic:present>
					<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="no-time" />
					<logic:present name="announcement" property="referedSubjectEnd">
						<bean:message key="label.listAnnouncements.event.occurs.to" bundle="MESSAGING_RESOURCES"/>
						<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="no-time" />
					</logic:present>
				</p>
				<div class="usitebody mvert025">
					<fr:view name="announcement" property="body">
						<fr:layout name="short-html">
								<fr:property name="length" value="350"/>
								<fr:property name="tooltipShown" value="false"/>
							</fr:layout>
					</fr:view>
				</div>
				<bean:define id="announcementID" name="announcement" property="idInternal"/>
				<bean:define id="siteID" name="site" property="idInternal"/>
				<p class="mtop025 mbottom15">
					<html:link page="<%= action + "?method=viewEvent&amp;siteID=" + siteID + "&amp;announcementId=" + announcementID%>"><bean:message key="link.more"/></html:link><br/>
				</p>
			</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="eventAnnouncements">
				<em><bean:message key="label.noEventAnnouncements" bundle="MESSAGING_RESOURCES"/></em>
			</logic:empty>
		</td>
	</logic:equal>
	</tr>
</table>

