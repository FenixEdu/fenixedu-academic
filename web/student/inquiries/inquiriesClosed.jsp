<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style>
table.tdwith50px td { width: 50px; }
table.tdwith90px td { width: 90px; }
.aleft { text-align: left !important; }
.acenter { text-align: center !important; }
.bold { font-weight: bold !important; }
</style>

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<bean:message key="message.inquiries.notOpen" bundle="INQUIRIES_RESOURCES"/>
