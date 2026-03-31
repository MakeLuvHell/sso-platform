package com.sso.common.core.constant;

/**
 * 租户相关常量
 */
public final class TenantConstants {

    private TenantConstants() {
    }

    /** 租户ID请求头 */
    public static final String TENANT_HEADER = "X-Tenant-Id";

    /** 租户ID上下文键 */
    public static final String TENANT_CONTEXT_KEY = "TENANT_ID";

    /** 忽略租户的表 */
    public static final String[] IGNORE_TENANT_TABLES = {
            "sys_dict",
            "sys_config",
            "sys_log"
    };

    /** 租户状态 - 正常 */
    public static final int TENANT_STATUS_NORMAL = 0;

    /** 租户状态 - 禁用 */
    public static final int TENANT_STATUS_DISABLED = 1;

    /** 租户状态 - 过期 */
    public static final int TENANT_STATUS_EXPIRED = 2;

    /** 默认租户ID */
    public static final Long DEFAULT_TENANT_ID = 0L;

    /** 数据库租户ID列名 */
    public static final String TENANT_COLUMN = "tenant_id";
}
