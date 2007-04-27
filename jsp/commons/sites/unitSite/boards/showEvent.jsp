<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h1 class="mtop0 mbottom03 cnone"><fr:view name="site" property="unit.name"/></h1>

<h2 class="mtop15"><bean:message key="label.messaging.events.title" bundle="MESSAGING_RESOURCES"/></h2>

<bean:define id="action" name="announcementActionVariable" toScope="request"/>

<logic:present name="announcement">
<bean:define id="announcementID" name="announcement" property="idInternal"/>

<h3 class="mvert025">
	<fr:view name="announcement" property="subject"/>
</h3>

<p class="mvert025 smalltxt greytxt2">
	<img src="<%= request.getContextPath() + "/images/dotist_post.gif"%>"/>
	<logic:present name="announcement" property="referedSubjectBegin">
		<bean:message key="label.listAnnouncements.event.occurs.from" bundle="MESSAGING_RESOURCES"/>
	</logic:present>
	<fr:view name="announcement" property="referedSubjectBegin" type="org.joda.time.DateTime" layout="no-time" />
	<logic:present name="announcement" property="referedSubjectEnd">
		<bean:message key="label.listAnnouncements.event.occurs.to" bundle="MESSAGING_RESOURCES"/>
		<fr:view name="announcement" property="referedSubjectEnd" type="org.joda.time.DateTime" layout="no-time" />
	</logic:present>
</p>
	
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