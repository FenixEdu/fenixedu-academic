package net.sourceforge.fenixedu.presentationTier.renderers.functionalities;

import org.fenixedu.bennu.portal.domain.MenuItem;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public class ContextBreadCrumRenderer extends OutputRenderer {

    private String separator;

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public ContextBreadCrumRenderer() {
        super();
        setSeparator("&gt;");
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                return new HtmlInlineContainer();
/*                MenuFunctionality context = (MenuFunctionality) object;

                if (context == null) {
                }

                HtmlInlineContainer inlineContainer = new HtmlInlineContainer();

                List<MenuItem> contents = context.getPathFromRoot();

                Iterator<MenuItem> contentIterator = contents.iterator();
                while (contentIterator.hasNext()) {

                    inlineContainer.addChild(getMenuComponent(contentIterator.next()));
                    if (contentIterator.hasNext()) {
                        inlineContainer.addChild(new HtmlText(getSeparator(), false));
                    }
                }

                return inlineContainer;
                */
            }

            private HtmlComponent getMenuComponent(MenuItem menuItem) {

                HtmlComponent component = new HtmlText(menuItem.getTitle().getContent());

                if (menuItem.isAvailableForCurrentUser()) {
                    final String prefix = GenericChecksumRewriter.NO_CHECKSUM_PREFIX;
                    HtmlLink link = new HtmlLinkWithPreprendedComment(prefix);

                    HtmlInlineContainer container = new HtmlInlineContainer();
                    container.addChild(component);
                    link.setContextRelative(true);
                    link.setModuleRelative(false);
                    link.setUrl(menuItem.getFullPath());
                    link.setBody(container);

                    component = link;
                }

                return component;
            }

        };
    }
}
