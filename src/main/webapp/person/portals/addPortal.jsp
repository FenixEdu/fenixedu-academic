<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<bean:define id="cid" name="container" property="externalId"/>

<fr:edit id="portal" name="bean" schema="select.portal.for.container" action="<%= "/contentManagement.do?method=addPortal&amp;cid=" + cid %>">
	<fr:hidden slot="container" name="container"/>
</fr:edit>