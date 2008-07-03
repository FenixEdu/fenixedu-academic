package net.sourceforge.fenixedu.domain.functionalities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.contents.ExplicitOrderNode;
import net.sourceforge.fenixedu.domain.contents.FunctionalityCall;
import net.sourceforge.fenixedu.domain.contents.Node;
import net.sourceforge.fenixedu.domain.functionalities.exceptions.IllegalOrderInModuleException;
import net.sourceforge.fenixedu.domain.functionalities.exceptions.MatchPathConflictException;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * The base class that represents a basic functionality available through the
 * application's interface.
 * 
 * @author cfgi
 */
public class Functionality extends Functionality_Base implements IFunctionality {

    /**
     * Comparator than can be used to order functionalities inside a module.
     * This comparator can be used to determine if a functinality should be
     * presented before other.
     * 
     * @author cfgi
     */
    public static class FunctionalityComparator implements Comparator<Functionality> {

	public int compare(Functionality f1, Functionality f2) {
	    Integer order1 = f1.getOrder();
	    Integer order2 = f2.getOrder();

	    return order1.compareTo(order2);
	}

    }

    /**
     * Generates an identifier that identifies a functionality in all systems
     * during the it's entire lifetime. The idea is that when functionalities
     * refer to each other during the exporting/importing operation we cannot
     * rely on database dependant information like the internal id of the
     * functionality. The UUID will be used instead and, since it's part of the
     * functionality, will be unique and unchanged in all systems unless
     * manually tampered with.
     * 
     * @return an UUID that identifies the new functionality in all systems
     */
    protected static UUID generateUuid() {
	return UUID.randomUUID();
    }

    /**
     * Required default constructor.
     */
    protected Functionality() {
	super();

	// setModule(Module.getRootModule());
    }

    public Functionality(MultiLanguageString name) {
	this();

	setName(name);
    }

    public Functionality(String uuid, MultiLanguageString name) {
	this();

	setContentId(uuid);
	setName(name);
    }

    @Override
    public void setDescription(MultiLanguageString description) {
	if (description == null || description.isEmpty()) {
	    super.setDescription(null);
	} else {
	    super.setDescription(description);
	}
    }

    @Override
    public void setExecutionPath(final String executionPathString) {
	invalidatePath();
	super.setExecutionPath(executionPathString);
	final ExecutionPath executionPath = getExecutionPathValue();
	if (executionPath != null) {
	    executionPath.delete();
	}
	if (executionPathString != null) {
	    new ExecutionPath(this, executionPathString);
	}
    }

    /**
     * The functionality, as a content, can have multiple parents but can have
     * only one module. This method finds and returns that module.
     * 
     * @return the module containing this functionality or <code>null</code>
     *         if this is a top level functionality
     */
    public Module getModule() {
	for (Node node : getParents()) {
	    Content content = node.getParent();
	    if (content instanceof Module) {
		return (Module) content;
	    }
	}
	return null;
    }

    public void setModule(Module module) {
	invalidatePath();
	Module oldModule = getModule();
	module.addChild(this);
	if (oldModule != null) {
	    oldModule.removeChild(this);
	}

	// Functionality.checkMatchPath();
    }

    /**
     * Checks that the public of this functionality does not conflict with the
     * public path of other functionalities.
     * 
     * @exception MatchPathConflictException
     *                    when the public path of this functionality conflicts
     *                    with the public path of another functionality, that
     *                    is, when there are two public paths that are equal
     */
    public static void checkMatchPath() {
	Set<String> paths = new HashSet<String>();

	for (Functionality functionality : Functionality.getFunctionalities()) {
	    String matchPath = functionality.getMatchPath();

	    if (matchPath == null) {
		continue;
	    }

	    if (!paths.add(matchPath)) {
		throw new MatchPathConflictException(matchPath);
	    }
	}
    }

    /**
     * The method follows the hierarchy of the functionalities to determine the
     * final public path through which the functionality is available. This
     * takes in account the module structure and if functionalities are relative
     * or not, that is, if the path is relative the the parent structure or not.
     * 
     * @return the path that should be publicly used to access the functionality
     *         or <code>null</code> if there is no path defined
     * 
     * @see #getModule()
     * @see #isRelative()
     */
    public String getPublicPath() {
	return getPath();
    }

    /**
     * The match path is the portion of the functionality
     * {@link #getPublicPath() public path} used to match agains the requested
     * path.
     * 
     * @return the path that should be used to match this functionality against
     *         the requested path or <code>null</code> if it does not have one
     */
    public String getMatchPath() {
	String path = getPublicPath();

	if (path == null) {
	    return null;
	}

	int queryStartIndex = path.lastIndexOf('?');
	if (queryStartIndex != -1) {
	    return path.substring(0, queryStartIndex);
	} else {
	    return path;
	}
    }

