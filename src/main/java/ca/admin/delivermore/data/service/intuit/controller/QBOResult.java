package ca.admin.delivermore.data.service.intuit.controller;

public class QBOResult {
    private Boolean success = Boolean.FALSE;
    private String messageHeader = "QBO Process Result";
    private String message = "";
    private String result = null;

    public QBOResult() {
    }

    public QBOResult(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setFailed(String message){
        setFailed(null, message);
    }

    public void setFailed(String messageHeader, String message){
        if(messageHeader!=null) this.messageHeader = messageHeader;
        this.message = message;
        this.success = Boolean.FALSE;
    }

    public void setSuccess(String message){
        setSuccess(null, message);
    }

    public void setSuccess(String messageHeader, String message){
        if(messageHeader!=null) this.messageHeader = messageHeader;
        this.message = message;
        this.success = Boolean.TRUE;
    }

    @Override
    public String toString() {
        return "QBOResult{" +
                "success=" + success +
                ", messageHeader='" + messageHeader + '\'' +
                ", message='" + message + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
