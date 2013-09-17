<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<logic:notEmpty name="announcements">
	<jsp:include flush="true" page="../listBoardAnnouncements.jsp"/>
</logic:notEmpty>

<logic:empty name="announcements">
	<bean:message key="label.research.announcements.noBoards" bundle="WEBSITEMANAGER_RESOURCES"/>
</logic:empty>