package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.StringRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;

public class PersonNameRenderer extends StringRenderer {

    private boolean firstLastOnly;
    private boolean middleNamesInicials;
    private String normalizedName;
    
    public boolean isFirstLastOnly() {
        return this.firstLastOnly;
    }

    public void setFirstLastOnly(boolean firstLastOnly) {
        this.firstLastOnly = firstLastOnly;
    }

    public boolean isMiddleNamesInicials() {
        return this.middleNamesInicials;
    }

    public void setMiddleNamesInicials(boolean middleNamesInicials) {
        this.middleNamesInicials = middleNamesInicials;
    }

    @Override
    public HtmlComponent render(Object object, Class type) {
        if (object != null) {
            String name = parseName((String) object);
            HtmlComponent component = super.render(name, type);

            if (isFirstLastOnly() || isMiddleNamesInicials()) {
                component.setTitle(this.normalizedName);
            }
            
            return component;
        }
        else {
            return super.render(object, type);
        }
    }

    private String parseName(String name) {
        String finalName;
        this.normalizedName = capitalize(name.toLowerCase());
        
        String[] allNames = this.normalizedName.split("\\p{Space}+");
        if (allNames.length == 0) {
            return "";
        }
        
        if (allNames.length == 1) {
            return allNames[0];
        }
        
        finalName = allNames[0];
        
        if (! isFirstLastOnly()) {
            for (int i = 1; i < allNames.length - 1; i++) {
                if (isMiddleNamesInicials()) {
                    finalName += " " + allNames[i].substring(0, 1) + ".";
                }
                else {
                    finalName += " " + allNames[i];
                }
            }
        }
        
        finalName += " " + allNames[allNames.length - 1];
        
        return finalName;
    }

    private String capitalize(String text) {
        StringBuilder buffer = new StringBuilder();
        char ch, prevCh;
        
        prevCh = ' ';
        for (int i = 0;  i < text.length();  i++) {
           ch = text.charAt(i);
           
           if (Character.isLetter(ch) && !Character.isLetter(prevCh)) {
               buffer.append(Character.toUpperCase(ch));
           }
           else {
               buffer.append(ch);
           }
           
           prevCh = ch;
        }
        
        return buffer.toString();
    }

}
