<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="label.thesis.document.confirmation"/></h2>


	<div class="color888">
	    <p class="mvert0"><bean:message key="ThesisPresentationState.UNEXISTING.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.UNEXISTING.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.DRAFT.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.DRAFT.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.SUBMITTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.SUBMITTED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.REJECTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.REJECTED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.APPROVED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.APPROVED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.DOCUMENTS_SUBMITTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.DOCUMENTS_SUBMITTED.label"/></p>
        <p class="mvert0"><bean:message key="ThesisPresentationState.DOCUMENTS_CONFIRMED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.DOCUMENTS_CONFIRMED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.CONFIRMED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.CONFIRMED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.EVALUATED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message key="ThesisPresentationState.EVALUATED.label"/></p>
    </div>

	<br/>

	<bean:message key="label.thesis.teacher.oriented"/>:
    <fr:view name="orientedTheses" schema="teacher.thesis.table">
        <fr:layout name="tabular-sortable">
            <fr:property name="classes" value="tstyle1"/>

            <fr:property name="link(view)" value="/thesisDocumentConfirmation.do?method=viewThesis&amp;thesisID=%s"/>
            <fr:property name="key(view)" value="link.view"/>
            <fr:property name="param(view)" value="thesis.idInternal/thesisID"/>
            <fr:property name="visibleIfNot(view)" value="documentsSubmitted"/>

            <fr:property name="link(confirm)" value="/thesisDocumentConfirmation.do?method=viewThesis&amp;thesisID=%s"/>
            <fr:property name="key(confirm)" value="link.thesis.confirm.documents"/>
            <fr:property name="param(confirm)" value="thesis.idInternal/thesisID"/>
            <fr:property name="visibleIf(confirm)" value="documentsSubmitted"/>

            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="/thesisDocumentConfirmation.do?method=showThesisList"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "thesis.student.number" : request.getParameter("sortBy") %>"/>
        </fr:layout>
    </fr:view>

	<br/>

	<bean:message key="label.thesis.teacher.cooriented"/>:
    <fr:view name="coorientedTheses" schema="teacher.thesis.table">
        <fr:layout name="tabular-sortable">
            <fr:property name="classes" value="tstyle1"/>

            <fr:property name="link(view)" value="/thesisDocumentConfirmation.do?method=viewThesis&amp;thesisID=%s"/>
            <fr:property name="key(view)" value="link.view"/>
            <fr:property name="param(view)" value="thesis.idInternal/thesisID"/>

            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="/thesisDocumentConfirmation.do?method=showThesisList"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "thesis.student.number" : request.getParameter("sortBy") %>"/>
        </fr:layout>
    </fr:view>
