package br.com.generic.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private String id;

    private String password;

    @NotBlank(message = "{user.email.notempty}")
    private String email;

    @NotBlank(message = "{user.firstname.notempty}")
    private String firstName;

    @NotBlank(message = "{user.lastname.notempty}")
    private String lastName;

    @NotBlank(message = "{user.doc.notempty}")
    @Size(min = 11, max = 13, message = "{user.doc.invalidated}")
    private String doc;

    @NotBlank(message = "{user.cellphone.notempty}")
    private String cellphone;

    private boolean status;


    @NotNull(message = "{user.dtbirthday.notempty}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="America/Sao_Paulo")
    private Date dtBirthday;

}
