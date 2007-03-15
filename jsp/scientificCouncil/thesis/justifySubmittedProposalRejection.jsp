<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="thesisId" name="thesis" property="idInternal" />

<html:xhtml />

<ul>
    <li>
        <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=listSubmitted&ampthesisID=%s", thesisId) %>">
            <bean:message key="title.scientificCouncil.list.submitted"/>
        </html:link>
    </li>
</ul>

<h2><bean:message key="title.scientificCouncil.justify.proposal.rejection"/></h2>