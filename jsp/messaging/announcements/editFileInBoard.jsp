<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>


<jsp:include flush="true" page="/messaging/context.jsp"/>

	<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
	<bean:define id="extraParameters" name="extraParameters" />
	<bean:define id="announcementBoard" name="board" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
	<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal"/>		

	<fr:edit name="fileContent" schema="edit.file.title.and.permissions" layout="tabular"
	action="<%=  contextPrefix + "method=prepareAddFile&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters %>"
	/>
