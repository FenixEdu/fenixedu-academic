package net.sourceforge.fenixedu.renderers.utils;

import java.util.Properties;

import net.sourceforge.fenixedu.renderers.Renderer;

/**
 * RendererDescription is used to mantain the renderer's class and the default
 * properties associated with that particular renderer.
 * 
 * @author cfgi
 */
public class RendererDescription {
    private Class<? extends Renderer> renderer;

    private Properties properties;

    public RendererDescription(Class<Renderer> renderer, Properties defaultProperties) {
        this.renderer = renderer;
        this.properties = defaultProperties;
    }

    public Properties getProperties() {
        return properties;
    }

    public Class<? extends Renderer> getRenderer() {
        return renderer;
    }

    public Renderer createRenderer() {
        Renderer renderer = null;

        try {
            renderer = getRenderer().newInstance();

            if (properties != null) {
                RenderUtils.setProperties(renderer, properties);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return renderer;
    }
}