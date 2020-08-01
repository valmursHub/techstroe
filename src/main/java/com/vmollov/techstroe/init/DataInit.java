package com.vmollov.techstroe.init;

import com.vmollov.techstroe.service.ProductTypeService;
import com.vmollov.techstroe.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInit implements CommandLineRunner {

    private final RoleService roleService;
    private final ProductTypeService productTypeService;

    public DataInit(RoleService roleService, ProductTypeService productTypeService) {
        this.roleService = roleService;
        this.productTypeService = productTypeService;
    }

    @Override
    public void run(String... args) throws Exception {
        this.roleService.seedRolesInDb();
        this.productTypeService.seedProductTypes();
    }
}
