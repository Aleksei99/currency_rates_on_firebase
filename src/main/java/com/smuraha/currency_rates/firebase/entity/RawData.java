package com.smuraha.currency_rates.firebase.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawData {
    private Update event;
}
