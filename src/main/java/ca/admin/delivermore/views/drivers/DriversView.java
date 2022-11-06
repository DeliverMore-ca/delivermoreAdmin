package ca.admin.delivermore.views.drivers;

import ca.admin.delivermore.collector.config.BatchConfigDrivers;
import ca.admin.delivermore.collector.data.service.DriversRepository;
import ca.admin.delivermore.collector.data.service.RestClientService;
import ca.admin.delivermore.collector.data.tookan.Driver;
import ca.admin.delivermore.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.List;


@PageTitle("Drivers")
@Route(value = "drivers", layout = MainLayout.class)
@AnonymousAllowed
public class DriversView extends VerticalLayout {

    RestClientService restClientService;
    DriversRepository driversRepository;

    public DriversView(DriversRepository driversRepository, RestClientService restClientService) {
        this.driversRepository = driversRepository;
        this.restClientService = restClientService;
        // crud instance
        GridCrud<Driver> crud = new GridCrud<>(Driver.class);

        // grid configuration
        //crud.getGrid().setColumns("fleetId", "isActive", "username", "name", "loginId", "email", "phone");
        crud.getGrid().removeAllColumns();
        crud.getGrid().addColumn(Driver::getName).setHeader("Name");
        crud.getGrid().addColumn(Driver::getIsActivePresentation).setHeader("IsActive");
        crud.getGrid().addColumn(Driver::getEmail).setHeader("Email");
        crud.getGrid().addColumn(Driver::getPhone).setHeader("Phone");
        crud.getGrid().addColumn(Driver::getUsername).setHeader("Username");
        crud.getGrid().addColumn(Driver::getLoginId).setHeader("LoginId");
        crud.getGrid().addColumn(Driver::getFleetId).setHeader("FleetId");
        crud.getGrid().setColumnReorderingAllowed(true);

        //Enable/Disable operations
        crud.setAddOperationVisible(false);
        crud.setDeleteOperationVisible(false);
        crud.setUpdateOperationVisible(true);
        crud.setFindAllOperationVisible(false);

        Button refreshFromTookanButton = new Button("Refresh from Tookan");
        refreshFromTookanButton.setDisableOnClick(true);
        refreshFromTookanButton.addClickListener(e -> {
            refreshDrivers();
            crud.refreshGrid();
            refreshFromTookanButton.setEnabled(true);
        });
        crud.getCrudLayout().addToolbarComponent(refreshFromTookanButton);
        //crud.setUpdateButtonColumnEnabled(true);

        // form configuration
        crud.getCrudFormFactory().setUseBeanValidation(true);
        crud.getCrudFormFactory().setVisibleProperties(
                "fleetId", "isActive", "username", "name", "loginId", "email", "phone");
        crud.getCrudFormFactory().setVisibleProperties(
                CrudOperation.UPDATE,
                "isActive", "username", "name", "loginId", "email", "phone");

        crud.getGrid().getColumns().forEach(col -> col.setAutoWidth(true));
        crud.getGrid().addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        // layout configuration
        setSizeFull();
        add(crud);

        // logic configuration
        crud.setOperations(
                () -> driversRepository.findAll(),
                driver -> driversRepository.save(driver),
                driver -> {
                    /* sample validate on update
                    if(user.getId().equals(10L)) {
                        throw new CrudOperationException("Simulated error.");
                    }
                     */
                    return driversRepository.save(driver);
                },
                driver -> driversRepository.delete(driver)
        );
    }

    private void refreshDrivers(){
        List<Driver> currentDriverList = restClientService.getAllDrivers();
        driversRepository.saveAll(currentDriverList);
        List<Driver> allDriverList = driversRepository.findAll();
        for (Driver driver : allDriverList) {
            driver.updateDriverIsActive(driver,currentDriverList);
        }

    }

}
