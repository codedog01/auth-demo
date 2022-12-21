package com.lengao.auth.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色 url映射表
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-19
 */
@Getter
@Setter
  @TableName("bmg_role_url")
@ApiModel(value = "BmgRoleUrl对象", description = "角色 url映射表")
public class BmgRoleUrl implements Serializable {

    private static final long serialVersionUID = 1L;

      private Long id;

    private Long roleId;

    private Long urlId;
}
