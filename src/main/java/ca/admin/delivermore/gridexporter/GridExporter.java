package ca.admin.delivermore.gridexporter;


import com.flowingcode.vaadin.addons.gridhelpers.GridHelper;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.grid.ColumnPathRenderer;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.renderer.BasicRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class GridExporter<T> implements Serializable {

    private final static Logger LOGGER = LoggerFactory.getLogger(GridExporter.class);
    private static final String GET_FOOTER_COMPONENT_METHOD_NAME = "getFooterComponent";
    private static final String GET_HEADER_COMPONENT_METHOD_NAME = "getHeaderComponent";

    static final String COLUMN_VALUE_PROVIDER_DATA = "column-value-provider-data";
    static final String COLUMN_EXPORTED_PROVIDER_DATA = "column-value-exported-data";
    static final String COLUMN_PARSING_FORMAT_PATTERN_DATA = "column-parsing-format-pattern-data";
    static final String COLUMN_EXCEL_FORMAT_DATA = "column-excel-format-data";
    static final String COLUMN_TYPE_DATA = "column-type-data";
    static final String COLUMN_TYPE_NUMBER = "number";
    static final String COLUMN_TYPE_DATE = "date";
    static final String COLUMN_HEADER = "column-header";
    static final String COLUMN_FOOTER = "column-footer";

    Grid<T> grid;

    Collection<Grid.Column<T>> columns;
    PropertySet<T> propertySet;

    String fileName = "export";

    boolean autoAttachExportButtons = true;

    SerializableSupplier<String> nullValueSupplier;

    private ButtonsAlignment buttonsAlignment = ButtonsAlignment.RIGHT;

    private GridExporter(Grid<T> grid) {
        this.grid = grid;
    }

    public static <T> GridExporter<T> createFor(Grid<T> grid) {
        GridExporter<T> exporter = new GridExporter<>(grid);
        grid.getElement().addAttachListener(ev -> {
            if (exporter.autoAttachExportButtons) {
                HorizontalLayout hl = new HorizontalLayout();
                //Anchor csvLink = new Anchor("", FontAwesome.Regular.FILE_LINES.create());
                Anchor csvLink = new Anchor("", new Icon(VaadinIcon.DOWNLOAD_ALT));

                csvLink.setHref(exporter.getCsvStreamResource());
                csvLink.getElement().setAttribute("download", true);
                hl.add(csvLink);
                hl.setSizeFull();

                hl.setJustifyContentMode(exporter.getJustifyContentMode());

                GridHelper.addToolbarFooter(grid, hl);
            }
        });
        return exporter;
    }

    private FlexComponent.JustifyContentMode getJustifyContentMode() {
        FlexComponent.JustifyContentMode justifyContentMode;
        if(this.buttonsAlignment == ButtonsAlignment.LEFT)
        {
            justifyContentMode =  JustifyContentMode.START;
        }
        else
        {
            justifyContentMode = JustifyContentMode.END;
        }
        return justifyContentMode;
    }

    public void setButtonsAlignment(ButtonsAlignment buttonsAlignment) {
        this.buttonsAlignment = buttonsAlignment;
    }

    Object extractValueFromColumn(T item, Column<T> column) {
        Object value = null;
        // first check if there is a value provider for the current column
        @SuppressWarnings("unchecked")
        ValueProvider<T,String> customVP = (ValueProvider<T, String>) ComponentUtil.getData(column, GridExporter.COLUMN_VALUE_PROVIDER_DATA);
        if (customVP!=null) {
            value = customVP.apply(item);
        }

        // if there is a key, assume that the property can be retrieved from it
        if (value==null && column.getKey() != null) {
            Optional<PropertyDefinition<T, ?>> propertyDefinition =
                    this.propertySet.getProperty(column.getKey());
            if (propertyDefinition.isPresent()) {
                value = propertyDefinition.get().getGetter().apply(item);
            } else {
                LOGGER.warn("Column key: " + column.getKey() + " is a property which cannot be found");
            }
        }

        // if the value still couldn't be retrieved then if the renderer is a LitRenderer, take the value only
        // if there is one value provider
        if (value==null && column.getRenderer() instanceof LitRenderer) {
            LitRenderer<T> r = (LitRenderer<T>) column.getRenderer();
            if (r.getValueProviders().values().size()==1) {
                value = r.getValueProviders().values().iterator().next().apply(item);
            }
        }

        // at this point if the value is still null then take the only value from ColumPathRenderer VP
        if (value == null && column.getRenderer() instanceof Renderer) {
            Renderer<T> renderer = column.getRenderer();
            if (renderer instanceof ColumnPathRenderer) {
                try {
                    Field provider = ColumnPathRenderer.class.getDeclaredField("provider");
                    provider.setAccessible(true);
                    ValueProvider<T, ?> vp = (ValueProvider<T, ?>) provider.get(renderer);
                    value = vp.apply(item);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new IllegalStateException("Problem obtaining value or exporting", e);
                }
            } else if (renderer instanceof BasicRenderer) {
                try {
                    Method getValueProviderMethod = BasicRenderer.class.getDeclaredMethod("getValueProvider");
                    getValueProviderMethod.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    ValueProvider<T, ?> vp = (ValueProvider<T, ?>) getValueProviderMethod.invoke(renderer);
                    value = vp.apply(item);
                } catch (NoSuchMethodException
                         | SecurityException
                         | IllegalAccessException
                         | IllegalArgumentException
                         | InvocationTargetException e) {
                    throw new IllegalStateException("Problem obtaining value or exporting", e);
                }
            }
        }

        if (value==null) {
            if (nullValueSupplier!=null) {
                value = nullValueSupplier.get();
            } else {
                throw new IllegalStateException("It's not possible to obtain a value for column, please set a value provider by calling setExportValue() column:" + column.getKey() + " item:" + item);
            }
        }
        return value;
    }

    public StreamResource getCsvStreamResource() {
        String fileNameFull = fileName;
        if(!fileName.endsWith(".csv")){
            fileNameFull = fileNameFull + ".csv";
        }
        return new StreamResource(fileNameFull, new CsvInputStreamFactory<>(this));
    }

    /*
    public void saveToFile(File file){
        this.columns = this.grid.getColumns().stream().filter(this::isExportable).collect(Collectors.toList());
        try {
            BufferedWriter bufWriter = new BufferedWriter(new FileWriter(file));
            CSVWriter writer = new CSVWriter(bufWriter);
            List<Pair<String, Column<T>>> headers = getGridHeaders(grid);
            writer.writeNext(headers.stream().map(pair->pair.getLeft()).collect(Collectors.toList()).toArray(new String[0]));
            for (JournalEntryRow row :
                 ) {

            }

            Stream<T> dataStream = obtainDataStream(grid.getDataProvider());
            dataStream.forEach(t -> {
                writer.writeNext(buildRow(t,writer));
            });
            List<Pair<String,Column<T>>> footers = getGridFooters(grid);
            writer.writeNext(footers.stream().map(pair->pair.getLeft()).collect(Collectors.toList()).toArray(new String[0]));


            writer.close();
        } catch (IOException e) {
            LOGGER.error("Problem generating export", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LOGGER.error("Problem generating export", e);
                }
            }
        }


    }
             */


    protected boolean isExportable(Grid.Column<T> column) {
        Boolean exported = (Boolean) ComponentUtil.getData(column, GridExporter.COLUMN_EXPORTED_PROVIDER_DATA);
        return exported!=null?exported:column.isVisible();
    }

    protected List<Pair<String, Column<T>>> getGridHeaders(Grid<T> grid) {
        return columns.stream().map(column -> ImmutablePair.of(renderCellTextContent(grid, column, GridExporter.COLUMN_HEADER),column))
                .collect(Collectors.toList());
    }

    private String renderCellTextContent(Grid<T> grid, Column<T> column, String columnType) {
        String headerOrFooter = (String) ComponentUtil.getData(column, columnType);
        String methodName = GET_HEADER_COMPONENT_METHOD_NAME;
        if (Strings.isBlank(headerOrFooter)) {
            if (GridExporter.COLUMN_HEADER.equals(columnType)) {
                headerOrFooter = GridHelper.getHeader(grid, column);
            } else if (GridExporter.COLUMN_FOOTER.equals(columnType)) {
                methodName = GET_FOOTER_COMPONENT_METHOD_NAME;
                headerOrFooter = GridHelper.getFooter(grid, column);
            }
        }
        if (Strings.isBlank(headerOrFooter)) {
            try {
                Method getHeaderOrFooterComponent = Column.class.getMethod(methodName);
                Element element = (Element) getHeaderOrFooterComponent.invoke(column);
                if (element!=null) {
                    headerOrFooter = element.getTextRecursively();
                }
            } catch (NoSuchMethodException e) {
                headerOrFooter = "";
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new IllegalStateException("Problem when trying to render header or footer cell text content", e);
            }
        }

        return headerOrFooter;
    }


    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the filename of the exported file
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isAutoAttachExportButtons() {
        return autoAttachExportButtons;
    }

    /**
     * If true, it will automatically generate export buttons in the asociated grid
     * @param autoAttachExportButtons
     */
    public void setAutoAttachExportButtons(boolean autoAttachExportButtons) {
        this.autoAttachExportButtons = autoAttachExportButtons;
    }

    /**
     * Configure a value provider for a given column. If there is a value provider,
     * that will be taken into account when exporting the column
     * @param column
     * @param vp
     */
    public void setExportValue(Column<T> column, ValueProvider<T, String> vp) {
        ComponentUtil.setData(column, COLUMN_VALUE_PROVIDER_DATA, vp);
    }

    /**
     * Configure if the column is exported or not
     * @param column
     * @param export: true will be included in the exported file, false will not be included
     */
    public void setExportColumn(Column<T> column, boolean export) {
        ComponentUtil.setData(column, COLUMN_EXPORTED_PROVIDER_DATA, export);
    }

    public void createExportColumn(Column<T> column, boolean visible, String header){
        //LOGGER.info("createExportColumn: header:" + header);
        column.setVisible(visible);
        column.setHeader(header);
        setExportColumn(column,true);
    }

    public void createExportColumn(Column<T> column, boolean visible, String header, Column<T> columnExportOnly){
        //LOGGER.info("createExportColumn: header:" + header);
        column.setVisible(visible);
        column.setHeader(header);
        setExportColumn(column,false);
        columnExportOnly.setVisible(false);
        columnExportOnly.setHeader(header);
        setExportColumn(columnExportOnly,true);
    }

    public void setNullValueHandler(SerializableSupplier<String> nullValueSupplier) {
        this.nullValueSupplier = nullValueSupplier;
    }

    /**
     * If the column is based on a String, it configures a DecimalFormat to parse a number from the value
     * of the column so it can be converted to a Double, and then allows to specify the excel format
     * to be applied to the cell when exported to excel, so the resulting cell is not a string
     * but a number that can be used in formulas.
     * @param column
     * @param decimalFormat
     * @param excelFormat
     */
    public void setNumberColumnFormat(Column<T> column, DecimalFormat decimalFormat, String excelFormat) {
        ComponentUtil.setData(column, COLUMN_PARSING_FORMAT_PATTERN_DATA, decimalFormat);
        ComponentUtil.setData(column, COLUMN_EXCEL_FORMAT_DATA, excelFormat);
        ComponentUtil.setData(column, COLUMN_TYPE_DATA, COLUMN_TYPE_NUMBER);
    }

    /**
     * If the column is based on a String, it configures a DateFormat to parse a date from the value of
     * the column so it can be converted to a java.util.Date, and then allows to specify the excel
     * format to be applied to the cell when exported to excel, so the resulting cell is not a string
     * but a date that can be used in formulas.
     * @param column
     * @param dateFormat
     * @param excelFormat
     */
    public void setDateColumnFormat(Column<T> column, DateFormat dateFormat, String excelFormat) {
        ComponentUtil.setData(column, COLUMN_PARSING_FORMAT_PATTERN_DATA, dateFormat);
        ComponentUtil.setData(column, COLUMN_EXCEL_FORMAT_DATA, excelFormat);
        ComponentUtil.setData(column, COLUMN_TYPE_DATA, COLUMN_TYPE_DATE);
    }

    /**
     * If the column is based on a number attribute of the item, rendered with a NumberRenderer, it configures
     * the excel format to be applied to the cell when exported to excel, so the resulting cell is not a string
     * but a number that can be used in formulas.
     * @param column
     * @param excelFormat
     */
    public void setNumberColumnFormat(Column<T> column, String excelFormat) {
        ComponentUtil.setData(column, COLUMN_EXCEL_FORMAT_DATA, excelFormat);
        ComponentUtil.setData(column, COLUMN_TYPE_DATA, COLUMN_TYPE_NUMBER);
    }

    /**
     * If the column is based on a LocalDate attribute of the item, rendered with a LocalDateRenderer, it configures
     * the excel format to be applied to the cell when exported to excel, so the resulting cell is not a string
     * but a date that can be used in formulas.
     * @param column
     * @param excelFormat
     */
    public void setDateColumnFormat(Column<T> column, String excelFormat) {
        ComponentUtil.setData(column, COLUMN_EXCEL_FORMAT_DATA, excelFormat);
        ComponentUtil.setData(column, COLUMN_TYPE_DATA, COLUMN_TYPE_DATE);
    }

    /**
     * Configures the exporter to use a custom string for a specific column's header. Usefull when the header
     * is a custom component.
     * @param column
     * @param header
     */
    public void setCustomHeader(Column<T> column, String header) {
        ComponentUtil.setData(column, COLUMN_HEADER, header);
    }

    /**
     * Configures the exporter to use a custom string for a specific column's footer. Usefull when the footer
     * is a custom component.
     * @param column
     * @param header
     */
    public void setCustomFooter(Column<T> column, String header) {
        ComponentUtil.setData(column, COLUMN_FOOTER, header);
    }
}