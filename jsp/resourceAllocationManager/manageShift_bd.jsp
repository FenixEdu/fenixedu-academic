<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<html:xhtml/>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="link.manage.turnos"/></h2>

<p class="mbottom05">O curso seleccionado &eacute;:</p>
<jsp:include page="context.jsp"/>

<h3><bean:message key="title.editTurno"/></h3>

<jsp:include page="editShift.jsp"/>

<jsp:include page="shiftLessonList.jsp"/>

<jsp:include page="shiftClassesList.jsp"/>