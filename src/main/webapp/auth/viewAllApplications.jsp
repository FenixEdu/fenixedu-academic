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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<html:xhtml />

<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<h2>
	<bean:message key="oauthapps.label.manage.applications" bundle="APPLICATION_RESOURCES" />
</h2>

<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/jquery/jquery-ui.js" %>"></script>
<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/dataTables/media/js/jquery.dataTables.js"%>"></script>
<script type="text/javascript">
	$(document).ready(function() {
   		$('.results').dataTable( {
    		"iDisplayLength": 25,
    		"aoColumns": [
				null,
				null,
				null,			
				{ "sType": "numeric" },
				null,			
				null,			
				null,			
				null,
				null,
				null,
			],
			
   			"oLanguage" : {
   				"sProcessing": "A processar...",
   				"sLengthMenu": "Mostrar _MENU_ registos",
   				"sZeroRecords": "N�o foram encontrados registos",
   				"sInfo": "_START_ - _END_ de _TOTAL_",
   				"sInfoEmpty": "0 - 0 de 0",
   				"sInfoFiltered": "(filtrado de _MAX_ total de registos)",
   				"sInfoPostFix": "",
   				"sSearch": "Procura",
   				"sFirst": "Primeiro",
   				"sPrevious": "Anterior",
   				"sNext": "Seguinte",
   				"sLast": "�ltimo"
   			},
    		"aaSorting": [[ 3, "desc" ], [ 1, "asc" ]]	    			
   		}
   		);
	});
</script>
		
<style type="text/css" title="currentStyle">
	@import "<%= request.getContextPath() + "/javaScript/dataTables/media/css/demo_table.css" %>";
	.dataTables_wrapper {
		position: relative;
		min-height: 302px;
		_height: 302px;
		clear: none;
	}
	tr.odd td.sorting_2 {
		background-color : #FAFAFA !important;
	}
</style>

<logic:notEmpty name="application">
<fr:view name="application" schema="oauthapps.view.applications">
	<fr:layout name="tabular">			
		<fr:property name="classes" value="ttstyle4 tstyle9 mtop05 results resultsLeft"/>
		<fr:property name="columnClasses" value="tdcenter, tdcenter, tdcenter, tdcenter, tdcenter"/>
		
		
		<fr:property name="linkFormat(viewAllAuthorizations)"  value="<%= "/externalApps.do?method=viewAllAuthorizations&appOid=${externalId}" %>" />
		<fr:property name="key(viewAllAuthorizations)" value="oauthapps.label.view.application.details"/>
		<fr:property name="bundle(viewAllAuthorizations)" value="APPLICATION_RESOURCES"/>
		
		<fr:property name="linkFormat(editApplication)" value="<%= "/externalApps.do?method=prepareEditApplicationAdmin&appOid=${externalId}" %>" />
		<fr:property name="key(editApplication)" value="oauthapps.label.edit.application"/>
		<fr:property name="bundle(editApplication)" value="APPLICATION_RESOURCES"/>
		<fr:property name="visibleIf(editApplication)" value="editable" />		
				
		<fr:property name="linkFormat(deleteApplicationAdmin)" value="<%= "/externalApps.do?method=deleteApplicationAdmin&appOid=${externalId}" %>" />
		<fr:property name="key(deleteApplicationAdmin)" value="oauthapps.label.delete.application"/>
		<fr:property name="bundle(deleteApplicationAdmin)" value="APPLICATION_RESOURCES"/>
		<fr:property name="visibleIf(deleteApplicationAdmin)" value="active" />
	
		<fr:property name="linkFormat(banApplicationAdmin)" value="<%= "/externalApps.do?method=banApplicationAdmin&appOid=${externalId}" %>" />
		<fr:property name="key(banApplicationAdmin)" value="oauthapps.label.ban.application"/>
		<fr:property name="bundle(banApplicationAdmin)" value="APPLICATION_RESOURCES"/>
		<fr:property name="visibleIf(banApplicationAdmin)" value="active" />

		<fr:property name="linkFormat(unbanApplicationAdmin)" value="<%= "/externalApps.do?method=unbanApplicationAdmin&appOid=${externalId}" %>" />
		<fr:property name="key(unbanApplicationAdmin)" value="oauthapps.label.unban.application"/>
		<fr:property name="bundle(unbanApplicationAdmin)" value="APPLICATION_RESOURCES"/>
		<fr:property name="visibleIf(unbanApplicationAdmin)" value="banned" />
				
		<fr:property name="renderCompliantTable" value="true"/>
	</fr:layout>
</fr:view>

<bean:define id="confirmDelete">
	<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.confirm.delete.application"/> 
</bean:define>
<bean:define id="confirmBan">
	<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.confirm.ban.application"/> 
</bean:define>
<bean:define id="confirmUnban">
	<bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.confirm.unban.application"/> 
</bean:define>


<script type="text/javascript">
		$("table img").width("75px").height("75px");
		$("a[href*=deleteApplicationAdmin]").click(function(e) {
			   answer = confirm('<%= confirmDelete %>');
			   return answer;
			});
		$("a[href*='=banApplicationAdmin']").click(function(e) {
			   answer = confirm('<%= confirmBan %>');
			   return answer;
			});
		$("a[href*='=unbanApplicationAdmin']").click(function(e) {
			   answer = confirm('<%= confirmUnban %>');
			   return answer;
			});
</script>

</logic:notEmpty>