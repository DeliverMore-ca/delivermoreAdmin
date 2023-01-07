package ca.admin.delivermore.components.custom;

import ca.admin.delivermore.views.UIUtilities;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.ArrayList;
import java.util.List;

public class ListEditor extends VerticalLayout {
    final static String LIST_BORDER = "1px var(--lumo-primary-color) solid";
    final static String LIST_HEIGHT = "300px";
    final static String LIST_LEFT_SIZE = "75%";
    final static String LIST_RIGHT_SIZE = "25%";
    private String value;
    private String separator = ",";
    private ListBox list = new ListBox<>();
    private TextField addField;
    private List<String> items = new ArrayList<>();

    public ListEditor() {
        createListEditor();
    }

    private void buildList(){
        addField.setValue("");
        items.clear();
        if (value!=null && !value.isEmpty()) {
            String parts[] = value.split(separator);
            if (parts!=null && parts.length>0) {
                for (String p : parts)
                    items.add(p);
            }
            list.setItems(items);
        }
    }

    private String getValueFromList(){
        StringBuffer sb = new StringBuffer();
        if (items!=null && items.size()>0) {
            int len = items.size();
            for (int i=0;i<len;i++) {
                sb.append(items.get(i));
                if (i<(len-1)) {
                    sb.append(separator);
                }
            }
        }
        return sb.toString();
    }

    private void createListEditor(){
        setPadding(false);
        setSpacing(false);
        setWidthFull();
        HorizontalLayout addArea = new HorizontalLayout();
        addArea.setWidthFull();
        addArea.setPadding(false);
        addArea.setSpacing(true);
        addField = UIUtilities.createSmallTextField("New item");
        addField.setAutofocus(true);
        addField.setValueChangeMode(ValueChangeMode.EAGER);
        UIUtilities.scrollIntoView(addField);
        addField.setWidth(LIST_LEFT_SIZE);
        Button addButton = UIUtilities.createSmallButton("Add");
        addButton.setWidth(LIST_RIGHT_SIZE);
        addButton.addClickListener(event -> {
            addItemToList();
        });
        ShortcutRegistration shortcutRegistration = Shortcuts
                .addShortcutListener(this, () -> addItemToList(), Key.ENTER)
                .listenOn(this);
        //shortcutRegistration.setEventPropagationAllowed(false);
        //shortcutRegistration.setBrowserDefaultAllowed(true);

        addArea.add(addField,addButton);
        addArea.setFlexGrow(1,addField);
        addArea.setAlignItems(FlexComponent.Alignment.BASELINE);
        HorizontalLayout listArea = new HorizontalLayout();
        listArea.setWidthFull();
        listArea.setPadding(false);
        listArea.setSpacing(false);
        VerticalLayout listLayout = new VerticalLayout();
        listLayout.setWidth(LIST_LEFT_SIZE);
        listLayout.setPadding(false);
        listLayout.setSpacing(false);
        listLayout.getStyle().set("border", LIST_BORDER);
        list.setHeight(LIST_HEIGHT);
        list.setWidthFull();
        listLayout.add(list);
        VerticalLayout listAreaButtons = new VerticalLayout();
        listAreaButtons.setPadding(false);
        listAreaButtons.setSpacing(false);
        listAreaButtons.setWidth(LIST_RIGHT_SIZE);
        listAreaButtons.setAlignItems(FlexComponent.Alignment.CENTER);
        listArea.add(listLayout,listAreaButtons);
        Button up = UIUtilities.createSmallButton("Up");
        up.addClickListener(event -> {
            if(list.getItemPosition(list.getValue())>0){
                Object selectedItem = list.getValue();
                int sel = list.getItemPosition(list.getValue());
                items.remove(sel);
                items.add(sel-1,list.getValue().toString());
                value = getValueFromList();
                list.setItems(items);
                if(selectedItem!=null) list.setValue(selectedItem);
            }
        });
        Button down = UIUtilities.createSmallButton("Down");
        down.addClickListener(event -> {
            int sel = list.getItemPosition(list.getValue());
            if(sel>=0 && sel<items.size()-1){
                Object selectedItem = list.getValue();
                items.remove(sel);
                items.add(sel+1,list.getValue().toString());
                value = getValueFromList();
                list.setItems(items);
                if(selectedItem!=null) list.setValue(selectedItem);
            }
        });
        Button remove = UIUtilities.createSmallButton("Remove");
        remove.addClickListener(event -> {
            if(list.getValue()!=null){
                items.remove(list.getItemPosition(list.getValue()));
                value = getValueFromList();
                list.setItems(items);
            }
        });
        listAreaButtons.add(up,down,remove);
        add(addArea,listArea);
    }

    private void addItemToList(){
        //log.info("addItemToList: addField value:" + addField.getValue() + " items size:" + items.size());
        if(addField.getValue()!=null && !addField.getValue().isEmpty()){
            if(items.contains(addField.getValue())){
                UIUtilities.showNotification("Item '" + addField.getValue() + "' already exists in the list.");
            }else{
                Object selectedItem = list.getValue();
                if(list.getItemPosition(selectedItem)>=0){
                    items.add(list.getItemPosition(selectedItem)+1,addField.getValue());
                    value = getValueFromList();
                }else{
                    items.add(addField.getValue());
                    value = getValueFromList();
                }
                list.setItems(items);
                if(selectedItem!=null) list.setValue(selectedItem);
                addField.setValue("");
            }
        }
        //log.info("addItemToList: AFTER addField value:" + addField.getValue() + " items size:" + items.size());

    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        buildList();
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

}
