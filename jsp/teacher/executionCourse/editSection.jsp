<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2>
	<bean:message key="link.createSection"/>
</h2>

<fr:edit name="section" type="net.sourceforge.fenixedu.domain.Section" schema="net.sourceforge.fenixedu.domain.SectionEditor">
</fr:edit>