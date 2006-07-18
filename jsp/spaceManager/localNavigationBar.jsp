<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="SPACE_MANAGER">
	<ul>
		<li>
			<html:link page="/index.do">
				<bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/>
			</html:link>
		</li>
		<li>
			<html:link page="/roomClassification.do?method=viewRoomClassifications">
				<bean:message bundle="SPACE_RESOURCES" key="space.manager.room.classification.title"/>
			</html:link>
		</li>
	</ul>	
</logic:present>
