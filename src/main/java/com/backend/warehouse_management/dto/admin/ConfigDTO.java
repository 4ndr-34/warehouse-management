package com.backend.warehouse_management.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDTO {

    private Long id;
    private String configName;
    private Integer configValue;
}
