package vn.gtel.user.models.response;


import lombok.Data;

@Data
public class MainResponse <T> {
    private String code;
    private String message;
    private T data;

    public MainResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public MainResponse() {
        this.code = "SUCCESS";
        this.message = "SUCCESS";
    }

}