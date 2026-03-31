package com.sso.common.mybatis.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.sso.common.core.constant.TenantConstants;
import com.sso.common.security.model.TenantContext;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 多租户行处理器
 */
@Slf4j
public class TenantLineHandlerImpl implements TenantLineHandler {

    /**
     * 忽略租户过滤的表
     */
    private final Set<String> ignoreTables = new HashSet<>();

    public TenantLineHandlerImpl() {
        ignoreTables.addAll(Arrays.asList(TenantConstants.IGNORE_TENANT_TABLES));
    }

    /**
     * 获取租户ID值
     */
    @Override
    public Expression getTenantId() {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            log.warn("租户上下文中无租户ID，使用默认值");
            return new NullValue();
        }
        return new LongValue(tenantId);
    }

    /**
     * 获取租户ID列名
     */
    @Override
    public String getTenantIdColumn() {
        return TenantConstants.TENANT_COLUMN;
    }

    /**
     * 判断是否忽略租户过滤
     */
    @Override
    public boolean ignoreTable(String tableName) {
        return ignoreTables.contains(tableName);
    }

    /**
     * 添加忽略表
     */
    public void addIgnoreTable(String tableName) {
        ignoreTables.add(tableName);
    }
}
