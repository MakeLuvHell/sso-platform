package com.sso.admin.client;

import com.sso.common.core.result.Result;
import com.sso.rbac.dto.RoleDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RBAC 服务 Feign 客户端
 */
@FeignClient(name = "sso-rbac-service", contextId = "rbacFeignClient")
public interface RbacFeignClient {

    @GetMapping("/roles")
    Result<List<RoleDTO>> listRoles();

    @GetMapping("/roles/{id}")
    Result<RoleDTO> getRoleById(@PathVariable("id") Long id);

    @PostMapping("/roles")
    Result<RoleDTO> createRole(@RequestBody RoleDTO request);

    @PutMapping("/roles/{id}")
    Result<RoleDTO> updateRole(@PathVariable("id") Long id, @RequestBody RoleDTO request);

    @DeleteMapping("/roles/{id}")
    Result<Void> deleteRole(@PathVariable("id") Long id);
}
