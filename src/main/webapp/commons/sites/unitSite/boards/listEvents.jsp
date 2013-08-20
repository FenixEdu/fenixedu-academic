<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/messaging.tld" prefix="messaging" %>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>

<h2 class="mtop15"><bean:message key="label.messaging.events.title" bundle="MESSAGING_RESOURCES"/></h2>

<bean:define id="action" name="eventActionVariable" toScope="request"/>
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
	<p><em><bean:message key="label.noEventAnnouncements" bundle="MESSAGING_RESOURCES"/></em></p>
</logic:empty>

<logic:notEmpty name="announcements">
<logic:iterate id="announcement" name="announcements">
<bean:define id="announcementID" name="announcement" property="externalId"/>

<div class="announcement mtop15 mbottom25">

<h3 class="mvert025">
	<html:link page="<%= String.format("%s?method=viewEvent&amp;%s&amp;announcementId=%s", action, context, announcementID) %>">
		<fr:view name="announcement" property="subject"/>
	</html:link>
</h3>


<p class="mvert025 smalltxt greytxt2">
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
	<logic:present name="announcement" property="place">
	/ <fr:view name="announcement" property="place"/>
	</logic:present>
</p>

				
	<div class="usitebody mvert025">
		<fr:view name="announcement" property="body" layout="html"/> 
	</div>

	<p>
		<em class="color888 smalltxt">
			<bean:message key="label.messaging.author" bundle="MESSAGING_RESOURCES"/>: 
			<logic:notEmpty name="announcement" property="authorEmail">
				<bean:define id="authorEmail" name="announcement" property="authorEmail"/>
				<a href="mailto:<%= authorEmail %>">
					<fr:view name="announcement" property="author"/>
				</a>
			</logic:notEmpty>
			<logic:empty name="announcement" property="authorEmail">
				<fr:view name="announcement" property="author"/>
			</logic:empty>
		</em>
	</p>

</logic:iterate>
</logic:notEmpty>

<logic:present name="archive">
    <logic:present name="announcementBoard">
        <bean:define id="board" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>

        <div class="aarchives">
            <messaging:archive name="archive" targetUrl="<%= request.getContextPath() + "/publico" + action + "?method=viewArchive&amp;announcementBoardId=" + board.getExternalId() + "&amp;" + context + "&amp;" %>"/>  
        </div>

    </logic:present>
</logic:present>
