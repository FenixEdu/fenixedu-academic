<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/messaging.tld" prefix="messaging" %>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>

<h2 class="mtop15"><bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/></h2>

<bean:define id="action" name="announcementActionVariable"/>
<bean:define id="context" name="extraParameters"/>

<logic:present name="archiveDate">
    <p>
        <span class="warning0">
            <bean:message key="label.messaging.archive.selected" bundle="MESSAGING_RESOURCES"/>
            <fr:view name="archiveDate">
                <fr:layout>
                    <fr:property name="format" value="MMMM yyyy"/>
                </fr:layout>
            </fr:view>
        </span>
    </p>
</logic:present>

<logic:empty name="announcements">
	<p><em><bean:message key="label.noAnnouncements" bundle="MESSAGING_RESOURCES"/></em></p>
</logic:empty>

<logic:notEmpty name="announcements">
<logic:iterate id="announcement" name="announcements">
	<bean:define id="announcementID" name="announcement" property="idInternal"/>
	
	<div class="announcement mtop15 mbottom25">
		<p class="mvert025 smalltxt greytxt2">
			<img src="<%= request.getContextPath() + "/images/dotist_post.gif"%>"/>
			<bean:message key="label.listAnnouncements.published.in" bundle="MESSAGING_RESOURCES"/>:
			<logic:present name="announcement" property="publicationBegin">
				<fr:view name="announcement" property="publicationBegin" layout="no-time"/>
			</logic:present>
			<logic:notPresent name="announcement" property="publicationBegin">
				<fr:view name="announcement" property="creationDate" layout="no-time"/>
			</logic:notPresent>
		</p>
		
		<h3 class="mvert025">
			<html:link page="<%= String.format("%s?method=viewAnnouncement&amp;%s&amp;announcementId=%s", action, context, announcementID) %>">
				<fr:view name="announcement" property="subject"/>
			</html:link>
		</h3>
		
		<div class="usitebody mvert025">
			<fr:view name="announcement" property="body" layout="html"/>
		</div>
		
		<p>
			<em class="color888 smalltxt">
				<bean:message key="label.messaging.author" bundle="MESSAGING_RESOURCES"/>: 
				<logic:equal name="announcement" property="creator.homePageAvailable" value="true">
					<bean:define id="userName" name="announcement" property="creator.username"/>
					<html:link target="blank" href="<%= request.getContextPath() + "/homepage/" + userName %>"><fr:view name="announcement" property="creator.nickname"/></html:link>
				</logic:equal>
				<logic:notEqual name="announcement" property="creator.homePageAvailable" value="true">
					<fr:view name="announcement" property="creator.nickname"/>
				</logic:notEqual>
			</em>
		</p>
	</div>
</logic:iterate>
</logic:notEmpty>

<logic:present name="archive">
    <logic:present name="announcementBoard">
        <bean:define id="board" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>

        <div class="aarchives">
            <messaging:archive name="archive" targetUrl="<%= request.getContextPath() + "/publico" + action + "?method=viewArchive&amp;announcementBoardId=" + board.getIdInternal() + "&amp;" + context + "&amp;" %>"/>  
        </div>

    </logic:present>
</logic:present>
