package com.backend.warehouse_management.dto.admin;

import lombok.Data;

@Data
public class EditConfigRequest {
    private Long configId;
    private int newValue;
}
