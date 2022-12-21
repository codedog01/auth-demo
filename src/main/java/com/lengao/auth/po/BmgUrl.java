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
 * url表
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-19
 */
@Getter
@Setter
  @TableName("bmg_url")
@ApiModel(value = "BmgUrl对象", description = "url表")
public class BmgUrl implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("urlId")
        private Long id;

      @ApiModelProperty("url 路径")
      private String urlPath;

      @ApiModelProperty("1 普通url 2 AntPath ")
      private Integer urlType;

      @ApiModelProperty("url名称功能")
      private String urlName;

      @ApiModelProperty("url描述")
      private String urlDesc;

      @ApiModelProperty("0 无效，1有效")
      private Boolean state;

      @ApiModelProperty("0 不需要记录操作日志，1需要记录操作日志")
      private Boolean logFlag;

      @ApiModelProperty("创建时间")
        @TableField(fill = FieldFill.INSERT)
      private LocalDateTime createTime;
}
