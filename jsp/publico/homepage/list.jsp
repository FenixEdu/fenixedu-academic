<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:present name="homepages">
	<logic:iterate id="entry" name="homepages">
		<bean:write name="entry" property="key"/>
	</logic:iterate>

	<br/>
	<br/>

	<logic:iterate id="entry" name="homepages">
		<logic:iterate id="homepage" name="entry" property="value">
			<bean:write name="homepage" property="name"/>
			<br/>
		</logic:iterate>		
	</logic:iterate>
</logic:present>