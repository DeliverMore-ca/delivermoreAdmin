package ca.admin.delivermore.data.report;

import ca.admin.delivermore.collector.data.Utility;
import ca.admin.delivermore.collector.data.entity.Restaurant;
import ca.admin.delivermore.collector.data.service.EmailService;
import ca.admin.delivermore.collector.data.service.RestaurantRepository;
import ca.admin.delivermore.data.entity.RestAdjustment;
import ca.admin.delivermore.data.service.Registry;
import ca.admin.delivermore.views.UIUtilities;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vaadin.componentfactory.DateRange;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.server.StreamResource;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.*;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.ZipOutputStream;

public class RestPayoutSummary {

    Map<Long, RestPayoutPeriod> restPayoutPeriodMap = new TreeMap<>();
    List<RestPayoutPeriod> restPayoutPeriodList = new ArrayList<>();
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private Double sale = 0.0;
    private Double taxes = 0.0;
    private Double totalSale = 0.0;
    private Integer itemCount = 0;

    private Double payoutSale = 0.0;
    private Double payoutTaxes = 0.0;
    private Double payoutTotalSale = 0.0;
    private Integer payoutItemCount = 0;

    private Double paidSale = 0.0;
    private Double paidTotalSale = 0.0;
    private Integer paidItemCount = 0;

    private Double directTotalSale = 0.0;
    private Double phoneInTotalSale = 0.0;
    private Integer directItemCount = 0;
    private Integer phoneInItemCount = 0;

    private Double deliveryFeeFromVendor = 0.0;
    private Double deliveryFeeFromExternal = 0.0;
    private Double commissionForPayout = 0.0;
    private Double commissionPerDelivery = 0.0;
    private Double adjustment = 0.0;
    private Double owingToVendor = 0.0;

    private VerticalLayout mainLayout = new VerticalLayout();
    private RestaurantRepository restaurantRepository;
    private List<RestAdjustment> restAdjustmentList = new ArrayList<>();
    private List<RestPayoutItem> restPayoutItemList = new ArrayList<>();
    private String[] columnsUpper;
    private String[] columnsUpperAdjustments;

    private List<RestPayoutFromExternalVendor> restPayoutFromExternalVendorList = new ArrayList<>();

    private RestPayoutAdjustmentDialog adjustmentDialog;
    private Details summaryDetails;
    private Grid<RestPayoutPeriod> restGrid;
    private Grid<RestAdjustment> grid;
    private Grid<PayoutDocument> periodDocumentsGrid;
    private VerticalLayout summaryDetailsSummary;
    private VerticalLayout summaryDetailsContent;
    private File csvFile = null;
    private File csvAdjustmentsFile = null;
    private File summaryFile = null;
    private List<PayoutDocument> payoutDocumentList = new ArrayList<>();

    //TODO: move these to Utilities
    private File appPath = new File(System.getProperty("user.dir"));
    private File outputDir = new File(appPath,"tozip");

    private Resource resourcePayStatementTemplate;
    @Autowired
    private EmailService emailService;

