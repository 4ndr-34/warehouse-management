package com.backend.warehouse_management.service;

import com.backend.warehouse_management.dto.admin.ConfigDTO;
import com.backend.warehouse_management.dto.admin.EditConfigRequest;

public interface AdminConfigService {

    ConfigDTO adminAddConfig(ConfigDTO configDTO) throws RuntimeException;

    ConfigDTO adminChangeConfigValue(EditConfigRequest editConfigRequest) throws RuntimeException;
}
