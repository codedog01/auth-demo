package com.lengao.auth.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户 角色映射表
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-19
 */
@Getter
@Setter
  @TableName("bmg_user_role")
@ApiModel(value = "BmgUserRole对象", description = "用户 角色映射表")
public class BmgUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("用户角色表")
        private Long id;

      @ApiModelProperty("用户Id")
      private Long userId;

      @ApiModelProperty("角色Id")
      private Long roleId;
}
