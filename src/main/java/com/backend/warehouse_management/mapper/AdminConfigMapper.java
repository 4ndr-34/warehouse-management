package com.backend.warehouse_management.mapper;

import com.backend.warehouse_management.dto.admin.ConfigDTO;
import com.backend.warehouse_management.entity.AdminConfig;
import org.mapstruct.Mapper;

@Mapper
public interface AdminConfigMapper {

    AdminConfig configDTOToConfig(ConfigDTO configDTO);

    ConfigDTO configToConfigDTO(AdminConfig adminConfig);
}
