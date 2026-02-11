package org.erp.vnoptic.service;

import org.erp.vnoptic.dto.EmployeeDto;
import org.erp.vnoptic.responese.ConfigResponse;

public interface EmployeeService {
    ConfigResponse<EmployeeDto> getEmployees(Integer page, Integer size);
    EmployeeDto getEmployee(Integer id);
}
