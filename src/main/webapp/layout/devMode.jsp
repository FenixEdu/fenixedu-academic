<%@page import="pt.ist.bennu.core.util.CoreConfiguration"%>
<%
	if (CoreConfiguration.getConfiguration().developmentMode()) {
%>
		
<style type="text/css">
    	#navtop { background: url('<%= request.getContextPath() %>/images/fenix_onblack.gif') center repeat-x !important; background-size: 7% !important; }
		#container {
			background: #88C1D5 !important; 
		}	
    	#navlateral { background-color: #88C1D5 !important }
    	#navlateral a { background-color: #88C1D5 !important }
		.navheader  { background-color: #88C1D5 !important } 
    	.navheader strong { background-color: #88C1D5 !important }
    	</style>
		
<%
    	}
	
%>
