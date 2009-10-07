<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present name="delegateBean" property="delegate.homepage">
	<bean:define id="personId" name="delegateBean" property="delegate.homepage.person.externalId" type="java.lang.String" />
	<div style="float: left; margin-right: 1em;"><img src="<%= request.getContextPath() +"/publico/retrievePersonalPhoto.do?method=retrievePhotographOnPublicSpace&amp;personId=" + personId %>"/></div>
</logic:present>