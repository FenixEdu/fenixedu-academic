<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>


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
	
			<bean:define id="externalId" name="announcement" property="announcementBoard.externalId" />		
			<bean:define id="linkRSSAnn" type="java.lang.String" toScope="request"><%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/external/announcementsRSS.do?announcementBoardId=" + externalId%></bean:define>
		</logic:present>
	</logic:notPresent>
</logic:present>


