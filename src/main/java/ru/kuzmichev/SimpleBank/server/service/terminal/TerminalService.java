package ru.kuzmichev.SimpleBank.server.service.terminal;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.kuzmichev.SimpleBank.server.service.terminal.repository.TerminalEntity;
import ru.kuzmichev.SimpleBank.server.service.terminal.repository.TerminalRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.kuzmichev.SimpleBank.server.util.Converters.convert;

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

    @Transactional(readOnly = true)
    public List<Terminal> getAllByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return terminalRepository.findAll().stream()
                    .map(t -> convert(t))
                    .collect(Collectors.toList());
        }
        return terminalRepository.findAllById(ids).stream()
                .map(t -> convert(t))
                .collect(Collectors.toList());
    }
}
