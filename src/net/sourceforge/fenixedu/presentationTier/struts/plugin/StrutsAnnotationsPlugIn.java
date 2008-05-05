/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.struts.plugin;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Exceptions;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Input;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;
import net.sourceforge.fenixedu.presentationTier.tiles.definition.FenixDefinitionsFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StrutsAnnotationsPlugIn implements PlugIn {

    private static final String INPUT_PAGE_AND_METHOD = ".do?page=0&method=";

    private static final String ROOT_CLASSES_PATH = "/WEB-INF/classes/";

    private static final String INPUT_DEFAULT_PAGE_AND_METHOD = ".do?page=0&method=prepare";

    private static final String EXCEPTION_KEY_DEFAULT_PREFIX = "resources.Action.exceptions.";

    private static Set<Class> actionClasses = new HashSet<Class>();

    private static boolean initialized = false;

    public Set<Class> getActionClasses() {
	return actionClasses;
    }

    public void destroy() {
	actionClasses = new HashSet<Class>();
    }

    public void init(ActionServlet servlet, ModuleConfig config) throws ServletException {

	if (!initialized) {
	    loadActionsFromServletContext(ROOT_CLASSES_PATH, servlet.getServletContext());
	    initialized = true;
	}

	final String modulePrefix = config.getPrefix().startsWith("/") ? config.getPrefix().substring(1) : config.getPrefix();

	for (Class actionClass : getActionClasses()) {
	    Mapping mapping = (Mapping) actionClass.getAnnotation(Mapping.class);
	    if (mapping == null || !modulePrefix.equals(mapping.module())) {
		continue;
	    }

	    final ActionMapping actionMapping = new ActionMapping();

	    actionMapping.setPath(mapping.path());
	    actionMapping.setType(actionClass.getName());
	    actionMapping.setScope("request");
	    actionMapping.setParameter("method");
	    actionMapping.setValidate(true);

	    if (mapping.formBeanClass() != ActionForm.class) {
		final String formName = mapping.formBeanClass().getName();
		createFormBeanConfigIfNecessary(config, mapping, formName);
		actionMapping.setName(formName);
	    } else if (!StringUtils.isEmpty(mapping.formBean())) {
		actionMapping.setName(mapping.formBean());
	    }

	    if (StringUtils.isEmpty(mapping.input())) {
		actionMapping.setInput(findInputMethod(actionClass, mapping));
	    }

	    Forwards forwards = (Forwards) actionClass.getAnnotation(Forwards.class);
	    if (forwards != null) {
		for (final Forward forward : forwards.value()) {
		    actionMapping.addForwardConfig(new ActionForward(forward.name(), forward.path(), false));
		    if (forward.path().endsWith(".jsp")) {
			FenixDefinitionsFactory.addDefinitionName(forward.path());
		    }
		}
	    }

	    Exceptions exceptions = (Exceptions) actionClass.getAnnotation(Exceptions.class);
	    if (exceptions != null) {
		for (Class<? extends Exception> exClass : exceptions.value()) {
		    final ExceptionConfig exceptionConfig = new ExceptionConfig();
		    exceptionConfig.setHandler(FenixErrorExceptionHandler.class.getName());
		    exceptionConfig.setType(exClass.getName());
		    exceptionConfig.setKey(EXCEPTION_KEY_DEFAULT_PREFIX + exClass.getSimpleName());
		    actionMapping.addExceptionConfig(exceptionConfig);
		}
	    }

	    config.addActionConfig(actionMapping);

	}

    }

    private void createFormBeanConfigIfNecessary(ModuleConfig config, Mapping mapping, final String formName) {
	FormBeanConfig formBeanConfig = config.findFormBeanConfig(formName);
	if (formBeanConfig == null) {
	    formBeanConfig = new FormBeanConfig();
	    formBeanConfig.setType(mapping.formBeanClass().getName());
	    formBeanConfig.setName(formName);
	    config.addFormBeanConfig(formBeanConfig);
	}
    }

    private String findInputMethod(Class actionClass, Mapping mapping) {
	for (Method method : actionClass.getMethods()) {
	    final Input input = method.getAnnotation(Input.class);
	    if (input != null) {
		return mapping.path() + INPUT_PAGE_AND_METHOD + method.getName();
	    }
	}
	return mapping.path() + INPUT_DEFAULT_PAGE_AND_METHOD;
    }

    private void loadActionsFromServletContext(String path, ServletContext servletContext) {
	Set<String> resourcePaths = servletContext.getResourcePaths(path);
	if (resourcePaths != null) {
	    for (String subPath : resourcePaths) {
		if (subPath.endsWith("/")) {
		    loadActionsFromServletContext(subPath, servletContext);
		} else if (subPath.endsWith(".class")) {
		    addIfAssignableToAction(subPath.replace(ROOT_CLASSES_PATH, ""));
		}
	    }
	}
    }

    private void addIfAssignableToAction(String classPath) {
	try {
	    ClassLoader loader = Thread.currentThread().getContextClassLoader();
	    String externalName = classPath.substring(0, classPath.indexOf('.')).replace('/', '.');

	    Class type = loader.loadClass(externalName);
	    if (Action.class.isAssignableFrom(type)) {
		actionClasses.add(type);
	    }
	} catch (Throwable t) {
	}
    }

}
