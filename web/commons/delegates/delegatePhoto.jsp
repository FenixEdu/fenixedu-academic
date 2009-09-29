<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="homepageID" name="delegateBean" property="delegate.homepage.idInternal"/>
<div style="float: left; margin-right: 1em;"><img src="<%= request.getContextPath() +"/publico/viewHomepage.do?method=retrievePhoto&amp;homepageID=" + homepageID.toString() %>"/></div>