    /**
     * Obtains the order of this functionality inside the holding module. if
     * this functionality does not have an order specified then
     * {@link Integer#MAX_VALUE} is returned.
     * 
     * @return the order of the functionality
     */
    public int getOrder() {
	Integer order = getOrderInModule();

	if (order == null) {
	    return Integer.MAX_VALUE;
	} else {
	    return order;
	}
    }

    /**
     * Updates the order of this functionality and possibly reorders all the
     * other siblings.
     * 
     * @param order
     *                the new order of this functionality
     */
    public void setOrder(int order) {
	setOrderInModule(order);
    }

    /**
     * Specifies the order the functionality should have inside the module.
     * Normally you don't need to use this directly. You should use the
     * move(up|down|top|bottom|inner|outter) methods instead.
     * 
     * @exception IllegalOrderInModuleException
     *                    if <tt> order < 0</tt>
     * 
     * @see #moveUp()
     * @see #moveDown()
     * @see #moveTop()
     * @see #moveBottom()
     * @see #moveInner()
     * @see #moveOutter()
     */
    public void setOrderInModule(Integer order) {
	if (order != null && order < 0) {
	    throw new IllegalOrderInModuleException(null, this);
	}

	Module module = getModule();
	ExplicitOrderNode node = (ExplicitOrderNode) getParentNode(module);

	node.setNodeOrder(order);
    }

    public Integer getOrderInModule() {
	return ((ExplicitOrderNode) getParentNode(getModule())).getNodeOrder();
    }

    public void setParameters(String parameters) {
	for (FunctionalityParameter parameter : getParameters()) {
	    parameter.delete();
	}

	if (parameters != null && parameters.trim().length() > 0) {
	    String[] parts = parameters.split(",");
	    for (int i = 0; i < parts.length; i++) {
		String part = parts[i];
		new FunctionalityParameter(this, part.trim());
	    }
	}
    }

    public String getParametersString() {
	StringBuilder builder = new StringBuilder();

	for (String name : getParameterList()) {
	    if (builder.length() > 0) {
		builder.append(", ");
	    }

	    builder.append(name);
	}

	return builder.toString();
    }

    /**
     * Convenience method for accessing the functionality
     * {@link #getParameters parameters}.
     * 
     * @return a list with all the parameters
     */
    public List<String> getParameterList() {
	List<String> list = new ArrayList<String>();

	for (FunctionalityParameter parameter : getParameters()) {
	    list.add(parameter.getName());
	}

	return list;
    }

    /**
     * Convenience method that receives the parameters as a list an converts the
     * list to a string were parameters are separated by commas before setting
     * the parameters.
     * 
     * @param parametersList
     *                the list of parameters that will be set
     */
    public void setParameterList(List<String> parametersList) {
	StringBuilder parameters = new StringBuilder();

	for (String parameter : parametersList) {
	    if (parameters.length() > 0) {
		parameters.append(",");
	    }

	    parameters.append(parameter);
	}

	setParameters(parameters.toString());
    }

    /**
     * Determines if this functionality is parameterized, that is, if it expects
     * any parameters to be passed in order to work properly.
     * 
     * @return true if any parameter was specified for this action
     */
    public boolean isParameterized() {
	return hasAnyParameters();
    }

    /**
     * Moves this position up one place inside the holding module.
     */
    public void moveUp() {
	moveInsideModule(-1);
    }

    /**
     * Moves this position down one place inside the holding module.
     */
    public void moveDown() {
	moveInsideModule(1);
    }

    /**
     * Moves this position to the top place inside the holding module.
     */
    public void moveTop() {
	moveInsideModule(-Integer.MAX_VALUE);
    }

    /**
     * Moves this position to the bottom place inside the holding module.
     */
    public void moveBottom() {
	moveInsideModule(Integer.MAX_VALUE);
    }

    /**
     * Moves this functionality inside the holding module by the specified
     * ammount. Negatives values move the functionality up.
     * 
     * @param ammount
     */
    protected void moveInsideModule(int ammount) {
	Module module = getModule();

	int desiredOrder = getOrder() + ammount;

	List<Content> orderedFunctionalities;
	if (module == null) {
	    orderedFunctionalities = getOrderedTopLevelFunctionalities();
	} else {
	    orderedFunctionalities = module.getOrderedFunctionalities();
	}

	orderedFunctionalities.remove(this);

	if (desiredOrder < 0) {
	    orderedFunctionalities.add(0, this);
	} else if (desiredOrder > orderedFunctionalities.size()) {
	    orderedFunctionalities.add(orderedFunctionalities.size(), this);
	} else {
	    orderedFunctionalities.add(desiredOrder, this);
	}

	// int index = 0;
	// for (Content functionality : orderedFunctionalities) {
	// functionality.setOrderInModule(index++);
	// }
    }

