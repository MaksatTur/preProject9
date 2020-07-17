package kg.max.crud.rest;

public class ServerResponse {
    private String status;
    private Object data;

    public ServerResponse(String status) {
        this.status = status;
    }

    public ServerResponse(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