    public RestPayoutSummary(LocalDate periodStart, LocalDate periodEnd) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        restaurantRepository = Registry.getBean(RestaurantRepository.class);
        this.resourcePayStatementTemplate = new ClassPathResource("Rest_SummaryPayStatement_Template.docx");
        emailService = Registry.getBean(EmailService.class);
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setMargin(false);
        adjustmentDialog = new RestPayoutAdjustmentDialog(periodStart);
        configureRestPayoutItemColumns();
        configureRestAdjustmentsColumns();
        buildSummary();
        buildSummaryLayout();
        buildRestaurantLayout();
    }

    public void refresh(){
        updateAdjustment();
        summaryDetailsSummary.removeAll();
        summaryDetailsSummary = buildSummaryDetailsSummary();
        restGrid.getDataProvider().refreshAll();
        grid.getDataProvider().refreshAll();
        payoutDocumentList.clear();
        periodDocumentsGrid.getDataProvider().refreshAll();
    }

    private void buildRestaurantLayout() {
        for (RestPayoutPeriod restPayoutPeriod: restPayoutPeriodList) {
            mainLayout.add(restPayoutPeriod.getMainLayout());
        }
    }

    /*
    * List all vendors that need to be invoiced for refund of delivery fees (example Opa! corporate)
     */
    private VerticalLayout buildExternalInvoiceList() {
        //list external vendors here
        VerticalLayout externalVendorLayout = UIUtilities.getVerticalLayout();
        if(restPayoutFromExternalVendorList.size()>0){
            //Header
            Label label = new Label("Invoice needed for external vendors");
            label.getElement().getStyle().set("font-weight", "bold");
            externalVendorLayout.add(label);
            //List of external vendor invoices
            for (RestPayoutFromExternalVendor restPayoutFromExternalVendor: restPayoutFromExternalVendorList) {
                externalVendorLayout.add(RestPayoutPeriod.getExternalVendorItem(restPayoutFromExternalVendor));
            }
        }
        return externalVendorLayout;
    }

    /*
    Build the layout for the summary section
     */
    private void buildSummaryLayout() {
        summaryDetails = UIUtilities.getDetails();
        mainLayout.add(summaryDetails);
        summaryDetailsSummary = buildSummaryDetailsSummary();
        summaryDetailsContent = UIUtilities.getVerticalLayout();
        summaryDetailsContent.add(buildSummaryDocuments());
        summaryDetailsContent.add(buildSummaryLayoutContentRestList());
        summaryDetailsContent.add(buildSummaryLayoutContentAdjustmentsList());
        summaryDetailsContent.add(buildExternalInvoiceList());
        summaryDetails.setContent(summaryDetailsContent);

    }

    private Details buildSummaryDocuments() {
        //Add the Documents review Details pane
        List<PayoutDocument> selectedPayoutDocuments = new ArrayList<>();
        Details periodDocuments = new Details("Documents");
        periodDocuments.addThemeVariants(DetailsVariant.FILLED);
        periodDocuments.setWidthFull();
        periodDocumentsGrid = new Grid<>();

        HorizontalLayout periodDocumentsToolbar = UIUtilities.getHorizontalLayout(true,true,false);
        Button createDocuments = new Button("Create documents");
        createDocuments.setDisableOnClick(true);
        createDocuments.addClickListener(e -> {
            payoutDocumentList = getPayoutDocuments();
            periodDocumentsGrid.setItems(payoutDocumentList);
            periodDocumentsGrid.getDataProvider().refreshAll();
            createDocuments.setEnabled(true);
        });
        Button emailDocumentsButton = new Button("Send documents");
        emailDocumentsButton.setDisableOnClick(true);
        emailDocumentsButton.setEnabled(false);
        emailDocumentsButton.addClickListener(e -> {
            for (PayoutDocument payoutDocument:selectedPayoutDocuments) {
                if(payoutDocument.getEmailAddress().isEmpty()){
                    //System.out.println("Skipping selected item:" + payoutDocument.getName() + " as no email");
                }else{
                    Notification.show("Sending:" + payoutDocument.getName());
                    String subject = "DeliverMore " + payoutDocument.getName();
                    String body = "";
                    emailService.sendMailWithAttachment(payoutDocument.getEmailAddress(), subject, body, payoutDocument.getFile(), payoutDocument.getFile().getName());
                    //System.out.println("Selected item to email:" + payoutDocument.getName() + " send to:" + payoutDocument.getEmailAddress());
                }
            }
            emailDocumentsButton.setEnabled(true);
        });
        Button zipDocumentsButton = new Button("Zip documents");
        zipDocumentsButton.setDisableOnClick(true);
        zipDocumentsButton.setEnabled(false);

        FileDownloadWrapper zipDocumentsButtonWrapper = new FileDownloadWrapper(
                new StreamResource("VendorPayoutFiles.zip", () -> {
                    try {
                        File zippedFile = getZippedFileforFileList(selectedPayoutDocuments);
                        zipDocumentsButton.setEnabled(true);
                        return new ByteArrayInputStream(Files.readAllBytes( zippedFile.toPath()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
        zipDocumentsButtonWrapper.wrapComponent(zipDocumentsButton);

        periodDocumentsToolbar.add(createDocuments,emailDocumentsButton,zipDocumentsButtonWrapper);
        VerticalLayout periodDocumentsContent = UIUtilities.getVerticalLayout();
        periodDocumentsContent.add(periodDocumentsToolbar);
        periodDocuments.addContent(periodDocumentsContent);
        //add checkbox group of all documents
        periodDocumentsContent.add(periodDocumentsGrid);
        periodDocumentsGrid.setWidthFull();
        //periodDocumentsGrid.setAllRowsVisible(true);
        periodDocumentsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        //periodDocumentsGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        periodDocumentsGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        periodDocumentsGrid.setItems(payoutDocumentList);
        periodDocumentsGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        periodDocumentsGrid.addComponentColumn(item -> {
            Anchor anchor = new Anchor(item.getResource(), item.getName());
            anchor.setTarget("_blank");
            return anchor;
        })
                .setFlexGrow(1)
                .setHeader("Document Link");
        periodDocumentsGrid.addColumn(PayoutDocument::getEmailPresentation)
                .setFlexGrow(0)
                .setWidth("250px")
                .setHeader("Email");
        //periodDocumentsGrid.getColumns().forEach(col -> col.setAutoWidth(true));
        periodDocumentsGrid.addSelectionListener(selection -> {
            selectedPayoutDocuments.clear();
            selectedPayoutDocuments.addAll(selection.getAllSelectedItems());
            emailDocumentsButton.setEnabled(false);
            zipDocumentsButton.setEnabled(false);
            for (PayoutDocument payoutDocument: selection.getAllSelectedItems()) {
                zipDocumentsButton.setEnabled(true);
                if(!payoutDocument.getEmailAddress().isEmpty()){
                    emailDocumentsButton.setEnabled(true);
                    break;
                }
            }
        });
        return periodDocuments;
    }

    private File getZippedFileforFileList(List<PayoutDocument> docList){
        System.out.println("***Start of ZIP process***");
        String zipFileName = "Start " + periodStart;
        File zippedFile = new File(appPath, zipFileName + ".zip");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(zipFileName + ".zip");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (PayoutDocument doc: docList) {
            try {
                Utility.zipFile(doc.getFile(), doc.getFile().getName(), zipOut);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            zipOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("***END of ZIP process***");

        return zippedFile;

    }


    private List<PayoutDocument> getPayoutDocuments(){
        payoutDocumentList.clear();

        createSummaryStatement();
        String docName = "Summary Payout Statement: " + getPeriodStart() + " - " + getPeriodEnd();
        payoutDocumentList.add(new PayoutDocument(docName, summaryFile,""));

        Integer count = 0;
        for (RestPayoutPeriod restPayoutPeriod: restPayoutPeriodList) {
            count++;
            restPayoutPeriod.createStatement();
            docName = "Statement for:" + restPayoutPeriod.getRestaurantName() + " : " + restPayoutPeriod.getPeriodRange();
            payoutDocumentList.add(new PayoutDocument(docName, restPayoutPeriod.getPdfFile(),restPayoutPeriod.getRestaurantEmail()));
            //remove the break after testing
            //if(count>6) break;
        }

        //save the csv source
        String csvFileName = "VendorPayoutDetails-" + getPeriodStart() + "-" + getPeriodEnd() + ".csv";
        csvFile = new File(outputDir,csvFileName);
        saveCSV(csvFile);
        if(csvFile!=null){
            payoutDocumentList.add(new PayoutDocument("All vendor payout tasks (Excel)", csvFile, ""));
        }

        //save the adjustments to csvAdjustmentsFile
        if(getRestAdjustmentList().size()>0){
            csvFileName = "VendorPayoutAdjustments-" + getPeriodStart() + "-" + getPeriodEnd() + ".csv";
            csvAdjustmentsFile = new File(outputDir,csvFileName);
            saveAdjustmentsCSV(csvAdjustmentsFile);
            if(csvAdjustmentsFile!=null){
                payoutDocumentList.add(new PayoutDocument("All vendor adjustments (Excel)", csvAdjustmentsFile, ""));
            }
        }

        return payoutDocumentList;
    }

    private void saveCSV(File csvFile){

        try {
            var mappingStrategy = new HeaderColumnNameMappingStrategy<RestPayoutItem>();
            mappingStrategy.setType(RestPayoutItem.class);
            mappingStrategy.setColumnOrderOnWrite(new FixedOrderComparator(columnsUpper));

            Stream<RestPayoutItem> restPayoutItemStream = restPayoutItemList.stream();
            StringWriter output = new StringWriter();
            StatefulBeanToCsv<RestPayoutItem> beanToCsv = new StatefulBeanToCsvBuilder<RestPayoutItem>(output)
                    .withMappingStrategy(mappingStrategy)
                    .build();
            beanToCsv.write(restPayoutItemStream);
            var content = output.toString();
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(content);

            writer.close();
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException ex) {
            System.out.println("saveCSV: CSV write failed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void saveAdjustmentsCSV(File csvFile){

        try {
            var mappingStrategy = new HeaderColumnNameMappingStrategy<RestAdjustment>();
            mappingStrategy.setType(RestAdjustment.class);
            mappingStrategy.setColumnOrderOnWrite(new FixedOrderComparator(columnsUpperAdjustments));

            Stream<RestAdjustment> restAdjustmentsStream = restAdjustmentList.stream();
            StringWriter output = new StringWriter();
            StatefulBeanToCsv<RestAdjustment> beanToCsv = new StatefulBeanToCsvBuilder<RestAdjustment>(output)
                    .withMappingStrategy(mappingStrategy)
                    .build();
            beanToCsv.write(restAdjustmentsStream);
            var content = output.toString();
            BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(content);

            writer.close();
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException ex) {
            System.out.println("saveAdjustmentsCSV: CSV write failed");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void configureRestPayoutItemColumns() {
        columnsUpper = new String[]{
                "restaurantId",
                "restaurantName",
                "orderId",
                "itemType",
                "saleType",
                "creationDateTime",
                "sale",
                "taxes",
                "totalSale",
                "deliveryFee",
                "deliveryFeeFromVendor",
                "paymentMethod",
                "commissionPerDelivery"
        };

        for(int i=0;i<columnsUpper.length;i++){
            columnsUpper[i] = columnsUpper[i].toUpperCase();
        }
    }

    private void configureRestAdjustmentsColumns() {
        columnsUpperAdjustments = new String[]{
                "Id",
                "restaurantId",
                "restaurantName",
                "adjustmentDate",
                "adjustmentNote",
                "adjustmentAmount"
        };

        for(int i=0;i<columnsUpperAdjustments.length;i++){
            columnsUpperAdjustments[i] = columnsUpperAdjustments[i].toUpperCase();
        }
    }

    private void createSummaryStatement() {
        System.out.println("createPDFStatements: summary statement");
        String outputFileExt = ".pdf";

        String outputFileName = "SummarySalesStatement-" + periodStart + "-" + periodEnd;
        File outputFile = new File(outputDir,outputFileName + outputFileExt);
        summaryFile = outputFile;

        try {
            // 1) Load Docx file by filling Velocity template engine and cache it to the registry
            InputStream in = resourcePayStatementTemplate.getInputStream();
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);

            // 2) Create context Java model
            IContext context = report.createContext();
            context.put("restPayoutSummary", this);

            FieldsMetadata metadata = new FieldsMetadata();
            // 2) Create fields metadata to manage lazy loop (#forech velocity)
            // for table row.
            metadata.addFieldAsList("restAdjustmentList.getRestaurantName()");
            metadata.addFieldAsList("restAdjustmentList.getAdjustmentDateFmt()");
            metadata.addFieldAsList("restAdjustmentList.getAdjustmentNote()");
            metadata.addFieldAsList("restAdjustmentList.getAdjustmentAmountFmt()");

            context.put("restAdjustmentList", restAdjustmentList);

            // 2) Create fields metadata to manage lazy loop (#forech velocity)
            // for table row.
            metadata.addFieldAsList("restPayoutPeriodList.getRestaurantName()");
            metadata.addFieldAsList("restPayoutPeriodList.getPeriodRange()");
            metadata.addFieldAsList("restPayoutPeriodList.getPayoutItemCount()");
            metadata.addFieldAsList("restPayoutPeriodList.getPaidItemCount()");
            metadata.addFieldAsList("restPayoutPeriodList.getDirectSalesCount()");
            metadata.addFieldAsList("restPayoutPeriodList.getPhoneInSalesCount()");
            metadata.addFieldAsList("restPayoutPeriodList.getItemCount()");
            metadata.addFieldAsList("restPayoutPeriodList.getPayoutTotalSale()");
            metadata.addFieldAsList("restPayoutPeriodList.getPaidTotalSale()");
            metadata.addFieldAsList("restPayoutPeriodList.getDirectTotalSale()");
            metadata.addFieldAsList("restPayoutPeriodList.getPhoneInTotalSale()");
            metadata.addFieldAsList("restPayoutPeriodList.getTotalSale()");
            metadata.addFieldAsList("restPayoutPeriodList.getOwingToVendor()");
            report.setFieldsMetadata(metadata);

            context.put("restPayoutPeriodList", restPayoutPeriodList);

            /*
            List<RestPayoutItem> restPaidItems = this.paidRestItems;
            // 2) Create fields metadata to manage lazy loop (#forech velocity)
            // for table row.
            metadata.addFieldAsList("restPaidItems.getCreationDateTimeFmt()");
            metadata.addFieldAsList("restPaidItems.getOrderId()");
            metadata.addFieldAsList("restPaidItems.getSale()");
            metadata.addFieldAsList("restPaidItems.getTaxes()");
            metadata.addFieldAsList("restPaidItems.getTotalSale()");
            metadata.addFieldAsList("restPaidItems.getDeliveryFee()");
            metadata.addFieldAsList("restPaidItems.getDeliveryFeeFromVendor()");
            metadata.addFieldAsList("restPaidItems.getPaymentMethod()");
            report.setFieldsMetadata(metadata);

            context.put("restPaidItems", restPaidItems);

             */

            // 3) Generate report by merging Java model with the Docx
            //To PDF
            OutputStream out = new FileOutputStream(outputFile);
            Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
            report.convert(context, options, out);
        } catch (IOException e) {
            System.out.println("createPDFStatements: FAILED vendor summary:" + " ERROR:" + e.toString());
            e.printStackTrace();
        } catch (XDocReportException e) {
            System.out.println("createPDFStatements: FAILED2 vendor summary:" + " ERROR:" + e.toString());
            e.printStackTrace();
        }

    }

    private VerticalLayout buildSummaryDetailsSummary(){
        VerticalLayout summaryDetailsSummary = UIUtilities.getVerticalLayout();
        HorizontalLayout summaryDetailsSummaryHeader = UIUtilities.getHorizontalLayout();
        HorizontalLayout summaryDetailsSummaryOwing = UIUtilities.getHorizontalLayoutNoWidthCentered();
        HorizontalLayout summaryDetailsSummaryFields = UIUtilities.getHorizontalLayout();
        String summaryTitle = "Payout Summary: " + periodStart + " - " + periodEnd;
        NumberField summaryOwing = UIUtilities.getNumberField("", getOwingToVendor());
        Label summaryOwingLabel = new Label("Owing :");
        summaryDetailsSummaryOwing.add(summaryOwingLabel,summaryOwing);
        summaryDetailsSummaryHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        summaryDetailsSummaryHeader.setAlignItems(FlexComponent.Alignment.START);
        summaryDetailsSummaryHeader.add(new Text(summaryTitle),summaryDetailsSummaryOwing);
        summaryDetailsSummary.add(summaryDetailsSummaryHeader,summaryDetailsSummaryFields);
        summaryDetails.setSummary(summaryDetailsSummary);
        NumberField summaryPayoutSale = UIUtilities.getNumberField("Sales", getPayoutSale());
        NumberField summaryPayoutTaxes = UIUtilities.getNumberField("Taxes", getPayoutTaxes());
        NumberField summaryPayoutTotalSale = UIUtilities.getNumberField("TotalSales", getPayoutTotalSale());
        TextField summaryItemCount = UIUtilities.getTextFieldRO("Count", getPayoutItemCount().toString(),"100px");
        NumberField summaryDeliveryFeeFromVendor = UIUtilities.getNumberField("Fee from Vendor",getDeliveryFeeFromVendor());
        NumberField summaryCommission = UIUtilities.getNumberField("Commission", getCommissionForPayout());
        NumberField summaryCommissionPerDelivery = UIUtilities.getNumberField("Commission Per", getCommissionPerDelivery());
        NumberField summaryAdjustment = UIUtilities.getNumberField("Adjustment", getAdjustment());
        summaryDetailsSummaryFields.add(
                summaryPayoutSale,
                summaryPayoutTaxes,
                summaryPayoutTotalSale,
                summaryItemCount,
                summaryDeliveryFeeFromVendor,
                summaryCommission,
                summaryCommissionPerDelivery,
                summaryAdjustment
        );
        return summaryDetailsSummary;
    }

    private Details buildSummaryLayoutContentRestList(){
        Details gridDetails = UIUtilities.getDetails();
        gridDetails.setSummaryText("Restaurant Payouts");
        gridDetails.setOpened(true);
        restGrid = new Grid<>();
        gridDetails.setContent(restGrid);
        restGrid.setItems(restPayoutPeriodList);
        restGrid.setWidthFull();
        restGrid.setAllRowsVisible(true);
        restGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        restGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        restGrid.setColumnReorderingAllowed(true);
        restGrid.addColumn(RestPayoutPeriod::getRestaurantName)
                .setHeader("Restaurant")
                .setKey("restaurant")
                .setWidth("175px")
                .setFlexGrow(0)
                .setResizable(true)
                .setSortable(true)
                .setFrozen(true);
        String numberColWidth = "100px";
        restGrid.addColumn(new NumberRenderer<>(RestPayoutPeriod::getOwingToVendor,new DecimalFormat("##0.00")))
                .setComparator(RestPayoutPeriod::getOwingToVendor)
                .setWidth(numberColWidth)
                .setFlexGrow(0)
                .setSortable(true)
                .setHeader("Owing").setTextAlign(ColumnTextAlign.END);
        restGrid.addColumn(RestPayoutPeriod::getPeriodRange)
                .setHeader("Period")
                .setFlexGrow(1);
        restGrid.addColumn(item -> UIUtilities.getNumberFormatted(item.getPayoutSale()))
                .setComparator(RestPayoutPeriod::getPayoutSale)
                .setWidth(numberColWidth)
                .setFlexGrow(0)
                .setSortable(true)
                .setHeader("Sales").setTextAlign(ColumnTextAlign.END);
        restGrid.addColumn(item -> UIUtilities.getNumberFormatted(item.getPayoutTaxes()))
                .setComparator(RestPayoutPeriod::getPayoutTaxes)
                .setWidth(numberColWidth)
                .setFlexGrow(0)
                .setHeader("Taxes").setTextAlign(ColumnTextAlign.END);
        restGrid.addColumn(item -> UIUtilities.getNumberFormatted(item.getPayoutTotalSale()))
                .setComparator(RestPayoutPeriod::getPayoutTotalSale)
                .setWidth(numberColWidth)
                .setFlexGrow(0)
                .setSortable(true)
                .setHeader("TotalSales").setTextAlign(ColumnTextAlign.END);
        restGrid.addColumn(item -> item.getPayoutItemCount())
                .setComparator(RestPayoutPeriod::getPayoutItemCount)
                .setWidth(numberColWidth)
                .setFlexGrow(0)
                .setSortable(true)
                .setHeader("Count").setTextAlign(ColumnTextAlign.END);
        restGrid.addColumn(item -> UIUtilities.getNumberFormatted(item.getDeliveryFeeFromVendor()))
                .setComparator(RestPayoutPeriod::getDeliveryFeeFromVendor)
                .setWidth(numberColWidth)
                .setFlexGrow(0)
                .setHeader("FeeFromVendor").setTextAlign(ColumnTextAlign.END);
        restGrid.addColumn(item -> UIUtilities.getNumberFormatted(item.getCommissionForPayout()))
                .setComparator(RestPayoutPeriod::getCommissionForPayout)
                .setWidth(numberColWidth)
                .setFlexGrow(0)
                .setSortable(true)
                .setHeader("Commission").setTextAlign(ColumnTextAlign.END);
        restGrid.addColumn(item -> UIUtilities.getNumberFormatted(item.getCommissionPerDelivery()))
                .setComparator(RestPayoutPeriod::getCommissionPerDelivery)
                .setWidth(numberColWidth)
                .setFlexGrow(0)
                .setHeader("Comm Per").setTextAlign(ColumnTextAlign.END);
        restGrid.addColumn(item -> UIUtilities.getNumberFormatted(item.getAdjustment()))
                .setComparator(RestPayoutPeriod::getAdjustment)
                .setWidth(numberColWidth)
                .setFlexGrow(0)
                .setSortable(true)
                .setHeader("Adjustment").setTextAlign(ColumnTextAlign.END);

        return gridDetails;
    }

    private Details buildSummaryLayoutContentAdjustmentsList(){
        Details gridDetails = UIUtilities.getDetails();
        gridDetails.setSummaryText("All Adjustments");
        gridDetails.setOpened(true);
        grid = new Grid<>();
        grid.setItems(restAdjustmentList);
        VerticalLayout adjustmentsLayout = UIUtilities.getVerticalLayout();
        HorizontalLayout periodAdustmentsToolbar = UIUtilities.getHorizontalLayout(true,true,false);
        Icon addNewIcon = new Icon("lumo", "plus");
        addNewIcon.setColor("green");
        Button adjustmentsAddNew = new Button("Add", addNewIcon);
        adjustmentsAddNew.addThemeVariants(ButtonVariant.LUMO_SMALL);
        adjustmentsAddNew.addClickListener(e -> {
            adjustmentDialog.setDialogMode(RestPayoutAdjustmentDialog.DialogMode.NEW);
            adjustmentDialog.dialogOpen(new RestAdjustment(), this, null);
        });
        periodAdustmentsToolbar.add(adjustmentsAddNew);
        adjustmentsLayout.add(periodAdustmentsToolbar);
        gridDetails.setContent(adjustmentsLayout);
        adjustmentsLayout.add(grid);
        grid.setWidthFull();
        grid.setAllRowsVisible(true);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
        grid.setColumnReorderingAllowed(true);
        grid.addComponentColumn(item -> {
            Icon editIcon = new Icon("lumo", "edit");
            //Button editButton = new Button("Edit");
            editIcon.addClickListener(e -> {
                adjustmentDialog.setDialogMode(RestPayoutAdjustmentDialog.DialogMode.EDIT);
                adjustmentDialog.dialogOpen(item,this,getPeriod(item.getRestaurantId()));
            });
            return editIcon;
        }).setWidth("150px").setFlexGrow(0);
        grid.addComponentColumn(item -> {
            Icon deleteIcon = new Icon("lumo", "cross");
            deleteIcon.setColor("red");
            deleteIcon.addClickListener(e -> {
                adjustmentDialog.setDialogMode(RestPayoutAdjustmentDialog.DialogMode.DELETE);
                adjustmentDialog.dialogOpen(item, this, getPeriod(item.getRestaurantId()));
            });
            return deleteIcon;
        }).setWidth("150px").setFlexGrow(0);
        grid.addColumn(RestAdjustment::getRestaurantName)
                .setFlexGrow(0)
                .setHeader("Restaurant");
        grid.addColumn(new LocalDateRenderer<>(RestAdjustment::getAdjustmentDate,"MM/dd"))
                .setSortable(true)
                .setFlexGrow(0)
                .setHeader("Date");
        grid.addColumn(RestAdjustment::getAdjustmentNote)
                .setFlexGrow(1)
                .setHeader("Note");
        grid.addColumn(new NumberRenderer<>(RestAdjustment::getAdjustmentAmount,new DecimalFormat("##0.00")))
                .setComparator(RestAdjustment::getAdjustmentAmount)
                .setFlexGrow(0)
                .setHeader("Amount").setTextAlign(ColumnTextAlign.END);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        return gridDetails;
    }

    private void buildSummary() {

        //determine which restaurants to process for given start date
        this.restPayoutItemList.clear();
        for (Restaurant restaurant: restaurantRepository.getEffectiveRestaurantsForPayout(periodStart)) {
            DateRange range = findRestPeriodDateRange(restaurant.getStartDayOffset(), restaurant.getWeeksInPeriod(), restaurant.getRangeStartDate());
            if(range==null){
                System.out.println("buildRestPayoutDetails:" + restaurant.getName() + " Not a valid payout period");
            }else{
                RestPayoutPeriod restPayoutPeriod = new RestPayoutPeriod(range.getStartDate(), range.getEndDate(), restaurant, this);
                restPayoutPeriodMap.put(restaurant.getRestaurantId(), restPayoutPeriod);
                System.out.println("buildRestPayoutDetails:" + restaurant.getName() + " start:" + range.getStartDate() + " end:" + range.getEndDate() + " PayoutSale:" + restPayoutPeriod.getPayoutSale() + " PayoutTaxes:" + restPayoutPeriod.getPayoutTaxes() + " PayoutTotalSale:" + restPayoutPeriod.getPayoutTotalSale() + " CommissionForPayout:" + restPayoutPeriod.getCommissionForPayout() + " CommissionPerDelivery:" + restPayoutPeriod.getCommissionPerDelivery() + " DeliveryFeeFromVendor:" + restPayoutPeriod.getDeliveryFeeFromVendor() + " owingToVendor:" + restPayoutPeriod.getOwingToVendor() + " commissionRate:" + restaurant.getCommissionRate() );
                this.payoutSale = this.payoutSale + restPayoutPeriod.getPayoutSale();
                this.payoutTaxes = this.payoutTaxes + restPayoutPeriod.getPayoutTaxes();
                this.payoutTotalSale = this.payoutTotalSale + restPayoutPeriod.getPayoutTotalSale();
                this.payoutItemCount = this.payoutItemCount + restPayoutPeriod.getPayoutItemCount();
                this.paidSale = this.paidSale + restPayoutPeriod.getPaidSale();
                this.paidTotalSale = this.paidTotalSale + restPayoutPeriod.getPaidTotalSale();
                this.paidItemCount = this.paidItemCount + restPayoutPeriod.getPaidItemCount();
                this.directTotalSale = this.directTotalSale + restPayoutPeriod.getDirectTotalSale();
                this.directItemCount = this.directItemCount + restPayoutPeriod.getDirectSalesCount();
                this.phoneInTotalSale = this.phoneInTotalSale + restPayoutPeriod.getPhoneInTotalSale();
                this.phoneInItemCount = this.phoneInItemCount + restPayoutPeriod.getPhoneInSalesCount();
                this.sale = this.sale + restPayoutPeriod.getSale();
                this.taxes = this.taxes + restPayoutPeriod.getTaxes();
                this.totalSale = this.totalSale + restPayoutPeriod.getTotalSale();
                this.itemCount = this.itemCount + restPayoutPeriod.getItemCount();
                this.adjustment = this.adjustment + restPayoutPeriod.getAdjustment();
                this.restAdjustmentList.addAll(restPayoutPeriod.getRestAdjustmentList());
                this.restPayoutItemList.addAll(restPayoutPeriod.getPayoutRestItems());
                this.restPayoutItemList.addAll(restPayoutPeriod.getPaidRestItems());
                this.restPayoutItemList.addAll(restPayoutPeriod.getCancelledRestItems());
                this.commissionForPayout = this.commissionForPayout + restPayoutPeriod.getCommissionForPayout();
                this.commissionPerDelivery = this.commissionPerDelivery + restPayoutPeriod.getCommissionPerDelivery();
                this.deliveryFeeFromExternal = this.deliveryFeeFromExternal + restPayoutPeriod.getDeliveryFeeFromExternal();
                this.deliveryFeeFromVendor = this.deliveryFeeFromVendor + restPayoutPeriod.getDeliveryFeeFromVendor();
                this.owingToVendor = this.owingToVendor + restPayoutPeriod.getOwingToVendor();

                if(restPayoutPeriod.hasPayoutFromExternalVendor()){
                    this.restPayoutFromExternalVendorList.add(restPayoutPeriod.getRestPayoutFromExternalVendor());
                }
            }
        }
        //need to sort the list for use in grids and restaurant lists
        Collection<RestPayoutPeriod> restPayoutPeriodCol = restPayoutPeriodMap.values();
        restPayoutPeriodList = new ArrayList<>(restPayoutPeriodCol);
        Collections.sort(restPayoutPeriodList, Comparator.comparing(RestPayoutPeriod::getRestaurantName));

    }

    private void updateAdjustment(){
        restAdjustmentList.clear();
        this.adjustment = 0.0;
        for (RestPayoutPeriod restPayoutPeriod: restPayoutPeriodList) {
            this.adjustment = this.adjustment + restPayoutPeriod.getAdjustment();
            this.restAdjustmentList.addAll(restPayoutPeriod.getRestAdjustmentList());
        }
    }

    /* Determine the date range to use for the specific restaurant
     *  - returns NULL if this restaurant should not be processed this period
     */
    private DateRange findRestPeriodDateRange(Integer startDayOffset, Integer weeksInPeriod, LocalDate rangeStartDate){
        LocalDate restPeriodStart;
        LocalDate restPeriodEnd;

        if(startDayOffset.equals(0)){
            restPeriodEnd = periodEnd;
        }else{
            restPeriodEnd = periodEnd.plusDays(startDayOffset);
        }

        restPeriodStart = restPeriodEnd.minusWeeks(weeksInPeriod).plusDays(1);

        //determine if this range is valid for restaurants processed other than weekly
        if(rangeStartDate==null){
            return new DateRange(restPeriodStart,restPeriodEnd);
        }else{
            Long weeksSinceRangeStart = ChronoUnit.WEEKS.between(rangeStartDate, restPeriodStart);
            boolean isDivisibleByWeeks = weeksSinceRangeStart % weeksInPeriod == 0;
            if(isDivisibleByWeeks){
                //System.out.println("Process this period: Weeks between:" + rangeStartDate + "and:" + restPeriodStart + " =:" + weeksSinceRangeStart);
                return new DateRange(restPeriodStart,restPeriodEnd);
            }else{
                //System.out.println("Skip this period: Weeks between:" + rangeStartDate + "and:" + restPeriodStart + " =:" + weeksSinceRangeStart);
                return null;
            }
        }

    }

    public VerticalLayout getMainLayout() {
        return mainLayout;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public Double getSale() {
        return Utility.getInstance().round(sale,2);
    }

    public Double getTaxes() {
        return Utility.getInstance().round(taxes,2);
    }

    public Double getTotalSale() {
        return Utility.getInstance().round(totalSale,2);
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public Double getPayoutSale() {
        return Utility.getInstance().round(payoutSale,2);
    }

    public Double getPayoutTaxes() {
        return Utility.getInstance().round(payoutTaxes,2);
    }

    public Double getPayoutTotalSale() {
        return Utility.getInstance().round(payoutTotalSale,2);
    }

    public Integer getPayoutItemCount() {
        return payoutItemCount;
    }

    public Double getPaidSale() {
        return Utility.getInstance().round(paidSale,2);
    }

    public Double getPaidTotalSale() {
        return Utility.getInstance().round(paidTotalSale,2);
    }

    public Integer getPaidItemCount() {
        return paidItemCount;
    }

    public Double getDeliveryFeeFromVendor() {
        return Utility.getInstance().round(deliveryFeeFromVendor,2);
    }

    public Double getDeliveryFeeFromExternal() {
        return Utility.getInstance().round(deliveryFeeFromExternal,2);
    }

    public Double getCommissionForPayout() {
        return Utility.getInstance().round(commissionForPayout,2);
    }

    public Double getCommissionPerDelivery() {
        return Utility.getInstance().round(commissionPerDelivery,2);
    }

    public Double getAdjustment() {
        return Utility.getInstance().round(adjustment,2);
    }

    public Double getOwingToVendor() {
        //this.owingToVendor = this.payoutTotalSale - this.commissionForPayout - this.commissionPerDelivery - this.deliveryFeeFromVendor - this.adjustment;
        return Utility.getInstance().round(owingToVendor,2);
    }

    public List<RestAdjustment> getRestAdjustmentList() {
        restAdjustmentList.sort(Comparator.comparing(RestAdjustment::getRestaurantName).thenComparing(RestAdjustment::getAdjustmentDate));
        return restAdjustmentList;
    }

    public RestPayoutPeriod getPeriod(Long restaurantID){
        return restPayoutPeriodMap.get(restaurantID);
    }

    public Double getDirectTotalSale() {
        return Utility.getInstance().round(directTotalSale,2);
    }

    public Double getPhoneInTotalSale() {
        return Utility.getInstance().round(phoneInTotalSale,2);
    }

    public Integer getDirectItemCount() {
        return directItemCount;
    }

    public Integer getPhoneInItemCount() {
        return phoneInItemCount;
    }

    @Override
    public String toString() {
        return "RestPayoutSummary{" +
                "periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", sale=" + getSale() +
                ", taxes=" + getTaxes() +
                ", totalSale=" + getTotalSale() +
                ", itemCount=" + itemCount +
                ", payoutSale=" + getPayoutSale() +
                ", payoutTaxes=" + getPayoutTaxes() +
                ", payoutTotalSale=" + getPayoutTotalSale() +
                ", payoutItemCount=" + payoutItemCount +
                ", paidSale=" + getPaidSale() +
                ", paidTotalSale=" + getPaidTotalSale() +
                ", paidItemCount=" + paidItemCount +
                ", deliveryFeeFromVendor=" + getDeliveryFeeFromVendor() +
                ", deliveryFeeFromExternal=" + getDeliveryFeeFromExternal() +
                ", commissionForPayout=" + getCommissionForPayout() +
                ", commissionPerDelivery=" + getCommissionPerDelivery() +
                ", adjustment=" + getAdjustment() +
                ", owingToVendor=" + getOwingToVendor() +
                '}';
    }
}
