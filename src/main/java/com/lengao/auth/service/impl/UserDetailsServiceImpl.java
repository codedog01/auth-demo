package com.lengao.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lengao.auth.mapper.*;
import com.lengao.auth.po.*;
import com.lengao.auth.service.IBmgUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

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

    @Autowired
    IBmgUserService bmgUserService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<SimpleGrantedAuthority> collect = bmgUserService.getRolesByUsername(username).stream().map(bmgRole -> new SimpleGrantedAuthority(bmgRole.getRoleCode())).collect(Collectors.toSet());
        BmgUser bmgUser = userMapper.selectOne(new LambdaQueryWrapper<BmgUser>().eq(BmgUser::getUsername, username));
        if (bmgUser == null) {
            return null;
        }
        Set<SimpleGrantedAuthority> authorities = userRoleMapper.selectList(new LambdaQueryWrapper<BmgUserRole>()
                        .eq(BmgUserRole::getUserId, bmgUser.getId()))
                .stream()
                .map(BmgUserRole::getRoleId)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        roleIds ->
                        {
                            if (roleIds.isEmpty()) {
                                return new HashSet<>();
                            }
                            return roleMapper.selectList(new LambdaQueryWrapper<BmgRole>().in(BmgRole::getId, roleIds))
                                    .stream()
                                    .collect(Collectors.collectingAndThen(Collectors.toSet(),
                                            roles -> {
                                                if (roles.isEmpty()) {
                                                    return new HashSet<>();
                                                }
                                                return roleUrlMapper.selectList(new LambdaQueryWrapper<BmgRoleUrl>().in(BmgRoleUrl::getRoleId,
                                                                roles.stream().map(BmgRole::getId).collect(Collectors.toList())))
                                                        .stream()
                                                        .collect(Collectors.collectingAndThen(Collectors.toSet(),
                                                                roleUrls -> {
                                                                    if (roleUrls.isEmpty()) {
                                                                        return new HashSet<>();
                                                                    }
                                                                    return urlMapper.selectList(new LambdaQueryWrapper<BmgUrl>().in(BmgUrl::getId,
                                                                                    roleUrls.stream().map(BmgRoleUrl::getUrlId).collect(Collectors.toSet())))
                                                                            .stream()
                                                                            .map(url -> {
                                                                                Optional<BmgRole> targetRole = roles.stream().filter(role -> {
                                                                                            Optional<BmgRoleUrl> first = roleUrls
                                                                                                    .stream()
                                                                                                    .filter(roleUrl -> Objects.equals(roleUrl.getUrlId(), url.getId())).findFirst();
                                                                                            return first.filter(roleUrl -> Objects.equals(role.getId(), roleUrl.getRoleId())).isPresent();
                                                                                        })
                                                                                        .findFirst();
                                                                                String role;
                                                                                role = targetRole.map(bmgRole -> bmgRole.getRoleCode() + "::" + url.getUrlPath()).orElseGet(() -> "none::" + url.getUrlPath());
                                                                                return new SimpleGrantedAuthority(role);
                                                                            }).collect(Collectors.toSet());
                                                                }));
                                            }));
                        }));

        User user = new User(username, bmgUser.getPassword(), collect);
        return user;
    }
}

