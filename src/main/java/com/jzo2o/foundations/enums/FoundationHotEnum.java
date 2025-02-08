package com.jzo2o.foundations.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FoundationHotEnum {

    OFF_HOT(0,"非热门"),
    ON_HOT(1,"热门");
    private int hot;
    private String description;

    public boolean equals(Integer hot) {
        return this.hot == hot;
    }

    public boolean equals(FoundationHotEnum hotEnum) {
        return hotEnum != null && hotEnum.hot == this.getHot();
    }
}
