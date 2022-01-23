package com.zengxianlong.eurekaprovider.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * TOOD
 *
 * @author long
 * @date 2022-01-23 16:29
 */
@Data
@Builder
public class User implements Serializable {
    private Integer id;
    private String name;


}
