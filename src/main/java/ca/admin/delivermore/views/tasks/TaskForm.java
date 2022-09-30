package ca.admin.delivermore.views.tasks;

import ca.admin.delivermore.collector.data.entity.TaskEntity;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class TaskForm extends FormLayout {
    Binder<TaskEntity> binder = new BeanValidationBinder<>(TaskEntity.class);

    TextField restaurantName = new TextField("Restaurant Name");
    TextField jobStatusName = new TextField("Job Status");
    EmailField customerUsername = new EmailField("Customer Name");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    private TaskEntity taskEntity;

    public TaskForm() {
        addClassName("task-form");
        binder.bindInstanceFields(this);

        add(restaurantName,
                jobStatusName,
                customerUsername,
                createButtonsLayout());
    }

    public void setTaskEntity(TaskEntity taskEntity){
        this.taskEntity = taskEntity;
        binder.readBean(taskEntity);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, taskEntity)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(taskEntity);
            fireEvent(new SaveEvent(this, taskEntity));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class TaskEntityFormEvent extends ComponentEvent<TaskForm> {
        private TaskEntity taskEntity;

        protected TaskEntityFormEvent(TaskForm source, TaskEntity taskEntity) {
            super(source, false);
            this.taskEntity = taskEntity;
        }

        public TaskEntity getTaskEntity() {
            return taskEntity;
        }
    }

    public static class SaveEvent extends TaskEntityFormEvent {
        SaveEvent(TaskForm source, TaskEntity taskEntity) {
            super(source, taskEntity);
        }
    }

    public static class DeleteEvent extends TaskEntityFormEvent {
        DeleteEvent(TaskForm source, TaskEntity taskEntity) {
            super(source, taskEntity);
        }

    }

    public static class CloseEvent extends TaskEntityFormEvent {
        CloseEvent(TaskForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
