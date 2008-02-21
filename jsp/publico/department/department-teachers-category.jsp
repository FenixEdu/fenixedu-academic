<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>
<html:xhtml/>

<div class="breadcumbs mvert0">
    <bean:define id="institutionUrl">
        <bean:message key="institution.url" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    <bean:define id="structureUrl">
        <bean:message key="link.institution.structure" bundle="GLOBAL_RESOURCES"/>
    </bean:define>
    
    <html:link href="<%= institutionUrl %>">
        <bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link href="<%= institutionUrl + structureUrl %>">
        <bean:message key="structure" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <html:link page="/department/showDepartments.faces">
        <bean:message key="academic.units" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:define id="unitId" name="unit" property="idInternal"/>
    <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>">
        <fr:view name="department" property="nameI18n"/>
    </html:link>
    &nbsp;&gt;&nbsp;
    <bean:message key="department.faculty" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
</div>

<h1>
    <bean:message key="department.faculty" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <bean:message key="of.masculine" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
    <fr:view name="department" property="nameI18n"/>
</h1>


<div id="top" class="mtop2 notarget">
	<bean:message key="label.organizeBy" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>: 
	<span class="highlight1"><bean:message key="link.teacher.byCategories" bundle="PUBLIC_DEPARTMENT_RESOURCES"/></span> 
	|
	<html:link page="<%= "/department/teachers.do?viewBy=area&amp;selectedDepartmentUnitID=" + unitId %>">
		<bean:message key="link.teacher.byAreas" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>
	</html:link>
</div>

<div  style=" padding-left: 2em; background: #fff;">
<table class="box" style="float: right;" cellspacing="0">
	<tr>
			<td class="box_header">
				<strong>
					<bean:message key="link.teacher.byCategories" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>
				</strong>
			</td>
	</tr>						
	<tr>
			<td class="box_cell">
			<ul>
					<logic:iterate id="category" name="categories" type="net.sourceforge.fenixedu.domain.teacher.Category" >
						<li>
							<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= "#" + category.getCode()%>"><fr:view name="category" property="name"/></a><br/>
						</li>
					</logic:iterate>
			</ul>
			</td>
	</tr>
</table>
</div>

<logic:iterate id="category" name="categories" type="net.sourceforge.fenixedu.domain.teacher.Category" indexId="index">
	<h2 id="<%= category.getCode() %>" class="greytxt mtop2 separator1" >
		<fr:view name="category" property="name"/>
		<logic:notEqual name="index" value="0"><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#logoist" style="<%= "padding-left: 1em; background: url(" + request.getContextPath() + "/images/cross_up.gif) left center no-repeat;" %>"><bean:message key="link.top" bundle="PUBLIC_DEPARTMENT_RESOURCES"/></a></logic:notEqual>
	</h2>

	<bean:define id="byCategory" value="true" toScope="request"/>
	<logic:iterate id="t" name="teachers" property="<%= category.getCode() %>" type="net.sourceforge.fenixedu.domain.Teacher">
		<bean:define id="teacher" name="t" toScope="request"/>
		<jsp:include page="department-teachers-card.jsp"/>
	</logic:iterate>
</logic:iterate>
