package com.vmollov.techstroe.service;

import com.vmollov.techstroe.model.service.AddressServiceModel;

public interface AddressService {

    AddressServiceModel createAddress(AddressServiceModel addressServiceModel, String username);

    AddressServiceModel findAddressById(String id);

    AddressServiceModel updateAddress(AddressServiceModel newAddress, String username);

    void deleteAddress(String addressId, String username);

    boolean userHasAddress(String addressId, String username);
}
