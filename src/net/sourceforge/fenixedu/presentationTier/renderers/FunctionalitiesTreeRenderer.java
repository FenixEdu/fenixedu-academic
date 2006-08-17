package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.functionalities.Functionality;
import net.sourceforge.fenixedu.domain.functionalities.Module;

/**
 * Specific renderer for a tree of functionalities. This renderer takes into
 * account the type of the functionalities and if it's enabled or not to decide
 * the type of the presentation.
 * 
 * @author cfgi
 */
public class FunctionalitiesTreeRenderer extends TreeRenderer {

    private String moduleEnabledImage;
    private String moduleDisabledImage;

    private String functionalityEnabledImage;
    private String functionalityDisabledImage;

    private String enabledClass;
    private String disabledClass;

    public FunctionalitiesTreeRenderer() {
        super();

        setChildrenFor(Module.class.getName(), "orderedFunctionalities");
        setDisabledClass("fDisabled");
    }

    public String getDisabledClass() {
        return this.disabledClass;
    }

    public void setDisabledClass(String disabledClass) {
        this.disabledClass = disabledClass;
    }

    public String getEnabledClass() {
        return this.enabledClass;
    }

    public void setEnabledClass(String enabledClass) {
        this.enabledClass = enabledClass;
    }

    public String getFunctionalityDisabledImage() {
        return this.functionalityDisabledImage;
    }

    public void setFunctionalityDisabledImage(String functionalityDisabledImage) {
        this.functionalityDisabledImage = functionalityDisabledImage;
    }

    public String getFunctionalityEnabledImage() {
        return this.functionalityEnabledImage;
    }

    public void setFunctionalityEnabledImage(String functionalityEnabledImage) {
        this.functionalityEnabledImage = functionalityEnabledImage;
    }

    public String getModuleDisabledImage() {
        return this.moduleDisabledImage;
    }

    public void setModuleDisabledImage(String moduleDisabledImage) {
        this.moduleDisabledImage = moduleDisabledImage;
    }

    public String getModuleEnabledImage() {
        return this.moduleEnabledImage;
    }

    public void setModuleEnabledImage(String moduleEnabledImage) {
        this.moduleEnabledImage = moduleEnabledImage;
    }

    @Override
    public boolean isIncludeImage() {
        return super.isIncludeImage() && getModuleEnabledImage() == null
                && getModuleDisabledImage() == null && getFunctionalityEnabledImage() == null
                && getFunctionalityEnabledImage() == null;
    }

    @Override
    protected String getClassFor(Object object) {
        String classes = chooseIfEnabled(object, getEnabledClass(), getDisabledClass(),
                getEnabledClass(), getDisabledClass());

        if (classes != null) {
            return classes;
        } else {
            return super.getClassFor(object);
        }
    }

    @Override
    protected String getImageFor(Object object) {
        String image = chooseIfEnabled(object, getModuleEnabledImage(), getModuleDisabledImage(),
                getFunctionalityEnabledImage(), getFunctionalityDisabledImage());

        if (image != null) {
            return image;
        } else {
            return super.getImageFor(object);
        }
    }

    private String chooseIfEnabled(Object object, String moduleEnabled, String moduleDisabled,
            String functionalityEnabled, String functionalityDisabled) {
        if (object instanceof Module) {
            Module module = (Module) object;

            if (module.isEnabled()) {
                return moduleEnabled;
            } else {
                return moduleDisabled;
            }
        } else {
            Functionality functionality = (Functionality) object;

            if (functionality.isEnabled()) {
                return functionalityEnabled;
            } else {
                return functionalityDisabled;
            }
        }
    }

}
