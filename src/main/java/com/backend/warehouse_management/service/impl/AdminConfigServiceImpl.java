package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.admin.ConfigDTO;
import com.backend.warehouse_management.dto.admin.EditConfigRequest;
import com.backend.warehouse_management.entity.AdminConfig;
import com.backend.warehouse_management.exception.NotFoundException;
import com.backend.warehouse_management.mapper.AdminConfigMapper;
import com.backend.warehouse_management.repository.ConfigRepository;
import com.backend.warehouse_management.service.AdminConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminConfigServiceImpl implements AdminConfigService {

    private final ConfigRepository configRepository;
    private final AdminConfigMapper adminConfigMapper;

    @Override
    public ConfigDTO adminAddConfig(ConfigDTO configDTO) throws RuntimeException {
        //if config does not exist, then create it
        if(!configRepository.existsByConfigName(configDTO.getConfigName())) {
            AdminConfig adminConfig = new AdminConfig();
            adminConfig.setConfigName(configDTO.getConfigName());
            adminConfig.setConfigValue(configDTO.getConfigValue());
            return adminConfigMapper.configToConfigDTO(
                    configRepository.save(adminConfig));
        }
        //if config exists, throw exception
        else {
            throw new RuntimeException("The config with this name: " + configDTO.getConfigName() + " already exists.");
        }
    }

    @Override
    public ConfigDTO adminChangeConfigValue(EditConfigRequest request) {
        //retrieve config
        Optional<AdminConfig> optionalConfig = configRepository.findById(request.getConfigId());
        //if config exists
        if(optionalConfig.isPresent()) {
            //edit the config value
            optionalConfig.get().setConfigValue(request.getNewValue());
            //save the new value
            return adminConfigMapper.configToConfigDTO(
                    configRepository.save(optionalConfig.get()));
        }
        //if config doesn't exist, throw exception
        else {
            throw new NotFoundException();
        }
    }
}
