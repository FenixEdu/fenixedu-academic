<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title>EP2002 - Criar Sitio</title>
  </head>
  <body>
    <table>
  	  <html:form action="/criarSitioForm">
        <tr>
          <td colspan="2"><h2>CriarSitio</h2></td>
        </tr>
    
        <!-- input Nome -->
        <tr>
          <td>Nome:</td>
          <td><html:text property="nome"/></td>
          <td><html:errors property="nome"/></td>
        </tr>
    
        <!-- input AnoCurricular -->
        <tr>
          <td>Ano Curricular</td>
          <td><html:text property="anoCurricular"/></td>
          <td><html:errors property="anoCurricular"/></td>
        </tr>
      
        <!-- input Semestre -->
        <tr>
          <td>Semestre</td>
          <td><html:text property="semestre"/></td>
          <td><html:errors property="semestre"/></td>
        </tr>
      
        <!-- input Curso -->
        <tr>
          <td>Curso</td>
          <td><html:text property="curso"/></td>
          <td><html:errors property="curso"/></td>
        </tr>
      
        <!-- input Departamento -->
        <tr>
          <td>Departamento</td>
          <td><html:text property="departamento"/></td>
          <td><html:errors property="departamento"/></td>
        </tr>
      
        <tr>
          <td colspan="2"><html:submit property="ok"/></td>
        </tr>
      </html:form>  
    </table>
  </body>
</html>
