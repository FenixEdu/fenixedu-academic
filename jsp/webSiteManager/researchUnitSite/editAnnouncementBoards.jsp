<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:notEmpty name="announcements">
	<jsp:include flush="true" page="../listBoardAnnouncements.jsp"/>
</logic:notEmpty>

<logic:empty name="announcements">
	<bean:message key="label.research.announcements.noBoards" bundle="WEBSITEMANAGER_RESOURCES"/>
</logic:empty>