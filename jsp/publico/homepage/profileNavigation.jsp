<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<ul class="treemenu">
	<li>
		<html:link page="/l1">
			Header Link 1
		</html:link>
    </li>
	<li>
		<html:link page="/l2">
			Header Link 2
		</html:link>
    </li>
	<li>
		<html:link page="/l3">
			Header Link 3
		</html:link>
    </li>
</ul>