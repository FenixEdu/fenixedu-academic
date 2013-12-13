<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<!-- Title and TOC -->
<a name="top"></a>
<div style="margin-bottom: 50px">
    <h2>The third situation: renderers meet actions</h2>
    
    <ul>
        <li><a href="#wrapper">How do I collect random pieces of information?</a></li>
        <li><a href="#example">Can you give an example?</a></li>
        <li><a href="#nested">I need to control the form</a></li>
        <li><a href="#controllers">Intercepting the submission and changing the destination</a></li>
        <li><a href="#attributes">Controllers and renderer's can save it's own attributes in the <code>ViewState</code></a></li>
        <li><a href="#components">Creating components directly in the actions</a></li>
        <li><a href="#viewstate">Specialized ViewState</a></li>
        <li><a href="#metaobject">Creating a MetaObject as an abstraction of the real object</a></li>
        <li><a href="#binding">Binding slots to components</a></li>
        <li><a href="#bean">Do we really nead a bean?</a></li>
        <li><a href="#controllers-custom">Converters and controllers in custom components</a></li>
    </ul>
</div>

<a name="wrapper"></a>
<h3>How do I collect random pieces of information?</h3>

<p>
   When you use the <code>edit</code> tag you are always editing an object.
   So you must wrap all the pieces of information in a bean and then edit
   that bean.
</p>

<a name="example"></a>
<h3>Can you give an example?</h3>

