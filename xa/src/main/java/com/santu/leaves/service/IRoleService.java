package com.santu.leaves.service;

import java.util.Set;

/**
 * @Author: LEAVES
 * @Date: 2020年10月27日 14时50分35秒
 * @Version 1.0
 * @Description:
 */
public interface IRoleService {

    Set<String> getPermissionByUserName(String username);

}
