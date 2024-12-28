package com.moremusic.moremusicwebapp.datalayer;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetResponse {
    private String resetToken;
    private String error;
}
