package ru.kuzmichev.SimpleBank.web.api.util.dto.request;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
public class EntityRequest {
    @Nullable
    private List<Long> ids;
}
