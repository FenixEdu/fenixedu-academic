<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>

<html:xhtml />

<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<em><bean:message key="label.person.main.title" /></em>
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
				null	
			],
			
   			"oLanguage" : {
   				"sProcessing": "A processar...",
   				"sLengthMenu": "Mostrar _MENU_ registos",
   				"sZeroRecords": "Não foram encontrados registos",
   				"sInfo": "_START_ - _END_ de _TOTAL_",
   				"sInfoEmpty": "0 - 0 de 0",
   				"sInfoFiltered": "(filtrado de _MAX_ total de registos)",
   				"sInfoPostFix": "",
   				"sSearch": "Procura",
   				"sFirst": "Primeiro",
   				"sPrevious": "Anterior",
   				"sNext": "Seguinte",
   				"sLast": "Último"
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

<fr:view name="application" schema="oauthapps.view.applications">
	<fr:layout name="tabular">			
		<fr:property name="classes" value="ttstyle4 tstyle9 mtop05 results resultsLeft"/>
		
		<fr:property name="linkFormat(viewAllAuthorizations)"  value="<%= "/externalApps.do?method=viewAllAuthorizations&appOid=${externalId}" %>" />
		<fr:property name="key(viewAllAuthorizations)" value="oauthapps.label.view.application.details"/>
		<fr:property name="bundle(viewAllAuthorizations)" value="APPLICATION_RESOURCES"/>
		
		<fr:property name="linkFormat(editApplication)" value="<%= "/externalApps.do?method=prepareEditApplicationAdmin&appOid=${externalId}" %>" />
		<fr:property name="key(editApplication)" value="oauthapps.label.edit.application"/>
		<fr:property name="bundle(editApplication)" value="APPLICATION_RESOURCES"/>
				
		<fr:property name="renderCompliantTable" value="true"/>
	</fr:layout>
</fr:view>
