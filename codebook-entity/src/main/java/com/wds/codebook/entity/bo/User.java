package com.wds.codebook.entity.bo;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author wds
 * @date 2022-12-29 14:22
 **/
@Data
public class User {
    private String name;
    private Integer age;
    private Integer sex;
}
