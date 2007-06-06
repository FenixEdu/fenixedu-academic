<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2>
	<bean:message key="title.library.theses.list.exported"/>
</h2>

<logic:empty name="theses">
	<p>
		<em><bean:message key="message.library.theses.list.empty"/></em>
	</p>
</logic:empty>

<logic:notEmpty name="theses">

	<logic:present name="unmarkSuccessfull">
		<div>
			<span class="success0">
				<bean:message key="message.theses.export.undo"/>
			</span>
		</div>
	</logic:present>

	<logic:messagesPresent property="warning" message="true">
	    <html:messages id="message" message="true" property="warning">
	        <p>
	        	<span class="warning0">
	        		<bean:write name="message"/>
	        	</span>
        	</p>
    	</html:messages>
	</logic:messagesPresent>

	<fr:form action="/theses/listExported.do?method=unmark">
	
		    <script type="text/javascript">
		    	function invertSelectionAll(n) {
		    	    var allChecked = true;
	    	        var elements = document.getElementsByName(n);
	    	        
    				for (var index=0; index<elements.length; index++) {
   				        var element = elements[index];
						if (! element.checked) {
					            allChecked = false;
	            	            element.checked = true;
						}
					}
					
					if (allChecked) {
				        for (var index=0; index<elements.length; index++) {
	                    	elements[index].checked = false;
                     	}
					}
				}
			</script>

		<p>
			<html:link href="#" onclick="invertSelectionAll('thesesIDs')">
				<bean:message key="link.theses.list.selectAll"/>
			</html:link>,
			<html:submit>
				<bean:message key="button.unexport"/>
			</html:submit>
		</p>

	    <fr:view name="theses" schema="library.thesis.list">
	        <fr:layout name="tabular-sortable">
		        <fr:property name="classes" value="tstyle1"/>
		        
	            <fr:property name="sortParameter" value="sortBy"/>
	            <fr:property name="sortUrl" value="/theses/listExported.do?method=prepare"/>
	            <fr:property name="sortBy" value="<%= request.getParameter("sortBy") == null ? "discussed" : request.getParameter("sortBy") %>"/>
	
				<fr:property name="checkable" value="true"/>
				<fr:property name="checkboxName" value="thesesIDs"/>
				<fr:property name="checkboxValue" value="idInternal"/>
	        </fr:layout>
	    </fr:view>

		<p>
			<html:link href="#" onclick="invertSelectionAll('thesesIDs')">
				<bean:message key="link.theses.list.selectAll"/>
			</html:link>,
			<html:submit>
				<bean:message key="button.unexport"/>
			</html:submit>
		</p>

	</fr:form>
</logic:notEmpty>
