<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<ul class="treemenu">
	<li>
		<html:link page="/l1">
			Home
		</html:link>
    </li>
	<logic:present name="homepage" property="blog">
		<li>
			<html:link page="/l1">
				Blog: <bean:write name="homepage" property="blog.name"/>
			</html:link>
    	</li>
	</logic:present>
	<li>
		<html:link page="/l1">
			Curriculum Vitae
		</html:link>
    </li>
	<li>
		<html:link page="/l1">
			Links
		</html:link>
    </li>
	<li>
		<html:link page="/l3">
			Contacts
		</html:link>
    </li>
</ul>
