<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:present name="space">

	<bean:define id="currentSpace" name="space"/>

	<logic:notPresent name="space" property="suroundingSpace">
		<html:link page="/index.do">
			<bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/>
		</html:link>
	</logic:notPresent>
	<logic:present name="space" property="suroundingSpace">
		<bean:define id="space" name="space" property="suroundingSpace" toScope="request"/>
		<jsp:include page="spaceCrumbs.jsp"/>
	</logic:present>	

	>
	
<% if (pageContext.findAttribute("currentSpace") == pageContext.findAttribute("selectedSpace")) { %>
	<logic:equal name="currentSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
		<bean:write name="currentSpace" property="spaceInformation.presentationName"/>
	</logic:equal>
	<logic:equal name="currentSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
		<bean:write name="currentSpace" property="spaceInformation.presentationName"/>
	</logic:equal>
	<logic:equal name="currentSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
		<bean:write name="currentSpace" property="spaceInformation.presentationName"/>
	</logic:equal>
	<logic:equal name="currentSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		<bean:write name="currentSpace" property="spaceInformation.presentationName"/>
	</logic:equal>
<% } else { %>
	<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="currentSpace" paramProperty="spaceInformation.idInternal">
		<logic:equal name="currentSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
			<bean:write name="currentSpace" property="spaceInformation.presentationName"/>
		</logic:equal>
		<logic:equal name="currentSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
			<bean:write name="currentSpace" property="spaceInformation.presentationName"/>
		</logic:equal>
		<logic:equal name="currentSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
			<bean:write name="currentSpace" property="spaceInformation.presentationName"/>
		</logic:equal>
		<logic:equal name="currentSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Room">
			<bean:write name="currentSpace" property="spaceInformation.presentationName"/>
		</logic:equal>
	</html:link>
<% } %>

</logic:present>
