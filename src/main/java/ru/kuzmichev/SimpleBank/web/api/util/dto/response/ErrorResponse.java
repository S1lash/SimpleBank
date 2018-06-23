package ru.kuzmichev.SimpleBank.web.api.util.dto.response;

import org.jetbrains.annotations.Nullable;
import ru.kuzmichev.SimpleBank.web.api.util.ResponseStatus;

import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType
public class ErrorResponse implements Response {

    private boolean error;
    @Nullable
    protected String errorMessage;

    public ResponseStatus getStatus() {
        return ResponseStatus.FAIL;
    }
}
