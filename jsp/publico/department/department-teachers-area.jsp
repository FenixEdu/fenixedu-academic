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
	<bean:message key="label.organizeBy" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>:: 
	<html:link page="<%= "/department/teachers.do?viewBy=category&amp;selectedDepartmentUnitID=" + unitId %>">
		<bean:message key="link.teacher.byCategories" bundle="PUBLIC_DEPARTMENT_RESOURCES"/> 
	</html:link>
	|
	<span class="highlight1"><bean:message key="link.teacher.byAreas" bundle="PUBLIC_DEPARTMENT_RESOURCES"/></span>
</div>

<logic:notPresent name="ignoreAreas">
	<table class="box" style="float: right;" cellspacing="0">
		<tr>
				<td class="box_header">
					<strong>
						<bean:message key="link.teacher.byAreas" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>
					</strong>
				</td>
		</tr>						
		<tr>
				<td class="box_cell">
					<ul class="nobullet">
						<logic:iterate id="area" name="areas" type="net.sourceforge.fenixedu.domain.organizationalStructure.Unit">
							<li>
								<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="<%= "#" + area.getIdInternal()%>"><fr:view name="area" property="nameI18n"/></a><br/>
							</li>
						</logic:iterate>
					<logic:notEmpty name="teachersNoArea">
			 				<li>
			 				<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#noarea"><bean:message key="link.teacher.area.noArea" bundle="PUBLIC_DEPARTMENT_RESOURCES"/></a>
		 					</li>
					 </logic:notEmpty>
					</td>
		</tr>
	</table>
</logic:notPresent>
	 
<logic:iterate id="area" name="areas" type="net.sourceforge.fenixedu.domain.organizationalStructure.Unit" indexId="index">
		
	<h2 id="<%= area.getIdInternal() %>" class="greytxt mtop2 separator1">
		<fr:view name="area" property="nameI18n"/>
		<logic:notEqual name="index" value="0"><%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#logoist" style="<%= "padding-left: 1em; background: url(" + request.getContextPath() + "/images/cross_up.gif) left center no-repeat;" %>"><bean:message key="link.top" bundle="PUBLIC_DEPARTMENT_RESOURCES"/></a></logic:notEqual>
	</h2>

	<bean:define id="byArea" value="true" toScope="request"/>
	<logic:iterate id="t" name="teachers" property="<%= area.getIdInternal().toString() %>" type="net.sourceforge.fenixedu.domain.Teacher">
		<bean:define id="teacher" name="t" toScope="request"/>
		<jsp:include page="department-teachers-card.jsp"/>
	</logic:iterate>
</logic:iterate>

<logic:notEmpty name="teachersNoArea">
	<div class="mtop2">
		<logic:notPresent name="ignoreAreas">
			<h2 id="noarea" class="greytxt mtop2 separator1">
				<bean:message key="link.teacher.area.noArea" bundle="PUBLIC_DEPARTMENT_RESOURCES"/>
				<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="#logoist" style="<%= "padding-left: 1em; background: url(" + request.getContextPath() + "/images/cross_up.gif) left center no-repeat;" %>"><bean:message key="link.top" bundle="PUBLIC_DEPARTMENT_RESOURCES"/></a>
			</h2>
		</logic:notPresent>	
		
		<bean:define id="byArea" value="true" toScope="request"/>
		<logic:iterate id="t" name="teachersNoArea" type="net.sourceforge.fenixedu.domain.Teacher">
			<bean:define id="teacher" name="t" toScope="request"/>
			<jsp:include page="department-teachers-card.jsp"/>
		</logic:iterate>
	</div>
</logic:notEmpty>
