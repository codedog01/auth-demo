package com.lengao.auth.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色 菜单映射表
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-19
 */
@Getter
@Setter
  @TableName("bmg_role_menu")
@ApiModel(value = "BmgRoleMenu对象", description = "角色 菜单映射表")
public class BmgRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long roleId;

      @ApiModelProperty("menu_code（id）")
      private String menuCode;

      @ApiModelProperty("1半选，0全选")
      private Boolean halfCheck;
}
