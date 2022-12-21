package com.lengao.auth.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-19
 */
@Getter
@Setter
  @TableName("bmg_menu")
@ApiModel(value = "BmgMenu对象", description = "菜单表")
public class BmgMenu implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("菜单编码")
      @TableId
      private String menuCode;

      @ApiModelProperty("菜单名称")
      private String menuName;

      @ApiModelProperty("菜单描述")
      private String menuDesc;

      @ApiModelProperty("菜单url")
      private String menuUrl;

      @ApiModelProperty("菜单类型")
      private Integer menuType;

      @ApiModelProperty("按钮urlId")
      private Integer buttonUrlId;

      @ApiModelProperty("父菜单编码")
      private String parentMenuCode;

      @ApiModelProperty("创建时间")
        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;

      @ApiModelProperty("更新时间")
        @TableField(fill = FieldFill.UPDATE)
      private LocalDateTime updateTime;

      @ApiModelProperty("图标")
      private String icon;

      @ApiModelProperty("排序")
      private Integer sortId;

      @ApiModelProperty("0 无效，1有效")
      private Boolean state;
}
