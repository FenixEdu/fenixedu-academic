<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h1 class="mtop0 mbottom03 cnone"><fr:view name="site" property="unit.name"/></h1>

<h2 class="mtop15"><bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/></h2>

<bean:define id="action" name="announcementActionVariable" toScope="request"/>

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
			<html:link page="<%= action + "?method=viewAnnouncement&amp;siteID=" + request.getParameter("siteID") + "&amp;announcementId=" + announcementID %>">
				<fr:view name="announcement" property="subject"/>
			</html:link>
		</h3>
		
		<div class="ann_body mvert025">
			<fr:view name="announcement" property="body" layout="html"/>
		</div>
		
		<p>
			<em class="color888 smalltxt">
				<bean:message key="label.messaging.author" bundle="MESSAGING_RESOURCES"/>: 
				<bean:define id="userName" name="announcement" property="creator.username"/>
				<html:link target="blank" href="<%= request.getContextPath() + "/homepage/" + userName %>"><fr:view name="announcement" property="creator.nickname"/></html:link>
			</em>
		</p>
	</div>
</logic:iterate>