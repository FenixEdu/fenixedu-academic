<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="contextPrefix" name="contextPrefix" />
<bean:define id="extraParameters" name="extraParameters" />

<logic:present name="announcementBoards">
	<bean:define id="contextPrefix" name="contextPrefix" />
	<bean:define id="extraParameters" name="extraParameters" />
	<bean:define id="person" name="person" type="net.sourceforge.fenixedu.domain.Person"/>

	<ul>
        <li class="navheader">
            <bean:message key="title.site.manage" bundle="WEBSITEMANAGER_RESOURCES"/>
        </li>
        	<logic:iterate id="announcementBoard" name="announcementBoards" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard">
        		<li>
        			<html:link action="<%= contextPrefix + "method=viewAnnouncementBoard&announcementBoardId="+announcementBoard.getExternalId() +"&" +extraParameters%>">
        				<bean:write name="announcementBoard" property="name"/>
        			</html:link>
        		</li>
        	</logic:iterate>
	</ul>
</logic:present>