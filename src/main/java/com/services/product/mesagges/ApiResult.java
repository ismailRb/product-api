package com.services.product.mesagges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResult<T> {
    private T data;
    private String error;

    public ApiResult(T data) {
        this.data = data;
        this.error = null;
    }

    public ApiResult(String error) {
        this.error = error;
        this.data = null;
    }
}
