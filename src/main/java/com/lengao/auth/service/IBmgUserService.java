package com.lengao.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lengao.auth.po.BmgRole;
import com.lengao.auth.po.BmgUrl;
import com.lengao.auth.po.BmgUser;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 冷澳
 * @since 2022-12-18
 */
public interface IBmgUserService extends IService<BmgUser> {
    void login(BmgUser user, HttpServletResponse response);

    int register(BmgUser user);

    List<BmgRole> getRolesByUsername(String username);
    List<BmgUrl> getUrlsByUsername(String username);
}
