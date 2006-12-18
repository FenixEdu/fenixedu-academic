<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">	
	<em><bean:message key="researcher.interests.title" bundle="RESEARCHER_RESOURCES"/></em> <!-- tobundle -->
  	<h2 id='pageTitle'/><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.interestsManagement.title"/></h2>

	<logic:notPresent name="alterOrder">
	<ul>
		<li>
			<html:link module="/researcher" page="/interests/interestsManagement.do?method=prepareInsertInterest">
				<bean:message bundle="RESEARCHER_RESOURCES" key="link.new.interest" />
			</html:link>
		</li>
	</ul>
	</logic:notPresent>
	
	<logic:present name="alterOrder">
	<ul>
		<li>
			<html:link module="/researcher" page="/interests/interestsManagement.do?method=prepare">
				<bean:message bundle="RESEARCHER_RESOURCES" key="link.back" />
			</html:link>
		</li>
	</ul>
	</logic:present>
	
	<p class="mtop15 mbottom05"><bean:message key="researcher.interests.management.tableTitle" bundle="RESEARCHER_RESOURCES"/>:</p> <!-- tobundle -->
	
	
	<logic:notPresent name="alterOrder">
	
		<fr:view name="researchInterests" layout="tabular-list" >
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="researchInterest.summary"/>

				<fr:property name="link(edit)" value="/interests/interestsManagement.do?method=prepareEditInterest"/>
				<fr:property name="param(edit)" value="idInternal/oid"/>
				<fr:property name="key(edit)" value="researcher.interestsManagement.edit"/>
				<fr:property name="bundle(edit)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(edit)" value="2"/>

				<fr:property name="link(delete)" value="/interests/interestsManagement.do?method=delete"/>
				<fr:property name="param(delete)" value="idInternal/oid"/>
				<fr:property name="key(delete)" value="researcher.interestsManagement.delete"/>
				<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(delete)" value="3"/>
				
				<fr:property name="classes" value="tstyle2 mtop0 mbottom1"/>
				<fr:property name="suffixes" value="),"/>
			</fr:layout>
		</fr:view>

		<html:link page="/interests/interestsManagement.do?method=alterOrder"> <bean:message key="link.alterOrder" bundle="RESEARCHER_RESOURCES"/></html:link>
	</logic:notPresent> 
		
	<logic:present name="alterOrder">

		<fr:form action="/interests/interestsManagement.do?method=changeOrderUsingAjaxTree">
   		<input id="tree-structure" type="hidden" name="tree" value=""/>
		</fr:form>
	
		<fr:view name="researchInterests" layout="tree">
		<fr:layout>
			<fr:property name="treeId" value="tree"/>
	        <fr:property name="fieldId" value="tree-structure"/> <!-- reference to the hidden field above -->
	        <fr:property name="eachLayout" value="values-dash"/>
	        <fr:property name="eachSchema" value="researchInterest.summary"/>
	        <fr:property name="includeImage" value="false"/>
	        <fr:property name="classes" value="mtop0 mbottom1"/>
		     <fr:property name="hiddenLinks">
	            <html:link page="/interests/interestsManagement.do?method=up&oid=${idInternal}">
	                <bean:message key="link.moveUp" bundle="RESEARCHER_RESOURCES"/>
	            </html:link>
	            <html:link page="/interests/interestsManagement.do?method=down&oid=${idInternal}">
	                <bean:message key="link.moveDown" bundle="RESEARCHER_RESOURCES"/>
	            </html:link>
            </fr:property>
		</fr:layout>
 	</fr:view>

	
	<div id="tree-controls" style="display: none;" class="mtop1">
	 	<fr:form action="/interests/interestsManagement.do?method=prepare">
	        <!-- submits the form on top of the page, search for: tree-structure -->
	        <html:button property="saveButton" onclick="treeRenderer_saveTree('tree');">
	            <bean:message key="button.save" bundle="RESEARCHER_RESOURCES"/>
	        </html:button>
	    
	        <html:submit>
	            <bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/>
	        </html:submit>
	    </fr:form>
	</div>
	</logic:present>

   	<script type="text/javascript">
	    document.getElementById("tree-controls").style.display = 'block';
	</script>
	
</logic:present>

