package com.farmar_mart.agriculture_order.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

}
