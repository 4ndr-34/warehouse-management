package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.admin.ConfigDTO;

public interface AdminService {

    ConfigDTO adminAddConfig(ConfigDTO configDTO) throws Exception;

    ConfigDTO adminChangeConfigValue(Long configId, Integer newValue) throws Exception;
}
