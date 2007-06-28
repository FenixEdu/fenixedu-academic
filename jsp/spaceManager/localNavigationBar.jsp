<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:xhtml/>

<logic:present role="SPACE_MANAGER">
	<ul>
		<li>
			<html:link page="/index.do">
				<bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/>
			</html:link>
		</li>
		
		<bean:define id="person" name="UserView" property="person" type="net.sourceforge.fenixedu.domain.Person"/>		
		<li>		
			<html:link page="/roomClassification.do?method=viewRoomClassifications">
				<bean:message key="space.manager.room.classification.title" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
		<%
			if(net.sourceforge.fenixedu.domain.space.Space.personIsSpacesAdministrator(person)){
		%>	
		<li>		
			<html:link page="/listChangesInTheSpaces.do?method=changesList">
				<bean:message key="space.list.changes.in.the.spaces.title" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
		<%
			}
		%>
		<li>		
			<html:link page="/searchSpace.do?method=prepare">
				<bean:message key="label.search.spaces" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
	</ul>	
</logic:present>
