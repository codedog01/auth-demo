package com.lengao.auth.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lengao.auth.po.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-18
 */
@Mapper
public interface BmgUserMapper extends BaseMapper<BmgUser> {


}
