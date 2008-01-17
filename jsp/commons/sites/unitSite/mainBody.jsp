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
				<h3 class="mvert025"><fr:view name="announcement" property="subject"/></h3>
				<p class="mtop025 mbottom05" style="color: #888;"><fr:view name="announcement" property="creationDate" layout="no-time"/></p>
				<div class="usitebody mvert025">
					<logic:equal name="announcement" property="excerptEmpty" value="true">
	                    <bean:define id="length" name="textLength"/>
						<%= ContentInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
						<fr:view name="announcement" property="body">
							<fr:layout name="html">
	<%-- 							<fr:property name="length" value="<%= String.valueOf(length) %>"/> --%>
	<%-- 							<fr:property name="tooltipShown" value="false"/> --%>
							</fr:layout>
						</fr:view>
						<%= ContentInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
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
				<h3 class="mvert025"><fr:view name="announcement" property="subject"/></h3>
				<p class="mtop025 mbottom05" style="color: #888;">
					<logic:present name="announcement" property="referedSubjectBegin">
						<logic:present name="announcement" property="referedSubjectEnd">
							<bean:message key="label.listAnnouncements.event.occurs.from" bundle="MESSAGING_RESOURCES"/>
						</logic:present>
						<logic:notPresent name="announcement" property="referedSubjectEnd">
							<bean:message key="label.listAnnouncements.event.occurs.at" bundle="MESSAGING_RESOURCES"/>
						</logic:notPresent>
				        <fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="no-time" />
					</logic:present>
					<logic:present name="announcement" property="referedSubjectEnd">
						<bean:message key="label.listAnnouncements.event.occurs.to" bundle="MESSAGING_RESOURCES"/>
						<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="no-time" />
					</logic:present>
					<logic:notEmpty name="announcement" property="place">
					/ <fr:view name="announcement" property="place"/>
					</logic:notEmpty>
				</p>
				<div class="usitebody mvert025">
					<logic:equal name="announcement" property="excerptEmpty" value="true">
	                    <bean:define id="length" name="textLength"/>
						<%= ContentInjectionRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
						<fr:view name="announcement" property="body">
		   					<fr:layout name="html">
	<%--   		      					<fr:property name="length" value="<%= String.valueOf(length) %>"/> --%>
	<%--								<fr:property name="tooltipShown" value="false"/> --%>
							</fr:layout>
						</fr:view>
  					<%= ContentInjectionRewriter.END_BLOCK_HAS_CONTEXT_PREFIX%>
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


