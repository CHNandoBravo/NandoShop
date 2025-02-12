package shop.nandoShop.nandoshop_app.dtos.responses;

public class ApiResponse<T> {
    private String message;
    private boolean success;
    private T data; // T es el tipo genérico

    // Constructor con datos
    public ApiResponse(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    // Constructor sin datos
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.data = null; // Si no hay datos, el campo es null
    }

    // Getters y setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
