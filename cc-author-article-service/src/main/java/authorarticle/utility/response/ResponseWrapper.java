package authorarticle.utility.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseWrapper<T> {

    @ApiModelProperty(required = true,
            value = "返回状态码",
            dataType = "int",
            example = "200")
    private Integer responseCode;
    @ApiModelProperty(required = true,
            value = "返回message信息",
            dataType = "string",
            example = "success", position = 1)
    private String responseMessage;
    @ApiModelProperty(required = true,
            value = "返回数据",
            dataType = "T",
            example = "data", position = 2)
    private T responseBody;

    public ResponseWrapper(Integer responseCode, String responseMessage, T responseBody) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.responseBody = responseBody;
    }

    public ResponseWrapper() {

    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }
}
