<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>

<h2>Renderers</h2>

<a name="top"></a>
<ul>
    <li><a href="#setup">Setup</a></li>
    <li><a href="#fundamentals">Fundamentals</a></li>
    <li><a href="#examples">Examples</a></li>
</ul>

<hr/>

<a name="setup"></a>
<h3>Setup</h3>

Renderers integrate with the application as a Struts Plugin. In the same way
as Tiles or even Struts Validator you have to declare it's use in the module's
configuration. Since the renderer's configuration is common to all modules the 
declaration of the plugin is made only in <code>struts-default.xml</code>.

Here is an extract of the module configuration:

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px; margin-top: 10px; margin-bottom: 10px;" >
    <pre>&lt;plug-in className=&quot;net.sourceforge.fenixedu.renderers.plugin.RenderersPlugin&quot;&gt;
    &lt;set-property property=&quot;config&quot; 
                  value=&quot;/WEB-INF/fenix-renderers-config.xml&quot;/&gt;
    &lt;set-property property=&quot;schemas&quot; 
                  value=&quot;/WEB-INF/fenix-renderers-schemas.xml&quot;/&gt;
    &lt;set-property property=&quot;metaObjectFactory&quot; 
                  value=&quot;net.sourceforge.fenixedu.presentationTier.renderers.factories.FenixMetaObjectFactory&quot;/&gt;
    &lt;set-property property=&quot;userIdentityFactory&quot; 
                  value=&quot;net.sourceforge.fenixedu.presentationTier.renderers.factories.FenixUserIdentityFactory&quot;/&gt;
    &lt;set-property property=&quot;schemaFactory&quot; 
                  value=&quot;net.sourceforge.fenixedu.presentationTier.renderers.factories.FenixSchemaFactory&quot;/&gt;
&lt;/plug-in&gt;</pre>
</div>

The plugin must be configured with 5 properties:
<dl>
  <dt>config</dt><dd>The context relative location of the renderers configuration</dd>
  <dt>schemas</dt><dd>The context relative location of the schemas defintions</dd>
  <dt>metaObjectFactory</dt><dd>Factory that creates meta objects that wrap the real objects</dd>
  <dt>userIdentityFactory</dt><dd>Factory that creates a user identity to associate to the current presentation</dd>
  <dt>schemaFactory</dt><dd>Factory that will create the default schemas for objects when no schema is provided</dd>
</dl>

As one of the simplest presentations of a domain object is a link to the object details you need to do
an additional modification to the module's struts configuration: you need to register the 
<code>ViewObjectAction</code>. The necessary modification is as follow:

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px; margin-top: 10px; margin-bottom: 10px;" >
    <pre>&lt;action path=&quot;/domain/view&quot; 
        type=&quot;net.sourceforge.fenixedu.presentationTier.renderers.actions.ViewObjectAction&quot;&gt;
    &lt;forward name=&quot;show&quot; path=&quot;domain.view&quot;/&gt;
&lt;/action&gt;</pre>
</div>

As you can see this requires another modification in the corresponding module's tiles definitions. An
example of the required modification is:

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px; margin-top: 10px; margin-bottom: 10px;" >
    <pre>&lt;definition name=&quot;domain.view&quot; extends=&quot;definition.manager.masterPage&quot; &gt;
    &lt;put name=&quot;body&quot; value=&quot;/commons/renderers/view.jsp&quot; /&gt;
&lt;/definition&gt;</pre>
</div>

<p>
    <strong>Note</strong> that the tiles definition example extends a definition only available in the 
    manager module. Using the conventions of the Fénix project you would extend 
    <code>"defintion.&lt;module&nbsp;name&gt;.masterPage"</code> were <code>&lt;module&nbsp;name&gt;</code>
    is replaced by the corresponding module name.
</p>

<a name="fundamentals"></a>
<h3>Fundamentals</h3>

There are three fundamental concepts:
<ul>
  <li>Renderers</li>
  <li>Layouts</li>
  <li>Schemas</li>
</ul>

<p>
    <strong>Renderers</strong> are Java classes that have as it's main task to create an html 
    component from a given datum, that is, a <strong>renderer</strong> is given some sort of 
    data, like a number or a more complex object, and the result of rendering that object is an 
    <code>HtmlComponent</code>. <strong>Renderers</strong> can have properties (through standard 
    getters and setters) that allow them to be configured and, through that, to configure the result 
    <code>HtmlComponent</code>.
</p>
<p>
    Each renderer can be associated with a specific type. This means that whenever the given type 
    should be presented (or renderer) the associated renderer is used. Nevertheless, often we need
    to present the same type in many different ways and allow to choose in each case wich of those
    ways we want. The conjuntion of a name, a renderer, and some properties for a renderer is a 
    <strong>layout</strong>.
</p>
<p>
    Every renderer as access to the object that needs to be presented and to the associated meta-object.
    This meta-object represents the object as asked to be presented, that is, contains at least the
    information in the schema. The structure of the meta-object should be
    obeyed. Among other things the meta-object contains information about which parts of the object
    should be presented. This information is represented through a <strong>schema</strong>.
    <strong>Schemas</strong> indicate, for complex objects, which slots should be shown, how they
    should be presented, and more.
</p>

<p>
    The renderers configuration is global to all modules and is divided in two files:
</p>
    
<ul>
  <li>one to configure the renderes used and layouts available (fenix-renderers-config.xml)</li>
  <li>and the other to define schemas for objects (fenix-renderers-schemas.xml)</li>
</ul>
    
<p>
    Each particular file together with the referenced DTD has some documentation that should allow
    you to understand how to extend it.
</p>

<a name="examples"></a>
<h3>Examples</h3>

The following examples intend to explain the use and extension of the renderers though simple,
concrete, and increasingly challenging situations.

<ul>
    <li><html:link page="/renderers/output.do">The first situation: presenting the world</html:link></li>
    <li><html:link page="/renderers/input.do">The second situation: give me input</html:link></li>
    <li><html:link page="/renderers/steroids.do">The third situation: input on steroids</html:link></li>
    <li><html:link page="/renderers/actions.do">The fourth situation: renderers meet actions</html:link></li>
    <li><html:link page="/renderers/new.do">The fifth situation: a new renderer</html:link></li>
    <li><html:link page="/renderers/schemas.do">Appendix A: Schemas</html:link></li>
</ul>
