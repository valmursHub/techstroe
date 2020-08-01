package com.vmollov.techstroe.service.impl;

import com.vmollov.techstroe.model.entity.Role;
import com.vmollov.techstroe.model.service.RoleServiceModel;
import com.vmollov.techstroe.repository.RoleRepository;
import com.vmollov.techstroe.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedRolesInDb() {
        if (roleRepository.count() == 0){
            Role root = new Role("ROLE_ROOT");
            Role admin = new Role("ROLE_ADMIN");
            Role user = new Role("ROLE_USER");
            this.roleRepository.save(root);
            this.roleRepository.save(admin);
            this.roleRepository.save(user);
        }
    }

    @Override
    public Set<RoleServiceModel> findAllRoles() {
        return this.roleRepository.findAll()
                .stream()
                .map(r -> this.modelMapper.map(r, RoleServiceModel.class))
                .collect(Collectors.toSet());

    }

    @Override
    public RoleServiceModel findByAuthority(String role) {
        return this.modelMapper.map(this.roleRepository.findByAuthority(role),RoleServiceModel.class);
    }
}

