package ru.kuzmichev.SimpleBank.web.api.util.dto.response;

import ru.kuzmichev.SimpleBank.web.api.util.ResponseStatus;

import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType
public abstract class SuccessfulResponse implements Response{
    public ResponseStatus getStatus() {
        return ResponseStatus.SUCCESS;
    }
}
