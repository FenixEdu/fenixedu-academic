<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<ul class="treemenu">
	<logic:present name="homepage">
		<li>
			<bean:define id="homepageID" name="homepage" property="idInternal"/>
			<html:link page="<%= "/viewHomepage.do?method=show&homepageID=" + homepageID.toString() %>">
				Home
			</html:link>
	    </li>
    </logic:present>
</ul>

