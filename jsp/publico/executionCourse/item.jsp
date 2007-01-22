<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.SectionProcessor" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ItemProcessor" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<logic:present name="section">
    <bean:define id="executionCourse" name="executionCourse" type="net.sourceforge.fenixedu.domain.ExecutionCourse"/>
    <bean:define id="section" name="section" type="net.sourceforge.fenixedu.domain.Section"/>

    <h2>
        <fr:view name="section" property="name" type="net.sourceforge.fenixedu.util.MultiLanguageString"/>
		<span class="permalink1">(<a href="<%= request.getContextPath() + SectionProcessor.getSectionAbsolutePath(executionCourse, section) %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h2>

 	<logic:notEmpty name="section" property="orderedSubSections">
		<fr:view name="section" property="orderedSubSections" layout="list">
		    <fr:layout>
		        <fr:property name="eachLayout" value="values"/>
		        <fr:property name="eachSchema" value="site.section.name"/>
		    </fr:layout>
		    <fr:destination name="section.view" path="<%= "/executionCourse.do?method=section&amp;sectionID=${idInternal}&amp;executionCourseID=" + executionCourse.getIdInternal() %>"/>
		</fr:view>
    </logic:notEmpty>

    <bean:define id="item" name="item" type="net.sourceforge.fenixedu.domain.Item"/>

	<h3 class="mtop2">
        <fr:view name="item" property="name"/>
   		<span class="permalink1">(<a href="<%= request.getContextPath() + ItemProcessor.getItemAbsolutePath(executionCourse, item) %>" name="<%= "item" + item.getIdInternal() %>"><bean:message key="label.link" bundle="SITE_RESOURCES"/></a>)</span>
    </h3>

    <logic:notEmpty name="item" property="information">
        	<fr:view name="item" property="information">
        		<fr:layout>
        			<fr:property name="classes" value="coutput1" />
        			<fr:property name="escaped" value="false" />
        			<fr:property name="newlineAware" value="false" />
        		</fr:layout>
        	</fr:view>
	</logic:notEmpty>
        
	<logic:notEmpty name="item" property="sortedVisibleFileItems">
        <fr:view name="item" property="sortedVisibleFileItems">
            <fr:layout name="list">
                <fr:property name="classes" value="coutput1 mvert0" />
                <fr:property name="eachSchema" value="site.item.file.basic"/>
                <fr:property name="eachLayout" value="values"/>
                <fr:property name="style" value="<%= "list-style-image: url(" + request.getContextPath() + "/images/icon_file.gif);" %>"/>
            </fr:layout>
        </fr:view>
	</logic:notEmpty>
</logic:present>