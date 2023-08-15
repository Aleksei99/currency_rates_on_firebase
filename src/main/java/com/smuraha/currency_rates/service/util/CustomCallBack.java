package com.smuraha.currency_rates.service.util;

import com.smuraha.currency_rates.service.enums.CallBackParams;
import lombok.*;

import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * Json объект должен быть не больше 64 байтов!
 */
public class CustomCallBack {

    private String id;

    private Integer order;

    private Map<CallBackParams, String> prms;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomCallBack that = (CustomCallBack) o;
        return Objects.equals(id, that.id) && Objects.equals(order, that.order) && Objects.equals(prms, that.prms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, prms);
    }
}
