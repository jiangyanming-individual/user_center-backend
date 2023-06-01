package com.jiang.user_center.model.request;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员删除用户
 */
@Data
public class UserDeleteRequest implements Serializable {

    private static final long serialVersionUID = -3375786825517853351L;
    private Long id;
}
