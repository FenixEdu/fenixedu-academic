<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:notEqual name="delegateBean" property="delegate.photoPubliclyAvailable" value="true">
     		<bean:define id="language" name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language"/>
	<div style="float: left; margin-right: 1em;">
		<img src="<%= request.getContextPath() %>/images/photo_placer01_<%= language == null ? "pt" : String.valueOf(language) %>.gif"/>
	</div>
</logic:notEqual>
<logic:equal name="delegateBean" property="delegate.photoPubliclyAvailable" value="true">
 			<bean:define id="homepageID" name="delegateBean" property="delegate.homepage.idInternal"/>
	<div style="float: left; margin-right: 1em;"><img src="<%= request.getContextPath() +"/publico/viewHomepage.do?method=retrievePhoto&amp;homepageID=" + homepageID.toString() %>"/></div>
</logic:equal>





