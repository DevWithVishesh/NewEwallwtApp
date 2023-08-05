package com.icsd.model.mail;


import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MailModule {

    String SendTo;
    String Email;
    LocalDate ExpireDate;
}
