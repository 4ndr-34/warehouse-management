package com.backend.warehouse_management.service.impl;

import com.backend.warehouse_management.dto.admin.ConfigDTO;
import com.backend.warehouse_management.entity.AdminConfig;
import com.backend.warehouse_management.mapper.AdminConfigMapper;
import com.backend.warehouse_management.repository.ConfigRepository;
import com.backend.warehouse_management.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final ConfigRepository configRepository;
    private final AdminConfigMapper adminConfigMapper;

    @Override
    public ConfigDTO adminAddConfig(ConfigDTO configDTO) throws Exception {
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
            throw new Exception("The config with this name: " + configDTO.getConfigName() + " already exists.");
        }
    }

    @Override
    public ConfigDTO adminChangeConfigValue(Long configId, Integer newValue) throws Exception {
        //retrieve config
        Optional<AdminConfig> optionalConfig = configRepository.findById(configId);
        //if config exists
        if(optionalConfig.isPresent()) {
            //edit the config value
            optionalConfig.get().setConfigValue(newValue);
            //save the new value
            return adminConfigMapper.configToConfigDTO(
                    configRepository.save(optionalConfig.get()));
        }
        //if config doesn't exist, throw exception
        else {
            throw new Exception("Config with this ID: " + configId + " doesn't exist");
        }
    }


}
