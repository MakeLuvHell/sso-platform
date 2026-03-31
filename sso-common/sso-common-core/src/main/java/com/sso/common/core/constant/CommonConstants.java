package com.sso.common.core.constant;

/**
 * 公共常量
 */
public final class CommonConstants {

    private CommonConstants() {
    }

    /** 成功标记 */
    public static final int SUCCESS = 0;

    /** 失败标记 */
    public static final int FAIL = 1;

    /** UTF-8 编码 */
    public static final String UTF8 = "UTF-8";

    /** 请求头 - 用户ID */
    public static final String HEADER_USER_ID = "X-User-Id";

    /** 请求头 - 用户名 */
    public static final String HEADER_USERNAME = "X-Username";

    /** 请求头 - 租户ID */
    public static final String HEADER_TENANT_ID = "X-Tenant-Id";

    /** 请求头 - 角色信息 */
    public static final String HEADER_ROLES = "X-Roles";

    /** 默认租户ID */
    public static final Long DEFAULT_TENANT_ID = 0L;

    /** 分页默认页码 */
    public static final int DEFAULT_PAGE = 1;

    /** 分页默认每页条数 */
    public static final int DEFAULT_SIZE = 10;

    /** 最大每页条数 */
    public static final int MAX_SIZE = 500;
}
