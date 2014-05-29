<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/jqTheme/ui.all.css">

<script src="${pageContext.request.contextPath}/javaScript/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/javaScript/dataTables/media/js/jquery.dataTables.js"></script>



<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="label.search.spaces.events" bundle="SPACE_RESOURCES"/></h2>

<logic:present role="role(SPACE_MANAGER)">
	 
	<logic:present name="startAfterEnd">
		<span class="error">
			<bean:message key="error.begin.after.end" bundle="SPACE_RESOURCES"/>
		</span>
	</logic:present>
	<logic:notEmpty name="bean">
		<fr:form action="/searchOccupations.do?method=searchSpaceEvents">
			<fr:edit id="bean" name="bean" schema="SearchSpaceEvents">
				<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
					<fr:property name="columnClasses" value=",,tderror1 tdclear" />
				</fr:layout>			
			</fr:edit>
			<html:submit>
				<bean:message key="link.search" bundle="SPACE_RESOURCES"/>
			</html:submit>
		</fr:form>	
	</logic:notEmpty>
	
	<logic:notEmpty name="results">
	
		<script type="text/javascript">
			$(document).ready(function() {
	    		$('.results').dataTable( {
	    			"iDisplayLength": 25,
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
	    			"aaSorting": [[ 0, "asc" ], [ 1, "asc" ] ]
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
</style>


<style type="text/css" title="currentStyle">
	.fast-entry-creation {
		display: none;
	}
	
	.fast-entry-creation div {
		margin: auto;
		vertical-align: middle;
		text-align: center;
	}
	
	.hidden-link {
		display: none;
	}
	
	.spinner {
		display: none;
	}
	
	.entry_deleted {
		opacity: 0.5;
		filter: alpha(opacity = 20);
		zoom: 1;
	}
	
	.resultsLeft th {
		text-align: left;
	}
	
</style>
		<bean:size id="resultSize" name="results"/>
		<p class="mtop15 mbottom3"><b><bean:message key="label.search.spaces.events.found" bundle="SPACE_RESOURCES"/>:  <bean:write name="resultSize"/> </b></p>	
	
		<fr:view name="results" schema="SpaceOccupationEventBean">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 tstyle9 mtop05 results resultsLeft" />
				<fr:property name="columnClasses" value="smalltxt acenter,acenter smalltxt width80px,acenter smalltxt width80px,acenter smalltxt,acenter smalltxt," />
				<fr:property name="renderCompliantTable" value="true"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>