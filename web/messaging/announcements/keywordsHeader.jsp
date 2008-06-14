<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="announcementBoard">
	<logic:notPresent name="announcementBoard" property="readers">
		<logic:present name="announcementBoard" property="keywords">
				<bean:define id="keywords" name="announcementBoard" property="keywords"/>
					<meta name="keywords" content="<%=keywords %>"/>	
			</logic:present>	
	</logic:notPresent>
</logic:present>

<logic:present name="announcement">
	<logic:notPresent name="announcement" property="readers">
		<logic:present name="announcement" property="keywords">
			<bean:define id="keywords" name="announcement" property="keywords"/>
			
			 <meta name="keywords" content="<%=keywords %>"/>
	
			<bean:define id="idInternal" name="announcement" property="announcementBoard.idInternal" />		
			<bean:define id="linkRSSAnn" type="java.lang.String" toScope="request"><%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/external/announcementsRSS.do?announcementBoardId=" + idInternal%></bean:define>
		</logic:present>
	</logic:notPresent>
</logic:present>


