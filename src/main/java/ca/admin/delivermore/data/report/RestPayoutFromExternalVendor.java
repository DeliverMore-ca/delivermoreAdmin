package ca.admin.delivermore.data.report;

public class RestPayoutFromExternalVendor {
    private String name;
    private Integer count = 0;
    private Double amount = 0.0;

    public RestPayoutFromExternalVendor(String name, Integer count, Double amount) {
        this.name = name;
        this.count = count;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "RestPayoutFromExternalVendor{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", amount=" + amount +
                '}';
    }
}
