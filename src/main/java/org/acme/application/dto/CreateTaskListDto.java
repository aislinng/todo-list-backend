package org.acme.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTaskListDto {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150)
    private String title;

    @Size(max = 500)
    private String description;

    @Size(max = 20)
    private String color;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
