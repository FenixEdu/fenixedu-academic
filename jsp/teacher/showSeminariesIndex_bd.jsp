<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:link page="/showCandidacies.do?seminaryID=3"><bean:message key="label.seminaries.viewSubmissions"/></html:link><br/>
<html:link page="/selectCandidacies.do?method=prepare&amp;seminaryID=3"><bean:message key="label.seminaries.selectSubmissions"/></html:link>