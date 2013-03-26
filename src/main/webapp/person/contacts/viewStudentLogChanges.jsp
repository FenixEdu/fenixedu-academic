<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.executionCourse.log" bundle="APPLICATION_RESOURCES"/></h2>

<logic:empty name="logsList">
 <div class="infoop6">
	<bean:message key="link.personInformation.noLog" bundle="APPLICATION_RESOURCES"/>
</div>
</logic:empty>

<p class="mtop2">
	<html:link page="/partyContacts.do?method=backToShowInformation">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>

<logic:notEmpty name="logsList">

	<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/dataTables/media/js/jquery.dataTables.js"%>"></script>
		<script type="text/javascript">
			$(document).ready(function() {
	    		$('.results').dataTable( {
	    			"iDisplayLength": 25,
	    			"sPaginationType": "full_numbers",
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
	    			"aaSorting": false
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
	
	<fr:view name="logsList">
		<fr:schema type="net.sourceforge.fenixedu.domain.PersonInformationLog" bundle="APPLICATION_RESOURCES">
			<fr:slot name="person.istUsername" key="label.istid" >
				<fr:property name="classes" value="nobullet noindent"/>   
		   	</fr:slot>
		   	<fr:slot name="whenDateTime" bundle="ENUMERATION_RESOURCES" key="DATE">
				<fr:property name="classes" value="nobullet noindent"/>   
		    </fr:slot>
		    <fr:slot name="description">
		    	<fr:property name="classes" value="nobullet noindent"/>   
			</fr:slot>	
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 tstyle9 mtop05 results resultsLeft" />
			<fr:property name="columnClasses" value="acenter ,acenter ,acenter" />
			<fr:property name="renderCompliantTable" value="true"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
