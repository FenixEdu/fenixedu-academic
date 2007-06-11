<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="announcementAction" name="announcementActionVariable" toScope="request"/>
<bean:define id="eventAction" name="eventActionVariable" toScope="request"/>

<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

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
    
    <bean:define id="textLength" value="350" toScope="request"/>
    
	<logic:notEqual name="site" property="showAnnouncements" value="true">
        <bean:define id="textLength" value="700" toScope="request"/>
    </logic:notEqual>
	<logic:notEqual name="site" property="showEvents" value="true">
        <bean:define id="textLength" value="700" toScope="request"/>
    </logic:notEqual>
    
	<logic:equal name="site" property="showAnnouncements" value="true">
		<td>
			<logic:notEmpty name="announcements">
			
			<logic:iterate id="announcement" name="announcements" length="1">
				<bean:define id="rssBoard" name="announcement" property="announcementBoard"/>
				
				<div class="fright">
					<html:link page="<%= String.format("/department/announcementsRSS.do?method=simple&amp;%s", context) %>" paramId="announcementBoardId" paramName="rssBoard" paramProperty="idInternal">
						<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>
					</html:link>
				</div>
			</logic:iterate>
			
			<logic:iterate id="announcement" name="announcements">
				<h3 class="mvert025"><fr:view name="announcement" property="subject"/></h3>
				<p class="mtop025 mbottom05" style="color: #888;"><fr:view name="announcement" property="creationDate" layout="no-time"/></p>
				<div class="usitebody mvert025">
					<logic:equal name="announcement" property="excerptEmpty" value="true">
	                    <bean:define id="length" name="textLength"/>
						<fr:view name="announcement" property="body">
							<fr:layout name="html">
	<%-- 							<fr:property name="length" value="<%= String.valueOf(length) %>"/> --%>
	<%-- 							<fr:property name="tooltipShown" value="false"/> --%>
							</fr:layout>
						</fr:view>
					</logic:equal>
					<logic:notEqual name="announcement" property="excerptEmpty" value="true">
						<fr:view name="announcement" property="excerpt" layout="html"/>
					</logic:notEqual>
				</div>
				<bean:define id="announcementID" name="announcement" property="idInternal"/>
				<bean:define id="siteID" name="site" property="idInternal"/>
				<p class="mtop025 mbottom15">
					<html:link page="<%= announcementAction + "?method=viewAnnouncement&amp;" + context +  "&amp;announcementId=" + announcementID%>"><bean:message key="link.viewMore"/></html:link><br/>				</p>

			</logic:iterate>

				<p>
					<html:link page="<%= announcementAction + "?method=viewAnnouncements&amp;" + context %>">
						<bean:message key="label.announcements.view.all" bundle="MESSAGING_RESOURCES"/>
					</html:link>
				</p>
			</logic:notEmpty>
			
			<logic:empty name="announcements">
                <div class="mbottom05">
        				<em><bean:message key="label.noAnnouncements" bundle="MESSAGING_RESOURCES"/></em>
                </div>
			</logic:empty>
		</td>
	</logic:equal>
	<logic:equal name="site" property="showEvents" value="true">
		<td>
			<logic:notEmpty name="eventAnnouncements">
			
			<logic:iterate id="announcement" name="eventAnnouncements" length="1">
				<bean:define id="rssBoard" name="announcement" property="announcementBoard"/>
				
				<div class="fright">
					<html:link page="<%= String.format("/department/eventsRSS.do?method=simple&amp;%s", context) %>" paramId="announcementBoardId" paramName="rssBoard" paramProperty="idInternal">
						<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>
					</html:link>
				</div>
			</logic:iterate>
			
			<logic:iterate id="announcement" name="eventAnnouncements">
				<bean:define id="rssBoard" name="announcement" property="announcementBoard" toScope="request"/>
			
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
					<logic:equal name="announcement" property="excerptEmpty" value="true">
	                    <bean:define id="length" name="textLength"/>
						<fr:view name="announcement" property="body">
		   					<fr:layout name="html">
	<%--   		      					<fr:property name="length" value="<%= String.valueOf(length) %>"/> --%>
	<%--								<fr:property name="tooltipShown" value="false"/> --%>
							</fr:layout>
						</fr:view>
					</logic:equal>
					<logic:notEqual name="announcement" property="excerptEmpty" value="true">
						<fr:view name="announcement" property="excerpt" layout="html"/>
					</logic:notEqual>
				</div>
				<bean:define id="announcementID" name="announcement" property="idInternal"/>
				<bean:define id="siteID" name="site" property="idInternal"/>
				<p class="mtop025 mbottom15">
					<html:link page="<%= eventAction + "?method=viewEvent&amp;" + context + "&amp;announcementId=" + announcementID%>"><bean:message key="link.viewMore"/></html:link><br/>
				</p>

			</logic:iterate>

				<p>
					<html:link page="<%= eventAction + "?method=viewEvents&amp;" + context %>">
						<bean:message key="label.events.view.all" bundle="MESSAGING_RESOURCES"/>
					</html:link>
				</p>
			</logic:notEmpty>
			<logic:empty name="eventAnnouncements">
                <div class="mbottom05">
        				<em><bean:message key="label.noEventAnnouncements" bundle="MESSAGING_RESOURCES"/></em>
                </div>
			</logic:empty>
		</td>
	</logic:equal>
	</tr>
</table>

