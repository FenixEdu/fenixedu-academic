<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="cid" name="container" property="idInternal"/>

<fr:edit id="portal" name="bean" schema="select.portal.for.container" action="<%= "/contentManagement.do?method=addPortal&amp;cid=" + cid %>">
	<fr:hidden slot="container" name="container"/>
</fr:edit>