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
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
<bean:define id="extraParameters" name="extraParameters" type="java.lang.String"/>

<jsp:include page="/commons/renderers/treeRendererHeader.jsp" />

<em><bean:message key="label.webSiteManagement" bundle="MESSAGING_RESOURCES"/></em>

<h2><bean:message key="label.sticky.ordering"
	bundle="MESSAGING_RESOURCES" /></h2>

<logic:notEmpty name="announcements">


<logic:present name="alterOrder">
	
	<bean:define id="announcementBoardId" name="announcementBoard"
		property="externalId" />
<p><bean:message key="messaging.sticky.info" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="MESSAGING_RESOURCES"/></p>		
	<fr:form
		action="<%="/announcementsManagement.do?method=changeOrderUsingAjaxTree&announcementBoardId="+announcementBoardId %>">
		<input alt="input.tree" id="tree-structure" type="hidden" name="tree"
			value="" />
	</fr:form>
	<div class="infoop8 mbottom1" style="padding: 15px;">
	<div class="mvert1">
			<fr:view name="announcements" layout="tree">
			<fr:layout>
				<fr:property name="treeId" value="tree"/>
		        <fr:property name="fieldId" value="tree-structure"/> <!-- reference to the hidden field above -->
		        <fr:property name="eachLayout" value="values-dash"/>
		        <fr:property name="eachSchema" value="announcement.view-with-priority-subject"/>
		        <fr:property name="includeImage" value="false"/>
		        <fr:property name="classes" value="mtop0 mbottom1"/>
			     <fr:property name="hiddenLinks">
		            <html:link page="/webSiteManager/announcementsManagement.do?method=up&oid=${externalId}">
		                <bean:message key="link.moveUp" bundle="RESEARCHER_RESOURCES"/>
		            </html:link>
		            <html:link page="/webSiteManager/announcementsManagement.do?method=down&oid=${externalId}">
		                <bean:message key="link.moveDown" bundle="RESEARCHER_RESOURCES"/>
		            </html:link>
	            </fr:property>
			</fr:layout>
		 	</fr:view>
	 	</div>
</div>
	
		
	<div id="tree-controls" style="display: none;" class="mtop1">
	 	<fr:form action="<%="/announcementsManagement.do?method=start&announcementBoardId="+announcementBoardId %>">
	        <!-- submits the form on top of the page, search for: tree-structure -->
	        <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="treeRenderer_saveTree('tree');">
	            <bean:message key="button.save" bundle="RESEARCHER_RESOURCES"/>
	        </html:button>
	    
	        <html:submit>
	            <bean:message key="button.back" bundle="RESEARCHER_RESOURCES"/>
	        </html:submit>
	    </fr:form>
	</div>
	</logic:present>

   	<script type="text/javascript">
	    document.getElementById("tree-controls").style.display = 'block';
	</script>
</logic:notEmpty>

<logic:empty name="announcements">
	<p><em><bean:message key="messaging.sticky.empty" bundle="MESSAGING_RESOURCES"/></em></p> 
</logic:empty>

<style type="text/css">
 #tree li, #tree-container li { margin: 5px 0;}
 /*#tree img { display: none !important; }*/
</style>
	