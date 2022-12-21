package com.lengao.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lengao.auth.config.BusinessException;
import com.lengao.auth.mapper.*;
import com.lengao.auth.po.*;
import com.lengao.auth.service.IBmgUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lengao.auth.utils.JwtUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-18
 */
@Service
public class BmgUserServiceImpl extends ServiceImpl<BmgUserMapper, BmgUser> implements IBmgUserService {

    @Autowired
    BmgUserMapper userMapper;

    @Autowired
    BmgUserRoleMapper userRoleMapper;

    @Autowired
    BmgRoleMapper roleMapper;

    @Autowired
    BmgRoleUrlMapper roleUrlMapper;

    @Autowired
    BmgUrlMapper urlMapper;


    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    RedissonClient redissonClient;

    @Override
    public void login(BmgUser bmgUser, HttpServletResponse response) {

    }

    @Override
    public int register(BmgUser user) {
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        return userMapper.insert(user);
    }

    @Override
    public List<BmgRole> getRolesByUsername(String username) {
        ArrayList<BmgRole> bmgRoles = new ArrayList<>();
        BmgUser bmgUser = userMapper.selectOne(new LambdaQueryWrapper<BmgUser>().eq(BmgUser::getUsername, username));
        if (bmgUser == null) {
            return bmgRoles;
        }
        userRoleMapper.selectList(new LambdaQueryWrapper<BmgUserRole>().eq(BmgUserRole::getUserId, bmgUser.getId())).stream().map(BmgUserRole::getRoleId).collect(Collectors.collectingAndThen(Collectors.toSet(), roleIds -> {
            if (roleIds.isEmpty()) {
                return bmgRoles;
            }
            return roleMapper.selectList(new LambdaQueryWrapper<BmgRole>().in(BmgRole::getId, roleIds));

        }));
        return bmgRoles;
    }

    @Override
    public List<BmgUrl> getUrlsByUsername(String username) {
        ArrayList<BmgUrl> bmgUrls = new ArrayList<>();
        getRolesByUsername(username).stream().map(BmgRole::getId).collect(Collectors.collectingAndThen(Collectors.toSet(), roleIds -> {
            if (roleIds.isEmpty()) {
                return bmgUrls;
            }
            return roleUrlMapper.selectList(new LambdaQueryWrapper<BmgRoleUrl>().in(BmgRoleUrl::getRoleId, roleIds))
                    .stream()
                    .map(BmgRoleUrl::getUrlId).collect(Collectors.collectingAndThen(Collectors.toSet(), urlIds -> {
                        if (urlIds.isEmpty()) {
                            return bmgUrls;
                        }
                        return new HashSet<>(urlMapper.selectList(new LambdaQueryWrapper<BmgUrl>().in(BmgUrl::getId, urlIds)));
                    }));
        }));
        return bmgUrls;
    }

}
