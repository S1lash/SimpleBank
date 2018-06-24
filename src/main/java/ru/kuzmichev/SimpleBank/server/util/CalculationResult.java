package ru.kuzmichev.SimpleBank.server.util;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@Accessors(chain = true)
public class CalculationResult {
    private boolean error;
    @Nullable
    private String errorMessage;
}
