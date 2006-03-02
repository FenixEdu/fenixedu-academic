package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.MenuOptionListRenderer;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

public class InputMenuOptionListRenderer extends MenuOptionListRenderer {
    private String choiceType;
    
    private String filterClass;
    
    public String getChoiceType() {
        return this.choiceType;
    }

    public void setChoiceType(String choiceType) {
        this.choiceType = choiceType;
    }

    public String getFilterClass() {
        return this.filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    @Override
    protected Converter getConverter() {
        if (getProviderClass() != null) {
            return super.getConverter();
        }
        else {
            return new DomainObjectKeyConverter();
        }
    }

    @Override
    protected Collection getPossibleObjects() {
        if (getProviderClass() != null) {
            return super.getPossibleObjects();
        }
        else {
            Object object = getInputContext().getParentContext().getMetaObject().getObject();
            
            String choiceType = getChoiceType();
            String filterClassName = getFilterClass();
            
            try {
                Collection allChoices = readAllChoicesByType(choiceType);

                if (getFilterClass() != null) {
                    Class filterClass = Class.forName(filterClassName);
                    DataFilter filter = (DataFilter) filterClass.newInstance();
    
                    List result = new ArrayList();
                    for (Object choice : allChoices) {
                        if (filter.acccepts(object, choice)) {
                            result.add(object);
                        }
                    }
                    
                    return result;
                }
                else {
                    return allChoices;
                }
            }
            catch (Exception e) {
                throw new RuntimeException("could not filter choices", e);
            }
        }
    }
    
    private Collection readAllChoicesByType(String choiceType) {
        try {
            IUserView userView = AccessControl.getUserView();
            Class type = Class.forName(choiceType);
            
            return (Collection) ServiceUtils.executeService(userView, "ReadAllDomainObjects", new Object[] { type });
        } catch (Exception e) {
            throw new RuntimeException("could not read all objects of type " + choiceType);
        }
    }
}
