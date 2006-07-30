<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<html:messages id="message" message="true" bundle="HOMEPAGE_RESOURCES">
	<span class="error"><!-- Error messages go here -->
		<bean:write name="message"/>
	</span>
	<br/><br/>
</html:messages>

<bean:define id="selectedPage" type="java.lang.String" name="selectedPage"/>

<logic:present name="homepages">
	<logic:iterate id="entry" name="homepages">
		<logic:equal name="entry" property="key" value="<%= selectedPage %>">
			<bean:write name="entry" property="key"/>
		</logic:equal>
		<logic:notEqual name="entry" property="key" value="<%= selectedPage %>">
			<html:link action="/viewHomepage.do?method=list" paramId="selectedPage" paramName="entry" paramProperty="key">
				<bean:write name="entry" property="key"/>
			</html:link>
		</logic:notEqual>
	</logic:iterate>

	<br/>
	<br/>

	<logic:iterate id="entry" name="homepages">
		<logic:equal name="entry" property="key" value="<%= selectedPage %>">
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