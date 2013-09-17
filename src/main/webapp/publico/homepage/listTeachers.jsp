<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<logic:present name="homepages">
	<logic:notPresent name="selectedPage">
		<logic:iterate id="entry" name="homepages">
			<html:link page="/viewHomepage.do?method=listTeachers" paramId="selectedPage" paramName="entry" paramProperty="key.externalId">
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
			<logic:equal name="entry" property="key.externalId" value="<%= selectedPage %>">
				<bean:write name="entry" property="key.name"/>
				<br/>
				<br/>
				<logic:iterate id="homepage" name="entry" property="value">
					<bean:write name="homepage" property="person.user.userUId"/>
					<html:link action="/viewHomepage.do?method=show" paramId="homepageID" paramName="homepage" paramProperty="externalId">
						<bean:write name="homepage" property="name"/>
					</html:link>
					<br/>
				</logic:iterate>
			</logic:equal>
		</logic:iterate>
	</logic:present>
</logic:present>