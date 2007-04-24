<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<table class="usitechannels">
	<tr>
		<logic:present name="announcements">
			<th class="usitechannel1">
				<bean:message key="label.announcements"/>
			</th>
		</logic:present>
		<logic:present name="eventAnnouncements">
			<th class="usitechannel2">
			<bean:message key="label.events"/>
			</th>
		</logic:present>
	</tr>
	<tr>
	<logic:present name="announcements">
		<td>
			<logic:iterate id="announcement" name="announcements">
				<h3 class="mvert025"><fr:view name="announcement" property="subject"/></h3>
				<p class="mtop025 mbottom05" style="color: #888;"><fr:view name="announcement" property="creationDate" layout="no-time"/></p>
				<div class="usitebody mvert025">
					<fr:view name="announcement" property="body">
						<fr:layout name="short-html">
							<fr:property name="tooltipShown" value="false"/>
						</fr:layout>
					</fr:view>
				</div>
				<bean:define id="announcementID" name="announcement" property="idInternal"/>
				<bean:define id="siteID" name="site" property="idInternal"/>
				<p class="mtop025 mbottom15">
					<html:link page="<%= "/researchSite/manageResearchUnitAnnouncements.do?method=viewAnnouncement&amp;siteID=" + siteID + "&amp;announcementId=" + announcementID%>"><bean:message key="label.permalink"/></html:link><br/>				</p>
			</logic:iterate>
		</td>
	</logic:present>
	<logic:present name="eventAnnouncements">
		<td>
			<logic:iterate id="announcement" name="eventAnnouncements">
			
				<h3 class="mvert025"><fr:view name="announcement" property="subject"/></h3>
				<p class="mtop025 mbottom05" style="color: #888;">
					<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="no-time" />
					<logic:present name="announcement" property="referedSubjectEnd">
						a 
						<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="no-time" />
					</logic:present>
				</p>
				<div class="usitebody mvert025">
					<fr:view name="announcement" property="body">
						<fr:layout name="short-html">
								<fr:property name="tooltipShown" value="false"/>
							</fr:layout>
					</fr:view>
				</div>
				<bean:define id="announcementID" name="announcement" property="idInternal"/>
				<bean:define id="siteID" name="site" property="idInternal"/>
				<p class="mtop025 mbottom15">
					<html:link page="<%= "/researchSite/manageResearchUnitAnnouncements.do?method=viewAnnouncement&amp;siteID=" + siteID + "&amp;announcementId=" + announcementID%>"><bean:message key="label.permalink"/></html:link><br/>
				</p>
			</logic:iterate>
		</td>
	</logic:present>
	</tr>
</table>