    /**
     * Moves this functionality so that it becomes a sibling of the holding
     * module. If the holding module is a top level module this functionality
     * isn't moved.
     * 
     * @throws NullPointerException
     *                 when {@link #getModule()} is <code>null</code>
     */
    public void moveOutter() {
	Module module = getModule();

	if (module == null) {
	    return;
	}

	moveToModule(module.getParent(Module.class));
    }

    /**
     * Moves this functionality to the given module. If the module is
     * <code>null</code> this functionality becomes a top level functionality.
     * 
     * @param module
     *                the destination module
     */
    protected void moveToModule(Module module) {
	setModule(module);
    }

    /**
     * Moves this functionality so that it becomes a sibling of the nearest
     * sibling module. If there are no sibling modules then this functionality
     * isn't moved. If there are two modules next to the functionality the one
     * above (acording to the functionality order) is choosen.
     * 
     * @throws NullPointerException
     *                 when {@link #getModule()} is <code>null</code>
     */
    public void moveInner() {
	Collection<Node> nodes;

	Module module = getModule();
	if (module == null) {
	    nodes = Module.getRootModule().getChildren();
	} else {
	    nodes = module.getChildren();
	}

	int distance = Integer.MAX_VALUE;
	Module closest = null;

	for (Node sibling : sort(nodes)) {
	    if (sibling.getChild() == this) { // as a module, we obviously
		// don't count
		continue;
	    }

	    int moduleDistance = Math.abs(((ExplicitOrderNode) sibling).getNodeOrder() - getOrder());
	    if (moduleDistance < distance) {
		distance = moduleDistance;
		closest = (Module) sibling.getParent();
	    }
	}

	if (closest != null) {
	    moveToModule(closest);
	}
    }

    /**
     * Checks is this functionality is available in the given context, that is,
     * if the person hold in the context may start the usecase pointed by this
     * functionality.
     * 
     * <p>
     * A functionality is available when:
     * <ul>
     * <li>it is {@link #isEnabled() enabled};</li>
     * <li>all the parameters defined for the functionality are available in
     * the context;</li>
     * <li>all the parent modules are available;</li>
     * <li>the defined availability policy allows the given person to access
     * the functionality;</li>
     * </ul>
     * 
     * @param context
     *                the current functionality context
     * 
     * @return <code>true</code> if the functionality is available for the
     *         person hold in the context
     */
    public boolean isAvailable(FunctionalityContext context) {
	if (context == null) {
	    return true;
	}

	if (!hasRequiredParameters(context)) {
	    return false;
	}

	if (getModule() != null && !getModule().isAvailable(context)) {
	    return false;
	}

	return super.isAvailable(context);
    }

    /**
     * Checks if all the parameters required by the
     * {@link #getFunctionality() functionality} are present in the given
     * context. A parameter is present in the context if the current request
     * contains a <code>null</code> for that parameter.
     * 
     * <p>
     * If the functionality {@link Functionality#isParameterized()} is not
     * parameterized then, by definition, the context has all the required
     * parameters.
     * 
     * @param context
     *                the context used to check for parameter existance
     * @return <code>true</code> if the functionality is not parameterized or
     *         if the current request, available from the given context,
     *         contains all the required parameters
     */
    protected boolean hasRequiredParameters(FunctionalityContext context) {
	if (isParameterized()) {
	    for (String parameter : getParameterList()) {
		if (context.getRequest().getParameter(parameter) == null) {
		    return false;
		}
	    }
	}

	return true;
    }

    @Override
    protected void disconnect() {
	if (hasExecutionPathValue()) {
	    getExecutionPathValue().delete();
	}
	for (final FunctionalityParameter functionalityParameter : getParametersSet()) {
	    functionalityParameter.delete();
	}

	for (FunctionalityCall functionalityCall : getFunctionalityCallsSet()) {
	    functionalityCall.delete();
	}
	
	super.disconnect();
    }

    @Override
    public void delete() {
	Functionality.UUID_TABLE.remove(getContentId());
	super.delete();
    }

    //
    //
    //

