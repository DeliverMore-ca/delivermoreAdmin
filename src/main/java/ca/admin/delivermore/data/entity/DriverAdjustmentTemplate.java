package ca.admin.delivermore.data.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class DriverAdjustmentTemplate {

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
    private String templateName = "";

    private Double templateAmount = 0.0;

    public DriverAdjustmentTemplate() {
    }

    public DriverAdjustmentTemplate(String templateName, Double templateAmount) {
        this.templateName = templateName;
        this.templateAmount = templateAmount;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Double getTemplateAmount() {
        return templateAmount;
    }

    public void setTemplateAmount(Double templateAmount) {
        this.templateAmount = templateAmount;
    }

    @Override
    public String toString() {
        if(templateAmount.equals(0.0)){
            return templateName;
        }
        return templateName + " (" + templateAmount + ")";
    }
}
