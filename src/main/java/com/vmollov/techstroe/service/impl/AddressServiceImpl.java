package com.vmollov.techstroe.service.impl;

import com.vmollov.techstroe.model.entity.Address;
import com.vmollov.techstroe.model.entity.User;
import com.vmollov.techstroe.model.service.AddressServiceModel;
import com.vmollov.techstroe.repository.AddressRepository;
import com.vmollov.techstroe.repository.UserRepository;
import com.vmollov.techstroe.service.AddressService;
import com.vmollov.techstroe.web.errors.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.vmollov.techstroe.constants.Errors.*;
import static com.vmollov.techstroe.constants.GlobalConstants.*;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public AddressServiceModel createAddress(AddressServiceModel addressServiceModel, String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(USERNAME_NOT_FOUND_EXCEPTION));
        Address address = this.modelMapper.map(addressServiceModel, Address.class);
        address = this.addressRepository.saveAndFlush(address);

        user.getAddresses().add(address);
        this.userRepository.saveAndFlush(user);

        return this.modelMapper.map(address, AddressServiceModel.class);
    }

    @Override
    public AddressServiceModel findAddressById(String id) {
        Address address = this.addressRepository.findById(id).orElseThrow(() -> new NotFoundException(ADDRESS_NOT_FOUND_EXCEPTION));

        return this.modelMapper.map(address, AddressServiceModel.class);
    }

    @Override
    public AddressServiceModel updateAddress(AddressServiceModel newAddress, String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(USERNAME_NOT_FOUND_EXCEPTION));
        Address oldAddress = this.addressRepository.findById(newAddress.getId()).orElseThrow(()-> new NotFoundException(ADDRESS_NOT_FOUND_EXCEPTION));

        if (!user.getAddresses().contains(oldAddress)){
            throw new NotFoundException(ADDRESS_NOT_FOUND_IN_ADDRESSES_EXCEPTION);
        }

        oldAddress.setName(newAddress.getName());
        oldAddress.setCity(newAddress.getCity());
        oldAddress.setAddress(newAddress.getAddress());
        oldAddress.setPhoneNumber(newAddress.getPhoneNumber());
        oldAddress.setNotes(newAddress.getNotes());

        oldAddress = this.addressRepository.saveAndFlush(oldAddress);

        return this.modelMapper.map(oldAddress, AddressServiceModel.class);
    }

    @Override
    public void deleteAddress(String addressId, String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(USERNAME_NOT_FOUND_EXCEPTION));
        Address address = this.addressRepository.findById(addressId).orElseThrow(()-> new NotFoundException(ADDRESS_NOT_FOUND_EXCEPTION));

        if (!user.getAddresses().contains(address)){
            throw new NotFoundException(ADDRESS_NOT_FOUND_IN_ADDRESSES_EXCEPTION);
        }

        user.getAddresses().remove(address);
        this.userRepository.saveAndFlush(user);

        this.addressRepository.delete(address);
    }

    @Override
    public boolean userHasAddress(String addressId, String username) {

        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(USERNAME_NOT_FOUND_EXCEPTION));
        Address address = this.addressRepository.findById(addressId).orElseThrow(()-> new NotFoundException(ADDRESS_NOT_FOUND_EXCEPTION));

        return user.getAddresses().contains(address);
    }
}
