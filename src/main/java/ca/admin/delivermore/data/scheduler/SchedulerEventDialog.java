package ca.admin.delivermore.data.scheduler;

import ca.admin.delivermore.collector.data.service.DriversRepository;
import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.components.custom.Divider;
import ca.admin.delivermore.data.service.Registry;
import ca.admin.delivermore.data.service.SchedulerEventRepository;
import ca.admin.delivermore.views.UIUtilities;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datepicker.DatePickerVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.select.SelectVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.component.timepicker.TimePickerVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.stefan.fullcalendar.EntryTimeChangedEvent;
import org.vaadin.stefan.fullcalendar.ResourceEntry;
import org.vaadin.stefan.fullcalendar.SchedulerView;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SchedulerEventDialog {
    public enum DialogMode{
        NEW, EDIT, DELETE
    }

    private Logger log = LoggerFactory.getLogger(SchedulerEventDialog.class);
    private Dialog dialog = new Dialog();
    private DialogMode dialogMode = DialogMode.EDIT;

    private Scheduler.EditType editType = Scheduler.EditType.CALENDAR;
    private Button dialogOkButton = new Button();
    private Icon okIcon = new Icon("lumo", "checkmark");
    private Button dialogCancelButton = new Button(new Icon("lumo", "cross"));
    private Button dialogResetButton = new Button();
    private Icon resetIcon = new Icon("lumo", "undo");
    private Button dialogDeleteButton = new Button();
    private Button dialogCloseButton = new Button(new Icon("lumo", "cross"));

    private Boolean validationEnabled = Boolean.FALSE;
    private Boolean hasChangedValues = Boolean.FALSE;

    //private ComboBox<Driver> dialogDriver = new ComboBox<>("Driver");
    private Select<Driver> dialogDriver = new Select<>();
    private RadioButtonGroup<Scheduler.EventType> dialogEventType = new RadioButtonGroup<>();

    private RadioButtonGroup<Scheduler.EventDurationType> dialogDurationType = new RadioButtonGroup<>();
    private DatePicker dialogStartDate = new DatePicker();
    private HorizontalLayout dialogTimeSelection = UIUtilities.getHorizontalLayout();

    private TimePicker dialogStartTime = new TimePicker();
    private TimePicker dialogEndTime = new TimePicker();
    private TextField dialogHours = UIUtilities.getTextFieldRO("hrs", "");

    private Checkbox dialogPublished = new Checkbox();

    private DriversRepository driversRepository;
    private SchedulerEventRepository schedulerEventRepository;

    private SchedulerEvent event;
    private Driver driver;

    private List<SchedulerRefreshNeededListener> schedulerRefreshNeededListeners = new ArrayList<>();

    public SchedulerEventDialog() {
        this.driversRepository = Registry.getBean(DriversRepository.class);
        schedulerEventRepository = Registry.getBean(SchedulerEventRepository.class);
        dialogConfigure();
    }

    private void dialogConfigure() {
        dialog.getElement().setAttribute("aria-label", "Edit adjustment");

        VerticalLayout dialogLayout = dialogLayout();
        dialog.add(dialogLayout);
        dialog.setHeaderTitle("Event");

        Icon deleteIcon = new Icon("lumo", "cross");
        deleteIcon.setColor("red");
        dialogDeleteButton.setIcon(deleteIcon);
        dialogDeleteButton.setText("Delete");
        dialogDeleteButton.addClickListener(event -> {
            dialogDelete();
        });

        dialogCloseButton.addClickListener((e) -> dialog.close());
        dialogCloseButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getHeader().add(dialogCloseButton);
        dialog.setCloseOnEsc(true);
        dialogCancelButton.addClickListener((e) -> dialog.close());

        dialogOkButton.setIcon(okIcon);
        dialogOkButton.setText("Save");
        dialogOkButton.setAutofocus(true);

        dialogOkButton.addClickListener(
                event -> {
                    //TODO: check for conflict
                    dialogSave();
                }
        );
        dialogOkButton.addClickShortcut(Key.ENTER);
        dialogOkButton.setEnabled(false);
        dialogOkButton.setDisableOnClick(true);

        dialogResetButton.setText("Reset");
        dialogResetButton.setIcon(resetIcon);
        dialogResetButton.addClickListener(
                event -> {
                    enableOkReset(false);
                    setValues();
                    dialogValidate();
                }
        );
        enableOkReset(false);

        HorizontalLayout footerLayout = new HorizontalLayout(dialogOkButton,dialogDeleteButton,dialogResetButton);

        // Prevent click shortcut of the OK button from also triggering when another button is focused
        ShortcutRegistration shortcutRegistration = Shortcuts
                .addShortcutListener(footerLayout, () -> {}, Key.ENTER)
                .listenOn(footerLayout);
        shortcutRegistration.setEventPropagationAllowed(false);
        shortcutRegistration.setBrowserDefaultAllowed(true);

        dialog.getFooter().add(footerLayout);
    }

    private void enableOkReset(Boolean enable){
        dialogOkButton.setEnabled(enable);
        dialogResetButton.setEnabled(enable);
        if(enable){
            okIcon.setColor("green");
            resetIcon.setColor("blue");
        }else{
            okIcon.setColor(UIUtilities.iconColorNotHighlighted);
            resetIcon.setColor(UIUtilities.iconColorNotHighlighted);
        }
    }

    private void dialogDelete() {
        //delete with confirmation
        log.info("dialogDelete:");
        ConfirmDialog dialogConfirmDelete = new ConfirmDialog();
        dialogConfirmDelete.setHeader("Delete schedule entry?");
        Html text = new Html("<p>Are you sure you want to permanently delete this item?<br>" + event.getDescription() + "</p>");
        dialogConfirmDelete.setText(text);

        dialogConfirmDelete.setCancelable(true);
        //dialog.addCancelListener(event -> setStatus("Canceled"));

        dialogConfirmDelete.setConfirmText("Delete");
        dialogConfirmDelete.setConfirmButtonTheme("error primary");
        dialogConfirmDelete.addConfirmListener(event -> {
            //TODO: perform delete here
            schedulerEventRepository.delete(this.event);
            dialog.close();
            notifyRefreshNeeded();
        });
        dialogConfirmDelete.open();

    }

    private void dialogSave() {
        log.info("dialogSave:");
        //TODO: check for conflict first
        String sourceId = null;
        if(this.event.getId()!=null){
            sourceId = this.event.getId().toString();
        }
        String targetResourceId = dialogDriver.getValue().getFleetId().toString();
        LocalDateTime targetStart = LocalDateTime.of(dialogStartDate.getValue(),dialogStartTime.getValue());
        LocalDateTime targetEnd = LocalDateTime.of(dialogStartDate.getValue(),dialogEndTime.getValue());
        if(dialogDurationType.getValue().equals(Scheduler.EventDurationType.FULLDAY)){
            targetStart = LocalDateTime.of(targetStart.toLocalDate(), Scheduler.minTime);
            targetEnd = LocalDateTime.of(targetStart.toLocalDate(), Scheduler.maxTime);
        }

        LocalDateTime targetStartWithOffset = targetStart;
        LocalDateTime targetEndWithOffset = targetEnd;
        Boolean forceAllDay = false;

        allowSave(sourceId, targetResourceId, targetStart, targetEnd, targetStartWithOffset, targetEndWithOffset, forceAllDay, null);

    }

    private void saveDialogEditedEvent(String id, String resourceId, LocalDateTime start, LocalDateTime end, Boolean allDayView ){
        this.event.setType(dialogEventType.getValue());
        if(dialogDurationType.getValue().equals(Scheduler.EventDurationType.FULLDAY)){
            this.event.setFullDay(true);
            //make sure the start and end are set to the start and end of the day
            this.event.setStart(LocalDateTime.of(start.toLocalDate(), Scheduler.minTime));
            this.event.setEnd(LocalDateTime.of(start.toLocalDate(), Scheduler.maxTime));
        }else{
            this.event.setFullDay(false);
            this.event.setStart(start);
            this.event.setEnd(end);
        }
        this.event.setResourceId(resourceId);
        //set the event to NOT published so it will get published later
        this.event.setPublished(false);

        schedulerEventRepository.save(this.event);
        notifyRefreshNeeded();
        dialog.close();
    }


    public void checkForConflict(EntryTimeChangedEvent e, SchedulerView schedulerView) {
        Boolean allDaySlotUsed = null;
        editType = Scheduler.EditType.CALENDAR;
        //needs to return info about conflict event for display
        //if()
        ResourceEntry oldResourceEntry = (ResourceEntry) e.getEntry();
        log.info("checkForConflict: oldResourceEntry:" + oldResourceEntry);
        ResourceEntry newResourceEntry = (ResourceEntry) e.applyChangesOnEntry();
        log.info("checkForConflict: newResourceEntry:" + newResourceEntry);

        allDaySlotUsed = newResourceEntry.isAllDay();
        log.info("checkForConflict: oldFullDay:" + e.getEntry().isAllDay() + " newFullDay:" + newResourceEntry.isAllDay());

        String targetResourceId = newResourceEntry.getResource().get().getId();
        LocalDateTime targetStart = newResourceEntry.getStart();
        LocalDateTime targetEnd = newResourceEntry.getEnd();
        LocalDateTime targetStartWithOffset = newResourceEntry.getStartWithOffset();
        log.info("*** 1. targetStartWithOffset:" + targetStartWithOffset);
        LocalDateTime targetEndWithOffset = newResourceEntry.getEndWithOffset();
        String sourceId = newResourceEntry.getId();

        Boolean forceAllDay = Boolean.TRUE;
        if(schedulerView.equals(SchedulerView.RESOURCE_TIMELINE_WEEK) || schedulerView.equals(SchedulerView.RESOURCE_TIMELINE_MONTH)){
            forceAllDay = Boolean.TRUE;
            //these views do not have an AllDaySlot so set to null
            allDaySlotUsed = null;
        }else{
            forceAllDay = Boolean.FALSE;
        }

        Optional<SchedulerEvent> eventToCheck = schedulerEventRepository.findById(Long.valueOf(sourceId));
        if(eventToCheck==null){
            log.info("checkForConflict: forceAllDay:" + forceAllDay + " sourceId:" + sourceId + " not found in the database");
            return;
        }
        if(forceAllDay || eventToCheck.get().getFullDay()){
            targetStartWithOffset = LocalDateTime.of(targetStart.toLocalDate(), eventToCheck.get().getStart().toLocalTime());
            log.info("*** 2. targetStartWithOffset:" + targetStartWithOffset);
            targetEndWithOffset = LocalDateTime.of(targetStart.toLocalDate(), eventToCheck.get().getEnd().toLocalTime());

        }else{
            //do not allow a SHIFT type entry to be moved to all day slot
            if(allDaySlotUsed && eventToCheck.get().getType().equals(Scheduler.EventType.SHIFT)){
                UIUtilities.showNotificationError("SHIFT type entry cannot be dropped on All Day slot.  Use edit instead.");
                log.info("checkForConflict: drop of SHIFT on All Day slot prevented");
                notifyRefreshNeeded();
                return;
            }
        }
        log.info("checkForConflict: forceAllDay:" + forceAllDay + " sourceId:" + sourceId + " targetResourceId:" + targetResourceId + " targetStartWithOffset:" + targetStartWithOffset);

        log.info("*** 3. targetStartWithOffset:" + targetStartWithOffset);
        allowSave(sourceId, targetResourceId, targetStart, targetEnd, targetStartWithOffset, targetEndWithOffset, forceAllDay, allDaySlotUsed);
    }

    private void allowSave(String sourceId, String targetResourceId, LocalDateTime targetStart, LocalDateTime targetEnd, LocalDateTime targetStartWithOffset, LocalDateTime targetEndWithOffset, Boolean forceAllDay, Boolean allDaySlotUsed){
        Boolean conflicts = Boolean.FALSE;
        log.info("allowSave: sourceId:" + sourceId + " targetResourceId:" + targetResourceId + " targetStartWithOffset:" + targetStartWithOffset);
        List<SchedulerEvent> resourceEvents = schedulerEventRepository.findByResourceIdAndStartBetween(targetResourceId,targetStartWithOffset.toLocalDate().atStartOfDay(),targetStartWithOffset.toLocalDate().atTime(23,59));
        if(resourceEvents==null || resourceEvents.size()==0){
            log.info("allowSave: resourceEvents was null or size was 0");
        }else{
            //review conflicts
            log.info("allowSave: source:" + sourceId + " startWithOffset:" + targetStartWithOffset + " endWithOffset:" + targetEndWithOffset);
            for (SchedulerEvent schedulerEvent : resourceEvents) {
                log.info("allowSave: checking against:" + schedulerEvent.getId() + " start:" + schedulerEvent.getStart() + " end:" + schedulerEvent.getEnd());
                if((schedulerEvent.getEnd() == null || targetStartWithOffset.isBefore(schedulerEvent.getEnd())) && (targetEndWithOffset == null || targetEndWithOffset.isAfter(schedulerEvent.getStart()))){
                    if(sourceId!=null && sourceId.equals(schedulerEvent.getId().toString())){
                        log.info("allowSave: ignoring original event:" + schedulerEvent.getId() + " sourceId:" + sourceId);
                    }else{
                        log.info("allowSave: found conflict:" + schedulerEvent.getId() + " sourceId:" + sourceId);
                        conflicts = Boolean.TRUE;
                        break;
                    }
                }
            }
        }

        if(conflicts){
            ConfirmDialog confirmDialogBox = new ConfirmDialog();
            confirmDialogBox.setHeader("Allow conflict");
            confirmDialogBox.setText(
                    "Do you want to allow this event to conflict with existing event?");

            confirmDialogBox.setCancelable(true);
            confirmDialogBox.addCancelListener(event -> {
                log.info("allowSave: conflict found - not saving (Cancel)");
                conflictRefresh();
            });

            confirmDialogBox.setRejectable(true);
            confirmDialogBox.setRejectText("Discard save");
            confirmDialogBox.addRejectListener(event -> {
                log.info("allowSave: conflict found - not saving (Reject)");
                conflictRefresh();
            });

            confirmDialogBox.setConfirmText("Allow");
            confirmDialogBox.addConfirmListener(event -> {
                log.info("allowSave: conflict found but saving");
                conflictSave(sourceId,targetResourceId,targetStart,targetEnd,forceAllDay, allDaySlotUsed);
            });

            confirmDialogBox.open();
        }else{
            log.info("allowSave: No conflict found");
            if(editType.equals(Scheduler.EditType.CALENDAR)){
                saveCalendarEditedEvent(sourceId,targetResourceId,targetStart,targetEnd, forceAllDay, allDaySlotUsed);
            }else{
                saveDialogEditedEvent(sourceId,targetResourceId,targetStart,targetEnd, forceAllDay);
            }
        }
    }

    private void conflictSave(String sourceId, String targetResourceId, LocalDateTime targetStart, LocalDateTime targetEnd, Boolean forceAllDay, Boolean allDaySlotUsed){
        if(editType.equals(Scheduler.EditType.CALENDAR)){
            log.info("conflictSave: CALENDAR: allow save passed: calling saveEditedEvent");
            saveCalendarEditedEvent(sourceId,targetResourceId,targetStart,targetEnd, forceAllDay, allDaySlotUsed);
        }else{
            log.info("conflictSave: DIALOG: allow save passed: calling saveEditedEvent");
            saveDialogEditedEvent(sourceId,targetResourceId,targetStart,targetEnd, forceAllDay);
        }
    }

    private void conflictRefresh(){
        if(editType.equals(Scheduler.EditType.CALENDAR)){
            log.info("conflictRefresh: CALENDAR: calling refresh");
            //TODO: this is NOT undoing the edit
            notifyRefreshNeeded();
        }else{
            log.info("conflictRefresh: DIALOG: calling validate and returning to Dialog");
            dialogValidate();
        }
    }

    private void saveCalendarEditedEvent(String id, String resourceId, LocalDateTime start, LocalDateTime end, Boolean allDayView, Boolean allDaySlotUsed ){
        log.info("saveCalendarEditedEvent: save changes: start:" + start + " end:" + end);
        Optional<SchedulerEvent> eventToEdit = schedulerEventRepository.findById(Long.valueOf(id));
        if(eventToEdit==null){
            log.info("saveCalendarEditedEvent: save FAILED: event id not found for:" + id);
        }else{
            if(allDayView || eventToEdit.get().getFullDay()){
                log.info("saveCalendarEditedEvent: AllDayView: update fields started");
                eventToEdit.get().setStart(LocalDateTime.of(start.toLocalDate(),eventToEdit.get().getStart().toLocalTime()));
                eventToEdit.get().setEnd(LocalDateTime.of(start.toLocalDate(),eventToEdit.get().getEnd().toLocalTime()));
            }else{
                log.info("saveCalendarEditedEvent: Other: update fields started: start:" + Scheduler.tzDefault.applyTimezoneOffset(start) + " end:" + Scheduler.tzDefault.applyTimezoneOffset(end));
                eventToEdit.get().setStart(Scheduler.tzDefault.applyTimezoneOffset(start));
                eventToEdit.get().setEnd(Scheduler.tzDefault.applyTimezoneOffset(end));
            }
            eventToEdit.get().setResourceId(resourceId);
            //check to see if we need to change the duration type
            if(allDaySlotUsed==null){
                //do not change anything
                log.info("saveCalendarEditedEvent: allDaySlotUsed was null - no change");
            }else if(allDaySlotUsed){
                log.info("saveCalendarEditedEvent: allDaySlotUsed true. Force duration to full day");
                eventToEdit.get().setFullDay(true);
                eventToEdit.get().setStart(LocalDateTime.of(start.toLocalDate(), Scheduler.minTime));
                eventToEdit.get().setEnd(LocalDateTime.of(start.toLocalDate(), Scheduler.maxTime));
            }else{
                log.info("saveCalendarEditedEvent: allDaySlotUsed false. Force duration to partial day: start:" + start + " end:" + end);
                eventToEdit.get().setFullDay(false);
                eventToEdit.get().setStart(Scheduler.tzDefault.applyTimezoneOffset(start));
                eventToEdit.get().setEnd(Scheduler.tzDefault.applyTimezoneOffset(end));
            }
            //set the event to NOT published so it will get published later
            eventToEdit.get().setPublished(false);

            schedulerEventRepository.save(eventToEdit.get());
            log.info("saveCalendarEditedEvent: save performed");
        }
        notifyRefreshNeeded();
    }

    public void addListener(SchedulerRefreshNeededListener listener){
        schedulerRefreshNeededListeners.add(listener);
    }

    private void notifyRefreshNeeded(){
        for (SchedulerRefreshNeededListener listener: schedulerRefreshNeededListeners) {
            listener.schedulerRefreshNeeded();
        }
    }

    private VerticalLayout dialogLayout() {

        //dialogEventType.setLabel("-Type");
        dialogEventType.setItems(Scheduler.EventType.values());
        dialogEventType.setItemLabelGenerator(type -> {
            return type.typeName;
        });
        dialogEventType.addValueChangeListener(e -> {
            if(validationEnabled){
                //this.event.setType(e.getValue());
                updateEventType();
                log.info("dialogLayout: value changed from:" + e.getOldValue() + " to:" + e.getValue());
            }
        });

        //dialogDriver.setItems(driversRepository.findAll(Sort.by(Sort.Direction.ASC, "name")));
        dialogDriver.setLabel("-Driver");
        dialogDriver.setItemLabelGenerator(driver -> {
            if (driver == null) {
                return Scheduler.availableShiftsDisplayName;
            }
            return driver.getName();
        });
        dialogDriver.setItems(driversRepository.findActiveOrderByNameAsc());
        dialogDriver.setReadOnly(true);
        dialogDriver.setPlaceholder("Select driver");
        dialogDriver.setEmptySelectionAllowed(true);
        dialogDriver.setEmptySelectionCaption(Scheduler.availableShiftsDisplayName);
        dialogDriver.addValueChangeListener(item -> {
            if(validationEnabled){
                //this.event.setResourceId(item.getValue().getFleetId().toString());
                dialogValidate();
            }
        });
        dialogDriver.addThemeVariants(SelectVariant.LUMO_SMALL);

        dialogStartDate.setLabel("-Date");
        dialogStartDate.setReadOnly(true);
        dialogStartDate.addThemeVariants(DatePickerVariant.LUMO_SMALL);
        dialogStartDate.addValueChangeListener(e -> {
            if(validationEnabled) updateDialogTimes();
        });

        Divider divider = new Divider();

        //TODO: part or full day
        dialogDurationType.setItems(Scheduler.EventDurationType.values());
        dialogDurationType.setItemLabelGenerator(type -> {
            return type.typeName;
        });
        dialogDurationType.addValueChangeListener(e -> {
            if(validationEnabled){
                updateDurationType();
                log.info("dialogLayout: duration type value changed from:" + e.getOldValue() + " to:" + e.getValue());
            }
        });

        dialogTimeSelection.setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        dialogStartTime.setLabel("-Start");
        dialogStartTime.setMin(Scheduler.minTime);
        dialogStartTime.setMax(Scheduler.maxTime);
        dialogStartTime.setStep(Scheduler.timeStep);
        dialogStartTime.setWidth("120px");
        dialogStartTime.addThemeVariants(TimePickerVariant.LUMO_SMALL);
        dialogStartTime.addValueChangeListener(e -> {
            dialogEndTime.setMin(e.getValue().plusMinutes(30));
            if(validationEnabled) updateDialogTimes();
        });
        dialogEndTime.setLabel("-End");
        dialogEndTime.setMin(Scheduler.minTime);
        dialogEndTime.setMax(Scheduler.maxTime);
        dialogEndTime.setStep(Scheduler.timeStep);
        dialogEndTime.setWidth("120px");
        dialogEndTime.addThemeVariants(TimePickerVariant.LUMO_SMALL);
        dialogEndTime.addValueChangeListener(e -> {
            dialogStartTime.setMax(e.getValue().minusMinutes(30));
            if(validationEnabled) updateDialogTimes();
        });
        dialogHours.setWidth("60px");
        dialogTimeSelection.add(dialogStartTime,dialogEndTime,dialogHours);

        //Not changable from dialog - must perform a publish
        //TODO: add a publish function
        dialogPublished.setLabel("Published");
        dialogPublished.setEnabled(false);

        VerticalLayout fieldLayout = new VerticalLayout(dialogEventType,dialogDriver, dialogStartDate, new Divider(), dialogDurationType, dialogTimeSelection,new Divider(), dialogPublished);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return fieldLayout;
    }
    
    private void updateEventType() {
        //based on the current value of EventType dialog field
        if(dialogEventType.getValue().equals(Scheduler.EventType.SHIFT)){
            //this.event.setFullDay(false);
            dialogDurationType.setValue(Scheduler.EventDurationType.PARTIALDAY);
            dialogDurationType.setEnabled(false);
        }else{
            //UNAVAILABLE and OFF allow full day
            //dialogDurationType.setValue(Scheduler.EventDurationType.FULLDAY);
            dialogDurationType.setEnabled(true);
        }
        dialogValidate();
    }

    private void updateDurationType() {
        //based on the current value of FullDay dialog field
        if(dialogDurationType.getValue().equals(Scheduler.EventDurationType.FULLDAY)){
            dialogTimeSelection.setVisible(false);
        }else{
            dialogTimeSelection.setVisible(true);
        }
        dialogValidate();
    }

    private void updateDialogTimes() {
        //this.event.setStart(LocalDateTime.of(dialogStartDate.getValue(),dialogStartTime.getValue()));
        //this.event.setEnd(LocalDateTime.of(dialogStartDate.getValue(),dialogEndTime.getValue()));
        dialogHours.setValue(getHours());
        dialogValidate();
    }

    private String getHours(){
        LocalDateTime start = LocalDateTime.of(dialogStartDate.getValue(),dialogStartTime.getValue());
        LocalDateTime end = LocalDateTime.of(dialogStartDate.getValue(),dialogEndTime.getValue());
        Duration dur = Duration.between(start, end);
        long millis = dur.toMillis();

        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)));
    }


    public void dialogOpen(SchedulerEvent event){
        validationEnabled = Boolean.FALSE;
        editType = Scheduler.EditType.DIALOG;
        driver = null;
        if(event==null){ //invalid event
            log.info("dialogOpen: null event passed");
            //TODO: failed - no event
            return;
        }else{
            this.event = event;
            if(this.event.getResourceId().equals(Scheduler.availableShiftsResourceId)){
                //leave driver as null = unassigned
            }else{
                log.info("dialogOpen: lookup driver by id:" + this.event.getResourceId());
                Long driverID = Long.valueOf(this.event.getResourceId());
                if(driverID!=null){
                    driver = driversRepository.findDriverByFleetId(driverID);
                }
                log.info("dialogOpen: AFTER lookup driver by id:" + this.event.getResourceId() + " driver:" + driver);
            }
        }

        setValues();
        dialogDriver.setReadOnly(false);
        dialogEventType.setReadOnly(false);
        dialogStartDate.setReadOnly(false);
        dialogDurationType.setReadOnly(false);
        dialogStartTime.setReadOnly(false);
        dialogEndTime.setReadOnly(false);
        dialogPublished.setReadOnly(true);
        if(dialogMode.equals(DialogMode.NEW)){
            dialogDeleteButton.setEnabled(false);
            dialogDeleteButton.setVisible(false);
            dialog.setHeaderTitle("Create schedule entry");
        }else { //View/Edit/Delete
            dialogDeleteButton.setEnabled(true);
            dialogDeleteButton.setVisible(true);
            dialog.setHeaderTitle("View/Edit schedule entry");
        }
        validationEnabled = Boolean.TRUE;

            //validate
        dialogValidate();
        dialog.open();
    }

    private void setValues(){
        //set values
        dialogDriver.setValue(driver);

        dialogEventType.setValue(event.getType());
        updateEventType();

        dialogStartDate.setValue(this.event.getStart().toLocalDate());

        log.info("setValues: fullDay:" + event.getFullDay());
        if(event.getFullDay()){
            dialogDurationType.setValue(Scheduler.EventDurationType.FULLDAY);
        }else{
            dialogDurationType.setValue(Scheduler.EventDurationType.PARTIALDAY);
        }
        updateDurationType();

        dialogStartTime.setValue(this.event.getStart().toLocalTime());
        dialogEndTime.setValue(this.event.getEnd().toLocalTime());
        dialogHours.setValue(this.event.getHours());
        dialogPublished.setValue(this.event.getPublished());
    }

    private void dialogValidate() {
        if(validationEnabled && this.event!=null){
            if(dialogMode.equals(DialogMode.NEW)){
                hasChangedValues = Boolean.TRUE;
            }else{
                hasChangedValues = Boolean.FALSE;
                validateDriver();
                validateRadioButtonGroup(dialogEventType, event.getType().toString());
                validateDateField(dialogStartDate, event.getStart().toLocalDate());
                Scheduler.EventDurationType durationType;
                if(event.getFullDay()){
                    durationType = Scheduler.EventDurationType.FULLDAY;
                }else{
                    durationType = Scheduler.EventDurationType.PARTIALDAY;
                }
                validateRadioButtonGroup(dialogDurationType,durationType.toString());
                validateTimeField(dialogStartTime, event.getStart().toLocalTime());
                validateTimeField(dialogEndTime, event.getEnd().toLocalTime());
            }
        }
        log.info("dialogValidate: hasChangedValues:" + hasChangedValues);
        if(hasChangedValues){
            enableOkReset(true);
            //dialogOkButton.setEnabled(true);
            //dialogResetButton.setEnabled(true);
        }else{
            enableOkReset(false);
            //dialogOkButton.setEnabled(false);
            //dialogResetButton.setEnabled(false);
        }

    }

    private void validateTimeField(TimePicker field, LocalTime value){
        if(value==null && field.getValue()==null){
            field.getStyle().set("box-shadow","none");
        }else if(value==null && field.getValue()!=null){
            field.getStyle().set("box-shadow",UIUtilities.boxShadowStyle);
            field.getStyle().set("border-radius",UIUtilities.boxShadowStyleRadius);
            hasChangedValues = Boolean.TRUE;
        }else if(field.getValue().equals(value)){
            field.getStyle().set("box-shadow","none");
        }else{
            field.getStyle().set("box-shadow",UIUtilities.boxShadowStyle);
            field.getStyle().set("border-radius",UIUtilities.boxShadowStyleRadius);
            hasChangedValues = Boolean.TRUE;
        }
    }

    private void validateDateField(DatePicker field, LocalDate value){
        if(value==null && field.getValue()==null){
            field.getStyle().set("box-shadow","none");
        }else if(value==null && field.getValue()!=null){
            field.getStyle().set("box-shadow",UIUtilities.boxShadowStyle);
            field.getStyle().set("border-radius",UIUtilities.boxShadowStyleRadius);
            hasChangedValues = Boolean.TRUE;
        }else if(field.getValue().equals(value)){
            field.getStyle().set("box-shadow","none");
        }else{
            field.getStyle().set("box-shadow",UIUtilities.boxShadowStyle);
            field.getStyle().set("border-radius",UIUtilities.boxShadowStyleRadius);
            hasChangedValues = Boolean.TRUE;
        }
    }

    private void validateCheckbox(Checkbox field, Boolean value){
        if(field.getValue().equals(value)){
            field.getStyle().set("box-shadow","none");
        }else{
            field.getStyle().set("box-shadow",UIUtilities.boxShadowStyle);
            field.getStyle().set("border-radius",UIUtilities.boxShadowStyleRadius);
            hasChangedValues = Boolean.TRUE;
        }
    }

    private void validateRadioButtonGroup(RadioButtonGroup field, String value){
        //log.info("validateRadioButtonGroup: field toString:" + field.getValue().toString() + " value:" + value);
        if(field.getValue().toString().equals(value)){
            field.getStyle().set("box-shadow","none");
        }else{
            field.getStyle().set("box-shadow",UIUtilities.boxShadowStyle);
            field.getStyle().set("border-radius",UIUtilities.boxShadowStyleRadius);
            hasChangedValues = Boolean.TRUE;
        }
    }

    private void validateDriver(){
        String value = event.getResourceId();
        String field = null;
        if(dialogDriver.getValue()!=null){
            field = dialogDriver.getValue().getFleetId().toString();
        }
        log.info("validateDriver: field:" + field + " value:" + value);
        if(value==null && field==null){
            dialogDriver.getStyle().set("box-shadow","none");
        }else if(value==null && field!=null || value!=null && field==null){
            dialogDriver.getStyle().set("box-shadow",UIUtilities.boxShadowStyle);
            dialogDriver.getStyle().set("border-radius",UIUtilities.boxShadowStyleRadius);
            hasChangedValues = Boolean.TRUE;
        }else if(field.equals(value)){
            dialogDriver.getStyle().set("box-shadow","none");
        }else{
            dialogDriver.getStyle().set("box-shadow",UIUtilities.boxShadowStyle);
            dialogDriver.getStyle().set("border-radius",UIUtilities.boxShadowStyleRadius);
            hasChangedValues = Boolean.TRUE;
        }
    }



    public DialogMode getDialogMode() {
        return dialogMode;
    }

    public void setDialogMode(DialogMode dialogMode) {
        this.dialogMode = dialogMode;
    }

    public Scheduler.EditType getEditType() {
        return editType;
    }

    public void setEditType(Scheduler.EditType editType) {
        this.editType = editType;
    }
}
