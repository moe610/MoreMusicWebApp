package com.moremusic.moremusicwebapp.datalayer;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
}
