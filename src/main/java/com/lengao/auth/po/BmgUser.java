package com.lengao.auth.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-19
 */
@Getter
@Setter
@TableName("bmg_user")
@ApiModel(value = "BmgUser对象", description = "用户表")
public class BmgUser {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("code")
    private String userCode;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("姓别")
    private Boolean sex;

    @ApiModelProperty("生日")
    private LocalDate birthday;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("状态（0失效，1启用，2冻结）")
    private Integer state;

    @ApiModelProperty("登录次数")
    private Integer loginNum;

    @ApiModelProperty("最后登录时间")
    private LocalDate lastLoginTime;

    @ApiModelProperty("最后修改密码时间")
    private LocalDate lastChgPwdTime;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
