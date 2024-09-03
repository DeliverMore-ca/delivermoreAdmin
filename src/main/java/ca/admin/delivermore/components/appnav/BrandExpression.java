package ca.admin.delivermore.components.appnav;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.NativeLabel;

//@CssImport("./components/brand-expression.css")
public class BrandExpression extends Div {

    private static String CLASS_NAME = "brand-expression";

    private Image logo;
    private NativeLabel title;

    public BrandExpression(String text) {
        setClassName(CLASS_NAME);

        //logo = new Image(UIUtils.IMG_PATH + "logos/18.png", "");
        logo = new Image("icons/icon.png", "");
        logo.setAlt(text + " logo");
        logo.setClassName(CLASS_NAME + "__logo");
        logo.setMaxHeight("50px");
        logo.setMaxWidth("50px");

        title = createH3Label(text);
        title.addClassName(CLASS_NAME + "__title");
        add(logo, title);
    }

    public static NativeLabel createH3Label(String text) {
        NativeLabel label = new NativeLabel(text);
        //label.addClassName(LumoStyles.Heading.H3);
        label.addClassName(CLASS_NAME + "__Heading.H3");
        return label;
    }
}
