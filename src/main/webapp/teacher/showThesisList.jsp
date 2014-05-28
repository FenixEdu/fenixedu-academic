<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message bundle="APPLICATION_RESOURCES" key="label.thesis.document.confirmation"/></h2>

<div class="info">
	<p class="mvert0"><bean:message bundle="APPLICATION_RESOURCES" key="message.only.presented.thesis.with.orientator.defined"/></p>
</div>

	<div class="color888">
	    <p class="mvert0"><bean:message key="ThesisPresentationState.UNEXISTING.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message bundle="APPLICATION_RESOURCES" key="ThesisPresentationState.UNEXISTING.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.DRAFT.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message bundle="APPLICATION_RESOURCES" key="ThesisPresentationState.DRAFT.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.SUBMITTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message bundle="APPLICATION_RESOURCES" key="ThesisPresentationState.SUBMITTED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.REJECTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message bundle="APPLICATION_RESOURCES" key="ThesisPresentationState.REJECTED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.APPROVED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message bundle="APPLICATION_RESOURCES" key="ThesisPresentationState.APPROVED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.DOCUMENTS_SUBMITTED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message bundle="APPLICATION_RESOURCES" key="ThesisPresentationState.DOCUMENTS_SUBMITTED.label"/></p>
        <p class="mvert0"><bean:message key="ThesisPresentationState.DOCUMENTS_CONFIRMED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message bundle="APPLICATION_RESOURCES" key="ThesisPresentationState.DOCUMENTS_CONFIRMED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.CONFIRMED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message bundle="APPLICATION_RESOURCES" key="ThesisPresentationState.CONFIRMED.label"/></p>
	    <p class="mvert0"><bean:message key="ThesisPresentationState.EVALUATED.simple" bundle="ENUMERATION_RESOURCES"/> - <bean:message bundle="APPLICATION_RESOURCES" key="ThesisPresentationState.EVALUATED.label"/></p>
    </div>

	<br/>

	<bean:message bundle="APPLICATION_RESOURCES" key="label.thesis.teacher.oriented"/>:
    <fr:view name="orientedTheses" schema="teacher.thesis.table">
        <fr:layout name="tabular-sortable">
            <fr:property name="classes" value="tstyle1"/>

            <fr:property name="link(view)" value="/thesisDocumentConfirmation.do?method=viewThesis&amp;thesisID=%s"/>
            <fr:property name="key(view)" value="link.view"/>
            <fr:property name="param(view)" value="thesis.externalId/thesisID"/>
            <fr:property name="visibleIfNot(view)" value="documentsSubmitted"/>

            <fr:property name="link(confirm)" value="/thesisDocumentConfirmation.do?method=viewThesis&amp;thesisID=%s"/>
            <fr:property name="key(confirm)" value="link.thesis.confirm.documents"/>
            <fr:property name="param(confirm)" value="thesis.externalId/thesisID"/>
            <fr:property name="visibleIf(confirm)" value="documentsSubmitted"/>

            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="/thesisDocumentConfirmation.do?method=showThesisList"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "thesis.student.number" : request.getParameter("sortBy") %>"/>
        </fr:layout>
    </fr:view>

	<br/>

	<bean:message bundle="APPLICATION_RESOURCES" key="label.thesis.teacher.cooriented"/>:
    <fr:view name="coorientedTheses" schema="teacher.thesis.table">
        <fr:layout name="tabular-sortable">
            <fr:property name="classes" value="tstyle1"/>

            <fr:property name="link(view)" value="/thesisDocumentConfirmation.do?method=viewThesis&amp;thesisID=%s"/>
            <fr:property name="key(view)" value="link.view"/>
            <fr:property name="param(view)" value="thesis.externalId/thesisID"/>

            <fr:property name="sortParameter" value="sortBy"/>
            <fr:property name="sortUrl" value="/thesisDocumentConfirmation.do?method=showThesisList"/>
            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "thesis.student.number" : request.getParameter("sortBy") %>"/>
        </fr:layout>
    </fr:view>
