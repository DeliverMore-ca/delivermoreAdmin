package ca.admin.delivermore.views.report;

import ca.admin.delivermore.collector.data.Utility;
import ca.admin.delivermore.data.report.*;
import ca.admin.delivermore.views.MainLayout;
import ca.admin.delivermore.views.UIUtilities;
import com.vaadin.componentfactory.DateRange;
import com.vaadin.componentfactory.EnhancedDateRangePicker;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Period Summary")
@Route(value = "periodsummary", layout = MainLayout.class)
@AnonymousAllowed
public class PeriodSummaryView extends Main {

    private Logger log = LoggerFactory.getLogger(PeriodSummaryView.class);
    private VerticalLayout detailsLayout = new VerticalLayout();
    private EnhancedDateRangePicker rangeDatePicker = new EnhancedDateRangePicker("Select summary period:");
    RestPayoutSummary restPayoutSummary;
    DriverPayoutPeriod driverPayoutPeriod;

    private MissingPOSDataDetails missingPOSDataDetails = new MissingPOSDataDetails();

    LocalDate startDate;
    LocalDate endDate;

    public PeriodSummaryView() {
        configureDatePicker();
        startDate = rangeDatePicker.getValue().getStartDate();
        endDate = rangeDatePicker.getValue().getEndDate();
        buildPeriodDetails();
        add(getToolbar(), getContent());

    }

