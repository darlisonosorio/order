package br.com.darlison.order.common.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Builder
public class APIException {
    private String code;
    private String reason;
    private String message;
    @Builder.Default
    private Collection<APIException> errors = new ArrayList<>();

}
