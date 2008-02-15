<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>
<html:xhtml/>

<bean:define id="announcementAction" name="announcementActionVariable" toScope="request"/>
<bean:define id="eventAction" name="eventActionVariable" toScope="request"/>

<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

	<logic:equal name="site" property="showAnnouncements" value="true"> 
	<logic:equal name="site" property="showEvents" value="true">
		<tr class="usitechannels">
			<th class="usitechannel1 width50pc">
				<bean:message key="label.announcements"/>
			</th>
			<th class="usitechannel2">
				<bean:message key="label.events"/>
			</th>
		</tr>
	</logic:equal>
	</logic:equal>
	
	<logic:equal name="site" property="showAnnouncements" value="false"> 
	<logic:equal name="site" property="showEvents" value="true">
		<tr class="usitechannels">
			<th class="usitechannel2">
				<bean:message key="label.events"/>
			</th>
		</tr>
	</logic:equal>
	</logic:equal>
	
	<logic:equal name="site" property="showAnnouncements" value="true"> 
	<logic:equal name="site" property="showEvents" value="false">
		<tr class="usitechannels">
			<th class="usitechannel1">
				<bean:message key="label.announcements"/>
			</th>
		</tr>
	</logic:equal>
	</logic:equal>


	<%-- <tr> that contains events and annoucements are shown if at least one channel exists--%>
	<logic:equal name="site" property="showEvents" value="true">
	<logic:equal name="site" property="showAnnouncements" value="true">
		<tr class="usitechannels">
	</logic:equal>
	</logic:equal>

	<logic:equal name="site" property="showEvents" value="true">
	<logic:equal name="site" property="showAnnouncements" value="false">
		<tr class="usitechannels">
	</logic:equal>
	</logic:equal>
	
	<logic:equal name="site" property="showEvents" value="false">
	<logic:equal name="site" property="showAnnouncements" value="true">
		<tr class="usitechannels">
	</logic:equal>
	</logic:equal>
    
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
			
			<logic:notEmpty name="announcementRSSActionVariable">
				<bean:define id="announcementRSSAction" name="announcementRSSActionVariable"/>
				
				<logic:iterate id="announcement" name="announcements" length="1">
					<bean:define id="rssBoard" name="announcement" property="announcementBoard"/>
					
					<div class="fright">
						<html:link page="<%= String.format("%s?method=simple&amp;%s", announcementRSSAction, context) %>" paramId="announcementBoardId" paramName="rssBoard" paramProperty="idInternal">
							<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>
						</html:link>
					</div>
				</logic:iterate>
			</logic:notEmpty>
			
			<logic:iterate id="announcement" name="announcements">
				<bean:define id="announcementID" name="announcement" property="idInternal"/>

				<fr:view name="announcement">
					<fr:layout name="view-announcement">
					  		<fr:property name="subjectClasses" value="mvert025"/>
					  		<fr:property name="dateClasses" value="mtop025 mbottom05 color888"/>
					  		<fr:property name="contentClasses" value="usiteBody mvert025"/>
					  		<fr:property name="viewMoreUrl" value="<%= announcementAction + "?method=viewAnnouncement&amp;" + context +  "&amp;announcementId=" + announcementID%>"/>
					</fr:layout>
				</fr:view>
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

			<logic:notEmpty name="eventRSSActionVariable">
				<bean:define id="eventRSSAction" name="eventRSSActionVariable"/>
			
				<logic:iterate id="announcement" name="eventAnnouncements" length="1">
					<bean:define id="rssBoard" name="announcement" property="announcementBoard"/>
					
					<div class="fright">
						<html:link page="<%= String.format("%s?method=simple&amp;%s", eventRSSAction, context) %>" paramId="announcementBoardId" paramName="rssBoard" paramProperty="idInternal">
							<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>
						</html:link>
					</div>
				</logic:iterate>
			</logic:notEmpty>
			
			<logic:iterate id="announcement" name="eventAnnouncements">
				<bean:define id="announcementID" name="announcement" property="idInternal"/>
	
				<fr:view name="announcement">
					<fr:layout name="view-announcement">
					  		<fr:property name="subjectClasses" value="mvert025"/>
					  		<fr:property name="dateClasses" value="mtop025 mbottom05 color888"/>
					  		<fr:property name="contentClasses" value="usiteBody mvert025"/>
					  		<fr:property name="viewMoreUrl" value="<%= eventAction + "?method=viewAnnouncement&amp;" + context +  "&amp;announcementId=" + announcementID%>"/>
					</fr:layout>
				</fr:view>
				<br/>
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


	<logic:equal name="site" property="showEvents" value="true">
	<logic:equal name="site" property="showAnnouncements" value="true">
		</tr>
	</logic:equal>
	</logic:equal>

	<logic:equal name="site" property="showEvents" value="true">
	<logic:equal name="site" property="showAnnouncements" value="false">
		</tr>
	</logic:equal>
	</logic:equal>
	
	<logic:equal name="site" property="showEvents" value="false">
	<logic:equal name="site" property="showAnnouncements" value="true">
		</tr>
	</logic:equal>
	</logic:equal>


