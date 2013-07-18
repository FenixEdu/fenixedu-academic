package net.sourceforge.fenixedu.presentationTier.renderers;

import pt.ist.fenixWebFramework.renderers.layouts.Layout;

/**
 * This class extends the FenixWebFramework's, so that the provider
 * class can be defined as a Top Level property, instead of forcing it
 * to be specified in every 'args'.
 * 
 * @author João Carvalho (joao.pedro.carvalho@ist.utl.pt)
 * 
 */
public class AutoCompleteInputRenderer extends pt.ist.fenixWebFramework.rendererExtensions.AutoCompleteInputRenderer {

    public AutoCompleteInputRenderer() {
        super();
        setMinChars(3);
    }

    private String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new AutoCompleteLayout() {
            /**
             * Add the provider property to the Arguments
             */
            @Override
            protected String getFormatedArgs() {
                return String.format("provider=%s,%s", getProvider(), super.getFormatedArgs());
            }
        };
    }

}
