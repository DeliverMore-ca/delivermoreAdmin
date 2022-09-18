package ca.admin.delivermore.data.global;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetails {
    List<OrderDetail> orderDetailList = new ArrayList<>();

    public OrderDetails() {
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public Boolean loadFromCSV(String fileName){

        try {
            CsvToBeanBuilder<OrderDetail> beanBuilder = new CsvToBeanBuilder<>(new InputStreamReader(new FileInputStream(fileName)));

            beanBuilder.withType(OrderDetail.class);
            // build methods returns a list of Beans
            beanBuilder.build().parse().forEach(e -> orderDetailList.add(e));
            /*
            for (OrderDetail orderDetail: orderDetailList ) {
                System.out.println("OrderDetail:" + orderDetail);
            }
             */

        } catch (FileNotFoundException e) {
            System.out.println("Csv load failed");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;

    }
}
