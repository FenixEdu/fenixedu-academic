<%@page import="net.sourceforge.fenixedu._development.PropertiesManager"%>
<% 
		Boolean devMode = PropertiesManager.getBooleanProperty("development.mode");
    	devMode = devMode == null ? false : devMode;
    	if (devMode) {
%>
		
		<style type="text/css">    	    
    		#navtop { background: #CC3333 url('<%= request.getContextPath() %>/images/fenix_onblack.gif') bottom repeat-x !important; background-size: 8% !important; height: 95px ! important; }
    	</style>
		
<%
    	}
	
%>