    private HorizontalLayout getToolbar() {
        HorizontalLayout toolbar = new HorizontalLayout(rangeDatePicker);
        toolbar.setPadding(true);
        toolbar.setAlignItems(FlexComponent.Alignment.BASELINE);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private Component getContent() {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.add(detailsLayout);
        detailsLayout.setSizeFull();
        return mainLayout;
    }

    private void configureDatePicker() {
        LocalDate defaultDate = LocalDate.parse("2022-08-14");

        //get lastWeek as the default for the range picker
        LocalDate nowDate = LocalDate.now();
        LocalDate prevSun = nowDate.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        prevSun = nowDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate startOfLastWeek = prevSun.minusWeeks(1);
        LocalDate endOfLastWeek = startOfLastWeek.plusDays(6);

        rangeDatePicker.setPattern("yyyy-MM-dd");
        rangeDatePicker.setParsers("yyyy-MM-dd");

        rangeDatePicker.setMin(defaultDate);
        rangeDatePicker.setValue(new DateRange(startOfLastWeek,endOfLastWeek));
        rangeDatePicker.addValueChangeListener(e -> {
            log.info("configureDatePicker: called: range:" + rangeDatePicker.getValue());
            startDate = rangeDatePicker.getValue().getStartDate();
            endDate = rangeDatePicker.getValue().getEndDate();
            if(startDate!=null){
                buildPeriodDetails();
            }
        });
    }

    private void buildPeriodDetails() {
        detailsLayout.removeAll();
        if(endDate==null) endDate = startDate;

        log.info("buildPeriodDetails: start:" + startDate + " end:" + endDate);

        //build the restaurant sales summary so we can use the totals
        restPayoutSummary = new RestPayoutSummary(startDate,endDate, Boolean.FALSE);

        //build the driver payout for the period summary
        String location = "Strathmore";
        driverPayoutPeriod = new DriverPayoutPeriod(location, startDate, endDate);

        //add a highlevel summary at the top of the income without expenses

        //add each section
        detailsLayout.add(buildTopSummary());
        detailsLayout.add(missingPOSDataDetails.buildMissingPOSData(startDate,endDate));
        detailsLayout.add(buildRestSaleSummary());
        detailsLayout.add(buildDriverPayoutSummary());

    }

    private VerticalLayout buildTopSummary() {
        VerticalLayout topSummary = UIUtilities.getVerticalLayout();
        HorizontalLayout topSummaryRow = UIUtilities.getHorizontalLayout(false,true,false);
        Double cogsSalesTotal = restPayoutSummary.getCOGS() - restPayoutSummary.getDeliveryFeeFromExternal();

        Double grossProfit = restPayoutSummary.getRestSaleSummary().getFundsTotal() - cogsSalesTotal - driverPayoutPeriod.getDriverCost() - driverPayoutPeriod.getTip() - restPayoutSummary.getPayoutTaxes();
        NumberField grossProfitField = UIUtilities.getNumberField("Gross profit", Utility.getInstance().round(grossProfit,2));

        String fieldWidth = "130px";
        NumberField totalFundsField = UIUtilities.getNumberField("Total Funds",restPayoutSummary.getRestSaleSummary().getFundsTotal());
        totalFundsField.setWidth(fieldWidth);
        NumberField totalSalesCOGSField = UIUtilities.getNumberField("Total Sales COGS",cogsSalesTotal);
        totalSalesCOGSField.setWidth(fieldWidth);
        NumberField driverCostField = UIUtilities.getNumberField("Driver COGS",driverPayoutPeriod.getDriverCost());
        driverCostField.setWidth(fieldWidth);
        NumberField driverTipsField = UIUtilities.getNumberField("Driver Tips",driverPayoutPeriod.getTip());
        driverTipsField.setWidth(fieldWidth);
        //use payoutTaxes as taxes includes places like Smiley's where the taxes have already been paid direct under Wise
        NumberField salesTaxPayableField = UIUtilities.getNumberField("Sales Tax",restPayoutSummary.getPayoutTaxes());
        salesTaxPayableField.setWidth(fieldWidth);
        topSummaryRow.setVerticalComponentAlignment(FlexComponent.Alignment.END);
        topSummaryRow.add(totalFundsField,totalSalesCOGSField,driverCostField,driverTipsField,salesTaxPayableField);

        HorizontalLayout topCOGSSummaryRow = UIUtilities.getHorizontalLayout(false,true,false);
        NumberField totalSalesCOGSSummaryField = UIUtilities.getNumberField("Total Sales COGS",cogsSalesTotal);
        totalSalesCOGSSummaryField.setWidth(fieldWidth);
        NumberField salesCOGSField = UIUtilities.getNumberField("Sales COGS",restPayoutSummary.getCOGS());
        salesCOGSField.setWidth(fieldWidth);
        NumberField cogsReductionDelFeeFromExternalField = UIUtilities.getNumberField("Fee from External",restPayoutSummary.getDeliveryFeeFromExternal());
        cogsReductionDelFeeFromExternalField.setWidth(fieldWidth);
        topCOGSSummaryRow.add(totalSalesCOGSSummaryField,salesCOGSField,cogsReductionDelFeeFromExternalField);

        String label = "Gross Profit before Expenses (" + restPayoutSummary.getRestSaleSummary().getCount() + " sales):";
        topSummary.add(new Label(label));
        topSummary.add(grossProfitField,topSummaryRow,topCOGSSummaryRow);
        return topSummary;
    }

    private Details buildRestSaleSummary() {
        Details restSaleSummaryDetails = UIUtilities.getDetails();
        VerticalLayout summaryHeader = UIUtilities.getVerticalLayout();
        HorizontalLayout summaryHeaderRow = UIUtilities.getHorizontalLayout();
        VerticalLayout summaryHeaderCol1 = UIUtilities.getVerticalLayout();
        VerticalLayout summaryHeaderCol2 = UIUtilities.getVerticalLayout();
        VerticalLayout summaryHeaderCol3 = UIUtilities.getVerticalLayout();
        NumberField totalSaleField= UIUtilities.getNumberField("Total Sale",restPayoutSummary.getRestSaleSummary().getSalesTotal());
        NumberField saleField= UIUtilities.getNumberField("Sale",restPayoutSummary.getRestSaleSummary().getSale());
        NumberField taxField= UIUtilities.getNumberField("Tax",restPayoutSummary.getRestSaleSummary().getTax());
        NumberField deliveryFeeField= UIUtilities.getNumberField("Del Fee",restPayoutSummary.getRestSaleSummary().getDeliveryFee());
        NumberField serviceFeeField= UIUtilities.getNumberField("Srv Fee",restPayoutSummary.getRestSaleSummary().getServiceFee());
        NumberField tipField= UIUtilities.getNumberField("Tips",restPayoutSummary.getRestSaleSummary().getTip());
        NumberField totalFundsField= UIUtilities.getNumberField("Total Funds",restPayoutSummary.getRestSaleSummary().getFundsTotal());
        NumberField cashSaleField= UIUtilities.getNumberField("Cash Sale",restPayoutSummary.getRestSaleSummary().getCashSale());
        NumberField cardSaleField= UIUtilities.getNumberField("Card Sale",restPayoutSummary.getRestSaleSummary().getCardSale());
        NumberField onlineSaleField= UIUtilities.getNumberField("Online Sale",restPayoutSummary.getRestSaleSummary().getOnlineSale());
        NumberField owingToVendorField = UIUtilities.getNumberField("Owing to vendors",restPayoutSummary.getOwingToVendor());
        NumberField cogsField = UIUtilities.getNumberField("COGS",restPayoutSummary.getCOGS());
        summaryHeaderCol1.add(totalSaleField,saleField,taxField,deliveryFeeField,serviceFeeField,tipField);
        summaryHeaderCol2.add(totalFundsField,cashSaleField,cardSaleField,onlineSaleField);
        summaryHeaderCol3.add(owingToVendorField,cogsField);
        String label = "Sales Summary (" + restPayoutSummary.getRestSaleSummary().getCount() + " sales):";
        summaryHeader.add(new Label(label),summaryHeaderRow);
        summaryHeaderRow.add(summaryHeaderCol1,summaryHeaderCol2,summaryHeaderCol3);
        restSaleSummaryDetails.setSummary(summaryHeader);
        Grid<RestPayoutPeriod> grid = new Grid<>();
        restSaleSummaryDetails.setContent(grid);
        grid.setItems(restPayoutSummary.getRestPayoutPeriodList());
        grid.setWidthFull();
        grid.setAllRowsVisible(true);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setColumnReorderingAllowed(true);
        grid.addComponentColumn(item -> {
            Icon editIcon = new Icon("lumo", "edit");
            FileDownloadWrapper downloadWrapper = new FileDownloadWrapper(
                    new StreamResource(item.getPdfFileName(), () -> {
                        try {
                            return new ByteArrayInputStream(Files.readAllBytes( item.getPdfFile().toPath()));
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    })
            );

            downloadWrapper.wrapComponent(editIcon);

            editIcon.addClickListener(e -> {
                File appPath = new File(System.getProperty("user.dir"));
                File outputDir = new File(appPath,"tozip");
                Utility.emptyDir(outputDir);
                try {
                    Files.createDirectory(outputDir.toPath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                item.createStatement();
            });
            return downloadWrapper;
        }).setWidth("75px").setFlexGrow(0).setFrozen(true);
        grid.addColumn(RestPayoutPeriod::getRestaurantName).setHeader("Restaurant").setFrozen(true);
        grid.addColumn(item -> item.getRestSaleSummary().getSalesTotal()).setHeader("Sale Total");
        grid.addColumn(item -> item.getRestSaleSummary().getFundsTotal()).setHeader("Funds Total");
        grid.addColumn(item -> item.getRestSaleSummary().getSalesMinusFundsTotal()).setHeader("Diff");
        grid.addColumn(item -> item.getRestSaleSummary().getCount()).setHeader("Count");
        grid.addColumn(item -> item.getRestSaleSummary().getSale()).setHeader("Sale");
        grid.addColumn(item -> item.getRestSaleSummary().getTax()).setHeader("Tax");
        grid.addColumn(item -> item.getRestSaleSummary().getDeliveryFee()).setHeader("Del Fee");
        grid.addColumn(item -> item.getRestSaleSummary().getServiceFee()).setHeader("Srv Fee");
        grid.addColumn(item -> item.getRestSaleSummary().getTip()).setHeader("Tip");
        grid.addColumn(item -> item.getRestSaleSummary().getCashSale()).setHeader("Cash Sale");
        grid.addColumn(item -> item.getRestSaleSummary().getCardSale()).setHeader("Card Sale");
        grid.addColumn(item -> item.getRestSaleSummary().getOnlineSale()).setHeader("Online Sale");
        grid.addColumn(RestPayoutPeriod::getOwingToVendor).setHeader("Owing to Vendor");
        grid.addColumn(RestPayoutPeriod::getPaidToVendor).setHeader("Paid to Vendor");
        grid.addColumn(RestPayoutPeriod::getCOGS).setHeader("COGS");
        return restSaleSummaryDetails;
    }

    private Details buildDriverPayoutSummary() {
        Details driverPayoutSummaryDetails = UIUtilities.getDetails();
        HorizontalLayout summaryHeader = UIUtilities.getHorizontalLayout();
        NumberField driverCostField = UIUtilities.getNumberField("Cost",driverPayoutPeriod.getDriverCost());
        NumberField driverPayField = UIUtilities.getNumberField("Pay",driverPayoutPeriod.getDriverPay());
        NumberField driverAdjField = UIUtilities.getNumberField("Adj",driverPayoutPeriod.getDriverAdjustment());
        NumberField driverCashField = UIUtilities.getNumberField("Cash",driverPayoutPeriod.getDriverCash());
        NumberField driverTipsfield = UIUtilities.getNumberField("Tips",driverPayoutPeriod.getTip());
        NumberField driverPayoutField = UIUtilities.getNumberField("Payout",driverPayoutPeriod.getDriverPayout());
        summaryHeader.add(new Label("Driver Cost:"),driverCostField,driverPayField,driverAdjField,driverCashField,driverTipsfield,driverPayoutField);
        driverPayoutSummaryDetails.setSummary(summaryHeader);
        Grid<DriverPayoutWeek> grid = new Grid<>();
        driverPayoutSummaryDetails.setContent(grid);
        grid.setItems(driverPayoutPeriod.getDriverPayoutWeekList());
        grid.setWidthFull();
        grid.setAllRowsVisible(true);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setColumnReorderingAllowed(true);
        grid.addColumn(DriverPayoutWeek::getFleetName).setHeader("Driver");
        grid.addColumn(DriverPayoutWeek::getDriverCostFmt).setHeader("COGS").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(DriverPayoutWeek::getDriverPayFmt).setHeader("Pay").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(DriverPayoutWeek::getDriverAdjustmentFmt).setHeader("Adjustment").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(DriverPayoutWeek::getDriverCashFmt).setHeader("Cash").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(DriverPayoutWeek::getTipFmt).setHeader("Tips").setTextAlign(ColumnTextAlign.END);
        grid.addColumn(DriverPayoutWeek::getDriverPayoutFmt).setHeader("Payout").setTextAlign(ColumnTextAlign.END);
        return driverPayoutSummaryDetails;

    }


}
