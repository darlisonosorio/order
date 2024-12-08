package br.com.darlison.order.application.model.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record ClientResponse (String name, String email) {
}
