<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:xhtml/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unitNameWithAcronym"/></h1>

<h2 class="mtop15"><bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/></h2>

<bean:define id="action" name="announcementActionVariable" toScope="request"/>

<logic:present name="announcement">
<bean:define id="announcementID" name="announcement" property="idInternal"/>

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

<h3 class="mvert05">
	<fr:view name="announcement" property="subject"/>
</h3>

<div class="ann_body mvert05">
	<fr:view name="announcement" property="body" layout="html"/>
</div>

<p>
	<em class="color888 smalltxt">
		<bean:message key="label.messaging.author" bundle="MESSAGING_RESOURCES"/>: 
		<bean:define id="userName" name="announcement" property="creator.username"/>
		<html:link target="blank" href="<%= request.getContextPath() + "/homepage/" + userName %>"><fr:view name="announcement" property="creator.nickname"/></html:link>
	</em>
</p>
</logic:present>

<logic:notPresent name="announcement">
	<bean:message key="error.cannot.display.announcement" bundle="MESSAGING_RESOURCES"/><br/>
	<bean:message key="error.not.allowed.to.view.announcement.possible.causes" bundle="MESSAGING_RESOURCES"/>
	<ul>
		<li>
			<bean:message key="error.not.allowed.to.view.announcement" bundle="MESSAGING_RESOURCES"/>
		</li>
		<li>
			<bean:message key="error.invisible.view.announcement" bundle="MESSAGING_RESOURCES"/>
		</li>
	</ul>
</logic:notPresent>

