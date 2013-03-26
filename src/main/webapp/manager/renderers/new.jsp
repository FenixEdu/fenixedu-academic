<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<!-- Title and TOC -->
<a name="top"></a>
<div style="margin-bottom: 50px">
    <h2>The fourth situation: a new renderer</h2>
    
    <ul>
        <li><a href="#reasons">When and why</a></li>
        <li><a href="#contract">What a renderer must and must not do</a></li>
        <li><a href="#meta">Objects and meta-objects</a></li>
        <li><a href="#examples">Some renderers explained</a></li>
    </ul>

<a name="reasons"></a>
<h3>When and why</h3>

<p>
    Now it's the time to show how to extends the existing renderers and create new ones. But first we
    must know when should we create a new renderer instead of using other renderers, like the template
    renderer, to achieve the desired presentation.
</p>

<p>
    Renderers should allow parameterization of most aspects to ensure that it can be reused more often.
    Nevertheless one renderer must obey a certain layout. For example, the standard renderer for some object 
    details, produces a table, that is, uses a tabular layout. Renderers should not provide properties to 
    choose between several layouts (although it's possible and simple to do). Given this you should create a 
    new renderer when you need to show the same information in a layout that is not available. Note that
    <em>layout</em> here is not the attribute of a tag but the organization in which the information is
    shown.
</p>

<p>
    When presenting an object we can always use the template renderer so why should a new renderer be created?
</p>

<p>
    If a certain presentation is very specific then probably using the template renderer is the best option.
    Nevertheless defining the renderer has some nice properties related to code and behaviour 
    reuse. But there are also some situations were implementing a renderer is specially usefull:
</p>

<ul>
  <li>Renderers can accept properties and configure the generated component accordingly</li>
  <li>You can only associate custom controllers and converters from a renderer</li>
</ul>

<h3>What a renderer must and must not do</h3>
<a name="contract"></a>

<p>
    The main objective of a renderer is to produce a <code>graph</code> of <code>HtmlComponent</code> that
    represent the presentation of some object, that is, to convert an object to and <code>HtmlComponent</code>.
    There is no restrictions to what a renderer can use or to what type of compoenents it creates nevertheless
    there is one crucial rule that a renderer must follow:
</p>

<p>
    <strong>
        Two invocations of the renderer for the same object must produce the same <code>HtmlComponent</code>, 
        that is, the rendering must be idempotent.
    </strong>
</p>

<p>
    Lets clarify this rule. It's important to understand what "the same <code>HtmlComponent</code>" means.
    This rule is important so that in each request the user sees a coherent presentation. First the renderer
    is executed and the result component is presented to the user. If a form is presented to the user, then
    the user submits information back and theoretically the component presented previously must be updated
    with the new values. To avoid storing that component between sessions, the renderer is executed again with
    the same object as argument. So what matters in this whole process is that components that allow the user
    to provide input back to the application must always be generated with the same type and have the same name 
    so they are updated correctly.
</p>

<p>
    Off course this rule can be bent when really needed but it makes the implementation of the renderer more
    complex. You need to control more aspects like the naming of all components and even the renderer's 
    lifecycle.
</p>

<a name="meta"></a>
<h3>Objects and meta-objects</h3>

<p>
    The basic interface for a renderer is:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>public HtmlComponent render(Object object, Class type);</pre>
</div>

<p>
    This means that the renderer has access to the object beeing presented &#0151; obviously &#0151; and to the
    type of the object. The type of the object is specially usefull if you want to present the <code>null</code>
    value. Now suppose you have a renderer that is presenting some object. You have no more information than the one
    you can get from the object itself and, for example, a string provides very little information. A renderer of
    a person has access to all the information in person but when it delegates the presentation of the person's
    name to another renderer, that renderer has no information about the string it is about to present.
</p>

<p>
    Each renderer has access to a meta-object associated to the object beeing presented. The meta-object gives
    and abstraction of the object suitable to be presented. The meta-object, for example, provides meta-slots 
    that abstract the concrete slots of the object that should be presented. This means that the meta-object
    will only contain the slots specified in schemas.
    
    Each, meta-slot, is in itself an meta-object, and also provides information about the meta-object that contains
    this slot. This can be used to give more semantic for the presentation of a value as is the case of the
    <code>LinkRenderer</code>.
</p>

<p>
    As you can see, the meta-object is an abstraction that most renderers need to use and in some cases it can even
    be a replacement for the concrete object passed to the renderer. 
</p>

<a name="examples"></a>
<h3>Some renderers explained</h3>

<p>
    Next, some examples of renderers are presented. We start by showing one of the simplest renderers:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>class DateRenderer extends OutputRenderer {

    private String format;
    
    public String getFormat() {
        return format == null ? &quot;dd/MM/yyyy&quot; : format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Date date = (Date) object;
                
                if (date == null) {
                    return new HtmlText();
                }
                
                HttpServletRequest request = getOutputContext().getViewState().getRequest();
                Locale locale = RequestUtils.getUserLocale(request, null);
                DateFormat dateFormat = new SimpleDateFormat(getFormat(), locale);
                
                return new HtmlText(dateFormat.format(date));
            }
            
        };
    }
}</pre>
</div>

<p>
    This output renderer provides the presentation for a date. As you can se, it provides the 
    possiblity to configure the format in which the date is presented through the property
    <code>format</code>. The result of this renderer in a simple <code>HtmlText</code> component
    that shows the formated date.
</p>

<p>
    The main code of the renderer is in the <code>getLayout</code> method because of some
    inherited behaviour that considers that each renderer has on layout associated and that,
    after configuring the layout with some of the properties of the rendererer, the presentation
    of the object is given by the layout.
</p>