    /**
     * Utility function that sorts a collection of functionalities according
     * with their order in the module and thei module hierarchy.
     * 
     * @param nodes
     *                the functionalities to srot
     * @return a list with all the functionalities sorted
     * 
     * @todo consider hierarchy
     */
    public static List<Node> sort(Collection<Node> nodes) {
	List<Node> sorted = new ArrayList<Node>(nodes);
	Collections.sort(sorted);

	return sorted;
    }

    /**
     * Obtains the set of all top-level functionalities, that is,
     * functionalities without a parent module.
     * 
     * @return all the top-level functionalities
     */
    public static List<Content> getTopLevelFunctionalities() {

	return Collections.singletonList((Content) Module.getRootModule());
    }

    /**
     * This function behaves like the
     * {@link #getTopLevelFunctionalities() previous one} but sorts the set of
     * functionalities.
     * 
     * @return an ordered list with all the top-level functionalities
     */
    public static List<Content> getOrderedTopLevelFunctionalities() {
	return (List<Content>) Module.getRootModule().getOrderedChildren(Content.class);
    }

    /**
     * Obtains the functionality with the given <tt>uuid</tt>.
     * 
     * @param uuid
     *                the <tt>uuid</tt> of the desired functionality
     * @return the functionality with the given <tt>uuid</tt> if it exists or
     *         <code>null</code> in all the other cases
     */
    public static Functionality getFunctionality(String uuid) {
	Functionality result = null;

	synchronized (UUID_TABLE) {
	    WeakReference<Functionality> reference = UUID_TABLE.get(uuid);
	    if (reference != null) {
		result = reference.get();
	    }

	    if (result == null) {
		for (Functionality functionality : Functionality.getFunctionalities()) {
		    UUID_TABLE.put(functionality.getContentId(), new WeakReference<Functionality>(functionality));

		    if (result == null && functionality.getContentId().equals(uuid)) {
			result = functionality;
		    }
		}
	    }
	}

	return result;
    }

    private static Map<String, WeakReference<Functionality>> UUID_TABLE = new WeakHashMap<String, WeakReference<Functionality>>();

    public boolean matchesFunctionality(String path) {
	String matchPath = getMatchPath();
	return matchPath != null && matchPath.equals(path);
    }

    public static Collection<Functionality> getFunctionalities() {
	List<Functionality> functionalities = new ArrayList<Functionality>();
	Queue<Module> modules = new LinkedList<Module>();

	modules.add(Module.getRootModule());

	while (!modules.isEmpty()) {
	    Module currentModule = modules.poll();

	    for (Node node : currentModule.getChildren()) {
		Content content = (Content) node.getChild();
		if (content instanceof Functionality) {
		    functionalities.add((Functionality) content);
		} else if (content instanceof Module) {
		    modules.add((Module) content);
		}
	    }
	}

	return functionalities;
    }

    private transient String path = null;

    public void invalidatePath() {
	path = null;
    }

    @Override
    public String getPath() {
	if (path == null) {
	    final StringBuilder stringBuilder = new StringBuilder();
	    getModule().appendPath(stringBuilder);
	    final String executionPath = getExecutionPath();
	    if (stringBuilder.charAt(stringBuilder.length() - 1) != '/' && executionPath.charAt(0) != '/') {
		stringBuilder.append('/');
	    }
	    stringBuilder.append(executionPath);
	    path = stringBuilder.toString();
	}
	return path;
    }

    //
    // FunctionalityContext
    //

    private static ThreadLocal<FunctionalityContext> CURRENT_CONTEXT = new ThreadLocal<FunctionalityContext>();

    public static void setCurrentContext(FunctionalityContext context) {
	Functionality.CURRENT_CONTEXT.set(context);
    }

    public static FunctionalityContext getCurrentContext() {
	return Functionality.CURRENT_CONTEXT.get();
    }

    public static Functionality findByExecutionPath(final String executionPathValue) {
	int endIndex = executionPathValue.indexOf('?');
	int firstIndex = endIndex > 0 ? executionPathValue.substring(0, endIndex).lastIndexOf("/") : executionPathValue
		.lastIndexOf("/");

	if (endIndex > firstIndex) {
	    String firstLookupPath = executionPathValue.substring(firstIndex > 0 ? firstIndex : 0, endIndex > 0 ? endIndex
		    : executionPathValue.length());
	    for (final ExecutionPath executionPath : RootDomainObject.getInstance().getExecutionPathsSet()) {
		if (executionPath.getExecutionPath().contains(firstLookupPath)) {
		    if (executionPathValue.startsWith(executionPath.getFunctionality().getPath())) {
			return executionPath.getFunctionality();
		    }
		}
	    }
	}
	return null;
    }

}
