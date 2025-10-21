package com.farmermart.orderservice.dto.requestDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;

}
