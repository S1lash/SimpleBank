package ru.kuzmichev.SimpleBank.web.api.util.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import ru.kuzmichev.SimpleBank.web.api.util.ResponseStatus;

import javax.xml.bind.annotation.XmlAccessorType;

@Data
@Accessors(chain = true)
@XmlAccessorType
public class ErrorResponse implements Response {

    private boolean error;
    @Nullable
    private String errorMessage;
    @Nullable
    private String cause;

    public ResponseStatus getStatus() {
        return ResponseStatus.FAIL;
    }
}
