package ca.admin.delivermore.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class GiftCardTranactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    private String code = "";
    @NotNull
    private LocalDateTime transactionDateTime = LocalDateTime.now();
    @NotNull
    private Double amount = 0.0;
    private String userName = "";

    public GiftCardTranactionEntity() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public String getTransactionDateTimeFmt() {
        return DateTimeFormatter.ofPattern("MM-dd HH:mm").format(transactionDateTime);
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
