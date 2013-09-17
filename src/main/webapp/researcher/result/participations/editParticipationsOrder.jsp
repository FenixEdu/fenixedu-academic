<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<bean:define id="result" name="result"/>
<bean:define id="participations" name="result" property="orderedResultParticipations"/>
<bean:define id="resultId" name="result" property="externalId"/>
<bean:define id="listSchema" name="listSchema" type="java.lang.String"/>
<bean:define id="parameters" value="<%="resultId=" + resultId + "&amp;resultType=" + result.getClass().getSimpleName()%>"/>
	<logic:present name="unit">
	<bean:define id="unitID" name="unit" property="externalId"/>
	<bean:define id="parameters" value="<%=parameters + "&unitId=" + unitID %>"/>
</logic:present>
<bean:define id="prepareEdit" value="<%="/resultParticipations/prepareEdit.do?" + parameters%>"/>
<bean:define id="saveOrder" value="<%="/resultParticipations/saveOrder.do?" + parameters%>"/>	
<bean:define id="moveUp" value="<%="/resultParticipations/moveUp.do?" + parameters%>"/>
<bean:define id="moveDown" value="<%="/resultParticipations/moveDown.do?" + parameters%>"/>
<bean:define id="moveTop" value="<%="/resultParticipations/moveTop.do?" + parameters%>"/>
<bean:define id="moveBottom" value="<%="/resultParticipations/moveBottom.do?" + parameters%>"/>

<p><b><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></b></p>

<!-- form used to submit the tree structure -->
<fr:form action="<%= saveOrder %>">
	<input alt="input.tree" id="tree-structure" type="hidden" name="tree" value=""/>
</fr:form>


<p><span class="warning0">Faça <em>drag & drop</em> dos items para reordenar a lista.</span></p> <!-- tobundle -->


<fr:view name="participations" layout="tree">
	<fr:layout>
		<fr:property name="treeId" value="tree"/>
        <fr:property name="fieldId" value="tree-structure"/> <!-- reference to the hidden field above -->
        <fr:property name="eachLayout" value="values-dash"/>
        <fr:property name="eachSchema" value="<%= listSchema %>"/>
        <fr:property name="includeImage" value="false"/>
        
        <fr:property name="hiddenLinks">
            <html:link page="<%= moveUp + "&amp;participationId=${externalId}"%>">
                <bean:message key="link.moveUp" bundle="RESEARCHER_RESOURCES"/>
            </html:link>,
            <html:link page="<%= moveDown + "&amp;participationId=${externalId}"%>">
                <bean:message key="link.moveDown" bundle="RESEARCHER_RESOURCES"/>
            </html:link>,
            <html:link page="<%= moveTop + "&amp;participationId=${externalId}"%>">
                <bean:message key="link.moveTop" bundle="RESEARCHER_RESOURCES"/>
            </html:link>,
            <html:link page="<%= moveBottom + "&amp;participationId=${externalId}"%>">
                <bean:message key="link.moveBottom" bundle="RESEARCHER_RESOURCES"/>
            </html:link>
        </fr:property>
	</fr:layout>
</fr:view>

<br/>

<div id="tree-controls" style="display: none;">
 	<fr:form action="<%= prepareEdit %>">
        <!-- submits the form on top of the page, search for: tree-structure -->
        <html:button bundle="HTMLALT_RESOURCES" altKey="button.saveButton" property="saveButton" onclick="treeRenderer_saveTree('tree');">
            <bean:message key="button.save" bundle="RESEARCHER_RESOURCES"/>
        </html:button>
    
        <html:submit>
            <bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/>
        </html:submit>
    </fr:form>
</div>
<script type="text/javascript">
    document.getElementById("tree-controls").style.display = 'block';
</script>
