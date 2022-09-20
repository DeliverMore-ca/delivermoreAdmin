package ca.admin.delivermore.components.custom;

import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LongTextField extends CustomField<String> {
    private TextField textField = new TextField();

    public LongTextField() {
        add(textField);
    }

    public LongTextField(String label) {
        this.textField.setLabel(label);
        add(textField);
    }

    @Override
    protected String generateModelValue() {
        return textField.getValue();
    }

    @Override
    protected void setPresentationValue(String s) {
        String regex = "(?<=[\\d])(,)(?=[\\d])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        String str = m.replaceAll("");
        textField.setValue(str);
    }

}