<p>
    Suppose you, for some strange reason, need the user to introduce two ages,
    a date, and a gender to search for persons. You could create the following bean:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>public class SearchBean implements Serializable {
    private int minAge;
    private int maxAge;
    private Date date;
    private Gender gender;

    public int getMinAge() {
        return this.minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}</pre>
</div>

<p>
    And in some action you create the bean and put it in a request attribute.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>SearchBean bean = new SearchBean();
bean.setDate(new Date());

request.setAttribute("bean", bean);</pre>
</div>

<p>
    But how do we send the input to the action that will search the persons? For that
    you use the <code>action</code> attribute of the <code>edit</code> tag. This attribute
    makes the form submit the content to the specified action. By default the form is
    submited to the input location, that is, the url originally requested.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>&lt;fr:edit name=&quot;bean&quot; action=&quot;/renderers/searchPersons.do?method=search&quot;/&gt;</pre>
</div>

<p>
    Now we need to know how, in the target action, we get access to the bean. Between
    interactions with the user the view state is mantained. Is through that view state
    the we can get the object that was edited. So in the action we would do something like:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>ViewState viewState = (ViewState) RenderUtils.getViewState();
SearchBean bean = (SearchBean) viewState.getMetaObject().getObject();

// search person
(...)</pre>
</div>

<p>
    All we need now is the <html:link page="/renderers/searchPersons.do?method=prepare">working example</html:link>.
    What I mean by "working" is that the pieces of code presented are executed and not that it will search
    persons.
</p>

<a name="nested"></a>
<h3>I need to control the form</h3>

<p>
    So you need to pass some extra hidden fields to the action, use some properties from 
    a Struts action form, or even put the form submission buttons in other place and with
    other names? Can you still do this?
</p>

<p>
    Yes!
</p>

<p>
    What you need to do is surround the renderer tag with an <code>html:form</code> tag. When you do
    this no form will be generated by the renderer. This means that the Submit and Cancel buttons
    you are used to see next to the renderer input presentation will not be present and you
    have to manually add new buttons. This gives you more control about the form and, in fact, is 
    the only way of changing the name of the submit button for just one page and to introduce
    hidden fields for actions.
</p>

<p>
    If you don't use the <code>html:form</code> but use a plain form instead then you have to manually
    indicate that the renderization is nested in another form. For this you can use the <code>nested</code>
    attribute of the <code>edit</code> or <code>create</code> tag. If you specify <code>nested=true</code> 
    then not form will be generating automatically no mather were the <code>fr:edit</code> or <code>fr:create</code>
    is beeing used.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>&lt;html:form action=&quot;/renderers/searchPersons.do?method=search&quot;&gt;
    &lt;html:hidden property=&quot;theProperty&quot; value=&quot;theValue&quot;/&gt;
    
    &lt;fr:edit name=&quot;bean&quot; action=&quot;/renderers/searchPersons.do?method=search&quot;/&gt;
    &lt;html:submit value=&quot;Save&quot;/&gt;
    &lt;html:cancel value=&quot;Forget&quot;/&gt;
&lt;/html:form&gt;</pre>
</div>

<p>
    The outter form now defines the target action but if some controler chooses to send the flow to other destination
    they will still override the destination specified in the form. The behaviour of the "Cancel" button that is
    normally generated when the <code>fr:edit</code> and <code>fr:create</code> are used can be reintroduced with
    the <code>html:cancel</code> tag. Renderers recognize Struts semantic for that button and also cancel the 
    viewstate processing.
</p>

<a name="controllers"></a>
<h3>Intercepting the submission and changing the destination</h3>

<p>
    Now imagine that you want to do something to the submited data before the action is executed or even choose
    the destination according to the user input. How can you do that? For this type of control you need to
    use controllers.
</p>

<p>
    Controllers can be associated to form components and are executed just after the components are updated
    with the submited values. Each controller has access to the corresponding controlled component and to
    the view state. This way they can influence the global structure or aspect of what is presented, control
    the lifecycle of the components, redirect to another location, etc.
</p>

<p>
    Controllers can only be associated to components by renderers and not from the configuration like 
    validators. So if you need to include controllers you probably need to create a new renderer. Here is an
    example of a controller that redirects to different locations according to gender:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>class ChooseFromGenderController extends HtmlController {

    @Override
    public void execute(IViewState viewState) {
        HtmlMenu genderMenu = (HtmlMenu) getControlledComponent();
        
        Gender gender = (Gender) genderMenu.getConvertedValue(Gender.class);
        if (Gender.MALE.equals(gender)) {
            viewState.setCurrentDestination("male");
        }
        else {
            viewState.setCurrentDestination("female");
        }
    }
    
}</pre>
</div>

<p>
    Now there is something there that needs explaining. Were do we define those names in <code>setCurrentDestination</code>?
    The <code>edit</code> tag supports an inner tag named <code>destination</code> that allows you to define destinations,
    or exit point, from the generated form. So the use of the <code>edit</code> tag would be a little different:
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>&lt;fr:edit name=&quot;bean&quot; action=&quot;/renderers/searchPersons.do?method=search&quot;&gt;
    &lt;fr:destination name=&quot;female&quot; path=&quot;/renderers/searchFemales.&quot;/&gt;
    &lt;fr:destination name=&quot;male&quot; path=&quot;/renderers/searchMales.do&quot;/&gt;
&lt;/fr:edit&gt;</pre>
</div>

<p>
    There is also a default behaviour that is important to know. By default there are three destination names
    that are used if provided:
</p>

<dl>
  <dt><code>"success"</code></dt><dd>is used if the submission is valid and has no errors</dd>
  <dt><code>"invalid"</code></dt><dd>is used when some validation fails</dd>
  <dt><code>"cancel"</code></dt><dd>is used when the cancel button is pressed</dd>
</dl>

<p>
    The next example shows a controller that capitalizes the text of a field. This controller needs to
    be associated to a submit button to be executed properlly. The behaviour inherited from 
    <code>HtmlSubmitButtonController</code> makes this controller be executed only when the button is
    pressed and changes the submission lifecycle to not alter the object.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>class CapitalizeController extends HtmlSubmitButtonController {

    private HtmlSimpleValueComponent input;
    
    public CapitalizeController(HtmlSimpleValueComponent component) {
        this.input = component;
    }

    @Override
    protected void buttonPressed(IViewState viewState, HtmlSubmitButton button) {
        String text = this.input.getValue();
        this.input.setValue(capitalize(text));
    }

    private String capitalize(String text) {
        StringBuilder buffer = new StringBuilder();
        char ch, prevCh;
        
        prevCh = ' ';
        for (int i = 0;  i &lt; text.length();  i++) {
           ch = text.charAt(i);
           
           if (Character.isLetter(ch)  &amp;&amp;  !Character.isLetter(prevCh)) {
               buffer.append(Character.toUpperCase(ch));
           }
           else {
               buffer.append(ch);
           }
           
           prevCh = ch;
        }
        
        return buffer.toString();
    }
    
}</pre>
</div>

<a name="attributes"></a>
<h3>Controllers and renderer's can save it's own attributes in the <code>ViewState</code></h3>

<p>
    Sometimes for a more complex implementation of a controller or renderer you need to store some
    values between requests of the user. The <code>ViewState</code> object allows you to set attributes
    that are persisted (in the client side) between requests. The view state attributes are global,
    that is, if you set the attribute <code>"a"</code> in one renderer then all renderers and controllers
    that are executed next will see that attribute. Nevetheless the default input context implementation
    tries make attributes local to each renderer. The objective is to avoid name conflicts and make 
    the implementation easier.
</p>

<p>
    The next example shows an additional controllers that change the value of a field to it's uppercase. 
    This controller is defined as a inner class of a renderer so it has access to the input context and
    consequently to the view state. It uses this fact to maintain the controller in the right state
    even after the values are submited and the object is changed. This controller also interacts with
    the previous controller to make the capitalize button visible only when the value is in lower case.
</p>

<div style="border: 1px solid #000; padding: 20px 20px 20px 20px">
    <pre>class CaseChangeController extends HtmlSubmitButtonController {

    private HtmlSubmitButton button;
    private HtmlSubmitButton capitalize;
    private HtmlSimpleValueComponent input;
    
    public CaseChangeController(HtmlSimpleValueComponent component, HtmlSubmitButton button, HtmlSubmitButton capitalizeButton) {
        this.input = component;
        this.button = button;
        this.capitalize = capitalizeButton;
        
        setupButtons();
    }

    private void setupButtons() {
        this.button.setText(isUpperCase() ? "To Upper Case" : "To Lower Case");
        this.capitalize.setVisible(isUpperCase());
    }
    
    private boolean isUpperCase() {
        if (getInputContext().getViewState().getAttribute("isUpperCase") == null) {
            return true;
        }
        
        return ((Boolean) getInputContext().getViewState().getAttribute("isUpperCase")).booleanValue();
    }
    
    private void setUpperCase(boolean isUpperCase) {
        getInputContext().getViewState().setAttribute("isUpperCase", new Boolean(isUpperCase));
    }
    
    @Override
    protected void buttonPressed(IViewState viewState, HtmlSubmitButton button) {
        String text = this.input.getValue();
        this.input.setValue(isUpperCase() ? text.toUpperCase() : text.toLowerCase());
        
        setUpperCase(!isUpperCase());
        setupButtons();
    }
}</pre>
</div>

<p>
    To use this two new controllers we need to create a custom renderer but that will only be shown latter. 
    Right now all we need to know is that a new layout was defined and associated with the implemented
    renderer. Now lets see the working example. 
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Schema -->
    <div>
        <p><strong>Schema</strong></p>
        <pre>&lt;schema name=&quot;person.create-minimal-defaults&quot; type=&quot;net.sourceforge.fenixedu.domain.Person&quot;&gt;
    &lt;slot name=&quot;name&quot; layout=&quot;allow-case-change&quot;/&gt;
&lt;/schema&gt;</pre>
    </div>

    <!-- Code -->
    <div>
        <p><strong>Code</strong></p>
        <pre>&lt;fr:edit id=&quot;case-change&quot; name=&quot;UserView&quot; property=&quot;person&quot; layout=&quot;tabular&quot; schema=&quot;person.name&quot;/&gt;</pre>
    </div>

    <!-- Result -->
    <div>
        <p><strong>Result</strong></p>
        <div style="border: 1px solid #000; padding: 20px 20px 20px 20px" >
             <fr:edit id="case-change" name="USER_SESSION_ATTRIBUTE" property="user.person" layout="tabular" schema="person.name"/>
        </div>
    </div>
</div>

<a name="components"></a>
<h3>Creating components directly in the actions</h3>

<p>
    There is another form of collection random pieces of data from the user but it probably still requires a bean to hold those
    random pieces. Nevertheless this new aproach allows you to have more control of some of the presentation aspects behind the
    renderers framework.
</p>

<p>
    The idea is to allow components to be created directly from the action. This way, in the action, we can create custom components,
    compose them at will, and use them directly in the JSP to form the presentation. This use of components is always composed of 
    three steps:
</p>

<ol>
  <li>Creating a <code>ViewState</code></li>
  <li>Obtaining a schema</li>
  <li>Wrapping the bean or holder of the information in a <code>MetaObject</code></li>
  <li>Creating components and compose them</li>
  <li>Binding input components to specific slots</li>
  <li>Setup controllers and converters</li>
  <li>Make the <code>ViewState</code> available in the request</li>
</ol>

<p>
    Let't watch a simple but complete example and then explain the details:
</p>

<div style="border-left: 1px solid #AAAAAA; padding-left: 10px;">
    <!-- Code -->
    <div>
        <p><strong>Code in action</strong></p>
        <pre>// 1
ActionViewState viewState = new ActionViewState(&quot;testing&quot;);

// 2
Schema schema = RenderKit.getInstance().findSchema(&quot;schema&quot;);

// 3
MetaObject metaObject = MetaObjectFactory.createObject(bean, schema);
viewState.setMetaObject(metaObject);

// 4
final HtmlTextInput input = new HtmlTextInput();

// 5
String aSlotName = schema.getSlotDescriptions().get(0).getSlotName();
input.bind(metaObject.getSlot(aSlotName));

// 6
input.setController(new HtmlController() {

    @Override
    public void execute(IViewState viewState) {
        System.out.println(&quot;changing the text to lowercase&quot;);
        
        if (input.getValue() != null) {
            input.setValue(input.getValue().toLowerCase());
        }
    }
    
});

input.setConverter(new Converter() {

    @Override
    public List&lt;String&gt; convert(Class type, Object value) {
        if (value == null) {
            return null;
        }
        else {
            return Arrays.asList(((String) value).split(&quot;\\p{Space}+&quot;));
        }
    }
    
});

// 7
request.setAttribute(&quot;customNamesInput&quot;, viewState);</pre>
    </div>
    
    <!-- Code -->
    <div>
        <p><strong>Code in JSP</strong></p>
        <pre>&lt;fr:viewstate name="customNamesInput" action="/example.do?method=collect"/&gt;</pre>
    </div>
    
    <!-- Code -->
    <div>
        <p><strong>Code in Action (handle submit)</strong></p>
        <pre>ViewState viewState = RenderUtils.getViewState(&quot;testing&quot;);
HtmlTextInput component = (HtmlTextInput) viewState.getComponent();
UsedBean bean = (UsedBean) viewState.getMetaObject().getObject();

List&lt;String&gt; names1 = (List&lt;String&lt;) component.getConvertedValue();
List&lt;String&gt; names2 = bean.getNamesList();</pre>
    </div>
</div>

<a name="viewstate"></a>
<h3>Specialized ViewState</h3>

<p>
    As you may notice we started by creating a specific <code>ViewState</code>. The <code>ActionViewState</code>
    is a specialization of the <code>ViewState</code> commonly available, that can be used directly by actions
    and that skips some steps in the renderers lifecycle.
</p>

<p>
    This <code>ViewState</code> has the same role as the other automatically created by the <code>fr:edit</code> 
    and <code>fr:create</code> tags. The essential about the <code>ViewState</code> is that it must contain a
    component. Optionally it can contain a meta object if some component is bound to a slot, that is, the 
    <code>ViewState</code> must contain the meta object containing the slots to wich component were bound.
</p>

<p>
    There aren't other requirements over the <code>ViewState</code>. After setting it up you just need to make it
    available to a JSP, usually by setting a request attribute.
</p>

<a name="metaobject"></a>
<h3>Creating a MetaObject as an abstraction of the real object</h3>

<p>
    When you have an object and want to associate some input components to slots of that object then you need to
    create an abstraction of that object called the meta object. This meta object it the one used by the renderers
    framework and is usefull to help serializing objects and prevent changes to object until they really need to be made.
</p>

<p>
    To create a meta object you normally should obtain a schema. The schema limits the slots that the meta object
    will contain but you don't really need the schema. If you pass <code>null</code> as a schema then all the
    object's slots are available in the meta object. Schemas can be obtained through the <code>RenderKit</code> object.
    Schemas are found by their name in the configuration.
</p>

<p>
    After you created the meta object don't forget to set the <code>ViewState</code> with it.
</p>

<a name="binding"></a>
<h3>Binding slots to components</h3>

<p>
    When you want to associate some input component's value to a slot as it normally happens in the <code>fr:edit</code>
    and <code>fr:create</code> tags you just need to select the desired meta slot from the create meta object and
    bind the input component to it. After doing this, the final value of the component, after the user submited the form,
    will be set in the object.
</p>

<p>
    Off course, you can only by one input component to a meta slot. You can bind several input component to the same slot
    but all well get is having the setter being called several times.
</p>

<a name="bean"></a>
<h3>Do we really nead a bean?</h3>

<p>
    As you may have noticed, you don't really need the bean in the example. If you don't bind any input component to a slot 
    then you don't need to create the meta object and so you won't need the object/bean. Nevertheless you will have to 
    obtain the value directly from the input component and probably have to take measures to make them easy to find like
    giving them specific ids.
</p>

<p>
    You may need a bean when you want some logic associated with the retrievel or processing of the information or just to 
    concentrate all the information in an object easy to pass to other parts of the program but it's never required.
</p>

<a name="controllers-custom"></a>
<h3>Converters and controllers in custom components</h3>

<p>
    The converter and controller you see in the example are just ther to make a point, they don't really make anything 
    usefull. But as you can see you can associate any piece of code that is executed afer the input of the user is 
    processed. This can be used, for example, to make a preprocessing of values or to copy some intermediate values to 
    a final location. Controllers can also change the lifecycle through the <code>ViewState</code> provided so they can
    be used for a lot of purposes.
</p>

<p>
    As mentioned a typical use is to use the values of several fields and put a computed value in a final field that
    normally has converter associated. For example you can provide 3 input fields and grab those three values to make
    a parseable time that you put in a 4th field.
</p>

<p>
    Convertes are used because all the values of the components are strings or arrays of strings. The converter allows
    to request the final value for the slot having the conversion been specified beforehand. The converter is specially
    usefull when an input component is bound to a slot. You need to add a converter whenever the value of the component
    cannot be trivially converter into the slot's type.
</p>

<p style="margin-top: 50px; padding-top: 10px; border-top: 1px solid #AAAAAA">
    <span style="float: left;"><a href="#top">Top</a></span>
    <span style="float: right;">
        Next: <html:link page="/renderers/new.do">The fourth situation: a new renderer</html:link>
    </span>
</p>

