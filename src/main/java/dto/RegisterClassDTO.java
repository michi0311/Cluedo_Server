package dto;

public class RegisterClassDTO extends RequestDTO {
    private String classToRegister;

    public String getClassToRegister() {
        return classToRegister;
    }

    public void setClassToRegister(String classToRegister) {
        this.classToRegister = classToRegister;
    }
}
