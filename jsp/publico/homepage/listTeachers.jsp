<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:present name="homepages">
	<logic:notPresent name="selectedPage">
		<logic:iterate id="entry" name="homepages">
			<html:link page="/viewHomepage.do?method=listTeachers" paramId="selectedPage" paramName="entry" paramProperty="key.idInternal">
				<bean:write name="entry" property="key.name"/>
			</html:link>
			<bean:size id="numberHomepages" name="entry" property="value"/>
			<bean:write name="numberHomepages"/>
			<br/>
		</logic:iterate>
	</logic:notPresent>
	<logic:present name="selectedPage">
		<bean:define id="selectedPage" type="java.lang.String" name="selectedPage"/>
		<br/>
		<logic:iterate id="entry" name="homepages">
			<logic:equal name="entry" property="key.idInternal" value="<%= selectedPage %>">
				<bean:write name="entry" property="key.name"/>
				<br/>
				<br/>
				<logic:iterate id="homepage" name="entry" property="value">
					<bean:write name="homepage" property="person.user.userUId"/>
					<html:link action="/viewHomepage.do?method=show" paramId="homepageID" paramName="homepage" paramProperty="idInternal">
						<bean:write name="homepage" property="name"/>
					</html:link>
					<br/>
				</logic:iterate>
			</logic:equal>
		</logic:iterate>
	</logic:present>
</logic:present>