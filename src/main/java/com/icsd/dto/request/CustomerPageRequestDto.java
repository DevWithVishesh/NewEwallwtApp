package com.icsd.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPageRequestDto {
private int pageLimit;
private int pageOffset;
private String sortDirection;
private String sortField;
private String search;
}
