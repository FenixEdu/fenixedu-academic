<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/gesdis-alt.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%-- Layout component parameters : header, navLocal, body --%>


<!-- Navbar Lateral e Body Content -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
   <tr>
     <td  align="left"  bgcolor="#B5BED6"  class="barraist">
     <img alt="" border="0"  src="<%= request.getContextPath() %>/images/LogoIST.gif"  />
	</td>
    <td id="principal" bgcolor="#FFFFFF">
      <div id="header"><h4><tiles:getAsString name="institutionName" /></h4></div>
      <h1><tiles:getAsString name="executionCourseName"/></h1>
      <br />
	  <tiles:insert attribute="body" />      
    </td>	
    <td id="barranav" bgcolor="#EBEFFA" valign="top">
      <div class="blue-bckgr">
        <h3>Navega&ccedil;&atilde;o</h3>
      </div>
      <div id="nav">
      <tiles:insert attribute="navbar"/>	
<%--        <ul>
		  <li><a href="index.html">P&aacute;gina Inicial</a></li>
          <li><a href="anuncios.html">An&uacute;ncios</a></li>
          <li><div class="selected">Objectivos</div></li>
          <li><a href="programa.html">Programa</a></li>
          <li><a href="horario.html">Hor&aacute;rio</a></li>
          <li><a href="bibliografia.html">Bibliografia</a></li>
          <li> <a href="/" onclick="houdini('seccao');return false;">Sec&ccedil;&atilde;o</a></li>
        </ul>
        <dl id="seccao" style="display: none;">
          <dd><a href="#">item1</a></dd>
          <dd><a href="#">item2</a></dd>
          <dd><a href="#">item3</a></dd>
        </dl> 
        <ul>
          <li><a href="#">Login GesDis</a></li>
        </ul>--%>
      </div>
<%--      <br />
      <div class="blue-bckgr">
        <h3>Contacto</h3>
      </div>
      <p class="contacto">Prof. Ant&oacute;nio Gomes</p>
      <p class="contacto">Dep. Matem&aacute;tica</p>
      <p class="contacto">Tel. +351 21 8417000</p>
      <p class="contacto">Ext. 1000</p>
      <p class="contacto"><a href="mailto:a.gomes@math.ist.utl.pt">a.gomes@math.ist.utl.pt</a></p>
      <br />
      <h3>Personaliza&ccedil;&atilde;o</h3>
      <center>
        <p> <a href="#" onclick="setActiveStyleSheet('default'); return false;"><img src="Images/css_orig.gif" width="20" height="20" border="0" alt="" /></a> <a href="#" onclick="setActiveStyleSheet('alt1'); return false;"><img src="Images/css_alt1.gif" width="20" height="20" border="0" alt="" /></a> </p>
      </center> --%>
    </td>
  </tr>
</table>
<!--End Navbar Lateral e Body Content -->

</body>
</html:html>

