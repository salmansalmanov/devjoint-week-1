package com.salman.week1.model.dto.response;

import com.salman.week1.model.enums.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {
    private UUID id;
    private String name;
    private String surname;
    private MemberStatus status;
}
