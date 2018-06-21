package ru.kuzmichev.SimpleBank.server.service.terminal;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kuzmichev.SimpleBank.server.service.terminal.repository.TerminalEntity;
import ru.kuzmichev.SimpleBank.server.service.terminal.repository.TerminalRepository;

@Service
public class TerminalService {

    @Autowired
    private TerminalRepository terminalRepository;

    @Nullable
    @Transactional(readOnly = true)
    public Terminal getAvailableTerminalById(long id) {
        TerminalEntity terminalEntity = terminalRepository.getOne(id);
        if (terminalEntity == null || !terminalEntity.isEnable()) {
            return null;
        }
        return convert(terminalEntity);
    }

    @Nullable
    private Terminal convert(TerminalEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Terminal()
                .setId(entity.getId())
                .setAddress(entity.getAddress())
                .setAccount(entity.getAccount())
                .setCreatedDate(entity.getCreatedDate())
                .setEnable(entity.isEnable())
                .setType(entity.getType());
    }
}
