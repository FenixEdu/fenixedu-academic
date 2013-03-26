<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="contextPrefix" name="contextPrefix" type="java.lang.String"/>
<bean:define id="extraParameters" name="extraParameters" type="java.lang.String"/>

<em><bean:message key="label.webSiteManagement" bundle="MESSAGING_RESOURCES"/></em>

<h2><bean:message key="label.sticky.ordering"
	bundle="MESSAGING_RESOURCES" /></h2>

<logic:notEmpty name="announcements">


<logic:present name="alterOrder">
	
	<bean:define id="announcementBoardId" name="announcementBoard"
		property="idInternal" />
<p><bean:message key="messaging.sticky.info" bundle="MESSAGING_RESOURCES"/></p>		
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
		            <html:link page="/webSiteManager/announcementsManagement.do?method=up&oid=${idInternal}">
		                <bean:message key="link.moveUp" bundle="RESEARCHER_RESOURCES"/>
		            </html:link>
		            <html:link page="/webSiteManager/announcementsManagement.do?method=down&oid=${idInternal}">
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
	