<p>
    The following renderer does input presentation of an enumeration. This renderer is a little more
    complex and creates an <code>HtmlMenu</code> with all available enum constants and an additional
    default selection entry.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>class EnumInputRenderer extends InputRenderer {

    private static Logger logger = Logger.getLogger(EnumInputRenderer.class);
    
    protected String getEnumDescription(Enum enumValue) {
        ResourceBundle bundle = getEnumerationBundle();
        
        String description = enumValue.toString();
        
        try {
            description = bundle.getString(enumValue.toString());
        } catch (MissingResourceException e) {
            logger.warn(&quot;could not get name of enumeration: &quot; + enumValue.toString());
        }
        
        return description;
    }

    protected ResourceBundle getEnumerationBundle() {
        return ResourceBundle.getBundle(&quot;resources.EnumerationResources&quot;);
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object targetObject, Class type) {
                Enum enumerate = (Enum) targetObject;
                
                HtmlMenu menu = new HtmlMenu();
                
                String defaultOptionTitle = RenderUtils.getResourceString(&quot;menu.default.title&quot;);
                menu.createDefaultOption(defaultOptionTitle).setSelected(enumerate == null);
                
                Object[] constants = type.getEnumConstants();
                for (Object object : constants) {
                    Enum oneEnum = (Enum) object;
                    String description = getEnumDescription(oneEnum);
                    
                    HtmlMenuOption option = menu.createOption(description);
                    option.setValue(oneEnum.toString());
                    
                    if (enumerate != null &amp;&amp; oneEnum.equals(enumerate)) {
                        option.setSelected(true);
                    }
                }
                
                menu.setConverter(new EnumConverter());
                menu.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
                
                return menu;
            }
            
        };
    }
}

class EnumConverter extends Converter {

    @Override
    public Object convert(Class type, Object value) {
        Object[] enums = type.getEnumConstants();
        
        for (int i = 0; i &lt; enums.length; i++) {
            if (enums[i].toString().equals(value)) {
                return enums[i];
            }
        }
        
        return null;
    }
    
}</pre>
</div>

<p>
    This renderer introduces a custom converter, used to convert the input value back to into the
    correct enumeration instance. The <code>EnumConverter</code> is quite simple and does not more
    information than the one provided by the interface but you could configure the converter before
    associating it to the form component with <code>setConverter</code>. If this converter is not
    specified then the standard converters from <code>BeanUtils</code> package are used and the 
    conversion would fail.
</p>

<p>
    You can also see that the renderer sets the target slot of the menu. Associating a form 
    component with a slot means that the value of the component will be used to change the 
    value of the slot when the user submits the information. Off course the value of the slot
    will only be changed if the input is validated and all values are converted successfully.
</p>

<p>
    The slot is given a key for the meta slot. The meta-object is persisted between requests so,
    when the value of the form element is submited, the value corresponding meta-slot is retrieved
    and updated accordingly. Note that associating a slot to a form component automatically names
    that component from the given key. Has a meta-slot alwyas has the same key, the component
    alwyas have the same name and is updated correctly.
</p>

<p>
    I one of the previous examples, regarding controllers, we said that to use the controllers
    we would need to define a new renderer. That renderer is shown next.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>class StringCaseChangerRenderer extends StringInputRenderer {
    
    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        HtmlInlineContainer container = new HtmlInlineContainer();

        HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) super.createTextField(object, type);
        HtmlSubmitButton caseChangeButton = new HtmlSubmitButton(&quot;&quot;);
        HtmlSubmitButton capitalizeButton = new HtmlSubmitButton(&quot;Capitalize&quot;);
        
        nameButton(caseChangeButton, &quot;case-button-name&quot;);
        nameButton(capitalizeButton, &quot;capitalize-button-name&quot;);
        
        container.addChild(component);
        container.addChild(caseChangeButton);
        container.addChild(capitalizeButton);
        
        caseChangeButton.setController(new CaseChangeController(component, caseChangeButton, capitalizeButton));
        capitalizeButton.setController(new CapitalizeController(component));
        
        return container;
    }

    private void nameButton(HtmlSubmitButton button, String attribute) {
        String buttonName = (String) getInputContext().getViewState().getAttribute(attribute);
        
        if (buttonName == null) {
            getInputContext().getViewState().setAttribute(attribute, button.getName());
        }
        else {
            button.setName(buttonName);
        }
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new TextFieldLayout() {

            @Override
            public void applyStyle(HtmlComponent component) {
                super.applyStyle(((HtmlInlineContainer)component).getChildren().get(0));
            }
        
            @Override
            protected void setContextSlot(HtmlComponent component, MetaSlotKey slotKey) {
                HtmlContainer container = (HtmlInlineContainer) component;
                HtmlSimpleValueComponent field = (HtmlSimpleValueComponent) container.getChildren().get(0);
                
                super.setContextSlot(field, slotKey);
            }
            
        };
    }
    
    class CaseChangeController extends HtmlSubmitButtonController {
        (...)
    }

    class CapitalizeController extends HtmlSubmitButtonController {
        (...)        
    }
}</pre>
</div>

<p>
    The organization of this renderer is much like the previous renderer. The main difference is
    that it extends an already defined renderer and overrides some of it's behaviour. The 
    <code>StringInputRenderer</code> generates an input field. This renderer wraps that field
    and the two buttons in a container so it needs to override the layout to consider the
    component as a container and not directly as a text field. Nevertheless in the 
    <code>TextFieldLayout.setContextSlot</code> method you can see the form component beeing
    associated with the corresponding slot.
</p>

<p style="margin-top: 50px; padding-top: 10px; border-top: 1px solid #AAAAAA">
    <span style="float: left;"><a href="#top">Top</a></span>
    <span style="float: right;">
        Next: <html:link page="/renderers/schemas.do">Appendix A: Schemas</html:link>
    </span>
</p>
