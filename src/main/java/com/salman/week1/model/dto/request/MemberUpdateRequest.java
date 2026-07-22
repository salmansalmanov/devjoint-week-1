package com.salman.week1.model.dto.request;

import com.salman.week1.model.enums.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateRequest {
    private String name;
    private String surname;
    private MemberStatus status;
}
