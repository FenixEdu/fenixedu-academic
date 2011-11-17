<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>


<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="label.search.spaces.events" bundle="SPACE_RESOURCES"/></h2>

<logic:present role="SPACE_MANAGER">
	
	<logic:notEmpty name="bean">
		<fr:form action="/searchSpace.do?method=searchSpaceEvents">
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
		
	<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/dataTables/media/js/jquery.dataTables.js"%>"></script>
	
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
	    			}
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

		<p class="mtop15 mbottom3"><b><bean:message key="label.search.spaces.events.found" bundle="SPACE_RESOURCES"/>:</b></p>	
	
		<fr:view name="results" schema="SpaceOccupationEventBean">			
			<fr:layout name="tabular-bennu">
				<fr:property name="classes" value="tstyle4 tstyle9 mtop05 results resultsLeft" />
				<fr:property name="columnClasses" value="smalltxt acenter,acenter smalltxt width80px,acenter smalltxt width80px,acenter smalltxt,acenter smalltxt," />
				<fr:property name="renderCompliantTable" value="true"/>
			</fr:layout>
		</fr:view>
		<%-- <fr:view name="results" schema="SpaceOccupationEventBean">
			<fr:layout name="ajax-tabular">
				<fr:property name="classes" value="tstyle4 mtop05" />
				<fr:property name="columnClasses" value="smalltxt,acenter smalltxt,acenter smalltxt,acenter smalltxt,acenter smalltxt,smalltxt" />
				<fr:property name="enableAjax" value="false" />
			</fr:layout>
		</fr:view> --%>
		
	</logic:notEmpty>
</logic:present>