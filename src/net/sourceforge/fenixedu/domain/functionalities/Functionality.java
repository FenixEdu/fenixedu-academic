package net.sourceforge.fenixedu.domain.functionalities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.functionalities.MoveFunctionality.Movement;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FieldIsRequiredException;
import net.sourceforge.fenixedu.domain.functionalities.exceptions.IllegalOrderInModuleException;
import net.sourceforge.fenixedu.domain.functionalities.exceptions.PublicPathConflictException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;
import pt.ist.utl.fenix.utils.Pair;

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
     * Required default constructor.
     */
    protected Functionality() {
        super();

        setOjbConcreteClass(this.getClass().getName());

        setRootDomainObject(RootDomainObject.getInstance());
        setRelative(true);
        setEnabled(true);

        // TODO: check if we can make a CommitListener to do this
        // only when the functionality is not inside a module
        setOrderInModule(getNextTopLevelOrder());
    }

    /**
     * Creates a new functionality ensuring that the name is not empty.
     * 
     * @see #setName()
     */
    public Functionality(MultiLanguageString name) {
        this();
    }

    /**
     * Changes the visible internationalizable name of the functionality. The
     * name must exist, that is, it may not be <code>null</code> or an empty
     * multilanguage string.
     * 
     * @see MultiLanguageString#isEmpty()
     */
    @Override
    public void setName(MultiLanguageString name) {
        if (name == null || name.isEmpty()) {
            throw new FieldIsRequiredException("name", "functionalities.functionality.required.name");
        }

        super.setName(name);
    }

    @Override
    public void setPath(String path) {
        super.setPath(path);
        
        Functionality.checkPublicPath();
    }

    @Override
    public void setRelative(Boolean relative) {
        super.setRelative(relative);

        Functionality.checkPublicPath();
    }

    /**
     * Checks that the public of this functionality does not conflict with the
     * public path of other functionalities.
     * 
     * @exception PublicPathConflictException
     *                when the public path of this functionality conflicts with
     *                the public path of another functionality, that is, when
     *                there are two public paths that are equal
     */
    public static void checkPublicPath() {
        Set<String> paths = new HashSet<String>();

        for (Functionality functionality : RootDomainObject.getInstance().getFunctionalities()) {
            String publicPath = functionality.getPublicPath();
            
            if (publicPath == null) {
                continue;
            }

            if (! paths.add(publicPath)) {
                throw new PublicPathConflictException(publicPath);
            }
        }
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
    public void setTitle(MultiLanguageString title) {
        if (title == null || title.isEmpty()) {
            super.setTitle(null);
        } else {
            super.setTitle(title);
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
        if (getPath() == null || getPath().trim().length() == 0) {
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
     * @return the path that should be used to match this functionality agains
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
     * Changes the availability policy. The previous policy is deleted.
     */
    @Override
    public void setAvailabilityPolicy(AvailabilityPolicy availabilityPolicy) {
        if (getAvailabilityPolicy() != null && getAvailabilityPolicy() != availabilityPolicy) {
            getAvailabilityPolicy().delete();
        }

        super.setAvailabilityPolicy(availabilityPolicy);
    }

    /**
     * @see #isEnabled()
     */
    @Override
    @Deprecated
    public Boolean getEnabled() {
        return super.getEnabled();
    }

    /**
     * Indicates if a functionality is generally available for use. A disabled
     * functionality is never available despite what the current availability
     * policy is. When a functionality is enabled then it is availability is
     * defined by the current availability policy.
     * 
     * @return <code>true</code> if this functionality is available for
     *         general use
     */
    public Boolean isEnabled() {
        return super.getEnabled();
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
     * Checks is this functionality is available for the given person and
     * context, that is, if the person may start the usecase pointed by this
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
     * @param person
     *            the person accessing the functionality or <code>null</code>
     *            if it's a public requester
     * 
     * @return <code>true</code> if the functionality is available for the
     *         given person
     */
    public boolean isAvailable(FunctionalityContext context, Person person) {
        if (context == null) {
            return true;
        }

        if (!isEnabled()) {
            return false;
        }

        if (!hasRequiredParameters(context)) {
            return false;
        }

        if (getModule() != null && !getModule().isAvailable(context, person)) {
            return false;
        }

        if (getAvailabilityPolicy() == null) {
            return true;
        }

        return getAvailabilityPolicy().isAvailable(context, person);
    }

    /**
     * Checks is this functionality is visible for the given person and context.
     * This method may be used to decide if a certain functionality is displayed
     * in the interface or not.
     * 
     * @param context
     *            the current context
     * @param person
     *            the accessing the functionality or <code>null</code> if it's
     *            the public requester
     * 
     * @return <code>true</code> if the functionality should be displayed to
     *         the user
     */
    public boolean isVisible(FunctionalityContext context, Person person) {
        return isAvailable(context, person);
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

    /**
     * Deletes this functionality from persistent storage.
     * 
     * <p>
     * This delete method is a template method for all functionalities. First
     * {@link #checkDeletion()} is called. If the object is not deletable then a
     * subclass must throw a {@link DomainException} explaining why. If no
     * exception is thrown then {@link #disconnect()} is called to allow the
     * object to remove any specific relations.
     * 
     * <p>
     * After all this the standard relations of a functionality are removed and
     * the object is marked for deletion in the database.
     */
    public void delete() {
        checkDeletion(); // throws exception if cannot delete
        disconnect();
        deleteDomainObject();
    }

    /**
     * Checks if the object can be deleted.
     * 
     * @exception DomainException
     *                if the object cannot be deleted
     */
    protected abstract void checkDeletion();

    /**
     * Removes any specific relations the funtionality has. Subclasses that
     * override this method <strong>must</strong> call super to remove all
     * relations.
     * 
     * <p>
     * If other objects should be deleted because of this object beeing deleted,
     * this is the place to do it.
     */
    protected void disconnect() {
        removeRootDomainObject();
        removeAvailabilityPolicy();
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

    //
    // Auxiliary methods
    //

    /**
     * Auxiliary method that invokes the real service that will delete the
     * functionality.
     * 
     * @param functionality
     *            the functionality that will be deleted
     * @throws Exception
     *             the exception throw by the service
     */
    public static void deleteFunctionality(Functionality functionality) throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "DeleteFunctionality", functionality);
    }

    /**
     * Auxiliary method that invokes the real service that will move the
     * functionality.
     * 
     * @param functionality
     *            the functionality to be moven
     * @param movement
     *            the type of movement
     * @throws Exception
     *             the exception thrown by the service
     */
    public static void moveFunctionality(Functionality functionality, Movement movement)
            throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "MoveFunctionality", functionality,
                movement);
    }

    /**
     * Auxiliary method that invokes the real service that will rearrange all
     * the functionalities. All pairs passed as argument describe the new
     * module/functionality relations. Relations will be broken and created
     * between all the referred functionalities in one transaction.
     * 
     * @param arrangements
     *            a list of pairs (parent, child)
     * @throws Exception
     *             the exception thrown by the service
     */
    public static void rearrangeFunctionalities(List<Pair<Module, Functionality>> arrangements)
            throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "ArrangeFunctionalities", arrangements);
    }

    /**
     * Auxiliary method that invokes the service to enable the given
     * functionality.
     * 
     * @param functionality
     *            the functionality to enable
     * @throws Exception
     *             the exception thrown by the service
     */
    public static void enable(Functionality functionality) throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "ChangeEnableInFunctionality",
                functionality, true);
    }

    /**
     * Auxiliary method that invokes the service to disable the given
     * functionality.
     * 
     * @param functionality
     *            the functionality to disable
     * @throws Exception
     *             the exception thrown by the service
     */
    public static void disable(Functionality functionality) throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "ChangeEnableInFunctionality",
                functionality, false);
    }

    /**
     * Auxiliary method that invokes a service to create a
     * {@link GroupAvailability} for the given functinolity
     * 
     * @param functionality
     *            the functionality that will have it's availability changed
     * @param expression
     *            the group expression used to the create the new group
     *            availability
     */
    public static void setGroupAvailability(Functionality functionality, String expression)
            throws Exception {
        ServiceUtils.executeService(AccessControl.getUserView(), "CreateGroupAvailability",
                functionality, expression);
    }

}
