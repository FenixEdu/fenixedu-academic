<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="thesisId" name="thesis" property="idInternal" />

<html:xhtml />

<ul>
    <li>
        <html:link page="<%= String.format("/scientificCouncilManageThesis.do?method=listApproved&ampthesisID=%s", thesisId) %>">
            <bean:message key="title.scientificCouncil.list.submitted"/>
        </html:link>
    </li>
</ul>

<h2><bean:message key="title.scientificCouncil.justify.proposal.rejection"/></h2>

<div class="dinline forminline">
    <fr:form action="<%= "/scientificCouncilManageThesis.do?method=rejectApprovedProposal&amp;thesisId=" + thesisId %>">
        <fr:edit id="bean" name="bean" schema="thesis.rejection.comment.edit">
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
                <fr:property name="columnClasses" value=",,tdclear tderror1"/>
            </fr:layout>
        </fr:edit>
        
        <html:submit>
            <bean:message key="label.submit"/>
        </html:submit>
    </fr:form>
    
    <fr:form action="/scientificCouncilManageThesis.do?method=listApproved">
        <html:submit>
            <bean:message key="label.scientificCouncil.reject.proposal"/>
        </html:submit>
    </fr:form>
</div>