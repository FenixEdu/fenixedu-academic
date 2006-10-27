package net.sourceforge.fenixedu.domain.functionalities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.functionalities.exceptions.IllegalOrderInModuleException;
import net.sourceforge.fenixedu.domain.functionalities.exceptions.MatchPathConflictException;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * The base class that represents a basic functionality available through the
 * application's interface.
 * 
 * @author cfgi
 */
public abstract class Functionality extends Functionality_Base {

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

        setRootDomainObject(RootDomainObject.getInstance());
        setRelative(true);
        setPrincipal(true);

        // TODO: check if we can make a CommitListener to do this
        // only when the functionality is not inside a module
        setOrderInModule(getNextTopLevelOrder());
    }

    @Override
    public void setUuid(UUID uuid) {
        // do not allow changes to the uuid
        // TODO: should an exception be thrown?
    }

    protected final void changeUuid(UUID uuid) {
        super.setUuid(uuid);
    }
    
    /**
     * Creates a new functionality ensuring that the name is not empty.
     * 
     * @see #setName()
     */
    public Functionality(MultiLanguageString name) {
        this();
    }

    @Override
    public void setDescription(MultiLanguageString description) {
        if (description == null || description.isEmpty()) {
            super.setDescription(null);
        } else {
            super.setDescription(description);
        }
    }

    /**
     * @return the {@link #getPath() current path} but ensuring that starts but
     *         does not end with "/"
     */
    protected String getNormalizedPath() {
        String path = getPath();

        if (path == null) {
            return null;
        }

        int end = path.endsWith("/") ? path.length() - 1 : path.length();
        return (path.startsWith("/") ? "" : "/") + path.substring(0, end);
    }

    /**
     * @see #isRelative()
     */
    @Override
    @Deprecated
    public Boolean getRelative() {
        return super.getRelative();
    }

    /**
     * Indicates if this functionality is relative to the parent module. If the
     * functionality is relative then it's {@link #getPublicPath() public path}
     * will be prefixed by the module's public path. If the functionality is not
     * relative then the public path is the same as the path.
     * 
     * @return <code>true</code> if this functionality has a path relative to
     *         the parent module
     * 
     * @see #getPath()
     * @see #getPublicPath()
     */
    public Boolean isRelative() {
        return super.getRelative();
    }

    @Override
    public void setPath(String path) {
        super.setPath(path);

        Functionality.checkMatchPath();
    }

    @Override
    public void setRelative(Boolean relative) {
        super.setRelative(relative);

        Functionality.checkMatchPath();
    }

    @Override
    public void setModule(Module module) {
        super.setModule(module);
        
        Functionality.checkMatchPath();
    }

    /**
     * Checks that the public of this functionality does not conflict with the
     * public path of other functionalities.
     * 
     * @exception MatchPathConflictException
     *                when the public path of this functionality conflicts with
     *                the public path of another functionality, that is, when
     *                there are two public paths that are equal
     */
    public static void checkMatchPath() {
        Set<String> paths = new HashSet<String>();

        for (Functionality functionality : RootDomainObject.getInstance().getFunctionalities()) {
            if (!functionality.isPrincipal()) {
                continue;
            }

            String matchPath = functionality.getMatchPath();

            if (matchPath == null) {
                continue;
            }

            if (!paths.add(matchPath)) {
                throw new MatchPathConflictException(matchPath);
            }
        }
    }

    @Override
    public void setPrincipal(Boolean principal) {
        super.setPrincipal(principal);
        Functionality.checkMatchPath();
    }

    public Boolean isPrincipal() {
        Boolean principal = getPrincipal();
        return principal == null ? true : principal;
    }

    public void setPathAndPrincipal(String path, Boolean principal) {
        super.setPrincipal(principal);
        setPath(path);
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
        String path = getPath();
        if (path == null || path.trim().length() == 0) {
            return null;
        }

        if (!isRelative()) {
            return getNormalizedPath();
        } else {
            if (!hasModule()) {
                return getNormalizedPath();
            } else {
                return getModule().getPublicPrefix() + getNormalizedPath();
            }
        }
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
     * Indicates if this a bottom functionality, that is, that it's the final
     * step to start a use case.
     * 
     * @return true if this funnctionality does not contain others and is a link
     *         to a use case
     */
    public boolean isConcrete() {
        return false;
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
     *            the new order of this functionality
     */
    public void setOrder(int order) {
        setOrderInModule(order);
        moveInsideModule(0); // force update of the other sibling's order
    }

    /**
     * Specifies the order the functionality should have inside the module.
     * Normally you don't need to use this directly. You should use the
     * move(up|down|top|bottom|inner|outter) methods instead.
     * 
     * @exception IllegalOrderInModuleException
     *                if <tt> order < 0</tt>
     * 
     * @see #moveUp()
     * @see #moveDown()
     * @see #moveTop()
     * @see #moveBottom()
     * @see #moveInner()
     * @see #moveOutter()
     */
    @Override
    public void setOrderInModule(Integer order) {
        if (order != null && order < 0) {
            throw new IllegalOrderInModuleException(null, this);
        }

        super.setOrderInModule(order);
    }

    @Override
    public void setParameters(String parameters) {
        if (parameters == null || parameters.trim().length() == 0) {
            super.setParameters(null);
        } else {
            StringBuilder normalized = new StringBuilder();

            String[] parts = parameters.split(",");
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];

                if (i > 0) {
                    normalized.append(",");
                }

                normalized.append(part.trim());
            }

            super.setParameters(normalized.toString());
        }
    }

    /**
     * Convenience method for accessing the functionality
     * {@link #getParameters parameters}.
     * 
     * @return a list with all the parameters
     */
    public List<String> getParameterList() {
        List<String> list = new ArrayList<String>();

        if (getParameters() == null) {
            return list;
        } else {
            String[] parameterParts = getParameters().split(",");
            for (String parameter : parameterParts) {
                list.add(parameter);
            }
        }

        return list;
    }

    /**
     * Convenience method that receives the parameters as a list an converts the
     * list to a string were parameters are separated by commas before setting
     * the parameters.
     * 
     * @param parametersList
     *            the list of parameters that will be set
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
        return getParameters() != null && getParameters().length() > 0;
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

        List<Functionality> orderedFunctionalities;
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

        int index = 0;
        for (Functionality functionality : orderedFunctionalities) {
            functionality.setOrderInModule(index++);
        }
    }

    /**
     * Moves this functionality so that it becomes a sibling of the holding
     * module. If the holding module is a top level module this functionality
     * isn't moved.
     * 
     * @throws NullPointerException
     *             when {@link #getModule()} is <code>null</code>
     */
    public void moveOutter() {
        Module module = getModule();

        if (module == null) {
            return;
        }

        moveToModule(module.getParent());
    }

    /**
     * Moves this functionality to the given module. If the module is
     * <code>null</code> this functionality becomes a top level functionality.
     * 
     * @param module
     *            the destination module
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
     *             when {@link #getModule()} is <code>null</code>
     */
    public void moveInner() {
        List<Module> modules;

        Module module = getModule();
        if (module == null) {
            modules = Module.getTopLevelModules();
        } else {
            modules = module.getModules();
        }

        int distance = Integer.MAX_VALUE;
        Module closest = null;

        for (Module sibling : sort(modules)) {
            if (sibling == this) { // as a module, we obviously don't count
                continue;
            }

            int moduleDistance = Math.abs(sibling.getOrder() - getOrder());
            if (moduleDistance < distance) {
                distance = moduleDistance;
                closest = sibling;
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
     *            the current functionality context
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
     *            the context used to check for parameter existance
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
        super.disconnect();
        
        removeRootDomainObject();
    }

    @Override
    protected void deleteSelf() {
        super.deleteSelf();

        // remove from cached table
        Functionality.UUID_TABLE.remove(getUuid());
    }
    
    //
    //
    //

    private static int getNextTopLevelOrder() {
        int max = -1;

        for (Functionality functionality : getTopLevelFunctionalities()) {
            if (functionality.getOrderInModule() != null) {
                max = Math.max(max, functionality.getOrderInModule());
            }
        }

        return max + 1;
    }

    
    
    /**
     * Utility function that sorts a collection of functionalities according
     * with their order in the module and thei module hierarchy.
     * 
     * @param functionalities
     *            the functionalities to srot
     * @return a list with all the functionalities sorted
     * 
     * @todo consider hierarchy
     */
    public static <T extends Functionality> List<T> sort(Collection<T> functionalities) {
        List<T> sorted = new ArrayList<T>(functionalities);
        Collections.sort(sorted, new Module.FunctionalityComparator());

        return sorted;
    }

    /**
     * Obtains the set of all top-level functionalities, that is,
     * functionalities without a parent module.
     * 
     * @return all the top-level functionalities
     */
    public static List<Functionality> getTopLevelFunctionalities() {
        List<Functionality> functionalities = new ArrayList<Functionality>();

        for (Functionality functionality : RootDomainObject.getInstance().getFunctionalities()) {
            if (functionality.getModule() == null) {
                functionalities.add(functionality);
            }
        }

        return functionalities;
    }

    /**
     * This function behaves like the
     * {@link #getTopLevelFunctionalities() previous one} but sorts the set of
     * functionalities.
     * 
     * @return an ordered list with all the top-level functionalities
     */
    public static List<Functionality> getOrderedTopLevelFunctionalities() {
        return sort(getTopLevelFunctionalities());
    }

    /**
     * Obtains the functionality with the given <tt>uuid</tt>.
     * 
     * @param uuid
     *            the <tt>uuid</tt> of the desired functionality
     * @return the functionality with the given <tt>uuid</tt> if it exists or
     *         <code>null</code> in all the other cases
     */
    public static Functionality getFunctionality(UUID uuid) {
        Functionality result = null;

        synchronized (UUID_TABLE) {
            WeakReference<Functionality> reference = UUID_TABLE.get(uuid);
            if (reference != null) {
                result = reference.get();
            }
            
            if (result == null) {
                for (Functionality functionality : RootDomainObject.getInstance().getFunctionalities()) {
                    UUID_TABLE.put(functionality.getUuid(), new WeakReference<Functionality>(functionality));
                    
                    if (result == null && functionality.getUuid().equals(uuid)) {
                        result = functionality;
                    }
                }
            }
        }
        
        return result;
    }

    private static Map<UUID, WeakReference<Functionality>> UUID_TABLE = new WeakHashMap<UUID, WeakReference<Functionality>>();
}
