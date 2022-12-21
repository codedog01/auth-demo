package com.lengao.auth.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-19
 */
@Getter
@Setter
  @TableName("bmg_role")
@ApiModel(value = "BmgRole对象", description = "角色表")
public class BmgRole implements Serializable {

    private static final long serialVersionUID = 1L;

      private Long id;

      @ApiModelProperty("角色编码")
      private String roleCode;

      @ApiModelProperty("角色名称")
      private String name;

      @ApiModelProperty("角色描述")
      private String decs;

      @ApiModelProperty("角色状态")
      private Boolean state;

      @ApiModelProperty("创建时间")
        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      @ApiModelProperty("修改时间")
        @TableField(fill = FieldFill.UPDATE)
      private LocalDateTime updateTime;
}
