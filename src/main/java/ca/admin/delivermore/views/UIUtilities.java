package ca.admin.delivermore.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class UIUtilities {

    public static Details getDetails(){
        Details details = new Details();
        details.setWidthFull();
        details.addThemeVariants(DetailsVariant.FILLED);
        return details;
    }

    public static String getNumberFormatted(Double input){
        return String.format("%.2f",input);
    }

    public static NumberField getNumberField(String label, Double number){
        NumberField numberField = getNumberField(label);
        numberField.setValue(number);
        return numberField;
    }
    public static NumberField getNumberField(String label){
        Div dollarPrefix = new Div();
        dollarPrefix.setText("$");
        NumberField numberField = new NumberField();
        if(!label.isEmpty()){
            numberField.setLabel(label);
        }
        numberField.setReadOnly(true);
        numberField.setPrefixComponent(dollarPrefix);
        numberField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        return numberField;
    }

    public static TextField getTextFieldRO(String label, String text, String width ){
        TextField textField = getTextFieldRO(label,text);
        textField.setWidth(width);
        return textField;
    }
    public static TextField getTextFieldRO(String label, String text){
        TextField textField = new TextField(label);
        textField.setReadOnly(true);
        textField.setValue(text);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        return textField;
    }

    public static HorizontalLayout getHorizontalLayout(){
        return getHorizontalLayout(false,false,false);
    }
    public static HorizontalLayout getHorizontalLayout(Boolean paddingEnabled, Boolean spacingEnabled, Boolean marginEnabled){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setPadding(paddingEnabled);
        horizontalLayout.setSpacing(spacingEnabled);
        horizontalLayout.setMargin(marginEnabled);
        horizontalLayout.setWidthFull();
        return horizontalLayout;
    }

    public static HorizontalLayout getHorizontalLayoutNoWidthCentered(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setPadding(false);
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return horizontalLayout;
    }

    public static VerticalLayout getVerticalLayout(){
        return getVerticalLayout(false,false,false);
    }
    public static VerticalLayout getVerticalLayout(Boolean paddingEnabled, Boolean spacingEnabled, Boolean marginEnabled){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setPadding(paddingEnabled);
        verticalLayout.setSpacing(spacingEnabled);
        verticalLayout.setMargin(marginEnabled);
        verticalLayout.setWidthFull();
        return verticalLayout;
    }

    public static String singlePlural(int count, String singular, String plural)
    {
        return count==1 ? singular : plural;
    }

    public static TextField createSmallTextField(String label) {
        TextField textField = new TextField(label);
        textField.addValueChangeListener(event ->{
            //setTooltip(event.getSource().getValue(),event.getSource());
        });
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        return textField;
    }

    public static void scrollIntoView(Component component){
        component.getElement().executeJs(
                "$0.scrollIntoView({behavior: \"smooth\", block: \"end\", inline: \"nearest\"});", component.getElement());
    }

    public static Button createSmallButton(String text) {
        return createButton(text, ButtonVariant.LUMO_SMALL);
    }

    public static Button createButton(String text, ButtonVariant... variants) {
        Button button = new Button(text);
        button.addThemeVariants(variants);
        button.getElement().setAttribute("aria-label", text);
        return button;
    }

    public static void showNotification(String text) {
        Notification.show(text, 3000, Notification.Position.BOTTOM_CENTER);
    }




}
