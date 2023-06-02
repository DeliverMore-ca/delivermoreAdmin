package ca.admin.delivermore.data.report;

import ca.admin.delivermore.collector.data.entity.Restaurant;
import ca.admin.delivermore.collector.data.entity.TaskEntity;
import ca.admin.delivermore.collector.data.service.RestaurantRepository;
import ca.admin.delivermore.collector.data.service.TaskDetailRepository;
import ca.admin.delivermore.components.custom.ValidationMessage;
import ca.admin.delivermore.data.service.Registry;
import ca.admin.delivermore.views.UIUtilities;
import com.vaadin.componentfactory.DateRange;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MissingPOSDataDetails {

    private Logger log = LoggerFactory.getLogger(MissingPOSDataDetails.class);
    private TaskEditDialog taskEditDialog;
    private List<TaskEntity> missingPOSDataList = new ArrayList<>();
    private Grid<TaskEntity> missingPOSDataGrid;
    private TaskDetailRepository taskDetailRepository;
    private RestaurantRepository restaurantRepository;

    public MissingPOSDataDetails() {
        taskDetailRepository = Registry.getBean(TaskDetailRepository.class);
        restaurantRepository = Registry.getBean(RestaurantRepository.class);
        taskEditDialog = new TaskEditDialog();
    }

    public Details buildMissingPOSData(LocalDate periodStart, LocalDate periodEnd){
        Details missingGlobalDetails = UIUtilities.getDetails();
        missingGlobalDetails.addThemeVariants(DetailsVariant.FILLED);
        missingGlobalDetails.setSizeUndefined();
        //missingGlobalDetails.setWidthFull();
        List<Restaurant> allRestSettings = restaurantRepository.getRestaurantsGlobalPos();
        if(allRestSettings==null || allRestSettings.size()==0){
            missingGlobalDetails.setSummaryText("No Missing Global POS Paid To Vendor Data");
        }
        else{
            missingPOSDataList.clear();
            for (Restaurant restSetting: allRestSettings) {
                //check if the setting is in effect
                if(restSetting.getDateExpired()!=null && restSetting.getDateExpired().isBefore(periodStart)){
                    //log.info("  **: expired BEFORE: pStart:" + periodStart + " pEnd:" + periodEnd + " sEffective:" + restSetting.getDateEffective() + " sExpiry:" + restSetting.getDateExpired());
                    continue; //skip this one as it's expired
                }else if(restSetting.getDateEffective().isAfter(periodEnd)){
                    //log.info("  **: effective AFTER: pStart:" + periodStart + " pEnd:" + periodEnd + " sEffective:" + restSetting.getDateEffective() + " sExpiry:" + restSetting.getDateExpired());
                    continue; //skip as this one is not yet in effect
                }
                //this setting should be in effect - determine the start/end
                LocalDate thisStart;
                LocalDate thisEnd;
                if(restSetting.getDateEffective().isBefore(periodStart)){
                    thisStart = periodStart;
                }else{
                    thisStart = restSetting.getDateEffective();
                }
                if(restSetting.getDateExpired()==null || restSetting.getDateExpired().isAfter(periodEnd)){
                    thisEnd = periodEnd;
                }else{
                    thisEnd = restSetting.getDateExpired();
                }
                //log.info("  **: within period: pStart:" + periodStart + " pEnd:" + periodEnd + " sEffective:" + restSetting.getDateEffective() + " sExpiry:" + restSetting.getDateExpired());
                //log.info("  **: within period: thisStart:" + thisStart + " thisEnd:" + thisEnd);
                List<TaskEntity> tasks = taskDetailRepository.getTaskEntityByDateMissingPOSInfo(thisStart.atStartOfDay(),thisEnd.atTime(23,59,59),restSetting.getRestaurantId());
                if(tasks!=null){
                    missingPOSDataList.addAll(tasks);
                }
            }
            //sort list by date
            missingPOSDataList.sort(Comparator.comparing(TaskEntity::getCreationDate));

            if(missingPOSDataList.size()>0){
                missingGlobalDetails.setSummaryText("Missing Global POS Paid To Vendor Data (" + missingPOSDataList.size() + " items)");
                missingGlobalDetails.setOpened(true);
            }else{
                missingGlobalDetails.setSummaryText("No Missing Global POS Paid To Vendor Data");
            }
            if(missingPOSDataList.size()>0){
                VerticalLayout missingGlobalGridLayout = UIUtilities.getVerticalLayout();
                missingPOSDataGrid = new Grid<>();
                missingPOSDataGrid.setWidthFull();
                missingPOSDataGrid.setAllRowsVisible(true);
                missingPOSDataGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
                missingPOSDataGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
                missingPOSDataGrid.setItems(missingPOSDataList);
                missingPOSDataGrid.addColumn(TaskEntity::getRestaurantName).setHeader("Restaurant");
                missingPOSDataGrid.addColumn(new LocalDateTimeRenderer<>(TaskEntity::getCreationDate,"MM/dd HH:mm")).setHeader("Date/Time");
                missingPOSDataGrid.addColumn(TaskEntity::getFleetName).setHeader("Driver");
                Grid.Column<TaskEntity> paidToVendorColumn = missingPOSDataGrid.addColumn(TaskEntity::getPaidToVendor).setHeader("Paid to Vendor");
                missingPOSDataGrid.addColumn(TaskEntity::getGlobalSubtotal).setHeader("Subtotal");
                missingPOSDataGrid.addColumn(TaskEntity::getGlobalTotalTaxes).setHeader("Taxes");
                missingPOSDataGrid.addColumn(TaskEntity::getTotalSale).setHeader("Total Sale");
                missingPOSDataGrid.addColumn(TaskEntity::getPaymentMethod).setHeader("Method");
                missingPOSDataGrid.addColumn(TaskEntity::getJobId).setHeader("JobId");
                missingPOSDataGrid.addColumn(TaskEntity::getOrderId).setHeader("OrderId");
                missingPOSDataGrid.getColumns().forEach(col -> col.setAutoWidth(true));

                missingPOSDataGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

                Binder<TaskEntity> binder = new Binder<>(TaskEntity.class);
                Editor<TaskEntity> editor = missingPOSDataGrid.getEditor();
                editor.setBinder(binder);
                editor.setBuffered(true);

                ValidationMessage paidToVendorValidationMessage = new ValidationMessage();

                NumberField paidToVendorField = new NumberField();
                paidToVendorField.setWidthFull();

                binder.forField(paidToVendorField)
                        .asRequired("Enter a valid number")
                        .withStatusLabel(paidToVendorValidationMessage)
                        //.withValidator(new DoubleRangeValidator("Enter a number above 0",0.0,99999.99))
                        .bind(TaskEntity::getPaidToVendor, TaskEntity::setPaidToVendor);
                paidToVendorColumn.setEditorComponent(paidToVendorField);

                paidToVendorField.getElement().addEventListener("keydown", e -> {
                    //log.info("Escape pressed");
                            editor.cancel();
                        })
                        .setFilter("event.code === 'Escape'");

                paidToVendorField.addBlurListener(e -> {
                   //logEvent("Blur",editor.getItem());
                   if(editor.getItem()==null){
                       //log.info("Blur with null item so cancel editor");
                       editor.cancel();
                   }else{
                       if(e.getSource().getValue()==null){
                           //log.info("Blur: paid to vendor field null so cancelling save: item:" + editor.getItem().getJobId());
                           editor.cancel();
                       }else{
                           //log.info("Blur: save item here: item:" + editor.getItem().getJobId());
                           editor.save();
                       }
                   }
                });

                missingPOSDataGrid.addSelectionListener(e -> {
                    //log.info("SelectionListener: called");
                    if(e.getAllSelectedItems().size()>0){
                        //log.info("SelectionListener: size:" + e.getAllSelectedItems().size());
                        if(e.getFirstSelectedItem().get()!=null){
                            logEvent("Selected:", e.getFirstSelectedItem().get());
                            //check if another item is already in edit mode and cancel it
                            if(editor.isOpen()) editor.cancel();
                            editor.editItem(e.getFirstSelectedItem().get());
                            Component editorComponent = paidToVendorColumn.getEditorComponent();
                            if (editorComponent instanceof Focusable) {
                                //log.info("Click setting focus");
                                ((Focusable) editorComponent).focus();
                            }
                        }
                    }
                });

                editor.addSaveListener(e -> {
                    logEvent("Save", e.getItem());
                    taskDetailRepository.save(e.getItem());
                    //missingPOSDataGrid.getDataProvider().refreshAll();
                    missingPOSDataGrid.getDataProvider().refreshItem(e.getItem());
                    //move to next item if any
                    /*
                    TaskEntity nextItem = missingPOSDataGrid.getListDataView().getNextItem(e.getItem()).stream().findFirst().orElse(null);
                    log.info("Save completed: nextItem:" + nextItem);
                    if(nextItem!=null){
                        log.info("Save completed: selecting nextItem:" + nextItem);
                        missingPOSDataGrid.select(nextItem);
                    }

                     */
                });

                editor.addCancelListener(e -> {
                    logEvent("Cancel", e.getItem());
                    paidToVendorValidationMessage.setText("");
                });

                missingGlobalGridLayout.add(missingPOSDataGrid);
                missingGlobalDetails.setContent(missingGlobalGridLayout);
            }

        }
        return missingGlobalDetails;
    }

    private void logEvent(String eventName, TaskEntity taskEntity){
        if(taskEntity==null){
            log.info(eventName + " called: item is null");
        }else{
            log.info(eventName + " called: item:" + taskEntity.getJobId() + " value:" + taskEntity.getPaidToVendor());
        }

    }

    public TaskEditDialog getTaskEditDialog() {
        return taskEditDialog;
    }

}
