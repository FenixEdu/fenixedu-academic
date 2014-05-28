<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:xhtml/>

<bean:define id="announcementAction" name="announcementActionVariable" toScope="request"/>
<bean:define id="eventAction" name="eventActionVariable" toScope="request"/>

<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

	<logic:equal name="actual$site" property="showAnnouncements" value="true"> 
	<logic:equal name="actual$site" property="showEvents" value="true">
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
	
	<logic:equal name="actual$site" property="showAnnouncements" value="false"> 
	<logic:equal name="actual$site" property="showEvents" value="true">
		<tr class="usitechannels">
			<th class="usitechannel2">
				<bean:message key="label.events"/>
			</th>
		</tr>
	</logic:equal>
	</logic:equal>
	
	<logic:equal name="actual$site" property="showAnnouncements" value="true"> 
	<logic:equal name="actual$site" property="showEvents" value="false">
		<tr class="usitechannels">
			<th class="usitechannel1">
				<bean:message key="label.announcements"/>
			</th>
		</tr>
	</logic:equal>
	</logic:equal>


	<%-- <tr> that contains events and annoucements are shown if at least one channel exists--%>
	<logic:equal name="actual$site" property="showEvents" value="true">
	<logic:equal name="actual$site" property="showAnnouncements" value="true">
		<tr class="usitechannels">
	</logic:equal>
	</logic:equal>

	<logic:equal name="actual$site" property="showEvents" value="true">
	<logic:equal name="actual$site" property="showAnnouncements" value="false">
		<tr class="usitechannels">
	</logic:equal>
	</logic:equal>
	
	<logic:equal name="actual$site" property="showEvents" value="false">
	<logic:equal name="actual$site" property="showAnnouncements" value="true">
		<tr class="usitechannels">
	</logic:equal>
	</logic:equal>
    
    <bean:define id="textLength" value="350" toScope="request"/>
    
	<logic:notEqual name="actual$site" property="showAnnouncements" value="true">
        <bean:define id="textLength" value="700" toScope="request"/>
    </logic:notEqual>
	<logic:notEqual name="actual$site" property="showEvents" value="true">
        <bean:define id="textLength" value="700" toScope="request"/>
    </logic:notEqual>
    
	<logic:equal name="actual$site" property="showAnnouncements" value="true">
		<td>
			<logic:notEmpty name="announcements">
			
			<logic:notEmpty name="announcementRSSActionVariable">
				<bean:define id="announcementRSSAction" name="announcementRSSActionVariable"/>
				
				<logic:iterate id="announcement" name="announcements" length="1">
					<bean:define id="rssBoard" name="announcement" property="announcementBoard"/>
					
					<div class="fright">
						<html:link page="<%= String.format("%s?method=simple&amp;%s", announcementRSSAction, context) %>" paramId="announcementBoardId" paramName="rssBoard" paramProperty="externalId">
							<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>
						</html:link>
					</div>
				</logic:iterate>
			</logic:notEmpty>
			
			<logic:iterate id="announcement" name="announcements">
				<bean:define id="announcementID" name="announcement" property="externalId"/>

				<fr:view name="announcement">
					<fr:layout name="view-announcement">
					  		<fr:property name="subjectClasses" value="mvert025"/>
					  		<fr:property name="dateClasses" value="mtop025 mbottom05 color888"/>
					  		<fr:property name="contentClasses" value="usiteBody mtop025 mbottom15"/>
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
	<logic:equal name="actual$site" property="showEvents" value="true">
		<td>
		    <bean:define id="hasAnnouncements" value="false" toScope="request"/>
		    <logic:notEmpty name="today-events">
		    	<bean:define id="hasAnnouncements" value="true" toScope="request"/>
		    </logic:notEmpty>
			<logic:notEmpty name="future-events">
		    	<bean:define id="hasAnnouncements" value="true" toScope="request"/>
		    </logic:notEmpty>
		    <logic:notEmpty name="eventAnnouncements">
		    	<bean:define id="hasAnnouncements" value="true" toScope="request"/>
		    </logic:notEmpty>
		    
		    <logic:equal name="hasAnnouncements" value="true">
			<logic:notEmpty name="eventRSSActionVariable">
				<bean:define id="eventRSSAction" name="eventRSSActionVariable"/>
				<bean:define id="rssBoard" name="announcementBoard"/>
					
					<div class="fright">
						<html:link page="<%= String.format("%s?method=simple&amp;%s", eventRSSAction, context) %>" paramId="announcementBoardId" paramName="rssBoard" paramProperty="externalId">
							<img src="<%= request.getContextPath() %>/images/rss_ico.png"/>
						</html:link>
					</div>
				
			</logic:notEmpty>
			</logic:equal>

			<logic:notEmpty name="today-events">
				 <p class="mtop0 mbottom05" style="color: #555;"><em><bean:message key="label.events.occurring" bundle="MESSAGING_RESOURCES"/></em>:</p>
				<logic:iterate id="announcement" name="today-events">
				<bean:define id="announcementID" name="announcement" property="externalId"/>
	
				<fr:view name="announcement">
					<fr:layout name="view-announcement">
					  		<fr:property name="subjectClasses" value="mvert025"/>
					  		<fr:property name="dateClasses" value="mtop025 mbottom05 color888"/>
					  		<fr:property name="contentClasses" value="usiteBody mtop025 mbottom15"/>
					  		<fr:property name="viewMoreUrl" value="<%= eventAction + "?method=viewAnnouncement&amp;" + context +  "&amp;announcementId=" + announcementID%>"/>
					</fr:layout>
				</fr:view>
			</logic:iterate>
			</logic:notEmpty>
	
			<logic:notEmpty name="future-events">
			    <p class="mtop0 mbottom05" style="color: #555;"><em><bean:message key="label.events.next" bundle="MESSAGING_RESOURCES"/></em>:</p>
				<logic:iterate id="announcement" name="future-events">
				<bean:define id="announcementID" name="announcement" property="externalId"/>
	
				<fr:view name="announcement">
					<fr:layout name="view-announcement">
					  		<fr:property name="subjectClasses" value="mvert025 dinline"/>
					  		<fr:property name="dateClasses" value="dblock mtop025 mbottom05 color888"/>
					  		<fr:property name="contentClasses" value="usiteBody mtop025 mbottom15"/>
					  		<fr:property name="viewMoreUrl" value="<%= eventAction + "?method=viewAnnouncement&amp;" + context +  "&amp;announcementId=" + announcementID%>"/>
					</fr:layout>
				</fr:view>
			</logic:iterate>
			</logic:notEmpty>




			<logic:notEmpty name="eventAnnouncements">
			<p class="mtop0 mbottom05" style="color: #555;"><em><bean:message key="label.events.past" bundle="MESSAGING_RESOURCES"/></em>:</p>
						
			<logic:iterate id="announcement" name="eventAnnouncements">
				<bean:define id="announcementID" name="announcement" property="externalId"/>
	
				<fr:view name="announcement">
					<fr:layout name="view-announcement">
					  		<fr:property name="subjectClasses" value="mvert025"/>
					  		<fr:property name="dateClasses" value="mtop025 mbottom05 color888"/>
					  		<fr:property name="contentClasses" value="usiteBody mtop025 mbottom15"/>
					  		<fr:property name="viewMoreUrl" value="<%= eventAction + "?method=viewAnnouncement&amp;" + context +  "&amp;announcementId=" + announcementID%>"/>
					</fr:layout>
				</fr:view>
			</logic:iterate>

			</logic:notEmpty>

				<logic:equal name="hasAnnouncements" value="true">
				<p>
					<html:link page="<%= eventAction + "?method=viewEvents&amp;" + context %>">
						<bean:message key="label.events.view.all" bundle="MESSAGING_RESOURCES"/>
					</html:link>
				</p>
				</logic:equal>

			<logic:empty name="future-events">
				<logic:empty name="today-events">
					<logic:empty name="eventAnnouncements">
		                <div class="mbottom05">
		        				<em><bean:message key="label.noEventAnnouncements" bundle="MESSAGING_RESOURCES"/></em>
		                </div>
					</logic:empty>
				</logic:empty>
			</logic:empty>
			
		</td>
	</logic:equal>


	<logic:equal name="actual$site" property="showEvents" value="true">
	<logic:equal name="actual$site" property="showAnnouncements" value="true">
		</tr>
	</logic:equal>
	</logic:equal>

	<logic:equal name="actual$site" property="showEvents" value="true">
	<logic:equal name="actual$site" property="showAnnouncements" value="false">
		</tr>
	</logic:equal>
	</logic:equal>
	
	<logic:equal name="actual$site" property="showEvents" value="false">
	<logic:equal name="actual$site" property="showAnnouncements" value="true">
		</tr>
	</logic:equal>
	</logic:equal>


